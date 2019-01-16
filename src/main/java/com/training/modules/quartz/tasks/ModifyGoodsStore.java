package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.ActivityService;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;

/**
 * 更新商品库存量
 * @author kele
 *
 */
@Component
public class ModifyGoodsStore extends CommonService{

	private Logger logger = Logger.getLogger(ModifyGoodsStore.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static GoodsService goodsService;
	private static ActivityService activityService;
	static{
		goodsService = (GoodsService) BeanUtil.getBean("goodsService");
		activityService = (ActivityService) BeanUtil.getBean("activityService");
	}
	
	/**
	 * 更新商品库存量
	 */
	public void modifyGoodsStore(){
		logger.info("[work0],start,更新商品库存量,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("modifyGoodsStore");
		taskLog.setStartDate(startDate);
		
		try {
			Set<String> l = redisClientTemplate.smembers(RedisConfig.GOODS_IDS_HASH);
			logger.info("[更新商品库存量]，更新商品数量："+l.size());
			for(String s : l){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goods_id", Integer.parseInt(s));
				if(null == redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+s) || "null".equals(redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+s)) || "".equals(redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+s))){
					System.out.println("########商品总库存异常-->> 商品ID："+Integer.parseInt(s)+",对应Redis key值："+RedisConfig.GOODS_STORECOUNT_PREFIX+Integer.parseInt(s));
				}
				int count = Integer.parseInt(redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+s));
				map.put("store_count", count);
				//logger.info("[更新库存量]，商品id："+s+"，数量："+count);
				if(count >= 0){
					goodsService.modifyStoreCount(map);
				}else {
					System.out.println("########商品总库存为负值-->> 商品ID："+Integer.parseInt(s)+",对应Redis key值："+RedisConfig.GOODS_STORECOUNT_PREFIX+Integer.parseInt(s));
				}
			}
			
			Set<String> lo = redisClientTemplate.smembers(RedisConfig.GOODS_SPECPRICE_HASH);
			logger.info("[更新商品规格库存量]，规格数量："+lo.size());
			for(String str : lo){
				Map<String, Object> map = new HashMap<String, Object>();
				String [] t = str.split("#");
				map.put("goods_id", t[0]);
				map.put("spec_key", t[1]);
				if(null == redisClientTemplate.get(RedisConfig.GOODS_SPECPRICE_PREFIX+str) || "null".equals(redisClientTemplate.get(RedisConfig.GOODS_SPECPRICE_PREFIX+str)) || "".equals(redisClientTemplate.get(RedisConfig.GOODS_SPECPRICE_PREFIX+str))){
					System.out.println("########商品规格库存异常-->> 商品ID："+t[0]+",规格key："+t[1]+",对应Redis key值："+RedisConfig.GOODS_SPECPRICE_PREFIX+str);
				}
				int count = Integer.parseInt(redisClientTemplate.get(RedisConfig.GOODS_SPECPRICE_PREFIX+str));
				//logger.info("[更新商品规格库存量]，商品id："+t[0]+"，规格key："+t[1]+",count："+count);
				map.put("store_count", count);
				if(count >= 0){
					goodsService.modifySpecStoreCount(map);
				}else {
					System.out.println("########商品规格库存为负值-->> 商品ID："+t[0]+",规格key："+t[1]+",对应Redis key值："+RedisConfig.GOODS_SPECPRICE_PREFIX+str);
				}
			}
			
			Set<String> ai = redisClientTemplate.hkeys(RedisConfig.coupon_amountids_hash);
			logger.info("[更新红包库存量]，红包数量："+ai.size());
			if(ai != null && ai.size() > 0){
				Iterator<String> iter = ai.iterator();
				while(iter.hasNext()){
					int id = Integer.parseInt(iter.next());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", id);
					int count = Integer.parseInt(redisClientTemplate.get(RedisConfig.coupon_store_prefix+id));
					map.put("count", count);
					
					activityService.modifyCouponNumber(map);
				}
			}
			
			taskLog.setJobDescription("[更新商品库存量]，更新商品数量："+l.size()+"  |  [更新商品规格库存量]，规格数量："+lo.size()+"  |  [更新红包库存量]，数量："+ai.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务modifyGoodsStore】更新商品库存量,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,更新商品库存量,结束时间："+df.format(new Date()));
	}
	/**
	 * 更新规格
	 * @param goods_id
	 * @param list
	 */
	public void delStore(int goods_id, List<String> list){
		logger.info("更新规格:goods_id:"+goods_id+",规格数："+list.size());
		if(list != null && list.size() > 0){
			redisClientTemplate.set(RedisConfig.GOODS_STORECOUNT_PREFIX+goods_id,"0");
			for(String key : list){
				logger.info("speckey -> "+goods_id+"#"+key);
				redisClientTemplate.del(RedisConfig.GOODS_SPECPRICE_PREFIX+goods_id+"#"+key);
				redisClientTemplate.srem(RedisConfig.GOODS_SPECPRICE_HASH, goods_id+"#"+key);
			}
		}
	}
	/**
	 * 添加规格
	 * @param goods_id
	 * @param list
	 */
	public void addStore(int goods_id,List<String> list){
		logger.info("添加规格:goods_id:"+goods_id+",规格数："+list.size());
		if(list != null && list.size() > 0){
			for(String key : list){
				redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+goods_id+"#"+key,"0");
				redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH, goods_id+"#"+key);
			}
		}
	}
}

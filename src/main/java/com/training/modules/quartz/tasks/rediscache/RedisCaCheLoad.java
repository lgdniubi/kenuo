package com.training.modules.quartz.tasks.rediscache;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.ec.utils.igtpush.exception.SysConstants;
import com.training.modules.ec.web.GoodsController;
import com.training.modules.quartz.entity.GoodsCollect;
import com.training.modules.quartz.entity.StoreVo;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.sys.entity.UserOfficeCode;
import com.training.modules.sys.entity.UserVo;
import com.training.modules.sys.service.SystemService;

/**
 * 每天美耶-后台-定时任务加载-缓存
 * @author kele
 * @version 2016年9月29日
 */
public class RedisCaCheLoad extends CommonService{

	private static final Log logger = LogFactory.getLog(RedisCaCheLoad.class);
	
	private static SystemService systemService;
	private static GoodsService goodsService;
	static{
		systemService = (SystemService) BeanUtil.getBean("systemService");
		goodsService = (GoodsService) BeanUtil.getBean("goodsService");
	}
	
	/**
	 * 定时器加载事件
	 */
	public void load() throws Exception{
		
		logger.info("#######[执行初始化]#######");
		logger.info("-->user_id-office_code");
		List<UserOfficeCode> uocs =  systemService.queryUserOfficeCodes();
		logger.info("-->user_id-office_id-size:"+uocs.size());
		for(UserOfficeCode uoc : uocs){
			//logger.info("user_id = "+uoc.getUserid()+", office_code = "+uoc.getOfficecode());
			redisClientTemplate.hset(RedisConfig.OFFICE_IDS_KEY, uoc.getUserid(), uoc.getOfficeid());
		}
		
		/*List<UserVo> vos = systemService.queryUserAll();
		logger.info("-->总排行榜-size:"+vos.size());
		for(int i=0;i<vos.size();i++){
			UserVo v = vos.get(i);
			if(v.getUsertype() != 2) continue;
			//logger.info("user_id："+v.getUserid()+"，total_core："+v.getTotlescore());
			String office_id = redisClientTemplate.hget(RedisConfig.OFFICE_IDS_KEY, v.getUserid());
			if(office_code.length()<9)continue;
			redisClientTemplate.sadd(RedisConfig.AREA_CODE_KEY, office_code.substring(0, 9));//区code集合
			redisClientTemplate.zadd(RedisConfig.SCORE_SORT_KEY+office_code.substring(0, 9), v.getTotlescore(), v.getUserid());//区总学分排行
			if(office_code.length()==21)
				redisClientTemplate.sadd(SysConstants.SHOP_CODE_KEY, office_code);
		}*/
		
		/*List<StoreVo> list = goodsService.queryStoreCount();
		logger.info("-->商品库存-"+list.size());
		for(StoreVo vo : list){
			logger.info("商品："+vo.getGoodsid()+",库存："+vo.getStorecount());
			redisClientTemplate.sadd(RedisConfig.GOODS_IDS_HASH, vo.getGoodsid()+"");
			redisClientTemplate.set(RedisConfig.GOODS_STORECOUNT_PREFIX+vo.getGoodsid(), vo.getStorecount()+"");
		}*/
		
		/*List<GoodsSpecPrice> l = goodsService.querySpecsPrices();
		logger.info("-->商品规格-"+l.size());
		for(GoodsSpecPrice sgp : l){
			logger.info("商品："+sgp.getGoodsId()+",规格key："+sgp.getSpecKey()+",key_value："+sgp.getSpecKeyValue()+"，规格库存："+sgp.getStoreCount());
			redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH,sgp.getGoodsId()+"#"+sgp.getSpecKey());
			redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+sgp.getGoodsId()+"#"+sgp.getSpecKey(), sgp.getStoreCount()+"");
		}*/
		
		List<GoodsCollect> lo = goodsService.queryAllGoodsCollect();
		logger.info("-->收藏商品-size:"+lo.size());
		for(GoodsCollect c : lo){
			//logger.info("user_id:"+c.getUserid()+", goods_id:"+c.getGoodsid());
			redisClientTemplate.hset(RedisConfig.GOODSCOLLECT_KEY, c.getUserid()+"#"+c.getGoodsid(), "true");
		}
		
		List<Integer> un = goodsService.queryAllUnshelve();
		logger.info("-->下架商品-size:"+un.size());
		for(int gid : un){
			logger.info("-->下架商品-goods_id:"+gid);
			redisClientTemplate.hset(GoodsController.GOOD_UNSHELVE_KEY, gid+"", "0");
		}
	}
}

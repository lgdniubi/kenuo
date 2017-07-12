package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.common.utils.ObjectUtils;
import com.training.modules.ec.dao.MtmyGoodsSubheadDao;
import com.training.modules.ec.service.MtmyGoodsSubheadService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.CommonService;

/**
 * 商品副标题活动到达开启时间和关闭时间时清除对应商品的缓存
 * @author xiaoye
 *
 */
@Component
public class MtmyGoodsSubheadCleanRedis extends CommonService{
	
	@Autowired
	private static MtmyGoodsSubheadService mtmyGoodsSubheadService;
	@Autowired
	private static MtmyGoodsSubheadDao mtmyGoodsSubheadDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	public static final String GOOD_DETAIL_KEY = "GOODS_DETAIL_"; // 商品详情app
	public static final String GOOD_DETAIL_WAP_KEY = "GOODS_DETAIL_WAP_"; // 商品详情wap
	public static final String GOODS_COMM_WAP_KEY = "GOODS_COMM_WAP_"; // 商品详情wap
	public static final String GOODS_RECOMMEND_KEY = "GOODS_RECOMMEND_"; // 商品详情wap
	public static final String GOODS_AD_WAP_KEY = "GOODS_AD_WAP_"; // 商品详情wap
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	
	static{
		mtmyGoodsSubheadService = (MtmyGoodsSubheadService) BeanUtil.getBean("mtmyGoodsSubheadService");
		mtmyGoodsSubheadDao = (MtmyGoodsSubheadDao) BeanUtil.getBean("mtmyGoodsSubheadDao");
	}
	
	
	/**
	 * 商品副标题活动到达开启时间和关闭时间时清除对应商品的缓存
	 */
	public void mtmyGoodsSubheadCleanRedis(){
		logger.info("[work0],start,商品副标题活动到达开启时间和关闭时间时清除对应商品的缓存,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("mtmyGoodsSubheadCleanRedis");
		taskLog.setStartDate(startDate);
		
		try {
			List<Integer> goodsSubheadIdsList = mtmyGoodsSubheadService.selectGoodsSubheadForTask();
			logger.info("[当前时间能清缓存的商品副标题活动数量]，数量："+goodsSubheadIdsList.size());
			if(goodsSubheadIdsList.size() > 0){
				for(Integer goodsSubheadId:goodsSubheadIdsList){
					logger.info("[当前商品副标题活动的ID为:]"+goodsSubheadIdsList.size());
					List<Integer> goodsIdslist = mtmyGoodsSubheadDao.selectGoodsId(goodsSubheadId);
					if(goodsIdslist.size() > 0){
						for(int goodsId:goodsIdslist){
							redisClientTemplate.del(ObjectUtils.serialize(GOOD_DETAIL_KEY + goodsId));
							redisClientTemplate.del(ObjectUtils.serialize(GOOD_DETAIL_WAP_KEY + goodsId));
							redisClientTemplate.del(ObjectUtils.serialize(GOODS_COMM_WAP_KEY + goodsId));
							redisClientTemplate.del(ObjectUtils.serialize(GOODS_RECOMMEND_KEY + goodsId));
							redisClientTemplate.del(ObjectUtils.serialize(GOODS_AD_WAP_KEY + goodsId));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("#####【定时任务mtmyGoodsSubheadCleanRedis】商品副标题活动到达开启时间和关闭时间时清除对应商品的缓存,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,商品副标题活动到达开启时间和关闭时间时清除对应商品的缓存,结束时间："+df.format(new Date()));
	}
}

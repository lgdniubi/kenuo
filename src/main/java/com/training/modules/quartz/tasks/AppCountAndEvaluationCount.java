/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月7日 上午11:47:12
 * 修改人:	
 * 修改时间:	2017年6月7日 上午11:47:12
 * 修改备注:	
 * @Version
 */
package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;
import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.service.BeautyService;
import com.training.modules.sys.service.ShopCountDataService;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称:	AppcountAndEvaluationcount
 * 类描述:	定时统计预约数和评论数以及评分
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月7日 上午11:47:12
 */
public class AppCountAndEvaluationCount extends CommonService {

	private Logger logger = Logger.getLogger(AppCountAndEvaluationCount.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static BeautyService beautyService;
	private static ShopCountDataService shopCountDataService;
	 
	static{
		beautyService = (BeautyService) BeanUtil.getBean("beautyService");
		shopCountDataService = (ShopCountDataService) BeanUtil.getBean("shopCountDataService");
	}
	public void appCountAndEvaluationCount(){
		logger.info("[work0],start,定时统计预约数、评论数以及评分,开始时间："+df.format(new Date()));
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("appCountAndEvaluationCount");
		taskLog.setStartDate(startDate);
		
		try {
			//查询技师的评论次数、评分和预约次数
			//更新 技师统计表数据
			Map<String, Object> map = beautyService.completeBeautyCountData();
			logger.info("[work],查询出的美容师的定位id是："+map.toString());
			//将查询的最新定位id存入数据库
			int a = QuartzStartConfigUtils.addQuartzStartConfig(map);
			logger.info("[work],美容师的定位id 插入数据库数量是："+a);
			
			//查询店铺的评论次数、评分和预约次数
			//更新 店铺统计表数据
			Map<String, Object> map2 = shopCountDataService.completeShopCountData();
			logger.info("[work],查询出的 店铺 的定位id是："+map2.toString());
			//将查询的最新定位id存入数据库
			int b = QuartzStartConfigUtils.addQuartzStartConfig(map2);
			logger.info("[work],查询出的 店铺的定位id 插入数据库数量是："+b);
			
			taskLog.setJobDescription("[work],：定时统计店铺、美容师的预约数、评论数以及评分  时间："+df.format(new Date()));
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,定时统计店铺、美容师的预约数、评论数以及评分,结束时间："+df.format(new Date()));
	}
}

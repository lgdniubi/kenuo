/**
 * 项目名称:	kenuo
 * 创建人:	土豆
 * 创建时间:	2017年6月19日 
 * 修改人:	
 * 修改时间:	2017年6月19日
 * 修改备注:	
 * @Version
 */
package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.ArticlesStatisticsCountData;
import com.training.modules.ec.service.ArticlesStatisticsService;
import com.training.modules.ec.service.GoodsStatisticsService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称: GoodsCountAndArticlesCount 
 * 类描述: 定时统计商品购买数和文章数以及评分 
 * 创建人: 土豆 
 * 创建时间:2017年6月19日
 * 
 */
@Component
public class GoodsCountAndArticlesCount extends CommonService {

	private Logger logger = Logger.getLogger(GoodsCountAndArticlesCount.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static GoodsStatisticsService goodsStatisticsService;
	private static ArticlesStatisticsService articlesStatisticsService;

	static {
		goodsStatisticsService = (GoodsStatisticsService) BeanUtil.getBean("goodsStatisticsService");
		articlesStatisticsService = (ArticlesStatisticsService) BeanUtil.getBean("articlesStatisticsService");
	}

	public void goodsCountAndArticlesCount() {
		logger.info("[work0],start,定时统计商品购买数、文章数以及评分,开始时间：" + df.format(new Date()));
		// 添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate; // 开始时间
		Date endDate; // 结束时间
		long runTime; // 运行时间

		startDate = new Date();
		taskLog.setJobName("goodsCountAndArticlesCount");
		taskLog.setStartDate(startDate);

		try {
			// 查询商品的统计数据:评分,评价数量,购买数量
			// 更新 商品统计表数据
			Map<String, Object> map = goodsStatisticsService.completeGoodsStatistics();
			logger.info("[work],查询出的商品的定位id是：" + map.toString());
			// 将查询的最新定位id存入数据库
			int a = QuartzStartConfigUtils.addQuartzStartConfig(map);
			logger.info("[work],商品的定位id 插入数据库数量是：" + a);

			// 查询文章的 浏览量  分享量 评论数  点赞数
			// 更新 文章统计表数据中的  浏览量
			/*ArticlesStatisticsCountData articlesStatisticsCountData = null;
			Map<String, String> map1 = redisClientTemplate.hgetAll(RedisConfig.COUNT_ARTICLE_DETAIL_NUM_KEY);
			Iterator<Map.Entry<String, String>> it = map1.entrySet().iterator();
			while (it.hasNext()) {
				articlesStatisticsCountData = new ArticlesStatisticsCountData();
				Map.Entry<String, String> entry = it.next();
				articlesStatisticsCountData.setArticlesId(Integer.parseInt(entry.getKey()));
				articlesStatisticsCountData.setLookCount(Integer.parseInt(entry.getValue()));
				//先获取文章的ID,浏览量 添加到数据库中
				articlesStatisticsService.addArticles(articlesStatisticsCountData);
			}
			//查询文章的  分享量
			Map<String, String> map2 = redisClientTemplate.hgetAll(RedisConfig.ARTICLE_SHARE_COUNT_KEY);
			Iterator<Map.Entry<String, String>> it1 = map2.entrySet().iterator();
			while (it1.hasNext()) {
				articlesStatisticsCountData = new ArticlesStatisticsCountData();
				Map.Entry<String, String> entry = it1.next();
				articlesStatisticsCountData.setArticlesId(Integer.parseInt(entry.getKey()));
				articlesStatisticsCountData.setShareCount(Integer.parseInt(entry.getValue()));
				//获取到文章的分享量,更新到  文章统计表数据中
				articlesStatisticsService.updateArticles(articlesStatisticsCountData);
			}
			
			//查询mtmy_articles_comment,查询出文章的  评论数  点赞数
			Map<String, Object> map3 = articlesStatisticsService.completeArticlesStatistics();
			logger.info("[work],查询出的 文章 的定位id是："+map3.toString());
			//将查询的最新定位id存入数据库 
			int b = QuartzStartConfigUtils.addQuartzStartConfig(map3);
			logger.info("[work],查询出的  文章 的定位id 插入数据库数量是："+b);*/
			 

			taskLog.setJobDescription("[work],：定时统计商品、文章评论数以及评分  时间：" + df.format(new Date()));
			taskLog.setStatus(0);// 任务状态
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endDate = new Date();// 结束时间
			runTime = (endDate.getTime() - startDate.getTime());// 运行时间
			taskLog.setEndDate(new Date()); // 结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,定时统计商品、文章的评论数以及评分,结束时间：" + df.format(new Date()));
	}
}

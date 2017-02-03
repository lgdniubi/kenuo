package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.MtmyArticleService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.service.ArticlesListService;

/**
 * 文章定时发布
 *
 */
@Component
public class ArticleTask extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static ArticlesListService articlesListService;
	private static MtmyArticleService mtmyArticleService;
	
	static{
		articlesListService = (ArticlesListService) BeanUtil.getBean("articlesListService");
		mtmyArticleService = (MtmyArticleService) BeanUtil.getBean("mtmyArticleService");
	}
	
	/**
	 * 文章定时发布
	 */
	public void articleTask(){
		logger.info("[work0],start,文章定时发布，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("articleTask");
		taskLog.setStartDate(startDate);
		
		try {
			int trainNum =  articlesListService.taskArticles();
			int mtmyNum = mtmyArticleService.taskArticles();
			taskLog.setJobDescription("[work],文章定时发布,妃子校发布个数："+trainNum+",每天美耶发布个数："+mtmyNum);
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务articleTask】文章定时发布,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,文章定时发布,结束时间："+df.format(new Date()));
	}
}

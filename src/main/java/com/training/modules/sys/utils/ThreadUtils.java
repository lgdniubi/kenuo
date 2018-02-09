package com.training.modules.sys.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.training.common.utils.SpringContextHolder;
import com.training.modules.ec.service.SaveLogService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.dao.BugLogDao;
import com.training.modules.sys.entity.SaveLog;

/**
 * 线程工具类
 *
 * @version 2018年1月19日
 */
public class ThreadUtils {
	
	@Autowired
	private static BugLogDao bugLogDao = SpringContextHolder.getBean(BugLogDao.class);
	@Autowired
	private static SaveLogService saveLogService = SpringContextHolder.getBean(SaveLogService.class);
	
	/**
	 * 线程工具类
	 * @param request
	 * @param title
	 * @param type		类型  1、接口调用 2、日志调用
	 * @param type1 	二级类型
	 * @param objects	可为多个Object
	 */
	public static void saveLog(HttpServletRequest request, String title,int type,int type1,Object... objects){
		
		switch (type){
			case 1:	// 接口掉用
				new PostObjectThread(request, title, type1, objects).start();
				break;
			case 2:	// 记录日志掉用
				new SaveLogThread(request, title, type1, objects).start();
				break;
			default: // 不存在类型
				System.out.println("不存在类型"); 
				break; 
		}
	}

	/**
	 * 调用接口线程
	 */
	public static class PostObjectThread extends Thread{
		
		private HttpServletRequest request;
		private String title;
		private int type;
		private String parpm;
		private String url;
		
		public PostObjectThread(HttpServletRequest request, String title,int type1,Object... objects){
			super(PostObjectThread.class.getSimpleName());
			this.request = request;
			this.title = title;
			this.type = type1;
			
			List<Object> listobj = new ArrayList<>();
			for (Object str : objects) {
				listobj.add(str);
			}
			
			this.parpm = (String)listobj.get(0);
			this.url = (String)listobj.get(1);
			
		}
		
		@Override
		public void run() {
			// 请求接口
			WebUtils.webUtilsPostObject(request,title,type,parpm,url);
		}
	}
	
	public static class SaveLogThread extends Thread{
		
		private HttpServletRequest request;
		private String title;
		private int type;
		private List<Object> obj;
		
		public SaveLogThread(HttpServletRequest request, String title,int type1,Object... objects){
			super(SaveLogThread.class.getSimpleName());
			
			this.request = request;
			this.title = title;
			this.type = type1;
			
			List<Object> listobj = new ArrayList<>();
			for (Object str : objects) {
				listobj.add(str);
			}
			this.obj = listobj;
		}
		
		@Override
		public void run() {
			boolean status = true;
			SaveLog saveLog = new SaveLog();	
			Date startDate;	//开始时间
			Date endDate;	//结束时间
			long runTime;	//运行时间
			
			startDate = new Date();
			
			try {
				
				switch (type){
				case 1:	// 订单日志
					saveLogService.ordersLog(request,title,obj);
					break;
				case 2:	// 预约日志
					saveLogService.saveApptOrderLog(request, title, obj);
					break;
				default: // 不存在类型
					System.out.println("不存在类型"); 
					break; 
				}
				
			} catch (Exception e) {
				status = false;
				saveLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			}finally {
				endDate = new Date();
			}
			if (!status) {	// 存在报错时
				runTime = (endDate.getTime() - startDate.getTime())/1000;//运行时间
				saveLog.setTitle(title);
				saveLog.setContent(obj.toString());
				saveLog.setStartDate(startDate);
				saveLog.setEndDate(endDate);
				saveLog.setRunTime(runTime);
				saveLog.setCreateBy(UserUtils.getUser());
				bugLogDao.insertSaveLog(saveLog);
			}
		}
	}

}

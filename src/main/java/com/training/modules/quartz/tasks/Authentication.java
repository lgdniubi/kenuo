package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.entity.AuthenticationBean;
import com.training.modules.train.service.AuthenticationService;

/**  
* <p>Title: CreateRefundOrder.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/
@Component
@SuppressWarnings("all")
public class Authentication extends CommonService{

	private Logger logger = Logger.getLogger(Authentication.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static AuthenticationService authenticationService;
	
	static{
		authenticationService = (AuthenticationService) BeanUtil.getBean("authenticationService");
	}
	
	/**
	 * 认证协议
	 * @author yangyang 
	 */
	public void authentication(){
	
		logger.info("[work0],start,认证协议，开始时间："+df.format(new Date()));
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("authentication");
		taskLog.setStartDate(startDate);
		
		try{
			
			//查询企业认证过期授权
			List<AuthenticationBean> list = authenticationService.querypastdueauthentication();
			for(AuthenticationBean s : list){
				//将认证授权状态改成已过期
				int count = authenticationService.updateauthenticationstatus(s.getId());
				if(s.getFranchisee_id() > 0 && count > 0){
					//将合同状态改成已失效
						authenticationService.updateprotocolstatus(s.getFranchisee_id());
					//修改pc菜单改为禁用
						authenticationService.updatepcmenustatus(s.getFranchisee_id());
					//获取该商家下的用户
						List<String> user = authenticationService.queryuserlist(s.getFranchisee_id());
						for(String user_id : user){
							redisClientTemplate.del("UTOKEN_"+user_id);
						}
				}else if(s.getFranchisee_id() == 0 && count > 0){
					redisClientTemplate.del("UTOKEN_"+s.getUser_id());
				}
				
			}
		
		
		}catch (Exception e) {
			logger.error("#####【定时任务authentication】认证协议,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,认证协议,结束时间："+df.format(new Date()));
		
	}
	
}

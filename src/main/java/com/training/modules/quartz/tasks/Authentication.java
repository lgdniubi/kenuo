package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.utils.PushUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.entity.AuthenticationBean;
import com.training.modules.train.entity.UserBean;
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
		PushUtils pushUtils = new PushUtils();
		
		try{
			
			//查询企业认证过期授权
			List<AuthenticationBean> list = authenticationService.querypastdueauthentication(1,0);
			Map<String, Object> map = new HashMap<String,Object>();
			for(AuthenticationBean s : list){
				//将认证授权状态改成已过期
				int count = authenticationService.updateauthenticationstatus(s.getId());
				
				if(count > 0){
					map.put("franchisee_id", s.getFranchisee_id());
					map.put("user_id", s.getUser_id());
					map.put("status2", 1);
					authenticationService.updatestatus(map);
					
					Map<String, Object> m = new HashMap<String,Object>();
					if(s.getFranchisee_id() > 0){
						String weburl = ParametersFactory.getTrainsParamValues("resign");
						//将合同状态改成已失效
							//authenticationMapper.updateprotocolstatus(map);
							String param = "{'franchisee_id':"+s.getFranchisee_id()+",'update_user':'1'}";
							WebUtils.postCSObject(param, weburl);
							
						//删除商家协议
						m.put("franchisee_id", s.getFranchisee_id());
						//推送
						if("qybz".equals(s.getMod_ename()))
							for(UserBean userbean : s.getUser_ids()){
								pushUtils.pushMsg(userbean.getUser_id(),"尊敬的"+userbean.getName()+"，您试用的妃子校"+s.getMod_name()+"已到期，请联系客服购买收费版，享受更多权益。",16,"体验到期");
							}
					}else if(s.getFranchisee_id() == 0){
						//删除用户协议
						m.put("user_id", s.getUser_id());
						//推送
						if("syrmf".equals(s.getMod_ename()))
							pushUtils.pushMsg(s.getUser_id(), "尊敬的"+s.getName()+"，您试用的妃子校"+s.getMod_name()+"已到期，请联系客服购买收费版，享受更多权益。",16,"体验到期");
					}
					authenticationService.delsupplyprotocol(m);
					
				}
				
			}
		
			try{
//				String day = ParametersFactory.getTrainsParamValues("queryContractInfo");
				int day = 2;
				List<AuthenticationBean> l = authenticationService.querypastdueauthentication(2,day);
				for(AuthenticationBean s : l){
					if(null == redisClientTemplate.get("franchisee"+s.getId())){
						if(!"qybz".equals(s.getMod_ename())){
							for(UserBean userbean : s.getUser_ids()){
								pushUtils.pushMsg(userbean.getUser_id(),"尊敬"+userbean.getName()+"，您的妃子校"+s.getMod_name()+"还有"+day+"天到期，续费有优惠。",16,"授权到期");
								redisClientTemplate.set("franchisee"+s.getId(), "0");
								redisClientTemplate.expireAt("franchisee"+s.getId(), getNowDateEndTime());
							}
						}
						if(!"syrmf".equals(s.getMod_ename())){
							pushUtils.pushMsg(s.getUser_id(), "尊敬的"+s.getName()+"，您的妃子校"+s.getMod_name()+"还有"+day+"天到期，续费有优惠。",16,"授权到期");
							redisClientTemplate.set("franchisee"+s.getId(), "0");
							redisClientTemplate.expireAt("franchisee"+s.getId(), getNowDateEndTime());
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
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
	
	private long getNowDateEndTime(){
		Calendar todayEnd = Calendar.getInstance();	//当天23:59:59:000
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 000);
		
        return todayEnd.getTime().getTime() / 1000;
	}
	
}

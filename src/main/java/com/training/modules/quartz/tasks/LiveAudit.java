package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.service.TrainLiveAuditService;

/**
 * 妃子校直播审核
 *
 */
@Component
public class LiveAudit extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static TrainLiveAuditService trainLiveAuditService;
	
	static{
		trainLiveAuditService = (TrainLiveAuditService) BeanUtil.getBean("trainLiveAuditService");
	}
	
	/**
	 * 妃子校直播审核
	 */
	public void liveAudit(){
		logger.info("[work0],start,妃子校直播处理，开始时间："+df.format(new Date()));
		HttpServletRequest request=null;
		List<TrainLiveAudit> list=new ArrayList<TrainLiveAudit>();
		List<TrainLiveAudit> wantlist=new ArrayList<TrainLiveAudit>();
		List<String> liveUsers=new ArrayList<String>();
		List<String> yuUser=new ArrayList<String>();
		List<String> pushList=new ArrayList<String>();
		int yueNum=0;
		int gouNum=0;
		int jiaNum=0;
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("trainLiveAudit");
		taskLog.setStartDate(startDate);
		
		try {
			
			list=  trainLiveAuditService.selectOutLive();					//查询过期的数据的 自动审核失败状态
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					TrainLiveAudit liveAudit=list.get(i);
					pushList.add(liveAudit.getUserId());
					trainLiveAuditService.updateLiveOut(liveAudit.getId());
				}
				trainLiveAuditService.pushMsg("0",pushList,new Date());				//直播过期没有审核通过推送提醒
			}
			
			pushList.clear();	//清空list
			wantlist=trainLiveAuditService.selectWantLive();
			if(wantlist.size()>0){									//直播将要开始  给主播提醒
				for (int i = 0; i < wantlist.size(); i++) {
					TrainLiveAudit liveAudit=wantlist.get(i);
					pushList.add(liveAudit.getUserId());
					jiaNum++;
				}
				trainLiveAuditService.pushMsg("8",pushList,new Date());	
			}
			
			if(wantlist.size()>0){
				for (int i = 0; i < wantlist.size(); i++) {
					TrainLiveAudit liveAudit=wantlist.get(i);
					if(liveAudit.getIsPay()==2){		//直播是付费直播  去查询购买了直播的用户推送
						liveUsers=trainLiveAuditService.selectLiveUser(liveAudit.getId());
						if(liveUsers.size()>0){
							trainLiveAuditService.pushMsg("9",liveUsers,new Date());	
							gouNum=gouNum+liveUsers.size();
							liveUsers.clear();
						}
					}else if(liveAudit.getIsPay()==1){	//直播免费的去查询有没有预约提醒的
						yuUser=trainLiveAuditService.selectWantLiveUser(liveAudit.getId());
						if(yuUser.size()>0){
							trainLiveAuditService.pushMsg("10",yuUser,new Date());
							yueNum=yueNum+yuUser.size();
							yuUser.clear(); 
						}
					}
				}
			}

			taskLog.setJobDescription("[work],妃子校直播审核过期个数："+list.size()+"直播将要开始的个数："+wantlist.size()
										+"将要直播的用户推送数："+jiaNum+",购买的直播个数："+gouNum+",预约直播个数："+yueNum);
			taskLog.setStatus(0);//任务状态

		} catch (Exception e) {
			logger.error("#####【定时任务trainLiveAudit】妃子校审核直播,出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "抢购活动定时器", e);
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,妃子校审核直播,结束时间："+df.format(new Date()));
	}
}

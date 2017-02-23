package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.entity.EntrySopCastBean;
import com.training.modules.train.entity.LiveRoomidAndAuditid;
import com.training.modules.train.entity.QueryccBack;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.rooms;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.TrainLiveAuditService;
import com.training.modules.train.utils.EncryptLiveUtils;

/**
 * 妃子校直播审核
 *	
 */
@Component
public class LiveAudit extends CommonService{

	private Logger logger = Logger.getLogger(LiveAudit.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static TrainLiveAuditService trainLiveAuditService;
	private static EntryService entryService;
	
	static{
		trainLiveAuditService = (TrainLiveAuditService) BeanUtil.getBean("trainLiveAuditService");
		entryService = (EntryService) BeanUtil.getBean("entryService");
	}
	
	/**
	 * 妃子校直播审核
	 * @author yangyang
	 */
	public void liveAudit(){
		logger.info("[work0],start,妃子校直播处理，开始时间："+df.format(new Date()));
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
			
			
			/**
			 * 1.扫面审核通过且没开直播的数据
			 * 2.扫面打开直播间没有正常关闭的数据
			 * 3.扫面直播的线下支付是否过期
			 * @author fengfeng
			 * @version 2017年2月23日
			 */
			logger.info("[直播定时任务任务]，操作过期的数据,开始时间：" + df.format(new Date()));
			
			//获取没开直播的过期规则
			int value = entryService.querylive_outTime("live_outTime");
			
			//获取没正常结束直播的过期规则
			int open_live_expiration_time = entryService.querylive_expiration_time("open_live_expiration_time");
			
			//将没开审核通过且没开直播的直播间作废
			entryService.upauditstatus(value);
			
			SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			//获取直播开启状态的所有数据
			List<LiveRoomidAndAuditid> noticelives = entryService.queryRoomidandAuditid();
			String roomids = "";
			for (int i = 0; i < noticelives.size(); i++) {
				roomids = roomids + noticelives.get(i).getRoomid() + ",";
			}

			List<rooms> recommendArr = null;
			if (roomids != "") {
				roomids = roomids.substring(0, roomids.length() - 1);
				
				//请求CC接口
				recommendArr = EncryptLiveUtils.querycc(roomids);
			}

			List<LiveRoomidAndAuditid> auditid = new ArrayList<LiveRoomidAndAuditid>();
			if (noticelives != null && recommendArr != null) {
				for (int h = 0; h < noticelives.size(); h++) {
					for (int f = 0; f < recommendArr.size(); f++) {
						if (noticelives.get(h).getRoomid().equals(recommendArr.get(f).getRoomId()) && recommendArr.get(f).getLiveStatus() == 0) {
							// auditid = auditid +
							// noticelives.get(h).getAuditid() + ",";
							
							//获取直播间处于关闭状态的直播
							auditid.add(noticelives.get(h));
						}
					}
				}
			}
			for (int i = 0; i < auditid.size(); i++) {
				//查看超时的数据
				int count = entryService.queryCount(open_live_expiration_time,auditid.get(i).getAuditid());
				if (count == 1) {
					String endtime = smp.format(new Date());
					String roomid = auditid.get(i).getRoomid();
					String begintime = auditid.get(i).getBengtime();
					
					//请求CC接口，有回放就插到回放表，状态为3（已完成），没有回放就直接作废，状态为5（作废）
					Object object = EncryptLiveUtils.queryccback(roomid,begintime, endtime);
					int auditids = auditid.get(i).getAuditid();
					if (object.equals("FAIL")) {
						
						// 没有回放的数据直接作废（状态5）
						entryService.uplivestatus(open_live_expiration_time,auditid.get(i).getAuditid());
					} else {
						
						//获取申请直播信息数据
						EntrySopCastBean entrysopcastbean = entryService.getliveendbyid(auditids);
						List<QueryccBack> lists = (List<QueryccBack>) object;
						Map<String, Object> m = new HashMap<String, Object>();
						for (int j = 0; j < lists.size(); j++) {
							m.put("playbackId", lists.get(j).getId());
							m.put("bengtime", lists.get(j).getStartTime());
							if (j == 0) {
								m.put("overtime", endtime);
							} else {
								m.put("overtime", lists.get(j).getEndTime());
							}
							if (entrysopcastbean != null) {
								m.put("auditId", entrysopcastbean.getAuditId());
								m.put("name", entrysopcastbean.getTitle());
								m.put("desc", entrysopcastbean.getDesc());
								m.put("imgurl", entrysopcastbean.getImgurl());
								m.put("playpass",
										entrysopcastbean.getPlaypass());
							}
							// 将回放的数据插到回放表
							entryService.LiveEnd(m);

						}
						//将数据的状态改为3（已完成）
						entryService.alterstatus(auditids);
						// entryService.liveendtime(auditids);
					}
				}
			}
			//扫面线下支付表，将过期的数据作废
			entryService.operatebackrecord();

			
		} catch (Exception e) {
			logger.error("#####【定时任务trainLiveAudit】妃子校审核直播,出现异常，异常信息为："+e.getMessage());
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

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
import com.training.modules.train.entity.rooms;
import com.training.modules.train.service.BeautyWeekScoreService;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.ShopWeekScoreService;
import com.training.modules.train.utils.EncryptLiveUtils;

@SuppressWarnings("all")
@Component
public class Detectionliveapply extends CommonService{

	private Logger logger = Logger.getLogger(Detectionliveapply.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static EntryService entryService;
	
	static{
		entryService = (EntryService) BeanUtil.getBean("entryService");
	}


	public void detectionliveapply() throws Exception{
		
		logger.info("[鐩存挱瀹氭椂浠诲姟浠诲姟]锛屾搷浣滆繃鏈熺殑鏁版嵁,寮�濮嬫椂闂达細"+df.format(new Date()));
		
		//娣诲姞鏃ュ織
		TaskLog taskLog = new TaskLog();
		Date startDate;	//寮�濮嬫椂闂�
		Date endDate;	//缁撴潫鏃堕棿
		long runTime;	//杩愯鏃堕棿
		
		startDate = new Date();
		taskLog.setJobName("detectionliveapply");
		taskLog.setStartDate(startDate);
		SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
		int value = entryService.querylive_outTime("live_outTime");
		int open_live_expiration_time = entryService.querylive_expiration_time("open_live_expiration_time");
		
		entryService.upauditstatus(value);
		List<LiveRoomidAndAuditid> noticelives = entryService.queryRoomidandAuditid();
		String roomids = "";
		for (int i = 0; i < noticelives.size(); i++) {
			roomids = roomids + noticelives.get(i).getRoomid()+",";
		}
		
		List<rooms> recommendArr = null;
		if (roomids != "") {
			roomids = roomids.substring(0, roomids.length() - 1);

			recommendArr = EncryptLiveUtils.querycc(roomids);
		}
		
		List<LiveRoomidAndAuditid> auditid = new ArrayList<LiveRoomidAndAuditid>();
		if (noticelives != null && recommendArr != null) {
			for (int h = 0; h < noticelives.size(); h++) {
				for (int f = 0; f < recommendArr.size(); f++) {
					if (noticelives.get(h).getRoomid().equals(recommendArr.get(f).getRoomId()) && recommendArr.get(f).getLiveStatus() == 0) {
						//auditid = auditid + noticelives.get(h).getAuditid() + ",";
						auditid.add(noticelives.get(h));
					}
				}
			}
		}
		for (int i = 0; i < auditid.size(); i++) {
			//鏌ヨ杩囨湡鏁版嵁
			int count = entryService.queryCount(open_live_expiration_time,auditid.get(i).getAuditid());
			if(count == 1){
			String endtime = smp.format(new Date());
			String roomid = auditid.get(i).getRoomid();
			String begintime = auditid.get(i).getBengtime();
			Object object = EncryptLiveUtils.queryccback(roomid,begintime,endtime);
			int auditids = auditid.get(i).getAuditid();
			if(object.equals("FAIL")){
			entryService.uplivestatus(open_live_expiration_time,auditid.get(i).getAuditid());
			}else{
				EntrySopCastBean entrysopcastbean = entryService.getliveendbyid(auditids);
				List<QueryccBack> list = (List<QueryccBack>) object;
				Map<String,Object> m = new HashMap<String,Object>();
				for(int j=0;j<list.size();j++){
						m.put("playbackId", list.get(j).getId());
						m.put("bengtime",list.get(j).getStartTime());
						if(j == 0){
							m.put("overtime",endtime);
						}else{
							m.put("overtime", list.get(j).getEndTime());
						}
						if (entrysopcastbean != null) {
							m.put("auditId", entrysopcastbean.getAuditId());
							m.put("name", entrysopcastbean.getTitle());
							m.put("desc", entrysopcastbean.getDesc());
							m.put("imgurl", entrysopcastbean.getImgurl());
							m.put("playpass", entrysopcastbean.getPlaypass());
				}
						entryService.LiveEnd(m);
						
			}
				entryService.alterstatus(auditids);
				entryService.liveendtime(auditids);
				}
			}
		}
		entryService.operatebackrecord();
	
	} catch (Exception e) {
		logger.error("#####銆愬畾鏃朵换鍔etectionliveapply銆戞搷浣滆繃鏈熺殑鏁版嵁,鍑虹幇寮傚父锛屽紓甯镐俊鎭负锛�"+e.getMessage());
		taskLog.setStatus(1);
		taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
	}finally {
		endDate = new Date();//缁撴潫鏃堕棿
		runTime = (endDate.getTime() - startDate.getTime());//杩愯鏃堕棿
		taskLog.setEndDate(new Date());	//缁撴潫鏃堕棿
		taskLog.setRunTime(runTime);
		taskService.saveTaskLog(taskLog);
	}
	
	logger.info("[work0],end,[鐩存挱瀹氭椂浠诲姟浠诲姟]锛屾搷浣滆繃鏈熺殑鏁版嵁,缁撴潫鏃堕棿锛�"+df.format(new Date()));
	
	}
}

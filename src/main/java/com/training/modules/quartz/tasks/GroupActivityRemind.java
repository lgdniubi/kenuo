package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gexin.rp.sdk.base.impl.Target;
import com.training.common.mapper.JsonMapper;
import com.training.common.utils.BeanUtil;
import com.training.common.utils.IdGen;
import com.training.common.utils.ListSplitUtils;
import com.training.modules.ec.dao.MtmyGroupActivityDao;
import com.training.modules.ec.entity.MtmyGroupActivity;
import com.training.modules.ec.entity.MtmyGroupActivityRemind;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.ThreadUtils;
import com.training.modules.train.entity.Subscribe;
import com.training.modules.train.service.EntryService;

import net.sf.json.JSONObject;

/**
 * 团购项目推送
 * @author coffee
 * @date 2018年4月12日
 */
@Component
@SuppressWarnings("all")
public class GroupActivityRemind extends CommonService{

	private Logger logger = Logger.getLogger(GroupActivityRemind.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static MtmyGroupActivityDao mtmyGroupActivityDao;
	
	static{
		mtmyGroupActivityDao = (MtmyGroupActivityDao) BeanUtil.getBean("mtmyGroupActivityDao");
	}
	
	public void groupActivityRemind(){
		logger.info("[work0],start,团购项目推送定时器开始执行，开始时间："+df.format(new Date()));

		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("GroupActivityRemind");
		taskLog.setStartDate(startDate);
		
		try{
			String message = "";
			List<MtmyGroupActivity> list = mtmyGroupActivityDao.findGroupActivity();	// 查询5分钟内的活动
			for (int i = 0; i < list.size(); i++) {
				List<MtmyGroupActivityRemind> list2 = mtmyGroupActivityDao.findGroupActivityRemind(list.get(i).getId());	// 查询提醒用户
				List<Integer> ids = new ArrayList<>();							// 提醒id 
				Map<String, List<String>> mapCid = new HashMap<String, List<String>>();		// 个推推送
				Map<String, List<String>> mapMobile = new HashMap<String, List<String>>();	// 短信推送
				
				for (int j = 0; j < list2.size(); j++) {
					ids.add(list2.get(j).getrId());
					String key = list2.get(j).getGoodsName();	// map key值，商品名称，避免商品名称变化导致数据不一致
					String cid = list2.get(j).getClientId();
					String mobile = list2.get(j).getMobile();
					
					if(cid != null && !"".equals(cid)){ // 存在cid,使用个推
						boolean a = mapCid.containsKey(key);
						if(a){
							List<String> listCid = mapCid.get(key);
							listCid.add(cid);
							mapCid.put(key, listCid);
						}else{
							List<String> listCid = new ArrayList<>();
							listCid.add(cid);
							mapCid.put(key, listCid);
						}
						
					}else{
						// 无cid使用短信推送
						boolean a = mapMobile.containsKey(key);
						if(a){
							List<String> listMobile = mapMobile.get(key);
							listMobile.add(mobile);
							mapMobile.put(key, listMobile);
						}else{
							List<String> listMobile = new ArrayList<>();
							listMobile.add(mobile);
							mapMobile.put(key, listMobile);
						}
					}
				}
				// 调用接口、短信 并修改推送信息
				push(mapCid,mapMobile,ids);
				message = message + "推送活动id"+list.get(i).getId()+",";
			}
			taskLog.setJobDescription("团购项目推送：");
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务groupActivityRemind】团购项目推送定时器,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,团购项目推送定时器结束,结束时间："+df.format(new Date()));
	}

	public static void push(Map<String, List<String>> mapCid,Map<String, List<String>> mapMobile,List<Integer> ids){
		if(ids.size() > 0){
			// 个推推送
			for (String string : mapCid.keySet()) {
				Map<String, Object> messageMap = new HashMap<String, Object>();
				Map<String, Object> putMap = new HashMap<String, Object>();
				// 有cid使用个推推送
				messageMap.put("title","团购");
				messageMap.put("notify_type", "0");
				messageMap.put("push_type",3);
				messageMap.put("push_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				messageMap.put("content",string+"即将开团，先到先得，快点进来占位置！");
				messageMap.put("notify_id", IdGen.uuid());
				
				putMap.put("content", messageMap);
				putMap.put("cid_list", mapCid.get(string));
				
				try {
					GetTUtil.list(JSONObject.fromObject(JsonMapper.toJsonString(putMap)));
				} catch (ResponseError e) {
					
				}
			}
			for (String string : mapMobile.keySet()) {
				String weburl = ParametersFactory.getMtmyParamValues("sendAdvanceNoticeToMobiles");
				System.out.println("##### web接口路径:"+weburl);
				
				List<Object> list = ListSplitUtils.listSplit("团购短信推送",mapMobile.get(string),500);
		        for (int i = 0; i < list.size(); i++) {
		        	@SuppressWarnings("unchecked")
					List<String> subList = (List<String>) list.get(i);
		        	
		        	if(!"-1".equals(weburl)){
		        		String parpm = "{\"mobiles\":\""+String.join(",", subList)+"\",\"goods_name\":\""+string+"\"}";
		        		String url=weburl;
		        		String result = WebUtils.postObject(parpm, url);
		        		JSONObject jsonObject = JSONObject.fromObject(result);
		        		System.out.println("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg"));
		        		if("200".equals(jsonObject.get("code"))){
		        			
		        		}else{
		        			
		        		}
		        	}
				}
			}
			mtmyGroupActivityDao.updateGroupActivityRemind(ids);
		}
	}
	
}

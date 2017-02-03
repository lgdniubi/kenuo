package com.training.modules.train.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.oa.dao.OaNotifyDao;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.dao.TrainLiveAuditDao;
import com.training.modules.train.dao.TrainLivePlaybackDao;
import com.training.modules.train.dao.TrainLiveUserDao;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLivePlayback;

import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = false)
public class TrainLiveAuditService  extends CrudService<TrainLiveAuditDao,TrainLiveAudit>{
	
	@Autowired
	private TrainLiveAuditDao trainLiveAuditDao;
	@Autowired
	private TrainLivePlaybackDao trainLivePlaybackDao;
	@Autowired
	private OaNotifyDao oaNotifyDao;
	@Autowired
	private TrainLiveUserDao trainLiveUserDao;
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLiveAudit> findLive(Page<TrainLiveAudit> page, TrainLiveAudit trainLiveAudit) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLiveAudit.setPage(page);
		// 执行分页查询
		page.setList(trainLiveAuditDao.findList(trainLiveAudit));
		return page;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLivePlayback> findback(Page<TrainLivePlayback> page, TrainLivePlayback trainLivePlayback) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLivePlayback.setPage(page);
		// 执行分页查询
		page.setList(trainLivePlaybackDao.findList(trainLivePlayback));
		return page;
	}
	
	
	/**
	 * 保存数据
	 * @param trainLiveAudit
	 * @return
	 */
	public int update(TrainLiveAudit trainLiveAudit){
		return trainLiveAuditDao.update(trainLiveAudit);
	}
	/**
	 * 直播审核
	 * @return
	 */
	public List<TrainLiveAudit> selectOutLive(){
		return dao.selectOutLive();
	}
	/**
	 * 修改过期数据
	 * @param id
	 * @return
	 */
	public int updateLiveOut(String id){
		return dao.updateLiveOut(id);
	}
	
	/**
	 * 查询将要直播的数据
	 * @return
	 */
	public List<TrainLiveAudit> selectWantLive(){
		return dao.selectWantLive();
	}
	
	/**
	 * 查询购买的数据
	 * @return
	 */
	public List<String> selectLiveUser(String auditId){
		return trainLiveUserDao.selectLiveUser(auditId);
	}
	/**
	 * 查询预约的用户
	 * @param auditId
	 * @return
	 */
	public List<String> selectWantLiveUser(String auditId){
		return trainLiveUserDao.selectWantLiveUser(auditId);
	}
	
	/**
	 * 推送
	 * @param trainLiveAudit
	 */
	public  void pushMsg(String Status,List<String> list,Date begTime){

		// 统一全部用列推
		JSONObject jsonObject = new JSONObject();
		String push_url = ParametersFactory.getMtmyParamValues("push_url");
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateStr=sdf.format(date);
		logger.info("##### 直播推送 状态："+Status);
		String param="{";
		String push_type="1";		//1 是列表推送
		String notify_id=" ";
		String title="";
		String notify_type="";
		String content="";
		
		if("0".equals(Status)){				//直播审核失败
			notify_id=UUID.randomUUID().toString();
			String cid="[";
			title="直播审核失败";
			notify_type="0";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您申请的直播审核失败，请重新申请！";
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
				}
				cid=cid.substring(0, cid.length()-1);
			}
			
			cid=cid+"]";
			param=param+"\"cid_list\":"+cid+"," ;
			param=param+"\"push_type\":"+push_type+",";
			param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
					+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
			System.out.println(param);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("2".equals(Status)){		//审核通过消息推送
			notify_id=UUID.randomUUID().toString();
			String cid="[";
			title="直播审核通过";
			notify_type="0";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您申请的直播已经通过，请准时直播！";
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
				}
				cid=cid.substring(0, cid.length()-1);
			}
			
			cid=cid+"]";
			param=param+"\"cid_list\":"+cid+"," ;
			param=param+"\"push_type\":"+push_type+",";
			param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
					+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
			System.out.println(param);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("8".equals(Status)){			// 8 直播将要开始 主播
			notify_id=UUID.randomUUID().toString();
			String cid="[";
			title="直播将要开始";
			notify_type="8";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您的直播将要在5分钟内开始，请准时直播！";
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
				}
				cid=cid.substring(0, cid.length()-1);
			}
			
			cid=cid+"]";
			param=param+"\"cid_list\":"+cid+"," ;
			param=param+"\"push_type\":"+push_type+",";
			param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
					+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
			System.out.println(param);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("9".equals(Status)){			//  9 直播将要开始  购买者
			notify_id=UUID.randomUUID().toString();
			String cid="[";
			title="购买直播将要开始";
			notify_type="9";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您购买的直播将要在5分钟内开始，请准时观看！";
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
				}
				cid=cid.substring(0, cid.length()-1);
			}
			
			cid=cid+"]";
			param=param+"\"cid_list\":"+cid+"," ;
			param=param+"\"push_type\":"+push_type+",";
			param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
					+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
			System.out.println(param);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("10".equals(Status)){				//9 预约直播提醒  预约
			notify_id=UUID.randomUUID().toString();
			String cid="[";
			title="预约直播将要开始";
			notify_type="9";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您预约的直播将要在5分钟内开始，请准时观看！";
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
				}
				cid=cid.substring(0, cid.length()-1);
			}
			
			cid=cid+"]";
			param=param+"\"cid_list\":"+cid+"," ;
			param=param+"\"push_type\":"+push_type+",";
			param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
					+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
			System.err.println(param);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}

		
	}
	
}

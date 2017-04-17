/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.ec.dao.MtmyOaNotifyDao;
import com.training.modules.ec.dao.MtmyOaNotifyRecordDao;
import com.training.modules.ec.entity.MtmyOaNotify;
import com.training.modules.ec.entity.MtmyOaNotifyRecord;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.ec.utils.igtpush.exception.SysConstants;

import net.sf.json.JSONObject;

/**
 * 每天每夜通知通告Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = false)
public class MtmyOaNotifyService extends CrudService<MtmyOaNotifyDao, MtmyOaNotify> {

	@Autowired
	private MtmyOaNotifyRecordDao mtmyOaNotifyRecordDao;

	public MtmyOaNotify get(String id) {
		MtmyOaNotify entity = dao.get(id);
		return entity;
	}
	
	@Transactional(readOnly = false)
	public void save(MtmyOaNotify mtmyOaNotify) {
		super.save(mtmyOaNotify);
		
		// 更新发送接受人记录
		mtmyOaNotifyRecordDao.deleteByOaNotifyId(mtmyOaNotify.getId());
		if (mtmyOaNotify.getMtmyOaNotifyRecordList().size() > 0){
			mtmyOaNotifyRecordDao.insertAll(mtmyOaNotify.getMtmyOaNotifyRecordList());
		}
	}
	/**
	 * 分页查询
	 * @param page
	 * @param mtmyOaNotify
	 * @return
	 */
	public Page<MtmyOaNotify> find(Page<MtmyOaNotify> page, MtmyOaNotify mtmyOaNotify) {
		mtmyOaNotify.setPage(page);
		page.setList(dao.findList(mtmyOaNotify));
		return page;
	}
	
	/**
	 * 获取通知发送记录
	 * @param oaNotify
	 * @return
	 */
	public MtmyOaNotify getRecordList(MtmyOaNotify mtmyOaNotify) {
		mtmyOaNotify.setMtmyOaNotifyRecordList(mtmyOaNotifyRecordDao.findList(new MtmyOaNotifyRecord(mtmyOaNotify)));
		return mtmyOaNotify;
	}
	
	/**
	 * 回写列推用户
	 * @param mtmyOaNotify
	 * @return
	 */
	public List<MtmyOaNotifyRecord> getUsers(MtmyOaNotify mtmyOaNotify){
		List<MtmyOaNotifyRecord> list = mtmyOaNotifyRecordDao.findList(new MtmyOaNotifyRecord(mtmyOaNotify));
		return list;
	}
	
	/**
	 * 推送
	 * @param notify_id
	 * @param push_type
	 * @return
	 * @throws ResponseError 
	 */
	public String pushMsg(String notify_id,Integer push_type) throws ResponseError{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = this.dao.findMapById(notify_id);
		
		formatDate(m);
		
		map.put("content", m);
		map.put("push_type", push_type);
		
		if(push_type == 1 || push_type == 2){
			map.put("cid_list", this.dao.selectCids(notify_id));
			return this.push(JsonMapper.toJsonString(map));
		}else if(push_type == 0){
			JSONObject jsStr = JSONObject.fromObject(map); 
			String resutl = GetTUtil.crowd(jsStr);
			return resutl;
		}else{
			//推送类型有误
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.ERROR_CONTENT_MESSAGE);
			json.put("message", SysConstants.ERROR_CONTENT_RESULT);
			return json.toString();
		}
	}
	
	/**
	 * 格式化时间
	 * @param map
	 */
	private void formatDate(Map<String, Object> map){
		String start = "";
		String end = "";
		if(map.get("start_time") != null)
			start = ((Timestamp) map.get("start_time")).toString();
		if(map.get("end_time") != null)
			end = ((Timestamp) map.get("end_time")).toString();
		if(StringUtils.isNotBlank(start))
			map.put("start_time", start.substring(0, start.lastIndexOf(".")));
		if(StringUtils.isNotBlank(end))
			map.put("end_time", end.substring(0, end.lastIndexOf(".")));
		map.put("push_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
	/**
	 * 调用推送接口
	 * @param jsonObj
	 * @return
	 * @throws ResponseError 
	 */
	private String push(String jsonObj) throws ResponseError{
		JSONObject jsStr = JSONObject.fromObject(jsonObj); 
		String resutl = GetTUtil.list(jsStr);
		return resutl;
	}
	
	@Transactional(readOnly = false)
	public void updateStatus(String notify,Integer status){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notify_id", notify);
		map.put("status", status);
		this.dao.updateStatus(map);
	}
	
	/**
	 * 保存任务id以便后续获取推送结果
	 * @param map
	 */
	public void updateNotifyContentId(Map<String, Object> map){
		this.mtmyOaNotifyRecordDao.updateNotifyContentId(map);
	}
	
	/**
	 * 修改推送结果
	 * @param map
	 */
	public void updatePushResult(Map<String, Object> map){
		this.mtmyOaNotifyRecordDao.updatePushResult(map);
	}
	
	/**
	 *  根据用户user_id查询用户是否有client
	 * @param userId
	 * @return
	 */
	public int selectClient(int userId){
		return mtmyOaNotifyRecordDao.selectClient(userId);
	}
}
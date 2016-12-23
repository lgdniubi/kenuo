/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.oa.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.oa.dao.OaNotifyDao;
import com.training.modules.oa.dao.OaNotifyRecordDao;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.oa.entity.OaNotifyRecord;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.dao.TrainCategorysDao;
import com.training.modules.train.dao.TrainLessonsDao;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainLessons;

/**
 * 通知通告Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OaNotifyService extends CrudService<OaNotifyDao, OaNotify> {

	@Autowired
	private OaNotifyRecordDao oaNotifyRecordDao;

	public OaNotify get(String id) {
		OaNotify entity = dao.get(id);
		return entity;
	}
	
	/**
	 * 获取通知发送记录
	 * @param oaNotify
	 * @return
	 */
	public OaNotify getRecordList(OaNotify oaNotify) {
		oaNotify.setOaNotifyRecordList(oaNotifyRecordDao.findList(new OaNotifyRecord(oaNotify)));
		return oaNotify;
	}
	/**
	 * 回写列推用户
	 * @param oaNotify
	 * @return
	 */
	public List<OaNotifyRecord> getUser(OaNotify oaNotify){
		return oaNotifyRecordDao.findList(new OaNotifyRecord(oaNotify));
	}
	public Page<OaNotify> find(Page<OaNotify> page, OaNotify oaNotify) {
		oaNotify.setPage(page);
		page.setList(dao.findList(oaNotify));
		return page;
	}
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify) {
		return dao.findCount(oaNotify);
	}
	
	@Transactional(readOnly = false)
	public void save(OaNotify oaNotify) {
		super.save(oaNotify);
		
		// 更新发送接受人记录
		oaNotifyRecordDao.deleteByOaNotifyId(oaNotify.getId());
		if (oaNotify.getOaNotifyRecordList().size() > 0){
			oaNotifyRecordDao.insertAll(oaNotify.getOaNotifyRecordList());
		}
	}
	
	/**
	 * 更新阅读状态
	 */
	@Transactional(readOnly = false)
	public void updateReadFlag(OaNotify oaNotify) {
		OaNotifyRecord oaNotifyRecord = new OaNotifyRecord(oaNotify);
		oaNotifyRecord.setUser(oaNotifyRecord.getCurrentUser());
		oaNotifyRecord.setReadDate(new Date());
		oaNotifyRecord.setReadFlag("1");
		oaNotifyRecordDao.update(oaNotifyRecord);
	}
	
	public String pushMsg(String notify_id,Integer push_type){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = this.dao.findMapById(notify_id);
		
		formatDate(m);
		
		map.put("content", m);
		map.put("push_type", push_type);
		
		if(push_type == 1 || push_type == 2){
			map.put("cid_list", this.dao.selectCids(notify_id));
		}
		
		return this.push(JsonMapper.toJsonString(map));
	}
	@Autowired
	private TrainLessonsDao trainLessonsDao;
	@Autowired
	private TrainCategorysDao trainCategorysDao;
	
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
		
		String type = (String) map.get("notify_type");
		if("4".equals(type)){
			TrainLessons l = this.trainLessonsDao.get(((String)map.get("content")).trim());
			
			map.put("name", l.getName());
			map.put("pic", l.getCoverPic());
		}else if("5".equals(type)){
			TrainCategorys tc = this.trainCategorysDao.get(((String)map.get("content")).trim());
			map.put("name", tc.getName());
			map.put("pic",tc.getCoverPic());
		}
		
		
	}
	private String push(String jsonObj){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonObj,headers);
		
		String push_url = ParametersFactory.getMtmyParamValues("push_url");
		//Global.getInstance().getConfig("push_url")
		
		String json = restTemplate.postForObject(push_url, entity, String.class);
		System.out.println("pushjsonStr == "+jsonObj);
		return json;
	}
	@Transactional(readOnly = false)
	public void updateStatus(String notify,Integer status){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notify_id", notify);
		map.put("status", status);
		this.dao.updateStatus(map);
	}
}
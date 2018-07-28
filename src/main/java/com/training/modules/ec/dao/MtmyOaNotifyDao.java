/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyOaNotify;

/**
 * 每天每夜通知通告DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface MtmyOaNotifyDao extends CrudDao<MtmyOaNotify> {
	
	
	public Map<String, Object> findMapById(String notify_id);
	
	public List<String> selectCids(String notify_id);
	
	public void updateStatus(Map<String, Object> map);
	
	/**
	 * 查询一定时间内未推送的需要定时推送的消息
	 * @return
	 */
	public List<MtmyOaNotify> queryAutoPushMessage();
}
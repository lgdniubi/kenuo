/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.oa.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.oa.entity.OaNotify;

/**
 * 通知通告DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyDao extends CrudDao<OaNotify> {
	
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	public Long findCount(OaNotify oaNotify);
	
	public Map<String, Object> findMapById(String notify_id);
	
	public List<String> selectCids(String notify_id);
	
	public void updateStatus(Map<String, Object> map);
	
}
/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyOaNotifyRecord;

/**
 * 每天每夜通知通告记录DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface MtmyOaNotifyRecordDao extends CrudDao<MtmyOaNotifyRecord> {
	
	/**
	 * 插入通知记录
	 * @param oaNotifyRecordList
	 * @return
	 */
	public int insertAll(List<MtmyOaNotifyRecord> mtmyOaNotifyRecord);
	
	/**
	 * 根据通知ID删除通知记录
	 * @param oaNotifyId 通知ID
	 * @return
	 */
	public int deleteByOaNotifyId(String oaNotifyId);
	
	/**
	 * 保存任务id以便后续获取推送结果
	 * @param map
	 */
	public void updateNotifyContentId(Map<String, Object> map);
	
	/**
	 * 修改推送结果
	 * @param map
	 */
	public void updatePushResult(Map<String, Object> map);
	
	/**
	 *  根据用户user_id查询用户是否有client
	 * @param userId
	 * @return
	 */
	public int selectClient(int userId);
	
}
package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveUser;

/**
 * 直播会员dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface TrainLiveUserDao extends CrudDao<TrainLiveUser>{

	/**
	 * 导入会员数据
	 * @param trainLiveUser
	 * @return
	 */
	public int insertLiveUser(TrainLiveUser trainLiveUser);
	
	/**
	 * 删除数据
	 * @param trainLiveUser
	 * @return
	 */
	public int deleteUser(TrainLiveUser trainLiveUser);
	
	/**
	 * 查询数据
	 * @param auditId
	 * @return
	 */
	public List<String> selectLiveUser(String auditId);
	
	/**
	 * 查询已经预约的用户
	 * @param auditId
	 * @return
	 */
	public List<String> selectWantLiveUser(String auditId);
	
	/**
	 * 查询每天美耶直播预约的用户cid
	 * @param auditId
	 * @return
	 */
	public List<String> selectMtmyLiveUserClient(String auditId);
	
	/**
	 * 查询每天美耶直播预约直播间信息
	 * @param auditId
	 * @return
	 */
	public String selectLiveAuitMessage(String auditId);
	
}

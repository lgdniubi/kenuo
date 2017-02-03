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

}

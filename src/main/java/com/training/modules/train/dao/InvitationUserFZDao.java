package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.InvitationUserFZ;

/**
 * 邀请明细管理Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface InvitationUserFZDao extends CrudDao<InvitationUserFZ>{
	
	/**
	 * 被邀请人查询
	 * @param invitationUser
	 * @return
	 */
	public List<InvitationUserFZ> findByList(InvitationUserFZ invitationUserFZ);
	
	/**
	 * 查询被邀请人信息
	 * @param userId
	 * @return
	 */
	public InvitationUserFZ findByUserId(int userId);
	
	/**
	 * 更新数据
	 * @param invitationUser
	 * @return
	 */
	public int UpdateInUser(InvitationUserFZ invitationUserFZ);
	
	/**
	 * 根据订单id 查询被邀请人信息
	 * @param userId
	 * @return
	 */
	public InvitationUserFZ findByOrderId(String orderId);
	

}

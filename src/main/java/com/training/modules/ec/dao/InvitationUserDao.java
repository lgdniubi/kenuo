package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.InvitationUser;

/**
 * 邀请明细管理Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface InvitationUserDao extends CrudDao<InvitationUser>{
	
	/**
	 * 被邀请人查询
	 * @param invitationUser
	 * @return
	 */
	public List<InvitationUser> findByList(InvitationUser invitationUser);
	
	/**
	 * 查询被邀请人信息
	 * @param userId
	 * @return
	 */
	public InvitationUser findByUserId(int userId);
	
	/**
	 * 更新数据
	 * @param invitationUser
	 * @return
	 */
	public int UpdateInUser(InvitationUser invitationUser);
	
	/**
	 * 根据订单id 查询被邀请人信息
	 * @param userId
	 * @return
	 */
	public InvitationUser findByOrderId(String orderId);
	

}

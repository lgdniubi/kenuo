package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.entity.UsersAccounts;
import com.training.modules.sys.entity.User;

/**
 * 用户Dao
 * @author coffee
 *
 */

@MyBatisDao
public interface MtmyUsersDao extends CrudDao<Users>{
	/**
	 * 通知--查询所有有cid的用户信息
	 * @param users
	 * @return
	 */
	public List<Users> findUserByCid(Users users);
	/**
	 * 冻结、解冻用户
	 * @param user
	 */
	public void lockUser(Users user);
	/**
	 * 验证手机号是否存在
	 * @param user
	 * @return
	 */
	public int findUserBymobile(Users user);
	/**
	 * 验证昵称是否存在
	 * @param users
	 * @return
	 */
	public int findUserBynickName(Users users);
	/**
	 * 创建订单验证手机号是否存在
	 * @param phone
	 * @return
	 */
	public List<Users> getUserByPhone(String phone);
	/**
	 * 创建订单时根据手机号获取用户信息
	 * @param phone
	 * @return
	 */
	public Users getUsersBy(String phone);
	
	/**
	 * 返现提成
	 * @param userid
	 * @param usermoney
	 * @return
	 */
	
	public int UpdateUserMoney(Users user);
	
	/**
	 * 消费赠送积分
	 * @param user
	 * @return
	 */
	
	public int UpdateUserPayPost(Users user);
	/**
	 * 修改账户余额
	 * @param map
	 * @return
	 */
	public int modifyUserMoneyForMtmy(Map<String, Object> map);
	
	public int modifyPay_points(Map<String, Object> map);
	/**
	 * 根据条件查询数据
	 * @param users
	 * @return
	 */
	public List<Users> findAllBy(Users users);
	/**
	 * 
	 * @return
	 */
	public List<Integer> selectSend();
	/**
	 * 妃子校用户更新到每天美耶
	 * @param user
	 * @return
	 */
	public int trainsInsertMtmy(User user);
	/**
	 * 妃子校更新用户到每天美耶时  进行验证
	 * @param user
	 * @return
	 */
	public Users findByMobile(User user); 
	/**
	 * 删除美容师  或解除用户时更新  每天美耶用户来源
	 */
	public void relieveUser(Users users);
	/**
	 * 新增用户时插入用户账目表
	 * @param users
	 */
	public void insertAccounts(Users users);
	/**
	 * 新增用户时插入用户统计表
	 * @param user
	 */
	public void insterSaleStats(Users users);
	
	/**
	 * 根据用户ID，查询用户账户信息
	 * @param users
	 * @return
	 */
	public UsersAccounts findUserAccounts(UsersAccounts usersAccounts);
	
	/**
	 * 根据手机号码查询妃子校用户信息
	 * @param mobile
	 * @return
	 */
	public Users findTrainsUserForMobile(String mobile);
	
	/**
	 * 根据手机号码查询每天美耶用户信息
	 * @param mobile
	 * @return
	 */
	public Users findMtmyUserForMobile(String mobile);
	
	/**
	 * 根据用户ID查询其用户的订单
	 * @param userId
	 * @return
	 */
	public List<Orders> findMtmyOrderForUser(int userId);
	
	/**
	 * 根据用户ID查询用户分销相关关系信息
	 * @param userId
	 * @return
	 */
	public MtmySaleRelieve findMtmySaleRelieve(int userId);
	
	/**
	 * 根据用户ID物理删除用户信息
	 * @param userId
	 */
	public int delMtmyUser(int userId);
	
	/**
	 * 根据用户ID删除分销关系数据
	 * @param userId
	 */
	public int delMtmyUserSaleRelations(int userId);
	
	/**
	 * 根据邀请码删除邀请码数据
	 * @param userId
	 */
	public int delMtmyUserSaleInvitationcodes(String invitationCode);
}

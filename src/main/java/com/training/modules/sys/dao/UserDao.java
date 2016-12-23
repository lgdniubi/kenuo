/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.entity.UserLog;
import com.training.modules.sys.entity.UserOfficeCode;
import com.training.modules.sys.entity.UserVo;

/**
 * 用户DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);
	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	/**
	 * 推送加载用户
	 * @param user
	 * @return
	 */
	public List<User> findOaUser(User user);
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 插入好友
	 */
	public int insertFriend(@Param("id")String id, @Param("userId")String userId, @Param("friendId")String friendId);
	
	/**
	 * 查找好友
	 */
	public User findFriend(@Param("userId")String userId, @Param("friendId")String friendId);
	/**
	 * 删除好友
	 */
	public void deleteFriend(@Param("userId")String userId, @Param("friendId")String friendId);
	
	/**
	 * 
	 * 获取我的好友列表
	 * 
	 */
	public List<User> findFriends(User currentUser);
	
	/**
	 * 
	 * 查询用户-->用来添加到常用联系人
	 * 
	 */
	public List<User> searchUsers(User user);
	
	/**
	 * 
	 */
	
	public List<User>  findListByOffice(User user);
	/**
	 * 获取用户工号最大值
	 * @return
	 */
	public int getNO(String officeid);
	/**
	 * 验证手机号是否存在
	 * @param mobile
	 * @return
	 */
	public User getByMobile(String mobile); 
	/**
	 * 验证手机号是否存在
	 * @param mobile
	 * @return
	 */
	public User getByNO(String no); 
	/**
	 * 验证身份证号码是否存在
	 * @param idCard
	 * @return
	 */
	public User getUserByIdCard(String idCard);
	/**
	 * 保存操作日志
	 * @param user
	 */
	public void saveUserLog(User user);
	
	/**
	 * 删除用户数据
	 * @param idCard
	 * @return
	 */
	public int userDeleteByidCard(String idCard);
	/**
	 * 查询用户最新两条日志
	 * @param id
	 * @return
	 */
	public List<UserLog> findUserLog(String userId);
	/**
	 * 查询用户所有日志
	 * @param userLog
	 * @return
	 */
	public List<UserLog> findUserLogList(UserLog userLog);
	/**
	 * 查询用户角色名称
	 * @param user
	 * @return
	 */
	public Role findRoleName(String id);
	/**
	 * 根据用户id查找office_code
	 * @param user_id
	 * @return
	 */
	public List<UserOfficeCode> queryUserOfficeCodes();
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<UserVo> queryUserAll();
	
	/**
	 * 返现提成
	 * @param user
	 * @return
	 */
	public int UpdateUserMoney(User user);
	
	/**
	 * 增加金币
	 * @param user
	 * @return
	 */
	public int UpdateUserPayPost(User user);
	/**
	 * 修改用户权限
	 * @param user
	 */
	public void UpdateDataScope(User user);
	/**
	 * 物理删除用户权限中间表
	 * @param user
	 */
	public void deleteUserOffice(User user);
	/**
	 * 按明细时修改用户权限中间表
	 * @param user
	 */
	public void insertDataScope(User user);
	/**
	 * 查询用户权限
	 * @param user
	 * @return
	 */
	public User findAuth(User user);
	/**
	 * 查询角色下的用户
	 * @param user
	 * @return
	 */
	public List<User> findRoleUser(User user);
}

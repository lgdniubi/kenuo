package com.training.modules.ec.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.track.thread.TrackThread;
import com.training.modules.ec.dao.IntegralLogDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.TradingLogDao;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.entity.UsersAccounts;


/**
 * 用户service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyUsersService extends CrudService<MtmyUsersDao,Users>{
	@Autowired
	private TradingLogDao tradingLogDao;
	@Autowired
	private IntegralLogDao integralLogDao;
	/**
	 * 添加用户
	 * @param articleCategory
	 */
	public void addUsers(Users users){
		dao.insert(users);
		
		/*##########[神策埋点{sign_up}-Begin]##########*/
		if(null != users.getUserid() && 0 != users.getUserid()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 方法名称-注册
			paramMap.put("METHOD_NAME", "sign_up");
			// 用户ID
			paramMap.put("DISTINCT_ID", users.getUserid());
			// 匿名ID
			paramMap.put("ANONYMOUS_ID", "");
			// 来源类型
			paramMap.put("SOURCE_TYPE", users.getSourceType());
			// 来源类型名称
			paramMap.put("ACTION_SOURCE", users.getActionSource());
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(paramMap));
		}
		/*##########[神策埋点end]##########*/
	}
	/**
	 * 查询所有用户
	 */
	public Page<Users> findPage(Page<Users> page, Users users) {
		users.setPage(page);
		page.setList(dao.findAllList(users));
		return page;
	}
	/**
	 * 通知--查询所有有cid的用户信息
	 * @param users
	 * @return
	 */
	public List<Users> treeFindList(Users users){
		return dao.findUserByCid(users);
	}
	/**
	 * 通过id查询Users
	 * @param user
	 * @return
	 */
	public Users findUserById(Users user){
		return  dao.get(user);
	}
	/**
	 * 验证用户手机号
	 * @param user
	 * @return
	 */
	public int findUserBymobile(Users user){
		return dao.findUserBymobile(user);
	}
	/**
	 * 验证昵称是否存在
	 * @param users
	 * @return
	 */
	public int findUserBynickName(Users users){
		return dao.findUserBynickName(users);
	}
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public void updateUser(Users user){
		dao.update(user);
	}
	/**
	 * 冻结用户
	 * @param user
	 */
	public void lockUser(Users user){
		dao.lockUser(user);
	}
	/**
	 * 创建订单时根据手机号获取用户信息
	 * @param phone
	 * @return
	 */
	public List<Users> getUserByPhone(String phone){
		return dao.getUserByPhone(phone);
	}
	
	public int modifyUserMoneyForMtmy(Map<String, Object> map){
		int i = dao.modifyUserMoneyForMtmy(map);
		this.tradingLogDao.insertTradingLog(map);
		return i;
	}
	
	public void modifyPay_points(int value,int user_id,int type,String remark,String order_id){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("value", value);
		this.dao.modifyPay_points(map);
		//积分明细
		map.put("type", type);
		map.put("remark", remark);
		map.put("order_id", order_id);
		this.integralLogDao.insertIntegralLog(map);
	}
	/**
	 * 
	 * @param users
	 * @return
	 */
	public List<Users> findAllBy(Users users){
		return dao.findAllBy(users);
	}
	/**
	 * 
	 * @return
	 */
	public List<Integer> selectSend(){
		return dao.selectSend();
	}
	
	/**
	 * 根据用户ID，查询用户账户信息
	 * @param users
	 * @return
	 */
	public UsersAccounts findUserAccounts(UsersAccounts usersAccounts){
		return dao.findUserAccounts(usersAccounts);
	}
	
	/**
	 * 根据手机号码查询妃子校用户信息
	 * @param mobile
	 * @return
	 */
	public Users findTrainsUserForMobile(String mobile){
		return dao.findTrainsUserForMobile(mobile);
	}
	
	/**
	 * 根据手机号码查询每天美耶用户信息
	 * @param mobile
	 * @return
	 */
	public Users findMtmyUserForMobile(String mobile){
		return dao.findMtmyUserForMobile(mobile);
	}
	
	/**
	 * 根据用户ID查询其用户的订单
	 * @param userId
	 * @return
	 */
	public List<Orders> findMtmyOrderForUser(int userId){
		return dao.findMtmyOrderForUser(userId);
	}
	
	/**
	 * 根据用户ID查询用户分销相关关系信息
	 * @param userId
	 * @return
	 */
	public MtmySaleRelieve findMtmySaleRelieve(int userId){
		return dao.findMtmySaleRelieve(userId);
	}
	
	/**
	 * 根据用户ID物理删除用户信息
	 * @param userId
	 */
	public int delMtmyUser(int userId){
		return dao.delMtmyUser(userId);
	}
	
	/**
	 * 根据用户ID删除分销关系数据
	 * @param userId
	 */
	public int delMtmyUserSaleRelations(int userId){
		return dao.delMtmyUserSaleRelations(userId);
	}
	
	/**
	 * 根据邀请码删除邀请码数据
	 * @param userId
	 */
	public int delMtmyUserSaleInvitationcodes(String invitationCode){
		return dao.delMtmyUserSaleInvitationcodes(invitationCode);
	}
	
	/**
	 * 根据手机号查找用户
	 * @return
	 */
	public Users getUserByMobile(String mobile){
		return dao.getUserByMobile(mobile);
	}
	
	/**
	 * 若新增用户的手机号在每天美耶已经存在，则将新该用户的等级改为A
	 */
	public void updateLayer(int userId){
		dao.updateLayer(userId);
	}
	
	/**
	 * 根据被邀请人的id删除表中对应的数据 
	 */
	public void deleteFromSaleRelations(int userId){
		dao.deleteFromSaleRelations(userId);
	}
	
	/**
	 * 修改是否正常数据
	 * @param isTest
	 * @param userId
	 */
	public void updateIsRealData(String isTest,String userId){
		dao.updateIsRealData(isTest, userId);
	}
}

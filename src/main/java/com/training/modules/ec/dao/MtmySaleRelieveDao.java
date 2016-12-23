package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.MtmySaleRelieveLog;

/**
 * 分销   解除用户DAO
 * @author coffee
 *
 */
@MyBatisDao
public interface MtmySaleRelieveDao extends CrudDao<MtmySaleRelieve>{
	/**
	 * 分销   分页查询所有待解除用户信息
	 * @param mtmySaleRelieve
	 * @return
	 */
	public List<MtmySaleRelieve> findAllRelieve(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 修改申请表申请状态及备注
	 * @param mtmySaleRelieve
	 */
	public void updateStatus(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 修改原始表申请状态
	 * @param mtmySaleRelieve
	 */
	public void updateBy(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 同意用户解除后   修改原始表parent_id
	 * @param mtmySaleRelieve
	 */
	public void updateParentId(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 查询所有A
	 * @param mtmySaleRelieve
	 * @return
	 */
	public List<MtmySaleRelieve> findAllA(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 查看A用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	public MtmySaleRelieve findByUserId(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 查询单个A级用户所有下级详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	public List<MtmySaleRelieve> findByParentId(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 查询A下级单个用户贡献信息
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public List<MtmySaleRelieveLog> findUserDetails(MtmySaleRelieveLog mtmySaleRelieveLog);
	/**
	 * A用户收益明细
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public List<MtmySaleRelieveLog> findUserEarnings(MtmySaleRelieveLog mtmySaleRelieveLog);
	/**
	 * 按天查询用户当天的收益明细
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public List<MtmySaleRelieveLog> findUserAllOrder(MtmySaleRelieveLog mtmySaleRelieveLog);
	/**
	 * 查询A以外的分销用户
	 * @param mtmySaleRelieve
	 * @return
	 */
	public List<MtmySaleRelieve> findOtherUsers(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 查询A以外的分销用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	public MtmySaleRelieve findOtherUsersByUserId(MtmySaleRelieve mtmySaleRelieve);
	/**
	 * 日结算订单存储过程
	 * @return
	 */
	public void SaleOrderInsertLog();
	/**
	 * 日结算存储过程
	 * @return
	 */
	public void SaleDayAccounts();
	/**
	 * 总结算
	 * @return
	 */
	public void SaleSettleDayAccounts();
}



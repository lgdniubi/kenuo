package com.training.modules.ec.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Customer;

/**
 * 新客表对应的Dao
 * @author xiaoye 2017年10月26日
 *
 */
@MyBatisDao
public interface CustomerDao extends CrudDao<Customer>{
	
	/**
	 * 插入新客
	 * @param customer
	 */
	public void insertCustomer(Customer customer);
	
	
	/**
	 * 插入新客日志
	 * @param customer
	 */
	public void insertCustomerLog(Customer customer);
	
	/**
	 * 查询每天美耶用户是否绑定商家 
	 * @param userId
	 * @param franchiseeId
	 * @return
	 */
	public int selectFranchisee(@Param(value="userId")int userId,@Param(value="franchiseeId")int franchiseeId);
	
	/**
	 * 每天美耶用户绑定商家的基础上绑定店铺或店铺和美容师
	 * @param userId
	 * @param franchiseeId
	 */
	public void insertOfficeOrBeauty(Customer customer);
	
	/**
	 * 查询每天美耶用户绑定的商家对应的店铺
	 * @param userId
	 * @param franchiseeId
	 * @return
	 */
	public Customer selectFranchiseeOffice(@Param(value="userId")int userId,@Param(value="franchiseeId")int franchiseeId);
}

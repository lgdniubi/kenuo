package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyCheckAccount;


/**
 * 对账接口
 * @author coffee
 *
 */
@MyBatisDao
public interface CheckAccountDao extends CrudDao<MtmyCheckAccount>{
	/**
	 * 插入微信支付宝数据
	 */
	public int insterAccount(List<MtmyCheckAccount> list);
	/**
	 * 导入ping++数据
	 * @param mtmyCheckAccount
	 */
	public int findByOrderNo(MtmyCheckAccount mtmyCheckAccount); 
	/**
	 * 查询所有标示
	 * @return
	 */
	public List<MtmyCheckAccount> findGroupFlag();
	
}

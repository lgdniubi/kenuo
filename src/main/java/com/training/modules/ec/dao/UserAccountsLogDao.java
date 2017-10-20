package com.training.modules.ec.dao;


import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.UserAccountsLog;


/**
 *操作日志dao
 * @author 土豆  2017-10-17
 *
 */

@MyBatisDao
public interface UserAccountsLogDao extends TreeDao<UserAccountsLog>{
	
	/**
	 * 插入用户账户充值记录表
	 * @param orderGoods
	 * @return
	 */
	public int insertUserAccountsLog(UserAccountsLog userAccountsLog);
	
}

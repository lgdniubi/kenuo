package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Franchisee;

/**
 * 加盟商管理DAO接口
 * @author kele
 * @version 2016-6-4 16:17:23
 */
@MyBatisDao
public interface FranchiseeDao extends TreeDao<Franchisee>{

	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Franchisee> findListbyPID(String pid);
	
	/**
	 * 添加数据
	 * @param franchisee
	 * @return
	 */
	public void insertFranchisee(Franchisee franchisee);
	
	/**
	 * 修改数据
	 */
	public int update(Franchisee franchisee);
}

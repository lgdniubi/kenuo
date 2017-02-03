package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Userinfo;

@MyBatisDao
public interface UserinfoDao extends CrudDao<Userinfo> {
	
	
	/**
	 * 插入数据库
	 */
	public int   insertUserinfo(Userinfo userinfo);
	
	/**
	 * 根据用户id检查数据是否存在
	 * @param userid
	 * @return
	 */
	public Userinfo  findByuserId(String userid);
	/**
	 * 更新数据
	 * @param userinfo
	 * @return
	 */
	
	public int updateUserinfo(Userinfo userinfo);
	
	 
	
	

}

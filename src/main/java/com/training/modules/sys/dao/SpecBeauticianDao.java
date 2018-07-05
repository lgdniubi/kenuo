package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.SpecBeautician;


/**
 * 特殊美容师dao
 * @author 小叶   2016-12-29 
 *
 */
@MyBatisDao
public interface SpecBeauticianDao extends CrudDao<SpecBeautician>{
	/**
	 * 查询出所有的特殊美容师
	 */
	public List<SpecBeautician> findAllList();
	
	/**
	 * 插入新的特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public int insertSpecBeautician(SpecBeautician specBeautician);
	
	/**
	 * 逻辑删除特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public int deleteSpecBeautician(SpecBeautician specBeautician);
	
	/**
	 * 物理删除特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public int newDeleteSpecBeautician(SpecBeautician specBeautician);
	
	/**
	 * 查询特殊美容师（用于删除用户）
	 * @param userId
	 * @return
	 */
	public int findSpecBeautician(String userId);
	/**
	 * 查询用户是否是特殊美容师
	 * @param id
	 * @return
	 */
	public SpecBeautician getSpecBeautician(String id);
	/**
	 * 从特殊美容师表删除后，删除他的排班记录
	 * @param specBeautician
	 */
	public void deleteArrangeShop(SpecBeautician specBeautician);
}

package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.Appellation;

/**
 * 称谓标签Dao
 * @author xiaoye  2017年9月15日
 *
 */
@MyBatisDao
public interface AppellationDao extends CrudDao<Appellation>{
	
	/**
	 * 查询称谓标签列表
	 * @return
	 */
	public List<Appellation> findList();
	
	/**
	 * 根据id查找称谓标签
	 * @param appellationId
	 * @return
	 */
	public Appellation getAppellation(int appellationId);
	
	/**
	 * 新增称谓标签
	 * @param appellation
	 */
	public void insertAppellation(Appellation appellation);
	
	/**
	 * 修改称谓标签
	 * @param appellation
	 */
	public void updateAppellation(Appellation appellation);
	
	/**
	 * 逻辑删除称谓
	 */
	public void delAppellation(int appellationId);
}

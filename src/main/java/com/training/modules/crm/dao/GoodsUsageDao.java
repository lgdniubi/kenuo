package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.GoodsUsage;

/**    
* kenuo      
* @description：商品使用DAO接口
* @author：sharp   
* @date：2017年3月7日            
*/
@MyBatisDao
public interface GoodsUsageDao extends CrudDao<GoodsUsage> {

	/**
	 * 查找单个用户的记录
	 * @param 根据userId查询使用记录
	 * @return List<GoodsUsage>
	 */
	public List<GoodsUsage> findUsageList(GoodsUsage entity);	
	
	/**
	 * 修改单条数据
	 * @param 
	 * @return int
	 */
	public int updateSigle(GoodsUsage entity);
}

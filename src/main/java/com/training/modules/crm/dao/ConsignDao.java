package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.Consign;

/**    
* kenuo      
* @description：  寄存DAO接口
* @author：sharp   
* @date：2017年3月7日            
*/
@MyBatisDao
public interface ConsignDao extends CrudDao<Consign> {

	
	public List<Consign> findConsignList(Consign entity);	
	public int updateSingle(Consign entity);
}

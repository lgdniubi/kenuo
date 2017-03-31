package com.training.modules.crm.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.ShapeFile;


/**    
* kenuo      
* @description:形体档案DAO
* @author：sharp   
* @date：2017年3月8日            
*/
@MyBatisDao
public interface ShapeFileDao extends CrudDao<ShapeFile> {

	
}

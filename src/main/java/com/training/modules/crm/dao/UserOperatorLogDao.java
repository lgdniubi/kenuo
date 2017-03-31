package com.training.modules.crm.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.UserOperatorLog;

/**    
* kenuo      
* @description： 用户详细信息操作日志DAO  
* @author：sharp   
* @date：2017年3月9日            
*/
@MyBatisDao
public interface UserOperatorLogDao extends CrudDao<UserOperatorLog>{
			
	
		
}

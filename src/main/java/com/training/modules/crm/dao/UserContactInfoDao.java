package com.training.modules.crm.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.UserContactInfo;

/**    
* kenuo      
* @description：用户联系信息的DAO   
* @author：sharp   
* @date：2017年3月6日            
*/
@MyBatisDao
public interface UserContactInfoDao extends CrudDao<UserContactInfo>{
	
	
	/**
	 * 修改记录
	 * @param 
	 * @return int
	 */
	public int updateSingle(UserContactInfo entity);
	
	
}

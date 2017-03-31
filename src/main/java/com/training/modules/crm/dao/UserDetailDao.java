package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.UserDetail;

/**   
*    
* kenuo      
* @description：   
* @author：sharp   
* @date：2017年3月2日    
*        
*/
@MyBatisDao
public interface UserDetailDao extends CrudDao<UserDetail>{
			
	/**
	 * @param 
	 * @return List<UserDetailDTO>
	 * @description 根据条件查询用户列表
	 */
	public List<UserDetail> findUserList(UserDetail entity);	
	
	/**
	 * 修改单条数据
	 * @param userDetail
	 * @return int
	 */
	public int updateSingle(UserDetail entity);		
	
	/**
	 * 用userId找到用户昵称
	 * @param 
	 * @return UserDetail
	 */
	public UserDetail getUserNickname(String userId);

}

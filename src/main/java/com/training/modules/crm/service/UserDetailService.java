package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.UserDetailDao;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.ec.utils.CommonScopeUtils;

/**   		  
* kenuo      
* @description：   
* @author：sharp   
* @date：2017年3月1日           
*/
@Service
@Transactional(readOnly = false)
public class UserDetailService extends CrudService<UserDetailDao,UserDetail> {
	
	
	/**
	 * 修改单条数据
	 * @param 
	 * @return int
	 */
	public int updateSingle(UserDetail entity){
		return dao.updateSingle(entity);
	}
	
	/**
	 * 用户管理界面查找用户列表
	 * @param 
	 * @return Page<UserDetail>
	 */
	public Page<UserDetail> getUserList(Page<UserDetail> page,UserDetail dto){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//orders.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		dto.getSqlMap().put("dsf",CommonScopeUtils.dataScopeFilter("mu"));
	    dto.setPage(page);
		page.setList(dao.findUserList(dto));
		return page;
	}
	
	/**
	 * 查找用户昵称
	 * @param 
	 * @return UserDetail
	 */
	public UserDetail getUserNickname(String userId){
		return dao.getUserNickname(userId);
	}
	
	/**
	 * 不过滤权限
	 * @param 
	 * @return UserDetail
	 */
	public Page<UserDetail> getUserWithoutScope(Page<UserDetail> page,UserDetail dto){
		//不生成数据权限过滤条件
		 dto.setPage(page);
		 page.setList(dao.getUserWithoutScope(dto));
		 return page;
	}
	
	/**
	 * 绑定店铺
	 * @param 
	 * @return int
	 */
	public int updateMtmyUsers(UserDetail entity){
		
		 return dao.updateMtmyUsers(entity);
	}
}

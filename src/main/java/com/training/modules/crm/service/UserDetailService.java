package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.UserDetailDao;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.ec.utils.CommonScopeUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

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
		User user = UserUtils.getUser();
		String a = user.getOffice().getParentIds()+user.getOffice().getId();
		StringBuilder sqlString = new StringBuilder();
		
		// 超级管理员，跳过权限过滤
		if(!user.isAdmin()){
			//当用户非“管理员”用户时，根据数据权限判断
			if(User.DATA_SCOPE_OFFICE_AND_CHILD.equals(String.valueOf(user.getDataScope()))){
				//数据范围(1:所在部门及以下数据)
				sqlString.append(" WHERE 1 = 1 ");
				sqlString.append(" AND mc.office_id IN (SELECT id FROM sys_office o ");
				sqlString.append(" WHERE locate( '" + a +"',concat(o.parent_ids, o.id))>0)");
			}else if(User.DATA_SCOPE_CUSTOM.equals(String.valueOf(user.getDataScope()))){
				//数据范围(2:按明细设置)
				sqlString.append(" WHERE 1 = 1 ");
				sqlString.append(" AND mc.office_id IN (SELECT b.office_id FROM sys_user_office b ");
				sqlString.append(" WHERE b.user_id = '"+user.getId()+"')");
			}
		}else{
			//当用户为“管理员”,查询所有数据
			sqlString.append(" WHERE 1 = 1 ");
		}
		dto.getSqlMap().put("dsf",sqlString.toString());
		System.out.println(sqlString.toString());
//		dto.getSqlMap().put("dsf",CommonScopeUtils.dataScopeFilter("mc"));
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
	 * 获取该用户的绑定店铺信息
	 * @param uDetail
	 * @return
	 */
	public UserDetail getOfficeByDetail(UserDetail uDetail) {
		return dao.getOfficeByDetail(uDetail);
	}
	
}

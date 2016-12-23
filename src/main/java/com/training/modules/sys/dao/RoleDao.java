/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Role;

/**
 * 角色DAO接口
 * 
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);
	/**
	 * 新增用户时 默认用户为美容师   查询美容师id  2016年11月9日18:17:31  咖啡
	 * @param role
	 * @return
	 */
	public Role getByNameNew(Role role);
}

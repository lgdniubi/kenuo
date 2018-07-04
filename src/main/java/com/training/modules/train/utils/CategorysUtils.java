package com.training.modules.train.utils;

import java.util.List;

import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainCategorys;

/**
 * 课程分类-工具类
 * @author kele
 *
 */
public class CategorysUtils {
	
	
	/**
	 * 课程分类-数据范围-获取数据
	 * @param trainCategorys
	 * @return
	 */
	public static TrainCategorys categorysFilter(TrainCategorys trainCategorys){
		//添加数据权限
		User user = UserUtils.getUser();//获取当前登录用户
		trainCategorys.setCreateuser(user.getId());
		trainCategorys.setOfficeType(user.getOffice().getType());
		String sysRoleType = CategorysUtils.getSysRoleType(user.getRoleList());
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		trainCategorys.getSqlMap().put("dsf", CategorysUtils.dataScopeFilter(user, sysRoleType));
		return trainCategorys;
	}
	
	/**
	 * 获取角色类型
	 * @param roleList 用户角色列表
	 * @return sysRoleType(1：超级管理员;2：普通管理员;3：培训师)
	 */
	public static String getSysRoleType(List<Role> roleList){
		int roletype = Integer.parseInt(roleList.get(0).getRoleType());
		for (int i = 0; i < roleList.size(); i++) {
			Role role = roleList.get(i);
			if(roletype > Integer.parseInt(role.getRoleType())){
				roletype = Integer.parseInt(role.getRoleType());
			}
		}
		return String.valueOf(roletype);
	}
	
	/**
	 * 课程分类-数据范围过滤
	 * @param user	用户对象
	 * @param sysRoleType(1：超级管理员;2：普通管理员;3：培训师)
	 * @return
	 */
	public static String dataScopeFilter(User user, String sysRoleType) {
		StringBuilder sqlString = new StringBuilder();
		if(!user.isAdmin()){
			//超级管理员
			if("1".equals(sysRoleType)){
				//查询所有数据
			}else if("2".equals(sysRoleType)){
				//普通管理员
				sqlString.append("AND (t.office_code LIKE '"+user.getOffice().getCode()+"%' OR t.cate_type = '1')");
			}else if("3".equals(sysRoleType)){
				//培训师
				sqlString.append("AND (t.createuser = '"+user.getId()+"' OR t.cate_type = '1')");
			}
		}
		return sqlString.toString();
	}
}

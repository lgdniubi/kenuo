package com.training.modules.train.utils;

import org.springframework.transaction.annotation.Transactional;

import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 权限工具类
 * @author 
 *
 */
@Transactional(readOnly = true)
public abstract class ScopeUtils {
	public static String dataScopeFilter(String Alias,String flag){
		User user = UserUtils.getUser();
		String a = user.getOffice().getParentIds()+user.getOffice().getId();
		StringBuilder sqlString = new StringBuilder();
		
		// 超级管理员，跳过权限过滤
		if(!user.isAdmin()){
			//当用户非“管理员”用户时，根据数据权限判断
			if(User.DATA_SCOPE_OFFICE_AND_CHILD.equals(String.valueOf(user.getDataScope()))){
				//数据范围(1:所在部门及以下数据)
				sqlString.append(" LEFT JOIN sys_office o ON o.id = "+Alias+".office_id");
				sqlString.append(" WHERE 1 = 1 ");
				sqlString.append("and ( locate( '" + a +"',concat(o.parent_ids, o.id))>0");
			}else if(User.DATA_SCOPE_CUSTOM.equals(String.valueOf(user.getDataScope()))){
				//数据范围(2:按明细设置)
				sqlString.append("LEFT JOIN sys_user_office b ON b.office_id = "+Alias+".office_id ");
				sqlString.append(" WHERE 1 = 1 AND ( b.user_id = '"+user.getId()+"'");
			}
			//当为课程分类时  增加cate_type = 1 定制
			if(flag.equals("category")){
				sqlString.append(" or " + Alias +".createuser = '"+ user.getId() +"' OR (" + Alias +".cate_type = '1' AND "+ Alias +".office_id = '"+ user.getCompany().getId() +"' ))");
			}else{
				sqlString.append(" or " + Alias +".createuser = '"+ user.getId() +"')");
			}
			
		}else{
			//当用户为“管理员”,查询所有数据
			sqlString.append(" WHERE 1 = 1 ");
		}
		return sqlString.toString();
	}
}

package com.training.modules.ec.utils;

import org.springframework.transaction.annotation.Transactional;

import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 通用权限工具类
 * @author 
 *
 */
@Transactional(readOnly = true)
public abstract class CommonScopeUtils {
	public static String dataScopeFilter(String Alias){
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
				sqlString.append("and ( locate( '" + a +"',concat(o.parent_ids, o.id))>0)");
			}else if(User.DATA_SCOPE_CUSTOM.equals(String.valueOf(user.getDataScope()))){
				//数据范围(2:按明细设置)
				sqlString.append("LEFT JOIN sys_user_office b ON b.office_id = "+Alias+".office_id ");
				sqlString.append(" WHERE 1 = 1 AND ( b.user_id = '"+user.getId()+"')");
			}
		}else{
			//当用户为“管理员”,查询所有数据
			sqlString.append(" WHERE 1 = 1 ");
		}
		return sqlString.toString();
	}
	
	//针对预约专门写的数据权限
	public static String newDataScopeFilter(String Alias){
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
				sqlString.append("and (( locate('" + a +"',concat(o1.parent_ids, o1.id))>0) OR ( locate( '"+ a +"',concat(o.parent_ids, o.id))>0))");
			}else if(User.DATA_SCOPE_CUSTOM.equals(String.valueOf(user.getDataScope()))){
				//数据范围(2:按明细设置)
				sqlString.append("LEFT JOIN sys_user_office b1 ON b1.office_id = "+Alias+".office_id OR b1.office_id =" + Alias+".shop_id ");
				sqlString.append(" WHERE 1 = 1 AND ((b1.user_id = '"+user.getId()+"') OR (b1.user_id =" +Alias+".user_id))");
			}
		}else{
			//当用户为“管理员”,查询所有数据
			sqlString.append(" WHERE 1 = 1 ");
		}
		return sqlString.toString();
	}
}

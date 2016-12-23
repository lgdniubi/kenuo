package com.training.modules.train.utils;

import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainLessons;

/**
 * 课程-工具类
 * @author kele
 *
 */
public class CourseUtils {
	/**
	 * 课程分类-数据范围-获取数据
	 * @param trainCategorys
	 * @return
	 */
	public static TrainLessons lessonsFilter(TrainLessons trainLessons){
		//添加数据权限
		User user = UserUtils.getUser();//获取当前登录用户
		trainLessons.setCreateuser(user.getId());
		String sysRoleType = CategorysUtils.getSysRoleType(user.getRoleList());
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		trainLessons.getSqlMap().put("dsf", CourseUtils.dataScopeFilter(user, sysRoleType));
		return trainLessons;
	}
	
	/**
	 * 课程-数据范围过滤
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
				sqlString.append("AND t.office_code LIKE '"+user.getOffice().getCode()+"%'");
			}else if("3".equals(sysRoleType)){
				//培训师
				sqlString.append("AND t.createuser = '"+user.getId()+"'");
			}
		}
		return sqlString.toString();
	}
}

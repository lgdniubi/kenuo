package com.training.modules.train.utils;

import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.Article;
import com.training.modules.train.entity.ArticleCategory;
import com.training.modules.train.entity.ExercisesCategorys;

/**
 * 试题-工具类
 * @author kele
 *
 */
public class ExamUtils {
	
	
	/**
	 * 试题-数据范围-获取数据
	 * @param exercisesCategorys
	 * @return
	 */
	public static ExercisesCategorys examFilter(ExercisesCategorys exercisesCategorys){
		//添加数据权限
		User user = UserUtils.getUser();//获取当前登录用户
		exercisesCategorys.setCreateuser(user.getId());
		exercisesCategorys.setRoleType(user.getOffice().getType());
		String sysRoleType = CategorysUtils.getSysRoleType(user.getRoleList());
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		exercisesCategorys.getSqlMap().put("dsf", ExamUtils.dataScopeFilter(user, sysRoleType));
		return exercisesCategorys;
	}
	/**
	 * 文章类别-数据范围-获取数据
	 * @param exercisesCategorys
	 * @return
	 */
	public static ArticleCategory articleCategoryFilter(ArticleCategory articleCategory){
		//添加数据权限
		User user = UserUtils.getUser();//获取当前登录用户
		articleCategory.setCreateuser(user.getId());
		articleCategory.setRoleType(user.getOffice().getType());
		String sysRoleType = CategorysUtils.getSysRoleType(user.getRoleList());
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		articleCategory.getSqlMap().put("dsf", ExamUtils.dataScopeFilter(user, sysRoleType));
		return articleCategory;
	}
	/**
	 * 文章类别-数据范围-获取数据
	 * @param exercisesCategorys
	 * @return
	 */
	public static Article articleFilter(Article article){
		//添加数据权限
		User user = UserUtils.getUser();//获取当前登录用户
		article.setCreateuser(user.getId());
		article.setRoleType(user.getOffice().getType());
		String sysRoleType = CategorysUtils.getSysRoleType(user.getRoleList());
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		article.getSqlMap().put("dsf", ExamUtils.dataScopeFilter(user, sysRoleType));
		return article;
	}
	/**
	 * @param user	用户对象
	 * @param sysRoleType(1：超级管理员;2：普通管理员;3：培训师)
	 * @return
	 */
	public static String dataScopeFilter(User user, String sysRoleType) {
		StringBuilder sqlString = new StringBuilder();
		//超级管理员
		if("1".equals(sysRoleType)){
			//查询所有数据
		}else if("2".equals(sysRoleType)){
			//普通管理员
			sqlString.append("AND (a.office_code LIKE '"+user.getOffice().getCode()+"%')");
		}else if("3".equals(sysRoleType)){
			//培训师
			sqlString.append("AND (a.createuser = '"+user.getId()+"')");
		}
		return sqlString.toString();
	}
}

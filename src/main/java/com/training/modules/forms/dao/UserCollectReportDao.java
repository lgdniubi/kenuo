package com.training.modules.forms.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.forms.entity.UserCollectReport;
import com.training.modules.forms.entity.UserInfoReport;

/**
 * 妃子校用户汇总
 * @author stone
 * @date 2017年4月24日
 */
@MyBatisDao
public interface UserCollectReportDao extends TreeDao<UserCollectReport>{

	/**
	 * 无条件分页查询
	 * @param userCollectReport
	 */
	public	List<UserCollectReport> firstCollectList(UserCollectReport userCollectReport);

	/**
	 * 根据时间段查询每天的数据
	 * @param userCollectReport
	 */
	public List<UserCollectReport> collectListByTime(UserCollectReport userCollectReport);

	/**妃子校用户信息
	 * 根据时间段查询每天的数据
	 * @param userCollectReport
	 */
	public List<UserInfoReport> infoList(UserInfoReport userInfoReport);
}

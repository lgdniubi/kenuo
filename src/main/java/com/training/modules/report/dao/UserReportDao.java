package com.training.modules.report.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.report.entity.AreaUserReport;
import com.training.modules.report.entity.CategoryReport;
import com.training.modules.report.entity.UserReport;
import com.training.modules.report.entity.UserStatisticsReport;

/**
 * 用户报表Dao
 * @author coffee
 *
 */
@MyBatisDao
public interface UserReportDao extends CrudDao<UserReport>{
	/**
	 * 客户统计
	 * @return
	 */
	public UserStatisticsReport statistics();
	/**
	 * 区域分布
	 * @return
	 */
	public List<AreaUserReport> findAreaUser();
	/**
	 * 类目分析
	 * @param categoryReport
	 * @return
	 */
	public List<CategoryReport> findCategoryReport(CategoryReport categoryReport);
}

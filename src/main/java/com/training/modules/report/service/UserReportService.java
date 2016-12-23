package com.training.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.report.dao.UserReportDao;
import com.training.modules.report.entity.AreaUserReport;
import com.training.modules.report.entity.CategoryReport;
import com.training.modules.report.entity.UserReport;
import com.training.modules.report.entity.UserStatisticsReport;

/**
 * 用户报表
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class UserReportService extends CrudService<UserReportDao, UserReport>{
	
	/**
	 * 新增用户
	 */
	public List<UserReport> findList(UserReport userReport){
		return dao.findList(userReport);
	}
	/**
	 * 客户统计
	 * @return
	 */
	public UserStatisticsReport statistics(){
		return dao.statistics();
	}
	/**
	 * 区域分布
	 * @return
	 */
	public List<AreaUserReport> findAreaUser(){
		return dao.findAreaUser();
	}
	/**
	 * 类目分析
	 * @param categoryReport
	 * @return
	 */
	public List<CategoryReport> findCategoryReport(CategoryReport categoryReport){
		return dao.findCategoryReport(categoryReport);
	}
}

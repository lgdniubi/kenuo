package com.training.modules.forms.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.forms.entity.TvShowReport;
import com.training.modules.forms.entity.TvShowTimeReport;

/**
 * 直播回放信息表
 * @author stone
 * @date 2017年4月26日
 */
@MyBatisDao
public interface TvShowReportDao extends TreeDao<TvShowReport>{

	/**
	 * 根据日期查询
	 * @param TvShowReport
	 */
	public	List<TvShowTimeReport> findTvList(TvShowTimeReport tvShowTimeReport);

	/**
	 * 根据ID查询
	 * @param TvShowReport
	 */
	public List<TvShowReport> findTvListById(TvShowReport tvShowReport);

	/**
	 * 一级分类
	 * @param TvShowReport
	 */
	public List<TvShowReport> onelist(TvShowReport tvShowReport);

	/**
	 * 二级分类
	 * @param TvShowReport
	 */
	public List<TvShowReport> findtwolist(TvShowReport tvShowReport);
}

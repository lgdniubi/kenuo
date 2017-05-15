package com.training.modules.forms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.forms.dao.TvShowReportDao;
import com.training.modules.forms.entity.TvShowReport;
import com.training.modules.forms.entity.TvShowTimeReport;

/**
 * 直播回放信息表
 * @author stone
 * @date 2017年4月26日
 */

@Service
@Transactional(readOnly = false)
public class TvShowReportService extends CrudService<TvShowReportDao,TvShowReport>{
	@Autowired
	private TvShowReportDao tvShowReportDao;

	/**
	 * 根据时间分页查询
	 * @param page
	 * @param TvShowReport
	 * @return
	 */
	public Page<TvShowTimeReport> tvList(Page<TvShowTimeReport> page, TvShowTimeReport tvShowTimeReport) {
		// 设置分页参数
		tvShowTimeReport.setPage(page);
		// 执行查询
		page.setList(tvShowReportDao.findTvList(tvShowTimeReport));
		return page;
	}

	/**
	 * 根据ID分页查询
	 * @param page
	 * @param TvShowReport
	 * @return
	 */
	public Page<TvShowReport> tvListById(Page<TvShowReport> page, TvShowReport tvShowReport) {
		//goodsReport.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		// 设置分页参数
		tvShowReport.setPage(page);
		// 执行查询
		page.setList(tvShowReportDao.findTvListById(tvShowReport));
		return page;
	}

	/**
	 * 一级分类
	 * @param TvShowReport
	 * @return
	 */
	public List<TvShowReport> onelist(TvShowReport tvShowReport) {
		List<TvShowReport> onelist = tvShowReportDao.onelist(tvShowReport);
		return onelist;
	}

	/**
	 * 二级分类
	 * @param TvShowReport
	 * @return
	 */
	public List<TvShowReport> findtwolist(TvShowReport tvShowReport) {
		List<TvShowReport> list = tvShowReportDao.findtwolist(tvShowReport);
		return list;
	}
}

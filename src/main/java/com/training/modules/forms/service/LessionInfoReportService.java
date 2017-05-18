package com.training.modules.forms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.forms.dao.LessionInfoReportDao;
import com.training.modules.forms.entity.LessionInfoReport;
import com.training.modules.forms.entity.LessionTimeReport;

/**
 * 妃子校用户汇总
 * @author stone
 * @date 2017年4月24日
 */
@Service
@Transactional(readOnly = false)
public class LessionInfoReportService extends CrudService<LessionInfoReportDao,LessionInfoReport>{
	@Autowired
	private LessionInfoReportDao lessionInfoReportDao;
	
	/**
	 * 
	 * 根据时间分页查询
	 * @param page
	 * @param lessionInfoReport
	 * @return
	 */
	public Page<LessionTimeReport> timeList(Page<LessionTimeReport> page, LessionTimeReport lessionTimeReport) {
		// 设置分页参数
		lessionTimeReport.setPage(page);
		// 执行查询
		page.setList(lessionInfoReportDao.timeList(lessionTimeReport));
		return page;
	}

	/**
	 * 根据ID分页查询
	 * @param page
	 * @param lessionInfoReport
	 * @return
	 */
	public Page<LessionInfoReport> infoListById(Page<LessionInfoReport> page, LessionInfoReport lessionInfoReport) {
		// 设置分页参数
		lessionInfoReport.setPage(page);
		// 执行查询
		page.setList(lessionInfoReportDao.findInfoListById(lessionInfoReport));
		return page;
	}
	
	/**
	 * 查询一级分类
	 * @param page
	 * @param lessionInfoReport
	 * @return
	 */
	public List<LessionInfoReport> findcategoryslist(LessionInfoReport lessionInfoReport) {
		List<LessionInfoReport> list = lessionInfoReportDao.findcategoryslist(lessionInfoReport);
		return list;
	}

	/**
	 * 查询二级分类
	 * @param page
	 * @param lessionInfoReport
	 * @return
	 */
	public List<LessionInfoReport> findtwolist(LessionInfoReport lessionInfoReport) {
		List<LessionInfoReport> list = lessionInfoReportDao.findtwolist(lessionInfoReport);
		return list;
	}

}

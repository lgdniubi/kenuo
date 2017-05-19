package com.training.modules.forms.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.forms.entity.LessionInfoReport;
import com.training.modules.forms.entity.LessionTimeReport;

/**
 * 妃子校用户汇总
 * @author stone
 * @date 2017年4月24日
 */
@MyBatisDao
public interface LessionInfoReportDao extends TreeDao<LessionInfoReport>{

	/**
	 * 根据日期查询
	 * @param lessionTimeReport
	 */
	public List<LessionTimeReport> findInfoList(LessionTimeReport lessionTimeReport);
	
	/**
	 * 根据ID查询
	 * @param lessionInfoReport
	 */
	public List<LessionInfoReport> findInfoListById(LessionInfoReport lessionInfoReport);

	/**
	 * 查询一级分类
	 * @param lessionInfoReport
	 */
	public List<LessionInfoReport> findCategorysList(LessionInfoReport lessionInfoReport);

	/**
	 * 查询二级分类
	 * @param lessionInfoReport
	 */
	public List<LessionInfoReport> findtwolist(LessionInfoReport lessionInfoReport);

	/**
	 * 视频文档日报表
	 * 查询
	 * @param lessionTimeReport
	 */
	public List<LessionTimeReport> timeList(LessionTimeReport lessionTimeReport);

	/**
	 * 查询商家分类
	 * @param lessionInfoReport
	 */
	public List<LessionInfoReport> findSellerList(LessionInfoReport lessionInfoReport);
}

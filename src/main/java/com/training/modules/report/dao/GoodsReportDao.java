package com.training.modules.report.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.report.entity.GoodsReport;

/**
 * 商品排行报表
 * @author yangyang
 *
 */

@MyBatisDao
public interface GoodsReportDao extends TreeDao<GoodsReport> {
	
	//根据日期查询
	public	List<GoodsReport> findGoodsList(GoodsReport goodsReport);
	

}

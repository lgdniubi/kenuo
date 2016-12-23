package com.training.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.report.dao.GoodsReportDao;
import com.training.modules.report.entity.GoodsReport;

/**
 * 商品销量排序
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class GoodsReportService extends CrudService<GoodsReportDao,GoodsReport>{
	
	@Autowired
	private GoodsReportDao goodsReportDao;
	

	/**
	 * 分页查询
	 * @param page
	 * @param orders
	 * @return
	 */
	public Page<GoodsReport> orderlist(Page<GoodsReport> page, GoodsReport goodsReport) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//goodsReport.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		// 设置分页参数
		goodsReport.setPage(page);
		// 执行分页查询
		page.setList(goodsReportDao.findList(goodsReport));
		return page;
	}

}

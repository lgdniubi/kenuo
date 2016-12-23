package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.SaleRebatesLog;
/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class SaleRebatesLogService extends TreeService<SaleRebatesLogDao,SaleRebatesLog> {
	
	@Autowired
	private SaleRebatesLogDao saleRebatesLogDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<SaleRebatesLog> findSaleRebatesLog(Page<SaleRebatesLog> page, SaleRebatesLog saleRebatesLog) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		saleRebatesLog.setPage(page);
		// 执行分页查询
		page.setList(saleRebatesLogDao.findList(saleRebatesLog));
		return page;
	}
	

	
}

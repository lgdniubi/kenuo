package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.TradingLogDao;
import com.training.modules.ec.entity.TradingLog;

/**
 * 账户管理
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class TradingLogService extends CrudService<TradingLogDao,TradingLog>{
	
	@Autowired
	private TradingLogDao tradinglogDao;

	
	/**
	 * 账户明细分页查询
	 * @param page
	 * @param invitationUser分页查询
	 * @return
	 */
	public Page<TradingLog> findTradingLog(Page<TradingLog> page, TradingLog tradingLog) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		tradingLog.setPage(page);
		// 执行分页查询
		page.setList(tradinglogDao.findList(tradingLog));
		return page;
	}
	
	

	


}

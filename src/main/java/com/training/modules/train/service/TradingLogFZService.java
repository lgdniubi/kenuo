package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.dao.TradingLogFZDao;
import com.training.modules.sys.entity.TradingFZLog;

/**
 * 账户管理
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class TradingLogFZService extends CrudService<TradingLogFZDao,TradingFZLog>{
	
	@Autowired
	private TradingLogFZDao tradinglogFZDao;

	
	/**
	 * 账户明细分页查询
	 * @param page
	 * @param invitationUser分页查询
	 * @return
	 */
	public Page<TradingFZLog> findTradingLog(Page<TradingFZLog> page, TradingFZLog tradingFZLog) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		tradingFZLog.setPage(page);
		// 执行分页查询
		page.setList(tradinglogFZDao.findList(tradingFZLog));
		return page;
	}
	
	

	


}

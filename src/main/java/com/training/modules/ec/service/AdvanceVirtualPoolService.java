package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.AdvanceVirtualPoolDao;
import com.training.modules.ec.entity.AdvanceVirtualPool;

/**
 * 预付款虚拟池Service
 * @author 小叶  2017-1-10 
 *
 */
@Service
public class AdvanceVirtualPoolService extends CrudService<AdvanceVirtualPoolDao,AdvanceVirtualPool>{
	@Autowired
	private AdvanceVirtualPoolDao advanceVirtualPoolDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param advanceVirtualPool
	 * @return
	 */
	public Page<AdvanceVirtualPool> findList(Page<AdvanceVirtualPool> page, AdvanceVirtualPool advanceVirtualPool) {
		// 设置分页参数
		advanceVirtualPool.setPage(page);
		// 执行分页查询
		page.setList(advanceVirtualPoolDao.newFindList(advanceVirtualPool));
		return page;
	}
}

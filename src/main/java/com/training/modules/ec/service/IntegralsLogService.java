package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.IntegralLogDao;
import com.training.modules.ec.entity.IntegralLog;


/**
 * 云币Service
 * @author 
 *
 */
@Service
@Transactional(readOnly = false)
public class IntegralsLogService extends CrudService<IntegralLogDao, IntegralLog>{
	
	/**
	 * 分页查询banner图
	 */
	public Page<IntegralLog> findPage(Page<IntegralLog> page,IntegralLog integralLog){
		integralLog.setPage(page);
		page.setList(dao.findList(integralLog));
		return page;
	}
}

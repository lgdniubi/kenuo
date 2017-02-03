package com.training.modules.ec.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.FreeOrderDao;
import com.training.modules.ec.entity.FreeOrder;


/**
 * 免费体验service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class FreeOrderService extends CrudService<FreeOrderDao,FreeOrder>{
	/**
	 * 查询所有免费体检
	 */
	public Page<FreeOrder> findPage(Page<FreeOrder> page, FreeOrder freeOrder) {
		freeOrder.setPage(page);
		page.setList(dao.findAllList(freeOrder));
		return page;
	}
	/**
	 * 查询备注
	 * @param freeOrder
	 * @return
	 */
	public List<FreeOrder> findRemark(FreeOrder freeOrder){
		return dao.findList(freeOrder);
	}
	/**
	 * 保存备注
	 * @param freeOrder
	 */
	public void saveRemark(FreeOrder freeOrder){
		dao.insert(freeOrder);
	}
}

package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainsVersionDao;
import com.training.modules.train.entity.Version;


/**
 * 妃子校版本控制Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainsVersionService extends CrudService<TrainsVersionDao, Version>{
	
	/**
	 * 分页查询
	 */
	public Page<Version> findPage(Page<Version> page,Version version){
		version.setPage(page);
		page.setList(dao.findList(version));
		return page;
	}
}

package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyVersionDao;
import com.training.modules.ec.entity.MtmyVersion;


/**
 * 每天美耶版本控制Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyVersionService extends CrudService<MtmyVersionDao, MtmyVersion>{
	
	/**
	 * 分页查询
	 */
	public Page<MtmyVersion> findPage(Page<MtmyVersion> page,MtmyVersion mtmyVersion){
		mtmyVersion.setPage(page);
		page.setList(dao.findList(mtmyVersion));
		return page;
	}
}

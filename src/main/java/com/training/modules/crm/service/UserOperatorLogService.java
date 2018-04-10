package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.UserOperatorLogDao;
import com.training.modules.crm.entity.UserOperatorLog;


/**    
* kenuo      
* @description：  用户详细信息操作service
* @author：sharp   
* @date：2017年3月9日            
*/
@Service
@Transactional(readOnly = false)
public class UserOperatorLogService extends CrudService<UserOperatorLogDao,UserOperatorLog> {

	
	public Page<UserOperatorLog> findList(Page<UserOperatorLog> page, UserOperatorLog log) {
		log.setPage(page);
		page.setList(dao.findList(log));
		return page;
	}
	
}

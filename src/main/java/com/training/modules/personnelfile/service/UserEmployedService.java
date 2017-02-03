package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.personnelfile.dao.UserEmployedDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserEmployed;

/**
 * 用户-入职情况及联系方式service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserEmployedService extends CrudService<UserEmployedDao, UserEmployed>{
	
	public void saveUserEmployed(PersonnelFile personnelFile){
		UserEmployed userEmployed = personnelFile.getUserEmployed();
		userEmployed.setUserId(personnelFile.getId());
		dao.saveUserEmployed(userEmployed);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

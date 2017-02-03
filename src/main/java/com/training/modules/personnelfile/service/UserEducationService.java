package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.personnelfile.dao.UserEducationDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserEducation;

/**
 * 用户-教育背景service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserEducationService extends CrudService<UserEducationDao, UserEducation>{
	
	public void saveUserEducation(PersonnelFile personnelFile){
		UserEducation userEducation = personnelFile.getUserEducation();
		userEducation.setUserId(personnelFile.getId());
		dao.saveUserEducation(userEducation);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

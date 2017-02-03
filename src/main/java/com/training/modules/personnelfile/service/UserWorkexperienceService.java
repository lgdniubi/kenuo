package com.training.modules.personnelfile.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.personnelfile.dao.UserWorkExperienceDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserWorkExperience;

/**
 * 用户-工作经历service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserWorkexperienceService extends CrudService<UserWorkExperienceDao, UserWorkExperience>{
	
	public void saveUserWorkexperience(PersonnelFile personnelFile){
		List<UserWorkExperience> userWorkExperienceList = new ArrayList<UserWorkExperience>();
		List<UserWorkExperience> userWorkExperiences = personnelFile.getUserWorkExperienceList();
		for (UserWorkExperience userWorkExperience : userWorkExperiences) {
			userWorkExperience.setUserId(personnelFile.getId());
			userWorkExperienceList.add(userWorkExperience);
		}
		dao.saveUserWorkExperienceList(userWorkExperienceList);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

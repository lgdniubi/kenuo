package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.personnelfile.dao.UserDeparturesDao;
import com.training.modules.personnelfile.entity.UserDepartures;

/**
 * 用户-离职情况service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserDeparturesService extends CrudService<UserDeparturesDao, UserDepartures>{
	
	public void saveUserDepartures(UserDepartures userDepartures){
		userDepartures.setId(IdGen.uuid());
		dao.insertSelective(userDepartures);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

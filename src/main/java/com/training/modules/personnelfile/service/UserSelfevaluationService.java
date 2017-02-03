package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.personnelfile.dao.UserSelfevaluationDao;
import com.training.modules.personnelfile.entity.UserSelfevaluation;

/**
 * 用户-基本信息service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserSelfevaluationService extends CrudService<UserSelfevaluationDao, UserSelfevaluation>{
	
	public void saveUserSelfevaluation(UserSelfevaluation userSelfevaluation){
		userSelfevaluation.setId(IdGen.uuid());
		dao.insertSelective(userSelfevaluation);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

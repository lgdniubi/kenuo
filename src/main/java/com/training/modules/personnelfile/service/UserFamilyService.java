package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.personnelfile.dao.UserFamilyDao;
import com.training.modules.personnelfile.entity.UserFamily;

/**
 * 用户-家庭情况service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserFamilyService extends CrudService<UserFamilyDao, UserFamily>{
	
	public void saveUserFamily(UserFamily userFamily){
		userFamily.setId(IdGen.uuid());
		dao.insertSelective(userFamily);
	}
	
	public UserFamily getObject(Integer baseInfoId, Integer num){
		return dao.getObject(baseInfoId, num);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

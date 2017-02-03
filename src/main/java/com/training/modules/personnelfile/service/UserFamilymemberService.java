package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.personnelfile.dao.UserFamilymemberDao;
import com.training.modules.personnelfile.entity.UserFamilymember;

/**
 * 用户-主要家庭成员信息service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserFamilymemberService extends CrudService<UserFamilymemberDao, UserFamilymember>{
	
	public void saveUserFamily(UserFamilymember userFamilymember){
		userFamilymember.setId(IdGen.uuid());
		dao.insertSelective(userFamilymember);
	}

	public UserFamilymember getObject(String id, int i) {
		return dao.getObject(id,i);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

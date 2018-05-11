package com.training.modules.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.crm.dao.UserContactInfoDao;
import com.training.modules.crm.entity.UserContactInfo;

/**    
* kenuo      
* @description 用户联系信息的service   
* @author：sharp   
* @date：2017年3月6日            
*/
@Service
@Transactional(readOnly = false)
public class UserContactInfoService extends CrudService<UserContactInfoDao,UserContactInfo> {
	
	@Autowired
	private UserContactInfoDao dao;
	
	/**
	 * @param UserContactInfo实体类 
	 * @return int
	 * @description 修改用户联系信息
	 */
	public int updateSingle(UserContactInfo entity){
		return dao.updateSingle(entity);
	}
	/**
	 * @param UserContactInfo实体类 
	 * @return int
	 * @description 删除用户联系信息
	 */
	public int delUserContactInfo(UserContactInfo entity){
		return dao.delete(entity);
	}
}

package com.training.modules.personnelfile.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.personnelfile.dao.UserImagesDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserImages;

/**
 * 用户-基本信息service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserImagesService extends CrudService<UserImagesDao, UserImages>{
	
	public void saveUserImages(PersonnelFile personnelFile){
		List<UserImages> imgList = new ArrayList<UserImages>();
		for (UserImages _userImage : personnelFile.getImgList()) {
			if(!"".equals(_userImage.getImgUrl()) && _userImage.getImgUrl() != null){
				_userImage.setUserId(personnelFile.getId());
				_userImage.setCreateDate(new Date());
				imgList.add(_userImage);
			}
		}
		if(imgList.size()>0){
			dao.saveUserImages(imgList);
		}
	}

	public UserImages getImgObject(String id, int i) {
		return dao.getImgObject(id,i);
	}

	public void deleteByUserId(String id) {
		dao.deleteByUserId(id);
	}
}

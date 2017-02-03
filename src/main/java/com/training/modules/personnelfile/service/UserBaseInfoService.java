package com.training.modules.personnelfile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.personnelfile.dao.UserBaseInfoDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserBaseInfo;
import com.training.modules.sys.entity.Area;

/**
 * 用户-基本信息service
 * 
 */
@Service
@Transactional(readOnly = true)
public class UserBaseInfoService extends CrudService<UserBaseInfoDao, UserBaseInfo>{
	
	public void save(PersonnelFile personnelFile){
		UserBaseInfo userBaseInfo = personnelFile.getUserBaseInfo();
		userBaseInfo.setUserId(personnelFile.getId());
		Area oneArea = personnelFile.getOneArea();
		String registerSite = oneArea.getId()+","+oneArea.getName()+","+userBaseInfo.getRegisterSite1();
		Area twoArea = personnelFile.getTwoArea();
		String dwelling = twoArea.getId()+","+twoArea.getName()+","+userBaseInfo.getDwelling1();
		userBaseInfo.setRegisterSite(registerSite);
		userBaseInfo.setDwelling(dwelling);
		userBaseInfo.setSex(personnelFile.getSex());
		dao.saveUserBaseInfo(userBaseInfo);
	}

	/**
	 * 修改基本信息
	 * @param userBaseInfo
	 */
	public void updateUserBaseInfo(UserBaseInfo userBaseInfo) {
		dao.update(userBaseInfo);
	}
	
	/**
	 * 物理删除
	 */
	public void deleteByUserId(String userId){
		dao.deleteByUserId(userId);
	}
}

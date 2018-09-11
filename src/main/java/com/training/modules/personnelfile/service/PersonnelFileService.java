package com.training.modules.personnelfile.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.personnelfile.dao.PersonnelFileDao;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.entity.UserBaseInfo;
import com.training.modules.personnelfile.entity.UserDepartures;
import com.training.modules.personnelfile.entity.UserFamily;
import com.training.modules.personnelfile.entity.UserFamilymember;
import com.training.modules.personnelfile.entity.UserImages;
import com.training.modules.personnelfile.entity.UserSelfevaluation;
import com.training.modules.personnelfile.entity.UserWorkExperience;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.User;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = false)
public class PersonnelFileService extends CrudService<PersonnelFileDao, PersonnelFile>{
	
	@Autowired
	private UserBaseInfoService userBaseInfoService;
	
	@Autowired
	private UserImagesService userImagesService;
	@Autowired
	private UserEmployedService userEmployedService;
	@Autowired
	private UserEducationService userEducationService;
	@Autowired
	private UserWorkexperienceService userWorkexperienceService;
	@Autowired
	private UserFamilyService userFamilyService;
	@Autowired
	private UserFamilymemberService userFamilymemberService;
	@Autowired
	private UserSelfevaluationService userSelfevaluationService;
	@Autowired
	private UserDeparturesService userDeparturesService;
	
	
	/**
	 * 查询档案列表
	 * @param page
	 * @param personnelFile
	 * @return
	 */
	public Page<PersonnelFile> findPersonnelFileAll(Page<PersonnelFile> page, PersonnelFile personnelFile) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		
		//2016-11-4 kele update 新的数据权限
		//user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		if(StringUtils.isEmpty(personnelFile.getOfficeId())){
			personnelFile.getSqlMap().put("dsf", dataScopeFilter(personnelFile.getCurrentUser(),"o"));
			User currentUser = personnelFile.getCurrentUser();
			personnelFile.setOfficeId(currentUser.getCompany().getId());
		}
		// 设置分页参数
		personnelFile.setPage(page);
		// 执行分页查询
		page.setList(dao.findPersonnelFileList(personnelFile));
		return page;
	}
	
	/**
	 * 查询添加页面需要回带的数据
	 * @param findPortionInfo
	 * @return
	 */
	public PersonnelFile findPortionInfo(PersonnelFile personnelFile){
		return dao.findPortionInfo(personnelFile);
	}
	
	/**
	 * 保存人事档案-处理业务
	 * @param personnelFile
	 * @param request
	 */
	@Transactional(readOnly = false)
	public void savePersonnelFile(PersonnelFile personnelFile, HttpServletRequest request){
		String id = personnelFile.getId();
		//保存基本信息表
		userBaseInfoService.save(personnelFile);
		//保存用户图片
		userImagesService.saveUserImages(personnelFile);
		//保存入职情况及联系方式
		userEmployedService.saveUserEmployed(personnelFile);
		//保存教育背景
		userEducationService.saveUserEducation(personnelFile);
		//保存工作经历
		userWorkexperienceService.saveUserWorkexperience(personnelFile);
		//保存家庭情况
		for(UserFamily userFamily : personnelFile.getUserFamilies()){
			userFamily.setUserId(personnelFile.getId());
			userFamilyService.saveUserFamily(userFamily);
		}
		//家庭主要成员信息
		for (UserFamilymember userFamilymember : personnelFile.getUserFamilymembers()) {
			userFamilymember.setUserId(id);
			userFamilymemberService.saveUserFamily(userFamilymember);
		}
		//个人评价
		UserSelfevaluation userSelfevaluation = personnelFile.getUserSelfevaluation();
		userSelfevaluation.setUserId(personnelFile.getId());
		userSelfevaluationService.saveUserSelfevaluation(userSelfevaluation);
		//修改sys——user表中得照片字段和是否建档字段
		dao.updateUserInfo(personnelFile);
		//离职情况
//		UserDepartures userDepartures = personnelFile.getUserDepartures();
//		userDepartures.setUserId(personnelFile.getId());
//		userDeparturesService.saveUserDepartures(userDepartures);
	}
	/**
	 * 返回修改页面和查看页面方法
	 * @param personnelFile
	 * @return
	 */

	public PersonnelFile getPersonnelFileBefor(PersonnelFile personnelFile) {
		//获得PersonnelFile中所有不是集合属性的关联查询
		PersonnelFile _personnelFile = dao.getPersonnelFileBefor(personnelFile);
		 UserBaseInfo userBaseInfo = _personnelFile.getUserBaseInfo();
		if(StringUtils.isNotEmpty(userBaseInfo.getDwelling())){
			String[] dwellings = userBaseInfo.getDwelling().split(",");
			Area twoArea = new Area();
			twoArea.setId(dwellings[0]);
			twoArea.setName(dwellings[1]);
			_personnelFile.setTwoArea(twoArea);
			_personnelFile.setRegisterSite1(dwellings[2]);
		}
		if(StringUtils.isNotEmpty(userBaseInfo.getRegisterSite())){
			String[] registerSites = userBaseInfo.getRegisterSite().split(",");
			Area oneArea = new Area();
			oneArea.setId(registerSites[0]);
			oneArea.setName(registerSites[1]);
			_personnelFile.setOneArea(oneArea);
			_personnelFile.setDwelling1(registerSites[2]);
		}
		//获得图片对象
//		UserImages userImage1 =userImagesService.getImgObject(personnelFile.getId(),1);
//		UserImages userImage2 =userImagesService.getImgObject(personnelFile.getId(),2);
//		UserImages userImage3 =userImagesService.getImgObject(personnelFile.getId(),3);
//		UserImages userImage4 =userImagesService.getImgObject(personnelFile.getId(),4);
//		UserImages userImage5 =userImagesService.getImgObject(personnelFile.getId(),5);
//		_personnelFile.setUserImage1(userImage1);
//		_personnelFile.setUserImage2(userImage2);
//		_personnelFile.setUserImage3(userImage3);
//		_personnelFile.setUserImage4(userImage4);
//		_personnelFile.setUserImage5(userImage5);
		//获得工作经历集合
		UserWorkExperience userWorkExperience = new UserWorkExperience();
		userWorkExperience.setUserId(personnelFile.getId());
		userWorkExperience.setBaseInfoId(personnelFile.getBaseInfoId());
		List<UserWorkExperience> userWorkExperienceList = userWorkexperienceService.findList(userWorkExperience);
		for (UserWorkExperience _userWorkExperience : userWorkExperienceList) {
			if(1 == _userWorkExperience.getWorkType()){
				_personnelFile.setOneUserWorkExperience(_userWorkExperience);
			}else if(2 == _userWorkExperience.getWorkType()){
				_personnelFile.setTwoUserWorkExperience(_userWorkExperience);
			}
		}
		//获得家庭情况
		UserFamily father = userFamilyService.getObject(personnelFile.getBaseInfoId(),1);
		UserFamily mother = userFamilyService.getObject(personnelFile.getBaseInfoId(),2);
		_personnelFile.setFather(father);
		_personnelFile.setMother(mother);
		//主要家庭成员
		UserFamilymember oneUser = userFamilymemberService.getObject(personnelFile.getBaseInfoId(),1);
		UserFamilymember twoUser = userFamilymemberService.getObject(personnelFile.getBaseInfoId(),2);
		UserFamilymember threeUser = userFamilymemberService.getObject(personnelFile.getBaseInfoId(),3);
		_personnelFile.setOneUser(oneUser);
		_personnelFile.setTwoUser(twoUser);
		_personnelFile.setThreeUser(threeUser);
		return _personnelFile;
	}

	/**
	 * 修改人事档案 逻辑先删除所有用户得档案数据，重新添加
	 * @param personnelFile
	 */
	public void updatePersonnelFile(PersonnelFile personnelFile) {
		String id = personnelFile.getId();
		//基本信息
		userBaseInfoService.deleteByUserId(id);
		userBaseInfoService.save(personnelFile);
		//用户-图片
		userImagesService.deleteByUserId(id);
		userImagesService.saveUserImages(personnelFile);
		//入职情况及联系方式
		userEmployedService.deleteByUserId(id);
		userEmployedService.saveUserEmployed(personnelFile);
		//教育背景
		userEducationService.deleteByUserId(id);
		userEducationService.saveUserEducation(personnelFile);
		//工作经历
		userWorkexperienceService.deleteByUserId(id);
		userWorkexperienceService.saveUserWorkexperience(personnelFile);
		//家庭情况
		userFamilyService.deleteByUserId(id);
		for(UserFamily userFamily : personnelFile.getUserFamilies()){
			userFamily.setUserId(id);
			userFamilyService.saveUserFamily(userFamily);
		}
		//主要家庭成员信息
		userFamilymemberService.deleteByUserId(id);
		for (UserFamilymember userFamilymember : personnelFile.getUserFamilymembers()) {
			userFamilymember.setUserId(id);
			userFamilymemberService.saveUserFamily(userFamilymember);
		}
		//个人评价
		userSelfevaluationService.deleteByUserId(id);
		UserSelfevaluation userSelfevaluation = personnelFile.getUserSelfevaluation();
		userSelfevaluation.setUserId(id);
		userSelfevaluationService.saveUserSelfevaluation(userSelfevaluation);
		//离职情况
		UserDepartures userDepartures = personnelFile.getUserDepartures();
		if(0 == userDepartures.getIsleaveoffice()){//离职
			userDeparturesService.deleteByUserId(id);
			userDepartures.setUserId(id);
			userDeparturesService.saveUserDepartures(userDepartures);
			personnelFile.setDelFlag("1");//用户表逻辑删除
		}else if(1 == userDepartures.getIsleaveoffice()){//不离职
			UserDepartures _userDepartures = new UserDepartures();
			_userDepartures.setUserId(id);
			_userDepartures.setIsleaveoffice(userDepartures.getIsleaveoffice());
			userDeparturesService.deleteByUserId(id);
			userDeparturesService.saveUserDepartures(_userDepartures);
			personnelFile.setDelFlag("0");
		}
		
		//修改sys——user表中得照片字段和是否建档字段
		dao.updateUserInfo(personnelFile);
	}

}

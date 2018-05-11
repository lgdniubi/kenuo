package com.training.modules.train.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.FzxRoleDao;
import com.training.modules.train.dao.PcRoleDao;
import com.training.modules.train.dao.UserCheckDao;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.PcRole;
import com.training.modules.train.entity.UserCheck;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *  用户审核Service
 * @author 
 * @version 2018年3月14日
 */
@Service
@Transactional(readOnly = false)
public class UserCheckService extends CrudService<UserCheckDao,UserCheck> {

	@Autowired
	private UserCheckDao userCheckDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private PcRoleDao pcRoleDao;
	@Autowired
	private FzxRoleDao fzxRoleDao;
	
	/**
	 * 保存用户审核的结果
	 * @param userCheck
	 */
	public void saveModel(UserCheck userCheck) {
		if (StringUtils.isNotEmpty(userCheck.getId())) {
			userCheckDao.editUserCheck(userCheck);
		}
	}

	/**
	 * 根据id查询单条版本记录
	 * @param trainModel
	 * @return
	 */
	public UserCheck getUserCheck(UserCheck userCheck) {
		return userCheckDao.getUserCheck(userCheck);
	}

	/**
	 * 分页查询审核信息记录
	 * @param page
	 * @param userCheck
	 * @return
	 */
	public Page<UserCheck> findList(Page<UserCheck> page, UserCheck userCheck) {
		// 设置分页参数
		userCheck.setPage(page);
		// 执行分页查询
		page.setList(userCheckDao.findList(userCheck));
		return page;
	}

	/**
	 * 保存修改的权限设置信息
	 * 保存手艺人的权益信息。
	 * @param modelFranchisee
	 */
	public void saveModelFranchisee(ModelFranchisee modelFranchisee) {
		//更改用户表type为手艺人类型
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			deleteOldFzxRoleAndOffice(modelFranchisee.getUserid());
			userCheckDao.updateUserType("syr",modelFranchisee.getUserid(),"999999");
			//为改手艺人设置角色，从fzx_role查询mod_id为syr的roleid插入fzx_user_role
			setSYRroleForUser(modelFranchisee);
		}else{
			//编辑的时候先删除超级管理员角色和公共的角色-----再重新设置新的版本的角色
			ModelFranchisee franchisee = getModelFranchiseeByUserid(modelFranchisee.getUserid());
			if(!franchisee.getModid().equals(modelFranchisee.getModid())){	//如果版本更换才重新设置
				deleteOldFzxRoleAndOffice(modelFranchisee.getUserid());	//不仅删除用户的fzx角色，还删除该角色对应的菜单
//				deleteFZXRolesForUser(modelFranchisee.getUserid());
				//为改手艺人设置角色，从fzx_role查询mod_id为syr的roleid插入fzx_user_role
				setSYRroleForUser(modelFranchisee);
			}
		}
		save(modelFranchisee);
	}

	//手艺人权益变更的时候删除以前的角色
//	private void deleteFZXRolesForUser(String userid) {
//		userCheckDao.deleteFzxUserRole(userid);
//	}

	private void setSYRroleForUser(ModelFranchisee modelFranchisee) {
		//从fzx_role查询mod_id为syr的roleid插入fzx_user_role
		userCheckDao.insertFzxUserRoleForsyr(modelFranchisee);
		int userRoleId = modelFranchisee.getUserRoleId();
		//向fzx_user_role_office插入一条数据===999999是数据库中的虚拟的商家id
		userCheckDao.insertFzxUserRoleOffice(userRoleId,"999999");
	}

	/**
	 * 保存企业和手艺人的权益信息
	 * @param modelFranchisee
	 */
	private void save(ModelFranchisee modelFranchisee) {
		UserCheck ck = new UserCheck();
		ck.setUserid(modelFranchisee.getUserid());
		ck.setId(modelFranchisee.getApplyid());
		ck.setStatus("3");//更改状态已授权
		userCheckDao.editUserCheck(ck );
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			modelFranchisee.preInsert();
			userCheckDao.saveModelFranchisee(modelFranchisee);
		}else{
			modelFranchisee.preUpdate();
			userCheckDao.editModelFranchisee(modelFranchisee);
		}
	}

	/**
	 * 根据userID查询权限设置信息
	 * @param userid
	 * @return
	 */
	public ModelFranchisee getModelFranchiseeByUserid(String userid) {
		return userCheckDao.getModelFranchiseeByUserid(userid);
	}

	/**
	 * 保存企业的权益信息。
	 * 保存修改企业的权限设置信息
	 * @param modelFranchisee
	 * 在授权企业的时候去把train_franchisee_info(企业审核信息)中的企业信息入库到sys_franchisee和sys_office中，然后去授权在去更改sys_user表中此用户的归属商家机构等信息
	 */
	public void saveQYModelFranchisee(ModelFranchisee modelFranchisee,UserCheck find) {
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			//向商家和机构插入一条信息
			String franchid = saveFranchiseeAndOffice(find);
			
			//设置该用户的超级管理员
			setSuperAdminForUserid(modelFranchisee,franchid);
			//设置公共的角色
			setRoleForUser(modelFranchisee,franchid);
			
			modelFranchisee.setFranchiseeid(franchid);
			userCheckDao.updateUserType("qy",modelFranchisee.getUserid(),franchid);//更改用户表type为企业类型
			modelFranchisee.setUserid("0");
		}else{
			//编辑的时候先删除超级管理员角色和公共的角色-----再重新设置新的版本的角色
			ModelFranchisee franchisee = getQYModelFranchiseeByUserid(modelFranchisee.getUserid());
			if(!franchisee.getModid().equals(modelFranchisee.getModid())){	//如果版本更换才重新设置
				deleteAllRolesForUser(modelFranchisee.getUserid(),franchisee.getFranchiseeid());
				//设置该用户的超级管理员
				setSuperAdminForUserid(modelFranchisee,franchisee.getFranchiseeid());
				//设置公共的角色
				setRoleForUser(modelFranchisee,franchisee.getFranchiseeid());
			}
		}
		updateInvitationAndPush(find);	//向邀请表和推送消息表更改数据，把所有推送消息设置为未通过，邀请记录：没同意的设置为3会员拒绝，同意的设置为2商家拒绝。
		save(modelFranchisee);
//		int a = 1/0;
	}

	/**
	 * 向邀请表和推送消息表更改数据，把所有推送消息设置为0:不可操作，邀请记录：没同意的设置为3会员拒绝，同意的设置为2商家拒绝。
	 * @param find
	 * @Description:
	 */
	private void updateInvitationAndPush(UserCheck find) {
		userCheckDao.updateInvitationByShop(find.getMobile());		//同意的设置为2商家拒绝。
		userCheckDao.updateInvitationByUser(find.getMobile());		//没同意的设置为3会员拒绝
		userCheckDao.updatePushByUser(find.getUserid());		//把所有推送消息设置为0:不可操作
	}

	/**
	 * @param modelFranchisee
	 * @Description: 更改权益设置，版本变更--先删除公共角色和管理员角色
	 */
	private void deleteAllRolesForUser(String userid,String franchiseeid) {
		userCheckDao.deletePcUserRole(userid);
		userCheckDao.deleteFzxUserRole(userid);
		List<PcRole> prList= userCheckDao.findAllPcCommonRoleIds(franchiseeid);
		if(prList != null && prList.size()>0){
			userCheckDao.deleteAllPcMenu(prList);
		}
		userCheckDao.deletePcCommonRole(franchiseeid);
		List<FzxRole> fzxList= userCheckDao.findAllFzxCommonRoleIds(franchiseeid);
		if(fzxList != null && fzxList.size()>0){
			userCheckDao.deleteAllFzxMenu(fzxList);
		}
		userCheckDao.deleteFzxCommonRole(franchiseeid);
//		userCheckDao.deleteFzxUserRoleOffice(franchiseeid);
	}

	/**
	 * 为该用户设置一些公共的角色
	 * @param modelFranchisee
	 * @param franchid
	 */
	private void setRoleForUser(ModelFranchisee modelFranchisee, String franchid) {
		List<PcRole> pcroleList = userCheckDao.findPcRoleByModid(modelFranchisee);
		for (PcRole pcrole : pcroleList) {
			String roleid = pcrole.getId();
			pcrole.setFranchiseeid(Integer.valueOf(franchid));
			pcrole.setOfficeid(franchid);
			pcrole.setModeid(Integer.valueOf(modelFranchisee.getModid()));
			pcrole.preInsert();
			userCheckDao.insertPcCommonRole(pcrole);
			//插入公共角色后创建公共权限
			setPcCommonMenuByRoleId(roleid,pcrole.getId());
		}
		
		//向妃子校角色表插入数据
		List<FzxRole> fzxRoleList = userCheckDao.findFzxRoleByModid(modelFranchisee);
		for (FzxRole fzxRole : fzxRoleList) {
			String roleid = fzxRole.getId();
			fzxRole.setFranchiseeid(Integer.valueOf(franchid));
			fzxRole.setOfficeid(franchid);
			fzxRole.setModeid(Integer.valueOf(modelFranchisee.getModid()));
			fzxRole.preInsert();
			userCheckDao.insertFzxCommonRole(fzxRole);
			//插入公共角色后创建公共权限
			setFzxCommonMenuByRoleId(roleid,fzxRole.getId());
		}
	}

	/**
	 * @param id
	 * @Description:查询该角色的全部菜单，插入新的角色id
	 */
	private void setPcCommonMenuByRoleId(String roleid ,String newRoleId) {
		List<PcRole> rmList = userCheckDao.finAllPcMenuByRoleId(roleid);
		for (PcRole pcRoleFind : rmList) {
			pcRoleFind.setRoleId(Integer.valueOf(newRoleId));
			pcRoleDao.insertRoleMenu(pcRoleFind);
		}
	}
	/**
	 * @param id
	 * @Description:查询该角色的全部菜单，插入新的角色id
	 */
	private void setFzxCommonMenuByRoleId(String roleid ,String newRoleId) {
		List<FzxRole> rmList = userCheckDao.finAllFzxMenuByRoleId(roleid);
		for (FzxRole fzxRoleFind : rmList) {
			fzxRoleFind.setRoleId(Integer.valueOf(newRoleId));
			fzxRoleDao.insertRoleMenu(fzxRoleFind);
		}
	}

	/**
	 * 设置该用户的超级管理员
	 * @param userid
	 */
	private void setSuperAdminForUserid(ModelFranchisee modelFranchisee,String franchid) {
		//从pc_role查出超级管理员角色记录插入pc_user_role
		int pc_roleid = userCheckDao.findByModidAndEname(modelFranchisee);
		//向pc_user_role中插入一条记录
		userCheckDao.insertPcUserRole(modelFranchisee.getUserid(),pc_roleid);
		
		//删除该用户以前的角色fzx_user_role和角色机构信息fzx_user_role_office
		deleteOldFzxRoleAndOffice(modelFranchisee.getUserid());
		//从fzx_role查出超级管理员角色记录插入fzx_user_role
		int fzx_roleid = userCheckDao.findByModidAndEnameFzx(modelFranchisee);
		//向pc_user_role中插入一条记录
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fzxUserRoleId", 0);
		map.put("userid", modelFranchisee.getUserid());
		map.put("roleid", fzx_roleid);
		userCheckDao.insertFzxUserRole(map);
		int fzxUserRoleId = (int) map.get("fzxUserRoleId");
		
		//向fzx_user_role_office插入一条数据
		userCheckDao.insertFzxUserRoleOffice(fzxUserRoleId,franchid);
		
	}

	/**
	 * 删除该用户以前的角色fzx_user_role和角色机构信息fzx_user_role_office
	 * @param userid
	 * @Description:
	 */
	private void deleteOldFzxRoleAndOffice(String userid) {
		userCheckDao.deleteOldFzxRoleOffice(userid);
		userCheckDao.deleteOldFzxRole(userid);
	}

	private String saveFranchiseeAndOffice(UserCheck find) {
		find.preInsert();
		//id为null或者"" 时，则为添加下级菜单时，code自增
		Long code = userCheckDao.findMaxCode();
//		if(code==null){
//			find.setCode(StringUtils.leftPad("1", 4, "0"));
//		}
		find.setCode(String.valueOf(code+1));
		userCheckDao.saveMtmyFranchisee(find);	//保存每天美耶的商家
		userCheckDao.saveFranchisee(find);	//保存平台的商家
		String id = find.getId();
		saveOffice(id,find);
		return id ;
	}

	
	private void saveOffice(String id,UserCheck find) {
		//保存组织机构信息，给组织机构添加一条父类数据
		Office office = new Office();
		office.setId(id);//id
		Office parent = new Office();
		parent.setId("1");
		office.setParent(parent);
		office.setParentIds("0,1,");
		office.setName(find.getCompanyName());
		office.setArea(new Area(find.getDistrictId()));
		office.setSort(10);
		office.setCode(find.getCode());
		office.setType("1");
		office.setGrade("2");
		office.setUseable("1");
		
		office.setCreateDate(new Date());
		office.setUpdateDate(new Date());
		office.setDelFlag("0");
		office.setRemarks("用户审核插入");
		office.setAddress(find.getAddress());
		office.setPhone(find.getMobile());
		office.setMaster(find.getName());
//		office.setIconUrl(franchisee.getIconUrl());
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			office.setUpdateBy(user);
			office.setCreateBy(user);
		}
		officeService.franchiseeSaveOffice(office);//保存组织结构表
	}

	/**
	 * 获取企业的权益信息
	 * @param userid
	 * @return
	 */
	public ModelFranchisee getQYModelFranchiseeByUserid(String userid) {
		return userCheckDao.getQYModelFranchiseeByUserid(userid);
	}

	/**
	 * 审核成功后推送消息
	 * @param userCheck
	 * @return
	 */
	public String pushMsg(UserCheck userCheck,String text) {
		JSONArray jsonArray = new JSONArray();
		String cid = userCheckDao.findCidByUserid(userCheck.getUserid());
//		jsonArray.add("35be8ac9632c9475ac67f9be3c340665");
		jsonArray.add(cid);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cid_list", jsonArray);
		jsonObj.put("push_type", 1);
		Map<String, Object> map = new HashMap<String, Object>();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		map.put("content", text);
		map.put("notify_id", UUID.randomUUID().toString().replace("-", ""));
		map.put("notify_type", 2);
		map.put("push_time", dateStr);
		map.put("title", "审核通知");
		jsonObj.put("content", map);
		
		String json =  this.push(jsonObj);
		return json;
	}
	/*
	 * 推送消息具体方法
	 */
	private String push(JSONObject jsonObj){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		//  用于加密
		String sign = WebUtils.MD5("train"+StringEscapeUtils.unescapeJava(jsonObj.toString())+"train");
		String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+jsonObj+"'}";
		
		HttpEntity<String> entity = new HttpEntity<String>(paramter,headers);
		
		String push_url = ParametersFactory.getMtmyParamValues("push_url");
//		String push_url = "http://10.10.8.22:8801/appServer/pushMsg/pushGT.do";
		
		String json = restTemplate.postForObject(push_url, entity, String.class);
//		System.out.println("pushjsonStr == "+jsonObj);
//		System.out.println(json);
		return json;
	}

	/**
	 * 用户通过审核后发消息，并更改用户成为认证类型。
	 * @param userCheck
	 * @param text
	 * @Description:
	 */
	public void updateTypeAndpushMsg(UserCheck userCheck, String text) {
		UserCheck userfind = userCheckDao.get(userCheck.getId());
		userCheckDao.updateUserType(userfind.getAuditType(),userCheck.getUserid(),null);//更改用户表type为企业类型
		pushMsg(userCheck, text);
	}
}

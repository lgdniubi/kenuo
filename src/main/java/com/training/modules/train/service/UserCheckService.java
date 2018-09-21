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

import com.training.common.Thread.ClearTokenThread;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.DateUtils;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.FzxRoleDao;
import com.training.modules.train.dao.MediaRoleDao;
import com.training.modules.train.dao.PcRoleDao;
import com.training.modules.train.dao.ProtocolModelDao;
import com.training.modules.train.dao.UserCheckDao;
import com.training.modules.train.entity.BankAccount;
import com.training.modules.train.entity.CheckAddr;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.entity.MediaRole;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.PayAccount;
import com.training.modules.train.entity.PcRole;
import com.training.modules.train.entity.SyrFranchise;
import com.training.modules.train.entity.TrainModel;
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
	@Autowired
	private PcRoleService pcRoleService;
	@Autowired
	private FzxRoleService fzxRoleService;
	@Autowired
	private MediaRoleService mediaRoleService;
	@Autowired
	private MediaRoleDao mediaRoleDao;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private TrainModelService trainModelService;
	@Autowired
	private ProtocolModelDao protocolModelDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 保存用户审核的结果
	 * @param userCheck
	 */
	public void saveModel(UserCheck userCheck) {
		if (StringUtils.isNotEmpty(userCheck.getId())) {
			userCheckDao.editUserCheck(userCheck);
		}
		if ("1".equals(userCheck.getStatus())){
			//审核通过不操作签过的协议，不通过根据userid和typeid删除协议
			String typeId = "syr".equals(userCheck.getAuditType())? "2":"3";
			userCheckDao.deleteProtocolShop(userCheck.getUserid(),typeId);
			
		}
	}

	/**
	 * 根据id查询单条版本记录
	 * @param trainModel
	 * @return
	 */
	public UserCheck getUserCheck(UserCheck userCheck) {
		UserCheck findCheck = userCheckDao.getUserCheck(userCheck);
		//查找银行卡信息
		List<BankAccount> accounts= userCheckDao.findBankAccountInfo(userCheck.getId());
		//查找支付宝微信信息
		if("syr".equals(findCheck.getAuditType())){//如果认证类型是手艺人，
			List<PayAccount> payAccounts= userCheckDao.findPayAccountInfo(userCheck);
			findCheck.setPayAccount(payAccounts);
		}
		findCheck.setBankAccount(accounts);
		userCheck.setAuditType(findCheck.getAuditType());
		CheckAddr checkAddr= userCheckDao.findCheckAddr(userCheck);
		findCheck.setAddr(checkAddr);
		return findCheck;
	}
	public UserCheck getUserCheckInfo(UserCheck userCheck) {
		UserCheck findCheck = userCheckDao.getUserCheck(userCheck);
		return findCheck;
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
			insertUserCompany(modelFranchisee.getUserid(),"999999");
			//授权通过后，改变认证时签的协议的状态为1履约中
			updateProtocolShopStatus(modelFranchisee.getUserid(),2);//2是手艺人认证
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
			//权益期限修改更改用户菜单状态
			if((DateUtils.formatDate(modelFranchisee.getAuthEndDate(), "yyyy-MM-dd").compareTo(DateUtils.getDate()))>=0){
				modelFranchisee.setStatus("0");
				updateUserMenu("0",modelFranchisee.getUserid());
			}
		}
		save(modelFranchisee);
		updateApplyStatus(modelFranchisee);
	}

	//手艺人权益变更的时候删除以前的角色
//	private void deleteFZXRolesForUser(String userid) {
//		userCheckDao.deleteFzxUserRole(userid);
//	}
	//授权通过后，改变认证时签的协议的状态为1履约中
	private void updateProtocolShopStatus(String userid, int typeId) {
		userCheckDao.updateProtocolShopStatus(userid, typeId);
	}

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
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			modelFranchisee.preInsert();
			userCheckDao.saveModelFranchisee(modelFranchisee);
		}else{
			modelFranchisee.preUpdate();
			userCheckDao.editModelFranchisee(modelFranchisee);
		}
		//保存操作日志
//		User user = UserUtils.getUser();
//		modelFranchisee.setCreateBy(user);
//		modelFranchisee.setCreateDate(new Date());
		//userCheckDao.saveLogModel(modelFranchisee);
	}
	//权益期限修改更改用户菜单状态
	private void updateUserMenu(String franchiseeid,String userid) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("franchisee_id", Integer.valueOf(franchiseeid));
		map.put("user_id", userid);
		map.put("status1", 1);
		map.put("status2", 0);
		map.put("update_user", UserUtils.getUser().getId());
		authenticationService.updatestatus(map);
	}

	//更改授权状态为已授权
	private void updateApplyStatus(ModelFranchisee modelFranchisee) {
		UserCheck ck = new UserCheck();
		ck.setUserid(modelFranchisee.getUserid());
		ck.setId(modelFranchisee.getApplyid());
		ck.setStatus("3");//更改状态已授权
		userCheckDao.editUserCheck(ck );
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
			
			//设置该用户的超级管理员	2018-06-01改为复制所有角色为该用户创建复制后 的sjgly角色
//			setSuperAdminForUserid(modelFranchisee,franchid);
			//设置公共的角色
			setRoleForUser(modelFranchisee,franchid);
			
			//向sys_user_company插入一条数据--问答
			insertUserCompany(modelFranchisee.getUserid(),franchid);
			//授权通过后，改变认证时签的协议的状态为1履约中
			updateProtocolShopStatus(modelFranchisee.getUserid(),3);//3是企业认证
			modelFranchisee.setFranchiseeid(franchid);
			userCheckDao.updateUserType("qy",modelFranchisee.getUserid(),franchid);//更改用户表type为企业类型
			modelFranchisee.setUserid("0");
		}else{
			//编辑的时候先删除超级管理员角色和公共的角色-----再重新设置新的版本的角色
			ModelFranchisee franchisee = getQYModelFranchiseeByUserid(modelFranchisee.getUserid());
			if(!franchisee.getModid().equals(modelFranchisee.getModid())){	//如果版本更换才重新设置
//				deleteAllRolesForUser(modelFranchisee.getUserid(),franchisee.getFranchiseeid());
				//设置该用户的超级管理员
//				setSuperAdminForUserid(modelFranchisee,franchisee.getFranchiseeid());
				//更改超级管理员版本菜单
				updateSuperAdminMenu(modelFranchisee);
				//设置公共的角色
//				setRoleForUser(modelFranchisee,franchisee.getFranchiseeid());
				//变更改商家角色版本id
				updateAllRoleModelId(franchisee.getFranchiseeid(),modelFranchisee.getModid());
			}
			//权益期限修改更改用户菜单状态
			if((DateUtils.formatDate(modelFranchisee.getAuthEndDate(), "yyyy-MM-dd").compareTo(DateUtils.getDate()))>=0
					&& DateUtils.formatDate(modelFranchisee.getAuthEndDate(), "yyyy-MM-dd").compareTo(DateUtils.formatDate(franchisee.getAuthEndDate(), "yyyy-MM-dd"))>0){
				modelFranchisee.setStatus("0");
				updateUserMenu(modelFranchisee.getFranchiseeid(),"0");
			}
			//权益修改后如果支付方式改变就清除支付方式，重新签约--改变签约状态
//			clearPayInfoAndChangeStatus(franchisee,modelFranchisee);	2018-09-21熊猫修改不重签
		}
		updateInvitationAndPush(find);	//向邀请表和推送消息表更改数据，把所有推送消息设置为未通过，邀请记录：没同意的设置为3会员拒绝，同意的设置为2商家拒绝。
		save(modelFranchisee);
		modelFranchisee.setUserid(find.getUserid());
		updateApplyStatus(modelFranchisee);
		UserUtils.removeCache("officeList");//清除用户机构缓存，认证通过之后能在机构管理看见
		clearUserToken(find.getCompanyId());//根据商家id查出所有用户id
//		int a = 1/0;
	}
	
	private void clearUserToken(String companyId) {
		List<String> uids = this.userDao.findUidByCompanyId(companyId);
		//删除菜单查找使用这菜单的所有用户清除token
		if(uids != null && uids.size() > 0){
			ClearTokenThread thread = new ClearTokenThread(uids);
			new Thread(thread).start();
		}
		
	}

	//权益修改后如果支付方式改变就清除支付方式，重新签约--改变签约状态
	private void clearPayInfoAndChangeStatus(ModelFranchisee findFranchisee, ModelFranchisee modelFranchisee) {
		String parpm1 = "{\"franchisee_id\":"+Integer.valueOf(findFranchisee.getFranchiseeid())+"}";
		if(!findFranchisee.getPaytype().equals(modelFranchisee.getPaytype())){
			postCSData(parpm1, "clearPayInfoOfFranchisee");
		}
		String parpm2 = "{\"franchisee_id\":"+Integer.valueOf(findFranchisee.getFranchiseeid())+",\"update_user\":\""+String.valueOf(UserUtils.getUser().getId())+"\"}";
		postCSData(parpm2, "resign");
		protocolModelDao.deleteProtocolShopById(Integer.valueOf(findFranchisee.getFranchiseeid()));
	}
	private void postCSData(String parpm, String key) {
		String url = ParametersFactory.getTrainsParamValues(key);
		logger.info("##### web接口路径trains:"+url);
//		String parpm = "{\"id\":"+Integer.valueOf(find.getId())+",\"name\":\""+find.getCompanyName()+"\",\"type\":\""+find.getAddr().getType()+"\","
//				+ "\"address\":\""+find.getAddress()+"\",\"legal_name\":\""+find.getLegalPerson()+"\",\"mobile\":\""+find.getMobile()
//						+"\",\"charter_url\":\""+find.getCharterUrl()+"\"}";
		logger.info("##### 打印参数param:"+parpm);
		String result = WebUtils.postCSObject(parpm, url);
		JSONObject jsonObject = JSONObject.fromObject(result);
		logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
		if(!"200".equals(jsonObject.get("result"))){
			throw new RuntimeException("用户认证--授权同步供应链数据出错");
		}
	}

	///向sys_user_company插入一条数据--问答
	private void insertUserCompany(String userid, String franchseeid) {
		userCheckDao.updateUserCompany(userid, franchseeid);
	}

	//更改超级管理员版本菜单
	private void updateSuperAdminMenu(ModelFranchisee modelFranchisee) {
		FzxRole fzxrole = userCheckDao.findFzxRoleByUserId(modelFranchisee.getUserid());
		PcRole pcrole = userCheckDao.findPcRoleByUserId(modelFranchisee.getUserid());
		MediaRole mdrole = userCheckDao.findMdRoleByUserId(modelFranchisee.getUserid());
		TrainModel trainModel = new TrainModel();
		trainModel.setId(modelFranchisee.getModid());
		TrainModel pcMenu = trainModelService.findmodpcMenu(trainModel );
		TrainModel mediaMenu = trainModelService.findmodMediaMenu(trainModel);
		TrainModel fzxMenu = trainModelService.findmodfzxMenu(trainModel);
		pcrole.setMenuIds(pcMenu.getMenuIds());
		fzxrole.setMenuIds(fzxMenu.getMenuIds());
		mdrole.setMenuIds(mediaMenu.getMenuIds());
		pcRoleService.saveRoleMenu(pcrole);
		fzxRoleService.saveRoleMenu(fzxrole);
		mediaRoleService.saveRoleMenu(mdrole);
	}

	//版本升级的时候变更改商家角色版本id
	private void updateAllRoleModelId(String franchiseeid, String modid) {
		//更改pcrole，该商家的角色版本
		userCheckDao.updatePcRoleModelId(franchiseeid, modid);
		//更改fzxrole，该商家的角色版本
		userCheckDao.updateFzxRoleModelId(franchiseeid, modid);
		//更改media_manage_role，该商家的角色版本
		userCheckDao.updateMediaRoleModelId(franchiseeid, modid);
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
		mediaRoleService.deleteUserRole(userid);
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
			pcrole.setModeid(Integer.valueOf(modelFranchisee.getModid()));
			pcrole.preInsert();
			userCheckDao.insertPcCommonRole(pcrole);
			//插入公共角色后创建公共权限
			setPcCommonMenuByRoleId(roleid,pcrole.getId());
			if ("sjgly".equals(pcrole.getEname())){
				//设置该用户的超级管理员
				setSuperAdminForUserid(modelFranchisee,franchid,pcrole.getId(),1);
			}
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
			if ("sjgly".equals(fzxRole.getEnname())){
				//设置该用户的超级管理员
				setSuperAdminForUserid(modelFranchisee,franchid,fzxRole.getId(),2);
			}
		}
		//赋予认证这个人自媒体超级管理员角色---先根据版本查出超管，再插入
		MediaRole mediaRole = mediaRoleService.getMediaRoleByModAndEname(modelFranchisee.getModid());
		MediaRole findRoleMenu = mediaRoleService.findRoleMenu(mediaRole);
		mediaRole.setRoleId(0);
		mediaRole.setFranchiseeid(Integer.valueOf(franchid));
		mediaRole.preInsert();
		mediaRoleDao.insert(mediaRole);
//		mediaRoleService.savemediaRole(mediaRole);
		findRoleMenu.setRoleId(mediaRole.getRoleId());
		mediaRoleService.saveRoleMenu(findRoleMenu);
		mediaRoleService.insertUserRole(modelFranchisee.getUserid(),mediaRole.getRoleId());
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
	 * @param roleid 
	 * @param userid
	 */
	private void setSuperAdminForUserid(ModelFranchisee modelFranchisee,String franchid,String roleid, Integer sw) {
		switch(sw){
		case 1:	//向pc_user_role中插入一条记录
			userCheckDao.insertPcUserRole(modelFranchisee.getUserid(),Integer.valueOf(roleid));
		    break;
		case 2://向pc_user_role中插入一条记录
			deleteOldFzxRoleAndOffice(modelFranchisee.getUserid());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("fzxUserRoleId", 0);
			map.put("userid", modelFranchisee.getUserid());
			map.put("roleid", roleid);
			userCheckDao.insertFzxUserRole(map);
			int fzxUserRoleId = (int) map.get("fzxUserRoleId");
			//向fzx_user_role_office插入一条数据
			userCheckDao.insertFzxUserRoleOffice(fzxUserRoleId,franchid);
		    break;
		default:
		    break;
		}
	}
/*	private void setSuperAdminForUserid(ModelFranchisee modelFranchisee,String franchid) {
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
		
		//赋予认证这个人自媒体超级管理员角色---先根据版本查出超管，再插入
		MediaRole mediaRole = mediaRoleService.getMediaRoleByModAndEname(modelFranchisee.getModid());
		mediaRoleService.insertUserRole(modelFranchisee.getUserid(),mediaRole.getRoleId());
		
	}
*/
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
//		int a = 1/0;
		userCheckDao.saveMtmyFranchisee(find);	//保存每天美耶的商家
		userCheckDao.saveFranchisee(find);	//保存平台的商家
		saveSupplyFranchisee(find);	//同步给供应链商家
		String id = find.getId();
		saveOffice(id,find);
		//更新认知支付银行账户表sys_bank_account 商家id
		userCheckDao.updateBankAccountFranchiseeId(id,find.getApplyId());
		return id ;
	}

	//同步给供应链商家
	private void saveSupplyFranchisee(UserCheck find) {
		try {
			String weburl = ParametersFactory.getMtmyParamValues("fzx_equally_franchisee");
			logger.info("##### web接口路径同步供应链:"+weburl);
			String parpm = "{\"id\":"+Integer.valueOf(find.getId())+",\"name\":\""+find.getCompanyName()+"\",\"type\":\"2\","
					+ "\"address\":\""+find.getAddress()+"\",\"legal_name\":\""+find.getLegalPerson()+"\",\"mobile\":\""+find.getMobile()
							+"\",\"charter_url\":\""+find.getCharterUrl()+"\"}";
			String url=weburl;
			logger.info("##### 打印参数param:"+parpm);
			String result = WebUtils.postCSObject(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
			/*if("200".equals(jsonObject.get("result"))){
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		return prePush(userCheck.getUserid(), 1, text, 2, "审核通知");
	}
//		JSONArray jsonArray = new JSONArray();
	/*//		jsonArray.add("35be8ac9632c9475ac67f9be3c340665");
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
		String json =  this.push(jsonObj);*/
	/**
	 * 发送消息组装方法
	 * @param cid 用户的userid
	 * @param push_type 推送类型1
	 * @param text 消息内容
	 * @param notify_type 通知类型2
	 * @param title 消息标题
	 * @return
	 */
	private String prePush(String userid,int push_type,String text,int notify_type,String title){
		String cid1 = userCheckDao.findCidByUserid(userid);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(cid1);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cid_list", jsonArray);
		jsonObj.put("push_type", push_type);
		Map<String, Object> map = new HashMap<String, Object>();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		map.put("content", text);
		map.put("notify_id", UUID.randomUUID().toString().replace("-", ""));
		map.put("notify_type", notify_type);
		map.put("push_time", dateStr);
		map.put("title", title);
		jsonObj.put("content", map);
		
		return this.push(jsonObj);
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

	/**
	 * 如果该用户同意其他商家邀请，就不能操作。
	 * @param userid
	 * @return
	 */
	public int isPermiss(String userid) {
		return userCheckDao.isPermiss(userid);
	}
	/**
	 * 重新授权发送消息
	 * 当版本重新授权后，系统给对应版本下各超级管理员推送消息，推送节点为完成续费。
		文案标题：版本续费
		文案内容：尊敬的*（姓名），您使用的妃子校*（版本名称）成功续费*（续费时长)天，已生效。
		续费时长：若当前授权开始时间为A，结束时间为B,续费授权开始时间为C，结束时间为D,若C<=B,则续费时长为D-B;若C>B,则续费时长为D-C
	 * @param modelFranchisee
	 */
	public void pushMsg(ModelFranchisee modelFranchisee,ModelFranchisee modelSelect, String opflag) {
		if (StringUtils.isNotEmpty(modelFranchisee.getId())) {
			User user = userDao.get(modelFranchisee.getUserid());
//			ModelFranchisee modelSelect = null;
//			if ("syr".equals(opflag)){
//				modelSelect = getModelFranchiseeByUserid(modelFranchisee.getUserid());
//			}else if ("qy".equals(opflag)){
//				modelSelect = getQYModelFranchiseeByUserid(modelFranchisee.getUserid());
//				
//			}
			if (modelSelect!= null){
				boolean dflag = DateUtils.formatDate(modelFranchisee.getAuthStartDate(), "yyyy-MM-dd").compareTo(DateUtils.formatDate(modelSelect.getAuthEndDate(), "yyyy-MM-dd"))>0;
				int day;
				if(dflag){
					//续费授权开始时间为C，结束时间为D,续费时长为D-C
					day = DateUtils.differentDays(modelFranchisee.getAuthStartDate(),modelFranchisee.getAuthEndDate());
				}else{
					day = DateUtils.differentDays(modelSelect.getAuthEndDate(),modelFranchisee.getAuthEndDate());
				}
				String mname = "";
				switch (modelFranchisee.getModid()) {
				case "3":
					mname = "手艺人免费版";
					break;
				case "4":
					mname = "手艺人收费版";
					break;
				case "5":
					mname = "企业标准版";
					break;
				case "6":
					mname = "企业高级版";
					break;
				case "7":
					mname = "企业旗舰版";
					break;
				default:
					break;
				}	
				//尊敬的*（姓名），您使用的妃子校*（版本名称）成功续费*（续费时长)天，已生效。
				StringBuilder sb = new StringBuilder();
				sb.append("尊敬的");
				sb.append(user.getName());
				sb.append("，您使用的妃子校");
				sb.append(mname);
				sb.append("成功续费");
				sb.append(day);
				sb.append("天，已生效。");
				System.out.println(sb.toString());
				//发消息--尊敬的17600001145，您使用的妃子校企业标准版成功续费8天，已生效。
				prePush(modelFranchisee.getUserid(), 1, sb.toString(), 2, "版本续费");
				
				//【版本升级】尊敬的XXX，恭喜您成功升级为企业（高级版），已开启更多权益！
				if(Integer.valueOf(modelFranchisee.getModid())>Integer.valueOf(modelSelect.getModid())){
					StringBuilder sb1 = new StringBuilder();
					sb1.append("尊敬的");
					sb1.append(user.getName());
					sb1.append("，恭喜您成功升级为（");
					sb1.append(mname);
					sb1.append("），已开启更多权益！");
					System.out.println(sb1.toString());
					prePush(modelFranchisee.getUserid(), 1, sb1.toString(), 2, "版本升级");
				}
			}
		}
	}
	/**
	 * 授权的时候，通过商家id，找申请的管理员id
	 * @param companyId
	 * @return
	 */
	public String findUserIdByCompanyId(Integer companyId) {
		return userCheckDao.findUserIdByCompanyId(companyId);
	}

	/**
	 * 查找手艺人商家信息列表
	 * @param page
	 * @param syrFranchise
	 * @return
	 */
	public Page<SyrFranchise> findSyrList(Page<SyrFranchise> page, SyrFranchise syrFranchise) {
		// 设置分页参数
		syrFranchise.setPage(page);
		// 执行分页查询
		page.setList(userCheckDao.findSyrList(syrFranchise));
		return page;
	}

	public Page<ModelFranchisee> findModelLogList(Page<ModelFranchisee> page, ModelFranchisee mf) {
		// 设置分页参数
		mf.setPage(page);
		// 执行分页查询
		page.setList(userCheckDao.findModelLogById(mf));
		return page;
	}
	
	
	
}

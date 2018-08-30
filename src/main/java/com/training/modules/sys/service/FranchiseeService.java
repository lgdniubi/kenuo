package com.training.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.dao.FranchiseeDao;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.FzxRoleDao;
import com.training.modules.train.dao.MediaRoleDao;
import com.training.modules.train.dao.PcRoleDao;
import com.training.modules.train.dao.UserCheckDao;
import com.training.modules.train.entity.BankAccount;
import com.training.modules.train.entity.PcRole;

import net.sf.json.JSONObject;

/**
 * 加盟商管理Service
 * @author kele
 * @version 2016-6-4 16:16:30
 */
@Service
@Transactional(readOnly = false)
public class FranchiseeService extends TreeService<FranchiseeDao,Franchisee>{

	@Autowired
	private FranchiseeDao franchiseeDao;
	@Autowired
	private UserCheckDao userCheckDao;
	@Autowired
	private PcRoleDao pcRoleDao;
	@Autowired
	private FzxRoleDao fzxRoleDao;
	@Autowired
	private MediaRoleDao mediaRoleDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 查询所有信息
	 */
	public List<Franchisee> findAllList(Franchisee franchisee){
		return franchiseeDao.findAllList(franchisee);
	}
	
	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Franchisee> findListbyPID(String pid){
		return franchiseeDao.findListbyPID(pid);
	}
	
	/**
	 * 保存
	 */
	public String saveFranchisee(Franchisee franchisee,HttpServletRequest request){
		try {
			franchiseeDao.insertFranchisee(franchisee);
			//把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
			String parentId = franchisee.getParent().getId();
			String createId = franchisee.getCreateBy().getId();
			franchiseeDao.saveMtmyFranchisee(franchisee.getId(),parentId,franchisee.getParentIds(),franchisee.getName(),franchisee.getType(),franchisee.getSort(),franchisee.getIconUrl(),createId,franchisee.getRemarks());
			
			String weburl = ParametersFactory.getMtmyParamValues("fzx_equally_franchisee");
			logger.info("##### web接口路径:"+weburl);
			String parpm = "{\"id\":"+Integer.valueOf(franchisee.getId())+",\"name\":\""+franchisee.getName()+"\",\"type\":\""+franchisee.getType()+"\","
					+ "\"address\":\""+franchisee.getAddress()+"\",\"legal_name\":\""+franchisee.getLegalName()+"\",\"contacts\":\""+franchisee.getContacts()+"\",\"mobile\":\""+franchisee.getMobile()+"\","
							+ "\"tel\":\""+franchisee.getTel()+"\",\"charter_url\":\""+franchisee.getCharterUrl()+"\",\"taxation_url\":\""+franchisee.getTaxationUrl()+"\","
									+ "\"bank_beneficiary\":\""+franchisee.getBankBeneficiary()+"\",\"bank_code\":\""+franchisee.getBankCode()+"\",\"bank_owner\":\""+franchisee.getBankName()+"\"}";
			String url=weburl;
			String result = WebUtils.postCSObject(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
		} catch (Exception e) {
			logger.error("保存商家同步错误信息："+e.getMessage());
    		BugLogUtils.saveBugLog(request, "保存商家同步失败", e);
		}
		return franchisee.getId();
	}
	
	/**
	 * 修改
	 * 
	 * @param franchisee
	 * @return
	 */
	public int update(Franchisee franchisee){
		int res = franchiseeDao.update(franchisee); 
		//把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
		String updateId = franchisee.getUpdateBy().getId();
		franchiseeDao.updateMtmyFranchisee(franchisee.getId(),franchisee.getName(),franchisee.getType(),franchisee.getIconUrl(),updateId,franchisee.getRemarks());
		
		if(1 == res){
			String weburl = ParametersFactory.getMtmyParamValues("fzx_equally_franchisee");
			logger.info("##### web接口路径:"+weburl);
			String parpm = "{\"id\":"+Integer.valueOf(franchisee.getId())+",\"name\":\""+franchisee.getName()+"\",\"type\":\""+franchisee.getType()+"\","
					+ "\"address\":\""+franchisee.getAddress()+"\",\"legal_name\":\""+franchisee.getLegalName()+"\",\"contacts\":\""+franchisee.getContacts()+"\",\"mobile\":\""+franchisee.getMobile()+"\","
							+ "\"tel\":\""+franchisee.getTel()+"\",\"charter_url\":\""+franchisee.getCharterUrl()+"\",\"taxation_url\":\""+franchisee.getTaxationUrl()+"\","
									+ "\"bank_beneficiary\":\""+franchisee.getBankBeneficiary()+"\",\"bank_code\":\""+franchisee.getBankCode()+"\",\"bank_owner\":\""+franchisee.getBankName()+"\"}";
			String url=weburl;
			String result = WebUtils.postCSObject(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
			if("200".equals(jsonObject.get("result"))){
				return 1;
			}
		}else{
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {
				
			}
		}
		//如果更换超管，操作
		if(!franchisee.getOldSuperUserId().equals(franchisee.getSuperUserId())){
			replaceSuperRole(franchisee.getId(),franchisee.getOldSuperUserId(),franchisee.getSuperUserId());
			//更换train_apply的user_id
			userCheckDao.updateApplyUserId(franchisee.getOldSuperUserId(),franchisee.getSuperUserId());
		}
		return res;
	}
	/**
	 * 更换超管操作
	 * 1清除旧管理员，pc、fzx、media的角色，user_office,user_role_office
	 * 2不用改user表data_scope部门及以下
	 * 3暂时不赋予
	 * 4清除新的管理员，pc、fzx、media的角色，user_office,user_role_office
	 * 5改user表data_scope部门及以下
	 * 6赋予超管的角色数据
	 * @param oldSuperUserId	就超管的id
	 * @param superUserId	新超管的id
	 * @param string 
	 */
	private void replaceSuperRole(String franchid, String oldSuperUserId, String superUserId) {
		deleteUserRoleOrOffice(oldSuperUserId);
		deleteUserRoleOrOffice(superUserId);
		
		User user = new User();
		user.setDataScope(1);	//设置部门及以下
		user.setId(superUserId);
		user.preUpdate();
		userDao.UpdateDataScope(user);
		
		int fzxRoleid = fzxRoleDao.findFzxSuperRoleId(franchid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fzxUserRoleId", 0);
		map.put("userid", superUserId);
		map.put("roleid", fzxRoleid);
		userCheckDao.insertFzxUserRole(map);
		int fzxUserRoleId = (int) map.get("fzxUserRoleId");
		//向fzx_user_role_office插入一条数据
		userCheckDao.insertFzxUserRoleOffice(fzxUserRoleId,franchid);
		
		int pcRoleId = pcRoleDao.findPcSuperRoleId(franchid);
		PcRole pcRole = new PcRole();
		pcRole.setId(superUserId);
		pcRole.setRoleId(pcRoleId);
		pcRoleDao.insertUserRole(pcRole );
		
		int mdRoleId = mediaRoleDao.findMdSuperRoleId(franchid);
		mediaRoleDao.insertUserRole(superUserId,mdRoleId);
	}
		

	private void deleteUserRoleOrOffice(String superUserId) {
		userCheckDao.deleteOldFzxRoleOffice(superUserId);
		userCheckDao.deleteOldFzxRole(superUserId);
		pcRoleDao.deleteUserRole(superUserId);
		fzxRoleDao.deleteUserRole(superUserId);
		mediaRoleDao.deleteUserRole(superUserId);
	}

	/**
	 * 修改公共商品服务标识
	 * @param franchisee
	 */
	public void updatePublicServiceFlag(Franchisee franchisee) {
		franchiseeDao.updatePublicServiceFlag(franchisee);
	}

	/**
	 * 删除  把trains库中的sys_franchisee的操作同步到mtmydb中mtmy_franchisee
	 * @param id
	 */
	public void deleteMtmyFranchisee(String id) {
		franchiseeDao.deleteMtmyFranchisee(id);
	}

	/**
	 * 同步mtmy商家表中修改公共商品服务标识
	 * @param franchisee
	 */
	public void updateMtmyPublicServiceFlag(Franchisee franchisee) {
		franchiseeDao.updateMtmyPublicServiceFlag(franchisee);
	}

	/**
	 * 查询商家认证时提交的银行信息
	 * @param franchisee
	 * @return
	 */
	public List<BankAccount> findBankAccountList(Franchisee franchisee) {
		return franchiseeDao.findBankAccountList(franchisee);
	}
	
	/**
	 * 保存商品详情
	 * @param description
	 * @param id
	 */
	public void saveFranchiseeDescription(String description,String id){
		franchiseeDao.saveFranchiseeDescription(description, id);
	}
	
	/**
	 * 同步每天美耶保存商品详情
	 * @param description
	 * @param id
	 */
	public void saveMtmyFranchiseeDescription(String description,String id){
		franchiseeDao.saveMtmyFranchiseeDescription(description, id);
	}

	/**
	 * 修改sys里面的"是否真实的商家"
	 * @param franchisee
	 */
	public void updateIsRealFranchisee(Franchisee franchisee) {
		franchiseeDao.updateIsRealFranchisee(franchisee);
	}

	/**
	 * 同步mtmy里面的"是否真实的商家"
	 * @param franchisee
	 */
	public void updateMtmyIsRealFranchisee(Franchisee franchisee) {
		franchiseeDao.updateMtmyIsRealFranchisee(franchisee);
	}

	/**
	 * 查找所有商家
	 * @return
	 */
	public List<Franchisee> findAllCompanyList() {
		return franchiseeDao.findAllCompanyList();
	}
}
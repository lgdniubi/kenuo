package com.training.modules.sys.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.dao.FranchiseeDao;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;

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
									+ "\"bank_beneficiary\":\""+franchisee.getBankBeneficiary()+"\",\"bank_code\":\""+franchisee.getBankCode()+"\",\"bank_owner\":\""+franchisee.getBankName()+"\",\"function\":\""+0+"\"}";
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
									+ "\"bank_beneficiary\":\""+franchisee.getBankBeneficiary()+"\",\"bank_code\":\""+franchisee.getBankCode()+"\",\"bank_owner\":\""+franchisee.getBankName()+"\",\"function\":\""+1+"\"}";
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
		return res;
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
}

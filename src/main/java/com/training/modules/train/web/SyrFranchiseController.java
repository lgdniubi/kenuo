package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.web.BaseController;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.SyrFranchise;
import com.training.modules.train.entity.UserCheck;
import com.training.modules.train.service.UserCheckService;

/**
 * 手艺人商家管理
 * @author jingfeng
 *2018-08
 */
@Controller
@RequestMapping(value = "${adminPath}/train/syr")
public class SyrFranchiseController extends BaseController{
	
	@Autowired
	private UserCheckService userCheckService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	
	/**
	 * 用户审核
	 * @param syrFranchise
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value="train:syr:list")
	@RequestMapping(value = {"list", ""})
	public String findalllist(SyrFranchise syrFranchise,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<SyrFranchise> page = userCheckService.findSyrList(new Page<SyrFranchise>(request, response), syrFranchise);
		model.addAttribute("page", page);
		return "modules/train/SyrFranchiseList";
	}
	/**
	 * 手艺人审核
	 * @param userCheck
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:syrFranchisee:permissEdit",},logical=Logical.OR)
	@RequestMapping(value={"form"})
	public String form(UserCheck userCheck,Model model,String opflag,Integer pageNo){
		model.addAttribute("pageNo", pageNo);
		String returnJsp = "";
		if ("setPermiss".equals(opflag)){
			if("syr".equals(userCheck.getType())){
				ModelFranchisee modelFranchisee = userCheckService.getModelFranchiseeByUserid(userCheck.getUserid());
				model.addAttribute("modelFranchisee",modelFranchisee);
				model.addAttribute("userid",userCheck.getUserid());
				model.addAttribute("applyid",userCheck.getId());
				returnJsp= "modules/train/syrSecondForm";
			}
		}
		
		return returnJsp;
	}
	
	
//
//
//	@RequestMapping(value = {"refuseForm"})
//	public String refuse(String id,String userid,String status,String auditType, Model model) {
//		model.addAttribute("id", id);
//		model.addAttribute("userid", userid);
//		model.addAttribute("status", status);
//		model.addAttribute("auditType", auditType);
//		return "modules/train/refuseForm";
//	}
//	
	/**
	 * 
	 * @param modelFranchisee
	 * @param opflag
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @Description:保存权限设置信息
	 */
	@RequiresPermissions(value={"train:syrFranchisee:permissSave"},logical=Logical.OR)
	@RequestMapping(value = {"saveFranchise"})
	public String saveFranchise(ModelFranchisee modelFranchisee,String opflag,Integer pageNo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		if((DateUtils.formatDate(modelFranchisee.getAuthEndDate(), "yyyy-MM-dd").compareTo(DateUtils.getDate()))<0){
			addMessage(redirectAttributes, "授权时间无效，请重新选择授权时间");
			return "redirect:" + adminPath + "/train/syr/list?pageNo="+pageNo;
		}
		UserCheck userCheck = new UserCheck();
		userCheck.setId(modelFranchisee.getApplyid());
		userCheck.setUserid(modelFranchisee.getUserid());
		userCheck.setAuditType(opflag);
		UserCheck find = userCheckService.getUserCheck(userCheck);
		boolean flag = "qy".equals(userCheck.getType()) && Integer.valueOf(userCheck.getStatus())==4;
		boolean flagsyr = "syr".equals(opflag) && "qy".equals(find.getType());
		//缓存锁
		RedisLock redisLock = new RedisLock(redisClientTemplate, "fzx_mobile_"+userCheck.getMobile());
		try {
			redisLock.lock();
			if(flag || flagsyr){
				addMessage(redirectAttributes, "该用户已经是企业用户，不能进行操作");
			}else{
				if ("syr".equals(opflag)){
					modelFranchisee.setFranchiseeid("0");
					modelFranchisee.setPaytype("0");
					ModelFranchisee modelSelect = userCheckService.getModelFranchiseeByUserid(modelFranchisee.getUserid());
					userCheckService.saveModelFranchisee(modelFranchisee);//保存手艺人权益信息
					userCheckService.pushMsg(modelFranchisee,modelSelect,opflag);//重新授权成功发送消息
				}
				redisClientTemplate.del("UTOKEN_"+modelFranchisee.getUserid());
				addMessage(redirectAttributes, "成功");
			}
			
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存权限设置出现异常,请与管理员联系");
			logger.error("保存权限设置异常,异常信息为："+e);
		}finally {
			redisLock.unlock();
		}
		return "redirect:" + adminPath + "/train/syr/list?pageNo="+pageNo;
	}
//	
//	
//	/**
//	 * 用户审核
//	 * @param userCheck
//	 * @param model
//	 * @return
//	 */
//	@RequiresPermissions(value={"train:userCheck:update",},logical=Logical.OR)
//	@RequestMapping(value={"form"})
//	public String form(UserCheck userCheck,Model model,String opflag,Integer pageNo){
//		model.addAttribute("pageNo", pageNo);
//		if ("setPermiss".equals(opflag)){
//			if("syr".equals(userCheck.getType())){
//				ModelFranchisee modelFranchisee = userCheckService.getModelFranchiseeByUserid(userCheck.getUserid());
//				model.addAttribute("modelFranchisee",modelFranchisee);
//				model.addAttribute("userid",userCheck.getUserid());
//				model.addAttribute("applyid",userCheck.getId());
//				return "modules/train/syrForm";
//			}
//			if("qy".equals(userCheck.getType())){
//				ModelFranchisee modelFranchisee = userCheckService.getQYModelFranchiseeByUserid(userCheck.getUserid());
//				if(modelFranchisee == null){
//					modelFranchisee = new ModelFranchisee();
//				}
//				modelFranchisee.setApplyid(userCheck.getId());
//				modelFranchisee.setUserid(userCheck.getUserid());
//				model.addAttribute("modelFranchisee",modelFranchisee);
//				return "modules/train/qyForm";
//			}
//		}
//		if (userCheck.getId() != null) {
//			userCheck =  userCheckService.getUserCheck(userCheck);
//			if("syr".equals(userCheck.getAuditType())){//如果认证类型是手艺人，跳转收益审核页面
//				model.addAttribute("userCheck", userCheck);
//				return "modules/train/syrCheckForm";
//			}
//		}
//		model.addAttribute("userCheck", userCheck);
//		return "modules/train/userCheckForm";
//	}
//	
//	/**
//	 * 判断是否可以进行权限设置
//	 * 如果该用户同意其他商家邀请，就不能操作。
//	 * @param userid
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value={"isPermiss"})
//	public boolean isPermiss(String userid){
//		int count = userCheckService.isPermiss(userid);
//		if (count == 0) return true;
//		return false;
//	}
}

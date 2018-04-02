package com.training.modules.train.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.UserCheck;
import com.training.modules.train.service.UserCheckService;

/**
 * 用户审核
 * @author jingfeng
 *2018-03-26
 */
@Controller
@RequestMapping(value = "${adminPath}/train/userCheck")
public class UserCheckController extends BaseController{
	
	@Autowired
	private UserCheckService userCheckService;
	
	
	/**
	 * 用户审核
	 * @param userCheck
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:userCheck:findalllist"},logical=Logical.OR)
	@RequestMapping(value = {"findalllist", ""})
	public String findalllist(UserCheck userCheck,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<UserCheck> page = userCheckService.findList(new Page<UserCheck>(request, response), userCheck);
		model.addAttribute("page", page);
		return "modules/train/userCheckList";
	}
	
	
	/**
	 * 用户审核
	 * @param userCheck
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequiresPermissions(value={"train:userCheck:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String savemodel(UserCheck userCheck, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			userCheckService.saveModel(userCheck);//保存审核信息
			if ("1".equals(userCheck.getStatus())){
				userCheckService.pushMsg(userCheck,"你的申请资料信息没有通过审核，请修改资料");
			}else if ("2".equals(userCheck.getStatus())){
				userCheckService.pushMsg(userCheck,"你的申请资料信息已通过审核，等待平台给您赋予权限");
			}
			addMessage(redirectAttributes, "成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存审核信息出现异常,请与管理员联系");
			logger.error("保存审核信息异常,异常信息为："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/userCheck/findalllist";
	}
	@RequiresPermissions(value={"train:userCheck:save"},logical=Logical.OR)
	@RequestMapping(value = {"saveFranchise"})
	public String saveFranchise(ModelFranchisee modelFranchisee,String opflag, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			if ("syr".equals(opflag)){
				modelFranchisee.setFranchiseeid("0");
				modelFranchisee.setPaytype("0");
				userCheckService.saveModelFranchisee(modelFranchisee);//保存版本分类信息
			}else if ("qy".equals(opflag)){
				userCheckService.saveQYModelFranchisee(modelFranchisee);//保存版本分类信息
			}
			addMessage(redirectAttributes, "成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存权限设置出现异常,请与管理员联系");
			logger.error("保存权限设置异常,异常信息为："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/userCheck/findalllist";
	}
	
	
	/**
	 * 用户审核
	 * @param userCheck
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:userCheck:update",},logical=Logical.OR)
	@RequestMapping(value={"form"})
	public String form(UserCheck userCheck,Model model,String opflag){
		if ("setPermiss".equals(opflag)){
			if("syr".equals(userCheck.getType())){
				ModelFranchisee modelFranchisee = userCheckService.getModelFranchiseeByUserid(userCheck.getUserid());
				model.addAttribute("modelFranchisee",modelFranchisee);
				return "modules/train/syrForm";
			}
			if("qy".equals(userCheck.getType())){
				ModelFranchisee modelFranchisee = userCheckService.getQYModelFranchiseeByUserid(userCheck.getUserid());
				if(modelFranchisee == null){
					modelFranchisee = new ModelFranchisee();
				}
				modelFranchisee.setApplyid(userCheck.getId());
				modelFranchisee.setUserid(userCheck.getUserid());
				model.addAttribute("modelFranchisee",modelFranchisee);
				return "modules/train/qyForm";
			}
		}
		if (userCheck.getId() != null) {
			userCheck =  userCheckService.getUserCheck(userCheck);
		}
		model.addAttribute("userCheck", userCheck);
		return "modules/train/userCheckForm";
	}
	
}

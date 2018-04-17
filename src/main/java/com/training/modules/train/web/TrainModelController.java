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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainModel;
import com.training.modules.train.service.FzxMenuService;
import com.training.modules.train.service.MediaMenuService;
import com.training.modules.train.service.TrainModelService;

/**
 * 版本管理
 * @author jingfeng
 *2018-03-15
 */
@Controller
@RequestMapping(value = "${adminPath}/train/model")
public class TrainModelController extends BaseController{
	
	@Autowired
	private TrainModelService trainModelService;
	@Autowired
	private FzxMenuService fzxMenuService;
	@Autowired
	private MediaMenuService mediaMenuService;
	
	
	/**
	 * 版本管理
	 * @param trainModel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:model:findalllist"},logical=Logical.OR)
	@RequestMapping(value = {"findalllist", ""})
	public String findalllist(TrainModel trainModel,HttpServletRequest request, HttpServletResponse response, Model model){
		List<TrainModel> findList = trainModelService.findList(trainModel);
		if(findList.size() >= 0){
			model.addAttribute("list", findList);
		}else{
			model.addAttribute("list", Lists.newArrayList());
		}
		return "modules/train/trainModelList";
	}
	
	
	/**
	 * 添加版本管理
	 * @param trainModel
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequiresPermissions(value={"train:model:savemodel"},logical=Logical.OR)
	@RequestMapping(value = {"savemodel", ""})
	public String savemodel(TrainModel trainModel, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User user = UserUtils.getUser();
			trainModel.setCreateBy(user);
			trainModelService.saveModel(trainModel);//保存版本分类信息
			addMessage(redirectAttributes, "成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存版本类型出现异常,请与管理员联系");
			logger.error("保存版本类型异常,异常信息为："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/model/findalllist";
	}
	
	/**
	 * 验证版本英文名称是否存在
	 * @param modEname
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkModEname")
	public String checkName(String modEname,String oldName){
		if (modEname != null && modEname.equals(oldName)) 
			return "true";
		int num=trainModelService.findByModEname(modEname);
		if(num == 0){
			return "true"; //不存在
		}else{
			return "false";
		}
	}
	
	/**
	 * 新增版本，查看版本，修改版本
	 * @param trainModel
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:model:updatemodel","train:model:view","train:model:addmodel"})
	@RequestMapping(value={"addmodel", "form"})
	public String form(TrainModel trainModel,Model model){
		if (trainModel.getId() != null) {
			trainModel =  trainModelService.getTrainModel(trainModel);
		}
		model.addAttribute("trainModel", trainModel);
		return "modules/train/trainModelForm";
	}
	/**
	 * 版本设置菜单
	 * 不同的版本具有的菜单功能是不一样的
	 * @param trainModel
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:model:auth"})
	@RequestMapping(value={"auth"})
	public String auth(TrainModel trainModel,Model model,String opflag,RedirectAttributes redirectAttributes,HttpServletRequest request){
		String returnjsp = "";
		try {
			//pc端菜单权限设置
			if ("pc".equals(opflag)) {
				model.addAttribute("pcMenu",trainModelService.findAllpcMenu());
				model.addAttribute("model",trainModelService.findmodpcMenu(trainModel));
				returnjsp = "modules/train/pcModAuth";
			}else if ("fzx".equals(opflag)) {	//fzx菜单权限设置
				model.addAttribute("fzxMenu",fzxMenuService.findAllMenu());//查询所有fzx_menu
				model.addAttribute("model",trainModelService.findmodfzxMenu(trainModel));
				returnjsp = "modules/train/fzxModAuth";
			}else if ("md".equals(opflag)) {	//自媒体菜单权限设置
				model.addAttribute("mediaMenu",mediaMenuService.findAllMenu());//查询所有自媒体菜单
				model.addAttribute("model",trainModelService.findmodMediaMenu(trainModel));
				returnjsp = "modules/train/mediaModAuth";
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "版本权限设置", e);
			logger.error("版本权限设置错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/train/model/findalllist";
		}
		return returnjsp;
	}
	/**
	 * 保存PC版本菜单权限
	 * @param model
	 * @param trainModel
	 * @param oldMenuIds
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "savePCAuth")
	public String savePCAuth(Model model,TrainModel trainModel,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(trainModel.getMenuIds())){
				trainModelService.saveModpcMenu(trainModel);
			}
			addMessage(redirectAttributes, "保存PC版本菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存PC版本菜单权限", e);
			logger.error("保存PC版本菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/model/findalllist";
	}
	/**
	 * 保存fzx版本菜单权限
	 * @param model
	 * @param trainModel
	 * @param oldMenuIds
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "savefzxAuth")
	public String savefzxAuth(Model model,TrainModel trainModel,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(trainModel.getMenuIds())){
				trainModelService.saveModfzxMenu(trainModel);
			}
			addMessage(redirectAttributes, "保存fzx版本菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存fzx版本菜单权限", e);
			logger.error("保存fzx版本菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/model/findalllist";
	}
	/**
	 * 保存自媒体版本菜单权限
	 * @param model
	 * @param trainModel
	 * @param oldMenuIds
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveMediaAuth")
	public String saveMediaAuth(Model model,TrainModel trainModel,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(trainModel.getMenuIds())){
				trainModelService.saveModMediaMenu(trainModel);
			}
			addMessage(redirectAttributes, "保存自媒体版本菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存自媒体版本菜单权限", e);
			logger.error("保存自媒体版本菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/model/findalllist";
	}
}

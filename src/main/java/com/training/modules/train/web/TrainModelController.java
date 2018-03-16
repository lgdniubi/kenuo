package com.training.modules.train.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.CategoryLesson;
import com.training.modules.train.entity.Department;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainModel;
import com.training.modules.train.service.TrainModelService;
import com.training.modules.train.utils.ScopeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

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
	
}

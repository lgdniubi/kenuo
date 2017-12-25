package com.training.modules.train.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainLiveCategory;
import com.training.modules.train.service.TrainLiveCategoryService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 直播分类管理
 * @author 小叶  2017年2月22日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/train/category")
public class TrainLiveCategoryController extends BaseController{
	
	@Autowired
	private TrainLiveCategoryService trainLiveCategoryService;
	
	/**
	 * 直播分类树list
	 * @param trainLiveCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(TrainLiveCategory trainLiveCategory,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try{
			List<TrainLiveCategory> list = trainLiveCategoryService.findList(trainLiveCategory);
			model.addAttribute("list",list);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看直播分类列表失败!", e);
			logger.error("查看直播分类列表失败：" + e.getMessage());
			
		}
		return "modules/train/liveCategoryList";
	}
	
	/**
	 * 新增修改直播分类
	 * @param trainLiveCategory
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(TrainLiveCategory trainLiveCategory,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(!"".equals(trainLiveCategory.getTrainLiveCategoryId()) && trainLiveCategory.getTrainLiveCategoryId() != null){
				trainLiveCategory = trainLiveCategoryService.get(trainLiveCategory.getTrainLiveCategoryId());
			}
			model.addAttribute("trainLiveCategory",trainLiveCategory);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增修改直播分类页面失败!", e);
			logger.error("跳转新增修改直播分类页面失败：" + e.getMessage());
		}
		return "modules/train/liveCategoryForm";
	}
	
	/**
	 * 新增修改直播分类
	 * @param trainLiveCategory
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveCategory")
	public String saveCategory(TrainLiveCategory trainLiveCategory,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		try{
			if("".equals(trainLiveCategory.getTrainLiveCategoryId()) || trainLiveCategory.getTrainLiveCategoryId() == null){
				trainLiveCategoryService.insertCategory(trainLiveCategory);
				addMessage(redirectAttributes, "新增直播分类成功!");
			}else{
				trainLiveCategoryService.updateCategory(trainLiveCategory);
				addMessage(redirectAttributes, "修改直播分类成功!");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "新增修改直播分类失败!", e);
			logger.error("新增修改直播分类失败：" + e.getMessage());
			addMessage(redirectAttributes, "新增修改直播分类失败!");
		}
		
		return "redirect:" + adminPath + "/train/category/list";
	}
	
	/**
	 * 删除直播分类
	 * @param trainLiveCategory
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delete")
	public String delete(TrainLiveCategory trainLiveCategory,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			trainLiveCategoryService.deleteCategory(trainLiveCategory);
			addMessage(redirectAttributes, "删除直播分类成功!");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除直播分类失败!", e);
			logger.error("删除直播分类失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除直播分类失败!");
		}
		return "redirect:" + adminPath + "/train/category/list";
	}
	
	/**
	 * 通过id加载子类
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getChildren")
	@ResponseBody
	public String getChildren(@RequestParam(required=false) String id, HttpServletResponse response){
		List<TrainLiveCategory> trainLiveCategory = trainLiveCategoryService.findByPidforChild(id);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(trainLiveCategory, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 
	 * @Title: treeData
	 * @Description: TODO 直播分类树
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param isGrade
	 * @param response
	 * @return:
	 * @return: List<Map<String,Object>>
	 * @throws
	 * 2017年12月18日 兵子
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll,@RequestParam(required=false) String isGrade, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TrainLiveCategory> list = trainLiveCategoryService.findAllList();
		for (int i=0; i<list.size(); i++){
			TrainLiveCategory e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getTrainLiveCategoryId());
			map.put("pId", e.getParentId());
			map.put("pIds", e.getParentIds());
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
}

package com.training.modules.ec.web;

import java.util.HashMap;
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

import com.training.common.web.BaseController;
import com.training.modules.ec.entity.MtmyWebAdCategory;
import com.training.modules.ec.service.MtmyWebAdCategoryService;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 广告图分类Controller
 * @author 小叶  2017年5月4日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/adCategory")
public class MtmyWebAdCategoryController extends BaseController{
	
	@Autowired
	private MtmyWebAdCategoryService mtmyWebAdCategoryService;
	
	/**
	 * 广告图分类树list
	 * @param mtmyWebAdCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(MtmyWebAdCategory mtmyWebAdCategory,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try{
			List<MtmyWebAdCategory> list = mtmyWebAdCategoryService.findList(mtmyWebAdCategory);
			model.addAttribute("list",list);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看广告图分类列表失败!", e);
			logger.error("查看广告图分类列表失败：" + e.getMessage());
			
		}
		return "modules/ec/mtmyWebAdCategoryList";
	}
	
	/**
	 * 跳转新增修改广告图分类页面
	 * @param mtmyWebAdCategory
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(MtmyWebAdCategory mtmyWebAdCategory,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(mtmyWebAdCategory.getMtmyWebAdCategoryId() != 0){
				mtmyWebAdCategory = mtmyWebAdCategoryService.getMtmyWebAdCategory(mtmyWebAdCategory.getMtmyWebAdCategoryId());
			}
			model.addAttribute("mtmyWebAdCategory",mtmyWebAdCategory);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增修改广告图分类页面失败!", e);
			logger.error("跳转新增修改广告图分类页面失败：" + e.getMessage());
		}
		return "modules/ec/mtmyWebAdCategoryForm";
	}
	
	/**
	 * 新增修改广告图分类
	 * @param mtmyWebAdCategory
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value="saveCategory")
	public String saveCategory(MtmyWebAdCategory mtmyWebAdCategory,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		try{
			if(mtmyWebAdCategory.getMtmyWebAdCategoryId() == 0){
				mtmyWebAdCategoryService.insertMtmyWebAdCategory(mtmyWebAdCategory);
				addMessage(redirectAttributes, "新增广告图分类成功!");
			}else{
				mtmyWebAdCategoryService.updateMtmyWebAdCategory(mtmyWebAdCategory);
				addMessage(redirectAttributes, "修改广告图分类成功!");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "新增修改广告图分类失败!", e);
			logger.error("新增修改直播分类失败：" + e.getMessage());
			addMessage(redirectAttributes, "新增修改广告图分类失败!");
		}
		
		return "redirect:" + adminPath + "/ec/adCategory/list";
	}
	
	/**
	 * 删除广告图分类
	 * @param mtmyWebAdCategory
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delete")
	public String delete(MtmyWebAdCategory mtmyWebAdCategory,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			mtmyWebAdCategoryService.deleteCategory(mtmyWebAdCategory.getMtmyWebAdCategoryId());
			addMessage(redirectAttributes, "删除广告图分类成功!");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除广告图分类失败!", e);
			logger.error("删除直播分类失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除广告图分类失败!");
		}
		return "redirect:" + adminPath + "/ec/adCategory/list";
	}
	
	/**
	 * 通过id加载子类
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getChildren")
	@ResponseBody
	public String getChildren(@RequestParam(required=false) int id, HttpServletResponse response){
		List<MtmyWebAdCategory> mtmyWebAdCategory = mtmyWebAdCategoryService.findByPidforChild(id);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(mtmyWebAdCategory, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 修改广告图分类的状态
	 * @param request
	 * @param mtmyWebAdCategory
	 * @return
	 */
	@RequestMapping(value="updateIsShow")
	@ResponseBody
	public Map<String, String> updateIsShow(HttpServletRequest request,MtmyWebAdCategory mtmyWebAdCategory) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isShow = request.getParameter("isShow");
			mtmyWebAdCategory = mtmyWebAdCategoryService.getMtmyWebAdCategory(mtmyWebAdCategory.getMtmyWebAdCategoryId());
			if("0".equals(isShow)){
				mtmyWebAdCategory.setIsShow(isShow);
				mtmyWebAdCategoryService.updateIsShow(mtmyWebAdCategory);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}else if("1".equals(isShow)){
				mtmyWebAdCategory.setIsShow(isShow);
				mtmyWebAdCategoryService.updateIsShow(mtmyWebAdCategory);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改广告图分类的状态失败", e);
			logger.error("修改广告图分类的状态失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}

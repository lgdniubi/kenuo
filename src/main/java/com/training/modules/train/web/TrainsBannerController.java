/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainsBanner;
import com.training.modules.train.service.TrainsBannerService;

/**
 * 每天每夜对账Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/trains/banner")
public class TrainsBannerController extends BaseController {
	
	@Autowired
	private TrainsBannerService trainsBannerService;
	
	@RequestMapping(value = "list")
	public String list(TrainsBanner trainsBanner,HttpServletRequest request, HttpServletResponse response,Model model) {
		
		Page<TrainsBanner> page=trainsBannerService.findPage(new Page<TrainsBanner>(request, response), trainsBanner);
		model.addAttribute("page", page);
		
		return "modules/train/bannerList";
	}
	
	@RequestMapping(value = "form")
	public String form(TrainsBanner trainsBanner,Model model){
		if(trainsBanner.getAdId()!=0){
			trainsBanner = trainsBannerService.getBanner(trainsBanner.getAdId());
			model.addAttribute("trainsBanner", trainsBanner);
		}
		return "modules/train/bannerForm";
	}
	
	@RequestMapping(value = "save")
	public String save(TrainsBanner trainsBanner,RedirectAttributes redirectAttributes){
		if(trainsBanner.getAdId()==0){
			trainsBannerService.save(trainsBanner);
			addMessage(redirectAttributes, "添加banner图成功！");
		}else{
			trainsBannerService.update(trainsBanner);
			addMessage(redirectAttributes, "修改banner图成功！");
		}
		return "redirect:" + adminPath + "/trains/banner/list";
	}
	
	@RequestMapping(value = "updateType")
	public @ResponseBody Map<String, String> updateType(TrainsBanner trainsBanner,RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			trainsBannerService.deleteBanner(trainsBanner);
			map.put("STATUS", "OK");
			map.put("FLAG", trainsBanner.getFlag());
		} catch (Exception e) {
			logger.error("banner图修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "banner图修改状态", e);
			map.put("STATUS", "ERROR");
			map.put("MESSAGE", "修改失败,出现异常");
		}
		return map;
	}
}
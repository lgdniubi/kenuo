/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.Version;
import com.training.modules.train.service.TrainsVersionService;

/**
 * 妃子校版本控制Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/trains/version")
public class TrainVersionController extends BaseController {
	
	@Autowired
	private TrainsVersionService trainsVersionService;
	
	@RequestMapping(value = "list")
	public String list(Version version,HttpServletRequest request, HttpServletResponse response,Model model) {
		try {
			Page<Version> page=trainsVersionService.findPage(new Page<Version>(request, response), version);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[妃子校版本管理-查看所有-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校版本管理-查看所有", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		return "modules/train/trainsVersionList";
	}
	
	@RequestMapping(value = "form")
	public String form(Version version,Model model,HttpServletRequest request){
		try {
			version = trainsVersionService.get(version);
			model.addAttribute("version", version);
		} catch (Exception e) {
			logger.error("#####[妃子校版本管理-查看-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校版本管理-查看", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		return "modules/train/trainsVersionForm";
	}
	
	@RequestMapping(value = "save")
	public String save(Version version,RedirectAttributes redirectAttributes,Model model,HttpServletRequest request){
		try {
			trainsVersionService.save(version);
			addMessage(redirectAttributes, "保存/修改成功");
		} catch (Exception e) {
			logger.error("#####[妃子校版本管理-保存/修改-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校版本管理-保存/修改", e);
			model.addAttribute("message", "保存/修改出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/trains/version/list";
	}
}
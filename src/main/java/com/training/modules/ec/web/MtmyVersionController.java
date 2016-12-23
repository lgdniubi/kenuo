/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.MtmyVersion;
import com.training.modules.ec.service.MtmyVersionService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 每天美耶版本控制Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/version")
public class MtmyVersionController extends BaseController {
	
	@Autowired
	private MtmyVersionService mtmyVersionService;
	
	@RequestMapping(value = "list")
	public String list(MtmyVersion mtmyVersion,HttpServletRequest request, HttpServletResponse response,Model model) {
		try {
			Page<MtmyVersion> page=mtmyVersionService.findPage(new Page<MtmyVersion>(request, response), mtmyVersion);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[每天美耶版本管理-查看所有-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "每天美耶版本管理-查看所有", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyVersionList";
	}
	
	@RequestMapping(value = "form")
	public String form(MtmyVersion mtmyVersion,Model model,HttpServletRequest request){
		try {
			mtmyVersion = mtmyVersionService.get(mtmyVersion);
			model.addAttribute("version", mtmyVersion);
		} catch (Exception e) {
			logger.error("#####[每天美耶版本管理-查看-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "每天美耶版本管理-查看", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyVersionForm";
	}
	
	@RequestMapping(value = "save")
	public String save(MtmyVersion mtmyVersion,RedirectAttributes redirectAttributes,Model model,HttpServletRequest request){
		try {
			mtmyVersionService.save(mtmyVersion);
			addMessage(redirectAttributes, "保存/修改成功");
		} catch (Exception e) {
			logger.error("#####[每天美耶版本管理-保存/修改-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "每天美耶版本管理-保存/修改", e);
			model.addAttribute("message", "保存/修改出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/version/list";
	}
}
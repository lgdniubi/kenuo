/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.BugLog;
import com.training.modules.sys.service.BugLogService;


/**
 * 日志Controller
 * 
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/buglog")
public class BugLogController extends BaseController {

	@Autowired
	private BugLogService buglogService;
	
	@RequiresPermissions("sys:buglog:list")
	@RequestMapping(value = {"list", ""})
	public String list(BugLog bugLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BugLog> page = buglogService.findPage(new Page<BugLog>(request, response), bugLog); 
        model.addAttribute("page", page);
		return "modules/sys/buglogList";
	}

	
	@RequestMapping(value = "form")
	public String form(BugLog bugLog, Model model) {
		bugLog = buglogService.get(bugLog.getId());

		model.addAttribute("bugLog", bugLog);

		return "modules/sys/buglogForm";
	}
	
	/**
	 * 批量删除
	 */
	@RequiresPermissions("sys:buglog:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			buglogService.delete(buglogService.get(id));
		}
		addMessage(redirectAttributes, "删除日志成功");
		return "redirect:"+Global.getAdminPath()+"/sys/buglog/?repage";
	}
	
	/**
	 * 批量删除
	 */
	@RequiresPermissions("sys:buglog:del")
	@RequestMapping(value = "empty")
	public String empty(RedirectAttributes redirectAttributes) {
		buglogService.empty();
		addMessage(redirectAttributes, "清空日志成功");
		return "redirect:"+Global.getAdminPath()+"/sys/buglog/?repage";
	}
	
	
}

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
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.FzxMenu;
import com.training.modules.train.service.FzxMenuService;


/**
 * 妃子校菜单管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/fzxMenu")
public class fzxMenuController extends BaseController{
	
	@Autowired
	private FzxMenuService fzxMenuService;
	
	/**
	 * 妃子校菜单list
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:fzxMenu:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<FzxMenu> page=fzxMenuService.findList(new Page<FzxMenu>(request, response), fzxMenu);
			model.addAttribute("page", page);
			model.addAttribute("fzxMenu", fzxMenu);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校菜单", e);
			logger.error("查询妃子校菜单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/fzxMenuList";
	}
	/**
	 * 妃子校菜单详情
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxMenu:view", "train:fzxMenu:add", "train:fzxMenu:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(fzxMenu.getMenuId() != 0){
				fzxMenu = fzxMenuService.get(fzxMenu);
			}else{
				fzxMenu = new FzxMenu();
			}
			model.addAttribute("fzxMenu", fzxMenu);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校菜单详情", e);
			logger.error("查询妃子校菜单详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/fzxMenuForm";
	}
	
	/**
	 * 保存菜单
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			fzxMenuService.saveFzxMenu(fzxMenu);
			addMessage(redirectAttributes, "保存/修改菜单成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校菜单", e);
			logger.error("保存妃子校菜单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxMenu/list";
	}
	
	/**
	 * 删除菜单
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxMenu:del"}, logical = Logical.OR)
	@RequestMapping(value = "delete")
	public String delete(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			fzxMenuService.delete(fzxMenu);
			fzxMenuService.deleteRoleMenu(fzxMenu.getMenuId());
			addMessage(redirectAttributes, "删除菜单成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除妃子校菜单", e);
			logger.error("删除妃子校菜单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxMenu/list";
	}
	
}

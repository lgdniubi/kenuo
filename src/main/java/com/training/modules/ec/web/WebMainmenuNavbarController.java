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
import com.training.modules.ec.entity.WebMainmenuNavbar;
import com.training.modules.ec.service.WebMainmenuNavbarService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 主菜单导航栏图Controller
 * 
 * @version 土豆 2017.5.4
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/webMainmenuNavbar")
public class WebMainmenuNavbarController extends BaseController{
	
	@Autowired
	private WebMainmenuNavbarService webMainmenuNavbarService;
	
	/**
	 * 查看主菜单导航栏列表
	 * @param webMainmenuNavbar
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(WebMainmenuNavbar webMainmenuNavbar,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<WebMainmenuNavbar> page = webMainmenuNavbarService.findPage(new Page<WebMainmenuNavbar>(request, response), webMainmenuNavbar);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看主菜单导航栏列表失败!", e);
			logger.error("查看主菜单导航栏列表失败：" + e.getMessage());
		}
		
		return "modules/ec/webMainmenuNavbarList";
	}
	
	/**
	 * 根据主菜单导航栏id查询
	 * @param webMainmenuNavbar
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(WebMainmenuNavbar webMainmenuNavbar,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		try {
			if(webMainmenuNavbar.getWebMainmenuNavbarId()!=0){
				webMainmenuNavbar = webMainmenuNavbarService.getWebMainmenuNavbar(webMainmenuNavbar.getWebMainmenuNavbarId());
				model.addAttribute("webMainmenuNavbar", webMainmenuNavbar);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看主菜单导航栏列表失败!", e);
			logger.error("查看主菜单导航栏列表失败：" + e.getMessage());
		}
		return "modules/ec/webMainmenuNavbarForm";
	}
	
	/**
	 * 保存/修改  主菜单导航栏图
	 * @param webMainmenuNavbar
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(WebMainmenuNavbar webMainmenuNavbar,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(webMainmenuNavbar.getWebMainmenuNavbarId()==0){
				webMainmenuNavbar.setIsShou("1");
				User user=UserUtils.getUser();
				webMainmenuNavbar.setCreateBy(user);
				webMainmenuNavbarService.save(webMainmenuNavbar);
				addMessage(redirectAttributes, "添加主菜单导航栏图成功！");
			}else{
				User user=UserUtils.getUser();
				webMainmenuNavbar.setUpdateBy(user);
				webMainmenuNavbarService.update(webMainmenuNavbar);
				addMessage(redirectAttributes, "修改主菜单导航栏图图成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存主菜单导航栏图失败!", e);
			logger.error("保存主菜单导航栏图失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存主菜单导航栏图失败");
		}
		return "redirect:" + adminPath + "/ec/webMainmenuNavbar/list";
	}
	
	/**
	 * 删除整组webMainmenuNavbar数据
	 * @param webMainmenuNavbar
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "del")
	public String delete(WebMainmenuNavbar webMainmenuNavbar,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			webMainmenuNavbarService.deleteWebMainmenuNavbarById(webMainmenuNavbar);
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除主菜单导航栏失败!", e);
			logger.error("(逻辑删除)主菜单导航栏失败：" + e.getMessage());
			addMessage(redirectAttributes, "(逻辑删除)主菜单导航栏失败");
		}
		
		return "redirect:" + adminPath + "/ec/webMainmenuNavbar/list";
	}
	
	/**
	 * 修改状态
	 * @param banner
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateType")
	public String updateType(WebMainmenuNavbar webMainmenuNavbar,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			webMainmenuNavbarService.changAllIsShowByType(webMainmenuNavbar);//根据当前类型,修改全部为不显示状态
			String flag = request.getParameter("flag");
			if("0".equals(flag)){//当前状态为不显示时,
				webMainmenuNavbar.setIsShou(flag);
				webMainmenuNavbarService.changIsShowByType(webMainmenuNavbar);
				return "redirect:" + adminPath + "/ec/webMainmenuNavbar/list";
			}else if("1".equals(flag)){
				int webMainmenuNavbarId = webMainmenuNavbarService.selectIdByType(webMainmenuNavbar);//根据type查询出最近修改的
				webMainmenuNavbar = webMainmenuNavbarService.getWebMainmenuNavbar(webMainmenuNavbarId);
				webMainmenuNavbar.setIsShou("0");
				webMainmenuNavbarService.changIsShowByType(webMainmenuNavbar);
				return "redirect:" + adminPath + "/ec/webMainmenuNavbar/list";
			}
			
		} catch (Exception e) {
			logger.error("主菜单导航栏修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "主菜单导航栏修改状态失败", e);
		}
		return "redirect:" + adminPath + "/ec/webMainmenuNavbar/list";
	}
	
}

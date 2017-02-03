package com.training.modules.ec.web;


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
import com.training.modules.ec.entity.TabBackground;
import com.training.modules.ec.entity.TabBanner;
import com.training.modules.ec.service.TabBackgroundService;
import com.training.modules.ec.service.TabBannerService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * tab_banner图Controller
 * 
 * @version 2016.12.8
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/tab_banner")
public class TabBannerController extends BaseController{
	
	@Autowired
	private TabBackgroundService tabBackgroundService;
	@Autowired
	private TabBannerService tabBannerService;
	
	@RequestMapping(value="list")
	public String list(TabBackground tabBackground,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<TabBackground> page = tabBackgroundService.findPage(new Page<TabBackground>(request, response), tabBackground);
		model.addAttribute("page", page);
		return "modules/ec/tab_bannerList";
	}
	
	
	@RequestMapping(value = "form")
	public String form(TabBackground tabBackground,Model model){
		if(tabBackground.getTabBackgroundId()!=0){
			tabBackground = tabBackgroundService.getTabBackground(tabBackground.getTabBackgroundId());
			model.addAttribute("tabBackground", tabBackground);
		}
		return "modules/ec/tabBackgroundForm";
	}
	
	/**
	 * 添加背景图
	 * @param tabBackground
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(TabBackground tabBackground,RedirectAttributes redirectAttributes){
		System.out.println("<>>>>>>>>>"+tabBackground);
		if(tabBackground.getTabBackgroundId()==0){
			User user=UserUtils.getUser();
			tabBackground.setCreateBy(user);
			tabBackgroundService.save(tabBackground);
			addMessage(redirectAttributes, "添加背景图成功！");
		}else{
			User user=UserUtils.getUser();
			tabBackground.setUpdateBy(user);
			tabBackgroundService.update(tabBackground);
			addMessage(redirectAttributes, "修改背景图成功！");
		}
		return "redirect:" + adminPath + "/ec/tab_banner/list";
	}
	
	/**
	 * 删除整组tab_banner图
	 * @param tabBackground
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "del")
	public String delete(TabBackground tabBackground,RedirectAttributes redirectAttributes){
		tabBackgroundService.deleteTabBackground(tabBackground);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + adminPath + "/ec/tab_banner/list";
	}
	
	
	/**
	 * 进入增加tab_banner的页面
	 * @param request
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bannerList")
	public String addBannerList(HttpServletRequest request,HttpServletResponse response,TabBackground tabBackground,TabBanner tabBanner, Model model) {
		tabBanner.setTabId(tabBackground.getTabBackgroundId());
		Page<TabBanner> page = tabBannerService.findPage(new Page<TabBanner>(request, response), tabBanner);
		model.addAttribute("page", page);	
		model.addAttribute("tabBanner",tabBanner);
		return "modules/ec/addBannerList";
	}
	
	/**
	 * 添加tab_banner图
	 * @param tabBackground
	 * @param model
	 * @param flag  判断传进来是以后的id是用来新增的还是更新用的
	 * @return
	 */
	@RequestMapping(value = "tabBannerForm")
	public String tabBannerForm(HttpServletRequest request,TabBanner tabBanner,Model model,String flag){
		if("update".equals(flag)){
			tabBanner.setTabBannerId(Integer.valueOf(request.getParameter("id")));
			tabBanner = tabBannerService.getTabBanner(tabBanner.getTabBannerId());
			model.addAttribute("tabBanner", tabBanner);
		}else{
			tabBanner.setTabId(Integer.valueOf(request.getParameter("id")));
			model.addAttribute("tabBanner", tabBanner);
		}
			return "modules/ec/tabBannerForm";
	}
	
	
	/**
	 * 查看背景图加上相应的按钮标签
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "togetherForm")
	public String togetherForm(HttpServletRequest request,HttpServletResponse response,TabBackground tabBackground,TabBanner tabBanner, Model model){
		if(tabBackground.getTabBackgroundId()!=0){
			tabBanner.setTabId(tabBackground.getTabBackgroundId());
			tabBackground = tabBackgroundService.getTabBackground(tabBackground.getTabBackgroundId());
			Page<TabBanner> page = tabBannerService.findPage(new Page<TabBanner>(request, response), tabBanner);
			model.addAttribute("page", page);	
			model.addAttribute("tabBackground", tabBackground);
		}
		return "modules/ec/togetherForm";
	}
	
	/**
	 * 查看tab_banner图
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "newTabBannerForm")
	public String newTabBannerForm(HttpServletRequest request,TabBanner tabBanner,Model model){
		if(tabBanner.getTabBannerId() != 0){
			tabBanner = tabBannerService.getTabBanner(tabBanner.getTabBannerId());
			model.addAttribute("tabBanner", tabBanner);
		}
		int tabBannerId = Integer.valueOf(request.getParameter("tabBannerId"));
		model.addAttribute("tabBannerId",tabBannerId);
		return "modules/ec/tabBannerForm";
	}
	
	/**
	 * 保存tab_banner图
	 * @param tabBanner
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveTabBanner")
	public String saveTabBanner(TabBanner tabBanner,RedirectAttributes redirectAttributes,HttpServletRequest request,String flag){
		try{
			if("add".equals(flag)){
				tabBanner.setTabId(Integer.valueOf(request.getParameter("id")));
				tabBannerService.save(tabBanner);
				return "success";
			}else{
				tabBanner.setTabBannerId(Integer.valueOf(request.getParameter("id")));
				tabBannerService.update(tabBanner);
				return "success";
			}
		}catch(Exception e){
			return "error";
		}
		
	}
	
	/**
	 * 保存tab_banner以后返回tab_banner列表
	 * @param request
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "newBannerList")
	public String newAddBannerList(HttpServletRequest request,HttpServletResponse response,TabBanner tabBanner, Model model) {
		tabBanner.setTabId(Integer.valueOf(request.getParameter("id")));
		Page<TabBanner> page = tabBannerService.findPage(new Page<TabBanner>(request, response), tabBanner);
		model.addAttribute("page", page);	
		model.addAttribute("tabBanner",tabBanner);
		return "modules/ec/addBannerList";
	}
	
	
	/**
	 * 修改状态
	 * @param banner
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateType")
	public String updateType(TabBackground tabBackground,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			tabBackgroundService.changAllIsShow();
			String flag = request.getParameter("flag");
			if("0".equals(flag)){
				tabBackground.setIsShow(Integer.valueOf(flag));
				tabBackgroundService.changIsShow(tabBackground);
				return "redirect:" + adminPath + "/ec/tab_banner/list";
			}else if("1".equals(flag)){
				int tabBackgroundId = tabBackgroundService.selectIdByUpdateDate();
				tabBackground = tabBackgroundService.getTabBackground(tabBackgroundId);
				tabBackground.setIsShow(0);
				tabBackgroundService.changIsShow(tabBackground);
				return "redirect:" + adminPath + "/ec/tab_banner/list";
			}
			
		} catch (Exception e) {
			logger.error("tab_banner图修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "tab_banner图修改状态", e);
		}
		return "redirect:" + adminPath + "/ec/tab_banner/list";
	}
	

	/**
	 * 删除某一tab_banner图
	 * @param tabBackground
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delTabBanner")
	@ResponseBody
	public String delTabBanner(TabBanner tabBanner,RedirectAttributes redirectAttributes){
		try{
			tabBannerService.delTabBanner(tabBanner);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
	
}

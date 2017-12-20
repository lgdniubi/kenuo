package com.training.modules.ec.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.WebMainmenuNavbar;
import com.training.modules.ec.entity.WebMainmenuNavbarContent;
import com.training.modules.ec.service.WebMainmenuNavbarContentService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * webMainmenuNavbarContent图Controller
 * 
 * @version 土豆 2017.5.4
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/webMainmenuNavbarContent")
public class WebMainmenuNavbarContentController extends BaseController{
	
	@Autowired
	private WebMainmenuNavbarContentService webMainmenuNavbarContentService;
	
	/**
	 * 查看中部导航图内容对应的列表
	 * @param request
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String addBannerList(HttpServletRequest request,HttpServletResponse response,WebMainmenuNavbar webMainmenuNavbar,WebMainmenuNavbarContent webMainmenuNavbarContent, Model model) {
		try{
			webMainmenuNavbarContent.setMainmenuId(webMainmenuNavbar.getWebMainmenuNavbarId());
			Page<WebMainmenuNavbarContent> page = webMainmenuNavbarContentService.findPage(new Page<WebMainmenuNavbarContent>(request, response), webMainmenuNavbarContent);
			String categoryIds = webMainmenuNavbarContentService.selectCategoryId(webMainmenuNavbar.getWebMainmenuNavbarId());
			model.addAttribute("page", page);
			model.addAttribute("mainmenuId", webMainmenuNavbar.getWebMainmenuNavbarId());
			model.addAttribute("webMainmenuNavbarContent", webMainmenuNavbarContent);	
			model.addAttribute("type",request.getParameter("type"));
			model.addAttribute("categoryIds",categoryIds);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看中部导航栏内容列表失败!", e);
			logger.error("查看中部导航栏表内容列表失败：" + e.getMessage());
		}
		return "modules/ec/webMainmenuNavbarContentList";
	}
	
	/**
	 * 跳转 编辑中部导航栏内容列表
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String newWebMainmenuNavbarContentForm(WebMainmenuNavbarContent webMainmenuNavbarContent,HttpServletRequest request,Model model,String flag){
		try{
			String type = request.getParameter("type");
			if("add".equals(flag)){//添加
				model.addAttribute("mainmenuId", webMainmenuNavbarContent.getMainmenuId());
				model.addAttribute("webMainmenuNavbarContent", webMainmenuNavbarContent);
			}else{//查看和修改
				webMainmenuNavbarContent = webMainmenuNavbarContentService.getWebMainmenuNavbarContentById(webMainmenuNavbarContent);
				model.addAttribute("webMainmenuNavbarContent", webMainmenuNavbarContent);
			}
			model.addAttribute("type",type);
			model.addAttribute("flag",flag);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看中部导航栏内容表图失败!", e);
			logger.error("查看中部导航栏内容表图失败：" + e.getMessage());
		}
		return "modules/ec/webMainmenuNavbarContentForm";
	}
	
	/**
	 * 保存/修改  中部导航栏表
	 * @param webMainmenuNavbar
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(WebMainmenuNavbarContent webMainmenuNavbarContent,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			webMainmenuNavbarContent.setRedirectUrl(HtmlUtils.htmlUnescape(webMainmenuNavbarContent.getRedirectUrl()));
			if(webMainmenuNavbarContent.getWebMainmenuNavbarContentId() == 0){
				webMainmenuNavbarContentService.saveWebMainmenuNavbarContent(webMainmenuNavbarContent);
				addMessage(redirectAttributes, "添加中部导航栏内容图成功！");
				return "success";
			}else{
				webMainmenuNavbarContentService.update(webMainmenuNavbarContent);
				addMessage(redirectAttributes, "修改中部导航栏内容图成功！");
				return "success";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存中部导航栏内容失败!", e);
			logger.error("保存中部导航栏内容失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存中部导航栏内容失败");
			return "error";
		}
	}
	
	/**
	 * 删除  根据ID删除中部导航栏内容数据
	 * @param webMainmenuNavbar
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "del")
	public String delete(WebMainmenuNavbarContent webMainmenuNavbarContent,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			webMainmenuNavbarContentService.deleteWebMainmenuNavbarContentById(webMainmenuNavbarContent);
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除中部导航栏内容失败!", e);
			logger.error("保存中部导航栏内容失败：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 保存 中部导航栏内容 ,之后返回 中部导航栏内容列表
	 * @param request
	 * @param response
	 * @param webMainmenuNavbarContent
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "newlist")
	public String newAddBannerList(HttpServletRequest request,HttpServletResponse response,WebMainmenuNavbarContent webMainmenuNavbarContent, Model model) {
		try{
			String type = request.getParameter("type");
			Integer mainmenuId = Integer.valueOf(request.getParameter("mainmenuId"));
			webMainmenuNavbarContent.setMainmenuId(mainmenuId);
			Page<WebMainmenuNavbarContent> page = webMainmenuNavbarContentService.findPage(new Page<WebMainmenuNavbarContent>(request, response), webMainmenuNavbarContent);
			String categoryIds = webMainmenuNavbarContentService.selectCategoryId(mainmenuId);
			model.addAttribute("page", page);
			model.addAttribute("mainmenuId", mainmenuId);
			model.addAttribute("webMainmenuNavbarContent", webMainmenuNavbarContent);	
			model.addAttribute("type",type);
			model.addAttribute("categoryIds",categoryIds);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看中部导航图内容列表失败!", e);
			logger.error("查看查看中部导航图内容列表失败：" + e.getMessage());
		}
		return "modules/ec/webMainmenuNavbarContentList";
	}*/
}

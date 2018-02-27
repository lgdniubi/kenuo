package com.training.modules.ec.web;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.AppStartPage;
import com.training.modules.ec.service.AppStartPageService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 启动页广告图Controller
 * 
 * @version 土豆 2017.6.6
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/appStartPage")
public class AppStartPageController extends BaseController{
	
	@Autowired
	private AppStartPageService appStartPageService;
	
	/**
	 * 查看启动页广告图
	 * @param appStartPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(AppStartPage appStartPage,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<AppStartPage> page = appStartPageService.findPage(new Page<AppStartPage>(request, response), appStartPage);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看启动页广告图列表失败!", e);
			logger.error("查看启动页广告图列表失败：" + e.getMessage());
		}
		
		return "modules/ec/appStartPageList";
	}
	
	/**
	 * 根据启动页广告图id查询
	 * @param appStartPage
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(AppStartPage appStartPage,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		try {
			if(appStartPage.getAppStartPageId()!=0){
				appStartPage = appStartPageService.getAppStartPageById(appStartPage);
				model.addAttribute("appStartPage", appStartPage);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据启动页广告图id查询失败!", e);
			logger.error("根据启动页广告图id查询失败：" + e.getMessage());
		}
		return "modules/ec/appStartPageForm";
	}
	
	/**
	 * 保存/修改  启动页广告图
	 * @param appStartPage
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(AppStartPage appStartPage,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			//当存在链接时,可能会出现转码问题
			if(!"".equals(appStartPage.getRedirectUrl()) && appStartPage.getRedirectUrl() != null){
				appStartPage.setRedirectUrl(HtmlUtils.htmlUnescape(appStartPage.getRedirectUrl()));
			}
			if(appStartPage.getAppStartPageId()==0){
				//添加
				appStartPage.setIsOnSale("0");
				User user=UserUtils.getUser();
				appStartPage.setCreateBy(user);
				appStartPageService.save(appStartPage);
				addMessage(redirectAttributes, "添加启动页广告图成功！");
			}else{
				//修改
				User user=UserUtils.getUser();
				appStartPage.setUpdateBy(user);
				appStartPageService.update(appStartPage);
				addMessage(redirectAttributes, "修改启动页广告图成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存启动页广告图失败!", e);
			logger.error("保存启动页广告图失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存启动页广告图失败");
		}
		return "redirect:" + adminPath + "/ec/appStartPage/list";
	}
	
	/**
	 * 修改状态
	 * @param appStartPage
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("ec:appStartPage:update")
	@RequestMapping(value = "updateType")
	@ResponseBody
	public Map<String, String> updateType(AppStartPage appStartPage,RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = null;
		try {
			String id = request.getParameter("ID");
			String flag = request.getParameter("flag");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(flag)){
				appStartPage.setAppStartPageId(Integer.parseInt(id));
				appStartPage.setIsOnSale(flag);
				map = appStartPageService.updateType(appStartPage);
			}else{
				map = new HashMap<String, String>();
				map.put("STATUS", "ERROR");
				map.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("启动页广告图修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "启动页广告图修改状态失败", e);
			map = new HashMap<String, String>();
			map.put("STATUS", "ERROR");
			map.put("MESSAGE", "修改失败");
		}
		return map;
	}
	
}

package com.training.modules.ec.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.MtmyFranchiseeBanner;
import com.training.modules.ec.service.FranchiseeBannerService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 商家主页banner图Controller
 * 
 * @version 土豆 2018.2.26
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/franchiseeBanner")
public class FranchiseeBannerController extends BaseController{
	
	@Autowired
	private FranchiseeBannerService franchiseeBannerService;
	
	/**
	 * 查看商家主页banner图
	 * @param mtmyFranchiseeBanner
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(MtmyFranchiseeBanner mtmyFranchiseeBanner,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<MtmyFranchiseeBanner> page = franchiseeBannerService.findPage(new Page<MtmyFranchiseeBanner>(request, response), mtmyFranchiseeBanner);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看商家主页banner图列表失败!", e);
			logger.error("查看商家主页banner图列表失败：" + e.getMessage());
		}
		
		return "modules/ec/franchiseeBannerList";
	}
	
	/**
	 * 根据商家主页banner图id查询
	 * @param mtmyFranchiseeBanner
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(MtmyFranchiseeBanner mtmyFranchiseeBanner,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		try {
			if(StringUtils.isNotEmpty(mtmyFranchiseeBanner.getId())){
				mtmyFranchiseeBanner = franchiseeBannerService.getMtmyFranchiseeBannerById(mtmyFranchiseeBanner);
				model.addAttribute("mtmyFranchiseeBanner", mtmyFranchiseeBanner);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据商家主页banner图id查询失败!", e);
			logger.error("根据商家主页banner图id查询失败：" + e.getMessage());
		}
		return "modules/ec/franchiseeBannerForm";
	}
	
	/**
	 * 保存/修改  商家主页banner图
	 * @param mtmyFranchiseeBanner
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(MtmyFranchiseeBanner mtmyFranchiseeBanner,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			//当存在链接时,可能会出现转码问题
			if(!"".equals(mtmyFranchiseeBanner.getRedirectUrl()) && mtmyFranchiseeBanner.getRedirectUrl() != null){
				mtmyFranchiseeBanner.setRedirectUrl(HtmlUtils.htmlUnescape(mtmyFranchiseeBanner.getRedirectUrl()));
			}
			User user=UserUtils.getUser();//获取当前操作人的信息
			if(null == mtmyFranchiseeBanner.getId() || "".equals(mtmyFranchiseeBanner.getId())){
				//添加
				mtmyFranchiseeBanner.setIsShow("0");
				mtmyFranchiseeBanner.setCreateBy(user);
				franchiseeBannerService.save(mtmyFranchiseeBanner);
				addMessage(redirectAttributes, "添加商家主页banner图成功！");
			}else{
				//修改
				mtmyFranchiseeBanner.setUpdateBy(user);
				franchiseeBannerService.update(mtmyFranchiseeBanner);
				addMessage(redirectAttributes, "修改商家主页banner图成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存商家主页banner图失败!", e);
			logger.error("保存商家主页banner图失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存商家主页banner图失败");
		}
		return "redirect:" + adminPath + "/ec/franchiseeBanner/list";
	}
	
	/**
	 * 修改状态
	 * @param mtmyFranchiseeBanner
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("ec:franchiseeBanner:update")
	@RequestMapping(value = "updateType")
	@ResponseBody
	public Map<String, String> updateType(MtmyFranchiseeBanner mtmyFranchiseeBanner,RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String flag = request.getParameter("flag");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(flag)){
				mtmyFranchiseeBanner.setId(id);
				mtmyFranchiseeBanner.setIsShow(flag);
				franchiseeBannerService.changIsShow(mtmyFranchiseeBanner);
				map.put("STATUS", "OK");
			}else{
				map.put("STATUS", "ERROR");
				map.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("商家主页banner图修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "商家主页banner图修改状态失败", e);
			map = new HashMap<String, String>();
			map.put("STATUS", "ERROR");
			map.put("MESSAGE", "修改失败");
		}
		return map;
	}
	
}

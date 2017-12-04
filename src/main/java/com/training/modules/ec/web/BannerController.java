/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

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
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Banner;
import com.training.modules.ec.service.BannerService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 每天每夜对账Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/banner")
public class BannerController extends BaseController {
	
	@Autowired
	private BannerService bannerService;
	
	/**
	 * 分页查询
	 * @param banner
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Banner banner,HttpServletRequest request, HttpServletResponse response,Model model) {
		
		Page<Banner> page=bannerService.findPage(new Page<Banner>(request, response), banner);
		model.addAttribute("page", page);
		return "modules/ec/bannerList";
	}
	
	/**
	 * 根据bannerId，查询
	 * @param banner
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(Banner banner,Model model){
		if(banner.getBannerId()!=0){
			banner = bannerService.get(banner);
			model.addAttribute("banner", banner);
		}
		return "modules/ec/bannerForm";
	}
	
	/**
	 * 保存/修改 banner图
	 * @param banner
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Banner banner,RedirectAttributes redirectAttributes){
		banner.setBannerName(HtmlUtils.htmlUnescape(banner.getBannerName()));
		if(!"".equals(banner.getRedirectUrl()) && banner.getRedirectUrl() != null){
			banner.setRedirectUrl(HtmlUtils.htmlUnescape(banner.getRedirectUrl()));
		}
		if(banner.getBannerId()==0){
			bannerService.save(banner);
			addMessage(redirectAttributes, "添加banner图成功！");
		}else{
			bannerService.update(banner);
			addMessage(redirectAttributes, "修改banner图成功！");
		}
		return "redirect:" + adminPath + "/ec/banner/list";
	}
	
	/**
	 * 修改banner图标识
	 * @param banner
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateType")
	public @ResponseBody Map<String, String> updateType(Banner banner,RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String fromid = request.getParameter("FROMID");
			String isyesno = request.getParameter("ISYESNO");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(fromid) && !StringUtils.isEmpty(isyesno)){
				banner.setBannerId(Integer.parseInt(id));
				if("ISSHOW".equals(fromid)){
					banner.setIsShow(isyesno);
				}else if("ISSHARE".equals(fromid)){
					banner.setIsShare(isyesno);
				}
				bannerService.updateflag(banner);
				map.put("STATUS", "OK");
				map.put("FLAG", isyesno);
			}else{
				map.put("STATUS", "ERROR");
				map.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("banner图修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "banner图修改状态", e);
			map.put("STATUS", "ERROR");
			map.put("MESSAGE", "修改失败,出现异常");
		}
		return map;
	}
}
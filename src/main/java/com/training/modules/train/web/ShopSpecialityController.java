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
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ShopSpeciality;
import com.training.modules.train.service.ShopSpecialityService;

/**
 * 店铺标签管理  Controller
 * @author 土豆
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/train/shopSpeciality")
public class ShopSpecialityController extends BaseController {

	@Autowired
	private ShopSpecialityService shopSpecialityService;

	/**
	 * 加载页面
	 * @param shopSpeciality
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list" })
	public String list(ShopSpeciality shopSpeciality, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ShopSpeciality> page = shopSpecialityService.findShopSpeciality(new Page<ShopSpeciality>(request, response), shopSpeciality);
		model.addAttribute("page", page);
		return "modules/train/shopSpecialityList";
	}

	/**
	 * 跳转页面编辑数据
	 * @param shopSpeciality
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(ShopSpeciality shopSpeciality, HttpServletRequest request, Model model) {
		try {
			if(shopSpeciality.getShopSpecialityid() != 0){
				shopSpeciality = shopSpecialityService.get(shopSpeciality);
				model.addAttribute("shopSpeciality", shopSpeciality);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑店铺标签失败!", e);
			logger.error("编辑店铺标签失败：" + e.getMessage());
		}
		return "modules/train/shopSpecialityForm";
	}

	/**
	 * 保存数据
	 * @param shopSpeciality
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(ShopSpeciality shopSpeciality, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		// 保存用户信息
		shopSpeciality.setCreateBy(UserUtils.getUser());
		shopSpecialityService.saveShopSpeciality(shopSpeciality);
		addMessage(redirectAttributes, "保存店铺标签'" + shopSpeciality.getName() + "'成功");
		return "redirect:" + adminPath + "/train/shopSpeciality/list";
	}

	/**
	 * 删除数据
	 * @param shopSpeciality
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(ShopSpeciality shopSpeciality, RedirectAttributes redirectAttributes) {
		shopSpecialityService.deleteShopSpeciality(shopSpeciality);
		addMessage(redirectAttributes, "删除店铺标签成功");
		return "redirect:" + adminPath + "/train/shopSpeciality/list";
	}
}

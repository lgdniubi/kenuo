package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.PdWareHouse;
import com.training.modules.ec.service.PdWareHouseService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 仓库
 * 
 * @author dalong
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/wareHouse")
public class PdWareHouseController extends BaseController {

	@Autowired
	private PdWareHouseService wareHouseService;

	@ModelAttribute
	public PdWareHouse get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return wareHouseService.get(id);
		} else {
			return new PdWareHouse();
		}
	}

	/**
	 * 分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(PdWareHouse wareHouse, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<PdWareHouse> page = wareHouseService.findCourier(new Page<PdWareHouse>(request, response), wareHouse);
			model.addAttribute("page", page);
			model.addAttribute("timeStart",wareHouse.getTimeStart());
			model.addAttribute("timeEnd",wareHouse.getTimeEnd());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "获取仓库列表信息错误", e);
			logger.error("获取仓库列表信息错误:"+e.getMessage());
		}
		return "modules/ec/wareHouseList";
	}

	/**
	 * 仓库跳转页面方法
	 * 
	 * @param activity
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, PdWareHouse courierHouse, Model model) {
		String returnUrl = "";
		try {
			model.addAttribute("courierHouse", courierHouse);
			if (courierHouse.getId() != null) {
				returnUrl = "modules/ec/wareHouseUpdate";
			}else{
				returnUrl = "modules/ec/wareHouseForm";
			}
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "仓库跳转页面", e);
			logger.error("仓库跳转页面：" + e.getMessage());
		}

		return returnUrl;
	}

	/**
	 * 保存
	 * 
	 * @param activity
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(PdWareHouse courierHouse, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			wareHouseService.insertWareHouse(courierHouse);
			addMessage(redirectAttributes, "保存仓库成功！");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建仓库", e);
			logger.error("方法：save，创建仓库：" + e.getMessage());
			addMessage(redirectAttributes, "创建仓库失败");
		}
		return "redirect:" + adminPath + "/ec/wareHouse/list";
	}
	
	/**
	 * 修改
	 * @param courierHouse
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "update")
	public String update(PdWareHouse courierHouse, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes){
		try {
			wareHouseService.updateWareHouse(courierHouse);
			addMessage(redirectAttributes, "修改仓库成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改仓库", e);
			logger.error("方法：updat，修改仓库：" + e.getMessage());
			addMessage(redirectAttributes, "修改仓库失败");
		}
		return "redirect:" + adminPath + "/ec/wareHouse/list";
	}
	/**
	 * 删除
	 * @param courierHouse
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(PdWareHouse courierHouse, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes){
		try {
			wareHouseService.updateWareHouseDelFlag(courierHouse);
			addMessage(redirectAttributes, "删除仓库成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除仓库失败！");
			BugLogUtils.saveBugLog(request, "删除仓库错误", e);
			logger.error("删除仓库错误："+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/wareHouse/list";
	}
}

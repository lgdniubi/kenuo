package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.training.modules.ec.entity.ReturnGoods;
import com.training.modules.ec.service.ReturnGoodsService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 订单Controller
 * 
 * @author yangyang
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/returngoods")
public class ReturnGoodsController extends BaseController {

	@Autowired
	private ReturnGoodsService returnGoodsService;

	@ModelAttribute
	public ReturnGoods get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return returnGoodsService.get(id);
		} else {
			return new ReturnGoods();
		}
	}

	/**
	 * 退货订单分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:return:list")
	@RequestMapping(value = { "list", "" })
	public String list(ReturnGoods returnGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReturnGoods> page = returnGoodsService.findReturnList(new Page<ReturnGoods>(request, response),
				returnGoods);
		model.addAttribute("page", page);
		return "modules/ec/returnGoodsList";
	}

	/**
	 * 修改
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:edit", "ec:return:view" }, logical = Logical.OR)
	@RequestMapping(value = "returnform")
	public String returnform(ReturnGoods returnGoods, String orderid, HttpServletRequest request,
			HttpServletResponse response, Model model) {

		returnGoods = returnGoodsService.get(orderid);

		model.addAttribute("returnGoods", returnGoods);
		return "modules/ec/returnGoodsForm";
	}

	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:edit", "ec:return:edit", "ec:return:list","ec:orders:return" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(ReturnGoods returnGoods, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			// String currentUser = UserUtils.getUser().getName();
			returnGoodsService.saveReturn(returnGoods);

			addMessage(redirectAttributes, "保存退货订单'" + returnGoods.getOrderid() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存退货订单", e);
			logger.error("方法：save,保存退货订单" + e.getMessage());
			addMessage(redirectAttributes, "保存退货订单失败！");
		}

		return "redirect:" + adminPath + "/ec/returngoods/list";

	}

	/**
	 * 确认退款
	 * 
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequiresPermissions("ec:return:confirm")
	@RequestMapping(value = "confirm")
	public String confirm(HttpServletRequest request, ReturnGoods returnGoods, RedirectAttributes redirectAttributes) {
		try {
			returnGoodsService.updateReturnMomeny(returnGoods);
			addMessage(redirectAttributes, "订单'" + returnGoods.getOrderid() + "'退款成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "订单退款", e);
			logger.error("方法：confirm,订单退款" + e.getMessage());
			addMessage(redirectAttributes, "订单退款失败");
		}

		return "redirect:" + adminPath + "/ec/returngoods/list?repage";
	}

}

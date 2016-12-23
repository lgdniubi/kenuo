package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.FreeOrder;
import com.training.modules.ec.service.FreeOrderService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;


/**
 * 免费体验Controller
 * 
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/freeorder")
public class FreeOrderController extends BaseController{
	
	@Autowired
	private FreeOrderService freeOrderService;
	/**
	 * 免费体验
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:freeorder:list")
	@RequestMapping(value = "list")
	public String freeOrderList(FreeOrder freeOrder,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<FreeOrder> page=freeOrderService.findPage(new Page<FreeOrder>(request, response), freeOrder);
		model.addAttribute("page", page);
		model.addAttribute("freeOrder", freeOrder);
		return "modules/ec/freeOrderList";
	}
	/**
	 * 修改免费体验
	 * @param freeOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(FreeOrder freeOrder,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		freeOrderService.save(freeOrder);
		addMessage(redirectAttributes, "修改免费体验号："+freeOrder.getId()+"成功");
		return "redirect:" + adminPath + "/ec/freeorder/list";
	}
	/**
	 * 查询备注
	 * @return
	 */
	@RequiresPermissions("ec:freeorder:findRemark")
	@RequestMapping(value = "findRemark")
	public String findRemark(FreeOrder freeOrder,HttpServletRequest request, HttpServletResponse response,Model model){
		List<FreeOrder> remark=freeOrderService.findRemark(freeOrder);
		model.addAttribute("remark", remark);
		model.addAttribute("divId", freeOrder.getDivId());
		return "modules/ec/freeRemark";
	}
	/**
	 * 添加备注
	 * @param freeOrder
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:freeorder:saveRemark")
	@RequestMapping(value = "saveRemark")
	public String saveRemark(FreeOrder freeOrder,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		//获取当前用户id
		User currentUser = UserUtils.getUser();
		freeOrder.setUser(currentUser);
		freeOrderService.saveRemark(freeOrder);
		addMessage(redirectAttributes, "添加备注成功");
		return "redirect:" + adminPath + "/ec/freeorder/list";
	}
}

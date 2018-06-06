package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.service.RefundOrderService;

@Controller
@RequestMapping(value = "${adminPath}/train/refundOrder")
public class RefundOrderController extends BaseController {

	@Autowired
	private RefundOrderService refundOrderService;
	/**
	 * 账单列表
	 * @param refundOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value="train:refundOrder:list")
	@RequestMapping(value="list")
	public String list(RefundOrder refundOrder,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			Page<RefundOrder> page = this.refundOrderService.findPage(new Page<RefundOrder>(request,response), refundOrder);
			model.addAttribute("page", page);
			model.addAttribute("refundOrder", refundOrder);
		} catch (Exception e) {
			logger.error("#####[妃子校账单管理-查看所有-出现异常：]" + e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校账单管理-查看所有", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		
		return "modules/train/refundOrderList";
	}
	/**
	 * 对账单列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="queryStatementOfRefund")
	public String queryStatementOfRefund(Model model,String office_id,String billmonth){
		model.addAttribute("statements", this.refundOrderService.queryStatementOfRefund(office_id,billmonth));
		return "modules/train/statementList";
	}
	/**
	 * 账单支付信息
	 * @param model
	 * @param order_id
	 * @return
	 */
	@RequestMapping(value="queryRefundOrderDetail")
	public String queryRefundOrderDetail(Model model,String order_id){
		model.addAttribute("refundOrder", this.refundOrderService.queryRefundOrderDetail(order_id));
		return "modules/train/refundOrderDetail";
	}
	/**
	 * 确认入账
	 * @param order_id
	 * @return
	 */
	@RequiresPermissions(value="train:refundOrder:makeSureInAccount")
	@RequestMapping(value="makeSureInAccount")
	public String makesure(Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,String order_id){
		try {
			this.refundOrderService.makeSureInAccount(order_id);
			addMessage(redirectAttributes, "已入账!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "确认入账", e);
			logger.error("确认入账错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}	
		return "redirect:" + adminPath + "/train/refundOrder/list";
	}
}

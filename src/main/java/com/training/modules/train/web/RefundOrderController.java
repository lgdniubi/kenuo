package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.service.RefundOrderService;

@Controller
@RequestMapping(value = "${adminPath}/train/refundOrder")
public class RefundOrderController extends BaseController {

	@Autowired
	private RefundOrderService refundOrderService;
	
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
}

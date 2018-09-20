package com.training.modules.train.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.entity.RefundOrderExport;
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
	public String queryStatementOfRefund(Model model,String order_id){
		model.addAttribute("statements", this.refundOrderService.queryStatementOfRefund(order_id));
		model.addAttribute("order_id", order_id);
		return "modules/train/statementList";
	}
	/**
	 * 账单支付信息
	 * @param model
	 * @param order_id
	 * @return
	 */
	@RequestMapping(value="queryRefundOrderDetail")
	public String queryRefundOrderDetail(Model model,String order_id,Integer opflag){
		model.addAttribute("refundOrder", this.refundOrderService.queryRefundOrderDetail(order_id));
		if("1".equals(opflag)){	//0是审核，1是详情
			model.addAttribute("log", this.refundOrderService.queryRefundOrderLogList(order_id));
		}
		model.addAttribute("opflag", opflag);
		return "modules/train/refundOrderDetail";
	}
	/**
	 * 审核账单
	 * @param model
	 * @param order_id
	 * @return
	 */
	@RequestMapping(value="toAuditRefundOrder")
	public String toAuditRefundOrder(Model model,String office_id,String order_id,double amount,String billmonth){
		model.addAttribute("office_id", office_id);
		model.addAttribute("order_id", order_id);
		model.addAttribute("amount",amount);
		model.addAttribute("billmonth", billmonth);
		return "modules/train/auditRefundOrder";
	}
	/**
	 * 确认入账
	 * @param order_id
	 * @return
	 */
	@RequiresPermissions(value="train:refundOrder:makeSureInAccount")
	@RequestMapping(value="makeSureInAccount")
	public String makesure(Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,String order_id,String office_id,double amount/*,String billmonth*/,String status,String remarks){
		try {
			this.refundOrderService.makeSureInAccount(order_id,office_id,amount,status,remarks);
			addMessage(redirectAttributes, "操作成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "确认入账", e);
			logger.error("确认入账错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}	
		return "redirect:" + adminPath + "/train/refundOrder/queryRefundOrderDetail?order_id="+order_id;
	}
	/**
	 * 订单日志
	 * @param model
	 * @param order_id
	 * @return
	 */
	@RequestMapping(value="queryRefundOrderLogList")
	public String queryRefundOrderLogList(Model model,String order_id){
		model.addAttribute("log", this.refundOrderService.queryRefundOrderLogList(order_id));
		return "modules/train/refundOrderLogList";
	}
	
	@RequiresPermissions("train:refundOrder:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(RefundOrderExport refundOrder, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "信用账单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
//			Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
			Page<RefundOrderExport> page = this.refundOrderService.findExportPage(new Page<RefundOrderExport>(request,response,-1), refundOrder);
			if(page.getList() !=null && page.getList().size()>0){
				new ExportExcel("信用账单", RefundOrderExport.class).setDataList(page.getList()).write(response, fileName).dispose();
			}
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e);
		}
		return "redirect:" + adminPath + "/train/refundOrder/list?repage";
	}
	/**
	 * 将待审核2的状态改已入账3
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("train:refundOrder:audit")
	@RequestMapping(value = "audit")
	public String audit(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		int successNum = idArray.length;
		if(successNum>0)
		refundOrderService.auditAll(idArray);
		addMessage(redirectAttributes, "已成功审核 " + successNum + " 条用户" );
		return "redirect:" + adminPath + "/train/refundOrder/list?repage";
	}
	
	
	@RequestMapping(value = "proof")
	public String proof(Model model,String order_id, RedirectAttributes redirectAttributes) {
		List<String> proofList = refundOrderService.findProofList(order_id);
		model.addAttribute("proofList",proofList);
		return  "modules/train/proofList";
	}
	
}

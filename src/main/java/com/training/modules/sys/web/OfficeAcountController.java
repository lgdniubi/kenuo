/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.web;

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
import com.training.modules.sys.entity.OfficeAcount;
import com.training.modules.sys.service.OfficeAcountService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.service.RefundOrderService;

/**
 * 机构信用额度
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/officeCredit")
public class OfficeAcountController extends BaseController {

	@Autowired
	private OfficeAcountService officeAcountService;
	@Autowired
	private RefundOrderService refundOrderService;
	

	/**
	 *信用机构-列表
	 * @param office
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:officeCredit:list")
	@RequestMapping(value = {"list"})
	public String list(OfficeAcount officeAcount, Model model,HttpServletResponse response, HttpServletRequest request) {
		Page<OfficeAcount> page = officeAcountService.findOfficeList(new Page<OfficeAcount>(request, response), officeAcount);
		model.addAttribute("page", page);
		model.addAttribute("officeAcount", officeAcount);
		return "modules/sys/officeAcountList";
	}
	
	 /**
     * 去信用额度编辑页面
     * @return
     */
    @RequiresPermissions("sys:office:editCredit")
    @RequestMapping("toEditCredit")
    public String toEditCredit(String office_id,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request){
    	
    	try {
			OfficeAcount officeAcount = this.officeAcountService.findOfficeAcount(office_id);
			if(officeAcount == null){
				officeAcount = new OfficeAcount();
				officeAcount.setOfficeId(office_id);
				this.officeAcountService.saveOfficeAcount(officeAcount);
			}
			model.addAttribute("officeAcount", officeAcount);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "modules/sys/editCreditLimit";
    }
    

    /**
     * 编辑信用额度
     * @param officeAcount
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping("updateOfficeCreditLimit")
    public String updateOfficeCreditLimit(OfficeAcount officeAcount,RedirectAttributes redirectAttributes,HttpServletRequest request){
    	try {
			this.officeAcountService.updateOfficeCreditLimit(officeAcount);
			addMessage(redirectAttributes, "变更信用额度成功");
		}catch (RuntimeException e) {
 			e.printStackTrace();
 			BugLogUtils.saveBugLog(request, "变更信用额度", e);
 			addMessage(redirectAttributes, e.getMessage());
 		}catch (Exception e) {
			e.printStackTrace();
			BugLogUtils.saveBugLog(request, "变更信用额度", e);
			addMessage(redirectAttributes, "变更信用额度失败");
		}
    	return "redirect:" + adminPath + "/sys/officeCredit/toEditCredit?office_id="+officeAcount.getOfficeId(); 
    }
	
    /**
	 * 还款记录
	 * @param refundOrder
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value="sys:office:refundList")
	@RequestMapping(value="refundList")
	public String refundList(RefundOrder refundOrder,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			Page<RefundOrder> page = refundOrderService.findPage(new Page<RefundOrder>(request,response), refundOrder);
			model.addAttribute("page", page);
			model.addAttribute("refundOrder", refundOrder);
		} catch (Exception e) {
			logger.error("#####[妃子校账单管理-查看所有-出现异常：]" + e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校账单管理-查看所有", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		
		return "modules/sys/officeRefundList";
	}
	/**
	 */
	@RequiresPermissions(value="sys:office:creditLogList")
	@RequestMapping(value="creditLogList")
	public String creditLogList(OfficeAcount officeAcount,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			Page<OfficeAcount> page = officeAcountService.findCreditLogPage(new Page<OfficeAcount>(request,response), officeAcount);
			model.addAttribute("page", page);
			model.addAttribute("officeAcount", officeAcount);
		} catch (Exception e) {
			logger.error("#####[妃子校额度管理-查询日志-出现异常：]" + e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校额度管理-查询日志-查看所有", e);
			model.addAttribute("message", "额度管理-查询日志异常，请与管理员联系");
		}
		
		return "modules/sys/creditLogList";
	}
}

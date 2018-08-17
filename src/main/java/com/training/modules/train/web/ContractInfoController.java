package com.training.modules.train.web;

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
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ContractInfo;
import com.training.modules.train.service.ContractInfoService;
import com.training.modules.train.service.ProtocolModelService;
/**
 * 查询
 * @author QJL
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/contractInfo")
public class ContractInfoController extends BaseController {

	
	@Autowired
	private ContractInfoService contractInfoService;
	@Autowired
	private ProtocolModelService protocolModelService;
	/**
	 * 查询签约列表
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value="train:contractInfo:list")
	@RequestMapping(value="list")
	public String queryContractAuditInfoList(ContractInfo contractInfo,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			Page<ContractInfo> page = this.contractInfoService.findPage(new Page<ContractInfo>(request, response),contractInfo);
			model.addAttribute("page", page);
			model.addAttribute("contractInfo", contractInfo);
		} catch (Exception e) {
			logger.error("#####[妃子校签约审核-列表-出现异常：]" + e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校签约审核-列表", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		
		return "modules/train/contractInfoList";
	}
	/**
	 * 查询已签约列表
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value="train:contractInfo:signedList")
	@RequestMapping(value="signedList")
	public String queryContractInfoList(ContractInfo contractInfo,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			Page<ContractInfo> page = this.contractInfoService.findSignedPage(new Page<ContractInfo>(request, response),contractInfo);
			model.addAttribute("page", page);
			model.addAttribute("contractInfo", contractInfo);
		} catch (Exception e) {
			logger.error("#####[妃子校签约审核-列表-出现异常：]" + e.getMessage());
			BugLogUtils.saveBugLog(request, "妃子校签约审核-列表", e);
			model.addAttribute("message", "查看出现异常，请与管理员联系");
		}
		
		return "modules/train/contractInfoSignedList";
	}
	/**
	 * 详情
	 * @return
	 */
	@RequestMapping(value="queryContractInfoDetail")
	public String queryContractInfoDetail(Model model,String office_id){
		
		
		model.addAttribute("contractInfo", this.contractInfoService.queryContractInfoDetail(office_id));
		
		return "modules/train/contractInfoDetail";
	}
	/**
	 * 跳转审核页面
	 * @return
	 */
	@RequestMapping(value="toAuditContractInfo")
	public String toAuditContractInfo(Model model,String office_id){
		model.addAttribute("office_id", office_id);
		return "modules/train/auditContractInfo";
	}
	/**
	 * 审核签约
	 * @return
	 */
	@RequiresPermissions(value="train:contractInfo:audit")
	@RequestMapping(value="auditContractInfo")
	public String auditContractInfo(ContractInfo contractInfo,RedirectAttributes redirectAttributes){
		try {
			this.contractInfoService.auditContractInfo(contractInfo);
			addMessage(redirectAttributes, "保存成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存出现异常,请与管理员联系");
			logger.error("保存出现异常,异常信息为："+e.getMessage());
			e.printStackTrace();
		}
		
		return "redirect:" + adminPath + "/train/contractInfo/list";
	}
	/**
	 * 查询机构签订协议
	 * @param office_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="findProtocolListOfOffice")
	public String findProtocolListOfOffice(String office_id,Model model){
		model.addAttribute("protocalUser", this.protocolModelService.findProtocolListOfOffice(office_id));
		return "modules/train/protocolListOfOffice";
	}
}

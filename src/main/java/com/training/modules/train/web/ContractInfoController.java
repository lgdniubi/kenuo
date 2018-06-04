package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ContractInfo;
import com.training.modules.train.service.ContractInfoService;
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
	/**
	 * 查询签约列表
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value="train:contractInfo:list")
	@RequestMapping(value="list")
	public String queryContractInfoList(ContractInfo contractInfo,HttpServletRequest request, HttpServletResponse response,Model model){
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
	 * 跳转审核页面
	 * @return
	 */
	@RequestMapping(value="toAuditContractInfo")
	public String toAuditContractInfo(Model model,String office_id){
		model.addAttribute("office_id", office_id);
		return "modules/train/contractInfoDetail";
	}
	
}

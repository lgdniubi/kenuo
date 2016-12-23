package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.SaleRebatesLog;
import com.training.modules.ec.service.SaleRebatesLogService;

/**
 * 订单日志表
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/salerelog")
public class SaleRebatesLogController extends BaseController {

	@Autowired
	private SaleRebatesLogService saleRebatesLogService;

	@ModelAttribute
	public SaleRebatesLog get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return saleRebatesLogService.get(id);
		} else {
			return new SaleRebatesLog();
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
	@RequiresPermissions("ec:salerelog:view")
	@RequestMapping(value = { "list", "" })
	public String list(SaleRebatesLog saleRebatesLog, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<SaleRebatesLog> page = saleRebatesLogService.findSaleRebatesLog(new Page<SaleRebatesLog>(request, response), saleRebatesLog);
		model.addAttribute("page", page);
		return "modules/ec/saleRebatesLogList";
	}

	

}

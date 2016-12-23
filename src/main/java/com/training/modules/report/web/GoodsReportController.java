package com.training.modules.report.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.report.entity.GoodsReport;
import com.training.modules.report.service.GoodsReportService;
import com.training.modules.sys.utils.BugLogUtils;



/**
 * 商品排行报表
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/report/goods")
public class GoodsReportController extends BaseController {

	@Autowired
	private GoodsReportService goodsReportService;
	

	/**
	 * 分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("report:report:view")
	@RequestMapping(value = { "report", "" })
	public String report(GoodsReport goodsReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<GoodsReport> page = goodsReportService.orderlist(new Page<GoodsReport>(request, response), goodsReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			
			BugLogUtils.saveBugLog(request, "报表查询", e);
		}
		
		return "modules/report/goodsReport";
	}
	
	
	

	

}

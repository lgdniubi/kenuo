package com.training.modules.forms.web;

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
import com.training.modules.forms.entity.UserCollectReport;
import com.training.modules.forms.entity.UserInfoReport;
import com.training.modules.forms.service.UserCollectReportService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 妃子校用户数据
 * @author stone
 * @date 2017年4月24日
 */
@Controller
@RequestMapping(value = "${adminPath}/forms/collect")
public class UserCollectReportController extends BaseController {
	@Autowired
	private UserCollectReportService userCollectReportService;
	
	/**
	 * 用户汇总表
	 *  条件查询
	 * @param userCollectReport
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions("report:report:view")
	@RequestMapping(value = { "collect", "" })
	public String collect(UserCollectReport userCollectReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			//判断有没有查询条件
			if(userCollectReport.getBegtime() != null && userCollectReport.getEndtime() != null){
				Page<UserCollectReport> page = userCollectReportService.collectListByTime(new Page<UserCollectReport>(request, response), userCollectReport);
				model.addAttribute("page", page);
			}else{
				Page<UserCollectReport> page = userCollectReportService.firstCollectList(new Page<UserCollectReport>(request, response), userCollectReport);
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "妃子校用户汇总查询", e);
		}
		return "modules/forms/userCollectReport";
	}
	
	/**
	 * 用户信息表
	 *  条件查询
	 * @param userCollectReport
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions("report:report:view")
	@RequestMapping(value = { "info", "" })
	public String info(UserInfoReport userInfoReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<UserInfoReport> page = userCollectReportService.infoList(new Page<UserInfoReport>(request, response), userInfoReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "妃子校用户汇总查询", e);
			}
		return "modules/forms/userInfoReport";
	}
	
	/**用户汇总表
	 * 导出用户数据
	 * @param userCollectReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
	@RequestMapping(value = "exportcollect", method = RequestMethod.POST)
	public String collectExportFile(UserCollectReport userCollectReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "妃子校用户汇总" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			//判断有没有查询条件
			if(userCollectReport.getBegtime() != null && userCollectReport.getEndtime() != null){
				Page<UserCollectReport>	page = userCollectReportService.collectListByTime(new Page<UserCollectReport>(request, response,-1), userCollectReport);
				new ExportExcel("妃子校用户汇总", UserCollectReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			}else{
				Page<UserCollectReport> page = userCollectReportService.firstCollectList(new Page<UserCollectReport>(request, response,-1), userCollectReport);
				new ExportExcel("妃子校用户汇总", UserCollectReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			}
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/forms/collect/collect?repage";
	}
	
	/**用户信息表
	 * 导出用户数据
	 * @param userCollectReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
	@RequestMapping(value = "exportinfo", method = RequestMethod.POST)
	public String infoExportFile(UserInfoReport userInfoReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户信息表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<UserInfoReport> page = userCollectReportService.infoList(new Page<UserInfoReport>(request, response, -1), userInfoReport);
			new ExportExcel("用户信息表", UserInfoReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/forms/collect/collect?repage";
	}
}
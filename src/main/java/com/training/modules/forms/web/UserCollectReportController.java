package com.training.modules.forms.web;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	@RequestMapping(value = { "collect", "" })
	public String collect(UserCollectReport userCollectReport, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
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
			logger.error("妃子校用户汇总查询出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = { "info", "" })
	public String info(UserInfoReport userInfoReport, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			//默认时间（开始：前七天, 结束：当天)
			if(userInfoReport.getBegtime() == null && userInfoReport.getEndtime() == null){
				Date dNow = new Date();   								//当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance();  			//得到日历
				calendar.setTime(dNow);									//把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, -7); 				//设置为前7天
				dBefore = calendar.getTime();   						//得到前7天的时间
				userInfoReport.setBegtime(dBefore);
				userInfoReport.setEndtime(dNow);
			}
			Page<UserInfoReport> page = userCollectReportService.infoList(new Page<UserInfoReport>(request, response), userInfoReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "妃子校用户汇总查询", e);
			logger.error("妃子校用户汇总查询出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = "exportcollect", method = RequestMethod.POST)
	public String collectExportFile(UserCollectReport userCollectReport, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
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
			BugLogUtils.saveBugLog(request, "导出妃子校用户汇总", e);
			logger.error("导出妃子校用户汇总出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = "exportinfo", method = RequestMethod.POST)
	public String infoExportFile(UserInfoReport userInfoReport, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户信息表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<UserInfoReport> page = userCollectReportService.infoList(new Page<UserInfoReport>(request, response, -1), userInfoReport);
			new ExportExcel("用户信息表", UserInfoReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出用户信息表", e);
			logger.error("导出用户信息表出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/forms/collect/collect?repage";
	}
}
package com.training.modules.forms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.forms.entity.TvShowReport;
import com.training.modules.forms.service.TvShowReportService;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 类名称:  TvShowReportController
 * 类描述:
 * 创建人:  Stone
 * 创建时间: 2017年5月11日 下午5:44:59
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/forms/show")
public class TvShowReportController extends BaseController{
	@Autowired
	private TvShowReportService tvShowReportService;
	
	/**直播回放数据表
	 *  条件查询
	 * @param TvShowReport
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = { "byidshow", "" })
	public String byIdShow(TvShowReport tvShowReport, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			//查询一级分类
			List<TvShowReport> listone = tvShowReportService.onelist(tvShowReport);
			model.addAttribute("listone", listone);
			model.addAttribute("categoryOne", tvShowReport.getShowLiveOne());
			model.addAttribute("categoryTwo", tvShowReport.getShowLiveTwo());
			model.addAttribute("categoryThree", tvShowReport.getShowLiveThree());
			model.addAttribute("pay", tvShowReport.getIsPay());
			Page<TvShowReport> page = tvShowReportService.tvListById(new Page<TvShowReport>(request, response), tvShowReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询直播回放数据表", e);
			logger.error("查询直播回放数据表出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "modules/forms/tvShowByIdReport";
	}
	
	/**
	 * 查询一级分类
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "oneclass")
	public String oneClass(@RequestParam(required=false) HttpServletResponse response,
			TvShowReport tvShowReport) {
		//一级分类
		List<TvShowReport> listone = tvShowReportService.onelist(tvShowReport);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(listone, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 三级联动
	 * 查询二,三级分类
	 * @param tvShowReport
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "twoclass")
	public String getChildren(@RequestParam(required=false) String ShowId, HttpServletResponse response,
			TvShowReport tvShowReport) {
		List<TvShowReport> listtwo = tvShowReportService.findtwolist(tvShowReport);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(listtwo, jsonConfig);
		return json.toString();
	}
	
	/**直播回放数据表
	 * 导出用户数据
	 * @param TvShowReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "exportinfo", method = RequestMethod.POST)
	public String exportInfo(TvShowReport tvShowReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "直播回放数据表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TvShowReport> page = tvShowReportService.tvListById(new Page<TvShowReport>(request, response, -1), tvShowReport);
			new ExportExcel("直播回放数据表", TvShowReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出直播回放数据表", e);
			logger.error("导出直播回放数据表出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/forms/show/byidshow?repage";
	}
}

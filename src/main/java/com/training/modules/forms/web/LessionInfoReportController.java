package com.training.modules.forms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.forms.entity.LessionInfoReport;
import com.training.modules.forms.entity.LessionTimeReport;
import com.training.modules.forms.service.LessionInfoReportService;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 类名称:  视频文档表
 * 创建人:  stone
 * 创建时间: 2017年5月11日 下午5:29:32
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/forms/document")
public class LessionInfoReportController extends BaseController {
	@Autowired
	private LessionInfoReportService lessionInfoReportService;
	
	/**
	 * 方法说明:	视频文档条件分页查询
	 * 创建时间:	2017年5月11日 下午5:31:43
	 * 创建人:	stone
	 * 修改记录:	修改人	修改记录	2017年5月11日 下午5:31:43
	 * @param lessionTimeReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("report:report:view")
	@RequestMapping(value = { "document", "" })
	public String document(LessionTimeReport lessionTimeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<LessionTimeReport> page = lessionInfoReportService.timeList(new Page<LessionTimeReport>(request, response), lessionTimeReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "视频文档日报表表查询", e);
		}
		return "modules/forms/lessionInfoReport";
	}
	
	/**视频文档信息表
	 * 分页查询 条件查询（根据ID）
	 * @param lessionInfoReport
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("report:report:view")
	@RequestMapping(value = { "documentbyid", "" })
	public String documentbyid(LessionInfoReport lessionInfoReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			//查询1级分类
			List<LessionInfoReport> listone = lessionInfoReportService.findcategoryslist(lessionInfoReport);
			model.addAttribute("lession", listone);
			model.addAttribute("one", lessionInfoReport.getOneClassify());
			model.addAttribute("two", lessionInfoReport.getTwoClassify());
			Page<LessionInfoReport> page = lessionInfoReportService.infoListById(new Page<LessionInfoReport>(request, response), lessionInfoReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "视频文档信息表", e);
		}
		return "modules/forms/lessionInfoByIdReport";
	}
	
	/**
	 * 查询二级分类
	 * @param lessionInfoReport
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "twoclass")
	public String getChildren(@RequestParam(required=false) HttpServletResponse response,
	LessionInfoReport lessionInfoReport) {
		List<LessionInfoReport> listtwo = lessionInfoReportService.findtwolist(lessionInfoReport);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(listtwo, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 导出用户数据
	 * 视频文档日报表
	 * @param lessionInfoReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
	@RequestMapping(value = "exportdate")
	public String exportFile(LessionTimeReport lessionTimeReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "视频文档日报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LessionTimeReport> page = lessionInfoReportService.timeList(new Page<LessionTimeReport>(request, response, -1), lessionTimeReport);
			new ExportExcel("视频文档日报表", LessionTimeReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/forms/document/document?repage";
	}
	
	/**
	 * 导出用户数据
	 * 视频文档信息表
	 * @param lessionInfoReport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
	@RequestMapping(value = "exportinfo")
	public String exportinfo1(LessionInfoReport lessionInfoReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "视频文档信息表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LessionInfoReport> page = lessionInfoReportService.infoListById(new Page<LessionInfoReport>(request, response ,-1), lessionInfoReport);
			new ExportExcel("视频文档信息表", LessionInfoReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/forms/document/documentbyid?repage";
	}
}

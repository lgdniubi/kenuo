package com.training.modules.forms.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	@RequestMapping(value = { "document", "" })
	public String document(LessionTimeReport lessionTimeReport, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			//默认时间（开始：前七天, 结束：当天)
			if(lessionTimeReport.getBegtime() == null){
				Date dNow = new Date();   								//当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance();  			//得到日历
				calendar.setTime(dNow);									//把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, -7); 				//设置为前7天
				dBefore = calendar.getTime();   						//得到前7天的时间
				lessionTimeReport.setBegtime(dBefore);
				lessionTimeReport.setEndtime(dNow);
			}
			Page<LessionTimeReport> page = lessionInfoReportService.timeList(new Page<LessionTimeReport>(request, response), lessionTimeReport);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "视频文档日报表表查询", e);
			logger.error("视频文档日报表表查询出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = { "documentbyid", "" })
	public String documentbyid(LessionInfoReport lessionInfoReport, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
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
			logger.error("视频文档信息表出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = "exportdate")
	public String exportFile(LessionTimeReport lessionTimeReport, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = "视频文档日报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LessionTimeReport> page = lessionInfoReportService.timeList(new Page<LessionTimeReport>(request, response, -1), lessionTimeReport);
			new ExportExcel("视频文档日报表", LessionTimeReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "视频文档日报表导出失败", e);
			logger.error("视频文档日报表导出出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
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
	@RequestMapping(value = "exportinfo")
	public String exportinfo1(LessionInfoReport lessionInfoReport, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "视频文档信息表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LessionInfoReport> page = lessionInfoReportService.infoListById(new Page<LessionInfoReport>(request, response ,-1), lessionInfoReport);
			new ExportExcel("视频文档信息表", LessionInfoReport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出视频文档信息表失败", e);
			logger.error("导出视频文档信息表失败出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/forms/document/documentbyid?repage";
	}
}

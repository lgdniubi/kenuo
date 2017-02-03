package com.training.modules.report.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.service.GoodsCategoryService;
import com.training.modules.report.entity.AreaUserReport;
import com.training.modules.report.entity.CategoryReport;
import com.training.modules.report.entity.UserReport;
import com.training.modules.report.entity.UserStatisticsReport;
import com.training.modules.report.service.UserReportService;
import com.training.modules.report.utils.EchartData;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONArray;




/**
 * 用户报表Controller
 * @author coffee
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/report/user")
public class UserReportController extends BaseController{
	
	@Autowired
	private UserReportService userReportService;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	
	/**
	 * 新增用户报表界面跳转
	 * @return
	 */
	@RequestMapping(value = "userReport")
	public String find(){
		return "modules/report/userReport";
	}
	
	/**
	 * 异步加载新增用户报表数据
	 * @param userReport
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "userReportList")
	public String userReportList(UserReport userReport,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			List<UserReport> list = userReportService.findList(userReport);
			List<String> xdata=new ArrayList<String>();
			List<Integer> data=new ArrayList<Integer>();
			for (UserReport userReport2 : list) {
				xdata.add(userReport2.getAddtime());
				data.add(userReport2.getUsernum());
			}
			 EchartData echart = new EchartData();
	         echart.setLegend(new ArrayList<String>(Arrays.asList("今天")));
	         echart.setXdata(new ArrayList<String>(xdata));
	         echart.setData(new ArrayList<Integer>(data));
	         List<EchartData> echartDataList = new ArrayList<EchartData>();
	         echartDataList.add(echart);
	         logger.info("用户报表返回参数："+JSONArray.fromObject(echartDataList).toString());
	         model.addAttribute("userReport", userReport);
			 return JSONArray.fromObject(echartDataList).toString();
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询用户报表", e);
			logger.error("查询用户报表错误信息:"+e.getMessage());
		}
		return "";
	}
	/**
	 * 客户统计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userStatisticsReport")
	public String userStatisticsReport(HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			UserStatisticsReport user = userReportService.statistics();
			model.addAttribute("userStatisticsReport", user);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询客户统计报表", e);
			logger.error("查询客户统计报表错误信息:"+e.getMessage());
		}
		return "modules/report/userStatisticsReport";
	}
	/**
	 * 区域分布界面跳转
	 * @return
	 */
	@RequestMapping(value = "areaUserReport")
	public String areaUserRepory(Model model){
		return "modules/report/areaUserReport";
	}
	
	/**
	 * 异步加载区域分布数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "areaUserList")
	public String areaUserList(HttpServletRequest request, HttpServletResponse response,Model model){
		try {
			List<AreaUserReport> list =  userReportService.findAreaUser();
			List<String> xdata=new ArrayList<String>();  	 //横坐标  区域
			List<Integer> userNum=new ArrayList<Integer>();  //下单用户数
			List<Integer> orderNum=new ArrayList<Integer>(); //下单总数
			List<String>  amount=new ArrayList<String>();	 //下单总金额
			for (AreaUserReport areaUserReport : list) {
				xdata.add("'"+areaUserReport.getProvince()+areaUserReport.getCity()+areaUserReport.getDistrict()+"'");
				userNum.add(areaUserReport.getUserNum());
				orderNum.add(areaUserReport.getOrderNum());
				amount.add("'"+areaUserReport.getAmount()+"'");
			}
			String string = "[{'xdata':"+xdata+",'userNum':"+userNum+",'orderNum':"+orderNum+",'amount':"+amount+"}]";
	        logger.info("用户报表返回参数："+JSONArray.fromObject(string).toString());
			 return JSONArray.fromObject(string).toString();
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询用户报表", e);
			logger.error("查询用户报表错误信息:"+e.getMessage());
		}
		return "";
	}
	/**
	 * 类目分析跳转
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "categoryReport")
	public String categoryReport(HttpServletRequest request, Model model){
		try {
			List<GoodsCategory> list = goodsCategoryService.findListbyPID("0");
			model.addAttribute("list", list);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "类目分析跳转--查询商品一级分类", e);
			logger.error("类目分析跳转--查询商品一级分类错误信息:"+e.getMessage());
		}
		return "modules/report/categoryReport";
	}
	/**
	 * 类目分析 下单金额
	 * @param categoryReport
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "categoryReportList")
	public String categoryReportList(CategoryReport categoryReport,HttpServletRequest request,Model model){
		try {
			List<CategoryReport> list = userReportService.findCategoryReport(categoryReport);
			List<String> xdata=new ArrayList<String>();		//横坐标 分类
			List<String> amount=new ArrayList<String>();	//下单金额
			List<String> goodsnum=new ArrayList<String>();	//下单商品数
			List<String> ordernum=new ArrayList<String>();	//下单量
			for (CategoryReport categoryReport2 : list) {
				xdata.add("'"+categoryReport2.getCategoryName()+"'");	
				amount.add(categoryReport2.getAmount());		
				goodsnum.add(categoryReport2.getGoodsnum());
				ordernum.add(categoryReport2.getOrdernum());
			}
			String string = "[{'xdata':"+xdata+",'amount':"+amount+",'goodsnum':"+goodsnum+",'ordernum':"+ordernum+"}]";
	        logger.info("类目分析--下单金额返回参数："+JSONArray.fromObject(string).toString());
	        return JSONArray.fromObject(string).toString();
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询类目分析报表", e);
			logger.error("查询类目分析报表错误信息:"+e.getMessage());
		}
		return "";
	}

}

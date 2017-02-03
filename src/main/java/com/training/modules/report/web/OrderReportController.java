package com.training.modules.report.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.training.common.web.BaseController;
import com.training.modules.report.entity.OrderReport;
import com.training.modules.report.service.OrderReportService;
import com.training.modules.report.utils.EchartData;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;



/**
 * 订单报表
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/report/order")
public class OrderReportController extends BaseController {

	@Autowired
	private OrderReportService orderReportService;
	



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
	public String report(OrderReport orderReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("orderReport",orderReport);
		return "modules/report/orderReport";
	}
	
	
	@ResponseBody  
	@RequiresPermissions("report:report:view")
	@RequestMapping(value ="lineData" )
	 public String lineData(OrderReport orderReport, HttpServletRequest request, HttpServletResponse response, Model model) {  
		try {
			
			
			List<String> xdata=new ArrayList<String>();
			List<Integer> data=new ArrayList<Integer>();
			List<OrderReport> list=orderReportService.orderlist(orderReport);
	
			for (int i = 0; i < list.size(); i++) {
				data.add(list.get(i).getAmount());
				xdata.add(list.get(i).getAddTime());
			}
		  	 List<EchartData> echartDataList = new ArrayList<EchartData>();
		  	 EchartData echart = new EchartData();
              echart.setLegend(new ArrayList<String>(Arrays.asList("增长量")));
              echart.setXdata(new ArrayList<String>(xdata));//里面值填充数组 datas = new String[]{"北京","天津"....}
              echart.setData(new ArrayList<Integer>(data));//里面值填充数组 datas = new Integer[]{2,7,8.....}
              echartDataList.add(echart);
             // System.out.println(JSONArray.fromObject(echartDataList).toString());
              return JSONArray.fromObject(echartDataList).toString(); //将值一json格式返回
		  //	return category.toString();
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "报表查询", e);
		}
	        
	    return null;  
	  }  

	@ResponseBody  
	@RequiresPermissions("report:report:view")
	@RequestMapping(value ="getOrderData" )
	 public String getOrderData(OrderReport orderReport, HttpServletRequest request, HttpServletResponse response, Model model) {  
		try {
			 int orderNum=0; 		// 订单数量
			 int amount=0; 			// 订单价格
			 int userNum=0; 		// 用户数量
			 int goodNum=0; 		// 商品数量
			 int totalMoney=0; 		// 商品总价
			 int avAmount=0;		//平均订单价格		
			 int avTotalMoney=0;	//商品平均价格
		//	List<OrderReport> listreport=new ArrayList<OrderReport>();
			List<OrderReport> list=orderReportService.orderlist(orderReport);
			
			OrderReport order=new OrderReport();
			for (int i = 0; i < list.size(); i++) {
				orderNum=orderNum+list.get(i).getOrderNum();
				amount=amount+list.get(i).getAmount();
				userNum=userNum+list.get(i).getUserNum();
				goodNum=goodNum+list.get(i).getGoodNum();
				totalMoney=totalMoney+list.get(i).getTotalMoney();
			}
			if(list.size()>0){
				avAmount=amount/orderNum;
			  	avTotalMoney=totalMoney/goodNum;
			}
		  	
		  	order.setOrderNum(orderNum);
		  	order.setAmount(amount);
		  	order.setUserNum(userNum);
		  	order.setGoodNum(goodNum);
		  	order.setTotalMoney(totalMoney);
		  	order.setAvAmount(avAmount);
		  	order.setAvTotalMoney(avTotalMoney);
		  	//listreport.add(order);
		  	JsonConfig jsonConfig = new JsonConfig();
		  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		  	JSONObject json =JSONObject.fromObject(order, jsonConfig);
		  //	System.out.println(json.toString());
             return json.toString(); //将值一json格式返回
		  //	return category.toString();
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "报表查询", e);
		}
	        
	    return null;  
	  }  
	

}

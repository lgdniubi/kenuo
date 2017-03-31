package com.training.modules.ec.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderInvoice;
import com.training.modules.ec.entity.OrderInvoiceExport;
import com.training.modules.ec.entity.OrderInvoiceRelevancy;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.OrderInvoiceService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 发票管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/invoice")
public class OrderInvoiceController extends BaseController {

	@Autowired
	private OrderInvoiceService orderInvoiceService;
	@Autowired
	private OrdersService ordersService;

	@ModelAttribute
	public OrderInvoice get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return orderInvoiceService.get(id);
		} else {
			return new OrderInvoice();
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
	//@RequiresPermissions("ec:invoice:view")
	@RequestMapping(value = { "list", "" })
	public String list(OrderInvoice orderInvoice, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<OrderInvoice> page = orderInvoiceService.findInvoice(new Page<OrderInvoice>(request, response), orderInvoice);
		model.addAttribute("page", page);
		return "modules/ec/orderInvoiceList";
	}
	/**
	 * 创建发票
	 * 
	 * @param orderInvoice
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:invoice:view", "ec:invoice:add", "ec:invoice:edit" }, logical = Logical.OR)
	@RequestMapping(value = "createForm")
	public String createForm(HttpServletRequest request,OrderInvoice orderInvoice, Model model) {
		try {
			model.addAttribute("orderInvoice", orderInvoice);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建发票页面", e);
			logger.error("创建发票页面：" + e.getMessage());
		}
		return "modules/ec/createInvoiceForm";
	}

	/**
	 * 查看数据
	 * @param request
	 * @param orderInvoice
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:invoice:view", "ec:invoice:add", "ec:invoice:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,OrderInvoice orderInvoice, Model model) {
		try {
			DecimalFormat formater = new DecimalFormat("#0.00");
			List<Orders> Orders=orderInvoiceService.findInvoiceRelevancy(orderInvoice.getId());
			for (int i = 0; i < Orders.size(); i++) {
				List<OrderGoods> orderGoodsList = orderInvoiceService.findOpenOrderListDetails(Orders.get(i).getOrderid());
				Orders.get(i).setOrderGoodList(orderGoodsList);
				double goodsprice = 0.00;
				for (int j = 0; j < orderGoodsList.size(); j++) {
					goodsprice = goodsprice + (orderGoodsList.get(j).getOpenNum()*orderGoodsList.get(j).getUnitPrice());
				}
				Orders.get(i).setGoodsprice(Double.parseDouble(formater.format(goodsprice)));
			}
			model.addAttribute("Orders",Orders);
			model.addAttribute("orderInvoice", orderInvoice);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建发票页面", e);
			logger.error("创建发票页面：" + e.getMessage());
		}

		return "modules/ec/invoiceViewForm";
	}
	
	/**
	 * 添加订单页面
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addOrder")
	public String addOrder(HttpServletRequest request,OrderInvoice orderInvoice, Model model) {
		try {
			
			model.addAttribute("orderInvoice", orderInvoice);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转添加订单发票", e);
			logger.error("跳转添加订单发票：" + e.getMessage());
		}

		return "modules/ec/addOrderInvoiceForm";
	}
	
	
	
	/**
	 * 查询订单详情
	 * 
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderGoods")
	public List<Map<String, Object>> getOrderGoods(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> mapList = Lists.newArrayList();

		List<Orders> list = ordersService.selectByOrderId(userId);
		for (int i = 0; i < list.size(); i++) {
			Orders e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getOrderid());
			map.put("name",e.getOrderid()+" 金额:"+e.getOrderamount());
			mapList.add(map);

		}
//		JSONArray jsonarray = JSONArray.fromObject(mapList);

		return mapList;
	}
	
	/**
	 * 保存数据
	 * @param orderInvoice
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "save")
	public String save(OrderInvoice orderInvoice, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			 DecimalFormat   df= new DecimalFormat("######0.00"); 
			 String content="";
			 double invoiceAmount=0;
			 if(orderInvoice.getInvoiceType()==1){
				 orderInvoice.setTaxNum("");
				 orderInvoice.setAccountHolder("");
				 orderInvoice.setBankName("");
				 orderInvoice.setBankNo("");
				 orderInvoice.setPhone("");
				 orderInvoice.setAddress("");
			 }
			 // 发票 订单 中间实体类
			 List<OrderInvoiceRelevancy> list=new ArrayList<OrderInvoiceRelevancy>();
			 if(orderInvoice.getOrderId().length()>0){
				 String[] arrId=orderInvoice.getOrderId().split(",");
				 for (int i = 0; i < arrId.length; i++) {
					List<OrderGoods> orderGoods = orderInvoiceService.findOrderListDetails(arrId[i].toString());
					for (int j = 0; j < orderGoods.size(); j++) {
						invoiceAmount = invoiceAmount + (orderGoods.get(j).getOpenNum()*orderGoods.get(j).getUnitPrice());
						content=content+"商品名称："+orderGoods.get(j).getGoodsname()+"  数量："+orderGoods.get(j).getOpenNum()+" 商品价格："+orderGoods.get(j).getOpenNum()*orderGoods.get(j).getUnitPrice()+",";
						OrderInvoiceRelevancy relevancy = new OrderInvoiceRelevancy();
						relevancy.setOrderId(arrId[i].toString());
						relevancy.setMappingId(String.valueOf(orderGoods.get(j).getRecid()));
						relevancy.setOpenNum(orderGoods.get(j).getOpenNum());
						list.add(relevancy);
					}
					 content=content.substring(0, content.length()-1);
				 }
			 }
			orderInvoice.setInvoiceAmount(Double.parseDouble(df.format(invoiceAmount)));
			orderInvoice.setInvoiceContent(content);
			orderInvoiceService.saveOrderInvoice(orderInvoice);
			
			if(orderInvoice.getOrderId().length()>0){
				for (int i = 0; i < list.size(); i++) {
					OrderInvoiceRelevancy relevancy = new OrderInvoiceRelevancy();
					relevancy.setInvoiceId(Integer.parseInt(orderInvoice.getId()));
					relevancy.setOrderId(list.get(i).getOrderId());
					relevancy.setMappingId(list.get(i).getMappingId());
					relevancy.setOpenNum(list.get(i).getOpenNum());
					orderInvoiceService.insertMaping(relevancy);
					orderInvoiceService.updateOrderIsinv(list.get(i).getOrderId());
				}
			}
			addMessage(redirectAttributes, "保存发票成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存发票", e);
			logger.error("方法：save，保存发票出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存发票失败");
		}
		return "redirect:" + adminPath + "/ec/invoice/list";

	}
	
	/**
	 * 修改数据
	 * @param orderInvoice
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "updateSave")
	public String updateSave(OrderInvoice orderInvoice, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {

			 if(orderInvoice.getInvoiceType()==1){
				 orderInvoice.setTaxNum("");
				 orderInvoice.setAccountHolder("");
				 orderInvoice.setBankName("");
				 orderInvoice.setBankNo("");
				 orderInvoice.setPhone("");
				 orderInvoice.setAddress("");
			 }

			orderInvoiceService.updateSave(orderInvoice);
			
			addMessage(redirectAttributes, "保存发票成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存发票", e);
			logger.error("方法：save，保存发票出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存发票失败");
		}

		return "redirect:" + adminPath + "/ec/invoice/list";

	}
	
	/**
	 * 删除数据
	 * @param orderInvoice
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delInvoic")
	public String delInvoic(OrderInvoice orderInvoice, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {

			orderInvoiceService.delInvoic(orderInvoice);
			
			addMessage(redirectAttributes, "删除发票成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除发票", e);
			logger.error("方法：save，删除发票出错：" + e.getMessage());
			addMessage(redirectAttributes, "删除发票失败");
		}

		return "redirect:" + adminPath + "/ec/invoice/list";

	}
	
	/**
	 * 导出发票
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:invoice:export")
	@RequestMapping(value = "export")
	public String export(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String fileName = "发票数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<OrderInvoiceExport> list= orderInvoiceService.findExportList();
			new ExportExcel("发票数据", OrderInvoiceExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出发票", e);
			logger.error("导出发票出错：" + e.getMessage());
			addMessage(redirectAttributes, "导出发票数据失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/invoice/list";
	}
	
//   2017年3月21日14:01:47  优化发票管理  
	
	@RequestMapping(value = "findOrderInvoiceList")
	public String findOrderInvoiceList(OrderInvoice orderInvoice,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		DecimalFormat formater = new DecimalFormat("#0.00");
		
		List<Orders> orderInvoiceCan = new ArrayList<Orders>();			// 订单可开发票
		List<Orders> orderInvoiceOvertime = new ArrayList<Orders>();	// 订单过期
		// 获取用户所有已开发票
		List<Orders> ordersInvoiceOpen = orderInvoiceService.findOpenOrderList(orderInvoice.getUserId());	
		for (int i = 0; i < ordersInvoiceOpen.size(); i++) {
			List<OrderGoods> orderGoodsList = orderInvoiceService.findOpenOrderListDetails(ordersInvoiceOpen.get(i).getOrderid());
			
			double goodsprice = 0.00;
			for (int j = 0; j < orderGoodsList.size(); j++) {
				goodsprice = goodsprice + (orderGoodsList.get(j).getOpenNum()*orderGoodsList.get(j).getUnitPrice());
			}
			ordersInvoiceOpen.get(i).setGoodsprice(Double.parseDouble(formater.format(goodsprice)));
			ordersInvoiceOpen.get(i).setOrderGoodList(orderGoodsList);
		}
		// 查询所有无欠款、已完成的订单(包含过期订单)
		List<Orders> list = orderInvoiceService.findOrderList(orderInvoice.getUserId());
		for (int i = 0; i < list.size(); i++) {
			List<OrderGoods> orderGoods = orderInvoiceService.findOrderListDetails(list.get(i).getOrderid());
			double goodsprice = 0.00;
			for (int j = 0; j < orderGoods.size(); j++) {
				goodsprice = goodsprice + (orderGoods.get(j).getOpenNum()*orderGoods.get(j).getUnitPrice());
			}
			Orders order = new Orders();
			order.setGoodsprice(Double.parseDouble(formater.format(goodsprice)));
			order.setOrderid(list.get(i).getOrderid());
			order.setOrderGoodList(orderGoods);
			// 过期订单
			if(list.get(i).getIsInvoice() == 1){
				orderInvoiceOvertime.add(order);
			}else{
				orderInvoiceCan.add(order);
			}
		}
		model.addAttribute("orderInvoiceCan", orderInvoiceCan);
		model.addAttribute("orderInvoiceOvertime", orderInvoiceOvertime);
		model.addAttribute("ordersInvoiceOpen", ordersInvoiceOpen);
		return "modules/ec/findOrderInvoiceList";
	}

}

package com.training.modules.ec.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.beanvalidator.BeanValidators;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.AcountLog;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsCoupon;
import com.training.modules.ec.entity.OrderGoodsPrice;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.OrdersLog;
import com.training.modules.ec.entity.Payment;
import com.training.modules.ec.entity.ReturnGoods;
import com.training.modules.ec.entity.Shipping;
import com.training.modules.ec.service.AcountLogService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.OrdersLogService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.service.PaymentService;
import com.training.modules.ec.service.ReturnGoodsService;
import com.training.modules.ec.utils.CourierUtils;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

import net.sf.json.JSONArray;

/**
 * 订单Controller
 * 
 * @author yangyang
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/orders")
public class OrdersController extends BaseController {

	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrderGoodsService ordergoodService;
	@Autowired
	private AcountLogService acountlogService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ReturnGoodsService returnGoodsService;
	@Autowired
	private OrdersLogService ordersLogService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;

	
	
	@ModelAttribute
	public Orders get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return ordersService.get(id);
		} else {
			return new Orders();
		}
	}

	/**
	 * 订单分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:orders:view")
	@RequestMapping(value = { "list", "" })
	public String list(Orders orders, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<Orders> page = ordersService.findOrders(new Page<Orders>(request, response), orders);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "订单列表", e);
			logger.error("订单列表：" + e.getMessage());
		}
		return "modules/ec/ordersList";
	}

	/**
	 * 查询订单下的商品
	 * 
	 * @param orderGoods
	 * @param id
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:view" }, logical = Logical.OR)
	@RequestMapping(value = "goodslist")
	public String goodslist(HttpServletRequest request, OrderGoodsPrice orderGoodsPrice, String orderid, Model model) {

		try {
			List<OrderGoods> orderlist = ordergoodService.orderlist(orderid);
			Orders orders = ordersService.get(orderid);
			orderGoodsPrice.setYouprice(orders.getCouponprice());
			orderGoodsPrice.setShippingPrice(orders.getShippingprice());
			orderGoodsPrice.setTotlprice(orders.getOrderamount());
			orderGoodsPrice.setName(orders.getName());
			orderGoodsPrice.setAddress(orders.getAddress());
			orderGoodsPrice.setPhone(orders.getMobile());
			orderGoodsPrice.setOrderid(orderid);
			List<OrderGoodsCoupon> couplist=ordersService.couplist(orderid);
			
			//屏蔽红包改版导致的数据
			boolean result = true;
			for (int i = 0; i < couplist.size(); i++) {
				logger.info("####:"+couplist.get(i).getActionName());
				if(null == couplist.get(i).getActionName() || couplist.get(i).getActionName().length() == 0){
					result = false;
				}
			}
			if(result){
				model.addAttribute("couplist", couplist);
			}else{
				model.addAttribute("couplist", null);
			}

			model.addAttribute("orderGoodsPrice", orderGoodsPrice);
			model.addAttribute("orderlist", orderlist);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看商品详", e);
			logger.error("查看商品详情出错：" + e.getMessage());
		}
		return "modules/ec/ordersGoodsList";
	}

	/**
	 * 物流信息
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:orders:view" }, logical = Logical.OR)
	@RequestMapping(value = "shipping")
	public String shipping(HttpServletRequest request,Orders orders, Model model) {
		try {
			String	xmlString="";
			List<CourierResultXML> list=new ArrayList<CourierResultXML>();
			orders = ordersService.get(orders.getOrderid());
			String shippingcode = orders.getShippingcode();
			if(orders.getOrderstatus()>=2){
				//物流编号不为null and 不为空 and 不为NULL
				if(null != shippingcode && shippingcode.length() >0 && !"NULL".equals(shippingcode.toUpperCase())){
					xmlString=CourierUtils.findCourierPost(orders.getShippingcode());
					List<CourierResultXML> xmllist=CourierUtils.readStringXmlOut(xmlString);
					for (int i = xmllist.size()-1; i >=0;i--) {
						CourierResultXML newxml=new CourierResultXML();
						newxml.setWaybillNo(xmllist.get(i).getWaybillNo());
						newxml.setUploadTime(xmllist.get(i).getUploadTime());
						newxml.setProcessInfo(xmllist.get(i).getProcessInfo());
						list.add(newxml);
					}	
				}	
			}
			
			model.addAttribute("list",list);
			model.addAttribute("orders", orders);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "物流信息页面", e);
			logger.error("物流信息页面：" + e.getMessage());
		}
		
		return "modules/ec/shippingList";
	}

	/**
	 * 修改
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:orders:edit", "ec:orders:view" }, logical = Logical.OR)
	@RequestMapping(value = "orderform")
	public String orderform(HttpServletRequest request, Orders orders, Model model) {
		try {
			List<Orders> orderlist = new ArrayList<Orders>();
			orders = ordersService.get(orders.getOrderid());
			orderlist.add(orders);
			List<AcountLog> acountlist = acountlogService.findByOrderidtwo(orders.getOrderid());
			List<Payment> paylist = paymentService.paylist();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("orderslist", orderlist);
			model.addAttribute("acountlist", acountlist);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "订单跳转修改页面", e);
			logger.error("跳转修改页面出错：" + e.getMessage());
		}

		return "modules/ec/ordersForm";
	}
	
	
	/**
	 * 订单添加商品
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addOderGoods")
	public String addOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			
			model.addAttribute("orders", orders);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转添加商品页面", e);
			logger.error("跳转添加商品页面：" + e.getMessage());
		}

		return "modules/ec/addOderGoodsForm";
	}
	

	/**
	 * 保存数据
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */


	@RequestMapping(value = "save")
	public String save(Orders orders, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			if(orders.getPaytime()==null){
				if(orders.getOrderstatus()>0){
					orders.setPaytime(new Date());
				}
			}
			ordersService.UpdateOrders(orders);
			addMessage(redirectAttributes, "保存订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存订单", e);
			logger.error("方法：save，保存订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存订单失败");
		}

		return "redirect:" + adminPath + "/ec/orders/list";

	}
	
	/**
	 * 修改物流信息
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */


	@RequestMapping(value = "UpdateShipping")
	public String UpdateShipping(Orders orders, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			ordersService.UpdateShipping(orders);
			addMessage(redirectAttributes, "订单：" + orders.getOrderid() + "物流修改成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "物流信息保存", e);
			logger.error("方法：save，物流信息保存：" + e.getMessage());
			addMessage(redirectAttributes, "物流信息保存");
		}

		return "redirect:" + adminPath + "/ec/orders/list";

	}

	/**
	 * 日志列表
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "loglist")
	public String loglist(Orders orders, Model model) {
		List<AcountLog> acountlist = acountlogService.findByOrderid(orders.getOrderid());
		model.addAttribute("acountlist", acountlist);
		return "modules/ec/logList";
	}
	/**
	 * 订单流程
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "orderlist")
	public String orderlist(Orders orders, Model model) {
		List<OrdersLog> orderslist = ordersLogService.findByOrderid(orders.getOrderid());
		model.addAttribute("orderslist", orderslist);
		return "modules/ec/oredersLogList";
	}

	/**
	 * 生成退货订单
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */


	@RequestMapping(value = "returnGoddsform")
	public String returnGoddsform(ReturnGoods returnGoods, String orderid, Model model) {
		returnGoods = returnGoodsService.get(orderid);
		model.addAttribute("returnGoods", returnGoods);
		return "modules/ec/returnForm";
	}

	/**
	 * 保存退货订单数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */


	@RequestMapping(value = "saveReturn")
	public String saveReturn(ReturnGoods returnGoods, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			
			int num=ordersService.selectSaleByOrderid(returnGoods.getOrderid());
			if(num>0){
				returnGoodsService.saveReturn(returnGoods);
				ordersService.updateSale(returnGoods.getOrderid());
				addMessage(redirectAttributes, "保存退货订单'" + returnGoods.getOrderid() + "'成功");
			}else{
				addMessage(redirectAttributes, "订单数据暂时没处理稍后再试！");
			}
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存退货订单", e);
			logger.error("方法：saveReturn,保存退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存退货订单失败！");
		}
		// String currentUser = UserUtils.getUser().getName();

		return "redirect:" + adminPath + "/ec/orders/list";

	}

	/**
	 * 编辑生成订单
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createOrder")
	public String createOrder(HttpServletRequest request,Orders orders,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建订单页面", e);
			logger.error("方法：createOrder，跳转创建订单页面出错：" + e.getMessage());
		}

		return "modules/ec/createOrderForm";
	}

	/**
	 * 添加订单保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveOrder")
	public String saveOrder(Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveOrder(orders);
			addMessage(redirectAttributes, "创建订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建订单", e);
			logger.error("方法：saveOrder，创建新订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建订单失败！");
		}

		return "redirect:" + adminPath + "/ec/orders/list";

	}

	/**
	 * 二级分类查询
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "catetwolist")
	public String catetwolist(String id, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();

		List<GoodsCategory> list = ordersService.catetwolist(id);
		for (int i = 0; i < list.size(); i++) {
			GoodsCategory e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getCategoryId());
			map.put("name", e.getName());
			mapList.add(map);

		}
		// System.out.println(list.toString());
		JSONArray jsonarray = JSONArray.fromObject(mapList);

		return jsonarray.toString();
	}

	/**
	 * 根据分类查询商品
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "goodscate")
	public String goodscate(String id, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Goods goods=new Goods();
		goods.setGoodsCategoryId(id);
		List<Goods> list = ordersService.goodslist(goods);
		for (int i = 0; i < list.size(); i++) {
			Goods e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getGoodsId());
			map.put("name", e.getGoodsName());
			mapList.add(map);

		}

		JSONArray jsonarray = JSONArray.fromObject(mapList);

		return jsonarray.toString();
	}

	/**
	 * 根据商品id 查询信息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "Getgood")
	public Goods Getgood(String id, HttpServletRequest request, HttpServletResponse response) {
		Goods goods = ordersService.getgoods(id);
		return goods;
	}

	/**
	 * 根据商品查询规格
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "specgoods")
	public String specgoods(String id, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<GoodsSpecPrice> list = ordersService.speclist(id);
		for (int i = 0; i < list.size(); i++) {
			GoodsSpecPrice e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getSpecKey());
			map.put("name", e.getSpecKeyValue());
			mapList.add(map);

		}

		JSONArray jsonarray = JSONArray.fromObject(mapList);

		return jsonarray.toString();
	}

	/**
	 * 根据商品规格key 查询信息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSpecPrce")
	public GoodsSpecPrice getSpecPrce(GoodsSpecPrice goodsSpec, String id, String goodid, HttpServletRequest request,
			HttpServletResponse response) {
		goodsSpec.setGoodsId(goodid);
		goodsSpec.setSpecKey(id);
		goodsSpec = ordersService.getSpecPrce(goodsSpec);
		return goodsSpec;
	}
	
	
//	/**
//	 * 查询用户等级折扣
//	 * @param mobile
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "getUserLevel")
//	public String getUserLevel(String mobile, HttpServletRequest request,HttpServletResponse response) {
//		String diString=ordersService.getUserLevel(mobile);
//		 
//		return diString;
//	}
	
	/**
	 * 导出用户数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(Orders orders, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "订单数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Orders> page = ordersService.findOrdersExcal(new Page<Orders>(request, response, -1), orders);
			new ExportExcel("订单数据", Orders.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单数据！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list?repage";
	}
	
	/**
	 * 跳转到导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/ec/importExcel";
	}
	
	
	/**
	 * 下载导入物流数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(Orders orders,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "物流数据.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[物流数据模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[物流数据模板-new-path"+path);
			File file = new File(path);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	
	/**
	 * 导入物流数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import")
	public String importShipping(MultipartFile file, RedirectAttributes redirectAttributes) {
	
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			int cell = ei.getLastCellNum();
			if (cell > 4) {
				failureMsg.insert(0, "<br/>导入的模板错误，请检查模板; ");

			} else {
				List<Shipping> list = ei.getDataList(Shipping.class);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date=null;
				for (Shipping shipping : list) {
					try {
						if (shipping.getOrderid() == null) {
							break;
						}
						BeanValidators.validateWithException(validator, shipping);
						Orders orders=new Orders();
						if(shipping.getShippingtime()!=null){
							date=sdf.parse(shipping.getShippingtime());
						}else{
							date=new Date();
						}
						orders.setOrderid(shipping.getOrderid());
						orders.setShippingcode(shipping.getShippingcode());
						orders.setShippingname(shipping.getShippingname());
						orders.setShippingtime(date);
						int index = ordersService.UpdateShipping(orders);
						if (index > 0) {		
							successNum++;
						} else {
							failureMsg.append("<br/>订单" + shipping.getOrderid() + "更新物流失败; ");
							failureNum++;
						}	

					} catch (ConstraintViolationException ex) {
						logger.error("导入物流出错："+ex.getMessage());
						failureMsg.append("<br/>订单 " + shipping.getOrderid() + " 更新物流失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ":");
						for (String message : messageList) {
							failureMsg.append(message + ";");
							failureNum++;
						}
					} catch (Exception ex) {
						logger.error("导入物流出错："+ex.getMessage());
						failureMsg.append("<br/>订单号 " + shipping.getOrderid() + " 更新物流失败：" + ex.getMessage());
					}
				}
				
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条，物流更新信息如下：");
			}
			addMessage(redirectAttributes, "已成功更新 " + successNum + " 条订单。" + failureMsg);
			
		} catch (Exception e) {
			logger.error("导入物流出错："+e.getMessage());
			addMessage(redirectAttributes, "导入物流失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}

}

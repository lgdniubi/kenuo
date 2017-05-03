package com.training.modules.ec.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
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
import com.training.modules.ec.dao.GoodsSpecPriceDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.AcountLog;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsDetailSum;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.ImportVirtualOrders;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsCoupon;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.OrderGoodsPrice;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.OrderRechargeLog;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.OrdersLog;
import com.training.modules.ec.entity.Payment;
import com.training.modules.ec.entity.ReturnGoods;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.Shipping;
import com.training.modules.ec.service.AcountLogService;
import com.training.modules.ec.service.OrderGoodsDetailsService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.OrdersLogService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.service.PaymentService;
import com.training.modules.ec.service.ReturnGoodsService;
import com.training.modules.ec.service.ReturnedGoodsService;
import com.training.modules.ec.utils.CourierUtils;
import com.training.modules.ec.utils.OrderUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
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
	@Autowired
	private ReturnedGoodsService returnedGoodsService;
	@Autowired
	private SaleRebatesLogDao saleRebatesLogDao;
	@Autowired
	private OrderGoodsDetailsService orderGoodsDetailsService;
	@Autowired
	private OrdersDao ordersDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private GoodsSpecPriceDao goodsSpecPriceDao;
	
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	public static final String buying_limit_prefix = "buying_limit_";				//抢购活动商品限购数量
	
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
			Page<Orders> page = new Page<Orders>();
			if(StringUtils.isNotBlank(orders.getUsername()) || StringUtils.isNotBlank(orders.getMobile()) || StringUtils.isNotBlank(orders.getOrderid())){
				page = ordersService.newFindOrders(new Page<Orders>(request, response), orders);
			}else{
				page = ordersService.findOrders(new Page<Orders>(request, response), orders);
			}
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
	public String orderform(HttpServletRequest request, Orders orders,String type, Model model) {
		try {
			User user = UserUtils.getUser(); //登陆用户
			List<Payment> paylist = paymentService.paylist();
			orders = ordersService.selectOrderById(orders.getOrderid());
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("user", user);
			model.addAttribute("type", type);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "订单跳转修改页面", e);
			logger.error("跳转修改页面出错：" + e.getMessage());
		}
		return "modules/ec/ordersForm";
	}
	
	
	/**
	 * 虚拟订单添加商品
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
			
			int returnDay = Integer.parseInt(ParametersFactory.getMtmyParamValues("returngoods_date"));
			if(-1 == returnDay){
				returnDay = 10;	//默认给一个10天（快递3天+7天退货）
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date returnTime = sdf.parse(OrderUtils.plusDay(returnDay, sdf.format(orders.getShippingtime())));
			orders.setReturnTime(returnTime);
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
	public String saveReturn(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveReturn(returnedGoods);

			addMessage(redirectAttributes, "保存退货订单'" + returnedGoods.getOrderId() + "'成功");

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存退货订单", e);
			logger.error("方法：saveReturn,保存退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存退货订单失败！");
		}
		// String currentUser = UserUtils.getUser().getName();

		return "redirect:" + adminPath + "/ec/orders/list";

	}

	/**
	 * 跳转虚拟添加订单页
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
			BugLogUtils.saveBugLog(request, "跳转创建虚拟订单页面", e);
			logger.error("方法：createOrder，跳转创建虚拟订单页面出错：" + e.getMessage());
		}

		return "modules/ec/createOrderForm";
	}

	/**
	 * 添加虚拟订单
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveVirtualOrder")
	public String saveVirtualOrder(Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveVirtualOrder(orders);
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
			map.put("serviceTimes", e.getServiceTimes());
			map.put("costPrice", e.getCostPrice());
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
	
	/**
	 * 查询退货订单
	 * @param orders
	 * @param orderid   
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "returnGoddsList")
	public String returnGoddsList(ReturnedGoods returnedGoods, String orderid, Model model) {
		//returnGoods = returnGoodsService.get(orderid);
		List<OrderGoods> list=new ArrayList<OrderGoods>();
		Orders orders=new Orders();
		orders = ordersService.findselectByOrderId(orderid);
		returnedGoods.setUserId(orders.getUserid());
		list=ordergoodService.orderlist(orderid);
		
		model.addAttribute("orders", orders);
		model.addAttribute("orderGoodList", list);
		model.addAttribute("returnedGoods", returnedGoods);
		return "modules/ec/returnForm";
	}
	
	
	
	/**
	 * 查询用户等级折扣
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getUserLevel")
	public String getUserLevel(String mobile, HttpServletRequest request,HttpServletResponse response) {
		String diString=ordersService.getUserLevel(mobile);
		 
		return diString;
	}
	
	/**
	 * 导出订单数据
	 * @param orders
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
			Page<Orders> page = new Page<Orders>();
			if(StringUtils.isNotBlank(orders.getUsername()) || StringUtils.isNotBlank(orders.getMobile()) || StringUtils.isNotBlank(orders.getOrderid())){
				page = ordersService.newFindOrdersExcal(new Page<Orders>(request, response, -1), orders);
			}else{
				page = ordersService.findOrdersExcal(new Page<Orders>(request, response, -1), orders);
			}
			new ExportExcel("订单数据", Orders.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出订单数据失败", e);
			addMessage(redirectAttributes, "导出订单数据,失败!" );
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
	public void importFileTemplate(Orders orders,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "wuliuImport.xlsx";
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
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导入模板下载失败", e);
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
	
	}
	
	
	/**
	 * 导入物流数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import")
	public String importShipping(HttpServletRequest request,MultipartFile file, RedirectAttributes redirectAttributes) {
	
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
							failureMsg.append("<br/>参数异常，请核对订单号 ");
							failureNum++;
							continue;
						}
						if(ordersService.selectOrdersId(shipping.getOrderid()) != 1){
							failureMsg.append("<br/>订单" + shipping.getOrderid() + "不存在，无法导入 ");
							failureNum++;
							continue;
						}
						if(ordersService.selectOrdersStatus(shipping.getOrderid()).getIsReal() == 1){
							failureMsg.append("<br/>订单" + shipping.getOrderid() + "是虚拟订单，无法导入 ");
							failureNum++;
							continue;
						}
						if(ordersService.selectOrdersStatus(shipping.getOrderid()).getOrderstatus() == -1){
							failureMsg.append("<br/>订单" + shipping.getOrderid() + "的订单状态为待付款，无法导入 ");
							failureNum++;
							continue;
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
						
						//修改退货日期
						int returnDay = Integer.parseInt(ParametersFactory.getMtmyParamValues("returngoods_date"));
						if(-1 == returnDay){
							returnDay = 10;	//默认给一个10天（快递3天+7天退货）
						}
						Date returnTime = sdf.parse(OrderUtils.plusDay(returnDay, sdf.format(orders.getShippingtime())));
						orders.setReturnTime(returnTime);
						
						int index = ordersService.UpdateShipping(orders);
						if (index > 0) {		
							successNum++;
						} else {
							failureMsg.append("<br/>订单" + shipping.getOrderid() + "更新物流失败; ");
							failureNum++;
						}	

					} catch (ConstraintViolationException ex) {
						BugLogUtils.saveBugLog(request, "导入物流出错", ex);
						logger.error("导入物流出错："+ex.getMessage());
						failureMsg.append("<br/>订单 " + shipping.getOrderid() + " 更新物流失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ":");
						for (String message : messageList) {
							failureMsg.append(message + ";");
							failureNum++;
						}
					} catch (Exception ex) {
						BugLogUtils.saveBugLog(request, "导入物流出错", ex);
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
			BugLogUtils.saveBugLog(request, "导入物流出错", e);
			logger.error("导入物流出错："+e.getMessage());
			addMessage(redirectAttributes, "导入物流失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 计算
	 * @param transactionPrice  成交价
	 * @param actualPayment		实际付款
	 * @param ServiceNum		服务次数
	 * @param favourablePrice	优惠价
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "computingCost")
	public static Orders computingCost(double transactionPrice,double actualPayment,int ServiceNum, double favourablePrice,HttpServletRequest request){
		Orders orders = new Orders();
		try {
			DecimalFormat formater = new DecimalFormat("#0.##");
			double singleRealityPrice = Double.parseDouble(formater.format(transactionPrice/ServiceNum)); //实际服务单次价
			int actualNum = (int)Math.floor(actualPayment/singleRealityPrice);	//实际服务次数 = 剩余服务次数
			double spareMoney = Double.parseDouble(formater.format(actualPayment - singleRealityPrice*actualNum)); //余款
			double afterPayment = Double.parseDouble(formater.format(actualPayment-spareMoney));	//计算后实际付款
			double debtMoney = Double.parseDouble(formater.format(transactionPrice-afterPayment));//欠款
			orders.setActualNum(actualNum);
			orders.setSpareMoney(spareMoney);
			orders.setAfterPayment(afterPayment);
			orders.setDebtMoney(debtMoney);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "商品规格错误！", e);
		}
		return orders;
	}
	/**
	 * 获取用户信息
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getUser")
	public Orders getUser(String mobile){
		Orders orders = ordersService.getUser(mobile);
		return orders;
	}
	/**
	 * 跳转充值页面
	 * @param orderid
	 * @param recid
	 * @param userid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addTopUp")
	public String addTopUp(String orderid,double singleRealityPrice,int userid,int isReal,double goodsBalance,Model model){
		model.addAttribute("orderid", orderid);
		model.addAttribute("singleRealityPrice", singleRealityPrice);
		model.addAttribute("userid", userid);
		model.addAttribute("isReal", isReal);
		model.addAttribute("goodsBalance",goodsBalance);
		return "modules/ec/addTopUp";
	}
	/**
	 * 查询用户账户信息
	 * @param userid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAccount")
	public double getAccount(int userid){
		double accountNalance = ordersService.getAccount(userid);
		return accountNalance;
	}
	/**
	 * 新增订单充值日志记录
	 * @param oLog
	 */
	@ResponseBody
	@RequestMapping(value = "addOrderRechargeLog")
	public String addOrderRechargeLog(OrderRechargeLog oLog){
		String success="";
		try{
			ordersService.addOrderRechargeLog(oLog);
			success = "success";
		}catch(Exception e){
			e.printStackTrace();
			success = "error";
		}
		return success;
	}
	/**
	 * 修改订单
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "updateVirtualOrder")
	public String updateVirtualOrder(Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Orders newOrders = ordersService.selectOrderById(orders.getOrderid());
			orders.setInvoiceOvertime(newOrders.getInvoiceOvertime());
			orders.setIsReal(newOrders.getIsReal());
			//判断收货地址是否修改了，若未修改则xml中不对address更新，若不修改，则将省市县详细地址存到相应的地方
			if(!orders.getOldAddress().equals(orders.getAddress())){
				ordersDao.updateAddress(orders);
			}
			
			if(orders.getOldstatus() != orders.getOrderstatus()){ //2次订单修改状态不一致
				if(-2 == orders.getOrderstatus()){//新订单状态 等于“取消订单”
					boolean result = returnRepository(orders.getOrderid(),request);
					if(result){
						// 取消订单  修改订单取消类型为后台取消
						orders.setCancelType("1");
						ordersService.updateVirtualOrder(orders);
						addMessage(redirectAttributes, "修改订单'" + orders.getOrderid() + "'成功");
					}else{
						addMessage(redirectAttributes, "修改订单'" + orders.getOrderid() + "'失败");
					}
					logger.info("商品退还仓库是否成功："+result);
				}else{//不是取消订单
					ordersService.updateVirtualOrder(orders);
					addMessage(redirectAttributes, "修改订单'" + orders.getOrderid() + "'成功");
				}
			}else{
				ordersService.updateVirtualOrder(orders);
				addMessage(redirectAttributes, "修改订单'" + orders.getOrderid() + "'成功");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改订单", e);
			logger.error("方法：updateVirtualOrder，修改订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "修改订单失败！");
		}
		
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 查看商品订单充值记录
	 * @param recid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getMappinfOrderView")
	public String getMappinfOrderRechargeView(Integer recid, String orderid, String orderType, HttpServletRequest request, Model model) {
		try {
			List<OrderGoodsDetails> orderGoodsDetails = ordersService.getMappinfOrderView(recid);
			/*List<OrderRechargeLog> orderRechargeLogs = ordersService.getOrderRechargeView(orderid);*/
			model.addAttribute("orderGoodsDetails", orderGoodsDetails);
			/*model.addAttribute("orderRechargeLogs", orderRechargeLogs);*/
			model.addAttribute("orderType", orderType);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看商品订单充值", e);
			logger.error("方法：mappinfOrderView，查看商品订单充值出现错误：" + e.getMessage());
		}
		return "modules/ec/mappinfOrderView";
	}
	
	/**
	 * 查看主订单充值记录
	 * @param orderid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getOrderRechargeView")
	public String getOrderRechargeView(String orderid,String orderType, HttpServletRequest request, Model model) {
		try {
			List<OrderRechargeLog> orderRechargeLogs = ordersService.getOrderRechargeView(orderid);
			model.addAttribute("orderRechargeLogs", orderRechargeLogs);
			model.addAttribute("orderType", orderType);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看商品订单充值", e);
			logger.error("方法：mappinfOrderView，查看商品订单充值出现错误：" + e.getMessage());
		}
		return "modules/ec/mappinfOrderView";
	}
	/**
	 * 跳转提成页面
	 * @param orderid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getPushmoneyView")
	public String getPushmoneyView(String orderid, HttpServletRequest request, Model model) {
		try {
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转提成页面错误", e);
			logger.error("方法：getPushmoneyView，跳转提成页面出现错误：" + e.getMessage());
		}
		return "modules/ec/pushmoneyView";
	}
	
	
	/**
	 * 跳转实物订单创建页
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createKindOrder")
	public String createKindOrder(HttpServletRequest request,Orders orders,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建实物订单页面", e);
			logger.error("方法：createOrder，跳转创建实物订单页面出错：" + e.getMessage());
		}
		return "modules/ec/createKindOrder";
	}
	
	/**
	 * 添加实物订单
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveKindOrder")
	public String saveKindOrder(Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveKindOrder(orders);
			addMessage(redirectAttributes, "创建实物订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建实物订单", e);
			logger.error("方法：saveOrder，创建实物订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建实物订单失败！");
		}

		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 实物订单添加商品
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addKindOderGoods")
	public String addKindOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			model.addAttribute("orders", orders);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转实物添加商品页面", e);
			logger.error("跳转实物添加商品页面：" + e.getMessage());
		}

		return "modules/ec/addKindOderGoodsForm";
	}
	
	/**
	 * 跳转订单备注
	 * @param orderid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getRemarksView")
	public String getRemarksView(String orderid, HttpServletRequest request, Model model) {
		return "modules/ec/remarksView";
	}
	
	/**
	 * 删除提成人员信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteMtmyUserInfo")
	public String deleteMtmyUserInfo(Integer mtmyUserId, HttpServletRequest request, HttpServletResponse response) {
		String type="";
		try {
			ordersService.deleteMtmyUserInfo(mtmyUserId);
			type = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除提成人员信息错误", e);
			logger.error("删除提成人员信息错误：" + e.getMessage());
			type = "error";
		}
		return type;
	}
	
	/**
	 * 删除订单备注
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteOrderRemarksLog")
	public String deleteOrderRemarksLog(Integer orderRemarksId, HttpServletRequest request, HttpServletResponse response) {
		String type="";
		try {
			ordersService.deleteOrderRemarksLog(orderRemarksId);
			type = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除备注信息错误", e);
			logger.error("删除备注信息错误：" + e.getMessage());
			type = "error";
		}
		return type;
	}
	
	/**
	 * 添加订单备注
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrderRemarksLog")
	public String saveOrderRemarksLog(Orders orders, HttpServletRequest request, HttpServletResponse response) {
		String type="";
		try {
			ordersService.saveOrderRemarksLog(orders);
			type = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加备注信息错误", e);
			logger.error("添加备注信息错误：" + e.getMessage());
			type = "error";
		}
		return type;
	}
	
	/**
	 * 添加单条提成人员信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrderPushmoneyRecord")
	public String saveOrderPushmoneyRecord(OrderPushmoneyRecord orderPushmoneyRecord, HttpServletRequest request, HttpServletResponse response) {
		String type="";
		try {
			ordersService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
			type = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加单条提成人员信息错误", e);
			logger.error("添加单条提成人员信息错误：" + e.getMessage());
			type = "error";
		}
		return type;
	}
	
	/**
	 * 计算订单欠费
	 * @param id
	 * @param isReal
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderGoodsIsFre")
	public double getOrderGoodsIsFre(String id,String isReal,String orderid, HttpServletRequest request, HttpServletResponse response) {
		double returnAmount=0;  // -2 分销 未处理   -1  无退款余额
		DecimalFormat    df   = new DecimalFormat("######0.00");  
		try {
			Orders orders=ordersService.selectByOrderIdSum(orderid);
			if(orders.getFlag()==0){
				if(orders.getOrderamount()==orders.getTotalamount()){
					returnAmount=-2;
				}else{
					GoodsDetailSum detil=new GoodsDetailSum();
					detil=ordersService.selectDetaiSum(id);
					if(detil!=null){
						if("0".equals(isReal)){
							if(detil.getOrderAmount()==detil.getDetaiAmount()){
								returnAmount=detil.getDetaiAmount()/detil.getGoodsNum();
								returnAmount=Double.parseDouble(df.format(returnAmount));
							}else{
								returnAmount=-1;
							}
						}else if("1".equals(isReal)){
							if(detil.getTimes()>0){
								returnAmount=detil.getSingleRealityPrice()*detil.getTimes();
								returnAmount=Double.parseDouble(df.format(returnAmount));
								if(returnAmount>detil.getOrderAmount()){
									returnAmount=detil.getOrderAmount();
								}
							}else{
								returnAmount=-1;
							}
						}
					}else{
						returnAmount=-1;
					}
				}
			}else if(orders.getFlag()==1){
				GoodsDetailSum detil=new GoodsDetailSum();
				detil=ordersService.selectDetaiSum(id);
				if(detil!=null){
					if("0".equals(isReal)){
						if(detil.getOrderAmount()==detil.getDetaiAmount()){
							returnAmount=detil.getDetaiAmount()/detil.getGoodsNum();
							returnAmount=Double.parseDouble(df.format(returnAmount));
						}else{
							returnAmount=-1;
						}
					}else if("1".equals(isReal)){
						if(detil.getTimes()>0){
							returnAmount=detil.getSingleRealityPrice()*detil.getTimes();
							returnAmount=Double.parseDouble(df.format(returnAmount));
							if(returnAmount>detil.getOrderAmount()){
								returnAmount=detil.getOrderAmount();
							}
						}else{
							returnAmount=-1;
						}
					}
				}else{
					returnAmount=-1;
				}
			}
			
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "计算欠款错误", e);
			logger.error("计算欠款错误：" + e.getMessage());
			
		}
		return returnAmount;
	}
	
	/**
	 * 取消订单
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "cancellationOrder")
	public String cancellationOrder(HttpServletRequest request, Orders orders,RedirectAttributes redirectAttributes) {
		try {
			List<OrderGoods> goodsList=new ArrayList<OrderGoods>();
			boolean result = returnRepository(orders.getOrderid(),request);
			if(result){
				ordersService.cancellationOrder(orders);
				goodsList=ordergoodService.orderlist(orders.getOrderid());
				//验证是否为抢购活动订单
				for (int i = 0; i < goodsList.size(); i++){
					if(goodsList.get(i).getActiontype()==1){
						redisClientTemplate.hincrBy(buying_limit_prefix+goodsList.get(i).getActionid(), goodsList.get(i).getUserid()+"_"+goodsList.get(i).getGoodsid(),-goodsList.get(i).getGoodsnum());
					}
				}
				addMessage(redirectAttributes, "取消订单'" + orders.getOrderid() + "'成功");
			}else{
				addMessage(redirectAttributes, "取消订单'" + orders.getOrderid() + "'失败");
			}
			logger.info("商品退还仓库是否成功："+result);
		} catch (Exception e) {
			addMessage(redirectAttributes, "取消订单'" + orders.getOrderid() + "'失败");
			BugLogUtils.saveBugLog(request, "取消订单失败", e);
			logger.error("取消订单失败：" + e.getMessage());
		}

		return "redirect:" + adminPath + "/ec/orders/list";
	}
	/**
	 * 强制取消
	 * @param id
	 * @param isReal
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "forcedCancel")
	public String forcedCancel(Orders orders, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			List<OrderGoods> goodsList=new ArrayList<OrderGoods>();
			
			orders = ordersService.findselectByOrderId(orders.getOrderid());
			boolean result = returnRepository(orders.getOrderid(),request);
			if(!result){
				logger.error("取消库存失败，导致强制取消无法进行");
				addMessage(redirectAttributes, "强制取消'" + orders.getOrderid() + "'失败！");
				return "redirect:" + adminPath + "/ec/orders/orderform?type=view&orderid="+orders.getOrderid();
			}
			logger.info("商品退还仓库是否成功："+result);
			goodsList=ordergoodService.orderlist(orders.getOrderid());
			if(goodsList.size()>0){
				for (int i = 0; i < goodsList.size(); i++) {
					Date date=new Date();
					SimpleDateFormat simd=new SimpleDateFormat("YYYYMMddHHmmssSSS");
					String str=simd.format(date);
					String id="01"+str+orders.getUserid();
					ReturnedGoods returnedGoods=new ReturnedGoods();
					if(orders!=null){
						if(orders.getOfficeId()!= null){
							returnedGoods.setOfficeId(orders.getOfficeId());
						}else{
							returnedGoods.setOfficeId("1000001");
						}
						
						returnedGoods.setIsReal(orders.getIsReal());
					}
					returnedGoods.setGoodsMappingId(goodsList.get(i).getRecid()+"");
					returnedGoods.setIsStorage("1");
					returnedGoods.setReturnStatus("15");
					returnedGoods.setIsReal(orders.getIsReal());
					returnedGoods.setUserId(orders.getUserid());
					returnedGoods.setOrderId(orders.getOrderid());
					returnedGoods.setReturnReason("强制取消");
					returnedGoods.setProblemDesc("强制取消");
					returnedGoods.setRemarks("强制取消");
					returnedGoods.setReturnNum(goodsList.get(i).getGoodsnum());
					returnedGoods.setTotalAmount(goodsList.get(i).getTotalAmount());
					returnedGoods.setOrderAmount(goodsList.get(i).getOrderAmount());
					returnedGoods.setReturnAmount(goodsList.get(i).getTotalAmount());
					returnedGoods.setId(id);
					returnedGoods.setApplyType(0);
					returnedGoods.setApplyDate(date);
					//保存到退货表
					returnedGoodsService.insertForcedCancel(returnedGoods);
					ordersService.updateOrderStatut(orders.getOrderid());
					//验证是否为抢购活动订单
					if(goodsList.get(i).getActiontype()==1){
						redisClientTemplate.hincrBy(buying_limit_prefix+goodsList.get(i).getActionid(), orders.getUserid()+"_"+goodsList.get(i).getGoodsid(),-goodsList.get(i).getGoodsnum());
					}
				}
			}
			//查询是否有退货记录
			if(saleRebatesLogDao.selectNumByOrderId(orders.getOrderid()) == 0){//如果无退货记录
				saleRebatesLogDao.updateSale(orders.getOrderid());// 插入分销日志
			}
			addMessage(redirectAttributes, "强制取消'" + orders.getOrderid() + "'成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "强制取消错误", e);
			logger.error("强制取消错误：" + e.getMessage());
			addMessage(redirectAttributes, "强制取消'" + orders.getOrderid() + "'失败！");
		}
		//return "redirect:" + adminPath + "/ec/orders/list";
		return "redirect:" + adminPath + "/ec/orders/orderform?type=view&orderid="+orders.getOrderid();
	}
	/**
	 * 归还仓库
	 * @return
	 */
	public boolean returnRepository(String orderId,HttpServletRequest request){
//		String weburl = ParametersFactory.getMtmyParamValues("mtmy_incrstore_url");
//		List<OrderGoods> orderGoods = ordergoodService.getGoodMapping(orderId);
//		if (null != orderGoods && orderGoods.size() > 0) {
//			for (OrderGoods orderGood : orderGoods) {
//				String parpm = "{\"goods_id\":"+orderGood.getGoodsid()+",\"spec_key\":\""+orderGood.getSpeckey()+"\",\"count\":"+orderGood.getGoodsnum()+"}";
//				String result = WebUtils.postObject(parpm, weburl);
//				JSONObject jsonObject = JSONObject.fromObject(result);
//				String code = jsonObject.get("code").toString();
//				if(!"200".equals(code)){
//					return false;
//				}
//			}
//		}
		try {
			List<OrderGoods> orderGoods = ordergoodService.getGoodMapping(orderId);
			if (null != orderGoods && orderGoods.size() > 0) {
				for (OrderGoods orderGood : orderGoods) {
					boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+orderGood.getGoodsid()+"#"+orderGood.getSpeckey());
					if(str){
						RedisLock redisLock = new RedisLock(redisClientTemplate, RedisConfig.GOODS_SPECPRICE_PREFIX+orderGood.getGoodsid()+"#"+orderGood.getSpeckey());
						redisLock.lock();
						redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+orderGood.getGoodsid()+"#"+orderGood.getSpeckey(), orderGood.getGoodsnum());
						redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+orderGood.getGoodsid(),orderGood.getGoodsnum());
						redisLock.unlock();
					}
				}
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "归还仓库错误", e);
			logger.error("归还仓库错误：" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 跳转处理预约金页面
	 * @param orderGoods
	 * @param request
	 * @return
	 */
	@RequestMapping(value="handleAdvanceFlagForm")
	public String handleAdvanceFlagForm(OrderGoods orderGoods,int userid,HttpServletRequest request,Model model){
		DecimalFormat formater = new DecimalFormat("#0.##");
		try{
			orderGoods = ordersService.selectOrderGoodsByRecid(orderGoods.getRecid());
			double advance = orderGoods.getAdvancePrice();                 //预约金
			
			double singleRealityPrice = orderGoods.getSingleRealityPrice();   //服务单次价
			orderGoods.setAdvance(advance);
			if(advance < singleRealityPrice){
				double c = Double.parseDouble(formater.format(singleRealityPrice - advance));
				orderGoods.setAdvanceServiceTimes(0);        //服务次数
				orderGoods.setDebt(c);                       //欠款
			}else{
				int a = (int)(advance/singleRealityPrice);
				double b = Double.parseDouble(formater.format(advance - a*singleRealityPrice));
				orderGoods.setAdvanceServiceTimes(a);        //服务次数 
				orderGoods.setAdvanceBalance(b);             //余额
			}
			double accountBalance = ordersService.getAccount(userid); //用户账户余额
			orderGoods.setAccountBalance(accountBalance);
			model.addAttribute("orderGoods", orderGoods);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转处理预约金页面出错", e);
			logger.error("跳转处理预约金页面出错："+e.getMessage());
		}
		return "modules/ec/handleAdvanceFlagForm";
	}
	
	/**
	 * 处理预约金
	 * @param oLog
	 * @param orderGoods
	 * @param userid
	 * @param orderid
	 * @param sum
	 * @return
	 */
	@RequestMapping(value="handleAdvanceFlag")
	@ResponseBody
	public String handleAdvanceFlag(OrderRechargeLog oLog,OrderGoods orderGoods,int sum,int userid,String orderid,HttpServletRequest request){
		String date="";
		DecimalFormat formater = new DecimalFormat("#0.##");
		try{
			orderGoods = ordersService.selectOrderGoodsByRecid(orderGoods.getRecid());    
			double detailsTotalAmount = orderGoods.getTotalAmount();       //预约金用了红包、折扣以后实际付款的钱
			double advance = orderGoods.getAdvancePrice();                 //预约金
			
			double singleRealityPrice = orderGoods.getSingleRealityPrice();   //服务单次价
			double goodsPrice = orderGoods.getGoodsprice();        //商品优惠单价
			int goodsType = orderGoods.getGoodsType();                    //商品区分(0: 老商品 1: 新商品)
			String officeId = orderGoods.getOfficeId();           //组织架构ID
			
			oLog.setMtmyUserId(userid);
			oLog.setOrderId(orderid);
			oLog.setRecid(orderGoods.getRecid());
			oLog.setSingleRealityPrice(orderGoods.getSingleRealityPrice());
			oLog.setSingleNormPrice(orderGoods.getSingleNormPrice());
			oLog.setAdvance(advance);
			//若订金小于单次价，则实付款金额就是单次价，
			if(advance < singleRealityPrice){
				oLog.setTotalAmount(singleRealityPrice);
				if(sum == 0){//用了账户余额
					oLog.setAccountBalance(Double.parseDouble(formater.format(singleRealityPrice-advance))); //这里的账户余额是用了多少账户余额，不是账户里有多少余额
				}else{
					oLog.setAccountBalance(0);
				}
			}else{   //若订金大于等于单次价，则实付款金额就是订金，充值金额也是订金
				oLog.setTotalAmount(advance);
				
			}
			orderGoodsDetailsService.updateAdvanceFlag(orderGoods.getRecid()+"");
			ordersService.handleAdvanceFlag(oLog,goodsPrice,detailsTotalAmount,goodsType,officeId);
			date = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "处理预约金异常", e);
			date = "error";
		}
		return date;
	}
	
	/**
	 * 跳转到导入虚拟订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importVirtualOrdersPage")
	public String importVirtualOrdersPage() {
		return "modules/ec/importVirtualOrdersPage";
	}
	
	
	/**
	 * 下载导入虚拟订单模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "importVirtualOrders/template")
	public void importVirtualOrdersTemplate(Orders orders,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "virtualOrdersImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[虚拟订单模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[虚拟订单模板-new-path"+path);
		
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
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导入模板下载失败", e);
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
	}
	
	/**
	 * 导入虚拟订单
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "importVirtualOrders")
	public String importVirtualOrders(HttpServletRequest request,MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			String str1 = "#售前卖#,#售后卖#,#老带新#";
			String str2 = "#微信支付#,#支付宝App支付#,#银联#,#微信公众号#,#微信公众号扫码#,#支付宝手机网页#,#支付宝扫码支付#,#现金支付#,#易宝手机网页支付#";
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			int cell = ei.getLastCellNum();
			if (cell != 12) {
				failureMsg.insert(0, "<br/>导入的模板错误，请检查模板; ");
			} else {
				
				List<ImportVirtualOrders> list = ei.getDataList(ImportVirtualOrders.class);
				for (ImportVirtualOrders importVirtualOrders : list) {
					try {
						BeanValidators.validateWithException(validator, importVirtualOrders);
						if(str1.indexOf("#"+importVirtualOrders.getDistinction()+"#") != -1){
							if(goodsSpecPriceDao.checkGoodsSpecKey(Integer.valueOf(importVirtualOrders.getGoodsId()), importVirtualOrders.getSpecKey()) == 1){
								if(goodsSpecPriceDao.checkServiceTimes(Integer.valueOf(importVirtualOrders.getGoodsId()), importVirtualOrders.getSpecKey()) >= Integer.valueOf(importVirtualOrders.getActualTimes())){
									if(mtmyUsersDao.checkMobile(importVirtualOrders.getMobile()) != 0){
										if(str2.indexOf("#"+importVirtualOrders.getPayCode()+"#") != -1){
											try {
												Orders orders = new Orders();
												String distinction = importVirtualOrders.getDistinction();  //导入的订单性质（0：电商；1：售前卖；2：售后卖；3：老带新）
												if("售前卖".equals(distinction)){
													orders.setDistinction(1);
												}else if("售后卖".equals(distinction)){
													orders.setDistinction(2);
												}else if("老带新".equals(distinction)){
													orders.setDistinction(3);
												}
												
												int goodsId = Integer.valueOf(importVirtualOrders.getGoodsId());  //导入的商品id
												String specKey = importVirtualOrders.getSpecKey();              //导入的规格id
												String mobile = importVirtualOrders.getMobile();        //手机号码
												int actualTimes = Integer.valueOf(importVirtualOrders.getActualTimes());    //实际次数
												String note = importVirtualOrders.getNote();                           //留言备注
												String payCode = importVirtualOrders.getPayCode();            //付款方式
												
												int mtmyUserId = mtmyUsersDao.getUserByMobile(mobile).getUserid();//每天每耶用户id
												orders.setUserid(mtmyUserId);
												
												List<Integer> goodselectIds = new ArrayList<Integer>();      //商品id集合 
												goodselectIds.add(goodsId);
												orders.setGoodselectIds(goodselectIds);
												
												List<String> speckeys = new ArrayList<String>();             //规格key集合
												speckeys.add(specKey);
												orders.setSpeckeys(speckeys);
												
												List<Double> orderAmounts = new ArrayList<Double>();        //成交价集合
												GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
												goodsSpecPrice.setGoodsId(String.valueOf(goodsId));
												goodsSpecPrice.setSpecKey(importVirtualOrders.getSpecKey());
												orderAmounts.add(goodsSpecPriceDao.getSpecPrce(goodsSpecPrice).getPrice());
												orders.setOrderAmounts(orderAmounts);
												
												List<Double> actualPayments = new ArrayList<Double>();      //实际付款集合
												actualPayments.add(goodsSpecPriceDao.getSpecPrce(goodsSpecPrice).getPrice());
												orders.setActualPayments(actualPayments);
												
												List<Integer> remaintimeNums = new ArrayList<Integer>();    //虚拟订单老产品-实际次数
												remaintimeNums.add(actualTimes);
												orders.setRemaintimeNums(remaintimeNums);
												
												orders.setIsNeworder(1);
												orders.setOrderstatus(4);
												if(!"".equals(note) || note != null){
													orders.setUsernote(note);
												}
												
												if("微信支付".equals(payCode)){
													orders.setPaycode("wx");
												}else if("支付宝App支付".equals(payCode)){
													orders.setPaycode("alipay");
												}else if("银联".equals(payCode)){
													orders.setPaycode("upacp_wap");
												}else if("微信公众号".equals(payCode)){
													orders.setPaycode("wx_pub");
												}else if("微信公众号扫码".equals(payCode)){
													orders.setPaycode("wx_pub_qr");
												}else if("支付宝手机网页".equals(payCode)){
													orders.setPaycode("alipay_wap");
												}else if("支付宝扫码支付".equals(payCode)){
													orders.setPaycode("alipay_qr");
												}else if("现金支付".equals(payCode)){
													orders.setPaycode("money");
												}else if("易宝手机网页支付".equals(payCode)){
													orders.setPaycode("yeepay_wap");
												}
										
												ordersService.saveVirtualOrder(orders);
												successNum++;
			
											} catch (Exception e) {
												e.getMessage();
												BugLogUtils.saveBugLog(request, "导入保存失败", e);
												failureMsg.append("<br/>导入保存失败 " + importVirtualOrders.getGoodsId()+","+importVirtualOrders.getSpecKey()+","+importVirtualOrders.getMobile());
												failureNum++;
											}
										}else{
											failureMsg.append("<br/>商品id:"+importVirtualOrders.getGoodsId()+"与商品规格:"+importVirtualOrders.getSpecKey() + "的付款方式有误");
											failureNum++;
										}
									}else{
										failureMsg.append("<br/>商品id:"+importVirtualOrders.getGoodsId()+"与商品规格:"+importVirtualOrders.getSpecKey() + "的手机号码有误");
										failureNum++;
									}
								}else{
									failureMsg.append("<br/>商品id:"+importVirtualOrders.getGoodsId()+"与商品规格:"+importVirtualOrders.getSpecKey()+"的实际次数有误");
									failureNum++;	
								}
							}else{
								failureMsg.append("<br/>商品id:"+importVirtualOrders.getGoodsId()+"与商品规格:"+importVirtualOrders.getSpecKey()+"不匹配;");
								failureNum++;	
							}
						}else{
							failureMsg.append("<br/>订单性质:"+importVirtualOrders.getDistinction()+",此订单性质有误");
							failureNum++;	
						}
					} catch (ConstraintViolationException ex) {
						logger.error("导入虚拟订单出错："+ex.getMessage());
						failureMsg.append("<br/>订单 " + importVirtualOrders.getGoodsId()+","+importVirtualOrders.getSpecKey()+","+importVirtualOrders.getMobile() + " 导入失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ":");
						for (String message : messageList) {
							failureMsg.append(message + ";");
						}
						failureNum++;
					} catch (Exception ex) {
						BugLogUtils.saveBugLog(request, "导入虚拟订单出错", ex);
						logger.error("导入虚拟订单出错："+ex.getMessage());
						failureMsg.append("<br/>订单 " + importVirtualOrders.getGoodsId()+","+importVirtualOrders.getSpecKey()+","+importVirtualOrders.getMobile() + " 导入失败：");
					}
				}
				
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条，虚拟订单导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条订单。" + failureMsg);
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导入虚拟订单出错", e);
			logger.error("导入虚拟订单出错："+e.getMessage());
			addMessage(redirectAttributes, "导入虚拟订单出错失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
}

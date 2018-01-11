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
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.ReturnedGoodsDao;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.AcountLog;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.ImportVirtualOrders;
import com.training.modules.ec.entity.IntegralsLog;
import com.training.modules.ec.entity.OfficeAccount;
import com.training.modules.ec.entity.OfficeAccountLog;
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
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.ec.service.AcountLogService;
import com.training.modules.ec.service.OrderGoodsDetailsService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.OrderPushmoneyRecordService;
import com.training.modules.ec.service.OrdersLogService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.service.PaymentService;
import com.training.modules.ec.service.ReturnGoodsService;
import com.training.modules.ec.service.ReturnedGoodsService;
import com.training.modules.ec.service.TurnOverDetailsService;
import com.training.modules.ec.utils.CourierUtils;
import com.training.modules.ec.utils.OrderUtils;
import com.training.modules.ec.utils.OrdersStatusChangeUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
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
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private OrderPushmoneyRecordService orderPushmoneyRecordService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TurnOverDetailsService turnOverDetailsService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ReturnedGoodsDao returnedGoodsDao;

	public static final String MTMY_ID = "mtmy_id_";//用户云币缓存前缀
	
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
		String pushMoneryDetails = "";
		int num = 0;
		try {
			User user = UserUtils.getUser(); //登陆用户
			List<Payment> paylist = paymentService.paylist();
			orders = ordersService.selectOrderById(orders.getOrderid());
			List<TurnOverDetails> turnOverDetailsList = turnOverDetailsService.selectDetailsByOrderId(orders.getOrderid());
			
			List<TurnOverDetails> pushmoneyRecordList = turnOverDetailsService.selectPushDetails(orders.getOrderid());
			if(pushmoneyRecordList.size() > 0){
				for(TurnOverDetails turnOverDetails:pushmoneyRecordList){
					num = turnOverDetails.getPushMoneyList().size();
					if(num == 0){
						num = 1;
					}
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(turnOverDetails.getCreateDate());
					pushMoneryDetails = pushMoneryDetails + 
							"<tr> "+
								"<td align='center' rowspan="+num+">"+date+"</td> ";
					if(turnOverDetails.getType() == 1){
						pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+num+">下单</td> ";
					}else if(turnOverDetails.getType() == 2){
						pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+num+">还款</td> ";
					}
					pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+num+">"+turnOverDetails.getAmount()+"</td> ";
					if(turnOverDetails.getPushMoneyList().size() == 0){
						pushMoneryDetails = pushMoneryDetails + "<td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'>";
						if("view".equals(type)){
							pushMoneryDetails = pushMoneryDetails + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs'><i class='fa fa-edit'></i>编辑</a>";
						}else{
							pushMoneryDetails = pushMoneryDetails +"<a href='#' onclick='editSysUserInfo("+"\""+turnOverDetails.getTurnOverDetailsId()+"\")' class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>";
						}
						pushMoneryDetails = pushMoneryDetails	
								+ "</td>"
							+"</tr>";
					}else{
						List<OrderPushmoneyRecord> list = turnOverDetails.getPushMoneyList();
							pushMoneryDetails = pushMoneryDetails + "<td align='center'>"+list.get(0).getPushmoneyUserName()+"</td>"
								+"<td align='center'>"+list.get(0).getDepartmentName()+"</td>"
							    +"<td align='center'>"+list.get(0).getPushmoneyUserMobile()+"</td>"
								+"<td align='center'>"+list.get(0).getPushMoney()+"</td>"
								+"<td align='center'rowspan="+num+">";
								if("view".equals(type)){
									pushMoneryDetails = pushMoneryDetails + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs'><i class='fa fa-edit'></i>编辑</a>";
								}else{
									pushMoneryDetails = pushMoneryDetails +"<a href='#' onclick='editSysUserInfo("+"\""+turnOverDetails.getTurnOverDetailsId()+"\")' class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>";
								}
								pushMoneryDetails = pushMoneryDetails 
										+"<a href=\"#\" onclick=\"openDialogView('操作日志', '/kenuo/a/ec/orders/operationLog?turnOverDetailsId="+turnOverDetails.getTurnOverDetailsId()+"','800px','550px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i>操作日志</a>"
								+"</td>"                                                                                
							+"</tr>";
						for(int i= 1;i<list.size();i++){
							pushMoneryDetails = pushMoneryDetails + 
									"<tr>"
										+"<td align='center'>"+list.get(i).getPushmoneyUserName()+"</td>"
										+"<td align='center'>"+list.get(i).getDepartmentName()+"</td>"
										+"<td align='center'>"+list.get(i).getPushmoneyUserMobile()+"</td>"
										+"<td align='center'>"+list.get(i).getPushMoney()+"</td>"
									+"</tr>";
						}
					}
				}	
			}
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("user", user);
			model.addAttribute("type", type);
			model.addAttribute("turnOverDetailsList",turnOverDetailsList);
			model.addAttribute("pushMoneryDetails",pushMoneryDetails);
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
			
			//判断修改物流之前是否存在物流单号信息
			String shopcodes = ordersService.getShippingcodeByid(orders);
			ordersService.UpdateShipping(orders);//修改物流信息
			if(StringUtils.isEmpty(shopcodes)){//为空:推送消息,修改订单状态
				ordersService.updateOrdersStatus(orders);//第一次修改物流信息时,修改订单状态
				OrdersStatusChangeUtils.pushMsg(orders, 3); //推送已发货信息给用户
			}
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
	 * 保存退货订单数据(实物商品)
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveReturnKind")
	public String saveReturnKind(ReturnedGoods returnedGoods, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			returnedGoodsService.saveReturnKind(returnedGoods);
			addMessage(redirectAttributes, "保存实物商品退货订单'" + returnedGoods.getOrderId() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存实物商品退货订单", e);
			logger.error("方法：saveReturn,保存实物商品退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存实物商品退货订单失败！");
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	/**
	 * 保存退货订单数据(虚拟商品)
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveReturn")
	public String saveReturn(ReturnedGoods returnedGoods, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			returnedGoodsService.saveReturn(returnedGoods);
			addMessage(redirectAttributes, "保存虚拟商品退货订单'" + returnedGoods.getOrderId() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存虚拟商品退货订单", e);
			logger.error("方法：saveReturn,保存虚拟商品退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存虚拟商品退货订单失败！");
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	/**
	 * 保存退货订单数据(套卡商品)
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveReturnSuit")
	public String saveReturnSuit(ReturnedGoods returnedGoods, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			returnedGoodsService.saveReturnSuit(returnedGoods);
			addMessage(redirectAttributes, "保存套卡商品退货订单'" + returnedGoods.getOrderId() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存套卡商品退货订单", e);
			logger.error("方法：saveReturn,保存套卡商品退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存套卡商品退货订单失败！");
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	/**
	 * 保存退货订单数据(通用卡商品)
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveReturnCommon")
	public String saveReturnCommon(ReturnedGoods returnedGoods, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			returnedGoodsService.saveReturnCommon(returnedGoods);
			addMessage(redirectAttributes, "保存通用卡商品退货订单'" + returnedGoods.getOrderId() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存通用卡商品退货订单", e);
			logger.error("方法：saveReturn,保存通用卡商品退货订单出错：" + e.getMessage());
			addMessage(redirectAttributes, "保存通用卡商品退货订单失败！");
		}
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
		
		List<OrderGoods> list=new ArrayList<OrderGoods>();
		Orders orders = ordersService.findselectByOrderId(orderid);
		returnedGoods.setUserId(orders.getUserid());
		
		model.addAttribute("orders", orders);
		model.addAttribute("returnedGoods", returnedGoods);
		
		String suitCardSons = "";
		String isreal = "";
		int num;
		List<OrderGoods> resultSon = new ArrayList<OrderGoods>();//存放一个卡项商品和它的子项
		List<List<OrderGoods>> result = new ArrayList<List<OrderGoods>>();//存放每个卡项商品和它的子项集合
		
		//虚拟和实物售后时
		list=ordergoodService.orderlist(orderid);
		model.addAttribute("orderGoodList", list);
		
		if(returnedGoods.getIsReal() == 2 || returnedGoods.getIsReal() == 3){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGroupId() == 0 && resultSon.size() > 0){
					result.add(resultSon);
					resultSon = new ArrayList<OrderGoods>();
				}
				resultSon.add(list.get(i));
				if(i == (list.size()-1)){                        //将最后一次循环的结果放到集合里
					result.add(resultSon);
				}
			}
		}
		if(returnedGoods.getIsReal() == 2){//套卡 售后
			if(result.size() > 0){
				for(List<OrderGoods> lists:result){                            
					if((lists.size() - 1) > 0){
						num = lists.size() - 1;
						OrderGoods father = lists.get(0);
						isreal = lists.get(1).getIsreal()==0?"实物":"虚拟";
						suitCardSons = suitCardSons +
								"<tr> "+
								"<td rowspan='"+num+"'><input id='selectId' name='selectId' type='radio' value='"+father.getRecid()+"'  class='form'  onchange='selectFunction(this)'/></td>"+
								"<td align='center' rowspan='"+num+"'> "+father.getGoodsname()+"</td> "+
								"<td align='center' rowspan='"+num+"'>套卡</td> "+
								"<td align='center'> "+lists.get(1).getGoodsname()+"</td> "+
								"<td align='center'> "+isreal+"</td> "+
								"<td align='center'> "+lists.get(1).getMarketprice()+"</td> "+
								"<td align='center'> "+lists.get(1).getGoodsprice()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getCostprice()+"</td> "+
								"<td align='center'> "+lists.get(1).getGoodsnum()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getOrderAmount()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getTotalAmount()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getOrderArrearage()+"</td> "+
							"</tr> ";
						suitCardSons = suitCardSons +
								"<input type='hidden' id='"+father.getRecid()+"orderAmount' name='orderAmount' value='"+father.getOrderAmount()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"totalAmount' name='totalAmount' value='"+father.getTotalAmount()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"advanceFlag' name='advanceFlag' value='"+father.getAdvanceFlag()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"orderArrearage' name='orderArrearage' value='"+father.getOrderArrearage()+"' />";
						for(int i=2;i<lists.size();i++){
							isreal = lists.get(i).getIsreal()==0?"实物":"虚拟";
							suitCardSons = suitCardSons +
								"<tr> "+
									"<td align='center'> "+lists.get(i).getGoodsname()+"</td> "+
									"<td align='center'> "+isreal+"</td> "+
									"<td align='center'> "+lists.get(i).getMarketprice()+"</td> "+
									"<td align='center'> "+lists.get(i).getGoodsprice()+"</td> "+
									"<td align='center'> "+lists.get(i).getGoodsnum()+"</td> "+
								"</tr>";
						}
			
					}
				}
			}
			model.addAttribute("suitCardSons", suitCardSons);
			return "modules/ec/returnFormCardSuit";
		}else if(returnedGoods.getIsReal() == 3){//通用卡 售后
			if(result.size() > 0){
				for(List<OrderGoods> lists:result){                            
					if((lists.size() - 1) > 0){
						num = lists.size() - 1;
						OrderGoods father = lists.get(0);
						isreal = lists.get(1).getIsreal()==0?"实物":"虚拟";
						suitCardSons = suitCardSons +
								"<tr> "+
								"<td rowspan='"+num+"'><input id='selectId' name='selectId' type='radio' value='"+father.getRecid()+"'  class='form'  onchange='selectFunction(this)'/></td>"+
								"<td align='center' rowspan='"+num+"'> "+father.getGoodsname()+"</td> "+
								"<td align='center' rowspan='"+num+"'>通用卡</td> "+
								"<td align='center' rowspan='"+num+"'>"+father.getSpeckeyname()+"</td> "+
								"<td align='center'> "+lists.get(1).getGoodsname()+"</td> "+
								"<td align='center'> "+isreal+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getMarketprice()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getGoodsprice()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getCostprice()+"</td> "+
								"<td align='center'> "+lists.get(1).getGoodsnum()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getOrderAmount()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getTotalAmount()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getRemaintimes()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getOrderArrearage()+"</td> "+
							"</tr> ";
						suitCardSons = suitCardSons +
								"<input type='hidden' id='"+father.getRecid()+"orderAmount' name='orderAmount' value='"+father.getOrderAmount()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"totalAmount' name='totalAmount' value='"+father.getTotalAmount()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"advanceFlag' name='advanceFlag' value='"+father.getAdvanceFlag()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"remaintimes' name='remaintimes' value='"+father.getRemaintimes()+"' />"+
								"<input type='hidden' id='"+father.getRecid()+"orderArrearage' name='orderArrearage' value='"+father.getOrderArrearage()+"' />";
						for(int i=2;i<lists.size();i++){
							isreal = lists.get(i).getIsreal()==0?"实物":"虚拟";
							suitCardSons = suitCardSons +
								"<tr> "+
									"<td align='center'> "+lists.get(i).getGoodsname()+"</td> "+
									"<td align='center'> "+isreal+"</td> "+
									"<td align='center'> "+lists.get(i).getGoodsnum()+"</td> "+
								"</tr>";
						}
			
					}
				}
			}
			model.addAttribute("suitCardSons", suitCardSons);
			return "modules/ec/returnFormCardCommon";
		}else if(returnedGoods.getIsReal() == 1){
			return "modules/ec/returnForm";
		}else{
			return "modules/ec/returnFormKind";
		}
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

						//判断修改物流之前是否存在物流信息
						String shopcodes = ordersService.getShippingcodeByid(orders);
						int index = ordersService.UpdateShipping(orders);
						if(StringUtils.isEmpty(shopcodes)){//为空:推送消息,修改订单状态
							if (index > 0) {		
								ordersService.updateOrdersStatus(orders);//第一次修改物流信息时,修改订单状态
								successNum++;
								OrdersStatusChangeUtils.pushMsg(orders, 3);//推送已发货信息给用户
							} else {
								failureMsg.append("<br/>订单" + shipping.getOrderid() + "更新物流失败; ");
								failureNum++;
							}	
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
	 * 获取妃子校用户信息
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSysUser")
	public Orders getSysUser(String mobile){
		Orders orders = ordersService.getSysUser(mobile);
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
	public String addTopUp(String orderid,double singleRealityPrice,int userid,int isReal,double goodsBalance,int servicetimes,Model model){
		model.addAttribute("orderid", orderid);
		model.addAttribute("singleRealityPrice", singleRealityPrice);
		model.addAttribute("userid", userid);
		model.addAttribute("isReal", isReal);
		model.addAttribute("goodsBalance",goodsBalance);
		model.addAttribute("servicetimes",servicetimes);
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
				}else if(orders.getOrderstatus() == 2){
					ordersService.updateVirtualOrder(orders);
					OrdersStatusChangeUtils.pushMsg(orders, 3); //推送已发货信息给用户
					addMessage(redirectAttributes, "修改订单'" + orders.getOrderid() + "'成功");
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
	 * 查看套卡订单充值记录
	 * @param recid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "suitCardRechargeLog")
	public String suitCardRechargeLog(Integer recid, String orderid, String orderType, HttpServletRequest request, Model model) {
		try {
			List<OrderGoodsDetails> orderGoodsDetails = ordersService.getMappinfOrderView(recid);
			model.addAttribute("orderGoodsDetails", orderGoodsDetails);
			model.addAttribute("orderType", orderType);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看套卡订单充值", e);
			logger.error("方法：suitCardRechargeLog，查看套卡订单充值出现错误：" + e.getMessage());
		}
		return "modules/ec/suitCardRechargeLog";
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
	public String getPushmoneyView(OrderPushmoneyRecord orderPushmoneyRecord, HttpServletRequest request, Model model) {
		try {
			if(orderPushmoneyRecord.getPushmoneyRecordId() != 0){
				orderPushmoneyRecord = orderPushmoneyRecordService.getOrderPushmoneyRecordById(Integer.valueOf(orderPushmoneyRecord.getPushmoneyRecordId()));
				model.addAttribute("orderPushmoneyRecord", orderPushmoneyRecord);
			}
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
	 * @param orderPushmoneyRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteSysUserInfo")
	public String deleteSysUserInfo(OrderPushmoneyRecord orderPushmoneyRecord,HttpServletRequest request, HttpServletResponse response) {
		String type="";
		try {
			ordersService.deleteSysUserInfo(orderPushmoneyRecord);
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
	 * 查询提成人员日志记录
	 * @param orderPushmoneyRecord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getOrderPushmoneyRecordList")
	public String getOrderPushmoneyRecordList(OrderPushmoneyRecord orderPushmoneyRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			if(StringUtils.isNotBlank(orderPushmoneyRecord.getOrderId())){
				Page<OrderPushmoneyRecord>  page = ordersService.getOrderPushmoneyRecordList(new Page<OrderPushmoneyRecord>(request, response), orderPushmoneyRecord);
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "提成人员日志记录列表", e);
			logger.error("提成人员日志记录列表：" + e.getMessage());
		}
		return "modules/ec/pushmoneyList";
	}
	
	/**
	 * 确定商品是否售后    (是:正在售后    或者   已经售后)
	 * @param goodsMappingId
	 * @param orderid
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "getReturnGoodsNum")
	@ResponseBody
	public boolean getReturnGoodsNum(ReturnedGoods returnedGoods, HttpServletRequest request, HttpServletResponse response) {
		return returnedGoodsService.getReturnGoodsNum(returnedGoods);
	}
	/**
	 * 获取实物子项的可售后数量(根据组ID(mapping_id)获取卡项中的实物集合)
	 * @param recid
	 * @param isReal
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "getCardRealNum")
	@ResponseBody
	public List<OrderGoods> getCardRealNum(OrderGoods orderGoods, HttpServletRequest request, HttpServletResponse response) {
		return ordergoodService.getCardRealNum(orderGoods);
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
			int integral = 0;
			
			orders = ordersService.findselectByOrderId(orders.getOrderid());
			boolean result = returnRepository(orders.getOrderid(),request);
			if(!result){
				logger.error("取消库存失败，导致强制取消无法进行");
				addMessage(redirectAttributes, "强制取消'" + orders.getOrderid() + "'失败！");
				return "redirect:" + adminPath + "/ec/orders/orderform?type=view&orderid="+orders.getOrderid();
			}
			logger.info("商品退还仓库是否成功："+result);
			
			List<ReturnedGoods> list = returnedGoodsDao.queryAfterSaleList(orders.getOrderid());
			if(list.size() > 0){
				for(ReturnedGoods returnedGoods:list){
					if(orders.getIsReal() == 0){
						returnedGoodsService.saveReturnedKind(returnedGoods);
					}else if(orders.getIsReal() == 1){
						returnedGoodsService.saveReturned(returnedGoods);
					}else if(orders.getIsReal() == 2){
						returnedGoodsService.
					}else if(orders.getIsReal() == 3){
						returnedGoodsService.saveReturnedCommon(returnedGoods);
					}
				}
			}
			
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
					integral = integral + orderGoodsDao.getintegralByRecId(goodsList.get(i).getRecid()+"") * goodsList.get(i).getGoodsnum();
					//验证是否为抢购活动订单
					if(goodsList.get(i).getActiontype()==1){
						redisClientTemplate.hincrBy(buying_limit_prefix+goodsList.get(i).getActionid(), orders.getUserid()+"_"+goodsList.get(i).getGoodsid(),-goodsList.get(i).getGoodsnum());
					}
				}
			}
			
			boolean userResult = redisClientTemplate.exists(MTMY_ID+orders.getUserid());
			if(userResult){   //若缓存存在，则操作缓存
				RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+orders.getUserid());
				redisLock.lock();
				redisClientTemplate.incrBy(MTMY_ID+orders.getUserid(),-integral);
				redisLock.unlock();
			}else{         //若缓存不存在，则操作mtmy_user_accounts
				orders.setUserid(orders.getUserid());
				orders.setUserIntegral(integral);
				ordersDao.updateIntegralAccount(orders);
			}
			
			//在mtmy_integrals_log表中插入日志
			IntegralsLog integralsLog = new IntegralsLog();
			integralsLog.setUserId(orders.getUserid());
			integralsLog.setIntegralType(1);
			integralsLog.setIntegralSource(0);
			integralsLog.setActionType(21);
			integralsLog.setIntegral(-integral);
			integralsLog.setOrderId(orders.getOrderid());
			integralsLog.setRemark("商品赠送(扣减)");
			ordersDao.insertIntegralLog(integralsLog);
			
			
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
			int servicetimes = orderGoods.getServicetimes();         //预计服务次数
			double orderArrearage = orderGoods.getOrderArrearage();  //欠款
			
			orderGoods = ordersService.selectOrderGoodsByRecid(orderGoods.getRecid());
			double goodsPrice = orderGoods.getGoodsprice();        //商品优惠单价
			double advance = orderGoods.getAdvancePrice();                 //预约金
			
			if(advance == 0){      //当预约金为0的时候付全款
				advance = goodsPrice;                 //预约金
			}else{
				advance = orderGoods.getAdvancePrice();            //预约金
			}
			
			double singleRealityPrice = orderGoods.getSingleRealityPrice();   //服务单次价
			orderGoods.setAdvance(advance);
			if(servicetimes == 999){                      //当该商品为时限卡时，需要单独处理
				orderGoods.setAdvanceServiceTimes(0);        //服务次数
				orderGoods.setDebt(orderArrearage);                       //欠款
			}else{
				if(advance < singleRealityPrice){
					double c = Double.parseDouble(formater.format(singleRealityPrice - advance));
					orderGoods.setAdvanceServiceTimes(0);        //服务次数
					orderGoods.setDebt(c);                       //欠款
				}else if(advance == goodsPrice){                     //预约金为0或者预约金等于商品优惠单价的时
					orderGoods.setAdvanceServiceTimes(servicetimes);        //服务次数
					orderGoods.setDebt(0);                       //欠款
				}else{
					int a = (int)(advance/singleRealityPrice);
					double b = Double.parseDouble(formater.format(advance - a*singleRealityPrice));
					orderGoods.setAdvanceServiceTimes(a);        //服务次数 
					orderGoods.setAdvanceBalance(b);             //余额
				}
			}
			
			double accountBalance = ordersService.getAccount(userid); //用户账户余额
			orderGoods.setAccountBalance(accountBalance);
			model.addAttribute("orderGoods", orderGoods);
			model.addAttribute("servicetimes", servicetimes);
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
			double goodsPrice = orderGoods.getGoodsprice();        //商品优惠单价
			double advance = orderGoods.getAdvancePrice();                 //预约金
			
			double realAdvancePrice = 0;                                //处理预约金给老商品送钱时，判断预约金的真实价格
			
			if(advance == 0){      //当预约金为0的时候付全款
				realAdvancePrice = 0;
				advance = goodsPrice;                 //预约金
			}else{
				realAdvancePrice = advance;
				advance = orderGoods.getAdvancePrice();                 //预约金
			}
			
			double singleRealityPrice = orderGoods.getSingleRealityPrice();   //服务单次价
			int goodsType = orderGoods.getGoodsType();                    //商品区分(0: 老商品 1: 新商品)
			String officeId = orderGoods.getOfficeId();           //组织架构ID
			
			oLog.setMtmyUserId(userid);
			oLog.setOrderId(orderid);
			oLog.setRecid(orderGoods.getRecid());
			oLog.setSingleRealityPrice(orderGoods.getSingleRealityPrice());
			oLog.setSingleNormPrice(orderGoods.getSingleNormPrice());
			oLog.setAdvance(advance);
			
			if(oLog.getServicetimes() == 999){        //时限卡单独处理
				oLog.setTotalAmount(oLog.getOrderArrearage());      //当时限卡处理预约金时，实付款金额就是待付尾款（欠款）
				if(sum == 0){//用了账户余额
					oLog.setAccountBalance(oLog.getOrderArrearage()); //这里的账户余额是用了多少账户余额，不是账户里有多少余额
				}else{
					oLog.setAccountBalance(0);
				}
			}else{
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
			}
			orderGoodsDetailsService.updateAdvanceFlag(orderGoods.getRecid()+"");
			ordersService.handleAdvanceFlag(oLog,goodsPrice,detailsTotalAmount,goodsType,officeId,realAdvancePrice);
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
	
	/**
	 * 跳转添加套卡订单页
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createSuitCardOrder")
	public String createSuitCardOrder(HttpServletRequest request,Orders orders,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建套卡订单页面", e);
			logger.error("方法：createSuitCardOrder，跳转创建套卡订单页面出错：" + e.getMessage());
		}

		return "modules/ec/createSuitCardOrderForm";
	}
	
	/**
	 * 套卡订单添加商品
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addSuitCardOderGoods")
	public String addSuitCardOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			model.addAttribute("orders", orders);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转套卡添加商品页面", e);
			logger.error("跳转套卡添加商品页面：" + e.getMessage());
		}

		return "modules/ec/addSuitCardOderGoodsForm";
	}
	
	/**
	 * 查找套卡的子项
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectSuitCardSon")
	@ResponseBody
	public String selectSuitCardSon(Goods goods,HttpServletRequest request,Model model){
		String suitCardSons = "";
		int num;
		try{
			double orderAmount = Double.valueOf(request.getParameter("orderAmount"));  //卡项成交价
			double actualPayment = Double.valueOf(request.getParameter("actualPayment"));  //卡项实际付款
			double costPrice = Double.valueOf(request.getParameter("costPrice"));  //卡项系统价
			double debtMoney = Double.valueOf(request.getParameter("debtMoney"));  //卡项欠款
			double spareMoney = Double.valueOf(request.getParameter("spareMoney"));  //卡项余款
			int tail = Integer.valueOf(request.getParameter("tail"));  //用来表示添加的卡项商品
			int isNeworder = Integer.valueOf(request.getParameter("isNeworder"));  //区分新老订单
			int goodsId = goods.getGoodsId();
			List<Goods> goodsList = ordersService.selectCardSon(goodsId);
			if(goodsList.size() > 0){
				num = goodsList.size();
				
				suitCardSons =
					"<tr class=suitCard"+tail+"> "+
						"<td rowspan="+num+"> "+goods.getGoodsName()+"<input id='goodselectIds' name='goodselectIds' type='hidden' value='"+goodsId+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsName()+"</td> "+
						"<td rowspan="+num+"> "+costPrice+"</td> "+
						"<td> "+goodsList.get(0).getMarketPrice()+"<input id='orderAmounts' name='orderAmounts' type='hidden' value='"+orderAmount+"'></td> "+
						"<td> "+goodsList.get(0).getShopPrice()+"<input id='actualPayments' name='actualPayments' type='hidden' value='"+actualPayment+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsNum()+"</td> ";
				if(isNeworder == 0){
					suitCardSons = suitCardSons + 
						"<td></td> ";
				}else if(isNeworder == 1){
					suitCardSons = suitCardSons + 
						"<td><input id='remaintimes_0' value='"+goodsList.get(0).getGoodsNum()+"' name='remaintimeNums' min='0' max='"+goodsList.get(0).getGoodsNum()+"' onkeyup='this.value=this.value.replace(/[^\\d]/g,&quot;&quot;)' class='form-control required'/></td> ";
				}
				suitCardSons = suitCardSons + 
						"<td rowspan="+num+"> "+spareMoney+"</td> "+
						"<td rowspan="+num+"> "+debtMoney+"</td> "+
						"<td rowspan="+num+"> "+
							"<a href='#' class='btn btn-danger btn-xs' onclick='delFile("+tail+","+costPrice+","+orderAmount+","+spareMoney+","+actualPayment+","+debtMoney+")'><i class='fa fa-trash'></i> 删除</a> "+
						"</td>"+
					"</tr>";
				for(int i=1;i<num;i++){
					suitCardSons = suitCardSons +
						"<tr class=suitCard"+tail+"> "+
							"<td> "+goodsList.get(i).getGoodsName()+"</td> "+
							"<td> "+goodsList.get(i).getMarketPrice()+"</td> "+
							"<td> "+goodsList.get(i).getShopPrice()+"</td> "+
							"<td> "+goodsList.get(i).getGoodsNum()+"</td> ";
					if(isNeworder == 0){
						suitCardSons = suitCardSons + 
							"<td></td></tr> ";
					}else if(isNeworder == 1){
						suitCardSons = suitCardSons + 
							"<td><input id='remaintimes_"+i+"' value='"+goodsList.get(i).getGoodsNum()+"' name='remaintimeNums' min='0' max='"+goodsList.get(i).getGoodsNum()+"' onkeyup='this.value=this.value.replace(/[^\\d]/g,&quot;&quot;)' class='form-control required'/></td></tr> ";
					}
				}
				
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找套卡子项", e);
			logger.error("查找套卡子项出错信息：" + e.getMessage());
		}
		return suitCardSons.toString();
	}
	
	
	
	/**
	 * 添加套卡订单
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveSuitCardOrder")
	public String saveSuitCardOrder(Orders orders,HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveSuitCardOrder(orders);
			addMessage(redirectAttributes, "创建套卡订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建套卡订单", e);
			logger.error("方法：saveSuitCardOrder，创建套卡订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建套卡订单失败！");
		}

		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	
	/**
	 * 跳转添加通用卡订单页
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createCommonCardOrder")
	public String createCommonCardOrder(HttpServletRequest request,Orders orders,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建通用卡订单页面", e);
			logger.error("方法：createSuitCardOrder，跳转创建通用卡订单页面出错：" + e.getMessage());
		}

		return "modules/ec/createCommonCardOrderForm";
	}
	
	/**
	 * 通用卡订单添加商品
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addCommonOderGoods")
	public String addCommonOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			model.addAttribute("orders", orders);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转通用卡添加商品页面", e);
			logger.error("跳转通用卡添加商品页面：" + e.getMessage());
		}

		return "modules/ec/addCommonOderGoodsForm";
	}
	
	/**
	 * 查找通用卡的子项
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectCommonCardSon")
	@ResponseBody
	public String selectCommonCardSon(Goods goods,HttpServletRequest request,Model model){
		String suitCardSons = "";
		int num;
		try{
			double actualPayment = Double.valueOf(request.getParameter("actualPayment"));  //卡项实际付款（前）
			String speckeyname = request.getParameter("speckeyname");                     //卡项规格名称
			String speckey = request.getParameter("speckey");                     //卡项规格key
			double marketPrice = Double.valueOf(request.getParameter("marketPrice"));  //卡项市场价
			double goodsPrice = Double.valueOf(request.getParameter("goodsPrice"));  //卡项优惠价
			double costPrice = Double.valueOf(request.getParameter("costPrice"));  //卡项系统价
			double orderAmount = Double.valueOf(request.getParameter("orderAmount"));  //卡项成交价
			double afterPayment = Double.valueOf(request.getParameter("afterPayment"));  //卡项实际付款（后）
			String actualNum = request.getParameter("actualNum");  //卡项实际次数
			double debtMoney = Double.valueOf(request.getParameter("debtMoney"));  //卡项欠款
			double spareMoney = Double.valueOf(request.getParameter("spareMoney"));  //卡项余款
			int isNeworderSon = Integer.valueOf(request.getParameter("isNeworderSon"));  //新老订单（0：新订单，1：老订单）
			int tail = Integer.valueOf(request.getParameter("tail"));  //用来表示添加的卡项商品
			Date realityAddTime;
			if(isNeworderSon == 1){
				realityAddTime =  new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("realityAddTime"));  //实际下单时间
			}else{
				realityAddTime =  new Date();  //实际下单时间
			}
			int goodsId = goods.getGoodsId();
			List<Goods> goodsList = ordersService.selectCardSon(goodsId);
			if(goodsList.size() > 0){
				num = goodsList.size();
				
				suitCardSons =
					"<tr class= commonCard"+tail+"> "+
						"<td rowspan="+num+"> "+goods.getGoodsName()+"<input id='goodselectIds' name='goodselectIds' type='hidden' value='"+goodsId+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsName()+"</td> "+
						"<td rowspan="+num+"> "+speckeyname+"<input id='speckeys' name='speckeys' type='hidden' value='"+speckey+"'></td> "+
						"<td rowspan="+num+"> "+marketPrice+"</td> "+
						"<td rowspan="+num+"> "+goodsPrice+"</td> "+
						"<td rowspan="+num+"> "+costPrice+"</td> "+
						"<td rowspan="+num+"> "+orderAmount+"<input id='orderAmounts' name='orderAmounts' type='hidden' value='"+orderAmount+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsNum()+"</td> "+
						"<td rowspan="+num+"> "+afterPayment+"<input id='actualPayments' name='actualPayments' type='hidden' value='"+actualPayment+"'></td> "+
						"<td rowspan="+num+"> "+actualNum+"<input id='remaintimes' name='remaintimeNums' type='hidden' value='"+actualNum+"'></td> "+
						"<td rowspan="+num+"> "+spareMoney+"</td> "+
						"<td rowspan="+num+"> "+debtMoney+"</td> "+
						"<td rowspan="+num+"> "+
							"<a href='#' class='btn btn-danger btn-xs' onclick='delFile("+tail+","+costPrice+","+orderAmount+","+spareMoney+","+afterPayment+","+debtMoney+")'><i class='fa fa-trash'></i> 删除</a> "+
						"</td>"+
						"<input id='realityAddTime' name='realityAddTimeList' type='hidden' value='"+realityAddTime+"'>"+
					"</tr>";
				for(int i=1;i<num;i++){
					suitCardSons = suitCardSons +
						"<tr class= commonCard"+tail+"> "+
							"<td> "+goodsList.get(i).getGoodsName()+"</td> "+
							"<td> "+goodsList.get(i).getGoodsNum()+"</td> "+
						"</tr>";
				}
				
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找套卡子项", e);
			logger.error("查找套卡子项出错信息：" + e.getMessage());
		}
		return suitCardSons.toString();
	}
	
	/**
	 * 添加通用卡订单
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveCommonCardlOrder")
	public String saveCommonCardlOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveCommonCardlOrder(orders);
			addMessage(redirectAttributes, "创建通用卡订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建通用卡订单", e);
			logger.error("方法：saveCommonCardlOrder，创建通用卡订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建通用卡订单失败！");
		}

		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 跳转修改卡项订单页面
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:orders:edit", "ec:orders:view" }, logical = Logical.OR)
	@RequestMapping(value = "cardOrdersForm")
	public String suitCardOrdersForm(HttpServletRequest request, Orders orders,String type, Model model) {
		try {
			String suitCardSons = "";
			int num;
			String pushMoneryDetails = "";
			int newNum = 0;
			
			List<List<OrderGoods>> result = new ArrayList<List<OrderGoods>>();    //分开存放每个卡项商品和它的子项
			List<OrderGoods> resultSon = new ArrayList<OrderGoods>();              //存放每个卡项商品和它的子项
			User user = UserUtils.getUser(); //登陆用户
			List<Payment> paylist = paymentService.paylist();
			orders = ordersService.selectOrderById(orders.getOrderid());
			List<TurnOverDetails> turnOverDetailsList = turnOverDetailsService.selectDetailsByOrderId(orders.getOrderid());
			
			List<OrderGoods> list = orders.getOrderGoodList();                   //根据订单id查找所有的mapping中的记录（每个卡项和子项都在里面）
			for(int i=0;i<list.size();i++){
				if(list.get(i).getGroupId() == 0 && resultSon.size() > 0){
					result.add(resultSon);
					resultSon = new ArrayList<OrderGoods>();
				}
				resultSon.add(list.get(i));
				if(i == (list.size()-1)){                                       //将最后一次循环的结果放到集合里
					result.add(resultSon);
				}
			}
			
			if(result.size() > 0){                                             //若该订单有商品
				for(List<OrderGoods> lists:result){                            
					if((lists.size() - 1) > 0){                               //若该卡项有子项
						num = lists.size() - 1;
						OrderGoods father = lists.get(0);
						
						if(orders.getIsReal() == 2){        //套卡
							suitCardSons = suitCardSons +
									"<tr> "+
										"<td align='center' rowspan="+num+"> "+father.getGoodsname()+"</td> "+
										"<td align='center'> "+lists.get(1).getGoodsname()+"</td> "+
										"<td align='center' rowspan="+num+">默认规格</td> "+
										"<td align='center' rowspan="+num+"> "+father.getCostprice()+"</td> "+
										"<td align='center'> "+lists.get(1).getMarketprice()+"</td> "+
										"<td align='center'> "+lists.get(1).getGoodsprice()+"</td> "+
										"<td align='center'> "+lists.get(1).getSpeckeyname()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getAdvancePrice()+"</td> "+
										"<td align='center'> "+lists.get(1).getGoodsnum()+"</td> ";
									if(orders.getIsNeworder() == 1){
										suitCardSons = suitCardSons +
												"<td align='center'> "+lists.get(1).getServicetimes()+"</td> ";
									}
									suitCardSons = suitCardSons +			
										"<td align='center' rowspan="+num+"> "+father.getCouponPrice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getDiscount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getMembergoodsprice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getOrderAmount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getTotalAmount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getOrderArrearage()+"</td> "+
										"<td align='center' rowspan="+num+">";
							if(!"view".equals(type)){
								if(father.getAdvanceFlag() != 1){                //不是预约金
									if(father.getOrderArrearage() != 0){         //有欠款
										suitCardSons = suitCardSons + "<a href='#' onclick='TopUp("+father.getRecid()+","+father.getSingleRealityPrice()+","+father.getSingleNormPrice()+","+father.getOrderArrearage()+","+father.getServicetimes()+","+father.getPayRemaintimes()+","+father.getGoodsBalance()+")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>充值</a>";
									}else if(father.getOrderArrearage() == 0){   //无欠款
										suitCardSons = suitCardSons + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs' ><i class='fa fa-edit'></i>充值</a>";
									} 
								}else if(father.getAdvanceFlag() == 1){        //是预约金   
									if(orders.getOrderstatus() == 4 && father.getSumAppt() == 1){    //已完成且预约已完成
										suitCardSons = suitCardSons + "<a href='#' onclick='ToAdvance("+"\""+orders.getOfficeId()+"\""+","+father.getRecid()+","+father.getServicetimes()+","+father.getOrderArrearage()+")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>处理预约金</a>";
									}else if(orders.getOrderstatus() != 4 || father.getSumAppt() == 0){   //无预约或订单未完成
										suitCardSons = suitCardSons + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs' ><i class='fa fa-edit'></i>处理预约金</a>";
									}
									
								}
							}
							
								suitCardSons = suitCardSons +
											"<a href='#' onclick='viewCardOrders("+father.getRecid()+","+orders.getOrderid()+","+orders.getIsReal()+")' class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i>商品充值查看</a>"+
										"</td>"+
									"</tr>";
								for(int i=2;i<lists.size();i++){
									suitCardSons = suitCardSons +
										"<tr> "+
											"<td align='center'> "+lists.get(i).getGoodsname()+"</td> "+
											"<td align='center'> "+lists.get(i).getMarketprice()+"</td> "+
											"<td align='center'> "+lists.get(i).getGoodsprice()+"</td> "+
											"<td align='center'> "+lists.get(i).getSpeckeyname()+"</td> "+
											"<td align='center'> "+lists.get(i).getGoodsnum()+"</td> ";
									if(orders.getIsNeworder() == 1){
										suitCardSons = suitCardSons +
												"<td align='center'> "+lists.get(i).getServicetimes()+"</td> ";
									}
									suitCardSons = suitCardSons + "</tr>";
								}
						}else if(orders.getIsReal() == 3){   //通用卡
							suitCardSons = suitCardSons +
									"<tr> "+
										"<td align='center' rowspan="+num+"> "+father.getGoodsname()+"</td> "+
										"<td align='center'> "+lists.get(1).getGoodsname()+"</td> "+
										"<td align='center' rowspan="+num+">"+father.getSpeckeyname()+"</td> "+
										"<td align='center' rowspan="+num+">"+father.getServicetimes()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getCostprice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getMarketprice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getGoodsprice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getAdvancePrice()+"</td> "+
										"<td align='center'> "+lists.get(1).getGoodsnum()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getRemaintimes()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getCouponPrice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getDiscount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getMembergoodsprice()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getOrderAmount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getTotalAmount()+"</td> "+
										"<td align='center' rowspan="+num+"> "+father.getOrderArrearage()+"</td> "+
										"<td align='center' rowspan="+num+">";
							
							if(!"view".equals(type)){
								if(father.getAdvanceFlag() != 1){                //不是预约金
									if(father.getOrderArrearage() != 0){         //有欠款
										suitCardSons = suitCardSons + "<a href='#' onclick='TopUp("+father.getRecid()+","+father.getSingleRealityPrice()+","+father.getSingleNormPrice()+","+father.getOrderArrearage()+","+father.getServicetimes()+","+father.getPayRemaintimes()+","+father.getGoodsBalance()+")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>充值</a>";
									}else if(father.getOrderArrearage() == 0){   //无欠款
										suitCardSons = suitCardSons + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs' ><i class='fa fa-edit'></i>充值</a>";
									} 
								}else if(father.getAdvanceFlag() == 1){        //是预约金   
									if(orders.getOrderstatus() == 4 && father.getSumAppt() == 1){    //已完成且预约已完成
										suitCardSons = suitCardSons + "<a href='#' onclick='ToAdvance("+"\""+orders.getOfficeId()+"\""+","+father.getRecid()+","+father.getServicetimes()+","+father.getOrderArrearage()+")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>处理预约金</a>";
									}else if(orders.getOrderstatus() != 4 || father.getSumAppt() == 0){   //无预约或订单未完成
										suitCardSons = suitCardSons + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs' ><i class='fa fa-edit'></i>处理预约金</a>";
									}
									
								}
							}
							
							suitCardSons = suitCardSons +
											"<a href='#' onclick='viewCardOrders("+father.getRecid()+","+orders.getOrderid()+","+orders.getIsReal()+")' class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i>商品充值查看</a>"+
										"</td>"+
									"</tr>";
								for(int i=2;i<lists.size();i++){
									suitCardSons = suitCardSons +
										"<tr> "+
											"<td align='center'> "+lists.get(i).getGoodsname()+"</td> "+
											"<td align='center'> "+lists.get(i).getGoodsnum()+"</td> "+
										"</tr>";
								}
						}
						
					}
					
				}
			
			}
			
			List<TurnOverDetails> pushmoneyRecordList = turnOverDetailsService.selectPushDetails(orders.getOrderid());
			if(pushmoneyRecordList.size() > 0){
				for(TurnOverDetails turnOverDetails:pushmoneyRecordList){
					newNum = turnOverDetails.getPushMoneyList().size();
					if(newNum == 0){
						newNum = 1;
					}
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(turnOverDetails.getCreateDate());
					pushMoneryDetails = pushMoneryDetails + 
							"<tr> "+
								"<td align='center' rowspan="+newNum+">"+date+"</td> ";
					if(turnOverDetails.getType() == 1){
						pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+newNum+">下单</td> ";
					}else if(turnOverDetails.getType() == 2){
						pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+newNum+">还款</td> ";
					}
					pushMoneryDetails = pushMoneryDetails + "<td align='center' rowspan="+newNum+">"+turnOverDetails.getAmount()+"</td> ";
					if(turnOverDetails.getPushMoneyList().size() == 0){
						pushMoneryDetails = pushMoneryDetails + "<td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'>";
						if("view".equals(type)){
							pushMoneryDetails = pushMoneryDetails + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs'><i class='fa fa-edit'></i>编辑</a>";
						}else{
							pushMoneryDetails = pushMoneryDetails +"<a href='#' onclick='editSysUserInfo("+"\""+turnOverDetails.getTurnOverDetailsId()+"\")' class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>";
						}
						pushMoneryDetails = pushMoneryDetails	
								+ "</td>"
							+"</tr>";
					}else{
						List<OrderPushmoneyRecord> newList = turnOverDetails.getPushMoneyList();
							pushMoneryDetails = pushMoneryDetails + "<td align='center'>"+newList.get(0).getPushmoneyUserName()+"</td>"
								+"<td align='center'>"+newList.get(0).getDepartmentName()+"</td>"
							    +"<td align='center'>"+newList.get(0).getPushmoneyUserMobile()+"</td>"
								+"<td align='center'>"+newList.get(0).getPushMoney()+"</td>"
								+"<td align='center'rowspan="+newNum+">";
								if("view".equals(type)){
									pushMoneryDetails = pushMoneryDetails + "<a href='#' style='background:#C0C0C0;color:#FFF' class='btn  btn-xs'><i class='fa fa-edit'></i>编辑</a>";
								}else{
									pushMoneryDetails = pushMoneryDetails +"<a href='#' onclick='editSysUserInfo("+"\""+turnOverDetails.getTurnOverDetailsId()+"\")' class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>";
								}
								pushMoneryDetails = pushMoneryDetails 
									+"<a href=\"#\" onclick=\"openDialogView('操作日志', '/kenuo/a/ec/orders/operationLog?turnOverDetailsId="+turnOverDetails.getTurnOverDetailsId()+"','800px','550px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i>操作日志</a>"
								+"</td>"                                                                                
							+"</tr>";
						for(int i= 1;i<newList.size();i++){
							pushMoneryDetails = pushMoneryDetails + 
									"<tr>"
										+"<td align='center'>"+newList.get(i).getPushmoneyUserName()+"</td>"
										+"<td align='center'>"+newList.get(i).getDepartmentName()+"</td>"
										+"<td align='center'>"+newList.get(i).getPushmoneyUserMobile()+"</td>"
										+"<td align='center'>"+newList.get(i).getPushMoney()+"</td>"
									+"</tr>";
						}
					}
				}	
			}
			
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("user", user);
			model.addAttribute("type", type);
			model.addAttribute("suitCardSons", suitCardSons);
			model.addAttribute("turnOverDetailsList",turnOverDetailsList);
			model.addAttribute("pushMoneryDetails",pushMoneryDetails);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转修改卡项订单页面", e);
			logger.error("跳转修改卡项订单页面出错：" + e.getMessage());
		}
		return "modules/ec/cardOrdersForm";
	}
	
	/**
	 * 修改卡项订单
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "updateCardOrder")
	public String updateCardOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
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
						addMessage(redirectAttributes, "修改卡项订单'" + orders.getOrderid() + "'成功");
					}else{
						addMessage(redirectAttributes, "修改卡项订单'" + orders.getOrderid() + "'失败");
					}
					logger.info("商品退还仓库是否成功："+result);
				}else if(orders.getOrderstatus() == 2){
					ordersService.updateVirtualOrder(orders);
					OrdersStatusChangeUtils.pushMsg(orders, 3); //推送已发货信息给用户
					addMessage(redirectAttributes, "修改卡项订单'" + orders.getOrderid() + "'成功");
				}else{//不是取消订单
					ordersService.updateVirtualOrder(orders);
					addMessage(redirectAttributes, "修改卡项订单'" + orders.getOrderid() + "'成功");
				}
			}else{
				ordersService.updateVirtualOrder(orders);
				addMessage(redirectAttributes, "修改卡项订单'" + orders.getOrderid() + "'成功");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改卡项订单", e);
			logger.error("方法：updateCardOrder，修改卡项订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "修改卡项订单失败！");
		}
		
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 跳转卡项充值页面
	 * @param orderid
	 * @param recid
	 * @param userid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addCardTopUp")
	public String addCardTopUp(String orderid,double singleRealityPrice,int userid,int isReal,double goodsBalance,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			model.addAttribute("orderid", orderid);
			model.addAttribute("singleRealityPrice", singleRealityPrice);
			model.addAttribute("userid", userid);
			model.addAttribute("isReal", isReal);
			model.addAttribute("goodsBalance",goodsBalance);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转卡项充值页面", e);
			logger.error("方法：addCardTopUp，跳转卡项充值页面出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "跳转卡项充值页面失败！");
		}
		return "modules/ec/addCardTopUp";
	}
	
	/**
	 * 新增卡项订单充值日志记录
	 * @param oLog
	 */
	@ResponseBody
	@RequestMapping(value = "addCardOrderRechargeLog")
	public String addCardOrderRechargeLog(OrderRechargeLog oLog){
		String success="";
		try{
			ordersService.addCardOrderRechargeLog(oLog);
			success = "success";
		}catch(Exception e){
			e.printStackTrace();
			success = "error";
		}
		return success;
	}
	
	/**
	 * 跳转处理卡项预约金页面
	 * @param orderGoods
	 * @param request
	 * @return
	 */
	@RequestMapping(value="handleCardAdvanceForm")
	public String handleCardAdvanceForm(OrderGoods orderGoods,int userid,HttpServletRequest request,Model model){
		DecimalFormat formater = new DecimalFormat("#0.##");
		try{
			int servicetimes = orderGoods.getServicetimes();         //预计服务次数
			int isReal = orderGoods.getIsreal();
			
			orderGoods = ordersService.selectOrderGoodsByRecid(orderGoods.getRecid()); //卡项本身
			double goodsPrice = orderGoods.getGoodsprice();        //商品优惠单价
			double advance = orderGoods.getAdvancePrice();                 //预约金
			if(advance == 0){      //当预约金为0的时候付全款
				advance = goodsPrice;                 //预约金
			}else{
				advance = orderGoods.getAdvancePrice();            //预约金
			}
			
			orderGoods.setAdvance(advance);
			
			//卡项中预约是某一个子项，故处理预约金是针对子项的，但是预约金本身却是卡项本身的
			OrderGoods orderGoodsSon = ordersService.selectCardSonReservation(orderGoods.getRecid());   //预约的子项
		
			if(isReal == 2){   //套卡
				double singleRealityPrice = orderGoodsSon.getSingleRealityPrice();   //子项服务单次价
				
				if(advance < singleRealityPrice){
					double c = Double.parseDouble(formater.format(singleRealityPrice - advance));
					orderGoods.setDebt(c);                       //欠款
				}else{
					double b = Double.parseDouble(formater.format(advance - singleRealityPrice));
					orderGoods.setAdvanceBalance(b);             //余额
				}
			}else if(isReal == 3){   //通用卡
				double singleRealityPrice = orderGoods.getSingleRealityPrice();   //服务单次价
				
				if(advance < singleRealityPrice){
					double c = Double.parseDouble(formater.format(singleRealityPrice - advance));
					orderGoods.setAdvanceServiceTimes(0);        //服务次数
					orderGoods.setDebt(c);                       //欠款
				}else if(advance == goodsPrice){                     //预约金为0或者预约金等于商品优惠单价的时
					orderGoods.setAdvanceServiceTimes(servicetimes);        //服务次数
					orderGoods.setDebt(0);                       //欠款
				}else{
					int a = (int)(advance/singleRealityPrice);
					double b = Double.parseDouble(formater.format(advance - a*singleRealityPrice));
					orderGoods.setAdvanceServiceTimes(a);        //服务次数 
					orderGoods.setAdvanceBalance(b);             //余额
				}
			}
			
			double accountBalance = ordersService.getAccount(userid); //用户账户余额
			orderGoods.setAccountBalance(accountBalance);
			model.addAttribute("orderGoods", orderGoods);
			model.addAttribute("servicetimes", servicetimes);
			model.addAttribute("isReal", isReal);
			model.addAttribute("orderGoodsSon", orderGoodsSon);    //卡项预约的子项
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转处理卡项预约金页面出错", e);
			logger.error("跳转处理卡项预约金页面出错："+e.getMessage());
		}
		return "modules/ec/handleCardAdvanceForm";
	}
	
	/**
	 * 处理卡项预约金
	 * @param oLog
	 * @param orderGoods
	 * @param userid
	 * @param orderid
	 * @param sum
	 * @return
	 */
	@RequestMapping(value="handleCardAdvance")
	@ResponseBody
	public String handleCardAdvance(OrderRechargeLog oLog,OrderGoods orderGoods,int sum,int userid,String orderid,HttpServletRequest request){
		String date="";
		double singleRealityPrice = 0;     //实际服务单次价
		DecimalFormat formater = new DecimalFormat("#0.##");
		try{
			int isReal = oLog.getIsReal();
			
			orderGoods = ordersService.selectOrderGoodsByRecid(orderGoods.getRecid());   //卡项本身  
			double detailsTotalAmount = orderGoods.getTotalAmount();       //预约金用了红包、折扣以后实际付款的钱
			int goodsType = orderGoods.getGoodsType();                    //商品区分(0: 老商品 1: 新商品)
			String officeId = orderGoods.getOfficeId();           //组织架构ID
			double goodsPrice = orderGoods.getGoodsprice();        //商品优惠单价
			
			double advance = orderGoods.getAdvancePrice();                 //预约金
			double realAdvancePrice = 0;                                //处理预约金给老商品送钱时，判断预约金的真实价格
			
			if(advance == 0){      //当预约金为0的时候付全款
				realAdvancePrice = 0;
				advance = goodsPrice;                 //预约金
			}else{
				realAdvancePrice = advance;
				advance = orderGoods.getAdvancePrice();            //预约金
			}
			
			//卡项中预约是某一个子项，故处理预约金是针对子项的，但是预约金本身却是卡项本身的
			OrderGoods orderGoodsSon = ordersService.selectCardSonReservation(orderGoods.getRecid());   //预约的子项
			
			if(isReal == 2){    //套卡
				singleRealityPrice = orderGoodsSon.getSingleRealityPrice();   //子项的实际服务单次价
			}else if(isReal == 3){  //通用卡
				singleRealityPrice = orderGoods.getSingleRealityPrice();   //卡项本身的实际服务单次价
				
			}
			
			oLog.setMtmyUserId(userid);
			oLog.setOrderId(orderid);
			oLog.setRecid(orderGoods.getRecid());
			oLog.setSingleRealityPrice(singleRealityPrice);
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
			ordersService.handleCardAdvance(oLog,goodsPrice,detailsTotalAmount,goodsType,officeId,isReal,realAdvancePrice);
			date = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "处理卡项预约金异常", e);
			date = "error";
		}
		return date;
	}
	
	/**
	 * 实物有预约金确认收货
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="affirmReceive")
	public String affirmReceive(Orders orders,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		DecimalFormat formater = new DecimalFormat("#0.##");
		try{
			if((!"".equals(orders.getOrderid()) && (orders.getOrderid() != null))){
				ordersService.updateOrderstatusForReal(orders.getOrderid());
				
				double detailsTotalAmount = 0;       //预约金用了红包、折扣以后实际付款的钱
				int goodsType = 0;                    //商品区分(0: 老商品 1: 新商品)
				String officeId = "";           //组织架构ID
				double advancePrice = 0;    //单个实物的预约金
				int recId = 0;
				List<OrderGoods> lists = ordersService.selectOrderGoodsByOrderid(orders.getOrderid());   //卡项本身  
				if(lists.size() > 0){
					detailsTotalAmount = lists.get(0).getTotalAmount();       //预约金用了红包、折扣以后实际付款的钱
					goodsType = lists.get(0).getGoodsType();                    //商品区分(0: 老商品 1: 新商品)
					officeId = lists.get(0).getOfficeId();           //组织架构ID
					advancePrice = lists.get(0).getAdvancePrice();    //单个实物的预约金
					recId = lists.get(0).getRecid();
				}
				
				if(!"bm".equals(orders.getChannelFlag())){
					
					//实物带预约金，点击确认收货，按照虚拟有预约金处理的方法入库
					orderGoodsDetailsService.updateAdvanceFlag(recId+"");
					
					//保存订单商品详情记录表
					OrderGoodsDetails details = new OrderGoodsDetails();
					details.setOrderId(orders.getOrderid());
					details.setGoodsMappingId(recId+"");
					details.setTotalAmount(0);	//实付款金额
					details.setOrderBalance(0);	//订单余款
					details.setOrderArrearage(0);	//订单欠款
					details.setItemAmount(0);	//项目金额
					details.setItemCapitalPool(0); //项目资金池
					details.setServiceTimes(0);	//剩余服务次数
					details.setAppTotalAmount(0);   //app实付金额
					details.setAppArrearage(0);        //app欠款金额
					details.setSurplusAmount(0);   //套卡剩余金额(套卡的才存)
					details.setType(0);
					details.setAdvanceFlag("2");
					details.setCreateOfficeId(UserUtils.getUser().getOffice().getId());
					details.setCreateBy(UserUtils.getUser());
					//保存订单商品详情记录
					orderGoodsDetailsService.saveOrderGoodsDetails(details);
					
					//同步数据到营业额明细表
					//第一次，同步下单的那条数据
					double appSum = orderGoodsDetailsService.queryAppSum(details.getOrderId());
					TurnOverDetails turnOverDetails1 = new TurnOverDetails();
					turnOverDetails1.setOrderId(details.getOrderId());
					turnOverDetails1.setDetailsId(details.getOrderId());
					turnOverDetails1.setType(1);
					turnOverDetails1.setAmount(appSum);
					turnOverDetails1.setUseBalance(0);
					turnOverDetails1.setStatus(1);
					turnOverDetails1.setUserId(orders.getUserid());
					turnOverDetails1.setBelongOfficeId(officeId);
					turnOverDetails1.setCreateBy(UserUtils.getUser());
					turnOverDetailsService.saveTurnOverDetails(turnOverDetails1);
					
					//第二次，同步处理预约金的那条数据
					TurnOverDetails turnOverDetails2 = new TurnOverDetails();
					turnOverDetails2.setOrderId(details.getOrderId());
					turnOverDetails2.setDetailsId(details.getId());
					turnOverDetails2.setType(2);
					turnOverDetails2.setAmount(details.getAppTotalAmount());
					turnOverDetails2.setUseBalance(details.getUseBalance());
					turnOverDetails2.setStatus(2);
					turnOverDetails2.setUserId(orders.getUserid());
					turnOverDetails2.setCreateBy(UserUtils.getUser());
					turnOverDetailsService.saveTurnOverDetails(turnOverDetails2);
				}
				
				//若为老商品，则对店铺有补偿
				if(goodsType == 0){
					//若预约金大于0
					if(advancePrice > 0){   
						//对登云账户进行操作
						if(detailsTotalAmount > 0){
							double claimMoney = 0.0;   //补偿金  补助不超过20
							if(detailsTotalAmount * 0.2 >= 20){
								claimMoney = detailsTotalAmount + 20;
							}else{
								claimMoney = detailsTotalAmount * 1.2;
							}
							OfficeAccountLog officeAccountLog = new OfficeAccountLog();
							User newUser = UserUtils.getUser();
							
							double amount = orderGoodsDetailsService.selectByOfficeId("1");   //登云美业公司账户的钱
							double afterAmount = Double.parseDouble(formater.format(amount - claimMoney));
							orderGoodsDetailsService.updateByOfficeId(afterAmount, "1");   //更新登云美业的登云账户金额
							
							//登云美业的登云账户减少钱时对日志进行操作
							officeAccountLog.setOrderId(orders.getOrderid());
							officeAccountLog.setOfficeId("1");
							officeAccountLog.setType("1");
							officeAccountLog.setOfficeFrom("1");
							officeAccountLog.setAmount(claimMoney);
							officeAccountLog.setCreateBy(newUser);
							orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
							
							if(orderGoodsDetailsService.selectShopByOfficeId(officeId) == 0){    //若登云账户中无该店铺的账户
								OfficeAccount officeAccount = new OfficeAccount();
								officeAccount.setAmount(claimMoney);
								officeAccount.setOfficeId(officeId);
								orderGoodsDetailsService.insertByOfficeId(officeAccount);
							}else{         
								double shopAmount = orderGoodsDetailsService.selectByOfficeId(officeId);   //登云账户中店铺的钱
								double afterShopAmount =  Double.parseDouble(formater.format(shopAmount + claimMoney));
								orderGoodsDetailsService.updateByOfficeId(afterShopAmount, officeId);
							}
							//店铺的登云账户减少钱时对日志进行操作
							officeAccountLog.setOrderId(orders.getOrderid());
							officeAccountLog.setOfficeId(officeId);
							officeAccountLog.setType("0");
							officeAccountLog.setOfficeFrom("1");
							officeAccountLog.setAmount(claimMoney);
							officeAccountLog.setCreateBy(newUser);
							orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
						}
					}
				}
				addMessage(redirectAttributes, "确认收货成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "实物有预约金确认收货", e);
			logger.error("方法：affirmReceive，实物有预约金确认收货出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "确认收货失败！");
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	
	/**
	 * 获取店铺详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getOfficeDetails")
	@ResponseBody
	public OfficeInfo getOfficeDetails(HttpServletRequest request){
		OfficeInfo officeInfo = new OfficeInfo();
		try{
			String officeId = request.getParameter("officeId");
			if(!"".equals(officeId) && officeId != null){
				officeInfo = officeService.selectOfficeDetails(officeId);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "获取店铺详情", e);
			logger.error("方法：getOfficeDetails，获取店铺详情出现错误：" + e.getMessage());
		}
		return officeInfo;
	}
	
	/**
	 * 给店营业额处理预约金的那条记录选择归属店铺
	 * @return
	 */
	@RequestMapping(value="addBelongOffice")
	public String addBelongOffice(){
		return "modules/ec/addBelongOffice";
	}
	
	/**
	 * 保存店营业额处理预约金的那条记录选择归属店铺
	 * @param turnOverDetails
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveBelongOffice")
	public String saveBelongOffice(TurnOverDetails turnOverDetails,HttpServletRequest request){
		String success="";
		try{
			turnOverDetailsService.updateBelongOffice(turnOverDetails.getBelongOfficeId(), turnOverDetails.getTurnOverDetailsId(),UserUtils.getUser().getId());
			success = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存店营业额处理预约金的那条记录选择归属店铺", e);
			logger.error("方法：saveBelongOffice，保存店营业额处理预约金的那条记录选择归属店铺出现错误：" + e.getMessage());
			success = "error";
		}
		return success;
	}
	
	/**
	 * 分享营业额查询提成人员信息 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "querySysUser")
	public List<User> querySysUser(User user){
		List<User> userList = userDao.querySysUser(user);
		return userList;
	}
	
	
	/**
	 * 新增/修改业务员营业额
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editPushmoneyUser")
	public String editPushmoneyUser(HttpServletRequest request, Orders orders,String type, Model model) {
		try {
			String turnOverDetailsId = request.getParameter("turnOverDetailsId");
			TurnOverDetails turnOverDetails = turnOverDetailsService.selectOneDetails(Integer.valueOf(turnOverDetailsId));
			model.addAttribute("turnOverDetails",turnOverDetails);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "新增/修改业务员营业额页面", e);
			logger.error("新增/修改业务员营业额出错：" + e.getMessage());
		}
		return "modules/ec/editPushmoneyUser";
	}
	
	/**
	 * 保存业务员提成营业额
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "savePushMoneyRecord")
	public String savePushMoneyRecord(String orderId,String turnOverDetailsId,String type,String pushmoneyUserId,String changeValue,String departmentId,HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] pushmoneyUserIds = pushmoneyUserId.split(",");
			String[] changeValues = changeValue.split(",");
			String[] departmentIds = departmentId.split(",");
			
			List<OrderPushmoneyRecord> list = new ArrayList<OrderPushmoneyRecord>();
			if(pushmoneyUserIds.length > 0){
				for(int i=0;i<pushmoneyUserIds.length;i++){
					OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
					orderPushmoneyRecord.setOrderId(orderId);
					orderPushmoneyRecord.setTurnOverDetailsId(Integer.valueOf(turnOverDetailsId));
					orderPushmoneyRecord.setType(Integer.valueOf(type));
					orderPushmoneyRecord.setPushmoneyUserId(pushmoneyUserIds[i]);
					orderPushmoneyRecord.setPushMoney(Double.valueOf(changeValues[i]));
					orderPushmoneyRecord.setDepartmentId(Integer.valueOf(departmentIds[i]));
					orderPushmoneyRecord.setOfficeId(UserUtils.getUser().getOffice().getId());
					orderPushmoneyRecord.setCreateBy(UserUtils.getUser());
					list.add(orderPushmoneyRecord);
				}
			}
			
			turnOverDetailsService.savePushMoneyRecord(list);
			type = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存业务员提成营业额信息错误", e);
			logger.error("保存业务员提成营业额信息错误：" + e.getMessage());
			type = "error";
		}
		return type;
	}
	
	/**
	 * 查看营业额操作日志
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "operationLog")
	public String operationLog(OrderPushmoneyRecord orderPushmoneyRecord,HttpServletRequest request,HttpServletResponse response,Model model) {
		try {
			Page<OrderPushmoneyRecord> page = turnOverDetailsService.queryDetailsForPush(new Page<OrderPushmoneyRecord>(request, response), orderPushmoneyRecord);
			model.addAttribute("page",page);
			model.addAttribute("turnOverDetailsId",orderPushmoneyRecord.getTurnOverDetailsId());
			model.addAttribute("orderId",orderPushmoneyRecord.getOrderId());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看营业额操作日志页面", e);
			logger.error("查看营业额操作日志出错信息：" + e.getMessage());
		}
		return "modules/ec/operationLog";
	}
	
	/**
	 * 跳转售后转虚拟订单页面
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "afterSaleOrdersForm")
	public String afterSaleOrdersForm(HttpServletRequest request,Orders orders,Model model){
		try {
			
			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转虚拟订单页面", e);
			logger.error("方法：afterSaleOrdersForm，跳转售后转虚拟订单页面出错：" + e.getMessage());
		}

		return "modules/ec/afterSaleOrdersForm";
	}
	
	/**
	 * 售后转单虚拟订单添加虚拟商品页面
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addAfterSaleGoods")
	public String addAfterSaleGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			
			model.addAttribute("orders", orders);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转单添加虚拟商品页面", e);
			logger.error("跳转售后转单添加虚拟商品页面出错信息：" + e.getMessage());
		}

		return "modules/ec/addAfterSaleGoods";
	}
	
	/**
	 * 保存售后转单的虚拟订单
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAfterSaleVirtualOrder")
	public String saveAfterSaleVirtualOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveAfterSaleVirtualOrder(orders);
			addMessage(redirectAttributes, "创建订单'" + orders.getOrderid() + "'成功,若想查看请到订单列表页");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加售后转单的虚拟订单", e);
			logger.error("方法：saveAfterSaleVirtualOrder，添加售后转单的虚拟订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建订单失败！");
		}
		return "redirect:" + adminPath + "/ec/returned/list";
	}
	
	/**
	 * 跳转售后转实物订单页面
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "afterSaleKindOrdersForm")
	public String afterSaleKindOrdersForm(HttpServletRequest request,Orders orders,Model model){
		try {
			
			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转实物订单页面", e);
			logger.error("方法：afterSaleKindOrdersForm，跳转售后转实物订单页面出错：" + e.getMessage());
		}

		return "modules/ec/afterSaleKindOrdersForm";
	}
	
	/**
	 * 售后转单实物订单添加商品页面
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addAfterSaleKindOderGoods")
	public String addAfterSaleKindOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			
			model.addAttribute("orders", orders);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转单添加实物商品页面", e);
			logger.error("跳转售后转单添加实物商品页面出错信息：" + e.getMessage());
		}

		return "modules/ec/addAfterSaleKindOderGoods";
	}
	
	/**
	 * 保存售后转单的实物订单
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAfterSaleKindOrder")
	public String saveAfterSaleKindOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveAfterSaleKindOrder(orders);
			addMessage(redirectAttributes, "创建订单'" + orders.getOrderid() + "'成功,若想查看请到订单列表页");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加售后转单的实物订单", e);
			logger.error("方法：saveAfterSaleKindOrder，添加售后转单的实物订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建订单失败！");
		}
		return "redirect:" + adminPath + "/ec/returned/list";
	}
	
	/**
	 * 跳转售后转套卡订单页面
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "afterSaleSuitCardOrdersForm")
	public String afterSaleSuitCardOrdersForm(HttpServletRequest request,Orders orders,Model model){
		try {
			
			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转套卡订单页面", e);
			logger.error("方法：afterSaleSuitCardOrdersForm，跳转售后转套卡订单页面页面出错：" + e.getMessage());
		}

		return "modules/ec/afterSaleSuitCardOrdersForm";
	}
	
	/**
	 * 售后转单套卡订单添加商品页面
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addAfterSaleSuitCardOderGoods")
	public String addAfterSaleSuitCardOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			
			model.addAttribute("orders", orders);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转单添加套卡商品页面", e);
			logger.error("跳转售后转单添加套卡商品页面出错信息：" + e.getMessage());
		}

		return "modules/ec/addAfterSaleSuitCardOderGoods";
	}
	
	/**
	 * 售后转单查找套卡的子项
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectAfterSaleSuitCardSon")
	@ResponseBody
	public String selectAfterSaleSuitCardSon(Goods goods,HttpServletRequest request,Model model){
		String suitCardSons = "";
		int num;
		try{
			double orderAmount = Double.valueOf(request.getParameter("orderAmount"));  //卡项成交价
			double actualPayment = Double.valueOf(request.getParameter("actualPayment"));  //卡项实际付款
			double costPrice = Double.valueOf(request.getParameter("costPrice"));  //卡项系统价
			double debtMoney = Double.valueOf(request.getParameter("debtMoney"));  //卡项欠款
			double spareMoney = Double.valueOf(request.getParameter("spareMoney"));  //卡项余款
			int tail = Integer.valueOf(request.getParameter("tail"));  //用来表示添加的卡项商品
			int isNeworder = Integer.valueOf(request.getParameter("isNeworder"));  //区分新老订单
			Date realityAddTime =  new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("realityAddTime"));  //实际下单时间
			int goodsId = goods.getGoodsId();
			List<Goods> goodsList = ordersService.selectCardSon(goodsId);
			if(goodsList.size() > 0){
				num = goodsList.size();
				
				suitCardSons =
					"<tr class=suitCard"+tail+"> "+
						"<td rowspan="+num+"> "+goods.getGoodsName()+"<input id='goodselectIds' name='goodselectIds' type='hidden' value='"+goodsId+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsName()+"</td> "+
						"<td rowspan="+num+"> "+costPrice+"</td> "+
						"<td> "+goodsList.get(0).getMarketPrice()+"<input id='orderAmounts' name='orderAmounts' type='hidden' value='"+orderAmount+"'></td> "+
						"<td> "+goodsList.get(0).getShopPrice()+"<input id='actualPayments' name='actualPayments' type='hidden' value='"+actualPayment+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsNum()+"</td> "+
						"<input id='realityAddTime' name='realityAddTimeList' type='hidden' value='"+realityAddTime+"'>";
				if(isNeworder == 0){
					suitCardSons = suitCardSons + 
						"<td></td> ";
				}else if(isNeworder == 1){
					suitCardSons = suitCardSons + 
						"<td><input id='remaintimes_0' value='"+goodsList.get(0).getGoodsNum()+"' name='remaintimeNums' min='0' max='"+goodsList.get(0).getGoodsNum()+"' onkeyup='this.value=this.value.replace(/[^\\d]/g,&quot;&quot;)' class='form-control required'/></td> ";
				}
				suitCardSons = suitCardSons + 
						"<td rowspan="+num+"> "+spareMoney+"</td> "+
						"<td rowspan="+num+"> "+debtMoney+"</td> "+
						"<td rowspan="+num+"> "+
							"<a href='#' class='btn btn-danger btn-xs' onclick='delFile("+tail+","+costPrice+","+orderAmount+","+spareMoney+","+actualPayment+","+debtMoney+")'><i class='fa fa-trash'></i> 删除</a> "+
						"</td>"+
					"</tr>";
				for(int i=1;i<num;i++){
					suitCardSons = suitCardSons +
						"<tr class=suitCard"+tail+"> "+
							"<td> "+goodsList.get(i).getGoodsName()+"</td> "+
							"<td> "+goodsList.get(i).getMarketPrice()+"</td> "+
							"<td> "+goodsList.get(i).getShopPrice()+"</td> "+
							"<td> "+goodsList.get(i).getGoodsNum()+"</td> ";
					if(isNeworder == 0){
						suitCardSons = suitCardSons + 
							"<td></td></tr> ";
					}else if(isNeworder == 1){
						suitCardSons = suitCardSons + 
							"<td><input id='remaintimes_"+i+"' value='"+goodsList.get(i).getGoodsNum()+"' name='remaintimeNums' min='0' max='"+goodsList.get(i).getGoodsNum()+"' onkeyup='this.value=this.value.replace(/[^\\d]/g,&quot;&quot;)' class='form-control required'/></td></tr> ";
					}
				}
				
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "售后转单查找套卡的子项", e);
			logger.error("售后转单查找套卡的子项出错信息：" + e.getMessage());
		}
		return suitCardSons.toString();
	}
	
	/**
	 * 保存售后转单的套卡订单
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAfterSaleSuitCardOrder")
	public String saveAfterSaleSuitCardOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveAfterSaleSuitCardOrder(orders);
			addMessage(redirectAttributes, "创建套卡订单'" + orders.getOrderid() + "'成功,若想查看请到订单列表页");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加售后转单的套卡订单", e);
			logger.error("方法：saveAfterSaleSuitCardOrder，添加售后转单的套卡订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建订单失败！");
		}
		return "redirect:" + adminPath + "/ec/returned/list";
	}
	
	
	/**
	 * 跳转售后转通用卡订单页面
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "afterSaleCommonCardOrdersForm")
	public String afterSaleCommonCardOrdersForm(HttpServletRequest request,Orders orders,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转通用卡订单页面", e);
			logger.error("方法：afterSaleCommonCardOrdersForm，跳转售后转通用卡订单页面出错：" + e.getMessage());
		}

		return "modules/ec/afterSaleCommonCardOrdersForm";
	}
	
	/**
	 * 售后转单通用卡订单添加商品页面
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addAfterSaleCommonOderGoods")
	public String addAfterSaleCommonOderGoods(HttpServletRequest request, Orders orders, Model model) {
		try {
			
			model.addAttribute("orders", orders);

		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转售后转单添加通用卡商品页面", e);
			logger.error("跳转售后转单添加通用卡商品页面出错信息：" + e.getMessage());
		}

		return "modules/ec/addAfterSaleCommonOderGoods";
	}
	
	/**
	 * 售后转单查找通用卡的子项
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="selectAfterSaleCommonCardSon")
	@ResponseBody
	public String selectAfterSaleCommonCardSon(Goods goods,HttpServletRequest request,Model model){
		String suitCardSons = "";
		int num;
		try{
			double actualPayment = Double.valueOf(request.getParameter("actualPayment"));  //卡项实际付款（前）
			String speckeyname = request.getParameter("speckeyname");                     //卡项规格名称
			String speckey = request.getParameter("speckey");                     //卡项规格key
			double marketPrice = Double.valueOf(request.getParameter("marketPrice"));  //卡项市场价
			double goodsPrice = Double.valueOf(request.getParameter("goodsPrice"));  //卡项优惠价
			double costPrice = Double.valueOf(request.getParameter("costPrice"));  //卡项系统价
			double orderAmount = Double.valueOf(request.getParameter("orderAmount"));  //卡项成交价
			double afterPayment = Double.valueOf(request.getParameter("afterPayment"));  //卡项实际付款（后）
			String actualNum = request.getParameter("actualNum");  //卡项实际次数
			double debtMoney = Double.valueOf(request.getParameter("debtMoney"));  //卡项欠款
			double spareMoney = Double.valueOf(request.getParameter("spareMoney"));  //卡项余款
			int tail = Integer.valueOf(request.getParameter("tail"));  //用来表示添加的卡项商品
			Date realityAddTime =  new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("realityAddTime"));  //实际下单时间
			Integer actualSpeckeyNum = Integer.valueOf(request.getParameter("actualSpeckeyNum"));     //售后转单订单自定的实际规格次数
			
			int goodsId = goods.getGoodsId();
			List<Goods> goodsList = ordersService.selectCardSon(goodsId);
			if(goodsList.size() > 0){
				num = goodsList.size();
				
				suitCardSons =
					"<tr class= commonCard"+tail+"> "+
						"<td rowspan="+num+"> "+goods.getGoodsName()+"<input id='goodselectIds' name='goodselectIds' type='hidden' value='"+goodsId+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsName()+"</td> "+
						"<td rowspan="+num+"> "+speckeyname+"<input id='speckeys' name='speckeys' type='hidden' value='"+speckey+"'></td> "+
						"<td rowspan="+num+"> "+actualSpeckeyNum+"<input id='actualSpeckeyNum' name='actualSpeckeyNums' type='hidden' value='"+actualSpeckeyNum+"'></td> "+
						"<td rowspan="+num+"> "+marketPrice+"</td> "+
						"<td rowspan="+num+"> "+goodsPrice+"</td> "+
						"<td rowspan="+num+"> "+costPrice+"</td> "+
						"<td rowspan="+num+"> "+orderAmount+"<input id='orderAmounts' name='orderAmounts' type='hidden' value='"+orderAmount+"'></td> "+
						"<td> "+goodsList.get(0).getGoodsNum()+"</td> "+
						"<td rowspan="+num+"> "+afterPayment+"<input id='actualPayments' name='actualPayments' type='hidden' value='"+actualPayment+"'></td> "+
						"<td rowspan="+num+"> "+actualNum+"<input id='remaintimes' name='remaintimeNums' type='hidden' value='"+actualNum+"'></td> "+
						"<td rowspan="+num+"> "+spareMoney+"</td> "+
						"<td rowspan="+num+"> "+debtMoney+"</td> "+
						"<td rowspan="+num+"> "+
							"<a href='#' class='btn btn-danger btn-xs' onclick='delFile("+tail+","+costPrice+","+orderAmount+","+spareMoney+","+afterPayment+","+debtMoney+")'><i class='fa fa-trash'></i> 删除</a> "+
						"</td>"+
						"<input id='realityAddTime' name='realityAddTimeList' type='hidden' value='"+realityAddTime+"'>"+
					"</tr>";
				for(int i=1;i<num;i++){
					suitCardSons = suitCardSons +
						"<tr class= commonCard"+tail+"> "+
							"<td> "+goodsList.get(i).getGoodsName()+"</td> "+
							"<td> "+goodsList.get(i).getGoodsNum()+"</td> "+
						"</tr>";
				}
				
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找通用卡子项", e);
			logger.error("查找通用卡子项出错信息：" + e.getMessage());
		}
		return suitCardSons.toString();
	}
	
	/**
	 * 保存售后转单的通用卡订单
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "saveAfterSaleCommonCardlOrder")
	public String saveAfterSaleCommonCardlOrder(Orders orders, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveAfterSaleCommonCardlOrder(orders);
			addMessage(redirectAttributes, "创建通用卡订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建售后转单通用卡订单", e);
			logger.error("方法：saveAfterSaleCommonCardlOrder，创建售后转单通用卡订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建售后转单通用卡订单失败！");
		}

		return "redirect:" + adminPath + "/ec/returned/list";
	}
}

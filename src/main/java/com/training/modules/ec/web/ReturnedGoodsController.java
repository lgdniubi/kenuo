package com.training.modules.ec.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.PdWareHouseDao;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.PdWareHouse;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.ReturnedGoodsService;
import com.training.modules.ec.utils.CourierUtils;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 订单Controller
 * 
 * @author yangyang
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/returned")
public class ReturnedGoodsController extends BaseController {

	@Autowired
	private ReturnedGoodsService returnedGoodsService;
	@Autowired
	private PdWareHouseDao wareHouseDao;
	@Autowired
	private OrderGoodsService ordergoodService;

	@ModelAttribute
	public ReturnedGoods get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return returnedGoodsService.get(id);
		} else {
			return new ReturnedGoods();
		}
	}

	/**
	 * 退货订单分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:returned:list")
	@RequestMapping(value = { "list", "" })
	public String list(ReturnedGoods returnedGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReturnedGoods> page = returnedGoodsService.findReturnList(new Page<ReturnedGoods>(request, response),returnedGoods);
		model.addAttribute("page", page);
		return "modules/ec/returnedGoodsList";
	}

	/**
	 * 修改
	 * 
	 * @param orders
	 * @param model
	 * @return
	 **/

	@RequestMapping(value = "returnform")
	public String returnform(ReturnedGoods returnedGoods, String id, HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			List<ReturnedGoodsImages> imgList=new ArrayList<ReturnedGoodsImages>();
			returnedGoods = returnedGoodsService.get(id);
			imgList=returnedGoodsService.selectImgById(id);
			List<PdWareHouse> pdlist=wareHouseDao.findAllList(new PdWareHouse());
			returnedGoods.setImgList(imgList);
			model.addAttribute("pdlist",pdlist);
			model.addAttribute("returnedGoods", returnedGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "审核页面", e);
			logger.error("方法：returnform,审核页面" + e.getMessage());
			// TODO: handle exception
		}
		
		return "modules/ec/returnGoodsForm";
	}
	/**
	 * 卡项售后
	 * 
	 * @param orders
	 * @param model
	 * @return
	 **/
	
	@RequestMapping(value = "returnformCard")
	public String returnformCard(ReturnedGoods returnedGoods, String id, HttpServletRequest request,HttpServletResponse response, Model model) {
		String flag = request.getParameter("flag");
		try {
			//查询的售后图片添加到退货中
			List<ReturnedGoodsImages> imgList=new ArrayList<ReturnedGoodsImages>();
			returnedGoods = returnedGoodsService.get(id);
			imgList=returnedGoodsService.selectImgById(id);
			returnedGoods.setImgList(imgList);
			
			String suitCardSons = "";
			String isreal = "";
			int num;
			List<List<OrderGoods>> result = new ArrayList<List<OrderGoods>>();//存放每个卡项商品和它的子项集合
			List<OrderGoods> resultSon = new ArrayList<OrderGoods>();//存放一个卡项商品和它的子项
			
			OrderGoods og = new OrderGoods();
			og.setOrderid(returnedGoods.getOrderId());
			og.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
			
			List<OrderGoods> list=ordergoodService.cardOrderid(og);//根据订单id和mapping查询订单中的套卡及其子项
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
			if(returnedGoods.getIsReal() == 2){//套卡 售后
				if(result.size() > 0){
					for(List<OrderGoods> lists:result){                            
						if((lists.size() - 1) > 0){
							num = lists.size() - 1;
							OrderGoods father = lists.get(0);
							isreal = lists.get(1).getIsreal()==0?"实物":"虚拟";
							suitCardSons = suitCardSons +
									"<tr> "+
									"<td rowspan='"+num+"'>"+father.getRecid()+"</td>"+
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
			}else if(returnedGoods.getIsReal() == 3){//通用卡 售后
				if(result.size() > 0){
					for(List<OrderGoods> lists:result){                            
						if((lists.size() - 1) > 0){
							num = lists.size() - 1;
							OrderGoods father = lists.get(0);
							isreal = lists.get(1).getIsreal()==0?"实物":"虚拟";
							suitCardSons = suitCardSons +
									"<tr> "+
									"<td rowspan='"+num+"'>"+father.getRecid()+"</td>"+
									"<td align='center' rowspan='"+num+"'> "+father.getGoodsname()+"</td> "+
									"<td align='center' rowspan='"+num+"'>通用卡</td> "+
									"<td align='center'> "+lists.get(1).getGoodsname()+"</td> "+
									"<td align='center'> "+isreal+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getMarketprice()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getGoodsprice()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getCostprice()+"</td> "+
									"<td align='center'> "+lists.get(1).getGoodsnum()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getOrderAmount()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getTotalAmount()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getOrderArrearage()+"</td> "+
								"</tr> ";
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
			}
			
			//查询卡项子项实物的售后数量
			String realnum = "";
			List<ReturnedGoods> rgList = returnedGoodsService.getRealnum(returnedGoods);
			if(rgList.size() != 0){
				for (ReturnedGoods rg : rgList) {
					realnum = realnum + 
						"<label>"+rg.getGoodsName()+"  售后数量：</label><input style='width: 180px;height:30px;' class='form-control' value='"+rg.getReturnNum()+"' readonly='true'/><p></p>";
				}
				model.addAttribute("realnum",realnum);
			}
			
			model.addAttribute("flag",flag);
			model.addAttribute("returnGoodsCard",suitCardSons.toString());
			model.addAttribute("returnedGoods", returnedGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "审核页面", e);
			logger.error("方法：returnform,审核页面" + e.getMessage());
			// TODO: handle exception
		}
		
		return "modules/ec/returnGoodsFormCard";
	}

	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "save")
	public String save(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveEdite(returnedGoods); 
			
			addMessage(redirectAttributes, "保存退货订单'" + returnedGoods.getId() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存退货订单", e);
			logger.error("方法：save,保存退货订单" + e.getMessage());
			addMessage(redirectAttributes, "保存退货订单失败！");
		}

		return "redirect:" + adminPath + "/ec/returned/list";

	}
	
	
	
	
	/**
	 * 确认入库
	 * 
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "confirmTake")
	public String confirmTake(HttpServletRequest request,ReturnedGoods returnedGoods, RedirectAttributes redirectAttributes) {
		try {
			String currentUser = UserUtils.getUser().getName();
			returnedGoods = returnedGoodsService.get(returnedGoods.getId());
			if("12".equals(returnedGoods.getReturnStatus()) || "13".equals(returnedGoods.getReturnStatus())){
				returnedGoods.setReceiptBy(currentUser);
				returnedGoods.setReturnStatus("14");
			}else if("22".equals(returnedGoods.getReturnStatus()) || "23".equals(returnedGoods.getReturnStatus())){
				returnedGoods.setReceiptBy(currentUser);
				returnedGoods.setReturnStatus("24");
			}
			returnedGoodsService.confirmTake(returnedGoods);
			addMessage(redirectAttributes, "退货单号：" + returnedGoods.getId() + "，已确认入库。");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "确认入库", e);
			logger.error("方法：confirm,确认入库" + e.getMessage());
			addMessage(redirectAttributes, "确认入库失败");
		}

		return "redirect:" + adminPath + "/ec/returned/list";
	}
	
	/**
	 * 物流信息
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "againSend")
	public String againSend(HttpServletRequest request,ReturnedGoods returnedGoods, Model model) {
		try {
			returnedGoods = returnedGoodsService.get(returnedGoods.getId());
			model.addAttribute("returnedGoods", returnedGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "退货物流信息页面", e);
			logger.error("退货物流信息页面：" + e.getMessage());
		}
		
		return "modules/ec/againSendShiping";
	}
	
	
	
	/**
	 * 重新发货
	 * 
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "againSendApply")
	public String againSendApply(HttpServletRequest request,ReturnedGoods returnedGoods, RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoods.setReturnStatus("25");
			returnedGoodsService.againSendApply(returnedGoods);
			addMessage(redirectAttributes, "退货单号：" + returnedGoods.getId() + "，确认重新发货。");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "确认重新发货", e);
			logger.error("方法：confirm,确认重新发货" + e.getMessage());
			addMessage(redirectAttributes, "确认重新发货失败");
		}

		return "redirect:" + adminPath + "/ec/returned/list";
	}
	
	/**
	 * 物流信息
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shippingList")
	public String shippingList(HttpServletRequest request,ReturnedGoods returnedGoods, Model model) {
		try {
			String	xmlString="";
			returnedGoods = returnedGoodsService.get(returnedGoods.getId());
			List<CourierResultXML> list=new ArrayList<CourierResultXML>();
			
			String shippingcode = returnedGoods.getShippingCode();
			if("25".equals(returnedGoods.getReturnStatus()) || "26".equals(returnedGoods.getReturnStatus())){
				//物流编号不为null and 不为空 and 不为NULL
				if(null != shippingcode && shippingcode.length() >0 && !"NULL".equals(shippingcode.toUpperCase())){
					xmlString=CourierUtils.findCourierPost(returnedGoods.getShippingCode());
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
			model.addAttribute("returnedGoods", returnedGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "退货物流信息页面", e);
			logger.error("退货物流信息页面：" + e.getMessage());
		}
		
		return "modules/ec/returnShippingView";
	}
	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "UpdateShipping")
	public String UpdateShipping(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			String currentUser = UserUtils.getUser().getName();
			returnedGoods.setShipperBy(currentUser);
			returnedGoods.setReturnStatus("26");
			returnedGoodsService.UpdateShipping(returnedGoods);
			addMessage(redirectAttributes, "退货单：" + returnedGoods.getId() + "'物流保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "退货物流修改", e);
			logger.error("方法：save,退货物流修改" + e.getMessage());
			addMessage(redirectAttributes, "退货物流修改失败！");
		}

		return "redirect:" + adminPath + "/ec/returned/list";

	}

	/**
	 * 申请退款
	 * @param returnedGoods
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "confirmApply")
	public String confirmApply(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			returnedGoods.setReturnStatus("15");
			returnedGoodsService.confirmApply(returnedGoods);
			addMessage(redirectAttributes, "退货单：" + returnedGoods.getId() + "'申请退款成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "退货申请退款", e);
			logger.error("方法：save,退货申请退款" + e.getMessage());
			addMessage(redirectAttributes, "退货申请退款失败！");
		}

		return "redirect:" + adminPath + "/ec/returned/list";

	}
	
	/**
	 * 确认退款
	 * againSend
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "confirm")
	public String confirm(HttpServletRequest request,ReturnedGoods returnedGoods, RedirectAttributes redirectAttributes) {
		try {
			String currentUser = UserUtils.getUser().getName();
			returnedGoods.setFinancialBy(currentUser);
			returnedGoods.setReturnStatus("16");
			returnedGoodsService.updateReturnMomeny(returnedGoods);
			addMessage(redirectAttributes, "退货单号" + returnedGoods.getId() + "'退款成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "确认退款", e);
			logger.error("方法：confirm,确认退款" + e.getMessage());
			addMessage(redirectAttributes, "确认退款失败");
		}

		return "redirect:" + adminPath + "/ec/returned/list";
	}
	/**
	 * 获取订单商品中的已退款金额
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@ResponseBody
	@RequestMapping(value = "getSurplusReturnAmount")
	public double getSurplusReturnAmount(HttpServletRequest request,ReturnedGoods returnedGoods, RedirectAttributes redirectAttributes) {
		return returnedGoodsService.getSurplusReturnAmount(returnedGoods);
	}
	/**
	 * 进入营业额界面
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "getTurnoverByOrderId")
	public String getUserTurnoverByOrderId(ReturnedGoods returnedGoods, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		//获取营业额信息
		TurnOverDetails list = returnedGoodsService.getTurnover(returnedGoods);
		model.addAttribute("mtmyTurnoverDetails", list);
		return "modules/ec/TurnoverDetails";
	}
	/**
	 * 获取店铺营业额的操作日志
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "findMtmyTurnoverDetailsList")
	public String findMtmyTurnoverDetailsList(TurnOverDetails turnOverDetails, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		Page<TurnOverDetails> page = returnedGoodsService.findMtmyTurnoverDetailsList(new Page<TurnOverDetails>(request, response),turnOverDetails);
		model.addAttribute("page", page);
		return "modules/ec/mtmyTurnoverDetailsList";
	}
	/**
	 * 编辑业务员营业额
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "editOrderPushmoneyRecord")
	public String editOrderPushmoneyRecord(TurnOverDetails turnOverDetails, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		DecimalFormat formater = new DecimalFormat("#0.##");
		
		//获取审核售后信息
		ReturnedGoods returnedGoods = new ReturnedGoods();
		returnedGoods.setOrderId(turnOverDetails.getOrderId());
		returnedGoods.setReturnedId(turnOverDetails.getDetailsId());
		turnOverDetails = returnedGoodsService.getTurnover(returnedGoods);
		
		String userTurnover = "";//传递jsp界面的页面展示
		List<OrderPushmoneyRecord> list = returnedGoodsService.findOrderPushmoneyRecordList(turnOverDetails);//获取业务员信息集合
		if(list.size() != 0){
			List<OrderPushmoneyRecord> pushmoneyList = returnedGoodsService.getReturnedPushmoneyList(turnOverDetails);//查询每个业务员的售后审核扣减的营业额
			//查询业务员营业额
			int num = list.size();
			double returnAmount = turnOverDetails.getAmount();//退款金额
			double beauticianTurnover= 0;//单个的（美容师营业额-该美容师已退营业额）
			double sumTurnover= 0;//合计的(美容师合计营业额-所有美容师已退营业额)
			double turnoverRatio= 0;//营业额占比
			double added = 0;//数据库查询的原始营业额
			String departmentIds = "";//所有部门的id字符串
			beauticianTurnover = list.get(0).getPushMoney();
			
			//之前的老订单会存在售后没有审核时间,由申请时间替代
			Date createDate = turnOverDetails.getCreateDate();
			if(createDate == null){
				createDate = turnOverDetails.getApplyDate();
			}
			if(pushmoneyList.size() !=0){//判断营业额是否为第一次编辑,为营业额赋值
				added = pushmoneyList.get(0).getPushMoney();
			}
			//获取各个部门的营业额合计
			List<OrderPushmoneyRecord> sumTurnoverList = returnedGoodsService.getSumBeauticianTurnover(turnOverDetails.getOrderId());
			for (OrderPushmoneyRecord opr : sumTurnoverList) {//循环所有部门的营业总额
				if(list.get(0).getDepartmentId() == opr.getDepartmentId()){//判断第一条信息属于哪个部门
					sumTurnover = opr.getPushMoney();//部门的营业额
					turnoverRatio = Double.parseDouble(formater.format(beauticianTurnover/sumTurnover*returnAmount));//占比
					userTurnover = userTurnover + 
							"<tr style='text-align: center;'> "+
							"<td rowspan='"+num+"'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
							"<td rowspan='"+num+"'>售后</td> "+
							"<td rowspan='"+num+"'>"+returnAmount+"</td> "+
							"<td style='text-align: center;'>"+list.get(0).getPushmoneyUserName()+"</td> "+
							"<td style='text-align: center;'>"+list.get(0).getDepartmentName()+"</td> "+
							"<td style='text-align: center;'>"+list.get(0).getPushmoneyUserMobile()+"</td> "+
							"<td style='text-align: center;'>"+added+"</td> "+
							"<td style='text-align: center;'>"+turnoverRatio+"</td> "+
							"<td style='text-align: center;'>"+
							"<input id='added0' name='added"+list.get(0).getDepartmentId()+"' type='hidden' value='"+added+"' class='form-control'>"+
							"<input id='Amount0' name='Amount"+list.get(0).getDepartmentId()+"' value='' class='form-control'>"+
							"<input id='beauticianTurnover0' value='"+beauticianTurnover+"' type='hidden' class='form-control'>"+
							"<input id='num' name='num' value='"+num+"' type='hidden' class='form-control'>"+
							"</td> "+
							"</tr>";
				}
				for (int i = 1; i < list.size(); i++) {//循环除第一条之外的业务员营业额,并且比较在哪个部门,计算营业额占比
					if(list.get(i).getDepartmentId() == opr.getDepartmentId()){
						if(pushmoneyList.size() !=0){//判断营业额是否为第一次编辑,为营业额赋值
							added = pushmoneyList.get(i).getPushMoney();
						}
						sumTurnover = opr.getPushMoney();//部门的营业额
						beauticianTurnover = list.get(i).getPushMoney();//单个业务员营业额
						turnoverRatio = Double.parseDouble(formater.format(beauticianTurnover/sumTurnover*returnAmount));//占比
						userTurnover = userTurnover + 
								"<tr style='text-align: center;'> "+
								"<td style='text-align: center;'>"+list.get(i).getPushmoneyUserName()+"</td> "+
								"<td style='text-align: center;'>"+list.get(i).getDepartmentName()+"</td> "+
								"<td style='text-align: center;'>"+list.get(i).getPushmoneyUserMobile()+"</td> "+
								"<td style='text-align: center;'>"+added+"</td> "+
								"<td style='text-align: center;'>"+turnoverRatio+"</td> "+
								"<td style='text-align: center;'>"+
								"<input id='added"+i+"' name='added"+list.get(i).getDepartmentId()+"' type='hidden' value='"+added+"' class='form-control'>"+
								"<input id='Amount"+i+"' name='Amount"+list.get(i).getDepartmentId()+"' value='' class='form-control'>"+
								"<input id='beauticianTurnover"+i+"' value='"+beauticianTurnover+"' type='hidden' class='form-control'>"+
								"</td> "+
								"</tr>";
					}
				}
				//拼接部门字符串
				departmentIds += opr.getDepartmentId()+",";
			}
			model.addAttribute("departmentIds",departmentIds);
			model.addAttribute("returnAmount",returnAmount);
			model.addAttribute("orderId",turnOverDetails.getOrderId());
			model.addAttribute("returnedId",turnOverDetails.getDetailsId());
			model.addAttribute("userTurnover",userTurnover);
		}else{
			model.addAttribute("userTurnover",userTurnover);
		}
		return "modules/ec/pushMoneyForm";
	}
	/**
	 * 保存业务员营业额
	 * @param orderPushmoneyRecord
	 * @param redirectAttributes
	 * @return  
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrderPushmoneyRecord")
	public String saveOrderPushmoneyRecord(OrderPushmoneyRecord orderPushmoneyRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		String flag="";
		try {
			returnedGoodsService.saveBeauticianTurnover(orderPushmoneyRecord); 
			flag = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存业务员营业额", e);
			logger.error("方法：save,保存业务员营业额" + e.getMessage());
			flag = "error";
		}
		return flag;
	}
	/**
	 * 编辑店铺营业额
	 * @param orderPushmoneyRecord
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "editMtmyTurnoverDetails")
	public String editMtmyTurnoverDetails(TurnOverDetails turnOverDetails, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		DecimalFormat formater = new DecimalFormat("#0.##");
		//获取营业额明细表
		ReturnedGoods returnedGoods = new ReturnedGoods();
		returnedGoods.setOrderId(turnOverDetails.getOrderId());
		returnedGoods.setReturnedId(turnOverDetails.getDetailsId());
		turnOverDetails = returnedGoodsService.getTurnover(returnedGoods);
		//获取店营业额明细列表
		List<TurnOverDetails> list = returnedGoodsService.getMtmyTurnoverDetailsList(turnOverDetails);
		//查询店的售后审核扣减的营业额
		List<TurnOverDetails> amountList = returnedGoodsService.getReturnedAmountList(turnOverDetails);
		
		double returnAmount = turnOverDetails.getAmount();//退款金额
		//获取营业额占比 = （店铺营业额-店铺已退营业额）/(店铺合计营业额-店铺已退营业额)*退款
		double storeTurnover = 0;//单个（店铺营业额-店铺已退营业额）
		double sumTurnover= 0;//合计的(店铺合计营业额-店铺已退营业额)
		double turnoverRatio= 0;//获取营业额占比
		double added = 0;//数据库查询的原始营业额
		String shopTurnover = "";//jsp界面的页面展示字符串
		if(list.size() != 0 ){
			//之前的老订单会存在售后没有审核时间,由申请时间替代
			Date createDate = turnOverDetails.getCreateDate();
			if(createDate == null){
				createDate = turnOverDetails.getApplyDate();
			}
			storeTurnover = list.get(0).getAmount();
			//获取每个店铺的营业额
			List<TurnOverDetails> sumTurnoverList = returnedGoodsService.getSumTurnover(turnOverDetails);
			for (TurnOverDetails mtd : sumTurnoverList) {//得到合计营业额
				sumTurnover += mtd.getAmount(); 
			}
			turnoverRatio = Double.parseDouble(formater.format(storeTurnover/sumTurnover*returnAmount));
	
			if(amountList.size() != 0){//判断营业额是否为第一次编辑,为营业额赋值
				added = amountList.get(0).getAmount();
			}
			int num = list.size(); //集合的长度
			shopTurnover = shopTurnover + 
					"<tr style='text-align: center;'> "+
						"<td rowspan='"+num+"'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
						"<td rowspan='"+num+"'>售后</td> "+
						"<td rowspan='"+num+"'>"+returnAmount+"</td> "+
						"<td style='text-align: center;'>"+list.get(0).getBelongOfficeName()+"</td> "+
						"<td style='text-align: center;'>"+added+"</td> "+
						"<td style='text-align: center;'>"+turnoverRatio+"</td> "+
						"<td style='text-align: center;'>"+
							"<input id='addeds0' name='addeds' type='hidden' value='"+added+"' class='form-control'>"+
							"<input id='amount0' name='amount' value='' class='form-control'>"+
							"<input id='storeTurnover0' value='"+storeTurnover+"' type='hidden' class='form-control'>"+
							"<input id='num' name='num' value='"+num+"' type='hidden' class='form-control'>"+
						"</td> "+
					"</tr>";
			for (int i = 1; i < list.size(); i++) {
				if(amountList.size() != 0){//判断营业额是否为第一次编辑,为营业额赋值
					added = amountList.get(i).getAmount();
				}
				storeTurnover = list.get(i).getAmount();//单个店铺营业额合计之后的金额
				turnoverRatio = Double.parseDouble(formater.format(storeTurnover/sumTurnover*returnAmount));
				shopTurnover = shopTurnover + 
					"<tr style='text-align: center;'> "+
						"<td style='text-align: center;'>"+list.get(i).getBelongOfficeName()+"</td> "+
						"<td style='text-align: center;'>"+added+"</td> "+
						"<td style='text-align: center;'>"+turnoverRatio+"</td> "+
						"<td style='text-align: center;'>"+
							"<input id='addeds"+i+"' name='addeds' type='hidden' value='"+added+"' class='form-control'>"+
							"<input id='amount"+i+"' name='amount' value='' class='form-control'>"+
							"<input id='storeTurnover"+i+"' value='"+storeTurnover+"' type='hidden' class='form-control'>"+
						"</td> "+
					"</tr>";
			}
		}
		model.addAttribute("returnAmount",returnAmount);
		model.addAttribute("turnOverDetails",turnOverDetails);
		model.addAttribute("shopTurnover",shopTurnover);
		
		return "modules/ec/mtmyTurnoverDetailsForm";
	}
	/**
	 * 保存店铺营业额
	 * @param orderPushmoneyRecord
	 * @param redirectAttributes
	 * @return  
	 */
	@ResponseBody
	@RequestMapping(value = "saveMtmyTurnoverDetails")
	public String saveMtmyTurnoverDetails(TurnOverDetails turnOverDetails, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		String flag="";
		try {
			returnedGoodsService.saveMtmyTurnoverDetails(turnOverDetails); 
			flag = "success";
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存店营业额", e);
			logger.error("方法：save,保存店营业额" + e.getMessage());
			flag = "error";
		}
		return flag;
	}
}

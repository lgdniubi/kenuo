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
import com.training.modules.ec.service.OrderGoodsDetailsService;
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
	@Autowired
	private OrderGoodsDetailsService orderGoodsDetailsService;
	
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
		String flag = request.getParameter("flag");
		model.addAttribute("flag",flag);
		int goodsNum = 0;//实物的可售后数量
		double amount = 0.0;//实物的可售后金额
		int times = 0;//虚拟的可售后次数
		double orderArrearage = 0;//判断有无'平欠款'记录  存在:不能修改售后数量,不存在:能修改售后数量(实物平欠款,购买数量全部退)
		int serviceTimes = 0;//判断是否为时限卡
		try {
			List<ReturnedGoodsImages> imgList=new ArrayList<ReturnedGoodsImages>();
			returnedGoods = returnedGoodsService.get(id);
			imgList=returnedGoodsService.selectImgById(id);
			List<PdWareHouse> pdlist=wareHouseDao.findAllList(new PdWareHouse());//查询仓库信息
			returnedGoods.setImgList(imgList);
			
			orderArrearage = orderGoodsDetailsService.getSumArrearage(returnedGoods);//查询该商品是否存在欠款
			//计算可售后数量(售后次数)和售后金额
			OrderGoods orderGoods = ordergoodService.getTotalAmountAndTimes(returnedGoods);//获取商品的实付金额和剩余服务次数
			ReturnedGoods rg = returnedGoodsService.getAmountAndNum(returnedGoods);//获取不包含自己本身的总的售后金额和售后数量
			amount = ((orderGoods.getTotalAmount()*100) - (rg.getReturnAmount()*100))/100;//可售后金额
			if(returnedGoods.getIsReal() == 0){//实物查询的数据
				goodsNum = ordergoodService.getGoodsNum(returnedGoods);//实物:获取mapping表中的总购买数量
				goodsNum = goodsNum - rg.getReturnNum();//可售后的数量
			}else if(returnedGoods.getIsReal() == 1){//虚拟需要查询的数据
				serviceTimes = orderGoods.getServicetimes();//判断是否为时限卡
				times = orderGoods.getRemaintimes() + returnedGoods.getReturnNum();//售后次数 = 剩余次数+售后次数
			}
			
			model.addAttribute("amount",amount);//商品的扣售后金额
			model.addAttribute("pdlist",pdlist);//查询仓库信息
			model.addAttribute("returnedGoods", returnedGoods);//申请售后信息
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "实物虚拟审核页面", e);
			logger.error("方法：实物虚拟,审核页面" + e.getMessage());
		}
		if(returnedGoods.getIsReal() == 0){
			model.addAttribute("orderArrearage",orderArrearage);//有无平欠款记录
			model.addAttribute("goodsNum",goodsNum);//商品的金额可售后数量
			return "modules/ec/returnGoodsFormKind";
		}else{
			model.addAttribute("times",times);//商品的可售后次数
			model.addAttribute("serviceTimes",serviceTimes);//判断是否为时限卡
			return "modules/ec/returnGoodsForm";
		}
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
		model.addAttribute("flag",flag);
		//查询的售后图片添加到退货中
		List<ReturnedGoodsImages> imgList=new ArrayList<ReturnedGoodsImages>();
		returnedGoods = returnedGoodsService.get(id);
		imgList=returnedGoodsService.selectImgById(id);
		returnedGoods.setImgList(imgList);
		List<PdWareHouse> pdlist=wareHouseDao.findAllList(new PdWareHouse());//查询仓库信息
		model.addAttribute("pdlist",pdlist);
		
		String suitCardSons = "";
		String isreal = "";
		int num;
		List<List<OrderGoods>> result = new ArrayList<List<OrderGoods>>();//存放每个卡项商品和它的子项集合
		List<OrderGoods> resultSon = new ArrayList<OrderGoods>();//存放一个卡项商品和它的子项
		
		OrderGoods og = new OrderGoods();
		og.setOrderid(returnedGoods.getOrderId());
		og.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
		List<OrderGoods> list=ordergoodService.cardOrderid(og);//根据订单id和mapping查询订单中的套卡及其子项
		//获得卡项的实付金额和剩余服务次数(主要用于通用卡的,套卡只是得到金额)
		OrderGoods orderGoods = ordergoodService.getTotalAmountAndTimes(returnedGoods);//获取商品的实付金额和剩余服务次数
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
		if(returnedGoods.getIsReal() == 2){//套卡 审核售后
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
								"<td align='center'> "+lists.get(1).getRatioPrice()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getCostprice()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getRatio()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getRatioPrice()+"</td> "+
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
									"<td align='center'> "+lists.get(i).getRatioPrice()+"</td> "+
									"<td align='center'> "+lists.get(i).getGoodsnum()+"</td> "+
								"</tr>";
						}
			
					}
				}
			}
			model.addAttribute("amount",orderGoods.getTotalAmount());
			model.addAttribute("returnGoodsCard",suitCardSons.toString());
			model.addAttribute("returnedGoods", returnedGoods);
			
			return "modules/ec/returnGoodsCardSuit";
		}else{//通用卡 审核售后
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
								"<td align='center' rowspan='"+num+"'> "+father.getRatio()+"</td> "+
								"<td align='center' rowspan='"+num+"'> "+father.getRatioPrice()+"</td> "+
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
			//计算通用卡可售后次数(查询的剩余次数+此次售后的次数)和售后金额(实付金额-退款金额)
			double amount = 0.0;//实物的可售后金额
			int times = 0;//通用卡的可售后次数
			ReturnedGoods returned = returnedGoodsService.getAmountAndNum(returnedGoods);//获取不包含自己本身的总的售后金额和售后数量
			amount = ((orderGoods.getTotalAmount()*100) - (returned.getReturnAmount()*100))/100;//可售后金额;
			times = result.get(0).get(0).getRemaintimes() + returnedGoods.getReturnNum();//可售后次数
			//查询卡项子项实物的售后数量
			String realGoods = "";
			int remainNum = 0;//售后之后,实物的可售后数量
			int j = 0;
			List<ReturnedGoods> rgList = returnedGoodsService.getRealnum(returnedGoods);
			if(rgList.size() != 0){
				for (ReturnedGoods rg : rgList) {
					int returnNum = returnedGoodsService.getRealReturnNum(rg);//查询除当前售后id之外,该通用卡商品实物子项的售后数量
					remainNum = rg.getGoodsNum()-returnNum;
					realGoods = realGoods + 
						"<label>"+rg.getGoodsName()+"  售后数量：</label><input id='recIds' name='recIds' value='"+rg.getGoodsMappingId()+"' type='hidden'/><input id='returnNums"+j+"' name='returnNums' value='"+rg.getReturnNum()+"' style='width:180px;' class='form-control required' onblur='findReturnNum(this,"+remainNum+")'/><input id='oldReturnNums"+j+"' name='oldReturnNums' value='"+rg.getReturnNum()+"' type='hidden'/><p></p>";
					j++;
				}
				model.addAttribute("realnum",j);
				model.addAttribute("realGoods",realGoods);
			}
			
			model.addAttribute("amount",amount);//通用卡可售后金额
			model.addAttribute("times",times);//通用卡可售后次数
			model.addAttribute("returnGoodsCard",suitCardSons.toString());
			model.addAttribute("returnedGoods", returnedGoods);
			
			return "modules/ec/returnGoodsCardCommon";
		}
	}

	/**
	 * 保存数据(实物)
	 * 
	 * @param returnedGoods
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "saveReturnedKind")
	public String saveReturnedKind(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveReturnedKind(returnedGoods); 
			
			addMessage(redirectAttributes, "保存实物退货订单'" + returnedGoods.getId() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存实物退货订单", e);
			logger.error("方法：saveReturnedKind,保存实物退货订单" + e.getMessage());
			addMessage(redirectAttributes, "保存实物退货订单失败！");
		}

		return "redirect:" + adminPath + "/ec/returned/list";

	}
	/**
	 * 保存数据(虚拟)
	 * 
	 * @param returnedGoods
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	
	@RequestMapping(value = "saveReturned")
	public String saveReturned(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveReturned(returnedGoods); 
			
			addMessage(redirectAttributes, "保存虚拟退货订单'" + returnedGoods.getId() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存虚拟退货订单", e);
			logger.error("方法：save,保存虚拟退货订单" + e.getMessage());
			addMessage(redirectAttributes, "保存虚拟退货订单失败！");
		}
		
		return "redirect:" + adminPath + "/ec/returned/list";
		
	}
	/**
	 * 保存数据(通用卡)
	 * 
	 * @param returnedGoods
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	
	@RequestMapping(value = "saveReturnedCommon")
	public String saveReturnedCommon(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveReturnedCommon(returnedGoods); 
			
			addMessage(redirectAttributes, "保存通用卡售后审核订单'" + returnedGoods.getId() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存通用卡售后审核订单", e);
			logger.error("方法：save,保存通用卡售后审核订单" + e.getMessage());
			addMessage(redirectAttributes, "保存通用卡售后审核失败！");
		}
		
		return "redirect:" + adminPath + "/ec/returned/list";
		
	}
	/**
	 * 保存数据(套卡)
	 * 
	 * @param returnedGoods
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	
	@RequestMapping(value = "saveReturnedSuit")
	public String saveReturnedSuit(ReturnedGoods returnedGoods, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try {
			
			returnedGoodsService.saveEditeSuit(returnedGoods); 
			
			addMessage(redirectAttributes, "保存虚拟退货订单'" + returnedGoods.getId() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存虚拟退货订单", e);
			logger.error("方法：save,保存虚拟退货订单" + e.getMessage());
			addMessage(redirectAttributes, "保存虚拟退货订单失败！");
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
			String currentUser = UserUtils.getUser().getId();
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
			String currentUser = UserUtils.getUser().getId();
			returnedGoods.setShipperBy(currentUser);
			returnedGoods.setReturnStatus("26");
			returnedGoodsService.UpdateShipping(returnedGoods);
			//换货时,扣减次数是在detail表和mapping表中中,所以在换货完成之后,要退还次数.
			returnedGoodsService.refuseService(returnedGoods);//退还申请扣减的数据(detail表和mapping表)
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
			String currentUser = UserUtils.getUser().getId();
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
		try {
			//获取售后信息
			TurnOverDetails turnOverDetails = returnedGoodsService.getTurnover(returnedGoods);
			//获取售后审核时间(老数据没审核时间,用申请时间代替)
			Date createDate = turnOverDetails.getCreateDate();//获取售后审核时间(老数据没审核时间,用申请时间代替)
			double returnAmount = turnOverDetails.getAmount();//退款金额
			String pushmoneyTurnover = "";//业务员jsp展示
			String shopTurnover = "";//店铺jsp展示
			
			//获取业务员营业额信息
			List<OrderPushmoneyRecord> userList = returnedGoodsService.getOrderPushmoneyRecordListView(turnOverDetails);//获取业务员信息集合
			int userNum = userList.size();//查询到数据的个数
			if(userList.size() > 0){//当存在数据时
				pushmoneyTurnover = pushmoneyTurnover + 
					"<tr style='text-align: center;' > "+
						"<td rowspan='"+userNum+"'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
						"<td rowspan='"+userNum+"'>售后</td> "+
						"<td rowspan='"+userNum+"'>"+returnAmount+"</td> "+
						"<td style='text-align: center;'>"+userList.get(0).getPushmoneyUserName()+"</td> "+
						"<td style='text-align: center;'>"+userList.get(0).getDepartmentName()+"</td> "+
						"<td style='text-align: center;'>"+userList.get(0).getPushmoneyUserMobile()+"</td> "+
						"<td style='text-align: center;'>"+userList.get(0).getPushMoney()+"</td> "+
						"<td style='text-align: center;' rowspan='"+userNum+"'>"+
							"<a href='#' onclick='editOrderPushmoneyRecord(\""+turnOverDetails.getOrderId()+"\",\""+turnOverDetails.getDetailsId()+"\")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>"+
							"<a href=\"#\" onclick=\"openDialogView('查看日志记录', '/kenuo/a/ec/returned/getReturnedBeauticianLog?orderId="+turnOverDetails.getOrderId()+"&returnedId="+turnOverDetails.getDetailsId()+"','800px','600px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i> 日志记录</a>"+
						"</td> "+
					"</tr>";
				for (int i = 1; i < userList.size(); i++) {
					pushmoneyTurnover = pushmoneyTurnover + 
							"<tr style='text-align: center;' > "+
								"<td style='text-align: center;'>"+userList.get(i).getPushmoneyUserName()+"</td> "+
								"<td style='text-align: center;'>"+userList.get(i).getDepartmentName()+"</td> "+
								"<td style='text-align: center;'>"+userList.get(i).getPushmoneyUserMobile()+"</td> "+
								"<td style='text-align: center;'>"+userList.get(i).getPushMoney()+"</td> "+
							"</tr>";
				}
			}else{//没有业务员营业额
				pushmoneyTurnover = pushmoneyTurnover + 
						"<tr style='text-align: center;' > "+
							"<td>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
							"<td>售后</td> "+
							"<td>"+returnAmount+"</td> "+
							"<td></td> "+
							"<td></td> "+
							"<td></td> "+
							"<td></td> "+
							"<td style='text-align: center;'>"+
								"<a href='#' onclick='editOrderPushmoneyRecord(\""+turnOverDetails.getOrderId()+"\",\""+turnOverDetails.getDetailsId()+"\")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>"+
								"<a href=\"#\" onclick=\"openDialogView('查看日志记录', '/kenuo/a/ec/returned/getReturnedBeauticianLog?orderId="+turnOverDetails.getOrderId()+"&returnedId="+turnOverDetails.getDetailsId()+"','800px','600px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i> 日志记录</a>"+
							"</td> "+
						"</tr>";
			}
			//获取店铺营业额信息
			List<TurnOverDetails> officelist = returnedGoodsService.getMtmyTurnoverDetailsListView(turnOverDetails);
			if(officelist.size() > 0){//当存在数据时
				int shopNum = officelist.size();//查询到数据的个数
				shopTurnover = shopTurnover + 
						"<tr style='text-align: center;'> "+
							"<td rowspan='"+shopNum+"'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
							"<td rowspan='"+shopNum+"'>售后</td> "+
							"<td rowspan='"+shopNum+"'>"+returnAmount+"</td> "+
							"<td style='text-align: center;'>"+officelist.get(0).getBelongOfficeName()+"</td> "+
							"<td style='text-align: center;'>"+officelist.get(0).getAmount()+"</td> "+
							"<td style='text-align: center;' rowspan='"+shopNum+"'>"+
								"<a href='#' onclick='editMtmyTurnoverDetails(\""+turnOverDetails.getOrderId()+"\",\""+turnOverDetails.getDetailsId()+"\")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>"+
								"<a href=\"#\" onclick=\"openDialogView('查看日志记录', '/kenuo/a/ec/returned/findMtmyTurnoverDetailsList?orderId="+turnOverDetails.getOrderId()+"&detailsId="+turnOverDetails.getDetailsId()+"','800px','600px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i> 操作日志</a>"+
							"</td> "+
						"</tr>";
				for (int i = 1; i < officelist.size(); i++) {
					shopTurnover = shopTurnover + 
							"<tr style='text-align: center;'> "+
								"<td style='text-align: center;'>"+officelist.get(i).getBelongOfficeName()+"</td> "+
								"<td style='text-align: center;'>"+officelist.get(i).getAmount()+"</td> "+
							"</tr>";
				}
				
			}else{//没有店铺营业额
				shopTurnover = shopTurnover + 
						"<tr style='text-align: center;'> "+
							"<td style='text-align: center;'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
							"<td style='text-align: center;'>售后</td> "+
							"<td style='text-align: center;'>"+returnAmount+"</td> "+
							"<td style='text-align: center;'></td> "+
							"<td style='text-align: center;'></td> "+
							"<td style='text-align: center;'>"+
								"<a href='#' onclick='editMtmyTurnoverDetails(\""+turnOverDetails.getOrderId()+"\",\""+turnOverDetails.getDetailsId()+"\")'  class='btn btn-success btn-xs' ><i class='fa fa-edit'></i>编辑</a>"+
								"<a href=\"#\" onclick=\"openDialogView('查看日志记录', '/kenuo/a/ec/returned/findMtmyTurnoverDetailsList?orderId="+turnOverDetails.getOrderId()+"&detailsId="+turnOverDetails.getDetailsId()+"','800px','600px')\" class='btn btn-info btn-xs' ><i class='fa fa-search-plus'></i> 操作日志</a>"+
							"</td> "+
						"</tr>";
			}
			model.addAttribute("pushmoneyTurnover", pushmoneyTurnover);
			model.addAttribute("shopTurnover", shopTurnover);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/ec/TurnoverDetails";
	}
	/**
	 * 获取业务员退货的营业额操作日志
	 * @param returnGoods
	 * @param redirectAttributes
	 * @return  
	 */
	@RequestMapping(value = "getReturnedBeauticianLog")
	public String getReturnedBeauticianLog(OrderPushmoneyRecord orderPushmoneyRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
		Page<OrderPushmoneyRecord> page = returnedGoodsService.getReturnedBeauticianLog(new Page<OrderPushmoneyRecord>(request, response),orderPushmoneyRecord);
		model.addAttribute("page", page);
		return "modules/ec/beauticianTurnoverList";
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
			List<OrderPushmoneyRecord> pushmoneyList = returnedGoodsService.getReturnedPushmoneyList(turnOverDetails);//在该售后ID查询每个业务员的售后审核扣减的营业额
			
			int num = 0;//获取编辑界面的数字
			for (OrderPushmoneyRecord op : list) {
				num += op.getChildren().size(); 
			}
			int j = num;//校验营业额+增减值>=0
			double returnAmount = turnOverDetails.getAmount();//退款金额
			double beauticianTurnover= 0;//单个的（美容师营业额-该美容师已退营业额）
			double sumTurnover= 0;//合计的(美容师合计营业额-所有美容师已退营业额)
			double turnoverRatio= 0;//营业额占比
			double added = 0;//数据库查询的原始营业额
			String departmentIds = "";//所有部门的id字符串
			
			//之前的老订单会存在售后没有审核时间,由申请时间替代
			Date createDate = turnOverDetails.getCreateDate();
			//获取各个部门的营业额合计
			List<OrderPushmoneyRecord> sumTurnoverList = returnedGoodsService.getSumBeauticianTurnover(turnOverDetails.getOrderId());
			for (OrderPushmoneyRecord opr : sumTurnoverList) {//循环所有部门的营业总额
				
				if(list.get(0).getDepartmentId() == opr.getDepartmentId()){//判断第一条信息属于哪个部门
					if(pushmoneyList.size() !=0){//判断在该退货id中'已经扣减的营业额',不存在'赋值为0'
						added = pushmoneyList.get(0).getPushMoney();//按部门和用户id区分的每个业务员的当前营业额
					}
					beauticianTurnover = list.get(0).getPushMoney();//单个业务员的sum营业额
					sumTurnover = opr.getPushMoney();//部门的营业额
					if(sumTurnover != 0 ){
						turnoverRatio = Double.parseDouble(formater.format(beauticianTurnover/sumTurnover*returnAmount));//占比
					}
					userTurnover = userTurnover + 
						"<input id='pushMoney' name='pushMoney"+list.get(0).getDepartmentId()+"' type='hidden' value='"+added+"' class='form-control'>"+
						"<input id='num' name='num' value='"+num+"' type='hidden' class='form-control'>";
					userTurnover = userTurnover + 
						"<tr style='text-align: center;'> "+
							"<td rowspan='"+num+"'>"+DateUtils.formatDate(createDate, "yyyy-MM-dd HH:mm:ss")+"</td> "+
							"<td rowspan='"+num+"'>售后</td> "+
							"<td rowspan='"+num+"'>"+returnAmount+"</td> "+
							"<td style='text-align: center;' rowspan='"+(list.get(0).getChildren().size())+"'>"+list.get(0).getPushmoneyUserName()+"</td> "+
							"<td style='text-align: center;' rowspan='"+(list.get(0).getChildren().size())+"'>"+list.get(0).getDepartmentName()+"</td> "+
							"<td style='text-align: center;' rowspan='"+(list.get(0).getChildren().size())+"'>"+list.get(0).getPushmoneyUserMobile()+"</td> "+
							"<td style='text-align: center;' rowspan='"+(list.get(0).getChildren().size())+"'>"+added+"</td> "+
							"<td style='text-align: center;' rowspan='"+(list.get(0).getChildren().size())+"'>"+turnoverRatio+"</td> ";
							
					for(int i =0; i<list.get(0).getChildren().size(); i++){
						userTurnover = userTurnover + 
								"<td style='text-align: center;'>"+list.get(0).getChildren().get(i).getBelongOfficeName()+"</td> "+
								"<td style='text-align: center;'>"+list.get(0).getChildren().get(i).getPushMoney()+"</td> "+
								"<td style='text-align: center;'>"+
									"<input id='added"+j+"' name='added"+list.get(0).getDepartmentId()+"' type='hidden' value='"+list.get(0).getChildren().get(i).getPushMoney()+"' class='form-control'>"+
									"<input id='Amount"+j+"' name='Amount"+list.get(0).getDepartmentId()+"' type='number' value='' class='form-control'>"+
								"</td> "+
							"</tr>";
						j--;
					}
				}
				for (int i = 1; i < list.size(); i++) {//循环除第一条之外的业务员营业额,并且比较在哪个部门,计算营业额占比
					if(list.get(i).getDepartmentId() == opr.getDepartmentId()){
						if(pushmoneyList.size() !=0){//判断营业额是否为第一次编辑,为营业额赋值
							added = pushmoneyList.get(i).getPushMoney();//按部门和用户id区分的每个业务员的当前营业额
						}
						sumTurnover = opr.getPushMoney();//部门的营业额
						beauticianTurnover = list.get(i).getPushMoney();//单个业务员营业额
						if(sumTurnover != 0 ){
							turnoverRatio = Double.parseDouble(formater.format(beauticianTurnover/sumTurnover*returnAmount));//占比
						}
						userTurnover = userTurnover + 
							"<input id='pushMoney' name='pushMoney"+list.get(i).getDepartmentId()+"' type='hidden' value='"+added+"' class='form-control'>";
						userTurnover = userTurnover + 
							"<tr style='text-align: center;'> "+
								"<td style='text-align: center;' rowspan='"+(list.get(i).getChildren().size())+"'>"+list.get(i).getPushmoneyUserName()+"</td> "+
								"<td style='text-align: center;' rowspan='"+(list.get(i).getChildren().size())+"'>"+list.get(i).getDepartmentName()+"</td> "+
								"<td style='text-align: center;' rowspan='"+(list.get(i).getChildren().size())+"'>"+list.get(i).getPushmoneyUserMobile()+"</td> "+
								"<td style='text-align: center;' rowspan='"+(list.get(i).getChildren().size())+"'>"+added+"</td> "+
								"<td style='text-align: center;' rowspan='"+(list.get(i).getChildren().size())+"'>"+turnoverRatio+"</td> ";
						for(int k =0; k<list.get(i).getChildren().size(); k++){
							userTurnover = userTurnover + 
									"<td style='text-align: center;'>"+list.get(i).getChildren().get(k).getBelongOfficeName()+"</td> "+
									"<td style='text-align: center;'>"+list.get(i).getChildren().get(k).getPushMoney()+"</td> "+
									"<td style='text-align: center;'>"+
										"<input id='added"+j+"' name='added"+list.get(i).getDepartmentId()+"' type='hidden' value='"+list.get(i).getChildren().get(k).getPushMoney()+"' class='form-control'>"+
										"<input id='Amount"+j+"' name='Amount"+list.get(i).getDepartmentId()+"' type='number' value='' class='form-control'>"+
									"</td> "+
								"</tr>";
							j--;
						}
					}
				}
				//除本次售后外,该部门剩余分享的营业额
				OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
				orderPushmoneyRecord.setOrderId(turnOverDetails.getOrderId());
				orderPushmoneyRecord.setReturnedId(turnOverDetails.getDetailsId());
				orderPushmoneyRecord.setDepartmentId(opr.getDepartmentId());
				double deptPushmoney = returnedGoodsService.getDeptPushmoney(orderPushmoneyRecord);
				userTurnover = userTurnover + 
						"<input id='"+opr.getDepartmentId()+"' name='"+opr.getDepartmentId()+"' type='hidden' value='"+deptPushmoney+"' class='form-control'>";
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
		//根据营业额中订单商品中间表ID获取到商品id
		String goodsId = ordergoodService.getGoodsIdByRecId(turnOverDetails.getMappingId());
		turnOverDetails.setGoodsId(goodsId);
		
		String shopTurnover = "";//jsp界面的页面展示字符串
		//获取店营业额明细列表(sum营业额)
		List<TurnOverDetails> list = returnedGoodsService.getMtmyTurnoverDetailsList(turnOverDetails);
		if(list.size() != 0 ){
			//除本次售后外,各门店剩余分享营业额之和
			double surplusTurnOver = returnedGoodsService.getSurplusTurnover(turnOverDetails);
			shopTurnover = shopTurnover + 
					"<input id='surplusTurnOver' type='hidden' value='"+surplusTurnOver+"' class='form-control'>";
			
			//查询店铺售后  sum增减值
			List<TurnOverDetails> amountList = returnedGoodsService.getReturnedAmountList(turnOverDetails);
			
			double returnAmount = turnOverDetails.getAmount();//退款金额
			//获取营业额占比 = （店铺营业额-店铺已退营业额）/(店铺合计营业额-店铺已退营业额)*退款
			double storeTurnover = 0;//单个店铺当前的营业额（店铺营业额-店铺已退营业额）
			double sumTurnover= 0;//合计的(店铺合计营业额-店铺已退营业额)
			double turnoverRatio= 0;//获取营业额占比
			double added = 0;//数据库查询的原始营业额
			
			//之前的老订单会存在售后没有审核时间,由申请时间替代
			Date createDate = turnOverDetails.getCreateDate();
			storeTurnover = list.get(0).getAmount();//店铺当前的营业额
			for (TurnOverDetails mtd : list) {
				sumTurnover += mtd.getAmount(); //得到店铺营业额合计
			}
			if(sumTurnover != 0 ){
				turnoverRatio = Double.parseDouble(formater.format(storeTurnover/sumTurnover*returnAmount));//店铺营业额占比
			}
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
							"<input id='amount0' name='amount' value='' type='number' class='form-control'>"+
							"<input id='storeTurnover0' value='"+storeTurnover+"' type='hidden' class='form-control'>"+
							"<input id='num' name='num' value='"+num+"' type='hidden' class='form-control'>"+
						"</td> "+
					"</tr>";
			for (int i = 1; i < list.size(); i++) {
				if(amountList.size() != 0){//判断营业额是否为第一次编辑,为营业额赋值
					added = amountList.get(i).getAmount();
				}
				storeTurnover = list.get(i).getAmount();//单个店铺营业额合计之后的金额
				if(sumTurnover != 0 ){
					turnoverRatio = Double.parseDouble(formater.format(storeTurnover/sumTurnover*returnAmount));
				}
				shopTurnover = shopTurnover + 
					"<tr style='text-align: center;'> "+
						"<td style='text-align: center;'>"+list.get(i).getBelongOfficeName()+"</td> "+
						"<td style='text-align: center;'>"+added+"</td> "+
						"<td style='text-align: center;'>"+turnoverRatio+"</td> "+
						"<td style='text-align: center;'>"+
							"<input id='addeds"+i+"' name='addeds' type='hidden' value='"+added+"' class='form-control'>"+
							"<input id='amount"+i+"' name='amount' value='' type='number' class='form-control'>"+
							"<input id='storeTurnover"+i+"' value='"+storeTurnover+"' type='hidden' class='form-control'>"+
						"</td> "+
					"</tr>";
			}
			model.addAttribute("returnAmount",returnAmount);
			model.addAttribute("turnOverDetails",turnOverDetails);
		}
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

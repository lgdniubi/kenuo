package com.training.modules.ec.web;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.PdWareHouseDao;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.PdWareHouse;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
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
			String goodsNum = "";
			int num;
			List<List<OrderGoods>> result = new ArrayList<List<OrderGoods>>();//存放每个卡项商品和它的子项集合
			List<OrderGoods> resultSon = new ArrayList<OrderGoods>();//存放一个卡项商品和它的子项
			
			List<OrderGoods> list=ordergoodService.orderlist(returnedGoods.getOrderId());//根据订单id查询订单中的套卡及其子项
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
							if(isreal.equals("实物")){//通用卡实物存在购买数量,虚拟不存在
								goodsNum = String.valueOf(lists.get(1).getGoodsnum());
							}else{
								goodsNum = "";
							}
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
									"<td align='center'> "+goodsNum+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getOrderAmount()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getTotalAmount()+"</td> "+
									"<td align='center' rowspan='"+num+"'> "+father.getOrderArrearage()+"</td> "+
								"</tr> ";
							for(int i=2;i<lists.size();i++){
								isreal = lists.get(i).getIsreal()==0?"实物":"虚拟";
								if(isreal.equals("实物")){//通用卡实物存在购买数量,虚拟不存在
									goodsNum = String.valueOf(lists.get(i).getGoodsnum());
								}else{
									goodsNum = "";
								}
								suitCardSons = suitCardSons +
									"<tr> "+
										"<td align='center'> "+lists.get(i).getGoodsname()+"</td> "+
										"<td align='center'> "+isreal+"</td> "+
										"<td align='center'> "+goodsNum+"</td> "+
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

}

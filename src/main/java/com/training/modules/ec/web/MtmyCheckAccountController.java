/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.beanvalidator.BeanValidators;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.entity.MtmyCheckAccount;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsPrice;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.CheckAccountService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

/**
 * 每天每夜对账Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyCheck")
public class MtmyCheckAccountController extends BaseController {
	
	@Autowired
	private CheckAccountService checkAccountService;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	
	@RequestMapping(value = "list")
	public String list(MtmyCheckAccount mtmyCheckAccount,HttpServletRequest request, HttpServletResponse response,Model model) {
		
		//标示
		List<MtmyCheckAccount> list = checkAccountService.findGroupFlag();
		model.addAttribute("list", list);
		
		if("reset".equals(request.getParameter("resetNum"))){
			Page<MtmyCheckAccount> page=checkAccountService.findPage(new Page<MtmyCheckAccount>(request, response), mtmyCheckAccount);
			model.addAttribute("page", page);
		}else{
			mtmyCheckAccount.setGroupFlag(list.get(0).getGroupFlag());
			Page<MtmyCheckAccount> page=checkAccountService.findPage(new Page<MtmyCheckAccount>(request, response), mtmyCheckAccount);
			model.addAttribute("page", page);
		}
		
		model.addAttribute("mtmyCheckAccount", mtmyCheckAccount);
		return "modules/ec/mtmyCheckAccount";
	}
	
	
	/**
	 * 导出订单
	 * @param mtmyCheckAccount
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(MtmyCheckAccount mtmyCheckAccount, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "订单数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<MtmyCheckAccount> list=checkAccountService.export(mtmyCheckAccount);
	    	new ExportExcel("订单数据", MtmyCheckAccount.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单数据！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/mtmyCheck/list";
	}
	
	/**
	 * 跳转到导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/ec/importCheckExcel";
	}
	
	/**
	 * 下载ping++数据导入模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "pingImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[ping++数据导入模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[ping++数据导入模板-new-path"+path);
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
		return "redirect:" + adminPath + "/ec/mtmyCheck/list";
	}
	
	/**
	 * 导入ping++数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import")
	public String importPing(MultipartFile file, RedirectAttributes redirectAttributes) {
	
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MtmyCheckAccount> list = ei.getDataList(MtmyCheckAccount.class);
			for (MtmyCheckAccount mtmyCheckAccount : list){
				try{
					BeanValidators.validateWithException(validator, mtmyCheckAccount);
					if(checkAccountService.findByOrderNo(mtmyCheckAccount) == 0){
						checkAccountService.insterPing(mtmyCheckAccount);
						successNum++;
					}else{
						failureMsg.append("<br/>商户订单号 "+mtmyCheckAccount.getOrderNo()+" 导入失败：商户订单号重复！;");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>商户订单号 "+mtmyCheckAccount.getOrderNo()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+";");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>商户订单号 "+mtmyCheckAccount.getOrderNo()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商户订单号，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条商户订单号"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入ping++数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/mtmyCheck/list";
	}
	@RequestMapping(value = "goodslist")
	public String goodslist(HttpServletRequest request, OrderGoodsPrice orderGoodsPrice, String orderid, Model model,RedirectAttributes redirectAttributes){
		double youprice = 0;
		double totlprice = 0;
		try {
			List<OrderGoods> orderlist = orderGoodsDao.findBarCodeByOederId(orderid);
			Orders orders = ordersService.get(orderid);
			for (int i = 0; i < orderlist.size(); i++) {

				double rowprice = orderlist.get(i).getGoodsprice() * orderlist.get(i).getGoodsnum();
				totlprice += rowprice;

			}
			youprice = totlprice - orders.getOrderamount();
			BigDecimal b = new BigDecimal(youprice);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			orderGoodsPrice.setYouprice(f1);
			orderGoodsPrice.setTotlprice(orders.getOrderamount());
			orderGoodsPrice.setName(orders.getName());
			orderGoodsPrice.setAddress(orders.getAddress());
			orderGoodsPrice.setPhone(orders.getMobile());
			orderGoodsPrice.setOrderid(orderid);
			model.addAttribute("orderGoodsPrice", orderGoodsPrice);
			model.addAttribute("orderlist", orderlist);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "对账管理--查看商品详", e);
			logger.error("对账管理--查看商品详情出错：" + e.getMessage());
			addMessage(redirectAttributes, "查询出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyCheckOrder";
	}
}
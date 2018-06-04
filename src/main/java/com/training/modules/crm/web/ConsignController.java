package com.training.modules.crm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.Consign;
import com.training.modules.crm.entity.CrmDepositLog;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.ConsignService;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * kenuo 用户寄存相关controller
 * 
 * @author：sharp @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/consign")
public class ConsignController extends BaseController {

	@Autowired
	private ConsignService consignService;
	@Autowired
	private UserDetailService userDetailService;
	@ModelAttribute
	public UserDetail  get(@RequestParam(required = false) String userId) {
		if (StringUtils.isNotBlank(userId)) {
			UserDetail detail=  userDetailService.getUserNickname(userId); 
		    return detail;
		} else {
			return new UserDetail();
		}
	}
	/**
	 * 查找用户寄存记录
	 * @param userId
	 * @return "modules/crm/consingn"
	 */
	@RequestMapping(value = "list")
	public String consingn(String userId, String franchiseeId, HttpServletRequest request, HttpServletResponse response, Model model) {

		if ("".equals(userId) || userId == null) {
			model.addAttribute("userId", null);
		} else {
			try {
				//consignList
				Consign entity = new Consign();
				entity.setUserId(userId);
				entity.setFranchiseeId(franchiseeId);
				
				Page<Consign> page = consignService.getConsignList(new Page<Consign>(request, response), entity);
				model.addAttribute("page", page);
				model.addAttribute("userId", userId);
				model.addAttribute("franchiseeId", franchiseeId);
			} catch (Exception e) {
				logger.debug("<<<<<<<<<<<<"+e.getMessage());
				e.printStackTrace();
			}
		}
		return "modules/crm/consingn";
	}

	/**
	 * 返回编辑寄存记录页面
	 * @param consign实体类
	 * @return "modules/crm/consignForm"
	 */
	@RequestMapping(value = { "add", })
	public String editConsign(Consign consign, @RequestParam(value = "userId") String userId, @RequestParam(value = "franchiseeId") String franchiseeId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String consignId = consign.getConsignId();
		if (null == consignId || "".equals(consignId)) {
			model.addAttribute("consign", consign);
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		} else {
			consign = consignService.get(consignId);
			model.addAttribute("consign", consign);
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		}
		return "modules/crm/consignForm";
	}

	/**
	 * 返回编辑寄存记录页面
	 * @param consign实体类
	 * @return "modules/crm/consignForm"
	 */
	@RequestMapping(value = {"update" })
	public String updateConsign(Consign consign, @RequestParam(value = "userId") String userId, @RequestParam(value = "franchiseeId") String franchiseeId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String consignId = consign.getConsignId();
		if (null == consignId || "".equals(consignId)) {
			model.addAttribute("userId", userId);
			model.addAttribute("consign", consign);
			model.addAttribute("franchiseeId", franchiseeId);
		} else {
			Consign entity = consignService.get(consignId);
			model.addAttribute("consign", entity);
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		}
		return "modules/crm/consignUpdateForm";
	}
	/**
	 * 保存记录
	 * @param Consign
	 * @return redirect+crm/consignList
	 */
	@RequestMapping(value = "save")
	public String saveConsign(Consign consign, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String consignId ;
		try {
			User user = UserUtils.getUser();
			consignId = consign.getConsignId();
			CrmDepositLog log = new CrmDepositLog();
			if (null==consignId|| consignId.trim().length()<=0) {
				consignService.save(consign);
				//save Log
				log.setProductName(consign.getGoodsName());
				log.setBuyNum(consign.getPurchaseNum());
				log.setTakeNum(consign.getTakenNum());
				log.setSurplusNum(consign.getConsignNum());
				log.setCreateBy(user);
				log.setRemarks("新增寄存档案:"+consign.getGoodsName()+"取走数量:"+consign.getTakenNum());
				consignService.saveCrmDepositLog(log);
			}else {
				consignService.updateSingle(consign);
				//save Log
				log.setProductName(consign.getGoodsName());
				log.setBuyNum(consign.getPurchaseNum());
				log.setTakeNum(consign.getTakenNum());
				log.setSurplusNum(consign.getConsignNum());
				log.setCreateBy(user);
				log.setRemarks("修改寄存档案:"+consign.getGoodsName()+"取走数量:"+consign.getTakenNum());
				consignService.saveCrmDepositLog(log);
			}
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return "redirect:" + adminPath + "/crm/consign/list?userId="+consign.getUserId()+"&franchiseeId="+consign.getFranchiseeId();
	}
	
	/**
	 * 获取资料中的操作日志记录
	 * @param log
	 */
	@RequestMapping("logDetail")
	public String logDetail(CrmDepositLog log, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			// 取得操作日志
			Page<CrmDepositLog> page = consignService.findDepositList(new Page<CrmDepositLog>(request, response), log);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			addMessage(model, "查询出现问题或者超时");
			e.printStackTrace();
		}
		return "modules/crm/depositLog";
	}
}

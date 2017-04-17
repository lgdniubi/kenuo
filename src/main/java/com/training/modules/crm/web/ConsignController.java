package com.training.modules.crm.web;

import java.util.List;

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
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.entity.UserOperatorLog;
import com.training.modules.crm.service.ConsignService;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.crm.service.UserOperatorLogService;

/**
 * kenuo 用户寄存相关controller
 * 
 * @author：sharp @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/consign")
public class ConsignController extends BaseController {

	@Autowired
	private UserOperatorLogService logService;
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
	public String consingn(String userId, HttpServletRequest request, HttpServletResponse response, Model model) {

		if ("".equals(userId) || userId == null) {
			model.addAttribute("userId", null);
		} else {
			try {
				//LogList
				UserOperatorLog log = new UserOperatorLog();
				log.setUserId(userId);
				log.setOperatorType("2");
				List<UserOperatorLog> logList = logService.findList(log);
				//consignList
				Consign entity = new Consign();
				entity.setUserId(userId);
				Page<Consign> page = consignService.getConsignList(new Page<Consign>(request, response), entity);
				model.addAttribute("page", page);
				model.addAttribute("userId", userId);
				model.addAttribute("logList", logList);
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
	public String editConsign(Consign consign, @RequestParam(value = "userId") String userId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String consignId = consign.getConsignId();
		if (null == consignId || "".equals(consignId)) {
			model.addAttribute("userId", userId);
			model.addAttribute("consign", consign);
		} else {
			Consign entity = consignService.get(consignId);
			model.addAttribute("consign", entity);
			model.addAttribute("userId", userId);
		}
		return "modules/crm/consignForm";
	}

	/**
	 * 返回编辑寄存记录页面
	 * @param consign实体类
	 * @return "modules/crm/consignForm"
	 */
	@RequestMapping(value = {"update" })
	public String updateConsign(Consign consign, @RequestParam(value = "userId") String userId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String consignId = consign.getConsignId();
		if (null == consignId || "".equals(consignId)) {
			model.addAttribute("userId", userId);
			model.addAttribute("consign", consign);
		} else {
			Consign entity = consignService.get(consignId);
			model.addAttribute("consign", entity);
			model.addAttribute("userId", userId);
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
			consignId = consign.getConsignId();
			UserOperatorLog log = new UserOperatorLog();
			log.setOperatorType("2");
			if (null==consignId|| consignId.trim().length()<=0) {
				consignService.save(consign);
				//save Log
				log.setUserId(consign.getUserId());
				log.setContent("创建新的寄存档案");
				logService.save(log);
			}else {
				consignService.updateSingle(consign);
				log.setUserId(consign.getUserId());
				log.setContent("修改寄存档案");
				logService.save(log);
			}
			
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return "redirect:" + adminPath + "/crm/consign/list?userId="+consign.getUserId();
	}
}

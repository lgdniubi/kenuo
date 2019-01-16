package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Banner;
import com.training.modules.ec.entity.IntegralsLog;
import com.training.modules.ec.service.BannerService;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.service.FranchiseeService;


	/**
	 * 每天每夜云币Controller
	 * 
	 * @version 2018-11-23
	 */
@Controller
@RequestMapping(value = "${adminPath}/ec/integralsLog")
public class IntegralsLogController extends BaseController {
		
		//@Autowired
	//	private IntegralsLogService integralsLogService;
	
		
		/**
		 * 分页查询
		 * @param banner
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "list")
		public String list(IntegralsLog integralsLog,HttpServletRequest request, HttpServletResponse response,Model model) {
			//查询可选择的商家
			//Franchisee franchisee = new Franchisee();
			//franchisee.setIsRealFranchisee("1");
			//List<Franchisee> franchiseeList = franchiseeService.findList(franchisee);
			
	//		Page<IntegralsLog> page=integralsLogService.findPage(new Page<IntegralsLog>(request, response), integralsLog);
		//	model.addAttribute("page", page);
			//model.addAttribute("franchiseeList", franchiseeList);
			return "modules/ec/integralsLogList";
		}

}

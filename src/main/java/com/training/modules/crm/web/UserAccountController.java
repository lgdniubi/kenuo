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
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.service.ReservationService;

/**
 * kenuo 
 * 账户详情
 * @author：sharp
 * @date：2017年3月8日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/account")
public class UserAccountController extends BaseController {

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private UserDetailService userDetailService;
	
	/**
	 * 默认返回用户信息
	 * @param 
	 * @return UserDetail
	 */
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
	 * 客户账户详情
	 * @param
	 * @return String 
	 */
	@RequestMapping(value = "list")
	public String schedule(String userId, HttpServletRequest request, HttpServletResponse response, Model model) {

		if (null!=userId ||userId.trim().length()>0 ) {
			Reservation reservation = new Reservation();
			reservation.setUserId(Integer.valueOf(userId));
			Page<Reservation> page = reservationService.findUserPage(new Page<Reservation>(request, response), reservation);
			model.addAttribute("userId", userId);
			model.addAttribute("page", page);
		}
		return "modules/crm/schedule";
	}
}

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
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.service.CommentService;
import com.training.modules.ec.service.ReservationService;

/**
 * kenuo 产品使用记录相关
 * 
 * @author：sharp @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/schedule")
public class ScheduleController extends BaseController {

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private CommentService commentService;
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
	 * 客户护理时间表
	 * @param
	 * @return String 
	 */
	@RequestMapping(value = "list")
	public String schedule(String userId, HttpServletRequest request, HttpServletResponse response, Model model) {

		if (null!=userId && userId.trim().length()>0 ) {
			Reservation reservation = new Reservation();
			reservation.setUserId(Integer.valueOf(userId));
			Page<Reservation> page = reservationService.findUserPage(new Page<Reservation>(request, response), reservation);
			model.addAttribute("userId", userId);
			model.addAttribute("page", page);
		}
		return "modules/crm/schedule";
	}
	
	/**
	 * 查找针对美容师的评论
	 * @param 
	 * @return String
	 */
	@RequestMapping(value = "comment")
	public String comment(Comment entity, HttpServletRequest request, HttpServletResponse response, Model model) {

			try {
				List<Comment> comments = commentService.findCommentByUserId(entity);
				model.addAttribute("userId", entity.getUserId());
				model.addAttribute("comment",comments);
			} catch (Exception e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
		return "modules/crm/apptComment";
	}

}

package com.training.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Feedback;
import com.training.modules.sys.service.FeedbackService;

/**
 * 反馈管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/feedback")
public class FeedbackController extends BaseController{
	
	@Autowired
	private FeedbackService feedbackService;
	/**
	 * 查看所有反馈
	 */
	@RequestMapping(value = { "feedback", "" })
	public String feedback(Feedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<Feedback> page=feedbackService.findPage(new Page<Feedback>(request, response),feedback);
		model.addAttribute("page", page);
		if(request.getParameter("content")!=null){
			model.addAttribute("content",request.getParameter("content"));
		}
		return "modules/sys/feedback";
	}
	/**
	 * 修改反馈状态
	 * @param feedback
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions(value={"sys:feedback:update"},logical=Logical.OR)
	@RequestMapping(value = { "update", "" })
	public void OneFeedback(Feedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		//查询单个问题的反馈状态
		Feedback f=feedbackService.get(feedback);
		if(f.getFbStatus()==1){
			feedback.setFbStatus(0);
			feedbackService.updateFbStatus(feedback);
		}else{
			feedback.setFbStatus(1);
			feedbackService.updateFbStatus(feedback);
		}
	}
	@RequestMapping(value = { "feedbackForm", "" })
	public String feedbackForm(Feedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		Feedback f=feedbackService.get(feedback);
		model.addAttribute("feedback",f);
		return "modules/sys/feedbackForm";
	}
}

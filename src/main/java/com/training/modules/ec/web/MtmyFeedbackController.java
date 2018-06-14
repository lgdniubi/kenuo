package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.MtmyFeedback;
import com.training.modules.ec.service.MtmyFeedbackService;

/**
 * 反馈管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyFeedback")
public class MtmyFeedbackController extends BaseController{
	
	@Autowired
	private MtmyFeedbackService feedbackService;
	/**
	 * 查看所有反馈
	 */
	@RequestMapping(value ="feedback")
	public String feedback(MtmyFeedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<MtmyFeedback> page=feedbackService.findPage(new Page<MtmyFeedback>(request, response),feedback);
		model.addAttribute("page", page);
		model.addAttribute("feedback",feedback);
		return "modules/ec/mtmyFeedback";
	}
	/**
	 * 修改反馈状态
	 * @param feedback
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value ="update")
	public void OneFeedback(MtmyFeedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		//查询单个问题的反馈状态
		MtmyFeedback f=feedbackService.get(feedback);
		if(f.getMsgStatus()==1){
			feedback.setMsgStatus(0);
			feedbackService.updateFbStatus(feedback);
		}else{
			feedback.setMsgStatus(1);
			feedbackService.updateFbStatus(feedback);
		}
	}
	/**
	 * 查询问题详情
	 * @param feedback
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="feedbackForm")
	public String feedbackForm(MtmyFeedback feedback,HttpServletRequest request, HttpServletResponse response, Model model){
		MtmyFeedback f=feedbackService.get(feedback);
		model.addAttribute("feedback",f);
		return "modules/ec/mtmyFeedbackForm";
	}
}

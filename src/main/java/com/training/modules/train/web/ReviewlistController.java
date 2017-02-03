package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.train.entity.LessonAskComments;
import com.training.modules.train.service.ReviewListService;

/**
 * 		评论详情列表
 * @author Superman
 *
 */ 
@Controller
@RequestMapping(value = "${adminPath}/train/reviewlist")
public class ReviewlistController extends BaseController{
	
	@Autowired
	private ReviewListService reviewListService;
	/**
	 * 		评论详情列表
	 */ 
	@RequestMapping(value = { "reviewlist", "" })
	public String reviewlist (LessonAskComments lessonAskComments,String id,HttpServletRequest request, HttpServletResponse response, Model model){
		//若重定向则跑if里面
		if(id!=null){
			lessonAskComments.setAskId(id);
		}
		Page<LessonAskComments> page=reviewListService.findPage(new Page<LessonAskComments>(request, response), lessonAskComments);
		model.addAttribute("page", page);
		model.addAttribute("askId", lessonAskComments.getAskId());
		return "modules/train/reviewlist";
	}
	/**
	 * 删除一条评论
	 * @param lessonAskComments
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:reviewlist:del"},logical=Logical.OR)
	@RequestMapping(value={"deleteOneComment",""})
	public String deleteOneComment(LessonAskComments lessonAskComments,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		//重定向给LessonAskComments赋值
		LessonAskComments Comments=reviewListService.get(lessonAskComments.getCommentId());

		if (StringUtils.isNotBlank(lessonAskComments.getCommentId())){
			reviewListService.deleteOneComment(lessonAskComments);
			addMessage(redirectAttributes, "删除评论成功");
		}
		//   重定向并传值
		return "redirect:" + adminPath + "/train/reviewlist/reviewlist?id="+Comments.getAskId();
	}
	
}

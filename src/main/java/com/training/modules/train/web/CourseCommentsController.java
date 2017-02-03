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
import com.training.modules.train.entity.TrainLessonComments;
import com.training.modules.train.service.TrainLessonCommentsService;
import com.training.modules.train.service.TrainLessonsService;

/**
 * 课程评论管理
 * @author kele
 * 2016年3月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/train/comments")
public class CourseCommentsController extends BaseController{

	@Autowired
	private TrainLessonCommentsService trainLessonCommentsService;
	
	@Autowired
	private TrainLessonsService trainLessonsService;
	
	/**
	 * 课程评论管理-分页展示list数据
	 * @param trainLessonComments
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:comments:listcomments"},logical=Logical.OR)
	@RequestMapping(value = {"listcomments", ""})
	public String listcomments(TrainLessonComments trainLessonComments, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String lessonId = request.getParameter("lessonId");
		trainLessonComments.setTrainLessons(trainLessonsService.get(lessonId));
		
		Page<TrainLessonComments> page = trainLessonCommentsService.find(new Page<TrainLessonComments>(request, response), trainLessonComments);
		model.addAttribute("page", page);
		model.addAttribute("trainLessonComments", trainLessonComments);
		return "modules/train/commentsviews";
	}
	
	/**
	 * 删除方法 
	 * 只是修改status状态
	 * @param trainLessons
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:comments:deletecomments"},logical=Logical.OR)
	@RequestMapping(value = {"deletecomments", ""})
	public String deletecomments(TrainLessonComments trainLessonComments, RedirectAttributes redirectAttributes){
		if (StringUtils.isNotBlank(trainLessonComments.getCommentId())){
			trainLessonCommentsService.deleteCommentsForUpdate(trainLessonComments);
			addMessage(redirectAttributes, "删除课程评论成功");
		}
		return "redirect:" + adminPath + "/train/comments/listcomments";
	}
	
	/**
	 * 批量删除方法 
	 * 循环修改status状态
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:comments:batchdeletecomments"},logical=Logical.OR)
	@RequestMapping(value = {"batchdeletecomments", ""})
	public String batchdeletecomments(String[] ids, RedirectAttributes redirectAttributes) {
		if(null != ids && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
				TrainLessonComments trainLessonComments = new TrainLessonComments(ids[i]);
	    		trainLessonCommentsService.deleteCommentsForUpdate(trainLessonComments);
	    	}
	    	addMessage(redirectAttributes, "批量删除课程评论成功");
		}
		return "redirect:" + adminPath + "/train/comments/listcomments";
	}
}

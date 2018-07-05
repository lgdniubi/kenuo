package com.training.modules.train.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.LessonAskComments;
import com.training.modules.train.entity.LessonAskContent;
import com.training.modules.train.entity.LessonAsks;
import com.training.modules.train.service.FaqlistService;

/**
 *   问答列表
 * @author Superman
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/faqlist")
public class FaqlistController extends BaseController{
	
	@Autowired
	private FaqlistService faqlistService;
	/**
	 * 	问答列表
	 */
	@RequestMapping(value = { "faqlist", "" })
	public String faqlist(LessonAsks lessonAsks,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<LessonAsks> page=faqlistService.findPage(new Page<LessonAsks>(request, response),lessonAsks);
		model.addAttribute("page", page);
		if(lessonAsks.getTitle()!=null){
			model.addAttribute("title",lessonAsks.getTitle());
		}
		return "modules/train/faqlist";
	}
	
	/**
	 * 快速回复
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:faqlist:add"},logical=Logical.OR)
	@RequestMapping(value = { "comment", "" })
	public String comment(LessonAskComments lessonAskComments,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		//获取当前登录用户
		try {
//			String id=IdGen.uuid();
			User currentUser = UserUtils.getUser();
//			lessonAskComments.setCommentId(id);
			lessonAskComments.setStatus(0);
			lessonAskComments.setUserId(currentUser.getId());
//		System.out.println(lessonAskComments.getAskId()+"   "+lessonAskComments.getUserId()+"    "+lessonAskComments.getContent()+"   "+lessonAskComments.getStatus());
			faqlistService.addcomment(lessonAskComments);
			addMessage(redirectAttributes, "快速回复成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "回复出错");
			e.printStackTrace();
		}
		return "redirect:" + adminPath + "/train/faqlist/faqlist";
	}
	/**
	 * 删除单个问题
	 * @param lessonAsks
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions(value={"train:faqlist:del"},logical=Logical.OR)
	@RequestMapping(value = { "deleteOneAsk", "" })
	public String deleteOneAsk(LessonAsks lessonAsks,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		if (StringUtils.isNotBlank(lessonAsks.getAskId())){
			faqlistService.deleteOneAsk1(lessonAsks.getAskId());
			faqlistService.deleteOneAsk2(lessonAsks.getAskId());
			faqlistService.deleteOneAsk3(lessonAsks.getAskId());
			addMessage(redirectAttributes, "删除问题成功");
		}
		//   重定向
		return "redirect:" + adminPath + "/train/faqlist/faqlist";
		
	}
	/**
	 * 批量删除问题
	 * @param ids
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:faqlist:del"},logical=Logical.OR)
	@RequestMapping(value = { "deleteAll", "" })
	public String deleteAll(String ids,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		System.out.println(ids);
		String idArray[] =ids.split(",");
		for(String id : idArray){
			faqlistService.deleteOneAsk1(id);
			faqlistService.deleteOneAsk2(id);
			faqlistService.deleteOneAsk3(id);
		}
		addMessage(redirectAttributes, "删除问题成功");
		//   重定向
		return "redirect:" + adminPath + "/train/faqlist/faqlist";
	}

	/**
	 * 		问答详情
	 */ 
	@RequestMapping(value = { "faqdetail", "" })
	public String faqdetail(LessonAskContent lessonAskContent,HttpServletRequest request, HttpServletResponse response, Model model){
		lessonAskContent=faqlistService.get(lessonAskContent);
		if(lessonAskContent.getAskType()!=1){
			List<LessonAskContent> list = faqlistService.findContentList(lessonAskContent);
			model.addAttribute("contentList", list);
		}
		model.addAttribute("lessonAskContent", lessonAskContent);
		return "modules/train/faqdetail";
	}
	
	/**
	 * 更新是否置顶
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"updateistop"})
	public @ResponseBody Map<String, String> updateIsTop(HttpServletRequest request){
		//商品属性-是否检索
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("id");
			String istop = request.getParameter("istop");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(istop)){
				LessonAsks lessonAsks = new LessonAsks();
				lessonAsks.setAskId(id);
				lessonAsks.setIsTop(istop);
				faqlistService.updateIsTop(lessonAsks);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISTOP", istop);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("商品属性-是否检索 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}

/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.HandbookType;
import com.training.modules.train.entity.Question;
import com.training.modules.train.service.HandbookTypeService;
import com.training.modules.train.service.QuestionService;

/**
 * 手册管理
 * @author: jingfeng
 * @date 2018年8月15日下午2:40:37
 */
@Controller
@RequestMapping(value = "${adminPath}/train/question")
public class QuestionController extends BaseController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private HandbookTypeService handbookService;
	
	
	/**
	 * 展示妃子校类型的手册
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list/{bookType}")
	public String fzxList(Question question, @PathVariable("bookType")String bookType, Model model,HttpServletRequest request, HttpServletResponse response) {
		/*List<Question> questionList = null;
		if("1".equals(bookType)){
			questionList = questionService.findList(new Question("1"));
			model.addAttribute("type", 1);
		}else if("2".equals(bookType)){
			questionList = questionService.findList(new Question("2"));
			model.addAttribute("type", 2);
		}else if("3".equals(bookType)){
			questionList = questionService.findList(new Question("3"));
			model.addAttribute("type", 3);
		}*/
		question.setType(bookType);
		Page<Question> page = questionService.findList(new Page<Question>(request, response), question);	
		model.addAttribute("type", bookType);
		model.addAttribute("page", page);
		List<HandbookType> typeList = handbookService.findList(new HandbookType(bookType,"0"));
		if("3".equals(bookType)){
			List<HandbookType> typeShopList = handbookService.findList(new HandbookType(bookType,"1"));
			model.addAttribute("typeShopList", typeShopList);
		}
			
		model.addAttribute("typeList", typeList);
		return "modules/train/questionList";
	}
	@RequestMapping(value = "shopList")
	public String shopList(Question question, Model model,HttpServletRequest request, HttpServletResponse response) {
		String bookType= "3";
		question.setType(bookType);
		Page<Question> page = questionService.findShopList(new Page<Question>(request, response), question);	
		model.addAttribute("type", bookType);
		model.addAttribute("page", page);
		List<HandbookType> typeList = handbookService.findList(new HandbookType(bookType,"0"));
		if("3".equals(bookType)){
			List<HandbookType> typeShopList = handbookService.findList(new HandbookType(bookType,"1"));
			model.addAttribute("typeShopList", typeShopList);
		}
		
		model.addAttribute("typeList", typeList);
		return "modules/train/questionList";
	}
	
	@RequestMapping(value = "form")
	public String form( Question question,String type,Model model) {
		if(question.getId() != null){
			question = questionService.getQuestion(question,type);
			
			String htmlEscape = HtmlUtils.htmlEscape(question.getContent());
			question.setContent(htmlEscape);
		}
		model.addAttribute("type", type);
		List<HandbookType> typeList = handbookService.findList(new HandbookType(type,"0"));
		if("3".equals(type)){
			List<HandbookType> typeShopList = handbookService.findList(new HandbookType(type,"1"));
			model.addAttribute("typeShopList", typeShopList);
		}
			
		model.addAttribute("typeList", typeList);
		model.addAttribute("question", question);
		return "modules/train/questionForm";
	}
	/**
	 * 保存手册
	 * @param question手册
	 * @param listType 列表类型，fzxList,meidaList,pcList
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Question question,String listType,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{	
			
			String htmlEscape = HtmlUtils.htmlUnescape(question.getContent());
			question.setContent(htmlEscape);
			questionService.saveQuestion(question,question.getType());
			addMessage(redirectAttributes, "保存/修改成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存手册", e);
			logger.error("保存手册错误信息:"+e);
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/question/list/"+question.getType();
	}
	@RequestMapping(value = "delete")
	public String delete(Model model,Question question,String listType,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String msg = "删除成功!";
			questionService.deleteQandType(question);
				
			addMessage(redirectAttributes, msg);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除手册", e);
			logger.error("删除手册错误信息:"+e);
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/question/list/"+listType;
	}
	
	/**
	 * 修改手册状态，1启用，2停用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateIsOpen")
	public Map<String, String> updateIsOpen(String qId,String isOpen){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			int ISOPEN = Integer.parseInt(isOpen);
			if(!StringUtils.isEmpty(qId) && (ISOPEN == 2 || ISOPEN == 1)){
				Question question = new Question();
				question.setId(qId);
				question.setStatus(isOpen);
				questionService.updateIsOpen(question);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISOPEN", isOpen);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("课程管理-修改课程状态 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}

 
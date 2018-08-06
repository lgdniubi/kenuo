/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.HandbookType;
import com.training.modules.train.service.HandbookTypeService;

/**
 * 协议模板--供应链
 * @author: jingfeng
 * @date 2018年5月15日下午2:40:37
 */
@Controller
@RequestMapping(value = "${adminPath}/train/handbook")
public class HandbookTypeController extends BaseController {

	@Autowired
	private HandbookTypeService handbookService;
	
	
	/**
	 * 3种分类的列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list( Model model) {
		List<HandbookType> typeList = handbookService.findTypeList();
		model.addAttribute("typeList", typeList);
		return "modules/train/HandbookTypeList";
	}
	/**
	 * 添加/修改
	 * @param handbookType
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form( HandbookType handbookType,Model model) {
		if(handbookType.getId() != null){
			handbookType = handbookService.get(handbookType.getId());
		}
		model.addAttribute("handbookType", handbookType);
		return "modules/train/handbookTypeForm";
	}
	/**
	 * 保存
	 * @param handbookType
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(HandbookType handbookType,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{	
			handbookService.save(handbookType);
			addMessage(redirectAttributes, "保存/修改成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存手册类型", e);
			logger.error("保存手册类型错误信息:"+e);
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/handbook/list";
	}
	/**
	 * 判断是否可以删除类型
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(Model model,HandbookType handbookType,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Integer count = handbookService.findQuestionList(handbookType.getId());	// 查询问题列表，有问题不能删除类型
			String msg = "删除分类成功!";
			if(count !=null && count>0){
				msg = "分类下有问题不能删除";
			}else{
				handbookService.delete(handbookType);
			}
				
			addMessage(redirectAttributes, msg);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除分类", e);
			logger.error("删除分类错误信息:"+e);
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/handbook/list";
	}
}

 
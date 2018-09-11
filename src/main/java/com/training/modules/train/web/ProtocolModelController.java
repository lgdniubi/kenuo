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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ProtocolModel;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.TrainLessons;
import com.training.modules.train.service.ProtocolModelService;

/**
 * 协议模板--供应链
 * @author: jingfeng
 * @date 2018年5月15日下午2:40:37
 */
@Controller
@RequestMapping(value = "${adminPath}/train/protocolModel")
public class ProtocolModelController extends BaseController {

	@Autowired
	private ProtocolModelService protocolModelService;
	
	
	/**
	 * 展示4中协议类型
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modelType")
	public String modelType( Model model) {
		List<ProtocolType> typeList = protocolModelService.findModelTypeList();
		model.addAttribute("typeList", typeList);
		return "modules/train/protocolTypeList";
	}
	
	/**
	 * 查询某一个类型的协议列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String modelList( Model model,String type) {
		List<ProtocolModel> modelList = protocolModelService.findModelList(type);
		model.addAttribute("modelList", modelList);
		return "modules/train/protocolModelList";
	}
	
	/**
	 * 添加协议弹出页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modelForm")
	public String modelForm( ProtocolModel protocolModel,Model model) {
		if(protocolModel.getId() != null){
			protocolModel = protocolModelService.findModel(protocolModel);
			String htmlEscape = HtmlUtils.htmlUnescape(protocolModel.getContent());
			protocolModel.setContent(htmlEscape);
		}
		model.addAttribute("protocolModel", protocolModel);
//		model.addAttribute("type", protocolModel.getType());
		return "modules/train/protocolModelForm";
	}
	
	/**
	 * 查询旧版本集合
	 * @param protocolModel
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "oldModelList")
	public String oldModelList( ProtocolModel protocolModel,Model model) {
		List<ProtocolModel> modelList = protocolModelService.findOldModelList(protocolModel.getId());
		model.addAttribute("modelList", modelList);
		model.addAttribute("isShow", 1);
		return "modules/train/protocolModelList";
	}
	
	/**
	 * 保存协议模板内容
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(ProtocolModel protocolModel,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			//保存数据的时候需要转码
			String htmlEscape = HtmlUtils.htmlEscape(protocolModel.getContent());
			protocolModel.setContent(htmlEscape);
			protocolModelService.saveProtocolModel(protocolModel);
			addMessage(redirectAttributes, "保存协议内容成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存协议内容", e);
			logger.error("保存协议内容错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/protocolModel/modelType";
	}
	
	/**
	 * 更改单个协议状态启用--停用
	 * @param protocolModel
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "status")
	public String status(ProtocolModel protocolModel,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			//
			protocolModelService.updateStatus(protocolModel);
			addMessage(redirectAttributes, "状态更改成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "状态更改", e);
			logger.error("状态更改错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/protocolModel/list?&type="+protocolModel.getType();
	}
	/**
	 * 模板类型的启用停用
	 * @param protocolModel
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "statusType")
	public String statusType(ProtocolModel protocolModel,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			//
			protocolModelService.updateStatusType(protocolModel);
			addMessage(redirectAttributes, "状态更改成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "状态更改", e);
			logger.error("状态更改错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/protocolModel/modelType";
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteProtocolShop")
	public String deleteProtocolShop(ProtocolModel protocolModel){
		protocolModelService.deleteProtocolShop(protocolModel);
		return "1";
	}
	@ResponseBody
	@RequestMapping(value = "updateIsOpen")
	public Map<String, String> updateIsOpen(String modelId,String isOpen){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			int ISOPEN = Integer.parseInt(isOpen);
			if(!StringUtils.isEmpty(modelId) && (ISOPEN == 3 || ISOPEN == 1)){
				ProtocolModel protocolModel = new ProtocolModel();
				protocolModel.setId(modelId);
				protocolModel.setStatus(isOpen);
				protocolModelService.updateIsOpen(protocolModel);
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
	/**
	 * 是否启用弹出页面
	 * @return
	 */
	@RequestMapping(value = "openProtocol")
	public String openProtocol(Integer isOpen,Model model){
		String msg = "协议启用是否重新签订？";
		if(isOpen == 3){
			msg="是否确定停用该协议？";
		}
		model.addAttribute("msg", msg);
		return "modules/train/openProtocol";
	}
	
}

 
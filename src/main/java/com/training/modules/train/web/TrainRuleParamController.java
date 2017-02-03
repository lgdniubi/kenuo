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
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.entity.TrainRuleParamType;
import com.training.modules.train.service.TrainRuleParamService;

/**
 * 培训规则参数Controller
 * @author kele
 * @version 2016-8-12
 */
@Controller
@RequestMapping(value = "${adminPath}/train/ruleparam")
public class TrainRuleParamController extends BaseController{
	
	@Autowired
	private TrainRuleParamService trainRuleParamService;
	
	/**
	 * 培训规则参数-分页展示list数据
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:ruleparam:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(TrainRuleParam trainRuleParam, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			
			//分页查询
			Page<TrainRuleParam> page = trainRuleParamService.find(new Page<TrainRuleParam>(request, response), trainRuleParam);
			model.addAttribute("page", page);
			
			//规则参数分类
			trainRuleParam.setRuleparamtypelist(trainRuleParamService.findParamTypeList());
			model.addAttribute("trainRuleParam", trainRuleParam);
			
		} catch (Exception e) {
			logger.error("#####[培训规则参数-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "培训规则参数-分页展示数据", e);
			model.addAttribute("message", "培训规则参数-查询-出现异常，请与管理员联系");
		}
		return "modules/train/trainruleparamList";
	}
	
	/**
	 * 培训规则参数-查看/添加/编辑
	 * @param trainRuleParam
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "train:ruleparam:view", "train:ruleparam:add", "train:ruleparam:edit" }, logical = Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(TrainRuleParam trainRuleParam, HttpServletRequest request, Model model){
		try {
			//当时id不为空与不为""时,查看
			if(!StringUtils.isEmpty(trainRuleParam.getId())){
				trainRuleParam = trainRuleParamService.get(trainRuleParam);
				
				String paramkey = "";//参数key值
				if(null != trainRuleParam.getTrainRuleParamType() && null != trainRuleParam.getTrainRuleParamType().getAliaName()){
					//替换参数别名
					paramkey = trainRuleParam.getParamKey().replace(trainRuleParam.getTrainRuleParamType().getAliaName(), "");
				}else{
					paramkey = trainRuleParam.getParamKey();
				}
				trainRuleParam.setParamKey(paramkey);
				
			}else{
				//id为null或者"" 时，则为添加下级菜单时，code自增
				List<TrainRuleParam> l = trainRuleParamService.findList(trainRuleParam);
				if(l.size()==0){
					trainRuleParam.setSort(trainRuleParam.getSort());
				}else{
					trainRuleParam.setSort(l.get(0).getSort() + 10);
				}
			}
			//规则参数分类
			trainRuleParam.setRuleparamtypelist(trainRuleParamService.findParamTypeList());
			model.addAttribute("trainRuleParam", trainRuleParam);
		} catch (Exception e) {
			logger.error("#####[培训规则参数-查看/添加/编辑-出现异常：]"+e.getMessage());
			model.addAttribute("message", "培训规则参数-查看/添加/编辑，请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则参数-查看/添加/编辑", e);
		}
		return "modules/train/trainruleparamForm";
	}
	
	/**
	 * 根据规则类型id，异步查询规格类型内容
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"getruletype"})
	public @ResponseBody Map<String, String> getruletype(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String ruletypeid = request.getParameter("ruletypeid");
			if(!StringUtils.isEmpty(ruletypeid)){
				TrainRuleParamType trainRuleParamType = new TrainRuleParamType();
				trainRuleParamType.setId(ruletypeid);
				trainRuleParamType = trainRuleParamService.getRuleType(trainRuleParamType);
				
				jsonMap.put("ALIANAME", trainRuleParamType.getAliaName());
				jsonMap.put("STATUS", "OK");
				jsonMap.put("MESSAGE", "添加成功");
				
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("异步查询规格类型内容-出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "异步查询规格类型内容", e);
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 培训规则参数-保存/修改
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"save"})
	public String save(TrainRuleParam trainRuleParam, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			
			String paramkey = "";//参数key值
			String aliaName = trainRuleParam.getTrainRuleParamType().getAliaName();//参数类型别名
			//当别名不为null，并且有值时，进行拼接参数key值
			if(null != aliaName && aliaName.trim().length() > 0){
				paramkey = trainRuleParam.getTrainRuleParamType().getAliaName()+trainRuleParam.getParamKey();
			}else{
				paramkey = trainRuleParam.getParamKey();
			}
			
			trainRuleParam.setParamKey(paramkey);
			trainRuleParamService.save(trainRuleParam);
			addMessage(redirectAttributes, "培训规则参数-保存/修改-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则参数-保存/修改-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "培训规则参数-保存/修改", e);
			addMessage(redirectAttributes, "培训规则参数-保存/修改-出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/ruleparam/list";
	}
	
	/**
	 * 培训规则参数-删除
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:ruleparam:del"},logical=Logical.OR)
	@RequestMapping(value = {"del"})
	public String delete(TrainRuleParam trainRuleParam, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			trainRuleParamService.delete(trainRuleParam);
			addMessage(redirectAttributes, "删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则参数-删除-出现异常：]"+e.getMessage());
			model.addAttribute("message", "培训规则参数-删除-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则参数-删除", e);
		}
		return "redirect:" + adminPath + "/train/ruleparam/list";
	}
	
	/**
	 * 培训规则参数-批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("train:ruleparam:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				TrainRuleParam tsp = new TrainRuleParam();
				tsp.setId(id);
				trainRuleParamService.delete(tsp);
			}
			addMessage(redirectAttributes, "批量删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则参数-批量删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "培训规则参数-批量删除-出现异常,请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则参数-批量删除", e);
		}
		return "redirect:" + adminPath + "/train/ruleparam/list";
	}
	
	/**
	 * 培训规则参数类型-列表
	 * @param trainRuleParamType
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:ruletype:typelist"},logical=Logical.OR)
	@RequestMapping(value = {"typelist"})
	public String typelist(TrainRuleParamType trainRuleParamType, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			//分页查询
			Page<TrainRuleParamType> page = trainRuleParamService.findRuleTypeList(new Page<TrainRuleParamType>(request, response), trainRuleParamType);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[培训规则参数分类-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "培训规则参数分类-分页展示数据", e);
			model.addAttribute("message", "培训规则参数分类-查询-出现异常，请与管理员联系");
		}
		return "modules/train/trainruleparamTypeList";
	}
	
	/**
	 * 培训规则参数类型-查看/添加/编辑
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "train:ruletype:typeview", "train:ruletype:typeadd", "train:ruletype:typeedit" }, logical = Logical.OR)
	@RequestMapping(value = {"typeform"})
	public String typeform(TrainRuleParamType trainRuleParamType, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			//当时id不为空与不为""时,查看
			if(!StringUtils.isEmpty(trainRuleParamType.getId())){
				trainRuleParamType = trainRuleParamService.getRuleType(trainRuleParamType);
			}
			model.addAttribute("trainRuleParamType", trainRuleParamType);
		} catch (Exception e) {
			logger.error("#####[培训规则类型参数-查看/添加/编辑-出现异常：]"+e.getMessage());
			model.addAttribute("message", "培训规则类型参数-查看/添加/编辑-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则类型参数-查看/添加/编辑", e);
		}
		return "modules/train/trainruleparamTypeForm";
	}
	
	/**
	 * 培训规则参数类型-保存
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"typesave"})
	public String typesave(TrainRuleParamType trainRuleParamType, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			trainRuleParamService.saveRuleType(trainRuleParamType);
			addMessage(redirectAttributes, "保存数据-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则类型参数-保存-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "培训规则类型参数-保存-出现异常,请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则类型参数-保存", e);
		}
		return "redirect:" + adminPath + "/train/ruleparam/typelist";
	}
	
	/**
	 * 培训规则参数类型-删除
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "train:ruletype:typedel" }, logical = Logical.OR)
	@RequestMapping(value = {"typedelete"})
	public String typedelete(TrainRuleParamType trainRuleParamType, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			trainRuleParamService.deleteRuleType(trainRuleParamType);
			
			//级联删除规则类型
			TrainRuleParam trainRuleParam = new TrainRuleParam();
			trainRuleParam.setParamType(trainRuleParamType.getId());
			trainRuleParamService.typefordelete(trainRuleParam);
			
			addMessage(redirectAttributes, "删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则类型参数-删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "培训规则类型参数-删除-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则类型参数-删除", e);
		}
		return "redirect:" + adminPath + "/train/ruleparam/typelist";
	}
	
	/**
	 * 培训规则参数-批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:ruletype:typedel" }, logical = Logical.OR)
	@RequestMapping(value = "typedeleteAll")
	public String typedeleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				
				TrainRuleParamType trainRuleParamType = new TrainRuleParamType();
				trainRuleParamType.setId(id);
				trainRuleParamService.deleteRuleType(trainRuleParamType);
				
				//级联删除规则类型
				TrainRuleParam trainRuleParam = new TrainRuleParam();
				trainRuleParam.setParamType(trainRuleParamType.getId());
				trainRuleParamService.typefordelete(trainRuleParam);
			}
			addMessage(redirectAttributes, "批量删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[培训规则类型参数-批量删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "培训规则类型参数-批量删除，请与管理员联系");
			BugLogUtils.saveBugLog(request, "培训规则类型参数-批量删除", e);
		}
		return "redirect:" + adminPath + "/train/ruleparam/typelist";
	}
	
}

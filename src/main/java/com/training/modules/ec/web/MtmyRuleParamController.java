package com.training.modules.ec.web;

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
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmyRuleParamType;
import com.training.modules.ec.service.MtmyRuleParamService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;

/**
 * 电商规则参数Controller
 * @author kele
 * @version 2016-8-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/ruleparam")
public class MtmyRuleParamController extends BaseController{
	
	@Autowired
	private MtmyRuleParamService mtmyRuleParamService;
	
	/**
	 * 电商规则参数-分页展示list数据
	 * @param mtmyRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:ruleparam:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		try {
			//分页查询
			Page<MtmyRuleParam> page = mtmyRuleParamService.find(new Page<MtmyRuleParam>(request, response), mtmyRuleParam);
			model.addAttribute("page", page);
			
			//规则参数分类
			mtmyRuleParam.setRuleparamtypelist(mtmyRuleParamService.findParamTypeList());
			model.addAttribute("mtmyRuleParam", mtmyRuleParam);
			
		} catch (Exception e) {
			logger.error("#####[电商规则参数-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "电商规则参数-分页展示数据", e);
			model.addAttribute("message", "电商规则参数-查询-出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyruleparamList";
	}
	
	/**
	 * 电商规则参数-查看/添加/编辑
	 * @param mtmyRuleParam
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:ruleparam:view", "ec:ruleparam:add", "ec:ruleparam:edit" }, logical = Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, Model model){
		try {
			//当时id不为空与不为""时,查看
			if(!StringUtils.isEmpty(mtmyRuleParam.getId())){
				mtmyRuleParam = mtmyRuleParamService.get(mtmyRuleParam);
				
				String paramkey = "";//参数key值
				if(null != mtmyRuleParam.getMtmyRuleParamType() && null != mtmyRuleParam.getMtmyRuleParamType().getAliaName()){
					//替换参数别名
					paramkey = mtmyRuleParam.getParamKey().replace(mtmyRuleParam.getMtmyRuleParamType().getAliaName(), "");
				}else{
					paramkey = mtmyRuleParam.getParamKey();
				}
				mtmyRuleParam.setParamKey(paramkey);
				
			}else{
				//id为null或者"" 时，则为添加下级菜单时，code自增
				List<MtmyRuleParam> l = mtmyRuleParamService.findList(mtmyRuleParam);
				if(l.size()==0){
					mtmyRuleParam.setSort(mtmyRuleParam.getSort());
				}else{
					mtmyRuleParam.setSort(l.get(0).getSort() + 10);
				}
			}
			//规则参数分类
			mtmyRuleParam.setRuleparamtypelist(mtmyRuleParamService.findParamTypeList());
			model.addAttribute("mtmyRuleParam", mtmyRuleParam);
		} catch (Exception e) {
			logger.error("#####[电商规则参数-查看/添加/编辑-出现异常：]"+e.getMessage());
			model.addAttribute("message", "电商规则参数-查看/添加/编辑，请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则参数-查看/添加/编辑", e);
		}
		return "modules/ec/mtmyruleparamForm";
	}
	
	/**
	 * 根据规则类型id，异步查询规格类型内容
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"getruletype"})
	public @ResponseBody Map<String, String> getruletype(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String ruletypeid = request.getParameter("ruletypeid");
			if(!StringUtils.isEmpty(ruletypeid)){
				MtmyRuleParamType mtmyRuleParamType = new MtmyRuleParamType();
				mtmyRuleParamType.setId(ruletypeid);
				mtmyRuleParamType = mtmyRuleParamService.getRuleType(mtmyRuleParamType);
				
				jsonMap.put("ALIANAME", mtmyRuleParamType.getAliaName());
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
	 * 电商规则参数-保存/修改
	 * @param mtmyRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"save"})
	public String save(MtmyRuleParam mtmyRuleParam, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			 
			String paramkey = "";//参数key值
			String aliaName = mtmyRuleParam.getMtmyRuleParamType().getAliaName();//参数类型别名
			//当别名不为null，并且有值时，进行拼接参数key值
			if(null != aliaName && aliaName.trim().length() > 0){
				paramkey = mtmyRuleParam.getMtmyRuleParamType().getAliaName()+mtmyRuleParam.getParamKey();
			}else{
				paramkey = mtmyRuleParam.getParamKey();
			}
			
			mtmyRuleParam.setParamKey(paramkey);
			mtmyRuleParamService.save(mtmyRuleParam);
			addMessage(redirectAttributes, "电商规则参数-保存/修改-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则参数-保存/修改-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "电商规则参数-保存/修改", e);
			addMessage(redirectAttributes, "电商规则参数-保存/修改-出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/ruleparam/list";
	}
	
	/**
	 * 电商规则参数-删除
	 * @param mtmyRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:ruleparam:del"},logical=Logical.OR)
	@RequestMapping(value = {"del"})
	public String delete(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			mtmyRuleParamService.delete(mtmyRuleParam);
			addMessage(redirectAttributes, "删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则参数-删除-出现异常：]"+e.getMessage());
			model.addAttribute("message", "电商规则参数-删除-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则参数-删除", e);
		}
		return "redirect:" + adminPath + "/ec/ruleparam/list";
	}
	
	/**
	 * 电商规则参数-批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:ruleparam:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				MtmyRuleParam mtmyRuleParam = new MtmyRuleParam();
				mtmyRuleParam.setId(id);
				mtmyRuleParamService.delete(mtmyRuleParam);
			}
			addMessage(redirectAttributes, "批量删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则参数-批量删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "电商规则参数-批量删除-出现异常,请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则参数-批量删除", e);
		}
		return "redirect:" + adminPath + "/ec/ruleparam/list";
	}
	
	/**
	 * 电商规则参数类型-列表
	 * @param mtmyRuleParamType
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:ruletype:typelist"},logical=Logical.OR)
	@RequestMapping(value = {"typelist"})
	public String typelist(MtmyRuleParamType mtmyRuleParamType, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			//分页查询
			Page<MtmyRuleParamType> page = mtmyRuleParamService.findRuleTypeList(new Page<MtmyRuleParamType>(request, response), mtmyRuleParamType);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[电商规则参数分类-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "电商规则参数分类-分页展示数据", e);
			model.addAttribute("message", "电商规则参数分类-查询-出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyruleparamTypeList";
	}
	
	/**
	 * 电商规则参数类型-查看/添加/编辑
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:ruletype:typeview", "ec:ruletype:typeadd", "ec:ruletype:typeedit" }, logical = Logical.OR)
	@RequestMapping(value = {"typeform"})
	public String typeform(MtmyRuleParamType mtmyRuleParamType, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			//当时id不为空与不为""时,查看
			if(!StringUtils.isEmpty(mtmyRuleParamType.getId())){
				mtmyRuleParamType = mtmyRuleParamService.getRuleType(mtmyRuleParamType);
			}
			model.addAttribute("mtmyRuleParamType", mtmyRuleParamType);
		} catch (Exception e) {
			logger.error("#####[电商规则类型参数-查看/添加/编辑-出现异常：]"+e.getMessage());
			model.addAttribute("message", "电商规则类型参数-查看/添加/编辑-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则类型参数-查看/添加/编辑", e);
		}
		return "modules/ec/mtmyruleparamTypeForm";
	}
	
	/**
	 * 电商规则参数类型-保存
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"typesave"})
	public String typesave(MtmyRuleParamType mtmyRuleParamType, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			mtmyRuleParamService.saveRuleType(mtmyRuleParamType);
			addMessage(redirectAttributes, "保存数据-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则类型参数-保存-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "电商规则类型参数-保存-出现异常,请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则类型参数-保存", e);
		}
		return "redirect:" + adminPath + "/ec/ruleparam/typelist";
	}
	
	/**
	 * 电商规则参数类型-删除
	 * @param trainRuleParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:ruletype:typedel" }, logical = Logical.OR)
	@RequestMapping(value = {"typedelete"})
	public String typedelete(MtmyRuleParamType mtmyRuleParamType, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			mtmyRuleParamService.deleteRuleType(mtmyRuleParamType);
			
			//级联删除规则类型
			MtmyRuleParam mtmyRuleParam = new MtmyRuleParam();
			mtmyRuleParam.setParamType(mtmyRuleParamType.getId());
			mtmyRuleParamService.typefordelete(mtmyRuleParam);
			
			addMessage(redirectAttributes, "删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则类型参数-删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "电商规则类型参数-删除-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则类型参数-删除", e);
		}
		return "redirect:" + adminPath + "/ec/ruleparam/typelist";
	}
	
	/**
	 * 电商规则参数-批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:ruletype:typedel" }, logical = Logical.OR)
	@RequestMapping(value = "typedeleteAll")
	public String typedeleteAll(String ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				
				MtmyRuleParamType mtmyRuleParamType = new MtmyRuleParamType();
				mtmyRuleParamType.setId(id);
				mtmyRuleParamService.deleteRuleType(mtmyRuleParamType);
				
				//级联删除规则类型
				MtmyRuleParam mtmyRuleParam = new MtmyRuleParam();
				mtmyRuleParam.setParamType(mtmyRuleParamType.getId());
				mtmyRuleParamService.typefordelete(mtmyRuleParam);
			}
			addMessage(redirectAttributes, "批量删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[电商规则类型参数-批量删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "电商规则类型参数-批量删除，请与管理员联系");
			BugLogUtils.saveBugLog(request, "电商规则类型参数-批量删除", e);
		}
		return "redirect:" + adminPath + "/ec/ruleparam/typelist";
	}
	
	/**
	 * 刷新系统规则参数表
	 */
	@RequiresPermissions(value = { "ec:ruletype:refreshparam" }, logical = Logical.OR)
	@RequestMapping(value = "refreshparam")
	public @ResponseBody Map<String, String> refreshparam(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		ParametersFactory.init();//刷新后台系统规则参数
		jsonMap.put("STATUS", "OK");
		jsonMap.put("MESSAGE", "刷新后台系统规则参数成功");
		return jsonMap;
	}
	
}

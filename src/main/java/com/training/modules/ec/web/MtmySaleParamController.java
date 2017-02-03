package com.training.modules.ec.web;

import java.util.List;

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
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmyRuleParamType;
import com.training.modules.ec.service.MtmyRuleParamService;
import com.training.modules.ec.dao.MtmyRuleParamDao;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 分销规则参数Controller
 * @author kele
 * @version 2016-8-12
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/saleParam")
public class MtmySaleParamController extends BaseController{
	
	@Autowired
	private MtmyRuleParamService mtmyRuleParamService;
	@Autowired
	private MtmyRuleParamDao mtmyRuleParamDao;
	/**
	 * 分销规则参数-分页展示list数据
	 * @param mtmyInviteParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:saleParam:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			//分页查询
			Page<MtmyRuleParam> page = mtmyRuleParamService.findSaleList(new Page<MtmyRuleParam>(request, response), mtmyRuleParam);
			model.addAttribute("page", page);
			
		} catch (Exception e) {
			logger.error("#####[分销规则参数-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销规则参数-分页展示数据", e);
			model.addAttribute("message", "分销规则参数-查询-出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleParamList";
	}
	
	/**
	 * 分销规则参数-查看/添加/编辑
	 * @param mtmyInviteParam
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:saleParam:view", "ec:saleParam:add", "ec:saleParam:edit" }, logical = Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, Model model){
		try {
			//当时id不为空与不为""时,查看
			if(!StringUtils.isEmpty(mtmyRuleParam.getId())){
				mtmyRuleParam = mtmyRuleParamService.get(mtmyRuleParam);
			}else{
				//id为null或者"" 时，则为添加下级菜单时，code自增
				List<MtmyRuleParam> l = mtmyRuleParamDao.findSaleList(mtmyRuleParam);
				if(l.size()==0){
					mtmyRuleParam.setSort(mtmyRuleParam.getSort());
				}else{
					mtmyRuleParam.setSort(l.get(0).getSort() + 10);
				}
			}
			model.addAttribute("mtmyRuleParam", mtmyRuleParam);
		} catch (Exception e) {
			logger.error("#####[分销规则参数-查看/添加/编辑-出现异常：]"+e.getMessage());
			model.addAttribute("message", "分销规则参数-查看/添加/编辑，请与管理员联系");
			BugLogUtils.saveBugLog(request, "分销规则参数-查看/添加/编辑", e);
		}
		return "modules/ec/mtmySaleParamForm";
	}
	
	/**
	 * 分销规则参数-保存/修改
	 * @param mtmyInviteParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"save"})
	public String save(MtmyRuleParam mtmyRuleParam, HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			//获取分销类别id
			MtmyRuleParam m = mtmyRuleParamService.findBySale();
			MtmyRuleParamType mtmyRuleParamType = new MtmyRuleParamType();
			mtmyRuleParamType.setId(m.getId());
			mtmyRuleParam.setMtmyRuleParamType(mtmyRuleParamType);
			
			mtmyRuleParamService.save(mtmyRuleParam);
			addMessage(redirectAttributes, "分销规则参数-保存/修改-成功");
		} catch (Exception e) {
			logger.error("#####[分销规则参数-保存/修改-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销规则参数-保存/修改", e);
			addMessage(redirectAttributes, "分销规则参数-保存/修改-出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/saleParam/list";
	}
	
	/**
	 * 分销规则参数-删除
	 * @param mtmyInviteParam
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:saleParam:del"},logical=Logical.OR)
	@RequestMapping(value = {"del"})
	public String delete(MtmyRuleParam mtmyRuleParam, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			mtmyRuleParamService.delete(mtmyRuleParam);
			addMessage(redirectAttributes, "删除数据-成功");
		} catch (Exception e) {
			logger.error("#####[分销规则参数-删除-出现异常：]"+e.getMessage());
			model.addAttribute("message", "分销规则参数-删除-出现异常，请与管理员联系");
			BugLogUtils.saveBugLog(request, "分销规则参数-删除", e);
		}
		return "redirect:" + adminPath + "/ec/saleParam/list";
	}
	
	/**
	 * 分销规则参数-批量删除
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
			logger.error("#####[分销规则参数-批量删除-出现异常：]"+e.getMessage());
			addMessage(redirectAttributes, "分销规则参数-批量删除-出现异常,请与管理员联系");
			BugLogUtils.saveBugLog(request, "分销规则参数-批量删除", e);
		}
		return "redirect:" + adminPath + "/ec/saleParam/list";
	}
	
}

package com.training.modules.ec.web;

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
import com.training.modules.ec.entity.MtmyReturnGoods;
import com.training.modules.ec.service.MtmyReturnGoodsService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 商品退货期Controller
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/returnGoodsDate")
public class MtmyReturnGoodsController extends BaseController{
	
	@Autowired
	private MtmyReturnGoodsService mtmyReturnGoodsService;
	
	/**
	 * 分页查询商品退货期
	 * @param mtmyReturnGoods
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:returnGoodsDate:list")
	@RequestMapping(value = { "list" })
	public String list(MtmyReturnGoods mtmyReturnGoods,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<MtmyReturnGoods> page = mtmyReturnGoodsService.findList(new Page<MtmyReturnGoods>(request, response), mtmyReturnGoods);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[商品退货期管理-分页展示list数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "商品退货期管理-分页展示数据", e);
			addMessage(redirectAttributes, "查询出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyReturnGoodsList";
	}
	/**
	 * 查询商品退货期详情
	 * @param mtmyReturnGoods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value = { "ec:returnGoodsDate:view", "ec:returnGoodsDate:add", "ec:returnGoodsDate:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(MtmyReturnGoods mtmyReturnGoods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			if(StringUtils.isNotBlank(mtmyReturnGoods.getId())){
				mtmyReturnGoods = mtmyReturnGoodsService.get(mtmyReturnGoods.getId());
			}
		} catch (Exception e) {
			logger.error("#####[商品退货期管理-查询商品退货期详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "商品退货期管理-查询商品退货期详情", e);
			addMessage(redirectAttributes, "查询出现异常，请与管理员联系");
		}
		model.addAttribute("mtmyReturnGoods", mtmyReturnGoods);
		return "modules/ec/mtmyReturnGoodsForm";
	}
	/**
	 * 保存或修改退货期
	 * @param mtmyReturnGoods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"save"})
	public String save(MtmyReturnGoods mtmyReturnGoods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			mtmyReturnGoodsService.save(mtmyReturnGoods);
			addMessage(redirectAttributes, "保存/修改退货期成功");
		} catch (Exception e) {
			logger.error("#####[商品退货期管理-保存/修改商品退货期-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "商品退货期管理-保存/修改商品退货期", e);
			addMessage(redirectAttributes, "保存/修改出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/returnGoodsDate/list";
	}
	/**
	 * 删除退货期
	 * @param mtmyReturnGoods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("ec:returnGoodsDate:del")
	@RequestMapping(value = "del")
	public String delete(MtmyReturnGoods mtmyReturnGoods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			mtmyReturnGoodsService.delete(mtmyReturnGoods);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			logger.error("#####[商品退货期管理-删除退货期-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "商品退货期管理-删除退货期", e);
			addMessage(redirectAttributes, "删除退货期失败，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/returnGoodsDate/list";
	}
	
	/**
	 * 批量删除退货期
	 * @param ids
	 * @param redirectAttributes
	 * @param mtmyReturnGoods
	 * @return
	 */
	@RequiresPermissions("ec:returnGoodsDate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes,HttpServletRequest request,Model model) {
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				MtmyReturnGoods mtmyReturnGoods = new MtmyReturnGoods();
				mtmyReturnGoods.setId(id);
				mtmyReturnGoodsService.delete(mtmyReturnGoods);
			}
			addMessage(redirectAttributes, "批量删除退货期成功，请与管理员联系");
		} catch (Exception e) {
			logger.error("#####[商品退货期管理-批量删除退货期-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "商品退货期管理-批量删除退货期", e);
			addMessage(redirectAttributes, "批量删除退货期失败，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/returnGoodsDate/list";
	}
}

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
import com.training.modules.ec.entity.GoodsType;
import com.training.modules.ec.service.GoodsTypeService;

/**
 * 商品类型-Controller层
 * @author kele
 * @version 2016-6-18
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodstype")
public class GoodsTypeController extends BaseController{

	@Autowired
	private GoodsTypeService goodsTypeService;
	
	/**
	 * 分页查询商品类型
	 * @param goodsBrand
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodstype:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(GoodsType goodsType,Model model, HttpServletRequest request, HttpServletResponse response){
		Page<GoodsType> page = goodsTypeService.find(new Page<GoodsType>(request, response), goodsType);
		model.addAttribute("page", page);
		model.addAttribute("goodsType", goodsType);
		return "modules/ec/goodsTypeList";
	}
	
	/**
	 * 商品类型 查看、修改、添加
	 * @param goodsType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodstype:view","ec:goodstype:add","ec:goodstype:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsType goodsType,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsType.getId())){
			goodsType = goodsTypeService.get(goodsType);
			model.addAttribute("goodsType", goodsType);
		}
		return "modules/ec/goodsTypeForm";
	}
	
	/**
	 * 保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodstype:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsType goodsType, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			goodsTypeService.save(goodsType);
			addMessage(redirectAttributes, "保存/修改  商品类型'" + goodsType.getName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodstype/list";
	}
	
	/**
	 * 删除
	 * @param goodsType
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodstype:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsType goodsType, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		goodsTypeService.delete(goodsType);;
		addMessage(redirectAttributes, "删除商品类型信息成功");
		return "redirect:" + adminPath + "/ec/goodstype/list";
	}
}

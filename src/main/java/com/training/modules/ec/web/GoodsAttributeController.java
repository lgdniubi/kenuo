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
import com.training.modules.ec.entity.GoodsAttribute;
import com.training.modules.ec.entity.GoodsType;
import com.training.modules.ec.service.GoodsAttributeService;
import com.training.modules.ec.service.GoodsTypeService;

/**
 * 商品属性-Controller层
 * @author kele
 * @version 2016-6-20
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsattribute")
public class GoodsAttributeController extends BaseController{

	@Autowired
	private GoodsAttributeService goodsAttributeService;
	@Autowired
	private GoodsTypeService goodsTypeService;
	
	/**
	 * 分页查询商品属性
	 * @param goodsBrand
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsattribute:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(GoodsAttribute goodsAttribute,Model model, HttpServletRequest request, HttpServletResponse response){
		//查询商品类型
		List<GoodsType> goodsTypeList = goodsTypeService.findAllList(new GoodsType());
		model.addAttribute("goodsTypeList", goodsTypeList);
		//商品属性列表
		Page<GoodsAttribute> page = goodsAttributeService.find(new Page<GoodsAttribute>(request, response), goodsAttribute);
		model.addAttribute("page", page);
		return "modules/ec/goodsAttributeList";
	}
	
	/**
	 * 商品属性 查看、修改、添加
	 * @param goodsType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsattribute:view","ec:goodsattribute:add","ec:goodsattribute:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsAttribute goodsAttribute,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//查询商品类型
		GoodsType goodsType = new GoodsType();
		List<GoodsType> goodsTypeList = goodsTypeService.findAllList(goodsType);
		model.addAttribute("goodsTypeList", goodsTypeList);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsAttribute.getId())){
			goodsAttribute = goodsAttributeService.get(goodsAttribute);
		}else{
			//id为null或者"" 时，则为添加下级菜单时，code自增
			List<GoodsAttribute> l = goodsAttributeService.findAllList(goodsAttribute);
			if(l.size()==0){
				goodsAttribute.setSort(goodsAttribute.getSort());
			}else{
				goodsAttribute.setSort(l.get(l.size()-1).getSort() + 10);
			}
			goodsAttribute.setAttrIndex(0);
			goodsAttribute.setAttrInputType(0);
		}
		model.addAttribute("goodsAttribute", goodsAttribute);
		return "modules/ec/goodsAttributeForm";
	}
	
	/**
	 * 商品属性保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsattribute:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsAttribute goodsAttribute, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			goodsAttributeService.save(goodsAttribute);
			addMessage(redirectAttributes, "保存/修改 商品属性'" + goodsAttribute.getAttrName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodsattribute/list";
	}
	
	/**
	 * 商品属性-是否检索
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"updateattrindex"})
	public @ResponseBody Map<String, String> updateattrindex(HttpServletRequest request){
		//商品属性-是否检索
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String attrIndex = request.getParameter("ATTRINDEX");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(attrIndex)){
				GoodsAttribute goodsAttribute = new GoodsAttribute();
				goodsAttribute.setId(id);
				goodsAttribute.setAttrIndex(Integer.parseInt(attrIndex));
				goodsAttributeService.updateAttrIndex(goodsAttribute);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ATTRINDEX", attrIndex);
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
	
	/**
	 * 商品属性删除
	 * @param goodsattribute
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsattribute:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsAttribute goodsAttribute, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		goodsAttributeService.delete(goodsAttribute);
		addMessage(redirectAttributes, "删除商品属性信息成功");
		return "redirect:" + adminPath + "/ec/goodsattribute/list";
	}
}

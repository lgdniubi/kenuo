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
import com.training.modules.ec.entity.GoodsSpec;
import com.training.modules.ec.entity.GoodsSpecItem;
import com.training.modules.ec.entity.GoodsType;
import com.training.modules.ec.service.GoodsSpecItemService;
import com.training.modules.ec.service.GoodsSpecService;
import com.training.modules.ec.service.GoodsTypeService;

/**
 * 商品规格-Controller层
 * @author kele
 * @version 2016-6-20
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsspec")
public class GoodsSpecController extends BaseController{

	@Autowired
	private GoodsSpecService goodsSpecService;
	@Autowired
	private GoodsTypeService goodsTypeService;
	@Autowired
	private GoodsSpecItemService goodsSpecItemService;
	
	/**
	 * 分页查询商品规格
	 * @param goodsBrand
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsspec:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(GoodsSpec goodsSpec,Model model, HttpServletRequest request, HttpServletResponse response){
		//查询商品类型
		List<GoodsType> goodsTypeList = goodsTypeService.findAllList(new GoodsType());
		model.addAttribute("goodsTypeList", goodsTypeList);
		//商品规格列表
		Page<GoodsSpec> page = goodsSpecService.find(new Page<GoodsSpec>(request, response), goodsSpec);
		model.addAttribute("page", page);
		return "modules/ec/goodsSpecList";
	}
	
	/**
	 * 商品规格 查看、修改、添加
	 * @param goodsType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsspec:view","ec:goodsspec:add","ec:goodsspec:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsSpec goodsSpec,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//查询商品类型
		GoodsType goodsType = new GoodsType();
		List<GoodsType> goodsTypeList = goodsTypeService.findAllList(goodsType);
		model.addAttribute("goodsTypeList", goodsTypeList);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsSpec.getId())){
			goodsSpec = goodsSpecService.get(goodsSpec);
			
		}else{
			//id为null或者"" 时，则为添加下级菜单时，code自增
			List<GoodsSpec> l = goodsSpecService.findAllList(goodsSpec);
			if(l.size()==0){
				goodsSpec.setSort(goodsSpec.getSort());
			}else{
				goodsSpec.setSort(l.get(l.size()-1).getSort() + 10);
			}
		}
		model.addAttribute("goodsSpec", goodsSpec);
		return "modules/ec/goodsSpecForm";
	}
	
	/**
	 * 商品规格保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsspec:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsSpec goodsSpec, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			logger.info("#####[specItems:]"+goodsSpec.getSpecItem());
			//保存/修改商品规格表以及商品规格项表
			goodsSpecService.saveGoodsSpec(goodsSpec);
			addMessage(redirectAttributes, "保存/修改 商品规格'" + goodsSpec.getName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodsspec/list";
	}
	
	/**
	 * 商品规格删除
	 * @param goodsspec
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsspec:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsSpec goodsSpec, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//逻辑删除
		goodsSpecService.delete(goodsSpec);
		addMessage(redirectAttributes, "删除商品规格信息成功");
		return "redirect:" + adminPath + "/ec/goodsspec/list";
	}
	
	/**
	 * 商品规格项-（删除、添加）
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"updatespecitems"})
	public @ResponseBody Map<String, String> updatespecitems(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String method = request.getParameter("METHOD");
			String specId = request.getParameter("SPECID");
			if(!StringUtils.isEmpty(method) && !StringUtils.isEmpty(specId)){
				
				GoodsSpecItem items = new GoodsSpecItem();
				items.setSpecId(Integer.parseInt(specId));
				
				if("ADD".equals(method)){
					String itemsvalue = request.getParameter("ITEMSVALUE");
					if(!StringUtils.isEmpty(itemsvalue)){
						//添加
						items.setItem(itemsvalue);
						int itemId = goodsSpecItemService.addItems(items);
						jsonMap.put("ITEMID", String.valueOf(itemId));
						jsonMap.put("STATUS", "OK");
						jsonMap.put("MESSAGE", "添加成功");
					}else{
						jsonMap.put("STATUS", "ERROR");
						jsonMap.put("MESSAGE", "请输入规格项值");
					}
				}else if("DELETE".equals(method)){
					String specItemId = request.getParameter("SPECITEMID");//规格项Id
					if(!StringUtils.isEmpty(specItemId)){
						//删除
						items.setSpecItemId(Integer.parseInt(specItemId));
						goodsSpecItemService.deleteItems(items);
						jsonMap.put("STATUS", "OK");
						jsonMap.put("MESSAGE", "删除成功");
					}else{
						jsonMap.put("STATUS", "ERROR");
						jsonMap.put("MESSAGE", "删除失败,规格项ID参数为空");
					}
				}
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("商品规格项-添加/删除 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}

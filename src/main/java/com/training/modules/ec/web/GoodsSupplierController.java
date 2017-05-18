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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsSupplier;
import com.training.modules.ec.entity.GoodsSupplierContacts;
import com.training.modules.ec.service.GoodsSupplierService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * goods_supplier图Controller
 * 
 * @version  土豆  2017.5.9
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsSupplier")
public class GoodsSupplierController extends BaseController{
	
	@Autowired
	private GoodsSupplierService goodsSupplierService;
	
	/**
	 * 查看供应商列表
	 * @param goodsSupplier
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(GoodsSupplier goodsSupplier,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<GoodsSupplier> page = goodsSupplierService.findPage(new Page<GoodsSupplier>(request, response), goodsSupplier);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看供应商列表失败!", e);
			logger.error("查看供应商列表失败：" + e.getMessage());
		}
		
		return "modules/ec/goodsSupplierList";
	}
	
	/**
	 * 跳转编辑供应商页面
	 * @param tabBackground
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(GoodsSupplier goodsSupplier,HttpServletRequest request,Model model){
		try{
			if(goodsSupplier.getGoodsSupplierId()!=0){
				goodsSupplier = goodsSupplierService.get(goodsSupplier);
				model.addAttribute("goodsSupplier", goodsSupplier);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑供应商页面失败!", e);
			logger.error("跳转编辑供应商页面：" + e.getMessage());
		}
		return "modules/ec/goodsSupplierForm";
	}
	
	/**
	 * 添加/修改 
	 * @param tabBackground
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(GoodsSupplier goodsSupplier,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(goodsSupplier.getGoodsSupplierId()==0){
				User user=UserUtils.getUser();
				goodsSupplier.setCreateBy(user);
				goodsSupplierService.save(goodsSupplier);
				addMessage(redirectAttributes, "添加供应商成功！");
			}else{
				User user=UserUtils.getUser();
				goodsSupplier.setUpdateBy(user);
				goodsSupplierService.update(goodsSupplier);
				addMessage(redirectAttributes, "修改供应商成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存供应商失败!", e);
			logger.error("保存供应商失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存供应商失败");
		}
		
		return "redirect:" + adminPath + "/ec/goodsSupplier/list";
	}
	
	/**
	 * 删除
	 * @param goodsSupplier
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "del")
	public String delete(GoodsSupplier goodsSupplier,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			GoodsSupplierContacts goodsSupplierContacts = new GoodsSupplierContacts();
			goodsSupplierContacts.setGoodsSupplierId(goodsSupplier.getGoodsSupplierId());
			goodsSupplierService.deleteGoodsSupplier(goodsSupplier,goodsSupplierContacts);
			addMessage(redirectAttributes, "删除成功");
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除供应商失败!", e);
			logger.error("逻辑删除供应商失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除失败");
			return "error";
		}
	}
	
	/**
	 * 异步 修改状态
	 * @param banner
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsSupplier:update"},logical=Logical.OR)
	@RequestMapping(value = {"updateStatus"})
	@ResponseBody
	public Map<String, String> updateStatus(GoodsSupplier goodsSupplier,HttpServletRequest request){
		//商品属性-是否检索
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			if(goodsSupplier.getGoodsSupplierId() !=0 && !StringUtils.isEmpty(goodsSupplier.getStatus())){
				goodsSupplierService.updateStatus(goodsSupplier);
				jsonMap.put("yesOrNo", "SUCCESS");//成功与否
				jsonMap.put("status", goodsSupplier.getStatus());//状态
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
	 * 获取供应商
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeDate(HttpServletRequest request, HttpServletResponse response,GoodsSupplier goodsSupplier){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Page<GoodsSupplier> page = goodsSupplierService.findPage(new Page<GoodsSupplier>(request, response,-1), goodsSupplier);
		List<GoodsSupplier> list = page.getList();
		for (int i = 0; i < list.size(); i++) {
			GoodsSupplier g = list.get(i);
			if("0".equals(g.getStatus())){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", g.getGoodsSupplierId());
				map.put("pId", 0);
				map.put("name", g.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}

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

import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsSupplierContacts;
import com.training.modules.ec.service.GoodsSupplierContactsService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * goods_supplier_contacts的Controller
 * 
 * @version 土豆   2017.5.9
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsSupplierContacts")
public class GoodsSupplierContactsController extends BaseController{
	
	@Autowired
	private GoodsSupplierContactsService goodsSupplierContactsservice;
	
	/**
	 * 查看供应商联系人列表
	 * @param goodsSupplierContacts
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			List<GoodsSupplierContacts> list = goodsSupplierContactsservice.findList(goodsSupplierContacts);
			model.addAttribute("list", list);
			model.addAttribute("supplierId", goodsSupplierContacts.getGoodsSupplierId());
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看供应商联系人列表失败!", e);
			logger.error("查看供应商联系人列表失败：" + e.getMessage());
		}
		
		return "modules/ec/goodsSupplierContactsList";
	}
	
	/**
	 * 跳转编辑供应商联系人页面
	 * @param goodsSupplierContacts
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request,Model model){
		try{
			if(goodsSupplierContacts.getGoodsSupplierContactsId()!=0){
				goodsSupplierContacts = goodsSupplierContactsservice.get(goodsSupplierContacts);
			}
			model.addAttribute("goodsSupplierContacts", goodsSupplierContacts);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑供应商联系人页面失败!", e);
			logger.error("跳转编辑供应商联系人页面：" + e.getMessage());
		}
		return "modules/ec/goodsSupplierContactsForm";
	}
	
	/**
	 * 添加/修改 
	 * @param goodsSupplierContacts
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(goodsSupplierContacts.getGoodsSupplierContactsId()==0){
				User user=UserUtils.getUser();
				goodsSupplierContacts.setCreateBy(user);
				goodsSupplierContactsservice.save(goodsSupplierContacts);
				addMessage(redirectAttributes, "添加供应商联系人成功！");
				return "success";
			}else{
				User user=UserUtils.getUser();
				goodsSupplierContacts.setUpdateBy(user);
				goodsSupplierContactsservice.update(goodsSupplierContacts);
				addMessage(redirectAttributes, "修改供应商联系人成功！");
				return "success";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存供应商联系人失败!", e);
			logger.error("保存供应商联系人失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存供应商联系人失败");
			return "error";
		}
	}
	
	/**
	 * 删除
	 * @param GoodsSupplierContacts
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "del")
	@ResponseBody
	public String delete(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			goodsSupplierContactsservice.deleteGoodsSupplierContacts(goodsSupplierContacts);
			addMessage(redirectAttributes, "删除供应商联系人成功");
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除供应商联系人失败!", e);
			logger.error("删除供应商联系人失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除供应商联系人失败");
			return "error";
		}
	}
	
	/**
	 * 删除供应商联系人,返回供应商联系人列表
	 * @param request
	 * @param goodsSupplierContacts
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "newgoodsSupplierContactsList")
	public String newgoodsSupplierContactsList(GoodsSupplierContacts goodsSupplierContacts, HttpServletRequest request,HttpServletResponse response, Model model) {
		try{
			List<GoodsSupplierContacts> list = goodsSupplierContactsservice.findList(goodsSupplierContacts);
			model.addAttribute("list", list);
			model.addAttribute("supplierId", goodsSupplierContacts.getGoodsSupplierId());
			model.addAttribute("goodsSupplierContacts",goodsSupplierContacts);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存供应商联系人以后返回供应商联系人列表失败!", e);
			logger.error("保存供应商联系人以后返回供应商联系人列表失败：" + e.getMessage());
		}
		return "modules/ec/goodsSupplierContactsList";
	}
	
	/**
	 * 异步 修改状态
	 * @param goodsSupplierContacts
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsSupplier:update"},logical=Logical.OR)
	@RequestMapping(value = {"updateStatus"})
	@ResponseBody
	public Map<String, String> updateStatus(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			if(goodsSupplierContacts.getGoodsSupplierContactsId() !=0 && !StringUtils.isEmpty(goodsSupplierContacts.getStatus())){
				goodsSupplierContactsservice.updateStatus(goodsSupplierContacts);
				jsonMap.put("yesOrNo", "SUCCESS");//成功与否
				jsonMap.put("status", goodsSupplierContacts.getStatus());//状态
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("修改供应商联系人状态 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 获取供应商下面是否还有联系人
	 * @param goodsSupplierContacts
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"getCountGoodsSupplierContacts"})
	public boolean getCountGoodsSupplierContacts(GoodsSupplierContacts goodsSupplierContacts,HttpServletRequest request){
		return goodsSupplierContactsservice.getCount(goodsSupplierContacts);
	}
}

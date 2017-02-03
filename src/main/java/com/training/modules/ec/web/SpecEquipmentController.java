package com.training.modules.ec.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.SpecEquipmentDao;
import com.training.modules.ec.entity.Equipment;
import com.training.modules.ec.entity.EquipmentLogs;
import com.training.modules.ec.entity.ShopComEquipment;
import com.training.modules.ec.service.SpecEquipmentService;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONObject;

/**
 * 设备管理
 * @author 小叶
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/specEquipment")
public class SpecEquipmentController extends BaseController {
	
	@Autowired 
	private SpecEquipmentService specEquipmentService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SpecEquipmentDao specEquipmentDao;
	
	@RequiresPermissions("ec:specEquipment:list")
	@RequestMapping(value="list")
	/**
	 * 分页查询设备列表
	 * @param equipment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String list(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			Page<Equipment> page = specEquipmentService.findList(new Page<Equipment>(request, response), equipment);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "设备列表", e);
			logger.error("设备列表出错信息：" + e.getMessage());
		}
		return "modules/ec/specEquipmentList";
	}
	
	/**
	 * 编辑设备
	 * @param equipment
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:specEquipment:add","ec:specEquipment:edit"})
	@RequestMapping(value="form")
	public String form(Equipment equipment,HttpServletRequest request ,Model model){
		try{
			if(equipment.getEquipmentId() != 0){
				equipment = specEquipmentService.get(equipment);
			}
			model.addAttribute("equipment", equipment);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增设备页面", e);
			logger.error("跳转新增设备页面出错信息：" + e.getMessage());
		}
		return "modules/ec/specEquipmentForm";
	}
	
	/**
	 * 新增修改设备
	 * @param equipment
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:specEquipment:add", "ec:specEquipment:edit" })
	@RequestMapping(value = "save")
	public String save(Equipment equipment, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try{
			specEquipmentService.saveSpecEquipment(equipment);
			addMessage(redirectAttributes, "保存设备成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存设备", e);
			logger.error("方法：save，保存设备出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存设备失败");
		}
		return "redirect:" + adminPath + "/ec/specEquipment/list";
	}
	
	/**
	 * 逻辑删除设备
	 * @param skill
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:specEquipment:del")
	@RequestMapping(value = "delete")
	public String delete(Equipment equipment, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try{
			specEquipmentService.deleteSpecEquipment(equipment);
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除设备", e);
			logger.error("方法：delete，删除设备出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除设备失败");
		}
		return "redirect:" + adminPath + "/ec/specEquipment/list";
	}
	
	
	/**
	 * 获取特殊设备JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Equipment> list = specEquipmentService.findAllList();
		for (int i = 0; i < list.size(); i++) {
			Equipment e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getEquipmentId());
				map.put("name", e.getName());
				
				mapList.add(map);
		}
		return mapList;
	}
	
	
	
	/**
	 * 获取特殊设备对应的店铺JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeDataForShop")
	public List<Map<String, Object>> treeDataForShop(HttpServletResponse response,HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.selectOfficeById(id);
		for(int i = 0; i < list.size(); i++){
			Office o = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", o.getId());
			map.put("name", o.getName());
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 店铺通用设备列表
	 * @param shopComEquipment
	 * @param response
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="shopComEquipmentList")
	public String shopComEquipmentList(ShopComEquipment shopComEquipment,HttpServletResponse response,HttpServletRequest request,Model model){
		try{
			Page<ShopComEquipment> page = specEquipmentService.findShopComEquipmentList(new Page<ShopComEquipment>(request, response), shopComEquipment);
			model.addAttribute("page", page);
			model.addAttribute("shopComEquipment",shopComEquipment);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "店铺通用设备列表", e);
			logger.error("店铺通用设备列表出错信息：" + e.getMessage());
		}
		return "modules/ec/shopComEquipmentList";
	}
	
	/**
	 * 店铺增加通用设备
	 * @param shopComEquipment
	 * @return
	 */
	@RequestMapping(value="shopComEquipmentForm")
	public String shopComEquipmentForm(ShopComEquipment shopComEquipment,Model model,HttpServletRequest request){
		try{
			model.addAttribute("shopComEquipment",shopComEquipment);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增店铺通用设备页面", e);
			logger.error("跳转新增店铺通用设备页面出错信息：" + e.getMessage());
		}
		
		return "modules/ec/shopComEquipmentForm";
	}
	
	/**
	 * 保存店铺添加的通用设备
	 * @param shopComEquipment
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveshopComEquipment")
	public String saveshopComEquipment(ShopComEquipment shopComEquipment,RedirectAttributes redirectAttributes,HttpServletRequest request){
		String type = "";
		try{
			shopComEquipment.setLabelId(specEquipmentDao.get(String.valueOf(shopComEquipment.getEquipmentId())).getLabelId());
			int querySum = specEquipmentService.querySum(shopComEquipment.getShopId(), shopComEquipment.getEquipmentId());
			int sum = shopComEquipment.getSum();
			String name = shopComEquipment.getName() + "_";
			for(int i = 1; i <= sum; i++){
				int num = querySum + i;
				shopComEquipment.setName(name + num);
				specEquipmentService.saveshopComEquipment(shopComEquipment);
			}
			type = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存店铺通用设备", e);
			logger.error("方法：saveshopComEquipment，保存店铺通用设备出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存店铺通用设备失败");
			type = "error";
		}
		return type;
	}
	
	/**
	 * 修改状态
	 * @param shopComEquipment
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateType")
	public String updateType(ShopComEquipment shopComEquipment,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			String flag = request.getParameter("flag");
			specEquipmentService.updateType(Integer.valueOf(flag), shopComEquipment.getShopComEquipmentId());
		} catch (Exception e) {
			logger.error("修改店铺通用设备状态出错信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "修改店铺通用设备状态", e);
		}
		return "redirect:" + adminPath + "/ec/specEquipment/shopComEquipmentList?shopId="+shopComEquipment.getShopId();
	}
	
	/**
	 * 获取店铺对应的通用设备JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeDataForCom")
	public List<Map<String, Object>> treeDataForCom(HttpServletResponse response,HttpServletRequest request) {
		String shopId = request.getParameter("shopId");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Equipment> list = specEquipmentService.findForCom(shopId);
		for (int i = 0; i < list.size(); i++) {
			Equipment e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getEquipmentId());
				map.put("name", e.getName());
				
				mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 根据设备标签id和选择的市场给设备命名
	 * @param equipment
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getEquipmentName")
	public String getEquipmentName(Equipment equipment, RedirectAttributes redirectAttributes,HttpServletRequest request){
		JSONObject jsonO = new JSONObject();
		try{
			int queryNum = specEquipmentService.queryNum(equipment.getBazaarId(), equipment.getLabelId());
			queryNum = queryNum + 1;
			String name = equipment.getLabelName() + queryNum;
			jsonO.put("name", name);
			jsonO.put("type", "success");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "根据设备标签id和选择的市场给设备命名", e);
			logger.error("根据设备标签id和选择的市场给设备命名出错信息：" + e.getMessage());
			jsonO.put("type", "error");
		}
		return jsonO.toString();
	}
	
	/**
	 * 查询设备排班日志详情
	 * 
	 * @param equipmentLogs
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "equipmentLogsForm")
	public String equipmentLogsForm(EquipmentLogs equipmentLogs, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			Page<EquipmentLogs> page = specEquipmentService.findEquipmentLogsList(new Page<EquipmentLogs>(request, response), equipmentLogs);
			model.addAttribute("page", page);
		}catch(Exception e){
			logger.error("查询设备排班日志详情出错信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "查询设备排班日志详情", e);
		}
		
		return "modules/ec/equipmentLogs";
	}
}

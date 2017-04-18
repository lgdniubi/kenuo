package com.training.modules.ec.web;

import java.util.HashMap;
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
import com.training.modules.ec.entity.EquipmentLabel;
import com.training.modules.ec.service.EquipmentLabelService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 设备标签管理
 * @author 小叶   2017-1-10 
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/equipmentLabel")
public class EquipmentLabelController extends BaseController{

	@Autowired
	private EquipmentLabelService equipmentLabelService;

	/**
	 * 分页查询设备标签列表
	 * @param equipmentLabel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:equipmentLabel:list")
	@RequestMapping(value="list")
	public String list(EquipmentLabel equipmentLabel, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			Page<EquipmentLabel> page = equipmentLabelService.findList(new Page<EquipmentLabel>(request, response), equipmentLabel);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "设备标签列表", e);
			logger.error("设备标签列表出错信息：" + e.getMessage());
		}
		return "modules/ec/equipmentLabelList";
	}
	
	/**
	 * 编辑新增设备标签
	 * @param equipmentLabel
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:equipmentLabel:view", "ec:equipmentLabel:add","ec:equipmentLabel:edit"})
	@RequestMapping(value="form")
	public String form(EquipmentLabel equipmentLabel,Model model,HttpServletRequest request){
		try{
			if(equipmentLabel.getEquipmentLabelId() != 0){
				equipmentLabel = equipmentLabelService.get(String.valueOf(equipmentLabel.getEquipmentLabelId()));
			}
			model.addAttribute("equipmentLabel", equipmentLabel);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增设备标签页面", e);
			logger.error("跳转新增设备标签页面出错信息：" + e.getMessage());
		}
		return "modules/ec/equipmentLabelForm";
	}
	
	/**
	 * 新增编辑设备标签
	 * @param equipmentLabel
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:equipmentLabel:add", "ec:equipmentLabel:edit" })
	@RequestMapping(value = "save")
	public String save(EquipmentLabel equipmentLabel, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try{
			equipmentLabelService.saveEquipmentLabel(equipmentLabel);
			addMessage(redirectAttributes, "保存设备标签成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存设备标签", e);
			logger.error("方法：save，保存设备标签出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存设备标签失败");
		}
		return "redirect:" + adminPath + "/ec/equipmentLabel/list";
	}
	
	/**
	 * 逻辑删除设备标签
	 * @param equipmentLabel
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:equipmentLabel:del")
	@RequestMapping(value = "delete")
	public String delete(EquipmentLabel equipmentLabel, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try{
			equipmentLabelService.deleteEquipmentLabel(equipmentLabel);
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除设备标签", e);
			logger.error("方法：delete，删除设备标签出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除设备标签失败");
		}
		return "redirect:" + adminPath + "/ec/equipmentLabel/list";
	}

	/**
	 * 获取设备标签JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response,HttpServletRequest request) {
		String newFlag = request.getParameter("newFlag");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<EquipmentLabel> list = equipmentLabelService.findAllList(newFlag);
		for (int i = 0; i < list.size(); i++) {
			EquipmentLabel e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getEquipmentLabelId());
				map.put("name", e.getName());
				
				mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 获取所有的设备标签JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "newTreeData")
	public List<Map<String, Object>> newTreeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<EquipmentLabel> list = equipmentLabelService.newFindAllList();
		for (int i = 0; i < list.size(); i++) {
			EquipmentLabel e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getEquipmentLabelId());
				map.put("name", e.getName());
				
				mapList.add(map);
		}
		return mapList;
	}
	
	
	/**
	 * 验证设备编号是否存在
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkNO")
	public String checkNO(String oldNo, String no,HttpServletRequest request) {
		String type = "";
		try{
			if (no != null && no.equals(oldNo)) {
				type = "true";
			} else if (no != null && equipmentLabelService.getEquipmentLabelByNO(no) <= 0) {
				type = "true";
			}else{
				type = "false";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "验证设备编号是否存在错误", e);
			logger.error("验证设备编号是否存在出错信息：" + e.getMessage());
		}
		return type;
	}
	
	/**
	 * 验证设备名称是否存在
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name,HttpServletRequest request) {
		String type = "";
		try{
			if (name != null && name.equals(oldName)) {
				type = "true";
			} else if (name != null && equipmentLabelService.getEquipmentLabelByName(name) <= 0) {
				type = "true";
			}else{
				type = "false";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "验证设备名称是否存在错误", e);
			logger.error("验证设备名称是否存在出错信息：" + e.getMessage());
		}
		return type;
	}
	
	/**
	 * 设备标签是否显示
	 * @param request
	 * @param equipmentLabel
	 * @return
	 */
	@RequestMapping(value = "changeIsShow")
	@ResponseBody
	public Map<String, String> changeIsShow(HttpServletRequest request,EquipmentLabel equipmentLabel) {
		
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isyesno = request.getParameter("isShow");
			if("1".equals(isyesno)){
				int sum = equipmentLabelService.selectGoodsisOnSale(equipmentLabel.getEquipmentLabelId());
				if(sum == 0){
					equipmentLabelService.updateIsShow(equipmentLabel);
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISYESNO", isyesno);
				}else{
					jsonMap.put("STATUS", "NO");
				}
			}else if("0".equals(isyesno)){
				equipmentLabelService.updateIsShow(equipmentLabel);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISYESNO", isyesno);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改设备标签是否显示失败", e);
			logger.error("修改设备标签是否显示失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}

}

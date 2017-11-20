package com.training.modules.train.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.Position;
import com.training.modules.train.service.PositionService;

/**
 * 
 * @className PositionController
 * @description TODO 职位
 * @author chenbing
 * @date 2017年11月16日 兵子
 *
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/position")
public class PositionController extends BaseController{
	
	@Autowired
	private PositionService positionService;
	
	/**
	 * 
	 * @Title: form
	 * @Description: TODO 页面跳转
	 * @param position
	 * @param model
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月16日 兵子
	 */
	@RequiresPermissions("train:position:view")
	@RequestMapping(value="form")
	public String form(Position position,Model model){
		if (position != null && StringUtils.isNotBlank(position.getValue()) 
				&& StringUtils.isNotBlank(position.getType())) {
			position = positionService.get(position);
		}
		model.addAttribute("position", position);
		return "modules/train/positionForm";
	}
	
	/**
	 * 
	 * @Title: deleteAll
	 * @Description: TODO 删除职位
	 * @param ids
	 * @param position
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月16日 兵子
	 */
	@RequiresPermissions("train:position:del")
	@RequestMapping(value="deleteAll")
	public String deleteAll(String ids,Position position,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			if (StringUtils.isNotBlank(ids) && position.getDepartment().getdId() != null) {
				positionService.deletePosition(ids,position);
				addMessage(redirectAttributes, "删除职位成功");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除职位", e);
			logger.error("删除职位错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return  "redirect:" + adminPath + "/train/department/positionList?dId="+position.getDepartment().getdId();
	}

	/**
	 * 
	 * @Title: addPosition
	 * @Description: TODO 添加页面跳转
	 * @param values
	 * @param position
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月17日 兵子
	 */
	@RequiresPermissions("train:position:add")
	@RequestMapping(value="addPosition")
	public String addPosition(String values,Position position,Model model){
		List<Position> exiPosition = Lists.newArrayList();
		List<Position> notPosition = Lists.newArrayList();
		if (position.getDepartment().getdId() != null) {
			position.setType("sys_user_type");
			if (StringUtils.isNotBlank(values)) {
				String[] pvalue = values.split(",");
				for (String value : pvalue) {
					position.setValue(value);
					Position ePosition = positionService.get(position);
					exiPosition.add(ePosition);
				}
			}
			notPosition = positionService.findPositionNotValues(values);
		}
		model.addAttribute("exiPosition", exiPosition);
		model.addAttribute("notPosition", notPosition);
		model.addAttribute("posdId", position.getDepartment().getdId());
		return "modules/train/addPosition";
	}
	
	/**
	 * 
	 * @Title: savePosition
	 * @Description: TODO 保存职位
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月17日 兵子
	 */
	@RequestMapping(value="savePosition")
	public String savePosition(String values,Position position,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			if (StringUtils.isNotBlank(values) && position.getDepartment().getdId() != null) {
				positionService.savePosition(values,position);
				addMessage(redirectAttributes, "保存职位成功");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存职位", e);
			logger.error("保存职位错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return  "redirect:" + adminPath + "/train/department/positionList?dId="+position.getDepartment().getdId();
	}
}

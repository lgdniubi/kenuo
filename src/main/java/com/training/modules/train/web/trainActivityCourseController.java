package com.training.modules.train.web;

import java.util.ArrayList;
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
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainActivityCourse;
import com.training.modules.train.entity.TrainActivityCourseContent;
import com.training.modules.train.entity.TrainOrder;
import com.training.modules.train.service.TrainActivityCourseService;


/**
 * 妃子校活动课程管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/trainActivityCourse")
public class trainActivityCourseController extends BaseController{
	
	@Autowired
	private TrainActivityCourseService trainActivityCourseService;
	@Autowired
	private FranchiseeService franchiseeService;
	
	/**
	 * 妃子校活动课程list
	 * @param model
	 * @param trainActivityCourse
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:trainActivityCourse:list"},logical=Logical.OR)
	@RequestMapping(value = "list")
	public String list(Model model,TrainActivityCourse trainActivityCourse,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<TrainActivityCourse> page=trainActivityCourseService.findList(new Page<TrainActivityCourse>(request, response), trainActivityCourse);
			model.addAttribute("page", page);
			model.addAttribute("trainActivityCourse", trainActivityCourse);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校活动课程", e);
			logger.error("查询妃子校活动课程错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/trainActivityCourseList";
	}
	/**
	 * 妃子校活动课程详情
	 * @param model
	 * @param trainActivityCourse
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:trainActivityCourse:view", "train:trainActivityCourse:add"}, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,TrainActivityCourse trainActivityCourse,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(trainActivityCourse.getAcId() != 0){
				trainActivityCourse = trainActivityCourseService.get(trainActivityCourse);
				String htmlEscape = HtmlUtils.htmlUnescape(trainActivityCourse.getContent());
				trainActivityCourse.setContent(htmlEscape);
				if(trainActivityCourse.getIsOpen()==1){
					String companyIds = trainActivityCourseService.findCompanyIds(trainActivityCourse.getAcId());
					trainActivityCourse.setFranchiseeId(companyIds);
				}
			}
			model.addAttribute("trainActivityCourse", trainActivityCourse);
			//查找所有商家
			model.addAttribute("officeList", franchiseeService.findAllCompanyList());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校活动课程详情", e);
			logger.error("查询妃子校活动课程详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/trainActivityCourseForm";
	}
	
	/**
	 * 保存妃子校活动课程
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,TrainActivityCourse trainActivityCourse,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String[] Images = request.getParameterValues("img");
			String htmlEscape = HtmlUtils.htmlEscape(trainActivityCourse.getContent());
			trainActivityCourse.setContent(htmlEscape);
			trainActivityCourseService.saveCourse(trainActivityCourse);
			/*int acId = trainActivityCourse.getAcId();
			if(acId != 0){
				List<TrainActivityCourseContent> list = new ArrayList<TrainActivityCourseContent>();
				for (int i = 0; i < Images.length; i++) {
					TrainActivityCourseContent t = new TrainActivityCourseContent();
					t.setAcId(acId);
					t.setImg(Images[i]);
					t.setType(1);
					t.setSort(i+1);
					list.add(t);
				}
				if(list.size() > 0){
					trainActivityCourseService.saveContent(list,acId);
				}
			}*/
			addMessage(redirectAttributes, "保存/修改课程活动成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校课程活动", e);
			logger.error("保存妃子校课程活动错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/trainActivityCourse/list";
	}
	
	/**
	 * 修改课程活动状态
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"train:trainActivityCourse:updateFlag"},logical=Logical.OR)
	@RequestMapping(value = "updateFlag")
	public Map<String, String> updateFlag(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String acId = request.getParameter("acId");
			String flag = request.getParameter("FLAG");
			String isyesno = request.getParameter("ISYESNO");
			if(!StringUtils.isEmpty(acId) && !StringUtils.isEmpty(flag) && !StringUtils.isEmpty(isyesno)){
				int num = trainActivityCourseService.updateFlag(acId, flag, isyesno);
				if(num != 0){
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISYESNO", isyesno);
				}else{
					jsonMap.put("STATUS", "ERROR");
					jsonMap.put("MESSAGE", "修改失败,必要参数为空");
				}
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("修改课程活动出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "删除文章", e);
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	/***
	 * 查看订单列表
	 * @param model
	 * @param trainOrder
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "findTrainOrder")
	public String findTrainOrder(Model model,TrainOrder trainOrder,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<TrainOrder> page=trainActivityCourseService.findTrainOrder(new Page<TrainOrder>(request, response), trainOrder);
			model.addAttribute("page", page);
			model.addAttribute("trainOrder", trainOrder);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校课程活动订单", e);
			logger.error("查询妃子校课程活动订单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/trainOrderList";
	}
}

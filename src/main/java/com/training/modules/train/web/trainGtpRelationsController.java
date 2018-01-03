package com.training.modules.train.web;

import java.util.ArrayList;
import java.util.List;




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
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainGtpRelations;
import com.training.modules.train.service.TrainGtpRelationsService;


/**
 * 地推
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/gtpRelations")
public class trainGtpRelationsController extends BaseController{
	@Autowired
	private TrainGtpRelationsService trainGtpRelationsService;
	@Autowired
	private OfficeService officeService;
	/**
	 * 地推管理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:gtpRelations:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String articlelist(TrainGtpRelations trainGtpRelations,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<TrainGtpRelations> page=trainGtpRelationsService.findList(new Page<TrainGtpRelations>(request, response), trainGtpRelations);
			model.addAttribute("page", page);
			model.addAttribute("trainGtpRelations", trainGtpRelations);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询地推管理", e);
			logger.error("查询地推管理错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/gtpRelationsList";
	}
	/**
	 * 邀请详情
	 * @param trainGtpRelations
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = {"train:gtpRelations:view"}, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(TrainGtpRelations trainGtpRelations,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			Page<TrainGtpRelations> page=trainGtpRelationsService.findAllList(new Page<TrainGtpRelations>(request, response), trainGtpRelations);
			model.addAttribute("page", page);
			model.addAttribute("trainGtpRelations", trainGtpRelations);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询地推邀请详情", e);
			logger.error("查询地推邀请详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/gtpRelationsForm";
	}
	/**
	 * 邀请数据统计
	 * @param trainGtpRelations
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = {"train:gtpRelations:report"}, logical = Logical.OR)
	@RequestMapping(value = "report")
	public String report(TrainGtpRelations trainGtpRelations,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			List<TrainGtpRelations> list = new ArrayList<TrainGtpRelations>();
			int totalNum = 0;
			if(trainGtpRelations.getOffice() != null){
				if(!trainGtpRelations.getOffice().getId().isEmpty()){
					TrainGtpRelations trainGtpRelation = new TrainGtpRelations();
					trainGtpRelation.setBeginDate(trainGtpRelations.getBeginDate());
					trainGtpRelation.setEndDate(trainGtpRelations.getEndDate());
					trainGtpRelation.setOffice(trainGtpRelations.getOffice());
					int num = trainGtpRelationsService.reportOffice(trainGtpRelation);
					trainGtpRelation.setGtpNnm(num);
					totalNum = num + totalNum;
					list.add(trainGtpRelation);
					
					List<Office> office = officeService.findByPidforChild(trainGtpRelations.getOffice());	// 获取下级所有机构
					for (int i = 0; i < office.size(); i++) {
						trainGtpRelation = new TrainGtpRelations();
						trainGtpRelation.setBeginDate(trainGtpRelations.getBeginDate());
						trainGtpRelation.setEndDate(trainGtpRelations.getEndDate());
						trainGtpRelation.setOffice(office.get(i));
						num = trainGtpRelationsService.report(trainGtpRelation);
						trainGtpRelation.setGtpNnm(num);
						totalNum = num + totalNum;
						list.add(trainGtpRelation);
					}
				}
			}
			model.addAttribute("list", list);
			model.addAttribute("trainGtpRelations", trainGtpRelations);
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询地推邀请统计", e);
			logger.error("查询地推邀请统计错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/gtpRelationsReport";
	}
}
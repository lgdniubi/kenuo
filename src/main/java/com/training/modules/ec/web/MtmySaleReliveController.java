package com.training.modules.ec.web;

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
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.MtmyRuleParamDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.MtmySaleRelieveExport;
import com.training.modules.ec.entity.MtmySaleRelieveLog;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.MtmySaleRelieveService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 分销
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmySale")
public class MtmySaleReliveController extends BaseController{
	
	@Autowired
	private MtmySaleRelieveService mtmySaleRelieveService;
	@Autowired
	private MtmyRuleParamDao mtmyRuleParamDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	/**
	 * 分销    分页查询申请解除用户
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmySale:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			//分页查询
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.find(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
			
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			
			model.addAttribute("mtmySaleRelieve", mtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-分页查询申请解除用户数据-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-分页查询申请解除用户数据", e);
			model.addAttribute("message", "查询出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleRelieveList";
	}
	/**
	 * 拒绝解除用户关系
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"refuse"})
	public String refuse(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			if(!StringUtils.isEmpty(mtmySaleRelieve.getId())){
//				申请表中 status 申请状态（0：申请中；1：同意；2：拒绝）
				mtmySaleRelieve.setStatus(2);
				User currentUser = UserUtils.getUser();
				mtmySaleRelieve.setCreateBy(currentUser);
				mtmySaleRelieveService.updateStatus(mtmySaleRelieve);
				//原始表中 status 状态（0：正常；1:解除申请）
				mtmySaleRelieve.setStatus(0);
				mtmySaleRelieveService.updateBy(mtmySaleRelieve);
				addMessage(redirectAttributes, "拒绝解除关系成功");
			}else{
				addMessage(redirectAttributes, "拒绝解除关系出现异常，请与管理员联系");
			}
		} catch (Exception e) {
			logger.error("#####[分销-拒绝解除关系-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-拒绝解除关系", e);
			model.addAttribute("message", "拒绝解除关系出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmySale/list";
	}
	/**
	 * 批量同意
	 * @param ids
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"agreeAll"})
	public String agreeAll(String parentIds,MtmySaleRelieve mtmySaleRelieve,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			if(mtmySaleRelieve.getNewParentId() != null && mtmySaleRelieve.getNewParentId() != ""){
				String idArray[] = parentIds.split(",");
				for (String id : idArray) {
					mtmySaleRelieve.setId(id);
					MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.get(mtmySaleRelieve);
					
					newMtmySaleRelieve.setNewParentId(mtmySaleRelieve.getNewParentId());
					newMtmySaleRelieve.setStatus(1);
					User currentUser = UserUtils.getUser();
					newMtmySaleRelieve.setCreateBy(currentUser);
					mtmySaleRelieveService.updateParentId(newMtmySaleRelieve);
					mtmySaleRelieveService.updateStatus(newMtmySaleRelieve);
					mtmySaleRelieveService.delete(newMtmySaleRelieve);
					//解除用户关系后  将用户来源改为13（北京代言人）
					Users users = new Users();
					users.setUserid(newMtmySaleRelieve.getUserId());
					users.setSource(13);
					mtmyUsersDao.relieveUser(users);
				}
				addMessage(redirectAttributes, "同意解除关系成功");
			}else{
				addMessage(redirectAttributes, "同意解除关系出现异常，请与管理员联系");
			}
		} catch (Exception e) {
			logger.error("#####[分销-同意解除关系-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-同意解除关系", e);
			model.addAttribute("message", "同意解除关系出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmySale/list";
	}
	/**
	 * 批量拒绝
	 * @param ids
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"refuseAll"})
	public String refuseAll(String ids,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			String idArray[] = ids.split(",");
			for (String id : idArray) {
				MtmySaleRelieve mtmySaleRelieve = new MtmySaleRelieve();
				mtmySaleRelieve.setId(id);
//				申请表中 status 申请状态（0：申请中；1：同意；2：拒绝）
				mtmySaleRelieve.setStatus(2);
				mtmySaleRelieve.setRemark("批量拒绝");
				User currentUser = UserUtils.getUser();
				mtmySaleRelieve.setCreateBy(currentUser);
				mtmySaleRelieveService.updateStatus(mtmySaleRelieve);
				//原始表中修改状态   需反查到userid  parentid
				mtmySaleRelieve = mtmySaleRelieveService.get(mtmySaleRelieve);
				mtmySaleRelieve.setStatus(0);
				mtmySaleRelieveService.updateBy(mtmySaleRelieve);
			}
			addMessage(redirectAttributes, "批量拒绝解除关系成功");
		} catch (Exception e) {
			logger.error("#####[分销-批量拒绝解除关系-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-批量拒绝解除关系", e);
			model.addAttribute("message", "批量拒绝解除关系出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmySale/list";
	}
	/**
	 * 分页查询所有A用户
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmySale:findAllA"},logical=Logical.OR)
	@RequestMapping(value = {"findAllA"})
	public String findAllA(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//查询所有A用户信息
 			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findAllA(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
			//查询邀请B用户上限
			MtmyRuleParam mtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_AB");
			model.addAttribute("mtmyRuleParam", mtmyRuleParam);
			//查询邀请C用户上限
			MtmyRuleParam ACmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_AC");
			model.addAttribute("ACmtmyRuleParam", ACmtmyRuleParam);
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			model.addAttribute("mtmySaleRelieve", mtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-分页查询A用户-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-分页查询A用户", e);
			model.addAttribute("message", "查询用户信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserA";
	}
	/**
	 * 查询A级用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	@RequestMapping(value = {"userFrom"})
	public String userFrom(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//A用户个人详情
			MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findByUserId(mtmySaleRelieve);
			model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
			//查询邀请B用户上限
			MtmyRuleParam mtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_AB");
			model.addAttribute("mtmyRuleParam", mtmyRuleParam);
			//查询邀请C用户上限
			MtmyRuleParam ACmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_AC");
			model.addAttribute("ACmtmyRuleParam", ACmtmyRuleParam);
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			//所有直接被A邀请的用户
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findByParentId(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[分销-查询A级用户详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询A级用户详情", e);
			model.addAttribute("message", "查询用户信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserAFrom";
	}
	/**
	 * 查询记录
	 * @param mtmySaleRelieveLog
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"userDetails"})
	public String userDetails(MtmySaleRelieveLog mtmySaleRelieveLog, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<MtmySaleRelieveLog> page = mtmySaleRelieveService.findUserDetails(new Page<MtmySaleRelieveLog>(request, response), mtmySaleRelieveLog);
			model.addAttribute("page", page);
			model.addAttribute("mtmySaleRelieveLog", mtmySaleRelieveLog);
		} catch (Exception e) {
			logger.error("#####[分销-查询记录-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询记录", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserDetails";
	}
	/**
	 * 收益明细
	 * @param mtmySaleRelieveLog
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"userEarnings"})
	public String findUserEarnings(MtmySaleRelieveLog mtmySaleRelieveLog, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<MtmySaleRelieveLog> page = mtmySaleRelieveService.findUserEarnings(new Page<MtmySaleRelieveLog>(request, response), mtmySaleRelieveLog);
			model.addAttribute("page", page);
			model.addAttribute("newMtmySaleRelieveLog", mtmySaleRelieveLog);
		} catch (Exception e) {
			logger.error("#####[分销-查询收益明细-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询收益明细", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserEarnings";
	}
	/**
	 * 查看明细
	 * @param mtmySaleRelieveLog
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"findUserAllOrder"})
	public String findUserAllOrder(MtmySaleRelieveLog mtmySaleRelieveLog, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<MtmySaleRelieveLog> page = mtmySaleRelieveService.findUserAllOrder(new Page<MtmySaleRelieveLog>(request, response), mtmySaleRelieveLog);
			model.addAttribute("page", page);
			model.addAttribute("newmtmySaleRelieveLog", mtmySaleRelieveLog);
		} catch (Exception e) {
			logger.error("#####[分销-查看明细-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查看明细", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserOrderDetails";
	}
	/**
	 * 分页查询所有B用户
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"findOtherUsers"})
	public String findOtherUsers(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//查询所有B级用户
			mtmySaleRelieve.setLayer("B");
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findOtherUsers(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			//查询邀请C用户上限
			MtmyRuleParam BCmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_BC");
			model.addAttribute("BCmtmyRuleParam", BCmtmyRuleParam);
			model.addAttribute("mtmySaleRelieve", mtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-分页查询B用户-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-分页查询B用户", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}  
		return "modules/ec/mtmySaleUserB";
	}
	/**
	 * 查询B级用户详情
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"otherUserFrom"})
	public String otherUserFrom(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//B级用户个人详情
			MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findOtherUsersByUserId(mtmySaleRelieve);
			model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
			//查询邀请C用户上限
			MtmyRuleParam BCmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_BC");
			model.addAttribute("BCmtmyRuleParam", BCmtmyRuleParam);
			//所有直接被B邀请的用户
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findByParentId(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[分销-查询B用户详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询B级的用户详情", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserBFrom";
	}
	

	
	/**
	 * 分页查询所有C用户
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"findAllC"})
	public String findAllC(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			mtmySaleRelieve.setLayer("C");
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findOtherUsers(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			//查询邀请D用户上限
			MtmyRuleParam CDmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_CD");
			model.addAttribute("CDmtmyRuleParam", CDmtmyRuleParam);
			model.addAttribute("mtmySaleRelieve", mtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-分页查询C用户-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-分页查询C用户", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserC";
	}
	/**
	 * 查询C级用户详情
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"CUserFrom"})
	public String CUserFrom(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findOtherUsersByUserId(mtmySaleRelieve);
			model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
			//查询邀请C用户上限
			MtmyRuleParam BCmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_CD");
			model.addAttribute("BCmtmyRuleParam", BCmtmyRuleParam);
			//A下级详情
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findByParentId(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.error("#####[分销-查询C级用户详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询C级用户详情", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserCFrom";
	}
	/**
	 * 分页查询所有D用户
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"findAllD"})
	public String findAllD(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			mtmySaleRelieve.setLayer("D");
			Page<MtmySaleRelieve> page = mtmySaleRelieveService.findOtherUsers(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
			model.addAttribute("page", page);
			//查询所有代言人
			List<MtmyRuleParam> SNmtmyRuleParam = mtmyRuleParamDao.findspokesman();
			model.addAttribute("SNmtmyRuleParam", SNmtmyRuleParam);
			
			model.addAttribute("mtmySaleRelieve", mtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-分页查询D用户-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-分页查询D用户", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserD";
	}
	/**
	 * 查询D级用户详情
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"DUserFrom"})
	public String DUserFrom(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findOtherUsersByUserId(mtmySaleRelieve);
			model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
		} catch (Exception e) {
			logger.error("#####[分销-查询D级用户详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查询D级用户详情", e);
			model.addAttribute("message", "查询信息出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySaleUserDFrom";
	}	
	
	
	
	/**
	 * 转移A用户关系
	 * @param mtmySaleRelieve
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"moveA"})
	public String moveA(MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//需将其下所有关联的用户来源改为 11（离职美容师）
			Users users = new Users();
			users.setUserid(mtmySaleRelieve.getUserId());
			users.setSource(11);
			mtmyUsersDao.relieveUser(users);
			addMessage(redirectAttributes, "转移用户关系成功");
		} catch (Exception e) {
			logger.error("#####[分销-转移A用户关系-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-转移A用户关系", e);
			model.addAttribute("message", "转移关系出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmySale/findAllA";
	}
	/**
	 * 转移其他用户关系
	 * @param moveType
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"move"})
	public String move(String moveType,MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			if(mtmySaleRelieve.getNewParentId() != "" && mtmySaleRelieve.getNewParentId() != null){
				mtmySaleRelieveService.updateParentId(mtmySaleRelieve);
				//转移用户关系后  将用户来源全部改为13（北京代言人）
				Users users = new Users();
				users.setUserid(mtmySaleRelieve.getUserId());
				users.setSource(13);
				mtmyUsersDao.relieveUser(users);
				addMessage(redirectAttributes, "转移用户关系成功");
			}else{
				addMessage(redirectAttributes, "转移关系出现异常，请与管理员联系");
			}
		} catch (Exception e) {
			logger.error("#####[分销-转移关系-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-转移关系", e);
			model.addAttribute("message", "转移关系出现异常，请与管理员联系");
		}
		if("AFrom".equals(moveType)){
			//A用户下级用户
			return "redirect:" + adminPath + "/ec/mtmySale/userFrom?users.userid="+mtmySaleRelieve.getParentId();
		}else if("BList".equals(moveType)){
			//所有B级用户
			return "redirect:" + adminPath + "/ec/mtmySale/findOtherUsers";
		}else if("BFrom".equals(moveType)){
			//B用户下级用户
			return "redirect:" + adminPath + "/ec/mtmySale/otherUserFrom?users.userid="+mtmySaleRelieve.getParentId();
		}else if("CList".equals(moveType)){
			//所有C级用户
			return "redirect:" + adminPath + "/ec/mtmySale/findAllC";
		}else if("CFrom".equals(moveType)){
			//C用户下级用户
			return "redirect:" + adminPath + "/ec/mtmySale/CUserFrom?users.userid="+mtmySaleRelieve.getParentId();
		}else if("DList".equals(moveType)){
			//所有D级用户
			return "redirect:" + adminPath + "/ec/mtmySale/findAllD";
		}
		return "";
	}
	
	@RequestMapping(value = {"findChild"})
	public String findChild(String moveType,MtmySaleRelieve mtmySaleRelieve, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			if("B".equals(moveType)){
				return "redirect:" + adminPath + "/ec/mtmySale/otherUserFrom?users.userid="+mtmySaleRelieve.getUsers().getUserid();
			}else if("AB".equals(moveType)){
				return "redirect:" + adminPath + "/ec/mtmySale/CUserFrom?users.userid="+mtmySaleRelieve.getUsers().getUserid();
			}else if("C".equals(moveType)){
				return "redirect:" + adminPath + "/ec/mtmySale/DUserFrom?users.userid="+mtmySaleRelieve.getUsers().getUserid();
			}
		} catch (Exception e) {
			logger.error("#####[分销-查看子类用户信息-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "分销-查看子类用户信息", e);
			model.addAttribute("message", "查看子类用户信息出现异常，请与管理员联系");
		}
		return "";
	}
	
	/**
	 * 导出A级用户
	 * @param mtmySaleRelieveExport
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="export")
	public String exportFile(MtmySaleRelieveExport mtmySaleRelieveExport,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<MtmySaleRelieveExport> page = mtmySaleRelieveService.exportFile(new Page<MtmySaleRelieveExport>(request, response,-1), mtmySaleRelieveExport);
			new ExportExcel("用户数据", MtmySaleRelieveExport.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/mtmySale/findAllA";
	}
}

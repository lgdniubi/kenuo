package com.training.modules.crm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.dao.MtmyRuleParamDao;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.MtmySaleRelieveService;
import com.training.modules.ec.service.MtmyUsersService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * kenuo 邀请明细 @description：
 * 
 * @author：sharp @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/invitation")
public class InvitationController extends BaseController {
	
	@Autowired
	private MtmySaleRelieveService mtmySaleRelieveService;
	@Autowired
	private MtmyRuleParamDao mtmyRuleParamDao;
	@Autowired
	private MtmyUsersService mtmyUsersService;
	@Autowired
	private UserDetailService userDetailService;
	
	/**
	 * 默认返回用户信息
	 * @param 
	 * @return UserDetail
	 */
	@ModelAttribute
	public UserDetail  get(@RequestParam(required = false) String userId) {
		if (StringUtils.isNotBlank(userId)) {
			UserDetail detail=  userDetailService.getUserNickname(userId); 
		    return detail;
		} else {
			return new UserDetail();
		}
	}
	
	/**
	 * 按照等级查询用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String chooseForm(String userId, String franchiseeId, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Users users= mtmyUsersService.get(userId);
			String layer = users.getLayer();
			MtmySaleRelieve mtmySaleRelieve = new MtmySaleRelieve();
			mtmySaleRelieve.setUsers(users);
			if ("A".equals(layer)) {
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
					//传回UserId
					model.addAttribute("userId",userId);
					model.addAttribute("franchiseeId",franchiseeId);
				} catch (Exception e) {
					logger.error("#####[分销-查询A级用户详情-出现异常：]"+e.getMessage());
					BugLogUtils.saveBugLog(request, "分销-查询A级用户详情", e);
					model.addAttribute("message", "查询用户信息出现异常，请与管理员联系");
				}
				return "modules/crm/mtmySaleUserAFrom";
			}
			if ("B".equals(layer)) {
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
					//传回UserId
					model.addAttribute("userId",userId);
					model.addAttribute("franchiseeId",franchiseeId);
				} catch (Exception e) {
					logger.error("#####[分销-查询B用户详情-出现异常：]"+e.getMessage());
					BugLogUtils.saveBugLog(request, "分销-查询B级的用户详情", e);
					model.addAttribute("message", "查询信息出现异常，请与管理员联系");
				}
				return "modules/crm/mtmySaleUserBFrom";
			}
			if ("C".equals(layer)) {
				try {
					MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findOtherUsersByUserId(mtmySaleRelieve);
					model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
					//查询邀请C用户上限
					MtmyRuleParam BCmtmyRuleParam=mtmyRuleParamDao.findProByKey("sale_invite_userNum_CD");
					model.addAttribute("BCmtmyRuleParam", BCmtmyRuleParam);
					//A下级详情
					Page<MtmySaleRelieve> page = mtmySaleRelieveService.findByParentId(new Page<MtmySaleRelieve>(request, response), mtmySaleRelieve);
					model.addAttribute("page", page);
					//传回UserId
					model.addAttribute("userId",userId);
					model.addAttribute("franchiseeId",franchiseeId);
				} catch (Exception e) {
					logger.error("#####[分销-查询C级用户详情-出现异常：]"+e.getMessage());
					BugLogUtils.saveBugLog(request, "分销-查询C级用户详情", e);
					model.addAttribute("message", "查询信息出现异常，请与管理员联系");
				}
				return "modules/crm/mtmySaleUserCFrom";
			}
			if ("D".equals(layer)) {
				try {
					MtmySaleRelieve newMtmySaleRelieve = mtmySaleRelieveService.findOtherUsersByUserId(mtmySaleRelieve);
					model.addAttribute("mtmySaleRelieve", newMtmySaleRelieve);
					//传回UserId
					model.addAttribute("userId",userId);
					model.addAttribute("franchiseeId",franchiseeId);
				} catch (Exception e) {
					logger.error("#####[分销-查询D级用户详情-出现异常：]"+e.getMessage());
					BugLogUtils.saveBugLog(request, "分销-查询D级用户详情", e);
					model.addAttribute("message", "查询信息出现异常，请与管理员联系");
				}
				return "modules/crm/mtmySaleUserDFrom";
			}
			model.addAttribute("userId",userId);
			model.addAttribute("franchiseeId",franchiseeId);
			return "modules/crm/mtmyUserZFrom";
		} catch (Exception e) {
			logger.error("#####[跳转到对应的用户邀请详情-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "跳转到用户邀请详情", e);
			model.addAttribute("message", "查询用户信息出现异常，请与管理员联系");
			return "";
		}
	}
}

/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.utils.SendNotifyUtils;

/**
 * 用户管理：pt,syr,qy
 * @author: jingfeng
 * @date 2018年5月11日下午5:45:36
 */
@Controller
@RequestMapping(value = "${adminPath}/train/specialUser")
public class SpecialUserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	
	
	@ModelAttribute
	public User get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getUser(id);
		} else {
			return new User();
		}
	}

	@RequiresPermissions("train:specialUser:list")
	@RequestMapping(value = "list")
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findSpecialUser(new Page<User>(request, response), user);
		model.addAttribute("page", page);
		return "modules/train/specialUserList";
	}
	
		
	@RequestMapping(value = "resetPassword")
	public String resetPassword(User user, Model model) {
		model.addAttribute("user", user);
		return "modules/train/resetPassword";
	}

	/**
	 * 修改密码
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:reset")
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String newPassword = user.getNewPassword();
		try {
			systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
			SendNotifyUtils.sendSMS(user.getMobile(), user.getNickname(), newPassword);
			redisClientTemplate.del("UTOKEN_"+user.getId());
			addMessage(redirectAttributes, "修改密码'" + newPassword + "'成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "修改密码失败！");
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存用户失败错误信息", e);
		}
		return "redirect:" + adminPath + "/train/specialUser/list?repage";
	}
	/**
	 * 冻结用户
	 * 如果是pt/syr 就改del_flag=1，如果是qy改train_model_franchisee的del_flag=1
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "sys:user:freeze", "sys:user:unfreeze" }, logical = Logical.OR)
	@RequestMapping(value = "freeze")
	public String freeze(User user,Integer opflag, RedirectAttributes redirectAttributes) {
		try {
			/*String type = user.getType();
			if ("qy".equals(type)){
				systemService.updateModelFranchisee(user,opflag);
			}else{
			}*/
			
			systemService.updateUserDel(user,opflag);
			addMessage(redirectAttributes, "成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败！");
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/specialUser/list?repage";
	}
	
	/**
	 *冻结原因 
	 * @param id
	 * @param opflag
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "freezeReason")
	public String freezeReason(String id,String opflag, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("opflag", opflag);
		return "modules/train/freezeReason";
	}
	
	
}


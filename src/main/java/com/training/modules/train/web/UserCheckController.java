package com.training.modules.train.web;

import java.io.IOException;
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

import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.track.thread.TrackThread;
import com.training.common.utils.DateUtils;
import com.training.common.web.BaseController;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.UserCheck;
import com.training.modules.train.service.UserCheckService;

/**
 * 用户审核
 * @author jingfeng
 *2018-03-26
 */
@Controller
@RequestMapping(value = "${adminPath}/train/userCheck")
public class UserCheckController extends BaseController{
	
	@Autowired
	private UserCheckService userCheckService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	
	/**
	 * 用户审核
	 * @param userCheck
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:userCheck:findalllist"},logical=Logical.OR)
	@RequestMapping(value = {"findalllist", ""})
	public String findalllist(UserCheck userCheck,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		Page<UserCheck> page = userCheckService.findList(new Page<UserCheck>(request, response), userCheck);
		model.addAttribute("page", page);
		model.addAttribute("status", userCheck.getStatus());
		addMessage(redirectAttributes, "成功");
		return "modules/train/userCheckList";
	}
	
	
	/**
	 * 用户审核，通过改状态发消息
	 * @param userCheck
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequiresPermissions(value={"train:userCheck:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String savemodel(UserCheck userCheck,Integer pageNo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		boolean flag = false;
		try {
			userCheckService.saveModel(userCheck);//保存审核信息保存用户审核状态
			if ("1".equals(userCheck.getStatus())){
				String text = creatText(userCheck);
//				userCheckService.pushMsg(userCheck,text);
			}else if ("2".equals(userCheck.getStatus())){
				//审核通过，发送消息并，更新用户type
//				userCheckService.pushMsg(userCheck,"你的申请资料信息已通过审核，等待平台给您赋予权限");
//				userCheckService.updateTypeAndpushMsg(userCheck,"你的申请资料信息已通过审核，等待平台给您赋予权限");
			}
//			flag = true;
			addMessage(redirectAttributes, "成功");
			
			/*##########[神策埋点{user_authent_win|user_authent_Loser}-Begin]##########*/
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			if ("1".equals(userCheck.getStatus())){
				// 1：审核不通过
				inParamMap.put("METHOD_NAME", "user_authent_Loser");
			}else if("2".equals(userCheck.getStatus())) {
				// 2:审核通过
				inParamMap.put("METHOD_NAME", "user_authent_win");
			}
			// 用户ID
			inParamMap.put("DISTINCT_ID", userCheck.getUserid());
			// 订单ID
			inParamMap.put("APPLY_ID", userCheck.getId());
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(inParamMap));
			/*##########[神策埋点end]##########*/
			
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存审核信息出现异常,请与管理员联系");
			logger.error("保存审核信息异常,异常信息为："+e);
		}
//		return flag;
		return "redirect:" + adminPath + "/train/userCheck/findalllist?pageNo="+pageNo;
	}
	//创建发送拒绝审核通知消息：您申请认证手艺人用户未被通过，原因如下：====您申请认证企业用户未被通过，原因如下：
	private String creatText(UserCheck userCheck) {
		StringBuilder sb = new StringBuilder();
		String str = "";
		if ("syr".equals(userCheck.getAuditType())){
			str = "认证手艺人";
		}else{
			str = "认证企业";
		}
		sb.append("您申请");
		sb.append(str);
		sb.append("用户未被通过，原因如下：");
		sb.append(userCheck.getRemarks());
		return sb.toString();
	}


	@RequestMapping(value = {"refuseForm"})
	public String refuse(String id,String userid,String status,String auditType, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("userid", userid);
		model.addAttribute("status", status);
		model.addAttribute("auditType", auditType);
		return "modules/train/refuseForm";
	}
	
	/**
	 * 
	 * @param modelFranchisee
	 * @param opflag
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 * @Description:保存权限设置信息
	 */
	@RequiresPermissions(value={"train:userCheck:save"},logical=Logical.OR)
	@RequestMapping(value = {"saveFranchise"})
	public String saveFranchise(ModelFranchisee modelFranchisee,String opflag,Integer pageNo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		if((DateUtils.formatDate(modelFranchisee.getAuthEndDate(), "yyyy-MM-dd").compareTo(DateUtils.getDate()))<0){
			addMessage(redirectAttributes, "授权时间无效，请重新选择授权时间");
			return "redirect:" + adminPath + "/train/userCheck/findalllist";
		}
		UserCheck userCheck = new UserCheck();
		userCheck.setId(modelFranchisee.getApplyid());
		userCheck.setUserid(modelFranchisee.getUserid());
		userCheck.setAuditType(opflag);
		UserCheck find = userCheckService.getUserCheck(userCheck);
		boolean flag = "qy".equals(userCheck.getType()) && Integer.valueOf(userCheck.getStatus())==4;
		boolean flagsyr = "syr".equals(opflag) && "qy".equals(find.getType());
		//缓存锁
		RedisLock redisLock = new RedisLock(redisClientTemplate, "fzx_mobile_"+userCheck.getMobile());
		try {
			redisLock.lock();
			if(flag || flagsyr){
				addMessage(redirectAttributes, "该用户已经是企业用户，不能进行操作");
			}else{
				if ("syr".equals(opflag)){
					modelFranchisee.setFranchiseeid("0");
					modelFranchisee.setPaytype("0");
					ModelFranchisee modelSelect = userCheckService.getModelFranchiseeByUserid(modelFranchisee.getUserid());
					if(modelSelect == null){modelFranchisee.setId(null);}
					userCheckService.saveModelFranchisee(modelFranchisee);//保存手艺人权益信息
					userCheckService.pushMsg(modelFranchisee,modelSelect,opflag);//重新授权成功发送消息
					userCheckService.pushMsg(userCheck, "您已具备手艺人用户的权益，开启新旅程吧。");//授权成功发送消息
				}else if ("qy".equals(opflag)){
					ModelFranchisee modelSelect = userCheckService.getQYModelFranchiseeByUserid(modelFranchisee.getUserid());
					if(modelSelect == null){modelFranchisee.setId(null);}
					userCheckService.saveQYModelFranchisee(modelFranchisee,find);//保存企业权益信息
					userCheckService.pushMsg(modelFranchisee,modelSelect,opflag);//重新授权成功发送消息
					userCheckService.pushMsg(userCheck, "您已具备企业用户的权益，开启新旅程吧。");//授权成功发送消息
				}
				redisClientTemplate.del("UTOKEN_"+modelFranchisee.getUserid());
				addMessage(redirectAttributes, "成功");
			}
			
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存权限设置出现异常,请与管理员联系");
			logger.error("保存权限设置异常,异常信息为："+e);
		}finally {
			redisLock.unlock();
		}
		return "redirect:" + adminPath + "/train/userCheck/findalllist?pageNo="+pageNo;
	}
	
	
	/**
	 * 用户审核
	 * @param userCheck
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:userCheck:update",},logical=Logical.OR)
	@RequestMapping(value={"form"})
	public String form(UserCheck userCheck,Model model,String opflag,Integer pageNo){
		model.addAttribute("pageNo", pageNo);
		if ("setPermiss".equals(opflag)){
			if("syr".equals(userCheck.getType())){
				ModelFranchisee modelFranchisee = userCheckService.getModelFranchiseeByUserid(userCheck.getUserid());
				model.addAttribute("modelFranchisee",modelFranchisee);
				model.addAttribute("userid",userCheck.getUserid());
				model.addAttribute("applyid",userCheck.getId());
				return "modules/train/syrForm";
			}
			if("qy".equals(userCheck.getType())){
				ModelFranchisee modelFranchisee = userCheckService.getQYModelFranchiseeByUserid(userCheck.getUserid());
				if(modelFranchisee == null){
					modelFranchisee = new ModelFranchisee();
				}
				modelFranchisee.setApplyid(userCheck.getId());
				modelFranchisee.setUserid(userCheck.getUserid());
				model.addAttribute("modelFranchisee",modelFranchisee);
				return "modules/train/qyForm";
			}
		}
		if (userCheck.getId() != null) {
			userCheck =  userCheckService.getUserCheck(userCheck);
			if("syr".equals(userCheck.getAuditType())){//如果认证类型是手艺人，跳转收益审核页面
				model.addAttribute("userCheck", userCheck);
				return "modules/train/syrCheckForm";
			}
		}
		model.addAttribute("userCheck", userCheck);
		return "modules/train/userCheckForm";
	}
	/**
	 * 审核并授权
	 * @param userCheck
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:userCheck:update",},logical=Logical.OR)
	@RequestMapping(value={"authForm"})
	public String authForm(UserCheck userCheck,Model model,String opflag,Integer pageNo){
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("applyid", userCheck.getId());
//		if ("setPermiss".equals(opflag)){
//			
//		}
		if (userCheck.getId() != null) {
			userCheck =  userCheckService.getUserCheck(userCheck);
			if("syr".equals(userCheck.getAuditType())){//如果认证类型是手艺人，跳转收益审核页面
				model.addAttribute("userCheck", userCheck);
				return "modules/train/syrAuthForm";
			}
		}
		model.addAttribute("userCheck", userCheck);
		return "modules/train/qyAuthForm";
	}
	
	/**
	 * 判断是否可以进行权限设置
	 * 如果该用户同意其他商家邀请，就不能操作。
	 * @param userid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"isPermiss"})
	public boolean isPermiss(String userid){
		int count = userCheckService.isPermiss(userid);
		if (count == 0) return true;
		return false;
	}
	
	/**
	 * 查询权益日志
	 * @param id 权益id
	 * @return
	 */
	@RequestMapping(value={"log"})
	public String modelLog(String id,Model model,HttpServletRequest request, HttpServletResponse response){
		ModelFranchisee mf = new ModelFranchisee();
		mf.setId(id);
		Page<ModelFranchisee> page = userCheckService.findModelLogList(new Page<ModelFranchisee>(request, response), mf);
		model.addAttribute("page", page);
//		List<ModelFranchisee> mList = userCheckService.findModelLogById(id);
//		model.addAttribute("mList", mList);
		return "modules/train/mLogList";
	}
	
}

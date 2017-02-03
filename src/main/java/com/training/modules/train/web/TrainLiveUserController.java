package com.training.modules.train.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.beanvalidator.BeanValidators;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.LiveUser;
import com.training.modules.train.entity.TrainLiveUser;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.service.TrainLiveUserService;

@Controller
@RequestMapping(value = "${adminPath}/train/liveUser")
public class TrainLiveUserController extends BaseController{
	

	@Autowired
	private TrainLiveUserService trainLiveUserService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	
	
	@ModelAttribute
	public TrainLiveUser get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return trainLiveUserService.get(id);
		} else {
			return new TrainLiveUser();
		}
	}
	
	/**
	 * 分页查询 条件查询
	 * @param trainLiveAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:liveUser:view")
	@RequestMapping(value = { "list", "" })
	public String list(TrainLiveUser trainLiveUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainLiveUser> page = trainLiveUserService.findLiveUser(new Page<TrainLiveUser>(request, response), trainLiveUser);
		model.addAttribute("page", page);
		return "modules/train/liveUserList";
	}
	

	/**
	 * 编辑直播
	 * @param request
	 * @param trainLiveUser
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,TrainLiveUser trainLiveUser, Model model) {
		try{
			model.addAttribute("trainLiveUser", trainLiveUser);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播会员编辑", e);
			logger.error("直播会员编辑：" + e.getMessage());
		}

		return "modules/train/liveUserForm";
	}
	
	/**
	 * 编辑直播
	 * @param request
	 * @param trainLiveUser
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "editform")
	public String editform(HttpServletRequest request,TrainLiveUser trainLiveUser, Model model) {
		try{
			model.addAttribute("trainLiveUser", trainLiveUser);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播会员编辑", e);
			logger.error("直播会员编辑：" + e.getMessage());
		}

		return "modules/train/liveUeditForm";
	}
	
	
	
	/**
	 * 保存数据
	 * 
	 * @param trainLiveAudit
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "save")
	public String save(TrainLiveUser trainLiveUser , HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			if(trainLiveUser.getId().length()==0){
				User sysUser=trainLiveUserService.findByMobile(trainLiveUser.getMobile());
				if(null!=sysUser){
					trainLiveUser.setUserId(sysUser.getId());
					trainLiveUser.setName(sysUser.getName());
					trainLiveUser.setCreateBy(UserUtils.getUser());
					trainLiveUserService.insertLiveUser(trainLiveUser);
				}
			}else{
				trainLiveUserService.update(trainLiveUser);
			}
			addMessage(redirectAttributes, "直播会员保存成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播会员保存失败", e);
			logger.error("方法：save，直播会员：" + e.getMessage());
			addMessage(redirectAttributes, "直播会员保存失败");
		}

		return "redirect:" + adminPath + "/train/liveUser/list";

	}
	
	/**
	 * 删除数据
	 * @param trainLiveUser
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(TrainLiveUser trainLiveUser , HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			trainLiveUserService.deleteUser(trainLiveUser);
			addMessage(redirectAttributes, "删除成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播会员删除失败", e);
			logger.error("方法：save，直播会员：" + e.getMessage());
			addMessage(redirectAttributes, "直播会员删除失败");
		}

		return "redirect:" + adminPath + "/train/liveUser/list";

	}
	/**
	 * 跳转到导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/train/importLiveUser";
	}
	
	/**
	 * 下载导入物流数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(TrainLiveUser trainLiveUser,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "userVipImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[会员导入模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[会员导入模板-new-path"+path);
			File file = new File(path);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导入模板下载失败", e);
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/train/liveUser/liveUserList";
	}
	
	
	
	/**
	 * 导入会员数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import")
	public String importTrainLiveUser(MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) {
	
		try {
			int successNum = 0;				//成功数
			int failureNum = 0;				//失败数
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			User loginUser=UserUtils.getUser();
			List<LiveUser> list = ei.getDataList(LiveUser.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			
			for (LiveUser liveUser : list){
				try {
					if (liveUser.getMobile() == null) {		//验证手机号不能为空
						failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，手机号不能为空");
					}else{
						BeanValidators.validateWithException(validator, liveUser);
						if(liveUser.getAuditId()!=null){		//验证直播ID 不能为空
							User sysUser=trainLiveUserService.findByMobile(liveUser.getMobile());
							if(null==sysUser){				//验证手机号不存在
								failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，用户不存在");
							}else{
								TrainLiveUser trainLiveUser=new TrainLiveUser();
								trainLiveUser.setUserId(sysUser.getId());
								trainLiveUser.setAuditId(Integer.parseInt(liveUser.getAuditId()));
								trainLiveUser.setName(sysUser.getName());
								trainLiveUser.setMobile(liveUser.getMobile());
								trainLiveUser.setMoney(Double.parseDouble(liveUser.getMoney()));
								trainLiveUser.setRemak(liveUser.getRemak());
								trainLiveUser.setPayment(liveUser.getPayment());
								trainLiveUser.setValidityDate(sdf.parse(liveUser.getValidityDate()));
								trainLiveUser.setCreateBy(loginUser);
								int index = trainLiveUserService.insertLiveUser(trainLiveUser);
								if (index > 0) {		
									successNum++;
								} else {
									failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，保存失败");
									failureNum++;
								}	
							}
						}else{
							failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，直播ID不能不为空");
						}
					}
				} catch (ConstraintViolationException ex) {
					logger.error("导入会员数据："+ex.getMessage());
					BugLogUtils.saveBugLog(request, "导入会员数据", ex);
					failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，出现异常");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ":");
					for (String message : messageList) {
						failureMsg.append(message + ";");
						failureNum++;
					}
				} catch (Exception ex) {
					BugLogUtils.saveBugLog(request, "导入会员数据", ex);
					logger.error("导入物流出错："+ex.getMessage());
					failureMsg.append("<br/>姓名："+liveUser.getName()+"  手机号：" + liveUser.getMobile() + "，出现异常");
					failureNum++;
				}
			}
				
			
			if (failureNum > 0) {
				failureMsg.insert(0, "失败 " + failureNum + " 条，会员导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入: " + successNum + " 条。" + failureMsg);
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导入会员数据", e);
			logger.error("导入直播会员："+e.getMessage());
			addMessage(redirectAttributes, "导入直播会员！失败" );
		}
		return "redirect:" + adminPath + "/train/liveUser/list";
	}
	
	/**
	 * 查询用户Name
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getUserName")
	public String getUserName(String mobile, HttpServletRequest request,HttpServletResponse response) {
		String diString="";
		User sysUser=trainLiveUserService.findByMobile(mobile);
		if(sysUser!=null){
			diString=sysUser.getName();
		}
		
		return diString;
	}
	

}

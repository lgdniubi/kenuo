/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.beanvalidator.BeanValidators;
import com.training.common.config.Global;
import com.training.common.json.AjaxJson;
import com.training.common.persistence.Page;
import com.training.common.service.ServiceException;
import com.training.common.utils.DateUtils;
import com.training.common.utils.FileUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Users;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.RoleDao;
import com.training.modules.sys.dao.SpecBeauticianDao;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Dict;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.entity.UserDelete;
import com.training.modules.sys.entity.UserLog;
import com.training.modules.sys.service.AreaService;
import com.training.modules.sys.service.DictService;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.SMSUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.tools.utils.TwoDimensionCode;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

/**
 * 用户Controller
 * 
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaService areaService;
	@Autowired
	private DictService dictService;
	@Autowired
	private ReservationDao reservationDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	@Autowired
	private SpecBeauticianDao specBeauticianDao;	//特殊美容师
	
	@ModelAttribute
	public User get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getUser(id);
		} else {
			return new User();
		}
	}

	@RequiresPermissions("sys:user:index")
	@RequestMapping(value = { "index" })
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	@RequiresPermissions("sys:user:index")
	@RequestMapping(value = { "list", "" })
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		model.addAttribute("page", page);
		return "modules/sys/userList";
	}
	
		
	@RequiresPermissions(value = { "sys:user:view", "sys:user:add", "sys:user:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		List<UserLog> userLogs = new ArrayList<UserLog>();
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}
		if (user.getUserinfo() == null || user.getUserinfo().getId() == null) {
			user.setUserinfo(systemService.getUserInfoByUserId(user.getId()));		
		}
		if (user.getSpeciality() == null || user.getSpeciality().getId() == null) {
			user.setSpeciality(systemService.getSpecialityByUserId(user.getId()));
		}
		if (user.getArealist() == null) {
			user.setArealist(areatreeData());
		}
		if(user.getSkill() == null || String.valueOf(user.getSkill().getSkillId()) == null){
			user.setSkill(systemService.getSkillByUserId(user.getId()));
		}
		if (user.getId() != null) {
			userLogs = systemService.findUserLog(user.getId());
			Dict dict = new Dict();
			dict.setValue(user.getUserType());
			dict.setType("sys_user_type");
			model.addAttribute("dict", dictService.findDict(dict));
		}
		
		model.addAttribute("userLogs", userLogs);
		model.addAttribute("user", user);
		//默认给美容师角色
		Role role = new Role();
		role.setName("美容师");
		role = roleDao.getByNameNew(role);
		model.addAttribute("role", role);
		/*model.addAttribute("allRoles", systemService.findAllRole());  2016年11月9日17:54:02   修改新数据权限   咖啡*/
		return "modules/sys/userForm";
	}

	/**
	 * 查询日志详情
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userLogForm")
	public String userLogForm(UserLog userLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserLog> page = systemService.findUserLogList(new Page<UserLog>(request, response), userLog);
		model.addAttribute("page", page);
		model.addAttribute("userId", userLog.getUserId());
		return "modules/sys/userLogForm";
	}
	
	/**
	 * 创建订单时根据手机号获取用户信息
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifyPhone" )
	public String verifyPhone(String mobile){
		
		if (mobile.length()>0){
			if(systemService.getUserByPhone(mobile).size()>0) {
				return "true";
			}
			
		}
		return "false";
	}
	
	/**
	 * 查看用户删除详情
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delForm")
	public String delForm(User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}
		if (user.getUserinfo() == null || user.getUserinfo().getId() == null) {
			user.setUserinfo(systemService.getUserInfoByUserId(user.getId()));
		}
		if (user.getSpeciality() == null || user.getSpeciality().getId() == null) {
			user.setSpeciality(systemService.getSpecialityByUserId(user.getId()));
		}
		if (user.getArealist() == null) {

			user.setArealist(areatreeData());
		}
		model.addAttribute("allRoles", systemService.findAllRole());
		model.addAttribute("user", user);
		return "modules/sys/userDelForm";
	}
	
	/**
	 * 
	 * @param user
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "sys:user:add", "sys:user:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			if(null != user.getId() && "" != user.getId()){
				User users = userDao.get(user.getId());
				if(null != users){
					if("2".equals(users.getUserType())){
						if(0 != reservationDao.findCountById(user.getId())){
							if(!user.getOffice().getId().equals(users.getOffice().getId()) || !user.getUserType().equals(users.getUserType())){
								addMessage(redirectAttributes, "修改用户失败,此美容师有预约，不允许修改其关键信息");
								UserUtils.clearCache(user);
								return "redirect:" + adminPath + "/sys/user/list?repage";
							}
						}
					}
				}
			}
			//新增用户时  验证其用户是否存在于每天美耶中
			if(user.getId().length() <= 0){
				if(mtmyUsersDao.findByMobile(user) != null){
					addMessage(redirectAttributes, "保存用户失败,此手机号已在每天美耶注册,请联系管理员");
					UserUtils.clearCache(user);
					return "redirect:" + adminPath + "/sys/user/list?repage";
				}
			}
			// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
			user.setCompany(new Office(request.getParameter("company.id")));
			user.setOffice(new Office(request.getParameter("office.id")));
			
			/*更新用户时，需要更新用户的TOKEN，用户妃子校*/
			redisClientTemplate.hdel("USERTOKEN", user.getId());
			
			// 由于导入功能增加code为必填 beanValidator验证报code不能为空
			user.setCode("1");

			// 如果新密码为空，则不更换密码
			if (StringUtils.isNotBlank(user.getNewPassword())) {
				user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
			}
			if (!beanValidator(model, user)) {
				return form(user, model);
			}
			if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
				addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
				return form(user, model);
			}
			// 角色数据有效性验证，过滤不在授权内的角色
			/*	保存修改用户信息时  对用户角色不操作（在权限设置里操作）    2016年12月19日   
		 	List<Role> roleList = Lists.newArrayList();
			List<String> roleIdList = user.getRoleIdList();
			for (Role r : systemService.findAllRole()) {
				if (roleIdList.contains(r.getId())) {
					roleList.add(r);
				}
			}
			user.setRoleList(roleList);*/

			// 生成用户二维码，使用登录名
			String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + user.getId() + "/qrcode/";
			FileUtils.createDirectory(realPath);
			String name = user.getId() + ".png"; // encoderImgId此处二维码的图片名
			String filePath = realPath + name; // 存放路径
			TwoDimensionCode.encoderQRCode(user.getLoginName(), filePath, "png");// 执行生成二维码
			user.setQrCode(request.getContextPath() + Global.USERFILES_BASE_URL + user.getId() + "/qrcode/" + name);
			// 保存用户信息
			systemService.saveUser(user);
			UserUtils.clearCache(user);		//清除指定用户缓存
			addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
		} catch (Exception e) {
			// TODO: handle exception
			UserUtils.clearCache(user);		//清除指定用户缓存
			addMessage(redirectAttributes, "保存用户失败！");
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存用户失败错误信息", e);
		}
		
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	/**
	 * 
	 * @param user
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:del")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if(reservationDao.findCountById(user.getId()) != 0){
			addMessage(redirectAttributes, "删除用户失败, 当前用户存在未完成的预约");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if(specBeauticianDao.findSpecBeautician(user.getId()) != 0){
			addMessage(redirectAttributes, "删除用户失败,当前用户属于特殊美容师,请先从特殊美容师列表删除");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if (UserUtils.getUser().getId().equals(user.getId())) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
		} else if (User.isAdmin(user.getId())) {
			addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
		} else {
			user.preUpdate();
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "删除用户成功");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 批量删除用户
	 */
	@RequiresPermissions("sys:user:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		int successNum = 0;
		int failureNum = 0;
		StringBuilder failureMsg = new StringBuilder();
		
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			User user = systemService.getUser(id);
			if (Global.isDemoMode()) {
				addMessage(redirectAttributes, "演示模式，不允许操作！");
				return "redirect:" + adminPath + "/sys/user/list?repage";
			}
			//验证用户是否有未完成的预约
			if(reservationDao.findCountById(id) != 0){
				failureMsg.append("<br/>删除用户"+user.getLoginName()+"失败 , 当前用户存在未完成的预约");
				failureNum++;
			}else if(specBeauticianDao.findSpecBeautician(user.getId()) != 0){
				failureMsg.append("<br/>删除用户"+user.getLoginName()+"失败 ,当前用户属于特殊美容师,请先从特殊美容师列表删除");
				failureNum++;
			}else if (UserUtils.getUser().getId().equals(user.getId())) {
				failureMsg.append("<br/>删除用户"+user.getLoginName()+"失败 , 不允许删除当前用户");
				failureNum++;
			} else if (User.isAdmin(user.getId())) {
				failureMsg.append("<br/>删除用户"+user.getLoginName()+"失败 , 不允许删除超级管理员用户");
				failureNum++;
			} else {
				systemService.deleteUser(user);
				successNum++;
			//	addMessage(redirectAttributes, "删除用户成功");
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "，失败 " + failureNum + " 条用户，删除信息如下：");
		}
		addMessage(redirectAttributes, "已成功删除 " + successNum + " 条用户" + failureMsg);
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	/**
	 * 用户数据权限跳转
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "auth")
	public String auth(User user,Model model){
		User u = userDao.findAuth(user);
		if(u != null ){
			user.setOfficeIdList(u.getOfficeIdList());
		}
		model.addAttribute("user", user);
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/userAuth";
	}
	/**
	 * 保存用户数据权限
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveAuth")
	public String saveAuth(User user,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		try {
			
			// 角色数据有效性验证，过滤不在授权内的角色
			List<Role> roleList = Lists.newArrayList();
			List<String> roleIdList = user.getRoleIdList();
			for (Role r : systemService.findAllRole()) {
				if (roleIdList.contains(r.getId())) {
					roleList.add(r);
				}
			}
			user.setRoleList(roleList);
			
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			// 更新用户与数据权限关联
			userDao.deleteUserOffice(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			
			if(1 == user.getDataScope()){
				user.preUpdate();
				userDao.UpdateDataScope(user);
				addMessage(redirectAttributes, "保存用户数据权限成功");
			}else if(2 == user.getDataScope()){
				user.preUpdate();
				userDao.UpdateDataScope(user);
				userDao.insertDataScope(user);
				addMessage(redirectAttributes, "保存用户数据权限成功");
			}else{
				addMessage(redirectAttributes, "保存出现异常，请与管理员联系");
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
		} catch (Exception e) {
			logger.error("#####[保存用户数据权限-出现异常：]"+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存用户数据权限", e);
			model.addAttribute("message", "保存出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	/**
	 * 导出用户数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(User user, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
			new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 跳转到导入页面
	 * 
	 * @return
	 */
	@RequiresPermissions("sys:user:importPage")
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/sys/importExcel";
	}

	/**
	 * 跳转到导入删除页面
	 * 
	 * @return
	 */
	@RequiresPermissions("sys:user:importPage")
	@RequestMapping(value = "importDelete")
	public String importExcelDelete() {
		return "modules/sys/importExcelDelete";
	}

	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:import")
	@RequestMapping(value = "import")
	public String importFile(HttpServletRequest request,MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			if (ei.getLastCellNum() != 12) {
				failureMsg.append("<br/>导入的模板错误，请检查模板; ");
			} else {
				List<User> list = ei.getDataList(User.class);

				for (User user : list) {
					try {

						if (user.getCode() == null) {
							break;
						}
						BeanValidators.validateWithException(validator, user);
						if ("true".equals(checkLoginName("", user.getLoginName()))) {
							if(isInteger(user.getMobile())){
								if (user.getMobile().length() == 11) {
									if ("4".equals(newCheckMobile(user.getMobile()))) {              
										if ("true".equals(checkIdcard("", user.getIdCard()))) {
											if (user.getIdCard().length() == 15 || user.getIdCard().length() == 18) {
												if("true".equals(checkOfficeId(user.getCode()))){
													try {
														//默认给美容师角色
														Role role = new Role();
														role.setName("美容师");
														role = roleDao.getByNameNew(role);
														user.setRole(role);
														List<Role> roleList = Lists.newArrayList();
														roleList.add(role);
														user.setRoleList(roleList);
														//默认职位为美容师
														user.setUserType("2");
														user.setPassword(SystemService.entryptPassword("123456"));
														Office office = systemService.getoffice(user.getCode());
														user.setOffice(office);
														user.setName(user.getName().replace(" ", ""));
														systemService.saveUser(user);
														successNum++;
		
													} catch (Exception e) {
														e.getMessage();
														BugLogUtils.saveBugLog(request, "导入保存失败", e);
														failureMsg.append("<br/>导入保存失败 " + user.getMobile());
														failureNum++;
													}
												}else{
													failureMsg.append("<br/>手机号" + user.getMobile() + " ,此用户机构编码有误; ");
													failureNum++;
												}
											} else {
												failureMsg.append("<br/>登录名" + user.getLoginName() + " ,身份证号，必须为15位或18位; ");
												failureNum++;
	
											}
										} else {
											User us = systemService.getUserByIdCard(user.getIdCard());
											if (us.getDelFlag() == "1") {
												failureMsg.append("<br/>身份证号码" + user.getIdCard() + " ,已经存在;用户信息为： 用户名："
														+ us.getLoginName() + " 姓名：" + us.getName() + " 身份证号码："
														+ us.getIdCard() + " 手机号：" + us.getMobile() + " 归属公司："
														+ us.getCompany().getName() + " 所属部门：" + us.getOffice().getName());
												failureNum++;
											} else {
												failureMsg.append("<br/>身份证号码:" + user.getIdCard() + " ,已经存在");
												failureNum++;
											}
	
										}
	
									} else {
										String result = newCheckMobile(user.getMobile());
										if(result == "3"){
											failureMsg.append("<br/>手机号" + user.getMobile() + " ,该号码妃子校和每天美耶都已注册,请联系管理员; ");
											failureNum++;
										}else if(result == "2"){
											failureMsg.append("<br/>手机号" + user.getMobile() + " ,该号码每天美耶已注册,请联系管理员; ");
											failureNum++;
										}else if(result == "1"){
											failureMsg.append("<br/>手机号" + user.getMobile() + " ,该号码妃子校已注册,请联系管理员; ");
											failureNum++;
										}
									}
								} else {
									failureMsg.append("<br/>手机号码 " + user.getMobile() + "要为11位数！");
									failureNum++;
								}
							
							}else{
								failureMsg.append("<br/>手机号码 " + user.getMobile() + "不合法！");
								failureNum++;
							}
														
						} else {
							failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
							failureNum++;
						}
					} catch (ConstraintViolationException ex) {
						logger.error("导入用户出错："+ex.getMessage());
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
						for (String message : messageList) {
							failureMsg.append(message + ";");
							failureNum++;
						}
					} catch (Exception ex) {
						logger.error("导入用户出错："+ex.getMessage());
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
					}
				}
			}

			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，请检查模板是否正确,导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
		} catch (Exception e) {
			logger.error("导入用户出错："+e.getMessage());
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/index?repage";
	}

	/**
	 * 导入删除用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:import")
	@RequestMapping(value = "importUserDelete")
	public String importUserDelete(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			int cell = ei.getLastCellNum();
			if (cell > 5) {
				failureMsg.insert(0, "<br/>导入的模板错误，请检查模板; ");

			} else {
				List<UserDelete> list = ei.getDataList(UserDelete.class);

				for (UserDelete deleteuser : list) {
					try {
						if (deleteuser.getIdCard() == null) {
							break;
						}

						BeanValidators.validateWithException(validator, deleteuser);
						User user = systemService.getUserByIdCard(deleteuser.getIdCard());

						if (user != null) {
							
							if(reservationDao.findCountById(user.getId()) != 0){
								failureMsg.append("<br/>删除用户" + deleteuser.getLoginName() + "失败,此用户存在未完成的预约; ");
								failureNum++;
							}else{
								int index = systemService.deleteUserByIdCard(deleteuser.getIdCard());
								if (index > 0) {
									
									successNum++;
								} else {
									failureMsg.append("<br/>删除用户" + deleteuser.getLoginName() + "失败; ");
									failureNum++;
								}
							}
							

						} else {
							failureMsg.append("<br/>用户" + deleteuser.getLoginName() + " 不存在; ");
							failureNum++;
						}
					} catch (ConstraintViolationException ex) {
						logger.error("导入删除数据出错："+ex.getMessage());
						failureMsg.append("<br/>登录名 " + deleteuser.getLoginName() + " 删除失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ":");
						for (String message : messageList) {
							failureMsg.append(message + ";");
							failureNum++;
						}
					} catch (Exception ex) {
						logger.error("导入删除数据出错："+ex.getMessage());
						failureMsg.append("<br/>登录名 " + deleteuser.getLoginName() + " 删除失败：" + ex.getMessage());
					}
				}

			}

			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，删除信息如下：");
			}
			addMessage(redirectAttributes, "已成功删除 " + successNum + " 条用户" + failureMsg);
		} catch (Exception e) {
			logger.error("导入删除数据出错："+e.getMessage());
			addMessage(redirectAttributes, "删除用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/index?repage";
	}

	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "sysUserImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[用户数据导入模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[用户数据导入模板-new-path"+path);
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

		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 下载删除用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:import")
	@RequestMapping(value = "import/templateDelete")
	public String importTemplateDelete(HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "delUserImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[删除用户数据模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[删除用户数据模板-new-path"+path);
			File file = new File(path);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			addMessage(redirectAttributes, "模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 验证身份证号是否存在
	 * 
	 * @param oldIdCard
	 * @param idCard
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkIdcard")
	public String checkIdcard(String oldIdCard, String idCard) {
		if (idCard != null && idCard.equals(oldIdCard)) {
			return "true";
		} else if (idCard != null && systemService.getUserByIdCard(idCard) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 
	 * @param oldIdCard
	 * @param idCard
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkDelIdcard")
	public User checkDelIdcard(String oldIdCard, String idCard) {
		User users = new User();
		if (idCard != null && systemService.getUserByIdCard(idCard) != null) {
			users = systemService.getUserByIdCard(idCard);
			if ("0".equals(users.getDelFlag())) {
				users.setDelindex("0");
			} else {
				users.setDelindex("1");
			}
			return users;
		}
		return null;
	}

	/**
	 * 验证工号是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { "sys:user:add", "sys:user:edit" }, logical = Logical.OR)
	@RequestMapping(value = "checkNO")
	public String checkNO(String oldNo, String no) {
		if (no != null && no.equals(oldNo)) {
			return "true";
		} else if (no != null && systemService.getUserByNO(no) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { "sys:user:add", "sys:user:edit" }, logical = Logical.OR)
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证手机号
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value = { "sys:user:add", "sys:user:edit" }, logical = Logical.OR)
	@RequestMapping(value = "newCheckMobile")
	public String newCheckMobile(String mobile) {
		Users user = new Users();
		user.setMobile(mobile);
		if(mobile != null && systemService.getByMobile(mobile) != null && mtmyUsersDao.findUserBymobile(user) != 0){
			return "3";
		}else if(mobile != null && mtmyUsersDao.findUserBymobile(user) != 0){
			return "2";
		}else if (mobile != null && systemService.getByMobile(mobile) != null){
			return "1";
		}
		return "4";
		
	}
	

	
	/**
	 * 导入用户验证用户上级机构是否准确
	 * @param officeCode
	 * @return
	 */
	public String checkOfficeId(String officeCode){
		OfficeInfo o = officeService.verifyOfficeName(officeCode,0);
		if(null == o){
			return "false";
		}
		return "true";
	}
	/**
	 * 用户信息显示
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	/**
	 * 用户信息显示编辑保存
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "infoEdit")
	public String infoEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			if (user.getName() != null)
				currentUser.setName(user.getName());
			if (user.getEmail() != null)
				currentUser.setEmail(user.getEmail());
			if (user.getPhone() != null)
				currentUser.setPhone(user.getPhone());
			if (user.getMobile() != null)
				currentUser.setMobile(user.getMobile());
			if (user.getRemarks() != null)
				currentUser.setRemarks(user.getRemarks());
			if (user.getPhoto() != null)
				currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			if (__ajax) {// 手机访问
				AjaxJson j = new AjaxJson();
				j.setSuccess(true);
				j.setMsg("修改个人资料成功!");
				return renderString(response, j);
			}
			model.addAttribute("user", currentUser);
			model.addAttribute("Global", new Global());
			model.addAttribute("message", "保存用户信息成功");
			return "modules/sys/userInfo";
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfoEdit";
	}

	/**
	 * 用户头像显示编辑保存
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "imageEdit")
	public String imageEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			if (user.getPhoto() != null)
				currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			if (__ajax) {// 手机访问
				AjaxJson j = new AjaxJson();
				j.setSuccess(true);
				j.setMsg("修改个人头像成功!");
				return renderString(response, j);
			}
			model.addAttribute("message", "保存用户信息成功");
			return "modules/sys/userInfo";
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userImageEdit";
	}

	/**
	 * 用户头像显示编辑保存
	 * 
	 * @param user
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "imageUpload")
	public String imageUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile file)
			throws IllegalStateException, IOException {
		User currentUser = UserUtils.getUser();

		// 判断文件是否为空
		if (!file.isEmpty()) {
			// 文件保存路径
			String realPath = Global.USERFILES_BASE_URL + UserUtils.getPrincipal() + "/images/";
			// 转存文件
			FileUtils.createDirectory(Global.getUserfilesBaseDir() + realPath);
			file.transferTo(new File(Global.getUserfilesBaseDir() + realPath + file.getOriginalFilename()));
			currentUser.setPhoto(request.getContextPath() + realPath + file.getOriginalFilename());
			systemService.updateUserInfo(currentUser);
		}

		return "modules/sys/userImageEdit";
	}

	/**
	 * 返回用户信息
	 * 
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public AjaxJson infoData() {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setErrorCode("-1");
		j.setMsg("获取个人信息成功!");
		j.put("data", UserUtils.getUser());
		return j;
	}

	/**
	 * 修改个人用户密码
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "修改密码成功");
			} else {
				model.addAttribute("message", "修改密码失败，旧密码错误");
			}
			return "modules/sys/userInfo";
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i = 0; i < list.size(); i++) {
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_" + e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 通知--》查询用户信息
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "oaList")
	public Map<String, List<User>> oaList(User user,HttpServletRequest request, HttpServletResponse response,Model model) {
		Map<String,List<User>> jsonMap=new HashMap<String, List<User>>();  
		List<User> list = systemService.findOaUser(user);
		jsonMap.put("list",list);
		return jsonMap;
	}
	/**
	 * web端ajax验证用户名是否可用
	 * 
	 * @param loginName
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "validateLoginName")
	public boolean validateLoginName(String loginName, HttpServletResponse response) {

		User user = userDao.findUniqueByProperty("login_name", loginName);
		if (user == null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * web端ajax验证手机号是否可以注册（数据库中不存在）
	 */
	@ResponseBody
	@RequestMapping(value = "validateMobile")
	public boolean validateMobile(String mobile, HttpServletResponse response, Model model) {
		User user = userDao.findUniqueByProperty("mobile", mobile);
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * web端ajax验证手机号是否已经注册（数据库中已存在）
	 */
	@ResponseBody
	@RequestMapping(value = "validateMobileExist")
	public boolean validateMobileExist(String mobile, HttpServletResponse response, Model model) {
		User user = userDao.findUniqueByProperty("mobile", mobile);
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "resetPassword")
	public AjaxJson resetPassword(String mobile, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		if (userDao.findUniqueByProperty("mobile", mobile) == null) {
			j.setSuccess(false);
			j.setMsg("手机号不存在!");
			j.setErrorCode("1");
			return j;
		}
		User user = userDao.findUniqueByProperty("mobile", mobile);
		String newPassword = String.valueOf((int) (Math.random() * 900000 + 100000));
		try {
			String result = SMSUtils.sendPass(mobile, newPassword);
			if (!result.equals("100")) {
				j.setSuccess(false);
				j.setErrorCode("2");
				j.setMsg("短信发送失败，密码重置失败，错误代码：" + result + "，请联系管理员。");
			} else {
				j.setSuccess(true);
				j.setErrorCode("-1");
				j.setMsg("短信发送成功，密码重置成功!");
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
			}
		} catch (IOException e) {
			j.setSuccess(false);
			j.setErrorCode("3");
			j.setMsg("因未知原因导致短信发送失败，请联系管理员。");
		}
		return j;
	}

	public List<Map<String, Object>> areatreeData() {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", e.getName());
			mapList.add(map);

		}
		// System.out.println("区域"+mapList);
		return mapList;
	}
	
	
	public static boolean isInteger(String value) {
		  try {
		   Long.parseLong(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
	 }


	// @InitBinder
	// public void initBinder(WebDataBinder b) {
	// b.registerCustomEditor(List.class, "roleList", new
	// PropertyEditorSupport(){
	// @Autowired
	// private SystemService systemService;
	// @Override
	// public void setAsText(String text) throws IllegalArgumentException {
	// String[] ids = StringUtils.split(text, ",");
	// List<Role> roles = new ArrayList<Role>();
	// for (String id : ids) {
	// Role role = systemService.getRole(Long.valueOf(id));
	// roles.add(role);
	// }
	// setValue(roles);
	// }
	// @Override
	// public String getAsText() {
	// return Collections3.extractToString((List) getValue(), "id", ",");
	// }
	// });
	// }
}


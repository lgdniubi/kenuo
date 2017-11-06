/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.beanvalidator.BeanValidators;
import com.training.common.config.Global;
import com.training.common.service.BaseService;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.DictUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 机构Controller
 * 
 * @version 2016-1-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	@Autowired
	private ReservationDao reservationDao;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return officeService.get(id);
		}else{
			return new Office();
		}
	}

	/**
	 * 机构管理-首页
	 * @param office
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:office:index")
	@RequestMapping(value = {""})
	public String index(Office office, Model model) {
		return "modules/sys/officeIndex";
	}

	/**
	 * 机构管理-列表
	 * @param office
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:office:index")
	@RequestMapping(value = {"list"})
	public String list(Office office, Model model,HttpServletRequest request) {
//		原机构界面
//		User user = UserUtils.getUser();
//		try {
//			if(!user.isAdmin()){
//				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a"));
//			}else{
//				office.getSqlMap().put("dsf", BaseService.superDataScope("a"));
//			}
//			model.addAttribute("list", officeService.findList(office));
//		} catch (Exception e) {
//			logger.error("#####[机构管理-列表-出现异常]："+e.getMessage());
//			model.addAttribute("message", "机构管理-列表-出现异常,请与管理员联系");
//			BugLogUtils.saveBugLog(request, "机构管理-列表-出现异常", e);
//		}
//		if(user.isAdmin() || User.DATA_SCOPE_OFFICE_AND_CHILD.equals(String.valueOf(user.getDataScope()))){
//			//用户为管理员和数据范围(1:所在部门及以下数据)
//			return "modules/sys/officeList";
//		}else{
//			//数据范围(2:按明细设置)
//			return "modules/sys/officeListDetail";
//		}
		return "modules/sys/officeLists";
	}
	
	/**
	 * 根据父类id查询子类数据 用于异步加载树形table
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findByPidforChild")
	public String findByPidforChild(@RequestParam(required=false) String id, HttpServletResponse response) {
		List<Office> officeList = Lists.newArrayList();
		Office office = new Office();
		office.setId(id);
		officeList = officeService.findByPidforChild(office);
		
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(officeList, jsonConfig);
		return json.toString();
	}
	
	
	/**
	 * 机构列表-查看/添加/修改
	 * @param office
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:office:view","sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent()==null || office.getParent().getId()==null){
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
//		添加下级机构  area区域默认值
//		if (office.getArea()==null){
//			office.setArea(user.getOffice().getArea());
//		}
		if(office.getId()==null||office.getId()==""){
			List<Office> l = officeService.findListbyPID(office.getParentId());
			long size=0;
			String newCode = null;
			if(l.size()==0){
					size=0;
					office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
				}else{
					for (int i = 0; i < l.size(); i++) {
						Office e = l.get(i);
							if(e.getCode().length()>=13){
								String oldSting =  e.getCode().substring(e.getCode().length()-5,e.getCode().length());
								String newString = String.valueOf(Long.valueOf(oldSting)+1);
								String [] ss = {"0000","000","00","0"};
								if(newString.length()<5){
									newString = ss[newString.length()-1] + newString;
								}
								if(e.getCode().endsWith(oldSting)){
									String a = e.getCode().substring(0, e.getCode().lastIndexOf(oldSting));
									newCode = a + newString;
								}
							}else{
								newCode=String.valueOf(Long.valueOf(e.getCode())+1);
							}
						}
					office.setCode(newCode);
			}
		}
//		// 自动获取排序号
//		if (StringUtils.isBlank(office.getId())&&office.getParent()!=null){
//			int size = 0;
//			List<Office> list = officeService.findAll();
//			for (int i=0; i<list.size(); i++){
//				Office e = list.get(i);
//				if (e.getParent()!=null && e.getParent().getId()!=null
//						&& e.getParent().getId().equals(office.getParent().getId())){
//					size++;
//				}
//			}
//			office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
//		}
		//当为实体店铺时查询其详细信息
		if("1".equals(office.getGrade())){
			OfficeInfo officeInfo = officeService.findbyid(office);
			if(officeInfo != null){
				//officeInfo中店铺标签 (多个标签用"#"分开)
				if(officeInfo.getTags() !=null){
					officeInfo.setTags(officeInfo.getTags().replaceAll("#", ","));
				}
				officeInfo.setDetails(HtmlUtils.htmlEscape(officeInfo.getDetails()));//详细介绍转码
				office.setOfficeInfo(officeInfo);
			}
		}
		//界面展示所属加盟商
		OfficeInfo o=new OfficeInfo();
		int codenum=office.getCode().length();
		int b=codenum%4;
		String c=office.getCode().substring(0, b+4);
		o.setFranchiseeCode(c);
		String a=officeService.findFNameByCode(o).getFranchiseeName();
		model.addAttribute("a", a);
		//判断机构类型
		if("10001".equals(c)){
			office.setType("1");
		}else{
			office.setType("2");
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}
	
	/**
	 * 机构管理-添加/修改
	 * @param office
	 * @param officeInfo
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Office office,OfficeInfo officeInfo, Model model,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		//officeInfo中店铺标签 (多个标签用"#"分开)
		if(office.getOfficeInfo() != null && office.getOfficeInfo().getTags() !=null){
			office.getOfficeInfo().setTags(office.getOfficeInfo().getTags().replaceAll(",", "#"));
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/";
		}
		if (!beanValidator(model, office)){
			return form(office, model);
		}
		
		if(office.getGrade().equals("2")){
			office.getArea().setId(officeInfo.getAreaInfo().getId());
		}

		//记录店铺首图图片上传相关信息所需要的数据
		User currentUser = UserUtils.getUser();//获取当前登录用户
		List<String> lifeImgUrls = new ArrayList<String>();//店铺首图需要用到
		String img = "";
				
		if("".equals(office.getId())){
			if(office.getGrade().equals("2")){
				//添加机构
				officeService.save(office);
			}else{
				//添加实体店
				officeService.save(office);
				office.setId(officeService.getByCode(office.getCode()).getId());
				officeService.saveOfficeInfo(office);

				//店铺首图
				img = office.getOfficeInfo().getImg();
				if(img != null && img != ""){
					reservationTime(3, currentUser.getCreateBy().getId(), img, "", lifeImgUrls, office.getId(), "bm", null);
				}
			}
		}else{
			//修改机构或修改实体店
			if(office.getGrade().equals("2")){
				//修改机构
				officeService.save(office);
			}else{
				//查询出修改前修改前店铺首图信息
				OfficeInfo oldOffice = officeService.findbyid(office);
				
				//修改实体店
				officeService.save(office);
				officeService.saveOfficeInfo(office);

				//店铺首图
				img = office.getOfficeInfo().getImg();
				if(img != null && img != "" && !oldOffice.getImg().equals(img)){
					reservationTime(3, currentUser.getCreateBy().getId(), img, oldOffice.getImg(), lifeImgUrls, office.getId(), "bm", null);
				}
			}
		}
		if(office.getChildDeptList()!=null){
			Office childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade())+1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}
	
	/**
	 * 机构管理-删除
	 * @param office
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:del")
	@RequestMapping(value = "delete")
	public String delete(Office office, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/list";
		}
//		if (Office.isRoot(id)){
//			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
//		}else{
			officeService.delete(office);
			//删除机构时关联删除实体店铺信息
			officeService.deleteOfficeInfo(office);
			addMessage(redirectAttributes, "删除机构成功");
//		}
		return "redirect:" + adminPath + "/sys/office/list?id="+office.getParentId()+"&parentIds="+office.getParentIds();
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll,@RequestParam(required=false) String isGrade, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
				//选择上级机构时      上级机构为非店铺
				if("true".equals(isGrade) && "1".equals(e.getGrade())){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			
		}
		return mapList;
	}
	/**
	 * 新机构
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "newTreeData")
	public List<Map<String, Object>> newTreeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll,@RequestParam(required=false) String isGrade, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
				//选择上级机构时      上级机构为非店铺
				if("true".equals(isGrade) && "1".equals(e.getGrade())){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				map.put("code", "机构编码:("+e.getCode()+")");
				map.put("grade", e.getGrade());
				map.put("num", e.getNum());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			
		}
		return mapList;
	}
	/**
	 * 通过区域加载店铺
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "storeTreeDate")
	public List<Map<String, Object>> storeTreeDate(@RequestParam(required=false) String type,String areaId,
			 @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findListByAreaId(areaId);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 验证店铺名称是否存在
	 * @param office
	 * @param oldOffice
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifyName")
	public String verifyName(Office office,String oldOfficeName){
		if(office.getName() != null && office.getName().equals(oldOfficeName)){
			return "true";
		}else if(office.getName() != null && officeService.verifyOfficeNameByPid(office).size() == 0){
			return "true";
		}
		return "false";
	}
	/**
	 * 导入店铺验证
	 * @param upOfficeCode
	 * @param areaCode
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String verifyOfficeInfo(String upOfficeCode,String areaCode,String beginTime,String endTime,String officeName,String shortName){
		//验证上级机构是否存在
		OfficeInfo a=officeService.verifyOfficeName(upOfficeCode,2);
		//验证区域是否存在
		OfficeInfo b=officeService.verifyAreaName(areaCode);
		//查询upOfficeCode机构下所有机构名称
		List<OfficeInfo> c = officeService.verifyOfficeNameByCode(upOfficeCode);
		String regex = "[0-2][0-9][:][0,3][0]"; //时间验证
		if(null==a){
			return "officeFalse";
		}else if(null==b || b.getNum()>0){
			return "areaFalse";
		}else if(false == beginTime.matches(regex) || false == endTime.matches(regex)){
			return "timeFalse";
		}else if(null != c){
			for (OfficeInfo officeInfo : c) {
				if(officeName.equals(officeInfo.getOfficeName())){
					return "officeNameFalse";
				}
			}
		}
		return "true";
	}
	/**
	 * 机构验证
	 * @param upOfficeCode
	 * @param areaCode
	 * @return
	 */
	public String verifyOffice(String upOfficeCode,String areaCode,String officeName){
		OfficeInfo a=officeService.verifyOfficeName(upOfficeCode,2);
		OfficeInfo b=officeService.verifyAreaName(areaCode);
		List<OfficeInfo> c = officeService.verifyOfficeNameByCode(upOfficeCode);
		if(null==a){
			return "officeFalse";
		}else if(null==b){
			return "areaFalse";
		}else if(null==officeName){
			return "officeNameFalse1";
		}else if(null != c){
			for (OfficeInfo officeInfo : c) {
				if(officeName.equals(officeInfo.getOfficeName())){
					return "officeNameFalse";
				}
			}
		}
		return "true";
	}
	/**
	 * 跳转到导入界面
	 * @return
	 */
	@RequiresPermissions("sys:office:importPage")
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/sys/officeImportExcel";
	}
	/**
	 * 导入店铺
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:import")
	@RequestMapping(value = "import")
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes){
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OfficeInfo> list=ei.getDataList(OfficeInfo.class);
			for (OfficeInfo officeInfo : list) {
				try {
//						if(officeInfo.getUpOfficeCode()==""){
//							continue;
//						}
					if("是".equals(officeInfo.getGrade())){
						String str=verifyOfficeInfo(officeInfo.getUpOfficeCode(),officeInfo.getAreaCode(),officeInfo.getBeginTime(),officeInfo.getEndTime(),officeInfo.getOfficeName(),officeInfo.getShortName());
						if ("true".equals(str)){
							BeanValidators.validateWithException(validator, officeInfo);
							//获取最大code
							Office office = new Office();
							List<Office> l = officeService.findListbyPID(officeService.verifyOfficeName(officeInfo.getUpOfficeCode(),2).getUpOfficeId());
							long size=0;
							String newCode = null;
							if(0==l.size()){
								size=0;
								office.setCode(officeInfo.getUpOfficeCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
							}else{
								for (int i = 0; i < l.size(); i++) {
									Office e = l.get(i);
									if(e.getCode().length()>=13){
										String oldSting =  e.getCode().substring(e.getCode().length()-5,e.getCode().length());
										String newString = String.valueOf(Long.valueOf(oldSting)+1);
										String [] ss = {"0000","000","00","0"};
										if(newString.length()<5){
											newString = ss[newString.length()-1] + newString;
										}
										if(e.getCode().endsWith(oldSting)){
											String a = e.getCode().substring(0, e.getCode().lastIndexOf(oldSting));
											newCode = a + newString;
										}
									}else{
										newCode=String.valueOf(Long.valueOf(e.getCode())+1);
									}
								}
								office.setCode(newCode);
							}
							
							Area area = new Area();
							area.setId(officeService.verifyAreaName(officeInfo.getAreaCode()).getAreaId());
							//判断直营店还是加盟店
							int codenum=office.getCode().length();
							int b=codenum%4;
							String c=office.getCode().substring(0, b+4);
							//判断机构类型
							if("10001".equals(c)){
								office.setType("1");
							}else{
								office.setType("2");
							}
							
							office.setParent(officeService.getByCode(officeInfo.getUpOfficeCode()));
							office.setArea(area);
							office.setName(officeInfo.getOfficeName());
							office.setUseable("1");  // 1 为可用
							office.setGrade("1");  // 1  为店铺
							//保存店铺到sys_office
							officeService.save(office);
							//保存店铺同时保存到sys_office_info
							officeInfo.setId(officeService.getByCode(office.getCode()).getId());
							officeService.saveOfficeInfo2(officeInfo);
							
							successNum++;
						}else if("officeFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：上级机构编码有误;");
							failureNum++;
						}else if("areaFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：归属区域编码有误或归属区域不为区县级区域;");
							failureNum++;
						}else if("timeFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：时间格式有误，将时间单元格格式改为文本格式且格式为09:00;");
							failureNum++;
						}else if("officeNameFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：此机构已存在");
							failureNum++;
						}else if("shortNameFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：此店铺短名称名已存在");
							failureNum++;
						}
					}else if("否".equals(officeInfo.getGrade())){
						String str=verifyOffice(officeInfo.getUpOfficeCode(),officeInfo.getAreaCode(),officeInfo.getOfficeName());
						if ("true".equals(str)){
							//获取最大code
							Office office = new Office();
							List<Office> l = officeService.findListbyPID(officeService.verifyOfficeName(officeInfo.getUpOfficeCode(),2).getUpOfficeId());
							long size=0;
							String newCode = null;
							if(0==l.size()){
								size=0;
								office.setCode(officeInfo.getUpOfficeCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
							}else{
								for (int i = 0; i < l.size(); i++) {
									Office e = l.get(i);
									if(e.getCode().length()>=13){
										String oldSting =  e.getCode().substring(e.getCode().length()-5,e.getCode().length());
										String newString = String.valueOf(Long.valueOf(oldSting)+1);
										String [] ss = {"0000","000","00","0"};
										if(newString.length()<5){
											newString = ss[newString.length()-1] + newString;
										}
										if(e.getCode().endsWith(oldSting)){
											String a = e.getCode().substring(0, e.getCode().lastIndexOf(oldSting));
											newCode = a + newString;
										}
									}else{
										newCode=String.valueOf(Long.valueOf(e.getCode())+1);
									}
								}
								office.setCode(newCode);
							}
							Area area = new Area();
							area.setId(officeService.verifyAreaName(officeInfo.getAreaCode()).getAreaId());
							//判断直营店还是加盟店
							int codenum=office.getCode().length();
							int b=codenum%4;
							String c=office.getCode().substring(0, b+4);
							//判断机构类型
							if("10001".equals(c)){
								office.setType("1");
							}else{
								office.setType("2");
							}
							
							office.setParent(officeService.getByCode(officeInfo.getUpOfficeCode()));
							office.setArea(area);
							office.setName(officeInfo.getOfficeName());
							office.setUseable("1");  // 1 为可用
							office.setGrade("2");  // 1  为店铺
							//保存店铺到sys_office
							officeService.save(office);

							successNum++;
						}else if("officeFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：上级机构编码有误;");
							failureNum++;
						}else if("areaFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：归属区域编码有误或归属区域不为区县级区域;");
							failureNum++;
						}else if("officeNameFalse1".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：机构名称输入有误;");
							failureNum++;
						}else if("officeNameFalse".equals(str)){
							failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：此机构已存在");
							failureNum++;
						}
					}else{
						failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：是否为店铺输入有误;");
						failureNum++;
					}
						
					} catch (ConstraintViolationException ex) {
						failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
						for (String message : messageList){
							failureMsg.append(message+"; ");
							failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>机构名 "+officeInfo.getOfficeName()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 家机构，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 家机构"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入机构失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/office/";
	}
	/**
	 * 下载机构数据导入模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:template")
    @RequestMapping(value = "import/template")
    public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String filename = "officeImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[机构数据导入模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[机构数据导入模板-new-path"+path);
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
		
    }
    /**
	 * 导出店铺数据
	 * @param office
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequiresPermissions("sys:office:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Office office, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "店铺数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            
            User user = UserUtils.getUser();
			office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
            
            List<OfficeInfo> o=officeService.exportOffice(office);
    		new ExportExcel("店铺数据", OfficeInfo.class).setDataList(o).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/office/list";
    }
    @RequestMapping(value = "lists")
    public String lists( HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    	return "modules/sys/officeLists";
    }
    
    /**
     * 验证店铺下是否有员工
     * @param office
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping(value="delConfirm")
    @ResponseBody
    public String delConfirm(Office office,RedirectAttributes redirectAttributes,HttpServletRequest request){
    	String result = "";
    	try{
    		if(officeService.delConfirm(office) > 0){
    			result= "error";
    		}else{
    			result= "success";
    		}
    	}catch(Exception e){
    		addMessage(redirectAttributes, "动态验证店铺下是否有员工失败！");
			logger.error("动态验证店铺下是否有员工出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "动态验证店铺下是否有员工失败错误信息", e);
    	}
    	return result;
    }
    
    /**
     * 修改店铺是否属性
     * @param request
     * @return
     */
    @RequestMapping(value = {"updateisyesno"})
    @ResponseBody
    public Map<String, String> updateisyesno(HttpServletRequest request){
    	Map<String, String> map = new HashMap<String, String>();
    	try {
    		String id = request.getParameter("ID");				// id
    		String type = request.getParameter("TYPE");			// 类型
    		String isyesno = request.getParameter("ISYESNO");	// 是否
    		if("isCargo".equals(type)){
    			String weburl = ParametersFactory.getMtmyParamValues("modifyIsCargo");
    			logger.info("##### web接口路径:"+weburl);
    			String parpm = "{\"office_id\":\""+id+"\",\"is_cargo\":\""+Integer.valueOf(isyesno)+"\"}";
    			String url=weburl;
    			String result = WebUtils.postCSObject(parpm, url);
    			JSONObject jsonObject = JSONObject.fromObject(result);
    			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
    			if("200".equals(jsonObject.get("result"))){
    				officeService.updateisyesno(id,type,isyesno);
    				map.put("FLAG", "OK");
    				map.put("MESSAGE", "修改成功");
    			}else{
    				map.put("FLAG", "ERROR");
    				map.put("MESSAGE", "修改失败："+jsonObject.get("msg"));
    			}
    		}else{
    			officeService.updateisyesno(id,type,isyesno);
    			map.put("FLAG", "OK");
    			if("status".equals(type)){
    				int num = reservationDao.findCountByOfficeId(id);
    				if(num > 0){
    					map.put("MESSAGE", "该店铺存在未完成的预约!");
    				}else{
    					map.put("MESSAGE", "隐藏该店铺成功");
    				}
    			}else{
    				map.put("MESSAGE", "修改成功");
    			}
    		}
			
		} catch (Exception e) {
			logger.error("修改店铺是否属性错误信息："+e.getMessage());
    		BugLogUtils.saveBugLog(request, "店铺是否属性修改失败", e);
    		map = new HashMap<String, String>();
    		map.put("FLAG", "ERROR");
    		map.put("MESSAGE", "修改失败");
		}
    	return map;
    }
    
    /**
	 * 记录店铺首图图片上传相关信息
	 * @param beauticianId
	 * @param serviceMin
	 * @param shopId
	 * @param labelId
	 * @param request
	 * @return
	 */
	private void reservationTime(int type, String createBy, String fileUrl, String oldUrl, List<String> lifeImgUrls, String userId, String client, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {
			String webReservationTime =	ParametersFactory.getMtmyParamValues("uploader_picture_url");	
			logger.info("##### web接口路径:"+webReservationTime);
			String parpm = "{\"type\":\""+type+"\",\"create_by\":\""+createBy+"\",\"file_url\":\""+fileUrl+"\",\"old_url\":\""+oldUrl+"\",\"life_img_urls\":\""+lifeImgUrls+"\",\"user_id\":\""+userId+"\",\"client\":\""+client+"\"}";
			String url=webReservationTime;
			String result = WebUtils.postTrainObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "记录店铺首图、美容院和美容师图片上传相关信息", e);
			logger.error("调用接口:记录店铺首图、美容院和美容师图片上传相关信息:"+e.getMessage());
		}
	}
	
    /**
     * 
     * @Title: newUserOffice
     * @Description: TODO 机构树跳转页面
     * @param compId
     * @return:
     * @return: String
     * @throws
     * 2017年11月2日 兵子
     */
    @RequestMapping(value="newUserOffice")
    public String newUserOffice(String compId,Model model) {
    	model.addAttribute("compId", compId);
    	return "modules/sys/newUserOffice";
	}
    
    /**
     * 
     * @Title: newOfficeTreeData
     * @Description: TODO 根据商家id查询此商家下的机构
     * @param compId
     * @return:
     * @return: List<Map<String,Object>>
     * @throws
     * 2017年11月2日 兵子
     */
    @ResponseBody
    @RequestMapping(value="newOfficeTreeData")
    public List<Map<String, Object>> newOfficeTreeData(String compId){
    	List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.newOfficeTreeData(compId);
		for (int i=0; i<list.size(); i++){
			Office e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("pIds", e.getParentIds());
			map.put("name", e.getName());
			mapList.add(map);
		}
    	return mapList;
    }
    
}

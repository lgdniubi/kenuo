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
import com.training.common.persistence.Page;
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
import com.training.modules.sys.entity.OfficeAcount;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.OfficeLog;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.DictUtils;
import com.training.modules.sys.utils.OfficeThreadUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.ProtocolModelDao;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.ContractInfoVo;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.PayInfo;
import com.training.modules.train.entity.TrainRuleParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
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
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("office")
	public Office get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			Office office = officeService.get(id);
			return office;
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
	public String form(Office office, Model model,String opflag) {
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
		User assistUser = new User();
		if("1".equals(office.getGrade())){
			OfficeInfo officeInfo = officeService.findbyid(office);
			if(officeInfo != null){
				//officeInfo中店铺标签 (多个标签用"#"分开)
				if(officeInfo.getTags() !=null){
					officeInfo.setTags(officeInfo.getTags().replaceAll("#", ","));
				}
				officeInfo.setDetails(HtmlUtils.htmlEscape(officeInfo.getDetails()));//详细介绍转码
				office.setOfficeInfo(officeInfo);
				assistUser = systemService.getUser(office.getOfficeInfo().getShopAssistantId());
			}
		}
		//界面展示所属加盟商
		OfficeInfo o=new OfficeInfo();
		int codenum=office.getCode().length();
		int b=codenum%4;
		String c=office.getCode().substring(0, b+4);
		o.setFranchiseeCode(c);
		String companyId=officeService.findFNameByCode(o).getFranchiseeId();
		model.addAttribute("companyId", companyId);
		//判断机构类型
		if("10001".equals(c)){
			office.setType("1");
		}else{
			office.setType("2");
		}
		model.addAttribute("office", office);
		model.addAttribute("opflag", opflag);
		model.addAttribute("user", assistUser);
		
		if(StringUtils.isNotEmpty(office.getId())){
			//0未签约，1已签约。
			Integer status = getOfficeSignStatus(office.getId());
			model.addAttribute("status", status);
		}
		return "modules/sys/officeForm";
	}
	
	private Integer getOfficeSignStatus(String id) {
		Integer status = null;
		String url = ParametersFactory.getTrainsParamValues("contract_data_path");
		logger.info("##### web接口路径查询机构签约状态:"+url);
		String parpm = "{\"office_id\":\""+id+"\"}";
		logger.info("##### web接口路径查询机构签约状态信息参数:"+parpm);
		String result = WebUtils.postCSObject(parpm, url);
		JSONObject jsonObject = JSONObject.fromObject(result);
		if(!(jsonObject.get("data") instanceof JSONNull)){
			status = jsonObject.getJSONObject("data").getInt("status");
		}
		logger.info("##### web接口查询机构状态返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
		return status;
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
			return form(office, model,"0");
		}
		
		if(office.getGrade().equals("2")){
			office.getArea().setId(officeInfo.getAreaInfo().getId());
		}

		//记录店铺首图图片上传相关信息所需要的数据
		User currentUser = UserUtils.getUser();//获取当前登录用户
		List<String> lifeImgUrls = new ArrayList<String>();//店铺首图需要用到
		String img = "";
		boolean notEmptyOfficeId = StringUtils.isNotEmpty(office.getId());
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
				if(img != null && !"".equals(img)){
					reservationTime(3, currentUser.getCreateBy().getId(), img, "", lifeImgUrls, office.getId(), "bm", null);
				}
				
				//操作店铺保存记录日志	创建店铺记录
				saveOfficeLog(office.getId(),0,"");
				//操作店铺保存记录日志	隐藏/开启记录
				saveOfficeLog(office.getId(),(office.getOfficeInfo().getStatus() == 0)?2:3,"");
			}
		}else{
			Office eqold = officeService.get(office.getId());
			//修改机构或修改实体店
			if(office.getGrade().equals("2")){
				//修改机构
				officeService.save(office);
				// 机构类型变更 由店铺修改为非店铺
				if (!eqold.getGrade().equals(office.getGrade())) {
					saveOfficeLog(office.getId(),3,"是否是店变更开启");
				}
				
			}else{
				//查询出修改前修改前店铺首图信息
				OfficeInfo oldOffice = officeService.findbyid(office);
				
				//修改实体店
				officeService.save(office);
				officeService.saveOfficeInfo(office);
				// 机构类型变更 由店铺修改为非店铺
				if (office.getOfficeInfo().getId() == null || "".equals(office.getOfficeInfo().getId())) {
					saveOfficeLog(office.getId(),(office.getOfficeInfo().getStatus() == 0)?2:3,"是否是店变更关闭");
				}

				//店铺首图
				img = office.getOfficeInfo().getImg() == null ? "" : office.getOfficeInfo().getImg();//修改后的首图信息
				String oldImg = "";
				if(oldOffice != null){//判断修改前的店铺信息是否为空,防止报空指针异常
					oldImg = oldOffice.getImg() == null ? "" : oldOffice.getImg();//修改前的首图信息
				}
				if(!oldImg.equals(img)){
//					reservationTime(3, currentUser.getCreateBy().getId(), img, oldOffice.getImg(), lifeImgUrls, office.getId(), "bm", null);
				}
			}
			
			/**
			 * 此处调用报货接口，在修改机构项时将修改的机构数据循环同步到报货
			 */
			//String weburl = ParametersFactory.getMtmyParamValues("modifyToOffice");
			OfficeLog officeLog = new OfficeLog();
			OfficeThreadUtils.equalBH(office,eqold,officeLog);
				
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
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		if(office.getGrade().equals("2") || notEmptyOfficeId){
			return "redirect:" + adminPath + "/sys/office/form?id="+office.getId()+"&opflag=1&parentIds="+office.getParentIds();
		}else{
			return "redirect:" + adminPath + "/sys/office/signInfo?id="+office.getId()+"&opflag=1&parentIds="+office.getParentIds();
		}
//		return "redirect:" + adminPath + "/sys/office/form?id="+office.getId()+"&parentIds="+office.getParentIds();
//		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}
	
	@RequiresPermissions(value={"sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "signInfo")
	@SuppressWarnings("unchecked")
	public String signInfo(Office office,String opflag, Model model,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User user = UserUtils.getUser();
			String weburl = ParametersFactory.getTrainsParamValues("contract_data_path");
			logger.info("##### web接口路径查询签约信息:"+weburl);
			String parpm = "{\"office_id\":\""+office.getId()+"\"}";
			logger.info("##### web接口路径查询签约信息参数:"+parpm);
//		String url="http://172.50.3.16:8081/cs_service/pub/queryContractInfoAudit.htm";
			String url=weburl;
			String result = WebUtils.postCSObject(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			ContractInfoVo infoVo = (ContractInfoVo) JSONObject.toBean(jsonObject.getJSONObject("data"), ContractInfoVo.class);
			if(!(jsonObject.get("data") instanceof JSONNull)){
				List<PayInfo> payInfos = JSONArray.toList(jsonObject.getJSONObject("data").getJSONArray("payInfos"), new PayInfo(),new JsonConfig());
				model.addAttribute("payInfos", payInfos);
				model.addAttribute("paylen", payInfos.size());
			}else{
				model.addAttribute("paylen", 0);
			}
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
			if(!"200".equals(jsonObject.get("result"))){
				throw new RuntimeException("获取签约信息失败");
			}
			if(infoVo==null ){
				OfficeInfo officeInfo = officeService.findbyid(office);
				infoVo = new ContractInfoVo();
				infoVo.setOffice_address(officeInfo.getDetailedAddress());
			}
			ModelFranchisee mod = officeService.findPayType(office.getId());
			model.addAttribute("infoVo", infoVo);
			model.addAttribute("office", office);
			model.addAttribute("payWay", mod.getPaytype());
			model.addAttribute("user", user);
			model.addAttribute("opflag", opflag);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "获取签约信息失败");
		}
		return "modules/sys/signInfoForm";
	}
	@RequiresPermissions(value={"sys:office:add","sys:office:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveSignInfo")
	public String saveSignInfo(ContractInfoVo contractInfo,Integer payWay, Model model,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			List<PayInfo> payInfos = contractInfo.getPayInfos();
			if(payInfos != null){		//判断是否有支付信息
				List<PayInfo> list = creatPayInfoList(payWay, payInfos,contractInfo.getCreate_user());
				contractInfo.setPayInfos(list);
			}
			JsonConfig config = new JsonConfig();
			JSONObject j = JSONObject.fromObject(contractInfo,config);
			System.out.println(j.toString());
			String weburl = ParametersFactory.getTrainsParamValues("contract_save_path");
			logger.info("##### web接口路径:"+weburl);
			String parpm = j.toString();
//		String url="http://172.50.3.16:8081/cs_service/pub/saveContractInfoAudit.htm";
			String url=weburl;
			String result = WebUtils.postCSObject(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
			if(!"200".equals(jsonObject.get("result"))){
				throw new RuntimeException("保存签约信息失败");
			}
			officeService.deleteProtocolShopById(String.valueOf(contractInfo.getFranchisee_id()));
			
			String oldOfficeAddress = request.getParameter("oldOfficeAddress");
			String oldOfficeName = request.getParameter("oldOfficeName");
			officeService.updateAddressAndName(contractInfo, oldOfficeAddress, oldOfficeName);
			
			addMessage(redirectAttributes, "保存签约信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "保存签约信息失败");
		}
		return "redirect:" + adminPath + "/sys/office/signInfo?id="+contractInfo.getOffice_id()+"&opflag=1";
	}


	private List<PayInfo> creatPayInfoList(Integer payWay, List<PayInfo> payInfos, String userid) {
		List<PayInfo> ns = new ArrayList<>();
		if(payWay == 0){ //线下支付
			PayInfo payInfo = payInfos.get(0);
			String[] username = payInfo.getPay_username().split(",");
			String[] account = payInfo.getPay_account().split(",");
			String[] name = payInfo.getPay_name().split(",");
			String[] font = payInfo.getPay_fonturl().split(",");
			String[] back = payInfo.getPay_backurl().split(",");
//			int a = back.length;
			PayInfo np ;
			for (int i = 0; i < back.length; i++) {
				np = new PayInfo();
				np.setCreate_user(userid);
				np.setPay_username(username[i]);
				np.setPay_account(account[i]);
				np.setPay_name(name[i]);
				np.setPay_type("0");
				np.setPay_fonturl(font[i]);
				np.setPay_backurl(back[i]);
				ns.add(np);
			}
		}else if(payWay == 1){ //支付宝支付
			PayInfo payInfo = payInfos.get(1);
			if(payInfo!=null && payInfo.getPay_type() !=null){
				String[] type = payInfo.getPay_type().split(",");
				if("1".equals(type[0])){
					String[] username = payInfo.getPay_username().split(",");
					String[] account = payInfo.getPay_account().split(",");
					String[] mobile = payInfo.getPay_mobile().split(",");
					PayInfo np ;
					for (int i = 0; i < account.length; i++) {
						np = new PayInfo();
						np.setCreate_user(userid);
						np.setPay_username(username[i]);
						np.setPay_account(account[i]);
						np.setPay_mobile(mobile[i]);
						np.setPay_name("微信");
						np.setPay_type("1");
						ns.add(np);
					}
				}	
			}
		 //微信支付
			if(payInfos.size()>2){
				PayInfo payInfo2 = payInfos.get(2);
				if(payInfo2!=null && payInfo2.getPay_type() !=null){
					String[] type = payInfo2.getPay_type().split(",");
					if("2".equals(type[0])){
						String[] username2 = payInfo2.getPay_username().split(",");
						String[] account2 = payInfo2.getPay_account().split(",");
						String[] mobile2 = payInfo2.getPay_mobile().split(",");
						PayInfo np2 ;
						for (int i = 0; i < account2.length; i++) {
							np2 = new PayInfo();
							np2.setCreate_user(userid);
							np2.setPay_username(username2[i]);
							np2.setPay_account(account2[i]);
							np2.setPay_mobile(mobile2[i]);
							np2.setPay_name("支付宝");
							np2.setPay_type("2");
							ns.add(np2);
						}
					}	
				}
			}
		}
		return ns;
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
			//操作店铺保存记录日志(添加日志记录必须在删除之前,因为对应的del_flag=0,如果是删除之后,就不符合条件)
			if("1".equals(office.getGrade())){
				saveOfficeLog(office.getId(),1,"");
			}
			
			//删除操作
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
				map.put("grade", e.getGrade());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			
		}
		return mapList;
	}
	
	/**
	 * 
	 * @Title: parentTreeData
	 * @Description: TODO 选择上级机构排除死循环
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param isGrade
	 * @param response
	 * @return:
	 * @return: List<Map<String,Object>>
	 * @throws
	 * 2017年12月1日 兵子
	 */
	@ResponseBody
	@RequestMapping(value = "parentTreeData")
	public List<Map<String, Object>> parentTreeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
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
				if (!"".equals(extId) && !"0".equals(e.getParentId()) && !extId.equals(e.getId()) && !e.getParentIds().contains(extId)) {
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("name", e.getName());
					if (type != null && "3".equals(type)){
						map.put("isParent", true);
					}
					mapList.add(map);
					}
				if ("".equals(extId)){
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("name", e.getName());
					if (type != null && "3".equals(type)){
						map.put("isParent", true);
					}
					mapList.add(map);
					}
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
	 * 1.深圳方面确认 对于可诺丹婷、罗蜜雅伦、慕伦格尔三个商家都是六级组织机构，并且第六级是店铺。（以下需求只针对可诺丹婷、罗蜜雅伦、慕伦格尔）
	 * 2.现需要在创建和修改1到5级的机构时，不能编辑“是否是店”，默认不是店铺。创建和修改第6级的机构时，不能编辑“是否是店”，默认是店铺。
	 * @param office 上级机构	
	 * @param grade 是否为店铺
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifyLevel")
	public Map<String, String> verifyLevel(HttpServletRequest request,Office office,String grade,String officeId){
		Map<String, String> map = new HashMap<>();
		try {
			// 机构不可为空(空指针)&&商家为可诺丹婷、罗蜜雅伦、慕伦格尔
			if(office != null){
				if("1000001,1000002,1000003".indexOf(office.getFranchisee().getId()) >= 0){
					// (上级机构等级为5&&机构类型为否店铺) || (上级机构等级小于5&&机构类型为是店铺) || (上级机构等级大于5)   
					if((office.getLevel() == 5 && Integer.valueOf(grade) == 2) || (office.getLevel() < 5 && Integer.valueOf(grade) == 1) || office.getLevel() > 5){
						map.put("FLAG", "ERROR");
	    				map.put("MESSAGE", "是否为店铺选择异常");
	    				return map;
					}
					
					Office nowOffice = officeService.get(officeId);
					if(nowOffice != null && "2".equals(grade)){
						if (office.getLevel() != (nowOffice.getLevel()-1)) {	// 三个特殊商家必须同级移动
							map.put("FLAG", "ERROR");
		    				map.put("MESSAGE", "非同层级移动");
		    				return map;
						}
					}
				}
			}else{
				map.put("FLAG", "ERROR");
				map.put("MESSAGE", "非同级移动");
				return map;
			}
		} catch (Exception e) {
			logger.error("上级机构或是否为店铺校验出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "上级机构或是否为店铺校验错误信息", e);
			map.put("FLAG", "ERROR");
			map.put("MESSAGE", "上级机构或是否为店铺选择异常");
			return map;
		}
		map.put("FLAG", "OK");
		return map;
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
							
							//操作店铺保存记录日志
							saveOfficeLog(officeInfo.getId(),0,"导入店铺");
							
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
    					map.put("MESSAGE", "操作成功");
    				}
    				//操作店铺保存记录日志
    				saveOfficeLog(id,("0".equals(isyesno))?2:3,"");
    				
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
			if(!"-1".equals(webReservationTime)){
				logger.info("##### web接口路径:"+webReservationTime);
				String parpm = "{\"type\":\""+type+"\",\"create_by\":\""+createBy+"\",\"file_url\":\""+fileUrl+"\",\"old_url\":\""+oldUrl+"\",\"life_img_urls\":\""+lifeImgUrls+"\",\"user_id\":\""+userId+"\",\"client\":\""+client+"\"}";
				String url=webReservationTime;
				String result = WebUtils.postTrainObject(parpm, url);
				jsonObject = JSONObject.fromObject(result);
				logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message")+",data:"+jsonObject.get("data"));
			}
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
			map.put("grade", e.getGrade());
			mapList.add(map);
		}
    	return mapList;
    }
    
    /**
     * 
     * @Title: checkOfficeCode
     * @Description: TODO 验证机构唯一编码
     * @param office
     * @return:
     * @return: String
     * @throws
     * 2017年12月26日 兵子
     */
    @ResponseBody
    @RequestMapping(value = "checkOfficeCode")
    public String checkOfficeCode(Office office,String oldOfficeCode){
    	if (StringUtils.isNotBlank(office.getOfficeCode()) && office.getOfficeCode().equals(oldOfficeCode)) {
    		return "true";
		}else if(StringUtils.isNotBlank(office.getOfficeCode()) && officeService.checkOfficeCode(office) == null){
			return "true";
		}
    	return "false";
    }
    /**
     * 记录开闭店log
     * @param id
     * @param type	0:添加店铺  1:删除店铺  2:开启店铺  3:隐藏店铺
     * @param title	日志内容(可为空)
     */
    public void saveOfficeLog(String id,int type,String title){
    	//操作店铺保存记录日志
		OfficeLog officeLog = new OfficeLog();
		officeLog.setOfficeId(id);
		switch (type) {
			case 0:
				officeLog.setType(0);
				officeLog.setContent((title.equals(""))?"添加店铺":title);
				break;
			case 1:
				officeLog.setType(1);
				officeLog.setContent((title.equals(""))?"删除店铺":title);
				break;
			case 2:
				officeLog.setType(2);
				officeLog.setContent((title.equals(""))?"开启店铺":title);
				break;
			case 3:
				officeLog.setType(3);
				officeLog.setContent((title.equals(""))?"隐藏店铺":title);
				break;
			default:
				officeLog.setType(99);
				officeLog.setContent("错误数据:"+title);
				break;
		}
		officeLog.setUpdateBy(UserUtils.getUser());
		officeService.saveOfficeLog(officeLog);
    }
    
    /**
     * 查看店铺开闭店日志
     * @param officeLog
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value="shopLogs")
    public String shopLogs(OfficeLog officeLog,HttpServletRequest request,HttpServletResponse response,Model model){
    	try{
    		Page<OfficeLog> page = officeService.queryOfficeLog(new Page<OfficeLog>(request,response),officeLog);
    		model.addAttribute("page",page);
    		model.addAttribute("officeId",officeLog.getOfficeId());
    	}catch (Exception e) {
    		BugLogUtils.saveBugLog(request, "查看店铺开闭店日志", e);
			logger.error("查看店铺开闭店日志出错信息:"+e.getMessage());
		}
    	return "modules/sys/shopLogs";
    }
    /**
     * 去信用额度编辑页面
     * @return
     */
    @RequiresPermissions("sys:office:editCredit")
    @RequestMapping("toEditCredit")
    public String toEditCredit(String office_id,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request){
    	
    	try {
			OfficeAcount officeAcount = this.officeService.findOfficeAcount(office_id);
			if(officeAcount == null){
				officeAcount = new OfficeAcount();
				officeAcount.setOfficeId(office_id);
				this.officeService.saveOfficeAcount(officeAcount);
			}
			model.addAttribute("officeAcount", officeAcount);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "modules/sys/editCreditLimit";
    }
    /**
     * 编辑信用额度
     * @param officeAcount
     * @param redirectAttributes
     * @param request
     * @return
     */
    @RequestMapping("updateOfficeCreditLimit")
    public String updateOfficeCreditLimit(OfficeAcount officeAcount,RedirectAttributes redirectAttributes,HttpServletRequest request){
    	try {
			this.officeService.updateOfficeCreditLimit(officeAcount);
			addMessage(redirectAttributes, "变更信用额度成功");
		}catch (RuntimeException e) {
 			e.printStackTrace();
 			BugLogUtils.saveBugLog(request, "变更信用额度", e);
 			addMessage(redirectAttributes, e.getMessage());
 		}catch (Exception e) {
			e.printStackTrace();
			BugLogUtils.saveBugLog(request, "变更信用额度", e);
			addMessage(redirectAttributes, "变更信用额度失败");
		}
    	return "redirect:" + adminPath + "/sys/office/list"; 
    }
}

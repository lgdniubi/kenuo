package com.training.modules.sys.web;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.BankAccount;

import net.sf.json.JSONObject;

/**
 * 加盟商管理Controller
 * @author kele
 * @version 2016-6-4
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/franchisee")
public class FranchiseeController extends BaseController{
	
	@Autowired
	private FranchiseeService franchiseeService;
	
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 加盟商列表
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:franchisee:index")
	@RequestMapping(value = {"list"})
	public String list(Franchisee franchisee,Model model){
		model.addAttribute("list", franchiseeService.findAllList(franchisee));
		return "modules/sys/franchiseeList";
	}
	
	/**
	 * 加盟商查看
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:franchisee:view","sys:franchisee:add","sys:franchisee:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(Franchisee franchisee,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(franchisee.getId())){
			franchisee = franchiseeService.get(franchisee);
			List<BankAccount> bankAccount = franchiseeService.findBankAccountList(franchisee);
			franchisee.setBankAccount(bankAccount);
		}else{
			//添加或者修改
			franchisee.setParent(franchiseeService.get(franchisee.getParent().getId()));
			//id为null或者"" 时，则为添加下级菜单时，code自增
			List<Franchisee> l = franchiseeService.findListbyPID(franchisee.getParentId());
			long size=0;
			if(l.size()==0){
				size=0;
				franchisee.setCode(StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
			}else{
				for (int i = 0; i < l.size(); i++) {
					Franchisee e = l.get(i);
					size=Long.valueOf(e.getCode())+1;
				}
				franchisee.setCode(String.valueOf(size));
			}
		}
			
		model.addAttribute("franchisee", franchisee);
		return "modules/sys/franchiseeFrom";
	}
	
	
	/**
	 * 加盟商-保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:franchisee:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(Franchisee franchisee, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			User user = UserUtils.getUser();
			//进行保存
			if (franchisee.getIsNewRecord()){
				franchisee.setParentIds(franchisee.getParentIds()+franchisee.getParent().getId()+",");
				
				if (StringUtils.isNotBlank(user.getId())){
					franchisee.setUpdateBy(user);
					franchisee.setCreateBy(user);
				}
				franchisee.setCreateDate(new Date());
				franchisee.setUpdateDate(new Date());
				
				//保存商家表,id自动增长，修改成功后返回id
				String id = franchiseeService.saveFranchisee(franchisee,request);
				logger.debug("#####id:"+id);
				
				//保存组织机构信息，给组织机构添加一条父类数据
				Office office = new Office();
				office.setId(id);
				Office parent = new Office();
				parent.setId("1");
				office.setParent(parent);
				office.setParentIds("0,"+parent.getId()+",");
				office.setName(franchisee.getName());
				office.setArea(franchisee.getArea());
				office.setSort(10);
				office.setCode(franchisee.getCode());
				office.setType("1");
				office.setGrade("2");
				office.setUseable("1");
				if (StringUtils.isNotBlank(user.getId())){
					office.setUpdateBy(user);
					office.setCreateBy(user);
				}
				office.setCreateDate(new Date());
				office.setUpdateDate(new Date());
				office.setDelFlag("0");
				office.setIconUrl(franchisee.getIconUrl());
				officeService.franchiseeSaveOffice(office);//保存组织结构表
				
				addMessage(redirectAttributes, "保存机构'" + franchisee.getName() + "'成功");
			}else{
				//修改商家信息表
				if (StringUtils.isNotBlank(user.getId())){
					franchisee.setUpdateBy(user);
				}
				franchiseeService.update(franchisee);
				
				//修改组织架构表
				Office office = new Office();
				office.setId(franchisee.getId());
				office.setName(franchisee.getName());
				office.setIconUrl(franchisee.getIconUrl());
				office.setArea(franchisee.getArea());
				office.setType(franchisee.getType());
				if (StringUtils.isNotBlank(user.getId())){
					office.setUpdateBy(user);
				}
				office.setUpdateDate(new Date());
				officeService.franchiseeUpdateOffice(office);//修改组织架构表
				
				addMessage(redirectAttributes, "修改机构'" + franchisee.getName() + "'成功");
			}
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/sys/franchisee/list";
	}
	
	
	/**
	 * 加盟商删除
	 * @param office
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"sys:franchisee:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(Franchisee franchisee, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		
		try {
			if (StringUtils.isNotBlank(franchisee.getId())) {
				Franchisee fran = franchiseeService.get(franchisee);
				//删除商家信息
				franchiseeService.delete(franchisee);
				//把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
				franchiseeService.deleteMtmyFranchisee(franchisee.getId());
				
				//删除组织结构信息（office）
				Office office = new Office();
				office.setId(franchisee.getId());
				officeService.delete(office);
				
				//删除商家同步数据到供应链
				String weburl = ParametersFactory.getMtmyParamValues("fzx_equally_franchisee");
				logger.info("##### web接口路径:"+weburl);
				String parpm = "{\"id\":"+Integer.valueOf(fran.getId())+",\"name\":\""+fran.getName()+"\",\"type\":\""+fran.getType()+"\","
						+ "\"address\":\""+fran.getAddress()+"\",\"legal_name\":\""+fran.getLegalName()+"\",\"contacts\":\""+fran.getContacts()+"\",\"mobile\":\""+fran.getMobile()+"\","
						+ "\"tel\":\""+fran.getTel()+"\",\"charter_url\":\""+fran.getCharterUrl()+"\",\"taxation_url\":\""+fran.getTaxationUrl()+"\","
						+ "\"bank_beneficiary\":\""+fran.getBankBeneficiary()+"\",\"bank_code\":\""+fran.getBankCode()+"\",\"bank_owner\":\""+fran.getBankName()+"\",\"is_delete\":"+1+",\"function\":\""+1+"\"}";
				String url=weburl;
				String result = WebUtils.postCSObject(parpm, url);
				JSONObject jsonObject = JSONObject.fromObject(result);
				logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
				addMessage(redirectAttributes, "删除商家信息成功");
			}
		} catch (Exception e) {
			logger.error("删除商家错误信息："+e.getMessage());
    		BugLogUtils.saveBugLog(request, "删除商家失败", e);
		}
		return "redirect:" + adminPath + "/sys/franchisee/list";
	}
	
	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：直营；2：加盟；）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Franchisee> list = franchiseeService.findAllList(null);
		for (int i=0; i<list.size(); i++){
			Franchisee e = list.get(i);
			//&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
			//&& (grade == null || (grade != null && Global.YES.equals(e.getStatus())))
			if ((StringUtils.isBlank(extId) || 
					/*(extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))){*/
					(extId!=null && !extId.equals(e.getId())))){
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
		}
		return mapList;
	}

	/**
	 * 公共商品服务标识(0: 做 1: 不做)
	 * @param franchisee
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "changeType")
	public Map<String, String> changeType(Franchisee franchisee, RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
    	try {
    		franchiseeService.updatePublicServiceFlag(franchisee);
    		franchiseeService.updateMtmyPublicServiceFlag(franchisee);//同步mtmy商家表中修改公共商品服务标识
			map.put("FLAG", "OK");
			map.put("MESSAGE", "修改成功");
		} catch (Exception e) {
			logger.error("修改商家公共商品服务标识错误信息："+e.getMessage());
    		BugLogUtils.saveBugLog(request, "商家公共商品服务标识修改失败", e);
    		map.put("FLAG", "ERROR");
    		map.put("MESSAGE", "修改失败");
		}
    	return map;
	}
	
	/**
	 * 跳转商家详情页
	 * @param franchisee
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="franchiseeDsecriptionForm")
	public String franchiseeDsecriptionForm(Franchisee franchisee,HttpServletRequest request,Model model){
		try{
			franchisee = franchiseeService.get(franchisee.getId());
			model.addAttribute("franchisee", franchisee);
		}catch(Exception e){
			logger.error("跳转商家详情页出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "跳转商家详情页出现异常", e);
		}
		return "modules/sys/franchiseeDsecriptionForm";
	}
	
	/**
	 * 保存商家详情
	 * @param franchisee
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="saveFranchiseeDsecription")
	public String saveFranchiseeDsecription(Franchisee franchisee,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			franchiseeService.saveFranchiseeDescription(franchisee.getDescription(),franchisee.getId());
			franchiseeService.saveMtmyFranchiseeDescription(franchisee.getDescription(),franchisee.getId());
			addMessage(redirectAttributes, "保存成功");
		}catch(Exception e){
			logger.error("保存商家详情出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存商家详情出现异常", e);
			addMessage(redirectAttributes, "保存失败");
		}
		return "redirect:" + adminPath + "/sys/franchisee/list";
	}
}

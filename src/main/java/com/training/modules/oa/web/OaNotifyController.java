/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.oa.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.oa.entity.OaNotifyRecord;
import com.training.modules.oa.service.OaNotifyService;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.service.TrainCategorysService;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * 通知通告Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaNotify")
public class OaNotifyController extends BaseController {

	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private TrainCategorysService trainCategorysService;
	
	
	@ModelAttribute
	public OaNotify get(@RequestParam(required=false) String id) {
		OaNotify entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaNotifyService.get(id);
		}
		if (entity == null){
			entity = new OaNotify();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaNotify:list")
	@RequestMapping(value = {"list", ""})
	public String list(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		model.addAttribute("page", page);
		return "modules/oa/oaNotifyList";
	}

	/**
	 * 查看，增加，编辑报告表单页面
	 */
	@RequiresPermissions(value={"oa:oaNotify:view","oa:oaNotify:add","oa:oaNotify:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OaNotify oaNotify, Model model,boolean isSelf) {
		if (StringUtils.isNotBlank(oaNotify.getId())){
			oaNotify = oaNotifyService.getRecordList(oaNotify);
			if("4".equals(oaNotify.getType())){
				oaNotify.setLessonId(oaNotify.getContent());
				oaNotify.setLessonName(trainCategorysService.findbyid(oaNotify).getLessonName());
			}else if("5".equals(oaNotify.getType())){
				oaNotify.setCategoryId(oaNotify.getContent());
				oaNotify.setName(trainCategorysService.findbyid(oaNotify).getName());
			}else{
				oaNotify.setContent(oaNotify.getContent());
			}
		}
		//开始时间为空时  设置开始时间为当前时间
		if (oaNotify.getStart() == null){
			oaNotify.setStart(DateUtils.getDate());
		}
		oaNotify.setSelf(isSelf);
		model.addAttribute("oaNotify", oaNotify);
		return "modules/oa/oaNotifyForm";
	}

	@RequiresPermissions(value={"oa:oaNotify:add","oa:oaNotify:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(OaNotify oaNotify,Model model,boolean isSelf, RedirectAttributes redirectAttributes) {
		oaNotify.setTitle(HtmlUtils.htmlUnescape(oaNotify.getTitle()));
		if (!beanValidator(model, oaNotify)){
			return form(oaNotify, model, isSelf);
		}
		// 如果是修改，则状态为已发布，则不能再进行操作
		if (StringUtils.isNotBlank(oaNotify.getId())){
			OaNotify e = oaNotifyService.get(oaNotify.getId());
			if ("1".equals(e.getStatus())){
				addMessage(redirectAttributes, "已发布，不能操作！");
				if(isSelf)
					return "redirect:" + adminPath + "/oa/oaNotify/self?repage";
				else
					return "redirect:" + adminPath + "/oa/oaNotify/?repage";
			}
		}
		//判断通知类型
		if("4".equals(oaNotify.getType())){
			oaNotify.setContent(oaNotify.getLessonId());
		}else if("5".equals(oaNotify.getType())){
			oaNotify.setContent(oaNotify.getCategoryId());
		}else{
			oaNotify.setContent(oaNotify.getContent());
		}
		oaNotifyService.save(oaNotify);
		addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");
		if(isSelf)
			return "redirect:" + adminPath + "/oa/oaNotify/self?repage";
		else
			return "redirect:" + adminPath + "/oa/oaNotify/?repage";
	}
	
	@RequiresPermissions("oa:oaNotify:del")
	@RequestMapping(value = "delete")
	public String delete(OaNotify oaNotify,boolean isSelf, RedirectAttributes redirectAttributes) {
		oaNotify.setSelf(isSelf);
		oaNotifyService.delete(oaNotify);
		addMessage(redirectAttributes, "删除通知成功");
		if(isSelf)
			return "redirect:" + adminPath + "/oa/oaNotify/self?repage";
		else
			return "redirect:" + adminPath + "/oa/oaNotify/?repage";
	}
	
	@RequiresPermissions("oa:oaNotify:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids,boolean isSelf, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			oaNotifyService.delete(oaNotifyService.get(id));
		}
		addMessage(redirectAttributes, "删除通知成功");
		if(isSelf)
			return "redirect:" + adminPath + "/oa/oaNotify/self?repage";
		else
			return "redirect:" + adminPath + "/oa/oaNotify/?repage";
	}
	
	/**
	 * 我的通知列表
	 */
	@RequestMapping(value = "self")
	public String selfList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaNotify.setSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify); 
		model.addAttribute("page", page);
		return "modules/oa/oaNotifySelfList";
	}

	/**
	 * 我的通知列表-数据
	 */
	@RequiresPermissions("oa:oaNotify:view")
	@RequestMapping(value = "selfData")
	@ResponseBody
	public Page<OaNotify> listData(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaNotify.setSelf(true);
		Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);
		return page;
	}
	
	/**
	 * 查看我的通知,重定向在当前页面打开
	 */
	@RequestMapping(value = "view")
	public String view(OaNotify oaNotify, Model model) {
		if (StringUtils.isNotBlank(oaNotify.getId())){
			oaNotifyService.updateReadFlag(oaNotify);
			oaNotify = oaNotifyService.getRecordList(oaNotify);
			model.addAttribute("oaNotify", oaNotify);
			return "modules/oa/oaNotifyForm";
		}
		return "redirect:" + adminPath + "/oa/oaNotify/self?repage";
	}

	/**
	 * 查看我的通知-数据
	 */
	@RequestMapping(value = "viewData")
	@ResponseBody
	public OaNotify viewData(OaNotify oaNotify, Model model) {
		if (StringUtils.isNotBlank(oaNotify.getId())){
			oaNotifyService.updateReadFlag(oaNotify);
			return oaNotify;
		}
		return null;
	}
	
	/**
	 * 查看我的通知-发送记录
	 */
	@RequestMapping(value = "viewRecordData")
	@ResponseBody
	public OaNotify viewRecordData(OaNotify oaNotify, Model model) {
		if (StringUtils.isNotBlank(oaNotify.getId())){
			oaNotify = oaNotifyService.getRecordList(oaNotify);
			return oaNotify;
		}
		return null;
	}
	
	/**
	 * 获取我的通知数目
	 */
	@RequestMapping(value = "self/count")
	@ResponseBody
	public String selfCount(OaNotify oaNotify, Model model) {
		oaNotify.setSelf(true);
		oaNotify.setReadFlag("0");
		return String.valueOf(oaNotifyService.findCount(oaNotify));
	}
	
	@RequiresPermissions("oa:oaNotify:pushMsg")
	@RequestMapping("pushMsg")
	public @ResponseBody Map<String, Object> pushMsg(@RequestParam String notify_id,@RequestParam Integer push_type){
		
		Map<String, Object> map  = (Map) JsonMapper.fromJsonString(this.oaNotifyService.pushMsg(notify_id, push_type), Map.class);
		if("200".equals(map.get("result"))){
			this.oaNotifyService.updateStatus(notify_id, 1);
		}
		
		return map;
	}
	@RequiresPermissions("oa:oaNotify:getPushResult")
	@RequestMapping("getPushResult")
	public @ResponseBody Map<String, Object> getPushResult(@RequestParam String notify_id,@RequestParam String content_id){
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("notify_id", notify_id);
		m.put("content_id", content_id);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		//  用于加密
		JSONObject jsonObj = JSONObject.fromObject(m);
		String sign = WebUtils.MD5("train"+jsonObj+"train");
		String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+jsonObj+"'}";
		
		HttpEntity<String> entity = new HttpEntity<String>(paramter,headers);
		
		String pushresult_url = ParametersFactory.getMtmyParamValues("pushresult_url");
		//Global.getInstance().getConfig("getPushResult")
		
		String json = restTemplate.postForObject(pushresult_url, entity, String.class);
		
		Map<String, Object> map = (Map<String, Object>) JsonMapper.fromJsonString(json, Map.class);
		
		if("200".equals(map.get("result"))){
			this.oaNotifyService.updateStatus(notify_id, 2);
		}
		return map;
	}
	/**
	 * 列推界面跳转
	 * @return
	 */
	@RequestMapping(value = "oaList")
	public String oaList(OaNotify oaNotify,Model model) {
		if(StringUtils.isNotBlank(oaNotify.getId())){
			List<OaNotifyRecord> list = oaNotifyService.getUser(oaNotify); 
			model.addAttribute("list", list);
		}
		return "modules/oa/oaUsersFrom";
	}
}
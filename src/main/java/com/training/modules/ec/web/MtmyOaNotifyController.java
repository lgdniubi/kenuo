/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.MtmyOaNotify;
import com.training.modules.ec.entity.MtmyOaNotifyRecord;
import com.training.modules.ec.service.MtmyOaNotifyService;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.sys.utils.BugLogUtils;

import net.sf.json.JSONObject;

/**
 * 每天每夜通知通告Controller
 * 
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyOaNotify")
public class MtmyOaNotifyController extends BaseController {

	@Autowired
	private MtmyOaNotifyService mtmyOaNotifyService;
	
	@ModelAttribute
	public MtmyOaNotify get(@RequestParam(required=false) String id) {
		MtmyOaNotify entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mtmyOaNotifyService.get(id);
		}
		if (entity == null){
			entity = new MtmyOaNotify();
		}
		return entity;
	}
	
	/**
	 * 查看所有通知
	 * @param mtmyOaNotify
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:mtmyOaNotify:list")
	@RequestMapping(value = {"list", ""})
	public String list(MtmyOaNotify mtmyOaNotify,Model model, HttpServletRequest request, HttpServletResponse response) {
		Page<MtmyOaNotify> page = mtmyOaNotifyService.find(new Page<MtmyOaNotify>(request, response), mtmyOaNotify);
		model.addAttribute("mtmyOaNotify", mtmyOaNotify);
		model.addAttribute("page", page);
		return "modules/ec/mtmyOaNotifyList";
	}
	/**
	 * 查看、添加、修改 通知
	 * @param mtmyOaNotify
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyOaNotify:view","ec:mtmyOaNotify:add","ec:mtmyOaNotify:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form", ""})
	public String form(MtmyOaNotify mtmyOaNotify,Model model){
		
		if (StringUtils.isNotBlank(mtmyOaNotify.getId())){
			mtmyOaNotify = mtmyOaNotifyService.getRecordList(mtmyOaNotify);
		}
		
		model.addAttribute("mtmyOaNotify", mtmyOaNotify);
		return "modules/ec/mtmyOaNotifyForm";
	}
	/**
	 * 保存通知
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyOaNotify:add","ec:mtmyOaNotify:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MtmyOaNotify mtmyOaNotify,Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			if (!beanValidator(model, mtmyOaNotify)){
				return form(mtmyOaNotify, model);
			}
			// 如果是修改，则状态为已发布，则不能再进行操作
			if (StringUtils.isNotBlank(mtmyOaNotify.getId())){
				MtmyOaNotify e = mtmyOaNotifyService.get(mtmyOaNotify.getId());
				if ("1".equals(e.getStatus())){
					addMessage(redirectAttributes, "已发布，不能操作！");
					return "redirect:" + adminPath + "/oa/oaNotify/?repage";
				}
			}
			mtmyOaNotifyService.save(mtmyOaNotify);
			addMessage(redirectAttributes, "保存/修改通知'" + mtmyOaNotify.getTitle() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改通知", e);
			logger.error("保存/修改通知错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改通知出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
	}
	
	/**
	 * 删除单条通知
	 * @param mtmyOaNotify
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyOaNotify:del")
	@RequestMapping(value = "delete")
	public String delete(MtmyOaNotify mtmyOaNotify, RedirectAttributes redirectAttributes) {
		mtmyOaNotifyService.delete(mtmyOaNotify);
		addMessage(redirectAttributes, "删除通知成功");
		return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:oaNotify:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			mtmyOaNotifyService.delete(mtmyOaNotifyService.get(id));
		}
		addMessage(redirectAttributes, "删除通知成功");
		return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
	}
	
	/**
	 * 推送
	 * @param notify_id
	 * @param push_type
	 * @return
	 */
	@RequiresPermissions("ec:mtmyOaNotify:pushMsg")
	@RequestMapping("pushMsg")
	public @ResponseBody Map<String, Object> pushMsg(@RequestParam String notify_id,@RequestParam Integer push_type){
		
		Map<String, Object> map = null;
		try {
			map = (Map) JsonMapper.fromJsonString(this.mtmyOaNotifyService.pushMsg(notify_id, push_type), Map.class);
			System.out.println(map.get("result"));
			if("200".equals(map.get("result"))){
				this.mtmyOaNotifyService.updateStatus(notify_id, 1);
			}
			
		} catch (ResponseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return map;
	}
	/**
	 * 获取结果
	 * @param notify_id
	 * @param content_id
	 * @return
	 * @throws ResponseError 
	 */
	@RequiresPermissions("ec:mtmyOaNotify:getPushResult")
	@RequestMapping("getPushResult")
	public @ResponseBody Map<String, Object> getPushResult(@RequestParam String notify_id,@RequestParam String content_id){
		Map<String, Object> map = null;
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("notify_id", notify_id);
			m.put("content_id", content_id);
			JSONObject jsStr = JSONObject.fromObject(m); 
			String resutl = GetTUtil.getPushResult(jsStr);
			map = (Map) JsonMapper.fromJsonString(resutl, Map.class);
		} catch (Exception e) {
		}
		return map;
	}
	
	/**
	 * 列推界面跳转
	 * @return
	 */
	@RequestMapping(value = "oaList")
	public String oaList(MtmyOaNotify mtmyOaNotify,Model model) {
		if(StringUtils.isNotBlank(mtmyOaNotify.getId())){
			List<MtmyOaNotifyRecord> list = mtmyOaNotifyService.getUsers(mtmyOaNotify);
			model.addAttribute("list", list);
		}
		return "modules/ec/mtmyOaUsersFrom";
	}
}
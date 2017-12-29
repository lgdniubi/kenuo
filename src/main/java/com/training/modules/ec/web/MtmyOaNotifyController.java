/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Lists;
import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsSkill;
import com.training.modules.ec.entity.MtmyOaNotify;
import com.training.modules.ec.entity.MtmyOaNotifyRecord;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.MtmyOaNotifyService;
import com.training.modules.ec.service.MtmyUsersService;
import com.training.modules.ec.utils.OrdersStatusChangeUtils;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

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
	
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	
	@Autowired
	private MtmyUsersService mtmyUsersService;
	
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
	@RequestMapping(value = {"list"})
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
	@RequestMapping(value = {"form"})
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
			mtmyOaNotify.setTitle(HtmlUtils.htmlUnescape(mtmyOaNotify.getTitle()));
			//当推送方式为组推时
			if(mtmyOaNotify.getPushType() == 2){
				List<MtmyOaNotifyRecord> mtmyOaNotifyRecordList = Lists.newArrayList();
				String phones = mtmyOaNotify.getPhones();
				//当导入的手机号不为空时
				if(!"".equals(phones) && phones != null){
					//手机号码去重
					String[] strs = phones.split(",");
					Set<String> set = new HashSet<>();  
			        for(int i=0;i<strs.length;i++){  
			            set.add(strs[i]);  
			        }  
			        String[] arrayResult = (String[]) set.toArray(new String[set.size()]);  
					for(int i=0;i<arrayResult.length;i++){
						Users users = mtmyUsersService.getUserByMobile(arrayResult[i]);
						//当用户不为空时
						if(users != null){
							if(mtmyOaNotifyService.selectClient(users.getUserid()) > 0){
								MtmyOaNotifyRecord mtmyOaNotifyRecord = new MtmyOaNotifyRecord();
								users.setId(String.valueOf(users.getUserid()));
								mtmyOaNotifyRecord.setUsers(users);
								mtmyOaNotifyRecord.setId(IdGen.uuid());
								mtmyOaNotifyRecord.setMtmyOaNotify(mtmyOaNotify);
								mtmyOaNotifyRecordList.add(mtmyOaNotifyRecord);
							}
						}
					}
				}
				mtmyOaNotify.setMtmyOaNotifyRecordList(mtmyOaNotifyRecordList);
				// 如果是修改，且状态为已发布，则不能再进行操作
				if(StringUtils.isNotBlank(mtmyOaNotify.getId())){
					MtmyOaNotify e = mtmyOaNotifyService.get(mtmyOaNotify.getId());
					if ("1".equals(e.getStatus())){
						addMessage(redirectAttributes, "已发布，不能操作！");
						return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
					}
				}else{    // 如果是修改，且状态为未发布，则继续操作，判断用户是否存在、是否登陆过客户端
					if(mtmyOaNotify.getMtmyOaNotifyRecordList().size() <= 0){
						addMessage(redirectAttributes, "推送的用户不存在或未登录过客户端导致保存/修改失败");
						return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
					}
				}
			}
			
			if (!beanValidator(model, mtmyOaNotify)){
				return form(mtmyOaNotify, model);
			}
			// 如果是修改，则状态为已发布，则不能再进行操作
			if (StringUtils.isNotBlank(mtmyOaNotify.getId())){
				MtmyOaNotify e = mtmyOaNotifyService.get(mtmyOaNotify.getId());
				if ("1".equals(e.getStatus())){
					addMessage(redirectAttributes, "已发布，不能操作！");
					return "redirect:" + adminPath + "/ec/mtmyOaNotify/list";
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
	public String oaList(MtmyOaNotify mtmyOaNotify,HttpServletRequest request,Model model) {
		List<MtmyOaNotifyRecord> list = new ArrayList<MtmyOaNotifyRecord>();
		if(StringUtils.isNotBlank(mtmyOaNotify.getId())){
			list = mtmyOaNotifyService.getUsers(mtmyOaNotify);
			model.addAttribute("list", list);
		}
		if("editNames".equals(request.getParameter("flag"))){
			String mtmyOaNotifyRecordIds = request.getParameter("mtmyOaNotifyRecordIds");
			String mtmyOaNotifyRecordNames = request.getParameter("mtmyOaNotifyRecordNames");
			String[] ids = mtmyOaNotifyRecordIds.split(",");
			String[] names = mtmyOaNotifyRecordNames.split(",");
			for (int i = 0; i < ids.length; i++) {
				MtmyOaNotifyRecord mtmyOaNotifyRecord = new MtmyOaNotifyRecord();
				Users users = new Users();
				users.setId(ids[i]);
				users.setNickname(names[i]);
				mtmyOaNotifyRecord.setUsers(users);
				list.add(mtmyOaNotifyRecord);
			}
			model.addAttribute("list", list);
		}
		return "modules/ec/mtmyOaUsersFrom";
	}
	
	
	/**
	 * 下载导入组推手机号模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "importPhonesOrders/template")
	public void importVirtualOrdersTemplate(MtmyOaNotify mtmyOaNotify,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			String filename = "phonesImport.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[组推手机号模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[组推手机号模板-new-path"+path);
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
			BugLogUtils.saveBugLog(request, "导入模板下载失败", e);
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
	}
	
	/**
	 * 导入组推手机号
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "importPhones")
	@ResponseBody
	public String importPhones(HttpServletRequest request,MultipartFile file, RedirectAttributes redirectAttributes) {
		JSONObject jsonO = new JSONObject();
		StringBuffer str = new StringBuffer();
		try {
			ImportExcel ei = new ImportExcel(file, 1, 0);
			int cell = ei.getLastCellNum();
			if (cell != 1) {
				jsonO.put("type", "error");
			}else{
				List<MtmyOaNotify> list = ei.getDataList(MtmyOaNotify.class);
				for(MtmyOaNotify mtmyOaNotify : list){
					str.append(mtmyOaNotify.getPhones()+",");
				}
				jsonO.put("phones", str.toString());
				jsonO.put("type", "success");
			}
			addMessage(redirectAttributes, "成功！");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "导入组推手机号出错", e);
			logger.error("导入组推手机号出错信息：" + e.getMessage());
			jsonO.put("type", "error");
		}
		return jsonO.toString();
		
	}
	
}
package com.training.modules.train.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLivePlayback;
import com.training.modules.train.entity.TrainLiveRoom;
import com.training.modules.train.entity.TrainLiveUser;
import com.training.modules.train.service.TrainLiveAuditService;
import com.training.modules.train.service.TrainLiveRoomService;
import com.training.modules.train.service.TrainLiveUserService;
import com.training.modules.train.utils.EncryptLiveUtil;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/train/live")
public class TrainLiveAuditController extends BaseController{
	
	@Autowired
	private TrainLiveAuditService trainLiveAuditService;
	@Autowired
	private TrainLiveRoomService trainLiveRoomService;
	@Autowired
	private TrainLiveUserService trainLiveUserService;

	
	
	
	public static String LIVE_USERID="E8F7E756412DC768";   //直播用户id   E8F7E756412DC768
	public static String API_KEY="K4MV4Mv4Q90FaEEQYclkz0XJIqEZf5rK";  	//API KEY  K4MV4Mv4Q90FaEEQYclkz0XJIqEZf5rK 
	
	@ModelAttribute
	public TrainLiveAudit get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return trainLiveAuditService.get(id);
		} else {
			return new TrainLiveAudit();
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
	@RequiresPermissions("train:live:view")
	@RequestMapping(value = { "list", "" })
	public String list(TrainLiveAudit trainLiveAudit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainLiveAudit> page = trainLiveAuditService.findLive(new Page<TrainLiveAudit>(request, response), trainLiveAudit);
		model.addAttribute("page", page);
		return "modules/train/LiveList";
	}
	
	

	/**
	 * 分页查询 条件查询
	 * @param trainLiveAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:live:view")
	@RequestMapping(value = { "backform"})
	public String backform(TrainLivePlayback trainLivePlayback, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainLivePlayback> page = trainLiveAuditService.findback(new Page<TrainLivePlayback>(request, response), trainLivePlayback);
		model.addAttribute("page", page);
		return "modules/train/backList";
	}
	
	
	/**
	 * 分页查询 条件查询
	 * @param trainLiveAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:live:view")
	@RequestMapping(value = { "liveUserform"})
	public String liveUserform(TrainLiveUser trainLiveUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainLiveUser> page = trainLiveUserService.findLiveUser(new Page<TrainLiveUser>(request, response), trainLiveUser);
		model.addAttribute("page", page);
		return "modules/train/liveUList";
	}
	/**
	 * 编辑直播
	 * @param request
	 * @param trainLiveAudit
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "train:live:edit"}, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,TrainLiveAudit trainLiveAudit, Model model) {
		try{
			model.addAttribute("trainLiveAudit", trainLiveAudit);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播编辑", e);
			logger.error("直播编辑：" + e.getMessage());
		}

		return "modules/train/LiveForm";
	}
	
	/**
	 * 保存数据
	 * userid	用户id	
	 * name	直播间名称	
	 * desc	直播间描述
	 * templatetype	直播模板类型，请求模板信息接口可获得模板类型的详细信息。	
	 * authtype	验证方式，0：接口验证，需要填写下面的checkurl；1：密码验证，需要填写下面的playpass；2：免密码验证
	 * publisherpass	推流端密码，即讲师密码	
	 * assistantpass	助教端密码	
	 * playpass	播放端密码	可选
	 * checkurl	验证地址	可选
	 * barrage	是否开启弹幕。0：不开启；1：开启
	 * foreignpublish	是否开启第三方推流。0：不开启；1：开启
	 * openlowdelaymode	开启直播低延时模式。0为关闭；1为开启	可选，默认为开启，若对直播实时性要求不高可以关闭该模式。
	 * showusercount	在页面显示当前在线人数。0表示不显示；1表示显示	可选，默认显示当前人数，模板一暂不支持此设置
	 * @param trainLiveAudit
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "train:live:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TrainLiveAudit trainLiveAudit, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Map<String, String> map = new TreeMap<String, String>();
		String adminName=UserUtils.getUser().getName();
		Date date=new Date();
		long time=date.getTime();
		try {
			trainLiveAudit.setAuditUser(adminName);
			trainLiveAuditService.update(trainLiveAudit);
			if(trainLiveAudit.getAuditStatus().equals("2")){
				int num=trainLiveRoomService.findByUserId(trainLiveAudit.getUserId());
				if(num>0){
					if(trainLiveAudit.getAuditStatus().equals(trainLiveAudit.getOldStatus())){
						
					}else{  //直播间已经创建  修改直播间
						try {
							TrainLiveRoom trainLiveRoom=trainLiveRoomService.get(trainLiveAudit.getUserId());
							TrainLiveAudit trainLive=get(trainLiveAudit.getId());
							map.put("roomid",trainLiveRoom.getRoomId());
							map.put("userid",URLEncoder.encode(LIVE_USERID,"utf-8"));
							map.put("name",URLEncoder.encode(trainLive.getName(),"utf-8"));
							map.put("desc",URLEncoder.encode(trainLive.getDesc(),"utf-8"));
							map.put("templatetype",URLEncoder.encode("2","utf-8"));
							map.put("authtype",URLEncoder.encode("1","utf-8")); //authtype
							map.put("publisherpass",URLEncoder.encode("123456","utf-8"));
							map.put("assistantpass",URLEncoder.encode("123456","utf-8"));
							map.put("playpass",URLEncoder.encode("123456","utf-8"));
							map.put("barrage",URLEncoder.encode("1","utf-8"));
							map.put("foreignpublish",URLEncoder.encode("0","utf-8"));
							map.put("openlowdelaymode",URLEncoder.encode("0","utf-8"));
							map.put("showusercount",URLEncoder.encode("1","utf-8"));
							String	qString=EncryptLiveUtil.createHashedQueryString(map,time,API_KEY);
							String result=EncryptLiveUtil.UpdateLiveGet(qString);
							JSONObject json = JSONObject.fromObject(result);
							System.out.println(json.getString("result"));
							if(json.getString("result").equals("OK")){
								addMessage(redirectAttributes,"保存成功！");
							}else{
								addMessage(redirectAttributes, "直播修改失败!");
							}
						} catch (Exception e) {
							BugLogUtils.saveBugLog(request, "直播修改失败!", e);
							logger.error("直播修改失败：" + e.getMessage());
							addMessage(redirectAttributes, "直播修改失败!");
						}
						
					
					}
				}else{		//用户没有直播间 创建新的直播间
					try {
						TrainLiveAudit trainLive=get(trainLiveAudit.getId());
						map.put("userid",URLEncoder.encode(LIVE_USERID,"utf-8"));
						map.put("name",URLEncoder.encode(trainLive.getName(),"utf-8"));
						map.put("desc",URLEncoder.encode(trainLive.getDesc(),"utf-8"));
						map.put("templatetype",URLEncoder.encode("2","utf-8"));
						map.put("authtype",URLEncoder.encode("1","utf-8")); //authtype
						map.put("publisherpass",URLEncoder.encode("123456","utf-8"));
						map.put("assistantpass",URLEncoder.encode("123456","utf-8"));
						map.put("playpass",URLEncoder.encode("123456","utf-8"));
						map.put("barrage",URLEncoder.encode("1","utf-8"));
						map.put("foreignpublish",URLEncoder.encode("0","utf-8"));
						map.put("openlowdelaymode",URLEncoder.encode("0","utf-8"));
						map.put("showusercount",URLEncoder.encode("1","utf-8"));
						String	qString=EncryptLiveUtil.createHashedQueryString(map,time,API_KEY);
						String result=EncryptLiveUtil.CreateLiveGet(qString);
						JSONObject json = JSONObject.fromObject(result);
						System.out.println(json.getString("result"));
						if(json.getString("result").equals("OK")){
							JSONObject jsond= JSONObject.fromObject(json.getString("room"));
							TrainLiveRoom trainLiveRoom=new TrainLiveRoom();
							trainLiveRoom.setUserId(trainLive.getUserId());
							trainLiveRoom.setRoomId(jsond.getString("id"));
							trainLiveRoom.setPublishUrl("");
							trainLiveRoomService.insertRoom(trainLiveRoom);
							addMessage(redirectAttributes,"保存成功！");
						}else{
							addMessage(redirectAttributes, "创建直播失败!");
						}

					} catch (Exception e) {
						BugLogUtils.saveBugLog(request, "创建直播失败!", e);
						logger.error("创建直播失败：" + e.getMessage());
						addMessage(redirectAttributes, "创建直播失败!");
					}
				}
			}
			if(!trainLiveAudit.getOldStatus().equals(trainLiveAudit.getAuditStatus())){
			// 0 审核失败  2 审核通过  
				if("0".equals(trainLiveAudit.getAuditStatus()) || "2".equals(trainLiveAudit.getAuditStatus())){
					List<String> list=new ArrayList<String>();
					list.add(trainLiveAudit.getUserId());
					trainLiveAuditService.pushMsg(trainLiveAudit.getAuditStatus(),list,trainLiveAudit.getBengTime());  //推送信息
				}
			}
			addMessage(redirectAttributes,"保存成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播保存失败", e);
			logger.error("方法：save，直播：" + e.getMessage());
			addMessage(redirectAttributes, "直播保存失败");
		}

		return "redirect:" + adminPath + "/train/live/list";

	}
	

}

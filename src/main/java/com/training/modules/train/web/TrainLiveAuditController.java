package com.training.modules.train.web;

import java.net.URLEncoder;
import java.util.Date;
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
import com.training.modules.ec.entity.Orders;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLivePlayback;
import com.training.modules.train.entity.TrainLiveRoom;
import com.training.modules.train.service.TrainLiveAuditService;
import com.training.modules.train.service.TrainLiveRoomService;
import com.training.modules.train.utils.EncryptLiveUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/train/live")
public class TrainLiveAuditController extends BaseController{
	
	@Autowired
	private TrainLiveAuditService trainLiveAuditService;
	@Autowired
	private TrainLiveRoomService trainLiveRoomService;
	
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
							map.put("authtype",URLEncoder.encode("2","utf-8")); //authtype
							map.put("publisherpass",URLEncoder.encode("123456","utf-8"));
							map.put("assistantpass",URLEncoder.encode("123456","utf-8"));
							//map.put("playpass",URLEncoder.encode(trainLive.getPlaypass(),"utf-8"));
							map.put("barrage",URLEncoder.encode("1","utf-8"));
							map.put("foreignpublish",URLEncoder.encode("0","utf-8"));
							map.put("openlowdelaymode",URLEncoder.encode("0","utf-8"));
							map.put("showusercount",URLEncoder.encode("1","utf-8"));
							String	qString=EncryptLiveUtil.createHashedQueryString(map,time,API_KEY);
							String result=EncryptLiveUtil.UpdateLiveGet(qString);
							System.out.println(result);
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
						map.put("authtype",URLEncoder.encode("2","utf-8")); //authtype
						map.put("publisherpass",URLEncoder.encode("123456","utf-8"));
						map.put("assistantpass",URLEncoder.encode("123456","utf-8"));
						//map.put("playpass",URLEncoder.encode(trainLive.getPlaypass(),"utf-8"));
						map.put("barrage",URLEncoder.encode("1","utf-8"));
						map.put("foreignpublish",URLEncoder.encode("0","utf-8"));
						map.put("openlowdelaymode",URLEncoder.encode("0","utf-8"));
						map.put("showusercount",URLEncoder.encode("1","utf-8"));
						String	qString=EncryptLiveUtil.createHashedQueryString(map,time,API_KEY);
						String result=EncryptLiveUtil.CreateLiveGet(qString);
						System.out.println(result);
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

			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "直播保存失败", e);
			logger.error("方法：save，直播：" + e.getMessage());
			addMessage(redirectAttributes, "直播保存失败");
		}

		return "redirect:" + adminPath + "/train/live/list";

	}
	
	
	/**
	 * 跳转到导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/train/importExcel";
	}
	
	/**
	 * 下载导入物流数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(Orders orders,HttpServletResponse response, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		try {
			// String fileName = "鐢ㄦ埛鏁版嵁瀵煎叆妯℃澘.xlsx";
			// List<User> list = Lists.newArrayList();
			// User user=UserUtils.getUser();
			// user.getCompany().setName("鍙涓瑰┓");
			// user.getOffice().setName("鍖椾含鐧讳簯缇庝笟");
			// user.setCode("10001000100010001");
			// user.setName("寮犳磱");
			// user.setIdCard("320324199202284521");
			// user.setInductionTime(new Date());
			// user.setLoginName("13146596978");
			// user.setMobile("13146596978");
			// user.setRemarks("yangyang");
			// list.add(user);
			// new ExportExcel("\n 鐢ㄦ埛鏁版嵁 "
			// + "\n (1.褰掑睘鍏徃蹇呭～锛屽綊灞炲叕鍙稿悕绉颁竴瀹氳鍜岄儴闂ㄤ笂绾у悕绉颁竴鑷达紝濡傦細鍙涓瑰┓)"
			// + "\n
			// (2.褰掑睘閮ㄩ棬锛屽彲鍐欏彲绌哄彧鍋氬弬鑰冿紝浠ラ儴闂ㄧ紪鍙蜂负鍑嗭紝閮ㄩ棬缂栧彿鍦ㄦ満鏋勯噷闈㈡壘鍒拌繖涓敤鎴锋墍鍦ㄩ儴闂ㄧ殑缂栫爜锛屽锛氬寳浜櫥浜戠編涓氾紝缂栫爜锛�10001000100010001)"
			// + "\n (3.鐧诲綍鍚嶅繀濉紝鐧诲綍鍚嶉粯璁ゅ～鍐欎负鐢ㄦ埛娉ㄥ唽鐨勬墜鏈哄彿鐮�)"
			// + "\n
			// (4.濮撳悕锛屾墜鏈哄彿锛岀敤鎴风被鍨嬶紝鎷ユ湁瑙掕壊蹇呭～锛岀敤鎴风被鍨嬪拰鎷ユ湁瑙掕壊蹇呴』鍜岃鑹茬鐞嗛噷闈㈠悕绉颁竴鑷达紝鍚﹀垯瑙掕壊鏃犳硶淇濆瓨锛屽锛氱編瀹瑰笀锛屾垨鑰咃細绠＄悊鍛�)"
			// + "\n (5.宸ュ彿锛岄偖绠憋紝鐢佃瘽锛屽垱寤烘椂闂达紝澶囨敞鍙～鍐欙紝鍙负绌恒��)", User.class,
			// 2).setDataList(list).write(response, fileName).dispose();
//			String filename = "物流数据.xlsx";
//			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
//			logger.info("#####[物流数据模板-old-path"+oldPath);
//			TrainRuleParam trainRuleParam  =new TrainRuleParam();
//			trainRuleParam.setParamKey("excel_path"); 
//			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
//			logger.info("#####[物流数据模板-new-path"+path);
//			File file = new File(path);
//			// 以流的形式下载文件。
//			InputStream fis = new BufferedInputStream(new FileInputStream(path));
//			byte[] buffer = new byte[fis.available()];
//			fis.read(buffer);
//			fis.close();
//			// 清空response
//			response.reset();
//			// 设置response的Header
//			response.addHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
//			response.addHeader("Content-Length", "" + file.length());
//			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//			response.setContentType("application/vnd.ms-excel");
//			toClient.write(buffer);
//			toClient.flush();
//			toClient.close();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/orders/list";
	}
	

}

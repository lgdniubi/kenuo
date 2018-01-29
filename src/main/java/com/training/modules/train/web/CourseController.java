package com.training.modules.train.web;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.StatisticsCollectionExport;
import com.training.modules.train.entity.StatisticsCommentExport;
import com.training.modules.train.entity.StatisticsTotalExport;
import com.training.modules.train.entity.StatisticsUnitExport;
import com.training.modules.train.entity.StatisticsUnitTotalExport;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainLessonContents;
import com.training.modules.train.entity.TrainLessons;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.service.TrainLessonContentsService;
import com.training.modules.train.service.TrainLessonsService;
import com.training.modules.train.utils.ScopeUtils;

/**
 * 添加课程
 * @author kele
 * 2016年3月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/train/course")
public class CourseController extends BaseController{
	
	@Autowired
	private TrainLessonsService trainLessonsService;
	
	@Autowired
	private TrainCategorysService trainCategorysService;
	
	@Autowired
	private TrainLessonContentsService trainlessonContentsService;
	
	
	@RequestMapping(value = {"/"})
	public void course(){
		
	}
	
	/**
	 * 生成课程ID
	 * @return
	 */
	@RequestMapping(value = {"guidcourse", ""})
	public @ResponseBody String guidcourse(){
		return IdGen.uuid();
	}
	
	/**
	 * 课程信息管理-分页展示list数据
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:course:listcourse"},logical=Logical.OR)
	@RequestMapping(value = {"listcourse", ""})
	public String listcourse(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		User user = UserUtils.getUser();
		TrainCategorys trainCategorys = new TrainCategorys();
		trainCategorys.setPriority(1);
		model.addAttribute("trainLessons", trainLessons);
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		
		//trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		//查询1级分类
	//	List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
	//	model.addAttribute("listone", listone);
		
		
		
		// 设置默认时间范围，默认当前月
//		if (trainLessons.getBeginDate() == null){
//			trainLessons.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
//		}
//		if (trainLessons.getEndDate() == null){
//			trainLessons.setEndDate(DateUtils.addMonths(trainLessons.getBeginDate(), 1));
//		}
		//我的课程
//		trainLessons.setCreateuser(UserUtils.getUser().getId());//用户ID
//		trainLessons = CourseUtils.lessonsFilter(trainLessons);
		trainLessons.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t",""));
		Page<TrainLessons> page = trainLessonsService.find(new Page<TrainLessons>(request, response), trainLessons);
		model.addAttribute("page", page);
		model.addAttribute("user", user);
		return "modules/train/coursechange";
	}
	
	/**
	 * 课程-修改
	 * @return
	 */
	@RequiresPermissions(value={"train:course:getcoursebyid"},logical=Logical.OR)
	@RequestMapping(value = {"getcoursebyid", ""})
	public String getcoursebyid(TrainLessons trainLessons, Model model){
		TrainCategorys trainCategorys = new TrainCategorys();
		trainCategorys.setPriority(1);
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		model.addAttribute("listone", listone);
		
		if (StringUtils.isNotBlank(trainLessons.getLessonId())){
			 trainLessons = trainLessonsService.get(trainLessons);
		}
		model.addAttribute("trainLessons", trainLessons);
		return "modules/train/courseinfoform";
	}
	
	/**
	 * 课程-保存
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions(value={"train:course:savecourse"},logical=Logical.OR)
	@RequestMapping(value = {"savecourse", ""})
	public String savecourse(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException{
		
		try {
			trainLessons.setName(HtmlUtils.htmlUnescape(trainLessons.getName()));
			if(null == trainLessons.getLessonId() || "".equals(trainLessons.getLessonId())){
				trainLessons.setLessonId(IdGen.uuid());
			}
			String categoryId = request.getParameter("categoryId");
			TrainCategorys trainCategorys = new TrainCategorys();
			trainCategorys.setCategoryId(categoryId); 
			trainLessons.setTrainCategorys(trainCategorys);//课程分类ID
			trainLessons.setStatus(0);//状态(-1无效  0有效)
			trainLessons.setCreateuser(UserUtils.getUser().getId());//用户ID
			trainLessons.setOfficeCode(UserUtils.getUser().getOffice().getCode());//用户机构Code
			
			trainLessonsService.save(trainLessons);
			addMessage(redirectAttributes, "保存课程'" + trainLessons.getName() + "'成功");
			return "redirect:" + adminPath + "/train/course/listcourse?beginDate=''&endDate=''";
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存课程出现异常,请与管理员联系");
			logger.error("保存课程出现异常,异常信息为："+e.getMessage());
			return "redirect:" + adminPath + "/train/course/addcourse";
		}
	}
	
	/**
	 * 课程-编辑修改
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions(value={"train:course:updatecourse"},logical=Logical.OR)
	@RequestMapping(value = {"updatecourse", ""})
	public String updatecourse(TrainLessons trainLessons,RedirectAttributes redirectAttributes) throws IllegalStateException, IOException{
		trainLessons.setName(HtmlUtils.htmlUnescape(trainLessons.getName()));
		trainLessonsService.updatecourse(trainLessons);
		addMessage(redirectAttributes, "修改课程'" + trainLessons.getName() + "'修改成功");
		return "redirect:" + adminPath + "/train/course/listcourse?beginDate=''&endDate=''";
	}
	
	/**
	 * 删除方法 
	 * 只是修改status状态
	 * @param trainLessons
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:course:deletecourse"},logical=Logical.OR)
	@RequestMapping(value = {"deletecourse", ""})
	public String deletecourse(TrainLessons trainLessons, RedirectAttributes redirectAttributes){
		if (StringUtils.isNotBlank(trainLessons.getLessonId())){
			trainLessonsService.deleteCourseForUpdate(trainLessons);
			addMessage(redirectAttributes, "删除课程成功");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	/**
	 * 批量删除方法 
	 * 循环修改status状态
	 * @param ids
	 * @param sorts
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:course:batchdeletecourse"},logical=Logical.OR)
	@RequestMapping(value = {"batchdeletecourse", ""})
	public String batchdeletecourse(String[] ids, RedirectAttributes redirectAttributes) {
		if(null != ids && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
	    		TrainLessons trainLessons = new TrainLessons(ids[i]);
	    		trainLessonsService.deleteCourseForUpdate(trainLessons);
	    	}
	    	addMessage(redirectAttributes, "批量删除课程成功");
		}
    	return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	/**
	 * 添加课程
	 * @return
	 */
	@RequiresPermissions(value={"train:course:addcourse"},logical=Logical.OR)
	@RequestMapping(value = {"addcourse", ""})
	public String addcourse(TrainLessons trainLessons,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		TrainCategorys trainCategorys = new TrainCategorys();
		trainCategorys.setPriority(1);
		
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		trainLessons.setLessonId(IdGen.uuid());
		
		model.addAttribute("listone", listone);
		model.addAttribute("trainLessons", trainLessons);
		return "modules/train/addcourse";
	}
	
	/**
	 * 课程内容(video/word/pdf) Ajax上传
	 * @param btnFile btnFile对应页面的name属性 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions(value={"train:course:fileuploadcontents"},logical=Logical.OR)
	@RequestMapping(value = {"fileuploadcontents", ""})
	public @ResponseBody Map<String, String> fileuploadcontents(HttpServletRequest request, HttpServletResponse response){	
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		try {
			String contentId = request.getParameter("contentId");//内容ID
			String contentTypeName = request.getParameter("contentTypeName");//内容类型
			String contentPath = request.getParameter("contentPath");//内容路径
			TrainLessonContents trainlessonContents = new TrainLessonContents();
			
			//根据内容ID，保存视频的缩略图
			if(null != contentId && !contentId.isEmpty()
					&& null != contentTypeName && "images".equals(contentTypeName)){
				trainlessonContents.setContentId(contentId);
				trainlessonContents.setCoverPic(contentPath);
				trainlessonContentsService.updateCoverPic(trainlessonContents);
				
				jsonMap.put("CONTENTSID", trainlessonContents.getContentId());
				jsonMap.put("STATUS", "OK");
				jsonMap.put("COVERPIC", trainlessonContents.getCoverPic());
				jsonMap.put("MESSAGE", "保存课程内容成功");
				
			}else{
				//保存课程内容（video、word、pdf）
				String lessonId = request.getParameter("lessonId");//课程ID
				String contentlength = request.getParameter("contentlength");//视频时长
				String filename = request.getParameter("filename");//文件名称
				trainlessonContents.setContentId(IdGen.uuid());//生成课程内容ID
				TrainLessons lessons = new TrainLessons();
				lessons.setLessonId(lessonId);
				trainlessonContents.setTrainLessons(lessons);
				trainlessonContents.setContent(contentPath);
				trainlessonContents.setDownUrl(contentPath);
				trainlessonContents.setName(filename);
				
				//课程内容类型
				if(null == contentlength || "".equals(contentlength)){
					trainlessonContents.setContentlength("");// 文件长度
				}else{
					trainlessonContents.setContentlength(contentlength);// 文件长度
				}
				
				//文件上传(video/word/pdf)类型
				if(null != contentTypeName && "video".equals(contentTypeName)){
					trainlessonContents.setContentType(1);
					jsonMap.put("CONTENTLENGTH", trainlessonContents.getContentlength());
				}else if(null != contentTypeName && "word".equals(contentTypeName)){
					trainlessonContents.setContentType(2);
					trainlessonContents.setCoverPic(Global.getConfig("web.word.png.url"));
				}else if(null != contentTypeName){
					trainlessonContents.setContentType(3);
					trainlessonContents.setCoverPic(Global.getConfig("web.pdf.png.url"));
				}
				
				//保存到课程内容表
				trainlessonContentsService.save(trainlessonContents);
				jsonMap.put("CONTENTSID", trainlessonContents.getContentId());
				jsonMap.put("STATUS", "OK");
				jsonMap.put("COVERPIC", trainlessonContents.getCoverPic());
				jsonMap.put("FILENAME", trainlessonContents.getName());
				jsonMap.put("MESSAGE", "保存课程内容成功");
			}
		} catch (Exception e) {
			logger.info("#####保存课程内容出现异常,异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "保存课程内容出现异常");
		}
		
		return jsonMap;
	}
	
	/**
	 * 删除上传的课程内容 Ajax删除
	 * @param contentTypeName
	 * @param contentId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"train:course:deleteuploadcontents"},logical=Logical.OR)
	@RequestMapping(value = {"deleteuploadcontents", ""})
	public @ResponseBody Map<String, String> deleteuploadcontents(HttpServletRequest request, HttpServletResponse response){  
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String contentId = request.getParameter("CONTENTSID");
			if(null != contentId && !"".equals(contentId.trim())){
				//根据contentId查询课程内容
				TrainLessonContents trainLessonContents = new TrainLessonContents();
				trainLessonContents.setContentId(contentId);
				trainLessonContents = trainlessonContentsService.getTrainlessonContents(trainLessonContents);
				//获取课程内容名称
				String filename = trainLessonContents.getName();
				//根据contentId获取对象
				trainlessonContentsService.deleteTrainlessonContents(trainLessonContents);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("FILENAME", filename);
				jsonMap.put("MESSAGE", "删除文件成功");
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "参数为空，请与管理员联系！");
			}
		} catch (Exception e) {
			jsonMap.put("FILENAME", "");
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "出现异常，请与管理员联系！");
		}
		
		return jsonMap;
	}
	
	/**
	 * 课程内容管理
	 * @return
	 */
	@RequiresPermissions(value={"train:course:contentmanage"},logical=Logical.OR)
	@RequestMapping(value = {"contentmanage", ""})
	public String contentmanage(HttpServletRequest request, HttpServletResponse response, Model model){
		//根据课程ID查询所有的课程内容
		String lessonId = request.getParameter("lessonId");
		TrainLessonContents trainLessonContents = new TrainLessonContents();
		TrainLessons trainLessons = new TrainLessons();
		trainLessons.setLessonId(lessonId);
		trainLessonContents.setTrainLessons(trainLessons);
		
		List<TrainLessonContents> listall = trainlessonContentsService.findlistcontents(trainLessonContents);
		//课程类型　1  视频，2  文档  3 其他
		List<TrainLessonContents> listvideo = new ArrayList<TrainLessonContents>();	//1
		List<TrainLessonContents> listword = new ArrayList<TrainLessonContents>();	//2
		List<TrainLessonContents> listpdf = new ArrayList<TrainLessonContents>();	//3
		
		//循环遍历，保存到各自对应的list中
		for (int i = 0; i < listall.size(); i++) {
			TrainLessonContents tlc = listall.get(i);
			int contenttype = tlc.getContentType();
			if(1 == contenttype){
				listvideo.add(tlc);
			}else if(2 == contenttype){
				listword.add(tlc);
			}else{
				listpdf.add(tlc);
			}
		}
		//保存
		model.addAttribute("trainLessons", trainLessonsService.get(trainLessons));
		model.addAttribute("listvideo", listvideo);
		model.addAttribute("listword", listword);
		model.addAttribute("listpdf", listpdf);
		
		return "modules/train/contentmanage";
	}
	
	/**
	 * 修改图片
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateContentPic")
	public String updateContentPic(TrainLessonContents trainLessonContents,HttpServletRequest request,HttpServletResponse response) {
		//String contenId=request.getParameter("contentId");
		//String coverPic=request.getParameter("coverPic");
		trainlessonContentsService.updateContentPic(trainLessonContents);
		
		return "success";
	}
	
	/**
	 * 课程-修改
	 * @returns
	 */
	@RequiresPermissions(value={"train:course:getcourseinfoview"},logical=Logical.OR)
	@RequestMapping(value = {"getcourseinfoview", ""})
	public String getcourseinfoview(TrainLessons trainLessons, Model model){
		if (StringUtils.isNotBlank(trainLessons.getLessonId())){
			 trainLessons = trainLessonsService.get(trainLessons);
		}
		model.addAttribute("trainLessons", trainLessons);
		return "modules/train/courseinfoview";
	}
	/**
	 * 修改课程状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateIsShow")
	public Map<String, String> updateIsShow(String lessonId,String isShow){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			int ISSHOW = Integer.parseInt(isShow);
			if(!StringUtils.isEmpty(lessonId) && (ISSHOW == 0 || ISSHOW == 1)){
				TrainLessons trainLessons = new TrainLessons();
				trainLessons.setLessonId(lessonId);
				trainLessons.setIsShow(ISSHOW);
				trainLessonsService.updateIsShow(trainLessons);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("课程管理-修改课程状态 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 导出课程统计总览
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "totalexport", method = RequestMethod.POST)
	public String totalExport(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "课程统计总览" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<StatisticsTotalExport> list=trainLessonsService.totalExport(trainLessons);
			new ExportExcel("课程统计总览", StatisticsTotalExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "课程统计总览", e);
			addMessage(redirectAttributes, "课程统计总览，导出失败！");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	
	/**
	 * 导出收藏统计
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "collectionexport", method = RequestMethod.POST)
	public String collectionExport(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "收藏统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<StatisticsCollectionExport> list=trainLessonsService.collectionExport(trainLessons);
			new ExportExcel("课程收藏统计", StatisticsCollectionExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "课程收藏统计", e);
			addMessage(redirectAttributes, "课程收藏统计，导出失败！");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	/**
	 * 导出评论统计
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "commentexport", method = RequestMethod.POST)
	public String commentExport(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "评论统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<StatisticsCommentExport> list=trainLessonsService.commentExport(trainLessons);
			new ExportExcel("课程评论统计", StatisticsCommentExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "课程评论统计", e);
			addMessage(redirectAttributes, "课程评论统计，导出失败！");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	/**
	 * 单元测试
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "unitexport", method = RequestMethod.POST)
	public String unitExport(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "单元测试" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<StatisticsUnitExport> list=trainLessonsService.unitExport(trainLessons);
			new ExportExcel("单元测试", StatisticsUnitExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "单元测试", e);
			addMessage(redirectAttributes, "单元测试，导出失败！");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
	/**
	 * 单元测试统计
	 * @param trainLessons
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "totalunitexport", method = RequestMethod.POST)
	public String totalUnitExport(TrainLessons trainLessons, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "单元测试统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<StatisticsUnitTotalExport> list=trainLessonsService.unitTotalExport(trainLessons);
			new ExportExcel("单元测试统计", StatisticsUnitTotalExport.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "单元测试统计", e);
			addMessage(redirectAttributes, "单元测试统计，导出失败！");
		}
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	
}

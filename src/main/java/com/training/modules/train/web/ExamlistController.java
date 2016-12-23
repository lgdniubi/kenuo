package com.training.modules.train.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.beanvalidator.BeanValidators;
import com.training.common.persistence.Page;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ImportExcel;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.Exercises;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.service.ExercisesService;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.utils.ScopeUtils;

/**
 * 
 *   试题
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/examlist")
public class ExamlistController extends BaseController{
	
	@Autowired
	private ExercisesService exercisesService;
	@Autowired
	private TrainCategorysService trainCategorysService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	
	/**
	 * 打开试题及问答管理--->试题列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "examlist", "" })
	public String examlist(String exerciseType,String s1,TrainCategorys trainCategorys,ExercisesCategorys exercisesCategorys,HttpServletRequest request, HttpServletResponse response, Model model) {	
		if(exercisesCategorys.getName()!=null || s1 != null){
			model.addAttribute("categoryid1",exercisesCategorys.getName());
			model.addAttribute("parentId",s1);
		}
//		exercisesCategorys=ExamUtils.examFilter(exercisesCategorys);
		exercisesCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("a",""));
		Page<ExercisesCategorys> page=exercisesService.findPage(new Page<ExercisesCategorys>(request, response), exercisesCategorys);
		if(exercisesCategorys.getExerciseTitle()!=null || exercisesCategorys.getExerciseTitle()!=""){
			model.addAttribute("exerciseTitle", exercisesCategorys.getExerciseTitle());
		}
		//添加数据权限
		TrainCategorys t=new TrainCategorys();
		t.setPriority(1);
//		t = CategorysUtils.categorysFilter(t);
		t.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(t);
		model.addAttribute("exerciseType", exerciseType);
		model.addAttribute("listone", listone);
		model.addAttribute("page", page);
		return "modules/train/examlist";
	}
	
	/**
	 * 删除单个试题方法 
	 * 只需修改status状态    
	 * @param exercises
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:examlist:deleteOneExam"},logical=Logical.OR)
	@RequestMapping(value = {"deleteOneExam", ""})
	public String deleteOneExam(Exercises exercises,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		if (StringUtils.isNotBlank(exercises.getExerciseId())){
			exercisesService.deleteOneExam(exercises);
			exercisesService.deleteOneLessonExam(exercises);
			addMessage(redirectAttributes, "删除试题成功");
		}
		//   重定向
		return "redirect:" + adminPath + "/train/examlist/examlist";
	}
	
	/** 
	 * 通过ID查询单个exercises 
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value={"train:course:examOne"},logical=Logical.OR)
	@RequestMapping(value ="examOne")
	public String examOne(TrainCategorys trainCategorys,ExercisesCategorys exercisesCategorys,HttpServletRequest request, HttpServletResponse response,Model model){
		trainCategorys.setPriority(1);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		model.addAttribute("listone", listone);
		if (StringUtils.isNotBlank(exercisesCategorys.getExerciseId())){
			 exercisesCategorys=exercisesService.get(exercisesCategorys.getExerciseId());
		}
			String str=exercisesCategorys.getExerciseContent();
			//使用  *#  分割String类型
			String[] strs=str.split("[*#]"); 
			for (int i = 0; i < strs.length; i++) {
				if(strs[i].length()==0){ continue;}
				model.addAttribute("exerciseContent"+i,strs[i]);
			}
		String parentId=exercisesService.getParentId(exercisesCategorys.getExerciseId());
		model.addAttribute("parentId", parentId);
		model.addAttribute("exercisesCategorys", exercisesCategorys);
		model.addAttribute("exerciseId",exercisesCategorys.getExerciseId());
		model.addAttribute("categoryid",exercisesCategorys.getCategoryId());
		model.addAttribute("exercisetype",exercisesCategorys.getExerciseType());
		return "modules/train/OneExam";
	}
	/**
	 * 删除多个试题
	 * @param ids
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:course:deleteAll"},logical=Logical.OR)
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Exercises exercises=new Exercises();
			exercises.setExerciseId(id);
			exercisesService.deleteOneExam(exercises);
			exercisesService.deleteOneLessonExam(exercises);
		}
		addMessage(redirectAttributes, "删除试题成功");
	//   重定向
		return "redirect:" + adminPath + "/train/examlist/examlist";
	}
	
	
	/**
	 * 添加试题
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:examlist:addexam"},logical=Logical.OR)
	@RequestMapping(value = { "addexam", "" })
	public String addexam(TrainCategorys trainCategorys,HttpServletRequest request, HttpServletResponse response, Model model){
		//添加数据权限
		trainCategorys.setPriority(1);
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		model.addAttribute("listone", listone);
		return "modules/train/addexam";
	}
	/**
	 * 保存试题
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions(value={"train:addExam:add"},logical=Logical.OR)
	@RequestMapping(value ="addExam")
	public void addExam(Exercises exercises,String exerciseType,String exerciseResult3,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		//获取当前登录用户
		User user = UserUtils.getUser();
		String userId = user.getId();
		logger.debug("#####[userId]:"+userId);
		//添加是否题
		if("3".equals(exerciseType)){
			String id=IdGen.uuid();
			exercises.setExerciseId(id);
			exercises.setExerciseType(3);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle1"));
			exercises.setExerciseContent("正确*#错误");
			exercises.setExerciseResult(request.getParameter("exerciseResult1") );
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags1"));
			exercises.setCategoryId(request.getParameter("name") );
			exercises.setCreateuser(userId);
			exercisesService.addExam(exercises);
			model.addAttribute("success", 1);
		}
		//添加单选题
		if("1".equals(exerciseType)){
			String id=IdGen.uuid();
			exercises.setExerciseId(id);
			exercises.setExerciseType(1);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle2"));
			String[] str = request.getParameterValues("exerciseContent2");
			String app = "";
			for (int i = 0; i < str.length; i++) {
				app = app + str[i]+"*#";
			}
			exercises.setExerciseContent(app);
			exercises.setExerciseResult(request.getParameter("exerciseResult2"));
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags2"));
			exercises.setCategoryId(request.getParameter("name"));
			exercises.setCreateuser(userId);
			exercisesService.addExam(exercises);
			model.addAttribute("success", 1);
		}
		//添加多选题
		if("2".equals(exerciseType)){
			String id=IdGen.uuid();
			exercises.setExerciseId(id);
			exercises.setExerciseType(2);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle3"));
			
			String[] str = request.getParameterValues("exerciseContent3");
			String app = "";
			for (int i = 0; i < str.length; i++) {
				app = app + str[i]+"*#";
			}
			exercises.setExerciseContent(app);
			exercises.setExerciseResult(exerciseResult3);
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags3"));
			exercises.setCategoryId(request.getParameter("name"));
			exercises.setCreateuser(userId);
			exercisesService.addExam(exercises);
			model.addAttribute("success", 1);
		}
	}
	/**
	 * 修改试题  分为是否题 单选题  多选题
	 * @param exercises
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:addexam:save"},logical=Logical.OR)
	@RequestMapping(value ="save")
	public String save(Exercises exercises,String exerciseResult3,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		//获取当前登录用户
		User user = UserUtils.getUser();
		String userId = user.getId();
		logger.debug("#####[userId]:"+userId);
		//修改是否题
		if("3".equals(request.getParameter("exercisetype"))){
			exercises.setExerciseId(request.getParameter("exerciseId"));
			exercises.setExerciseType(3);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle1"));
			exercises.setExerciseContent("正确*#错误");
			exercises.setExerciseResult(request.getParameter("exerciseResult1") );
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags1"));
			exercises.setCategoryId(request.getParameter("name") );
			exercises.setCreateuser(userId);
			exercisesService.updateExam(exercises);
		}
		//修改单选题
		if("1".equals(request.getParameter("exercisetype"))){
			exercises.setExerciseId(request.getParameter("exerciseId"));
			exercises.setExerciseType(1);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle2"));
			String[] str = request.getParameterValues("exerciseContent2");
			String app = "";
			for (int i = 0; i < str.length; i++) {
				app = app + str[i]+"*#";
			}
			exercises.setExerciseContent(app);
			exercises.setExerciseResult(request.getParameter("exerciseResult2"));
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags2"));
			exercises.setCategoryId(request.getParameter("name"));
			exercises.setCreateuser(userId);
			exercisesService.updateExam(exercises);
		}
		//修改多选题
		if("2".equals(request.getParameter("exercisetype"))){
			exercises.setExerciseId(request.getParameter("exerciseId"));
			exercises.setExerciseType(2);
			exercises.setExerciseTitle(request.getParameter("exerciseTitle3"));
			
			String[] str = request.getParameterValues("exerciseContent3");
			String app = "";
			for (int i = 0; i < str.length; i++) {
				app = app + str[i]+"*#";
			}
			exercises.setExerciseContent(app);
			exercises.setExerciseResult(exerciseResult3);
			exercises.setStatus(0);
			exercises.setTags(request.getParameter("tags3"));
			exercises.setCategoryId(request.getParameter("name"));
			exercises.setCreateuser(userId);
			exercisesService.updateExam(exercises);
		}
		addMessage(redirectAttributes, "修改试题'" + exercises.getExerciseTitle() + "'成功");
		return "redirect:" + adminPath + "/train/examlist/examlist";
	}
	public String verifyExercises(Exercises exercises){
		if(null !=exercisesService.findByName(exercises.getCategoryName())){
			if("单选题".equals(exercises.getType()) || "多选题".equals(exercises.getType())){
				if(null == exercises.getA() || "".equals(exercises.getA())){
					return "false";
				}else if(null == exercises.getB() || "".equals(exercises.getB())){
					return "false";
				}else if(null == exercises.getC() || "".equals(exercises.getC())){
					return "false";
				}else if(null == exercises.getD() || "".equals(exercises.getD())){
					return "false";
				}else{
					return "true";
				}
			}else if("判断题".equals(exercises.getType())){
				return "true";
			}else{
				return "false";
			}
		}else{
			return "cateNameFalse";
		}
	}
	//答案验证
	public String verifyResult(Exercises exercises){
		if("单选题".equals(exercises.getType())){
			if(exercises.getResult().length() != 1){
				return "false";
			}else if(!"ABCD".contains(exercises.getResult())){
				return "false";
			}else{
				return "true";
			}
		}else if("多选题".equals(exercises.getType())){
			if(exercises.getResult().length() <= 2){
				return "false";
			}else{
				//   用于验证多选题
				if("true".equals(verifyChoice(exercises))){
					return "true";
				}else{
					return "false";
				}
			}
		}else if("判断题".equals(exercises.getType())){
			if(exercises.getResult().length() != 1){
				return "false";
			}else if(!"AB".contains(exercises.getResult())){
				return "false";
			}else{
				return "true";
			}
		}else{
			return "false";
		}
	}
	//多选题答案验证
	public String verifyChoice(Exercises exercises){
		String s1=exercises.getResult().replace("，", ",");
		if(s1.length() == 3){
			String regex = "[A-D][,][A-D]"; //答案验证（A,B,C,D）
			if(true == s1.matches(regex)){
				return "true";
			}else{
				return "false";
			}
		}else if(s1.length() == 5){
			String regex = "[A-D][,][A-D][,][A-D]"; //答案验证（A,B,C,D）
			if(true == s1.matches(regex)){
				return "true";
			}else{
				return "false";
			}
		}else if(s1.length() == 7){
			String regex = "[A-D][,][A-D][,][A-D][,][A-D]"; //答案验证（A,B,C,D）
			if(true == s1.matches(regex)){
				return "true";
			}else{
				return "false";
			}
		}else{
			return "false";
		}
	}
//	1 单选题 2 多选题 3 是非题  
	public Exercises add(Exercises exercises){
		//获取当前登录用户
		User user = UserUtils.getUser();
		exercises.setCreateuser(user.getId());
		String id=IdGen.uuid();
		exercises.setExerciseId(id);
		exercises.setStatus(0);
		exercises.setCategoryId(exercisesService.findByName(exercises.getCategoryName()).getCategoryId());
		if("单选题".equals(exercises.getType())){
			exercises.setExerciseType(1);
			exercises.setExerciseContent(exercises.getA()+"*#"+exercises.getB()+"*#"+exercises.getC()+"*#"+exercises.getD()+"*#");
			if("B".equals(exercises.getResult())){
				exercises.setExerciseResult("2");
			}else if("C".equals(exercises.getResult())){
				exercises.setExerciseResult("3");
			}else if("D".equals(exercises.getResult())){
				exercises.setExerciseResult("4");
			}else{
				exercises.setExerciseResult("1");
			}
		}else if("多选题".equals(exercises.getType())){
			exercises.setExerciseType(2);
			exercises.setExerciseContent(exercises.getA()+"*#"+exercises.getB()+"*#"+exercises.getC()+"*#"+exercises.getD()+"*#");
			String s1=exercises.getResult().replace("，", ",");
			String s2=s1.replace("A","1");
			String s3=s2.replace("B","2");
			String s4=s3.replace("C","3");
			String s5=s4.replace("D","4");
			exercises.setExerciseResult(s5);
		}else if("判断题".equals(exercises.getType())){
			exercises.setExerciseType(3);
			exercises.setExerciseContent("正确*#错误");
			if("B".equals(exercises.getResult())){
				exercises.setExerciseResult("2");
			}else{
				exercises.setExerciseResult("1");
			}
		}
		return exercises;
	}
	/**
	 * 跳转到导入页面
	 * @return
	 */
    @RequestMapping(value = "importPage")
	public String importPage() {
		return "modules/train/importExcel";
	}
	/**
	 * 导入试题数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "import")
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			if (ei.getLastCellNum() != 9) {
				failureMsg.append("<br/>导入的模板错误，请检查模板; ");
			} else {
				List<Exercises> list = ei.getDataList(Exercises.class);
				for (Exercises exercises : list){
					try{
						String str = verifyExercises(exercises);
						if ("true".equals(str)){
							if("true".equals(verifyResult(exercises))){
								BeanValidators.validateWithException(validator, exercises);
								Exercises e = add(exercises);
								exercisesService.addExam(e);
								successNum++;
							}else{
								failureMsg.append("<br/>试题 "+exercises.getExerciseTitle()+" 导入失败：答案输入有误,请仔细核对注意事项; ");
								failureNum++;
							}
						}else if("cateNameFalse".equals(str)){
							failureMsg.append("<br/>试题 "+exercises.getExerciseTitle()+" 导入失败：所属分类错误,请仔细核对注意事项; ");
							failureNum++;
						}else{
							failureMsg.append("<br/>试题 "+exercises.getExerciseTitle()+" 导入失败：填写试题A、B、C、D内容有误,请仔细核对注意事项; ");
							failureNum++;
						}
					}catch(ConstraintViolationException ex){
						failureMsg.append("<br/>试题 "+exercises.getExerciseTitle()+" 导入失败：");
						List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
						for (String message : messageList){
							failureMsg.append(message+";");
							failureNum++;
						}
					}catch (Exception ex) {
						failureMsg.append("<br/>试题 "+exercises.getExerciseTitle()+" 导入失败："+ex.getMessage());
					}
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条试题，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条试题"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入试题失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/examlist/examlist";
    }
    /**
     * 下载模板
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String filename = "试题数据导入模板.xlsx";
			String oldPath = request.getServletContext().getRealPath("/") + "static/Exceltemplate/" + filename;
			logger.info("#####[试题数据导入模板-old-path"+oldPath);
			TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
			String path = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/Exceltemplate/" + filename;
			logger.info("#####[试题数据导入模板-new-path"+path);
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
		return null;
    }
    public static void main(String[] args) {
    	String regex = "[A-D][,][A-D][,][A-D]"; //答案验证（A,B,C,D）
    	String s1 = "A,B";
    	System.out.println(s1.matches(regex));
	}
}

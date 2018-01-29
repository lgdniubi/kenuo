package com.training.modules.train.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ExamLessionMapping;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.service.ExamBankService;
import com.training.modules.train.service.ExercisesService;
import com.training.modules.train.service.ExercisesTuBeService;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.utils.ScopeUtils;

/**
 * 试题库
 * @author Superman
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/exambank")
public class ExambankController extends BaseController{
	@Autowired
	private ExercisesService exercisesService;
	@Autowired
	private TrainCategorysService trainCategorysService;
	@Autowired
	private ExamBankService examBankService;
	@Autowired
	private ExercisesTuBeService exTuBeService;
	
	/**
	 * 
	 * @Title: exambankFrom
	 * @Description: TODO 根据课程id查询此课程下的习题或单元测试
	 * @param exerciseType
	 * @param s1
	 * @param trainCategorys
	 * @param exercisesCategorys
	 * @param examLession
	 * @param request
	 * @param response
	 * @param model
	 * @return:
	 * @return: String
	 * @throws
	 * 2018年1月24日 兵子
	 */
	@RequiresPermissions(value={"train:exambank:index"},logical=Logical.OR)
	@RequestMapping(value = { "exambankFrom", "" })
	public String exambankFrom(ExercisesCategorys exercisesCategorys,HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("lessonsId", exercisesCategorys.getLessonId());
		model.addAttribute("categoryId", exercisesCategorys.getCategoryId());
		model.addAttribute("lessontype",exercisesCategorys.getLessontype());
		Page<ExercisesCategorys> page=exTuBeService.findPage(new Page<ExercisesCategorys>(request, response), exercisesCategorys);
		model.addAttribute("page", page);	
		return "modules/train/exambankFrom";
	}
	
	/**
	 * 
	 * @Title: delete
	 * @Description: TODO 删除以及批量删除
	 * @throws
	 * 2018年1月25日 兵子
	 */
	@RequestMapping(value ="delete")
	public String delete(ExercisesCategorys exercisesCategorys,String exerciseIds, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request){
			try {
				int num = exTuBeService.deleteExercises(exerciseIds,exercisesCategorys);
				if (num > 0) {
					addMessage(redirectAttributes, "删除试题成功");
				}else{
					addMessage(redirectAttributes, "删除试题失败，没有此试题，请联系管理员");
				}
			} catch (Exception e) {
				addMessage(redirectAttributes, "删除试题失败！");
				logger.error("删除 出现异常，异常信息为："+e.getMessage());
				BugLogUtils.saveBugLog(request, "删除试题错误信息", e);
			}
		return "redirect:" + adminPath + "/train/exambank/exambankFrom?lessonId="+exercisesCategorys.getLessonId()+"&categoryId="+exercisesCategorys.getCategoryId()+"&lessontype="+exercisesCategorys.getLessontype();
	}
	
	/**
	 * 	查看试题库
	 */
	
	@RequestMapping(value = "exambank")
	public String exambank(String exerciseType,ExercisesCategorys exercisesCategorys,HttpServletRequest request, HttpServletResponse response, Model model) {
//		List<String> typeList = exercisesService.findTypeList();
//		model.addAttribute("typeList", typeList);
		
		
		model.addAttribute("exerciseType", exerciseType);
		exercisesCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("a",""));
		Page<ExercisesCategorys> page=exercisesService.findPage(new Page<ExercisesCategorys>(request, response), exercisesCategorys);
		if(exercisesCategorys.getExerciseTitle()!=null || exercisesCategorys.getExerciseTitle()!=""){
			model.addAttribute("exerciseTitle", exercisesCategorys.getExerciseTitle());
		}
		//添加数据权限
		TrainCategorys t=new TrainCategorys();
		t.setPriority(1);
		t.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(t);
		model.addAttribute("listone", listone);
		model.addAttribute("page", page);
		return "modules/train/exambank";
	}
	/**
	 * 批量添加试题
	 * @param lessonId
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = { "addAllExam", "" })
	public String addAllExam(ExamLessionMapping examLession,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		String idArray1[] =request.getParameter("exerciseId1").split(",");
		String idArray2[]=request.getParameter("exerciseId2").split(",");
		if(request.getParameter("lessontype").equals("1")){
			examLession.setLessonId(request.getParameter("lessonId"));
			examLession.setLessontype(request.getParameter("lessontype"));
			examBankService.deleteAll(examLession);
			if(request.getParameter("exerciseId2")!=""){
				for(String id:idArray2){
					if(id.length()!=0){ 
						ExamLessionMapping examLessionMapping = new ExamLessionMapping();
						examLessionMapping.setExerciseId(id);
						examLessionMapping.setLessonId(request.getParameter("lessonId"));
						examLessionMapping.setCategoryId("0");
						examLessionMapping.setStatus(0);
						examBankService.addExamLesson(examLessionMapping);	
					}
				}
			}
			if(request.getParameter("exerciseId1")!=""){
				for(String id:idArray1){
					if(id.length()!=0){ 
						ExamLessionMapping examLessionMapping = new ExamLessionMapping();
						examLessionMapping.setExerciseId(id);
						examLessionMapping.setLessonId(request.getParameter("lessonId"));
						examLessionMapping.setCategoryId("0");
						examLessionMapping.setStatus(0);
						examBankService.addExamLesson(examLessionMapping);		
					}
				}
			}
		}else if(request.getParameter("lessontype").equals("2")){
			examLession.setCategoryId(request.getParameter("ziCategoryId"));
			examLession.setLessontype(request.getParameter("lessontype"));
			examBankService.deleteAll(examLession);
			if(request.getParameter("exerciseId2")!=""){
				for(String id:idArray2){
					if(id.length()!=0){ 
						ExamLessionMapping examLessionMapping = new ExamLessionMapping();
						examLessionMapping.setExerciseId(id);
						examLessionMapping.setLessonId("0");
						examLessionMapping.setCategoryId(request.getParameter("ziCategoryId"));
						examLessionMapping.setStatus(0);
						examBankService.addExamLesson(examLessionMapping);	
					}
				}
			}
			if(request.getParameter("exerciseId1")!=""){
				for(String id:idArray1){
					if(id.length()!=0){ 
						ExamLessionMapping examLessionMapping = new ExamLessionMapping();
						examLessionMapping.setExerciseId(id);
						examLessionMapping.setLessonId("0");
						examLessionMapping.setCategoryId(request.getParameter("ziCategoryId"));
						examLessionMapping.setStatus(0);
						examBankService.addExamLesson(examLessionMapping);		
					}
				}
			}
			
		}
		addMessage(redirectAttributes, "批量添加试题成功");
		return "redirect:" + adminPath + "/train/course/listcourse";
	}
	/**
	 * 跳转到随机试题界面
	 * @return
	 */
	@RequestMapping(value ="random")
	public String random(String lessonId,String categoryId,String lessontype,Model model){
		
		//添加数据权限
		TrainCategorys t=new TrainCategorys();
		t.setPriority(1);
//		t = CategorysUtils.categorysFilter(t);
		t.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(t);
		model.addAttribute("listone", listone);
		
//		当前课程
		if(lessontype.equals("1")){
			model.addAttribute("parentIdval",examBankService.findByLessonid(lessonId).getParentId());
			model.addAttribute("lessontype",1);
			model.addAttribute("Tid",lessonId);
		}else{
//		当前单元
			model.addAttribute("parentIdval",examBankService.findByziCategoryId(categoryId).getParentId());
			System.out.println(examBankService.findByziCategoryId(categoryId).getParentId()+"=======");
			model.addAttribute("lessontype",2);
			model.addAttribute("Tid",categoryId);
		}
		return "modules/train/examBankForm";
	}
	/**
	 * 异步加载当前分类下的试题数量
	 * @param categoryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="jiaZaiNum")
	public Map<String,Integer> jiaZaiNum(String categoryId){
		Map<String, Integer> jsonMap = new HashMap<String, Integer>();
		ExercisesCategorys exercisesCategorys = new ExercisesCategorys();
		exercisesCategorys.setCategoryId(categoryId);
		exercisesCategorys.setLessontype("1");
		int e1=examBankService.jiaZaiNum(exercisesCategorys);
		exercisesCategorys.setLessontype("2");
		int e2=examBankService.jiaZaiNum(exercisesCategorys);
		exercisesCategorys.setLessontype("3");
		int e3=examBankService.jiaZaiNum(exercisesCategorys);
		jsonMap.put("e1",e1);
		jsonMap.put("e2",e2);
		jsonMap.put("e3",e3);
		return jsonMap;
	}
	/**
	 * 异步加载随机试题
	 * @param exercisesCategorys
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="randomList")
	public Map<String, List<ExercisesCategorys>> randomList(ExercisesCategorys exercisesCategorys,Model model){
		Map<String, List<ExercisesCategorys>> jsonMap = new HashMap<String, List<ExercisesCategorys>>();
		List<ExercisesCategorys> e1 = new ArrayList<ExercisesCategorys>();
		List<ExercisesCategorys> e2 = new ArrayList<ExercisesCategorys>();
		List<ExercisesCategorys> e3 = new ArrayList<ExercisesCategorys>();
		if(!"".equals(exercisesCategorys.getNum1())){
			exercisesCategorys.setLessontype("1");
			exercisesCategorys.setNewNum(Integer.valueOf(exercisesCategorys.getNum1()));
			e1 = examBankService.findByExerciseType(exercisesCategorys);
		}
		if(!"".equals(exercisesCategorys.getNum2())){
			exercisesCategorys.setLessontype("2");
			exercisesCategorys.setNewNum(Integer.valueOf(exercisesCategorys.getNum2()));
			e2 = examBankService.findByExerciseType(exercisesCategorys);
		}
		if(!"".equals(exercisesCategorys.getNum3())){
			exercisesCategorys.setLessontype("3");
			exercisesCategorys.setNewNum(Integer.valueOf(exercisesCategorys.getNum3()));
			e3 = examBankService.findByExerciseType(exercisesCategorys);
		}
		jsonMap.put("e1",e1);
		jsonMap.put("e2",e2);
		jsonMap.put("e3",e3);
		return jsonMap;
	}
	/**
	 * 添加随机试题
	 * @param examLession
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="addRandomExam")
	public String addRandomExam(ExamLessionMapping examLession,String Tid,Model model,RedirectAttributes redirectAttributes){
		String idArray[]=examLession.getExerciseId().split(",");
		if(examLession.getLessontype().equals("1")){
			examLession.setLessonId(Tid);
			examBankService.deleteAll(examLession);
			for(String id:idArray){
				if(id.length()!=0){ 
					ExamLessionMapping examLessionMapping = new ExamLessionMapping();
					examLessionMapping.setExerciseId(id);
					examLessionMapping.setLessonId(Tid);
					examLessionMapping.setCategoryId("0");
					examLessionMapping.setStatus(0);
					examBankService.addExamLesson(examLessionMapping);	
				}
			}
		}
		if(examLession.getLessontype().equals("2")){
			examLession.setCategoryId(Tid);
			examBankService.deleteAll(examLession);
			for(String id:idArray){
				if(id.length()!=0){ 
					ExamLessionMapping examLessionMapping = new ExamLessionMapping();
					examLessionMapping.setExerciseId(id);
					examLessionMapping.setLessonId("0");
					examLessionMapping.setCategoryId(Tid);
					examLessionMapping.setStatus(0);
					examBankService.addExamLesson(examLessionMapping);		
				}
			}
		}
		addMessage(redirectAttributes, "批量添加随机试题成功");
		return "redirect:" + adminPath + "/train/exambank/exambankFrom?lessonId="+Tid+"&categoryId="+Tid+"&lessontype="+examLession.getLessontype();
	}
}

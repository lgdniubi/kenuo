package com.training.modules.quartz.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.quartz.entity.Task;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.service.TaskService;


/**
 * 定时任务
 * @author kele
 * @version 2016-8-19
 */
@Controller
@RequestMapping(value = "${adminPath}/quartz/task")
public class TaskController extends BaseController{
	
	@Autowired
	public TaskService taskService;

	/**
	 * 定时任务-列表
	 * @param request
	 * @return
	 */
	@RequestMapping("list")
	public String list(Task task,Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("#####定时任务列表");
		//商品规格列表
		Page<Task> page = taskService.findAllTasks(new Page<Task>(request, response), task);
		model.addAttribute("page", page);
		return "modules/quartz/taskList";
	}
	
	/**
	 * 定时任务-查看/修改/添加
	 * @param request
	 * @param taskJob
	 * @return
	 */
	@RequiresPermissions(value={"quartz:task:view","quartz:task:add","quartz:task:edit"},logical=Logical.OR)
	@RequestMapping("form")
	public String form(Task task, Model model,HttpServletRequest request) {
		try {
			if(task.getJobId() != null && !"".equals(task.getJobId())){
				task = taskService.findTaskById(task.getJobId());
			}
			model.addAttribute("task", task);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/quartz/taskForm";
	}
	
	/**
	 * 定时任务-修改/保存
	 * @param request
	 * @param taskJob
	 * @return
	 */
	@RequestMapping("save")
	public String save(Task task, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			if(null != task.getJobId() && task.getJobId().length() > 0){
				//修改
				taskService.updateTask(task);
			}else{
				//新增
				taskService.addTaskJob(task);
			}
			addMessage(redirectAttributes, "定时任务-修改/保存-成功");
		} catch (Exception e) {
			logger.error("定时任务-修改/保存 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "定时任务-修改/保存-出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/quartz/task/list";
	}
	
	/**
	 * 定时任务-删除
	 * @param request
	 * @param taskJob
	 * @return
	 */
	@RequiresPermissions(value={"quartz:task:del"},logical=Logical.OR)
	@RequestMapping("delete")
	public String delete(Task task, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			task = taskService.findTaskById(task.getJobId());
			taskService.deleteByLogic(task);//删除任务数据库
			taskService.deleteJob(task);//删除任务
			addMessage(redirectAttributes, "定时任务-删除："+task.getJobName()+"  成功");
		} catch (Exception e) {
			logger.error("定时任务-删除 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "定时任务-删除-出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/quartz/task/list";
	}
	
	/**
	 * 根据id，修改任务状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"updatetaskstatus"})
	public @ResponseBody Map<String, String> updatetaskstatus(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("id");
			String status = request.getParameter("status");
			logger.info("#####[任务id]:"+id+" | [状态status:]"+status);
			if(null != id && id.length() >0 && null != status && status.length() >0){
				Task task = taskService.findTaskById(id);
				if(null != task){
					//设置任务状态
					task.setJobStatus(status);
					int result = taskService.updateTaskStatus(task);
					if(1 == result){
						taskService.changeStatus(task, status);
						jsonMap.put("STATUS", "OK");
						jsonMap.put("MESSAGE", "成功修改任务："+task.getJobName());
					}
				}else{
					jsonMap.put("STATUS", "ERROR");
					jsonMap.put("MESSAGE", "未查询到该任务");
				}
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "查询失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("根据id，修改任务状态-出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改任务状态-出现异常，请与管理员联系");
		}
		return jsonMap;	
	}
	
	/**
	 * 根据id，立即执行该任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"runjobnow"})
	public @ResponseBody Map<String, String> runjobnow(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("id");
			logger.info("#####[任务id]:"+id);
			if(!StringUtils.isEmpty(id) && id.length() >0){
				Task task = taskService.findTaskById(id);
				
				if("0".equals(task.getJobStatus())){
					//只有当任务状态为[0:开启]状态时，才能执行任务
					taskService.runJobNow(task);
					jsonMap.put("STATUS", "OK");
					jsonMap.put("MESSAGE", "成功执行任务："+task.getJobName());
				}else{
					jsonMap.put("STATUS", "OK");
					jsonMap.put("MESSAGE", "执行任务："+task.getJobName()+" 失败，失败原因:任务状态为关闭，请先开启定时器");
				}
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "查询失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("根据任务id，立即执行该任务-出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "立即执行任务-出现异常，请与管理员联系");
		}
		return jsonMap;	
	}
	
	/**
	 * 验证时间语法是否正确
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"checkcron"})
	public @ResponseBody Map<String, String> checkcron(HttpServletRequest request){ 
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String cron = request.getParameter("cron");
			logger.info("#####[任务id]:"+cron);
			if(null != cron && cron.length() > 0){
				CronScheduleBuilder.cronSchedule(cron);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "请输入执行时间");
			}
		} catch (Exception e) {
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "格式错误，请输入正确的时间格式");
		}
		return jsonMap;
	}
	
	/**
	 * 定时任务日志-列表
	 * @param tasklog
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions(value={"quartz:task:loglist"},logical=Logical.OR)
	@RequestMapping("loglist")
	public String loglist(TaskLog tasklog,Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("#####定时任务Log列表");
		//商品规格列表
		Page<TaskLog> page = taskService.findAllTasksLogs(new Page<TaskLog>(request, response), tasklog);
		model.addAttribute("page", page);
		return "modules/quartz/taskLogList";
	}
	
	/**
	 * 定时任务日志-列表
	 * @param tasklog
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("findlogbyid")
	public String findlogbyid(TaskLog tasklog,Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			if(tasklog.getId() != null && !"".equals(tasklog.getId())){
				tasklog = taskService.findLogById(tasklog.getId());
			}
			model.addAttribute("tasklog", tasklog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/quartz/taskLogForm";
	}
	
}

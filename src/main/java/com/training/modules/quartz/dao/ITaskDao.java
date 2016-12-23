package com.training.modules.quartz.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.quartz.entity.Task;
import com.training.modules.quartz.entity.TaskLog;

/**
 * 定时任务-接口
 * @author kele
 *
 */
@MyBatisDao
public interface ITaskDao extends CrudDao<Task>{

	/**
	 * 查询所有的定时任务
	 * @return
	 */
	public List<Task> findAllTasks();
	
	/**
	 * 查询所有的定时任务
	 * @return
	 */
	public List<Task> findAllTasks(Task task);
	
	/**
	 * 根据定时任务TaskId，查询其定时任务
	 * @param id
	 * @return
	 */
	public Task findTaskById(String id);
	
	/**
	 * 添加定时任务
	 * @param task
	 */
	public void insertTask(Task task);
	
	/**
	 * 修改定时任务
	 * @param task
	 * @return
	 */
	public int updateTask(Task task);
	
	/**
	 * 修改定时任务状态
	 * @param task
	 * @return
	 */
	public int updateTaskStatus(Task task);
	
	/**
	 * 修改定时任务时间表达式
	 * @param task
	 * @return
	 */
	public int updateCronExpression(Task task);
	
	/**
	 * 添加定时任务日志
	 * @param taskLog
	 */
	public void insertTaskLog(TaskLog taskLog);
	
	/**
	 * 查询所有的定时任务Log
	 * @return
	 */
	public List<TaskLog> findAllTasksLogs(TaskLog taskLog);
	
	/**
	 * 根据定时任务Id，查询其定时任务log
	 * @param id
	 * @return
	 */
	public TaskLog findLogById(String id);
}

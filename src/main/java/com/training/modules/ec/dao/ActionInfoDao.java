package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ActionInfo;


/**
 *活动dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface ActionInfoDao extends TreeDao<ActionInfo>{
	
	
	
	/**
	 * 保存数据
	 * @param actionInfo
	 * @return
	 */
	public int insertAction(ActionInfo actionInfo);
	
	/**
	 * 开启，关闭状态
	 * @param actionInfo
	 * @return
	 */
	public int updateStatus(ActionInfo actionInfo);
	/**
	 * 查询全部活动
	 */
	public List<ActionInfo> findAllList();
	
	/**
	 * 定时器查询所有开启 时间已经到的 状态为开启
	 * @return
	 */
	public List<ActionInfo> selectActionStarttime();
	/**
	 * 定时器查询所有关闭时间已经到的 状态为开启，关闭
	 * @return
	 */
	public List<ActionInfo> selectActionCloseTime();
	/**
	 * 更新数据定时器状态
	 * @param actionInfo
	 * @return
	 */
	public int updateExecuteStatus(ActionInfo actionInfo);
	

}

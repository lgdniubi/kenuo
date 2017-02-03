package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.entity.TrainRuleParamType;

/**
 * 培训规则参数Dao
 * @author kele
 * @version 2016-8-12
 */
@MyBatisDao
public interface TrainRuleParamDao extends CrudDao<TrainRuleParam>{
	
	/**
	 * 规则参数类型级联-删除
	 * @param trainRuleParam
	 * @return
	 */
	public int typefordelete(TrainRuleParam trainRuleParam);
	
	/**
	 * 查询所有培训规则参数类型
	 * @param trainRuleParamType
	 * @return
	 */
	public List<TrainRuleParamType> findParamTypeList(TrainRuleParamType trainRuleParamType);
	
	/**
	 * 根据id，查询规则类型
	 * @param trainRuleParamType
	 * @return
	 */
	public TrainRuleParamType findRuleType(TrainRuleParamType trainRuleParamType);
	
	/**
	 * 规则参数类型-保存
	 * @param trainRuleParamType
	 * @return
	 */
	public int insertRuleType(TrainRuleParamType trainRuleParamType);
	
	/**
	 * 规则参数类型-修改
	 * @param trainRuleParamType
	 * @return
	 */
	public int updateRuleType(TrainRuleParamType trainRuleParamType);
	
	/**
	 * 规则参数类型-删除
	 * @param trainRuleParamType
	 * @return
	 */
	public int deleteRuleType(TrainRuleParamType trainRuleParamType);
	
	/**
	 * 通过参数Key查询参数值
	 * @param trainRuleParamType
	 * @return
	 */
	public TrainRuleParam findParamByKey(TrainRuleParam trainRuleParam);
}



package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.RuleParam;


/**
 * 系统规则参数-Mapper
 * @author kele
 * @version 2016-10-19
 */
@MyBatisDao
public interface IRuleParamMapper {

	/**
	 * 查询所有Trains系统规则参数
	 * @param map
	 * @return
	 */
	public List<RuleParam> findAllTrainsRuleParam();
	
	/**
	 * 查询所有Mtmy系统规则参数
	 * @param map
	 * @return
	 */
	public List<RuleParam> findAllMtmyRuleParam();
}

package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmyRuleParamType;

/**
 * 培训规则参数Dao
 * @author kele
 * @version 2016-8-12
 */
@MyBatisDao
public interface MtmyRuleParamDao extends CrudDao<MtmyRuleParam>{
	
	/**
	 * 规则参数类型级联-删除
	 * @param trainRuleParam
	 * @return
	 */
	public int typefordelete(MtmyRuleParam mtmyRuleParamm);
	
	/**
	 * 查询所有培训规则参数类型
	 * @param trainRuleParamType
	 * @return
	 */
	public List<MtmyRuleParamType> findParamTypeList(MtmyRuleParamType mtmyRuleParamType);
	
	/**
	 * 根据id，查询规则类型
	 * @param trainRuleParamType
	 * @return
	 */
	public MtmyRuleParamType findRuleType(MtmyRuleParamType mtmyRuleParamType);
	
	/**
	 * 规则参数类型-保存
	 * @param trainRuleParamType
	 * @return
	 */
	public int insertRuleType(MtmyRuleParamType mtmyRuleParamType);
	
	/**
	 * 规则参数类型-修改
	 * @param trainRuleParamType
	 * @return
	 */
	public int updateRuleType(MtmyRuleParamType mtmyRuleParamType);
	
	/**
	 * 规则参数类型-删除
	 * @param trainRuleParamType
	 * @return
	 */
	public int deleteRuleType(MtmyRuleParamType mtmyRuleParamType);
	
	/**
	 * 根据key，查询规则类型
	 * @param trainRuleParamType
	 * @return
	 */
	public MtmyRuleParam findProByKey(String key);
	/**
	 * 根据规则类型查找规则
	 * @param alianame
	 * @return
	 */
	public List<MtmyRuleParam> queryRuleByType(String alianame);
	/**
	 * 分页查询分销信息
	 * @param mtmyRuleParam
	 * @return
	 */
	public List<MtmyRuleParam> findSaleList(MtmyRuleParam mtmyRuleParam);
	/**
	 * 获取分销类别id
	 * @return
	 */
	public MtmyRuleParam findBySale();
	/**
	 * 查询所有代言人
	 * @return
	 */
	public List<MtmyRuleParam> findspokesman();
}



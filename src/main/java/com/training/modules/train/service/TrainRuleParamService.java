package com.training.modules.train.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.entity.TrainRuleParamType;



/**
 * 培训规则参数Service
 * @author kele
 * @version 2016年3月10日
 */
@Service
@Transactional(readOnly = false)
public class TrainRuleParamService extends CrudService<TrainRuleParamDao,TrainRuleParam> {
	
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	
	/**
	 * 分页展示规则参数列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<TrainRuleParam> find(Page<TrainRuleParam> page, TrainRuleParam trainRuleParam) {
		trainRuleParam.setPage(page);
		page.setList(dao.findList(trainRuleParam));
		return page;
	} 
	
	/**
	 * 根据规则类型-级联删除
	 * @return
	 */
	public int typefordelete(TrainRuleParam trainRuleParam){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			trainRuleParam.setUpdateBy(user);
			trainRuleParam.setUpdateDate(new Date());
		}
		return trainRuleParamDao.typefordelete(trainRuleParam);
	}
	
	/**
	 * 查询所有培训规则参数类型
	 * @return
	 */
	public List<TrainRuleParamType> findParamTypeList(){
		return trainRuleParamDao.findParamTypeList(null);
	}
	
	/**
	 * 分页展示规则参数类型列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<TrainRuleParamType> findRuleTypeList(Page<TrainRuleParamType> page, TrainRuleParamType trainRuleParamType) {
		//分页查询
		trainRuleParamType.setPage(page);
		page.setList(trainRuleParamDao.findParamTypeList(trainRuleParamType));
		return page;
	} 
	
	/**
	 * 根据ID，查询其规则参数类型
	 * @param trainRuleParamType
	 * @return
	 */
	public TrainRuleParamType getRuleType(TrainRuleParamType trainRuleParamType){
		return trainRuleParamDao.findRuleType(trainRuleParamType);
	}
	
	/**
	 * 规则参数类型-添加/修改
	 * @param trainRuleParamType
	 */
	public void saveRuleType(TrainRuleParamType trainRuleParamType) {
		if (trainRuleParamType.getIsNewRecord()){
			//保存
			trainRuleParamDao.insertRuleType(trainRuleParamType);
		}else{
			//修改
			trainRuleParamDao.updateRuleType(trainRuleParamType);
		}
	}
	
	/**
	 * 规则参数类型-删除
	 * @param trainRuleParamType
	 */
	public void deleteRuleType(TrainRuleParamType trainRuleParamType){
		//删除规则参数类型
		trainRuleParamDao.deleteRuleType(trainRuleParamType);
	}
}

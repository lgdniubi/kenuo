package com.training.modules.ec.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyRuleParamDao;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.MtmyRuleParamType;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;



/**
 * 培训规则参数Service
 * @author kele
 * @version 2016年3月10日
 */
@Service
@Transactional(readOnly = false)
public class MtmyRuleParamService extends CrudService<MtmyRuleParamDao,MtmyRuleParam> {
	
	@Autowired
	private MtmyRuleParamDao mtmyRuleParamDao;
	
	/**
	 * 分页展示规则参数列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<MtmyRuleParam> find(Page<MtmyRuleParam> page, MtmyRuleParam mtmyRuleParam) {
		mtmyRuleParam.setPage(page);
		page.setList(dao.findList(mtmyRuleParam));
		return page;
	} 
	/**
	 * 根据规则类型-级联删除
	 * @return
	 */
	public int typefordelete(MtmyRuleParam mtmyRuleParam){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			mtmyRuleParam.setUpdateBy(user);
			mtmyRuleParam.setUpdateDate(new Date());
		}
		return mtmyRuleParamDao.typefordelete(mtmyRuleParam);
	}
	
	/**
	 * 查询所有培训规则参数类型
	 * @return
	 */
	public List<MtmyRuleParamType> findParamTypeList(){
		return mtmyRuleParamDao.findParamTypeList(null);
	}
	
	/**
	 * 分页展示规则参数类型列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<MtmyRuleParamType> findRuleTypeList(Page<MtmyRuleParamType> page, MtmyRuleParamType mtmyRuleParamType) {
		//分页查询
		mtmyRuleParamType.setPage(page);
		page.setList(mtmyRuleParamDao.findParamTypeList(mtmyRuleParamType));
		return page;
	} 
	
	/**
	 * 根据ID，查询其规则参数类型
	 * @param trainRuleParamType
	 * @return
	 */
	public MtmyRuleParamType getRuleType(MtmyRuleParamType mtmyRuleParamType){
		return mtmyRuleParamDao.findRuleType(mtmyRuleParamType);
	}
	
	/**
	 * 规则参数类型-添加/修改
	 * @param trainRuleParamType
	 */
	public void saveRuleType(MtmyRuleParamType mtmyRuleParamType) {
		if (mtmyRuleParamType.getIsNewRecord()){
			//保存
			mtmyRuleParamDao.insertRuleType(mtmyRuleParamType);
		}else{
			//修改
			mtmyRuleParamDao.updateRuleType(mtmyRuleParamType);
		}
	}
	
	/**
	 * 规则参数类型-删除
	 * @param trainRuleParamType
	 */
	public void deleteRuleType(MtmyRuleParamType mtmyRuleParamType){
		//删除规则参数类型
		mtmyRuleParamDao.deleteRuleType(mtmyRuleParamType);
	}
	/**
	 * 根据规则类型查找规则
	 * @param alianame
	 * @return
	 */
	public List<MtmyRuleParam> queryRuleByType(String alianame){
		return this.mtmyRuleParamDao.queryRuleByType(alianame);
	}
	
	/**
	 * 获取分销类别id
	 * @return
	 */
	public MtmyRuleParam findBySale(){
		return dao.findBySale();
	}
	/**
	 * 分页展示分销规则参数列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<MtmyRuleParam> findSaleList(Page<MtmyRuleParam> page, MtmyRuleParam mtmyRuleParam) {
		mtmyRuleParam.setPage(page);
		page.setList(dao.findSaleList(mtmyRuleParam));
		return page;
	} 
}

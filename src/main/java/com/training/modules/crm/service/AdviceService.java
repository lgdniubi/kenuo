package com.training.modules.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.AdviceDao;
import com.training.modules.crm.entity.Complain;
import com.training.modules.sys.entity.User;
import com.training.modules.train.utils.ScopeUtils;

/**
 * @author：星星
 * @description：投诉咨询service类 
 * 2017年3月10日
 */ 
@Service
public class AdviceService extends CrudService<AdviceDao, Complain>{
	
	/**
	 * @author：星星
	 * @description：保存问题
	 * 2017年3月17日
	 */
	@Transactional(readOnly = false)
	public int saveQuestion(Complain complain){
		int id = dao.saveQuestion(complain);
		return id;
	}
	
	/**
	 * @author：星星
	 * @description：保存处理过程
	 * 2017年3月17日
	 */
	@Transactional(readOnly = false)
	public void saveSolve(Complain complain){
       dao.saveSolve(complain);		
	}

	/**
	 * @author：星星
	 * @description：问题详情查询
	 * 2017年3月17日
	 */
	public Complain detailed(Complain complain) {
		return dao.detailed(complain);
	}

	/**
	 * @author：星星
	 * @description：获取一个问题的所有处理过程
	 * 2017年3月17日
	 */
	public List<Complain> procedure(Complain complain){
		return dao.procedure(complain);
	}

	/**
	 * @author：星星
	 * @description：查询最后一次处理过程
	 * 2017年3月17日
	 */
	public Complain selectHandle(Complain complain) {		
		return dao.selectHandle(complain);		
	}

	/**
	 * @author：星星
	 * @description：改变问题的状态
	 * 2017年3月17日
	 */
	@Transactional(readOnly = false)
	public void creatHandle(Complain complains) {
		 dao.creatHandle(complains);		
	}
	
	/**
	 * @author：星星
	 * @description：保存处理过程
	 * 2017年3月17日
	 */
	@Transactional(readOnly = false)
	public void saveHandle(Complain complain) {
		dao.saveHandle(complain);
	}

	/**
	 * @author：星星
	 * @description：查询是否会员
	 * 2017年3月17日
	 */
	public Complain selectMember(Complain complain) {
		return dao.selectMember(complain);
		
	}
	
	/**
	 * @author：星星
	 * @description：查询是否会员
	 * 2017年3月17日
	 */
	public Complain selectMemb(Complain complain) {
		return dao.selectMemb(complain);
		
	}
	/**
	 * @author：星星
	 * @description：查询未/已处理的问题list
	 * 2017年3月17日
	 */
	public List<Complain> selectStatus(Complain complain) {
		return dao.selectStatus(complain);
	}
	
	/**
	 * @author：星星
	 * @description：改变问题的状态
	 * 2017年3月17日
	 */
	@Transactional(readOnly = false)
	public void creatResult(Complain complain) {
        dao.creatResult(complain); 		
	}
	
	/**
	 * 
	 * @author：星星
	 * @description：获取需要改变问题来源的处理过程Id
	 * 2017年3月23日
	 */
	public Complain selectId(Complain complain) {
		return dao.selectId(complain);		
	}

	public List<User> findUser(User user) {
		return dao.findUser(user);
	}

	/**
	 * @author：星星
	 * @description：根据电话号码查询会员信息
	 * 2017年3月10日
	 */
	public Complain getUser(String mobile) {		
		return dao.getUser(mobile);
	}

	/**
	 * @author：星星
	 * @description：验证转交人
	 * 2017年3月10日
	 */
	public List<Complain> getRedirectUserId(Complain complain) {
		return dao.getRedirectUserId(complain);
	}
	
	/**
	 * @author：星星
	 * @description：已/未处理/快速来电个自总记录数
	 * 2017年3月10日
	 */
	public int getCount(Complain complain) {	
		return dao.getCount(complain);
	}
	
	/**
	 * @author：星星
	 * @description：根据电话查询是否有投诉记录
	 * 2017年3月10日
	 */
	public List<Complain> selectSeek(Complain complain) {
		return dao.selectSeek(complain);
	}

	/**
	 * 根据用户id查询投诉咨询集合,带有数据权限
	 * @param 土豆
	 * @param complain
	 * @return
	 * 2018-4-2
	 */
	public Page<Complain> newfindPage(Page<Complain> page, Complain complain) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		complain.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("q", "orderOrRet"));
		// 设置分页参数
		complain.setPage(page);
		// 执行分页查询
		page.setList(dao.newfindPage(complain));
		return page;
	}

		
}


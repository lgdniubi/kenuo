package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmySaleRelieveDao;
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.MtmySaleRelieveLog;



/**
 * 分销  Service层
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmySaleRelieveService extends CrudService<MtmySaleRelieveDao,MtmySaleRelieve> {
	
	/**
	 * 分销  解除用户分页查询
	 * @param page
	 * @param mtmySaleRelieve
	 * @return
	 */
	public Page<MtmySaleRelieve> find(Page<MtmySaleRelieve> page, MtmySaleRelieve mtmySaleRelieve) {
		mtmySaleRelieve.setPage(page);
		page.setList(dao.findAllRelieve(mtmySaleRelieve));
		return page;
	} 
	/**
	 * 修改申请表申请状态及备注
	 * @param mtmySaleRelieve
	 */
	public void updateStatus(MtmySaleRelieve mtmySaleRelieve){
		dao.updateStatus(mtmySaleRelieve);
	}
	/**
	 * 修改原始表申请状态
	 * @param mtmySaleRelieve
	 */
	public void updateBy(MtmySaleRelieve mtmySaleRelieve){
		dao.updateBy(mtmySaleRelieve);
	}
	
	/**
	 * 同意用户解除后  修改原始表parent_id
	 * @param mtmySaleRelieve
	 */
	public void updateParentId(MtmySaleRelieve mtmySaleRelieve){
		dao.updateParentId(mtmySaleRelieve);
		
	}
	/**
	 * 查询所有A
	 * @param mtmySaleRelieve
	 * @return
	 */
	public Page<MtmySaleRelieve> findAllA(Page<MtmySaleRelieve> page,MtmySaleRelieve mtmySaleRelieve){
		mtmySaleRelieve.setPage(page);
		page.setList(dao.findAllA(mtmySaleRelieve));
		return page;
	}
	/**
	 * 查询A用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	public MtmySaleRelieve findByUserId(MtmySaleRelieve mtmySaleRelieve){
		return dao.findByUserId(mtmySaleRelieve);
	}
	/**
	 * 查询单个A级用户所有下级详情
	 * @param page
	 * @param mtmySaleRelieve
	 * @return
	 */
	public Page<MtmySaleRelieve> findByParentId(Page<MtmySaleRelieve> page,MtmySaleRelieve mtmySaleRelieve){
		mtmySaleRelieve.setPage(page);
		page.setList(dao.findByParentId(mtmySaleRelieve));
		return page;
	}
	/**
	 * 查询A下级单个用户贡献信息
	 * @param page
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public Page<MtmySaleRelieveLog> findUserDetails(Page<MtmySaleRelieveLog> page,MtmySaleRelieveLog mtmySaleRelieveLog){
		mtmySaleRelieveLog.setPage(page);
		page.setList(dao.findUserDetails(mtmySaleRelieveLog));
		return page;
	}
	/**
	 *  A用户收益明细
	 * @param page
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public Page<MtmySaleRelieveLog> findUserEarnings(Page<MtmySaleRelieveLog> page,MtmySaleRelieveLog mtmySaleRelieveLog){
		mtmySaleRelieveLog.setPage(page);
		page.setList(dao.findUserEarnings(mtmySaleRelieveLog));
		return page;
	}
	/**
	 * 按天查询用户当天的收益明细
	 * @param page
	 * @param mtmySaleRelieveLog
	 * @return
	 */
	public Page<MtmySaleRelieveLog> findUserAllOrder(Page<MtmySaleRelieveLog> page,MtmySaleRelieveLog mtmySaleRelieveLog){
		mtmySaleRelieveLog.setPage(page);
		page.setList(dao.findUserAllOrder(mtmySaleRelieveLog));
		return page;
	}
	/**
	 * 查询A以外的分销用户
	 * @param page
	 * @param mtmySaleRelieve
	 * @return
	 */
	public Page<MtmySaleRelieve> findOtherUsers(Page<MtmySaleRelieve> page,MtmySaleRelieve mtmySaleRelieve){
		mtmySaleRelieve.setPage(page);
		page.setList(dao.findOtherUsers(mtmySaleRelieve));
		return page;
	}
	/**
	 * 查询A以外的分销用户详情
	 * @param mtmySaleRelieve
	 * @return
	 */
	public MtmySaleRelieve findOtherUsersByUserId(MtmySaleRelieve mtmySaleRelieve){
		return dao.findOtherUsersByUserId(mtmySaleRelieve);
	}
	/**
	 * 日订单结算
	 * @return
	 */
	public void SaleOrderInsertLog(){
		dao.SaleOrderInsertLog();
	}
	/**
	 * 日结算存储过程
	 * @return
	 */
	public void SaleDayAccounts(){
		dao.SaleDayAccounts();
	};
	/**
	 * 总结算
	 * @return
	 */
	public void SaleSettleDayAccounts(){
		dao.SaleSettleDayAccounts();
	};
}

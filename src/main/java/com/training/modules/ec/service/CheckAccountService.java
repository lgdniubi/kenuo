package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CheckAccountDao;
import com.training.modules.ec.entity.MtmyCheckAccount;

/**
 * 对账Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class CheckAccountService extends CrudService<CheckAccountDao, MtmyCheckAccount>{
	/**
	 * 导入ping++数据
	 * @param mtmyCheckAccount
	 */
	public void insterPing(MtmyCheckAccount mtmyCheckAccount){
		mtmyCheckAccount.setGroupFlag(new SimpleDateFormat("yyyyMMdd").format(mtmyCheckAccount.getPayDate()));
		dao.insert(mtmyCheckAccount);
	}
	/**
	 * 分页查询
	 * @param mtmyCheckAccount
	 * @return
	 */
	public Page<MtmyCheckAccount> findPage(Page<MtmyCheckAccount> page,MtmyCheckAccount mtmyCheckAccount){
		mtmyCheckAccount.setPage(page);
		page.setList(dao.findAllList(mtmyCheckAccount));
		return page;
	}
	/**
	 * 查询所有标示
	 * @return
	 */
	public List<MtmyCheckAccount> findGroupFlag(){
		return dao.findGroupFlag();
	}
	/**
	 * 导出
	 * @param mtmyCheckAccount
	 * @return
	 */
	public List<MtmyCheckAccount> export(MtmyCheckAccount mtmyCheckAccount){
		return dao.findList(mtmyCheckAccount);
	}
	/**
	 * 验证商户订单号是否存在
	 * @param mtmyCheckAccount
	 * @return
	 */
	public int findByOrderNo(MtmyCheckAccount mtmyCheckAccount){
		return dao.findByOrderNo(mtmyCheckAccount);
	}
}

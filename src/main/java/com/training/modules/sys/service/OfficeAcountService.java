/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.dao.OfficeAcountDao;	
import com.training.modules.sys.entity.OfficeAcount;

/**
 * 机构Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeAcountService extends CrudService<OfficeAcountDao, OfficeAcount> {
	
	@Autowired
	private OfficeAcountDao officeAcountDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	/**
	 * 
	 * @param page
	 * @param officeAcount
	 * @return
	 */
	public Page<OfficeAcount> findOfficeList(Page<OfficeAcount> page, OfficeAcount officeAcount) {
		/// 设置分页参数
		officeAcount.setPage(page);
		// 执行分页查询
		page.setList(officeAcountDao.findOfficeList(officeAcount));
		return page;
	}

	/**
	 * 查询机构账户
	 * @param officeId
	 * @return
	 */
	public OfficeAcount findOfficeAcount(String officeId){
		return this.officeAcountDao.findOfficeAcount(officeId);
	}
	
	/**
	 * 创建账户
	 * @param OfficeAcount
	 */
	@Transactional(readOnly = false)
	public void saveOfficeAcount(OfficeAcount officeAcount){
		this.officeAcountDao.saveOfficeAcount(officeAcount);
	}
	
	/**
	 * 变更信用额度
	 * @param OfficeAcount
	 */
	@Transactional(readOnly = false)
	public void updateOfficeCreditLimit(OfficeAcount OfficeAcount){
		//缓存锁
		RedisLock redisLock = new RedisLock(redisClientTemplate, "account_lock_office_id"+OfficeAcount.getOfficeId());
		redisLock.lock();
		double usedLimit = this.officeAcountDao.queryusedLimit(OfficeAcount);
		if(OfficeAcount.getUseLimit() > usedLimit)
			throw new RuntimeException("可用额度发生改变，暂不可修改");
		this.officeAcountDao.updateOfficeCreditLimit(OfficeAcount);
		redisLock.unlock();
		//保存额度修改日志
		OfficeAcount.preInsert();
		officeAcountDao.saveOfficeCreditLog(OfficeAcount);
	}
	
	public Page<OfficeAcount> findCreditLogPage(Page<OfficeAcount> page, OfficeAcount officeAcount) {
		// 设置分页参数
		officeAcount.setPage(page);
		// 执行分页查询
		page.setList(officeAcountDao.findCreditLogList(officeAcount));
		return page;
	}
	
}

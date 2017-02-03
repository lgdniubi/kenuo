package com.training.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.DateUtils;
import com.training.modules.sys.dao.BugLogDao;
import com.training.modules.sys.entity.BugLog;


@Service
@Transactional(readOnly = true)
public class BugLogService extends CrudService<BugLogDao,BugLog>{
	
	@Autowired
	private BugLogDao bugLogDao;
	
	public Page<BugLog> findPage(Page<BugLog> page, BugLog buglog) {
		
		// 设置默认时间范围，默认当前月
		if (buglog.getBeginDate() == null){
			buglog.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (buglog.getEndDate() == null){
			buglog.setEndDate(DateUtils.addMonths(buglog.getBeginDate(), 1));
		}
		
		return super.findPage(page, buglog);
		
	}
	
	

	/**
	 * 删除全部数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void empty(){
		
		bugLogDao.empty();
	}
	
	
	

}

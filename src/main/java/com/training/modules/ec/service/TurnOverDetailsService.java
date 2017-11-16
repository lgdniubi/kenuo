package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.TurnOverDetailsDao;
import com.training.modules.ec.entity.TurnOverDetails;

/**
 * 营业额明细表Service
 * @author xiaoye  2017年11月16日
 *
 */
@Service
@Transactional(readOnly = false)
public class TurnOverDetailsService extends CrudService<TurnOverDetailsDao, TurnOverDetails>{
	
	@Autowired
	private TurnOverDetailsDao turnOverDetailsDao;
	
	/**
	 * 同步details表的数据到营业额明细表中
	 * @param turnOverDetails
	 */
	public void saveTurnOverDetails(TurnOverDetails turnOverDetails){
		turnOverDetailsDao.saveTurnOverDetails(turnOverDetails);
	}
}

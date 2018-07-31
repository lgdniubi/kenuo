package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.ConsignDao;
import com.training.modules.crm.entity.Consign;
import com.training.modules.crm.entity.CrmDepositLog;


/**    
* kenuo      
* @description：   
* @author：sharp   
* @date：2017年3月7日            
*/
@Service
@Transactional(readOnly = false)
public class ConsignService extends CrudService<ConsignDao,Consign>{
		
	/**
	 * @param 
	 * @return int
	 * 更新某一条数据
	 */
	public int updateSingle(Consign consign){
		Integer result = dao.updateSingle(consign);
		return result ;
	}
	
	/**
	 * @param 
	 * @return Page<Consign>
	 * 根据用户ID查找寄存列表
	 */
	public Page<Consign> getConsignList(Page<Consign> page,Consign dto){
		dto.setPage(page);
		page.setList(dao.findConsignList(dto));
		return page;
	}

	/**
	 * 记录物品存储档案的日志
	 * @param log
	 */
	public void saveCrmDepositLog(CrmDepositLog log) {
		dao.saveCrmDepositLog(log);
	}

	/**
	 * 获取物品寄存操作日志记录
	 * @param page
	 * @param log
	 * @return
	 */
	public Page<CrmDepositLog> findDepositList(Page<CrmDepositLog> page, CrmDepositLog log) {
		log.setPage(page);
		page.setList(dao.findDepositList(log));
		return page;
	}

}

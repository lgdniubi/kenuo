package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.Consign;
import com.training.modules.crm.entity.CrmDepositLog;

/**    
* kenuo      
* @description：  寄存DAO接口
* @author：sharp   
* @date：2017年3月7日            
*/
@MyBatisDao
public interface ConsignDao extends CrudDao<Consign> {

	
	public List<Consign> findConsignList(Consign entity);	
	public int updateSingle(Consign entity);
	/**
	 * 保存/修改 物品寄存档案日志记录
	 * @param log
	 */
	public void saveCrmDepositLog(CrmDepositLog log);
	/**
	 * 获取物品寄存档案日志记录
	 * @param log
	 * @return
	 */
	public List<CrmDepositLog> findDepositList(CrmDepositLog log);
}

package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.crm.dao.GoodsUsageDao;
import com.training.modules.crm.entity.GoodsUsage;

/**    
* kenuo      
* 用户产品记录   
* @author：sharp   
* @date：2017年3月7日            
*/
@Service
@Transactional(readOnly = false)
public class GoodsUsageService extends CrudService<GoodsUsageDao,GoodsUsage>{
		
	/**
	 * @param 
	 * @return int
	 * 更新某一条数据
	 */
	public int updateSigle(GoodsUsage goodsUsage){
		Integer result = dao.updateSigle(goodsUsage);
		return result ;
	}
	
	/**
	 * @param 
	 * @return Page<Consign>
	 * 根据用户ID查找使用记录列表
	 */
	public Page<GoodsUsage> getConsignList(Page<GoodsUsage> page,GoodsUsage dto){
		dto.setPage(page);
		page.setList(dao.findUsageList(dto));
		return page;
	}
}

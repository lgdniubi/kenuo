package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.AvaliableCoupon;

/**    
* kenuo      
* @description：可用红包 
* @author：sharp   
* @date：2017年3月7日            
*/
@MyBatisDao
public interface AvaliableCouponDao extends CrudDao<AvaliableCoupon> {

	
	public List<AvaliableCoupon> findAvaliableList(AvaliableCoupon entity);	
	public int getAvaliableNumber(String userId);
}

package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.CrmDict;

/**    
* kenuo      
* @description：  字典DAO接口
* @author：sharp   
* @date：2017年3月7日            
*/
@MyBatisDao
public interface CrmDictDao extends CrudDao<CrmDict> {

	public List<String> findTypeList(CrmDict dict);
	public CrmDict findDict(CrmDict dict);
	public List<CrmDict> getSkinFile();
		
	                     
}

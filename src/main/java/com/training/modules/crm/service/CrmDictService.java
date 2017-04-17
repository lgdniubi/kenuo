package com.training.modules.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.CacheUtils;
import com.training.modules.crm.dao.CrmDictDao;
import com.training.modules.crm.entity.CrmDict;
import com.training.modules.sys.utils.DictUtils;

/**    
* kenuo      
* @description：   
* @author：sharp   
* @date：2017年3月7日            
*/
@Service
@Transactional(readOnly = true)
public class CrmDictService extends CrudService<CrmDictDao, CrmDict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new CrmDict());
	}

	@Transactional(readOnly = false)
	public void save(CrmDict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(CrmDict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	/**
	 * 查询单个字段属性
	 * @param dict
	 * @return
	 */
	public CrmDict findDict(CrmDict dict){
		return dao.findDict(dict);
	}
	
	/**
	 * @param 无需参数
	 * @return List<CrmDict>
	 * @description 获取数据字典所有关于皮肤档案的条目
	 */
	public List<CrmDict> getSkinFile(){
		return dao.getSkinFile();
	}
}

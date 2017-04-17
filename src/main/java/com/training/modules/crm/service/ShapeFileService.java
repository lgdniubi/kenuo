package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.crm.dao.ShapeFileDao;
import com.training.modules.crm.entity.ShapeFile;

/**    
* kenuo      
* @description：形体档案Service   
* @author：sharp   
* @date：2017年3月8日            
*/
@Service
@Transactional(readOnly = false)
public class ShapeFileService extends CrudService<ShapeFileDao,ShapeFile> {
	
	
	@Transactional(readOnly = false)
	public void save(ShapeFile file) {
		super.save(file);
	}

	public Integer update(ShapeFile file) {
		return dao.update(file);
	}
}

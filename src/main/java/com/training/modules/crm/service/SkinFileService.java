package com.training.modules.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.crm.dao.SkinFileDao;
import com.training.modules.crm.entity.SkinFile;

/**
 * kenuo
 * @description：皮肤档案Service
 * @author：sharp @date：2017年3月8日
 */
@Service
@Transactional(readOnly = false)
public class SkinFileService extends CrudService<SkinFileDao, SkinFile> {

	/**
	 * skinFile的修改
	 * @param
	 * @return Integer
	 * @description
	 */
	public int update(SkinFile file) {
		return dao.update(file);
	}
}

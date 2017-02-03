package com.training.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.utils.CommonScopeUtils;
import com.training.modules.sys.dao.SpecBeauticianDao;
import com.training.modules.sys.entity.SpecBeautician;

/**
 * 特殊美容师Service
 * @author 小叶  2016-12-29 
 *
 */
@Service
@Transactional(readOnly = false)
public class SpecBeauticianService extends CrudService<SpecBeauticianDao, SpecBeautician>{

	@Autowired
	private SpecBeauticianDao specBeauticianDao;
	
	/**
	 * 查询出所有的特殊美容师
	 */
	public List<SpecBeautician> findAllList(){
		return specBeauticianDao.findAllList();
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param specBeautician
	 * @return
	 */
	public Page<SpecBeautician> findList(Page<SpecBeautician> page, SpecBeautician specBeautician) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		specBeautician.getSqlMap().put("dsf", CommonScopeUtils.dataScopeFilter("sb"));
		// 设置分页参数
		specBeautician.setPage(page);
		// 执行分页查询
		page.setList(specBeauticianDao.findList(specBeautician));
		return page;
	}
	
	/**
	 * 插入新的特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public void insertSpecBeautician(SpecBeautician specBeautician){
		specBeauticianDao.insertSpecBeautician(specBeautician);
	}
	
	/**
	 * 逻辑删除特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public void deleteSpecBeautician(SpecBeautician specBeautician){
		specBeauticianDao.deleteSpecBeautician(specBeautician);
	}
	
	/**
	 * 物理删除特殊美容师
	 * @param specBeautician
	 * @return
	 */
	public void newDeleteSpecBeautician(SpecBeautician specBeautician){
		specBeauticianDao.newDeleteSpecBeautician(specBeautician);
	}
	
}

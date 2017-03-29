package com.training.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.dao.SpecialityDao;
import com.training.modules.sys.entity.Speciality;

/**
 * 特长service
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class SpecialityService extends CrudService<SpecialityDao, Speciality>{
	
	@Autowired
	private SpecialityDao scialityDao;
	
	
	/**
	 * 查询所有
	 * @return
	 */
	
	public List<Speciality> findALLList(){
			return scialityDao.findAllList();
		
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param speciality
	 * @return
	 */
	

	public Page<Speciality> findSpeciality(Page<Speciality> page, Speciality speciality) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		speciality.getSqlMap().put("dsf", dataScopeFilter(speciality.getCurrentUser(),"o"));
		// 设置分页参数
		speciality.setPage(page);
		// 执行分页查询
		page.setList(scialityDao.findList(speciality));
		return page;
	}
	/**
	 * 保存更新信息
	 * @param speciality
	 */
	@Transactional(readOnly = false)
	public void saveSpeciality(Speciality speciality){
		if(StringUtils.isBlank(speciality.getId())){
			speciality.preInsert();
			scialityDao.insertSpec(speciality);
		}else{
			scialityDao.updateSpec(speciality);
		}
		
	}
	/**
	 * 删除数据
	 * @param speciality
	 */
	public void deleteSpeciality(Speciality speciality){
		
		scialityDao.deleteSpeciality(speciality);
	}
	
	

}

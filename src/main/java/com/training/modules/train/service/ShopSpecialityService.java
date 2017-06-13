package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.ShopSpecialityDao;
import com.training.modules.train.entity.ShopSpeciality;

/**
 * 店铺标签管理  Service
 * @author 土豆
 * @version 2017-06-08
 */

@Service
@Transactional(readOnly = false)
public class ShopSpecialityService extends CrudService<ShopSpecialityDao, ShopSpeciality>{
	
	@Autowired
	private ShopSpecialityDao shopSpecialityDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param shopSpeciality
	 * @return
	 */
	public Page<ShopSpeciality> findShopSpeciality(Page<ShopSpeciality> page, ShopSpeciality shopSpeciality) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		shopSpeciality.getSqlMap().put("dsf", dataScopeFilter(shopSpeciality.getCurrentUser(),"o"));
		// 设置分页参数
		shopSpeciality.setPage(page);
		// 执行分页查询
		page.setList(shopSpecialityDao.findList(shopSpeciality));
		return page;
	}
	
	/**
	 * 保存/修改     信息
	 * @param shopSpeciality
	 */
	public void saveShopSpeciality(ShopSpeciality shopSpeciality){
		if(shopSpeciality.getShopSpecialityid() != 0){
			shopSpecialityDao.update(shopSpeciality);
		}else{
			shopSpecialityDao.insert(shopSpeciality);
		}
	}
	
	/**
	 * 删除数据 (根据id逻辑删除)
	 * @param shopSpeciality
	 */
	public void deleteShopSpeciality(ShopSpeciality shopSpeciality) {
		shopSpecialityDao.deleteShopSpeciality(shopSpeciality);
	}

	/**
	 * 查询除del_flag != 0 之外的全部标签
	 * @return
	 */
	public List<ShopSpeciality> findAllShopSpeciality() {
		return shopSpecialityDao.findAllShopSpeciality();
	}

}

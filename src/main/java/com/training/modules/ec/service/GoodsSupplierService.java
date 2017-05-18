package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsSupplierDao;
import com.training.modules.ec.entity.GoodsSupplier;
import com.training.modules.ec.entity.GoodsSupplierContacts;

/**
 * goods_supplier图的Service
 * @author 土豆  2017.5.9
 *
 */
@Service
@Transactional(readOnly = false)
public class GoodsSupplierService extends CrudService<GoodsSupplierDao, GoodsSupplier>{

	@Autowired
	private GoodsSupplierContactsService goodsSupplierContactsService;
	
	/**
	 * 分页查询tab_banner图
	 */
	public Page<GoodsSupplier> findPage(Page<GoodsSupplier> page,GoodsSupplier goodsSupplier){
		goodsSupplier.setPage(page);
		page.setList(dao.findList(goodsSupplier));
		return page;
	}

	/**
	 * 根据ID查询供应商信息
	 * @param goodsSupplier
	 * @return
	 */
	public GoodsSupplier get(GoodsSupplier goodsSupplier) {
		return dao.get(goodsSupplier);
	}
	
	/**
	 * 修改
	 * @param 
	 */
	public void update(GoodsSupplier goodsSupplier){
		dao.update(goodsSupplier);
	}

	/**
	 * 逻辑删除
	 * @param goodsSupplier
	 * @param goodsSupplierContacts 
	 */
	public void deleteGoodsSupplier(GoodsSupplier goodsSupplier, GoodsSupplierContacts goodsSupplierContacts) {
		dao.delete(goodsSupplier);
		goodsSupplierContactsService.deleteByGoodsSupplierId(goodsSupplierContacts);
	}

	/**
	 * 异步 修改状态
	 * @param goodsSupplier
	 */
	public void updateStatus(GoodsSupplier goodsSupplier) {
		dao.updateStatus(goodsSupplier);
	}
}

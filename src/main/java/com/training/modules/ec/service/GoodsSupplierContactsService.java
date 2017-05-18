package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsSupplierContactsDao;
import com.training.modules.ec.entity.GoodsSupplierContacts;

/**
 * goods_supplier_contacts图的Service
 * @author 土豆  2017.5.9
 *
 */
@Service
@Transactional(readOnly = false)
public class GoodsSupplierContactsService extends CrudService<GoodsSupplierContactsDao, GoodsSupplierContacts>{

	@Autowired
	private GoodsSupplierContactsDao goodsSupplierContactsDao;
	
	/**
	 * 分页查询tab_banner图
	 */
	public Page<GoodsSupplierContacts> findPage(Page<GoodsSupplierContacts> page,GoodsSupplierContacts goodsSupplierContacts){
		goodsSupplierContacts.setPage(page);
		page.setList(dao.findList(goodsSupplierContacts));
		return page;
	}

	/**
	 * 根据ID查询供应商信息
	 * @param goodsSupplier
	 * @return
	 */
	public GoodsSupplierContacts get(GoodsSupplierContacts goodsSupplierContacts) {
		return dao.get(goodsSupplierContacts);
	}
	
	/**
	 * 修改
	 * @param 
	 */
	public void update(GoodsSupplierContacts goodsSupplierContacts){
		dao.update(goodsSupplierContacts);
	}


	/**
	 * 逻辑删除
	 * @param GoodsSupplierContacts
	 */
	public void deleteGoodsSupplierContacts(GoodsSupplierContacts goodsSupplierContacts) {
		dao.delete(goodsSupplierContacts);
	}

	/**
	 * 异步 修改状态
	 * @param goodsSupplier
	 */
	public void updateStatus(GoodsSupplierContacts goodsSupplierContacts) {
		dao.updateStatus(goodsSupplierContacts);
	}

	/**
	 * 获取供应商下面是否还有联系人
	 * @param goodsSupplierContacts
	 * @return
	 */
	public boolean getCount(GoodsSupplierContacts goodsSupplierContacts) {
		return dao.getCount(goodsSupplierContacts)>0;
	}

	/**
	 * 根据供应商ID逻辑删除联系人
	 * @param goodsSupplierContacts
	 */
	public void deleteByGoodsSupplierId(GoodsSupplierContacts goodsSupplierContacts) {
		goodsSupplierContactsDao.deleteByGoodsSupplierId(goodsSupplierContacts);
	}

}

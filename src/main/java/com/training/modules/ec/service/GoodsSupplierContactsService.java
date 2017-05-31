package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsSupplierContactsDao;
import com.training.modules.ec.entity.GoodsSupplierContacts;

/**
 * goods_supplier_contacts的Service
 * @author 土豆  2017.5.9
 *
 */
@Service
@Transactional(readOnly = false)
public class GoodsSupplierContactsService extends CrudService<GoodsSupplierContactsDao, GoodsSupplierContacts>{

	@Autowired
	private GoodsSupplierContactsDao goodsSupplierContactsdao;
	
	/**
	 * 分页查询
	 */
	public List<GoodsSupplierContacts> findList(GoodsSupplierContacts goodsSupplierContacts){
		return goodsSupplierContactsdao.findList(goodsSupplierContacts);
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
		goodsSupplierContactsdao.deleteByGoodsSupplierId(goodsSupplierContacts);
	}

}

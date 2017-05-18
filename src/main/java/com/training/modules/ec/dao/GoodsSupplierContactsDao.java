package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSupplierContacts;

/**
 * 
 * @author 土豆  2017.5.9
 *
 */
@MyBatisDao
public interface GoodsSupplierContactsDao extends CrudDao<GoodsSupplierContacts>{

	/**
	 * 异步 修改状态
	 * @param goodsSupplierContacts
	 */
	public void updateStatus(GoodsSupplierContacts goodsSupplierContacts);

	/**
	 * 获取供应商下面是否还有联系人
	 * @param goodsSupplierContacts
	 * @return
	 */
	public int getCount(GoodsSupplierContacts goodsSupplierContacts);

	/**
	 * 根据供应商ID逻辑删除联系人
	 * @param goodsSupplierContacts
	 */
	public void deleteByGoodsSupplierId(GoodsSupplierContacts goodsSupplierContacts);

	
}

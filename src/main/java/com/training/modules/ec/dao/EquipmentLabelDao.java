package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.EquipmentLabel;
import com.training.modules.ec.entity.GoodsEquipmentLabel;

/**
 * 设备标签Dao
 * @author 小叶   2017-1-10 
 *
 */
@MyBatisDao
public interface EquipmentLabelDao extends CrudDao<EquipmentLabel>{
	
	/**
	 * 查询出所有的设备标签
	 */
	public List<EquipmentLabel> findAllList(String newFlag);
	
	
	/**
	 * 新增设备标签
	 * @param equipmentLabel
	 * @return
	 */
	public int insertEquipmentLabel(EquipmentLabel equipmentLabel);
	
	/**
	 * 编辑设备标签
	 * @param equipmentLabel
	 * @return
	 */
	public int updateEquipmentLabel(EquipmentLabel equipmentLabel);
	
	/**
	 * 逻辑删除设备标签
	 * @param 
	 * @return
	 */
	public int deleteEquipmentLabel(EquipmentLabel equipmentLabel);
	
	/**
	 * 验证设备编号
	 * @param no
	 * @return
	 */
	public int getByNO(String no);
	
	/**
	 * 验证设备名称
	 * @param name
	 * @return
	 */
	public int getByName(String name);
	
	/**
	 * 根据商品id 查找设备标签
	 * @param goodsId
	 * @return
	 */
	public List<EquipmentLabel> findEquipmentLabelListByGoodsId(int goodsId);
	
	/**
	 * 插入商品设备标签中间表
	 * @param 
	 * @return
	 */
	public int insertGoodsEquipmentLabel(List<GoodsEquipmentLabel> list);
	
	/**
	 * 删除商品设备标签表中间冗余部分数据并且从新更新
	 * @param goodsId
	 * @return
	 */
	public int deleteGoodsEquipmentLabel(int goodsId);
}

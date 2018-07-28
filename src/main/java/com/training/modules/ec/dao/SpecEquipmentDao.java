package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Equipment;
import com.training.modules.ec.entity.EquipmentLogs;
import com.training.modules.ec.entity.ShopComEquipment;

/**
 * 设备dao
 * @author 小叶  2016-12-28 
 *
 */
@MyBatisDao
public interface SpecEquipmentDao extends CrudDao<Equipment> {
	
	/**
	 * 查询出所有的特殊设备
	 */
	public List<Equipment> findAllList();
	
	/**
	 * 查询出所有的通用设备
	 */
	public List<Equipment> newFindAllList();
	
	/**
	 * 新增设备
	 * @param equipment
	 * @return
	 */
	public int insertSpecEquipment(Equipment equipment);
	
	/**
	 * 修改设备
	 * @param equipment
	 * @return
	 */
	public int updateSpecEquipment(Equipment equipment);
	
	/**
	 * 逻辑删除设备 
	 * @param equipment
	 * @return
	 */
	public int deleteSpecEquipment(Equipment equipment);
	
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	public List<ShopComEquipment> findShopComEquipmentList(ShopComEquipment shopComEquipment);
	
	/**
	 * 新增店铺的通用设备
	 * @param shopComEquipment
	 * @return
	 */
	public int saveshopComEquipment(ShopComEquipment shopComEquipment);
	
	/**
	 * 查询某店铺的某通用设备的数量
	 * @return
	 */
	public int querySum(@Param(value="shopId")String shopId, @Param(value="equipmentId")int equipmentId);
	
	/**
	 * 修改店铺通用设备状态
	 * @param isEnabled
	 * @param shopComEquipmentId
	 * @return
	 */
	public int updateType(@Param(value="isEnabled")int isEnabled, @Param(value="shopComEquipmentId")String shopComEquipmentId);
	
	/**
	 * 查找店铺所属市场对应的通用设备
	 * @return
	 */
	public List<Equipment> findForCom(String shopId);
	
	/**
	 * 查询某市场里的某设备数量
	 * @return
	 */
	public int queryNum(@Param(value="bazaarId")String bazaarId, @Param(value="labelId")int labelId);
	
	/**
	 * 分页查询查询设备排班详情
	 * @param equipmentLogs
	 * @return
	 */
	public List<EquipmentLogs> findEquipmentLogsList(EquipmentLogs equipmentLogs);
	
	/**
	 * 新增店铺通用设备操作日志
	 * @param equipmentLogs
	 */
	public void insertComEquipmentLog(EquipmentLogs equipmentLogs);
	
	/**
	 * 查看店铺通用设备操作日志
	 * @param officeId
	 * @return
	 */
	public List<EquipmentLogs> findAllLogs(EquipmentLogs equipmentLogs);

	//删除与新增设备名称相同的数据
	public void deleteSpecEquipmentByName(Equipment equipment);

}

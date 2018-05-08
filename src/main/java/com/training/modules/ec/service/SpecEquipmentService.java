package com.training.modules.ec.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.ec.dao.SpecEquipmentDao;
import com.training.modules.ec.entity.Equipment;
import com.training.modules.ec.entity.EquipmentLogs;
import com.training.modules.ec.entity.ShopComEquipment;
import com.training.modules.ec.utils.CommonScopeUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 设备Service
 * @author 小叶   2016-12-29 
 *
 */
@Service
@Transactional(readOnly = false)
public class SpecEquipmentService extends CrudService<SpecEquipmentDao, Equipment>{

	@Autowired 
	private SpecEquipmentDao specEquipmentDao;
	

	/**
	 * 查询出所有的特殊设备
	 */
	public List<Equipment> findAllList(){
		return specEquipmentDao.findAllList();
	}
	
	/**
	 * 查询出所有的通用设备
	 */
	public List<Equipment> newFindAllList(){
		return specEquipmentDao.newFindAllList();
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param equipment
	 * @return
	 */
	public Page<Equipment> findList(Page<Equipment> page, Equipment equipment) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		equipment.getSqlMap().put("dsf", CommonScopeUtils.dataScopeFilter("e"));
		
		// 设置分页参数
		equipment.setPage(page);
		// 执行分页查询
		page.setList(specEquipmentDao.findList(equipment));
		return page;
	}
	
	/**
	 * 新增修改设备
	 * @param equipment
	 */
	public void saveSpecEquipment(Equipment equipment){
		User user = UserUtils.getUser();
		if(equipment.getEquipmentId() != 0){
			specEquipmentDao.updateSpecEquipment(equipment);
		}else{
			//删除与新增设备名称相同的数据，新增的时候设置office_id为1（平台的id）
			specEquipmentDao.deleteSpecEquipmentByName(equipment);
			//获取当前用户的office的id
			equipment.setOfficeId("1");
			equipment.setCreateBy(user);
			specEquipmentDao.insertSpecEquipment(equipment);
		}
	}
	/**
	 * 逻辑删除设备 
	 * @param equipment
	 * @return
	 */
	public void deleteSpecEquipment(Equipment equipment){
		specEquipmentDao.deleteSpecEquipment(equipment);
	}
	
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param shopComEquipment
	 * @return
	 */
	public Page<ShopComEquipment> findShopComEquipmentList(Page<ShopComEquipment> page, ShopComEquipment shopComEquipment) {
		// 设置分页参数
		shopComEquipment.setPage(page);
		// 执行分页查询
		page.setList(specEquipmentDao.findShopComEquipmentList(shopComEquipment));
		return page;
	}
	
	/**
	 * 新增店铺的通用设备
	 * @param shopComEquipment
	 */
	public void saveshopComEquipment(ShopComEquipment shopComEquipment){
		shopComEquipment.setShopComEquipmentId(IdGen.uuid());
		User user = UserUtils.getUser();
		shopComEquipment.setCreateBy(user);
		specEquipmentDao.saveshopComEquipment(shopComEquipment);
	}
	
	
	/**
	 * 查询某店铺的某通用设备的数量
	 * @return
	 */
	public int querySum(String shopId, int equipmentId){
		return specEquipmentDao.querySum(shopId, equipmentId);
	}
	
	/**
	 * 修改店铺通用设备状态
	 * @param isEnabled
	 * @param shopComEquipmentId
	 * @return
	 */
	public void updateType(int isEnabled, String shopComEquipmentId){
		specEquipmentDao.updateType(isEnabled, shopComEquipmentId);
	}
	
	/**
	 * 查找店铺所属市场对应的通用设备
	 * @return
	 */
	public List<Equipment> findForCom(String shopId){
		return specEquipmentDao.findForCom(shopId);
	}
	
	/**
	 * 查询某市场里的某设备数量
	 * @return
	 */
	public int queryNum(@Param(value="bazaarId")String bazaarId, @Param(value="labelId")int labelId){
		return specEquipmentDao.queryNum(bazaarId, labelId);
	}
	
	
	/**
	 * 分页查询查询设备排班详情
	 * @param page
	 * @param equipment
	 * @return
	 */
	public Page<EquipmentLogs> findEquipmentLogsList(Page<EquipmentLogs> page, EquipmentLogs equipmentLogs) {
		// 设置分页参数
		equipmentLogs.setPage(page);
		// 执行分页查询
		page.setList(specEquipmentDao.findEquipmentLogsList(equipmentLogs));
		return page;
	}
}

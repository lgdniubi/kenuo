package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.EquipmentLabelDao;
import com.training.modules.ec.entity.EquipmentLabel;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 设备标签Servie
 * @author 小叶   2017-1-10 
 *
 */
@Service
@Transactional(readOnly = false)
public class EquipmentLabelService extends CrudService<EquipmentLabelDao, EquipmentLabel>{
	
	@Autowired
	private EquipmentLabelDao equipmentLabelDao;
	
	/**
	 * 查询设备标签
	 */
	public List<EquipmentLabel> findAllList(String newFlag){
		return equipmentLabelDao.findAllList(newFlag);
	}
	
	/**
	 * 查询所有的设备标签
	 */
	public List<EquipmentLabel> newFindAllList(){
		return equipmentLabelDao.newFindAllList();
	}
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param skill
	 * @return
	 */
	public Page<EquipmentLabel> findList(Page<EquipmentLabel> page, EquipmentLabel equipmentLabel) {
		// 设置分页参数
		equipmentLabel.setPage(page);
		// 执行分页查询
		page.setList(equipmentLabelDao.findList(equipmentLabel));
		return page;
	}
	
	/**
	 * 新增编辑设备标签
	 * @param 
	 */
	public void saveEquipmentLabel(EquipmentLabel equipmentLabel){
		User user = UserUtils.getUser();
		if(equipmentLabel.getEquipmentLabelId() != 0){
			equipmentLabel.setUpdateBy(user);
			equipmentLabelDao.updateEquipmentLabel(equipmentLabel);
		}else{
			equipmentLabel.setCreateBy(user);
			equipmentLabelDao.insertEquipmentLabel(equipmentLabel);
		}
	}
	
	/**
	 * 逻辑删除设备标签
	 * @param 
	 * @return
	 */
	public void deleteEquipmentLabel(EquipmentLabel equipmentLabel){
		equipmentLabelDao.deleteEquipmentLabel(equipmentLabel);
	}
	
	/**
	 * 验证设备编号
	 * 
	 * @param no
	 * @return
	 */
	public int getEquipmentLabelByNO(String no) {
		return equipmentLabelDao.getByNO(no);
	}
	
	/**
	 * 验证设备名称
	 * 
	 * @param name
	 * @return
	 */
	public int getEquipmentLabelByName(String name) {
		return equipmentLabelDao.getByName(name);
	}
	
	/**
	 * 设备标签是否显示
	 */
	public void updateIsShow(EquipmentLabel equipmentLabel){
		equipmentLabelDao.updateIsShow(equipmentLabel);
	}
}

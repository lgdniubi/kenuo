/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.ProtocolModelDao;
import com.training.modules.train.entity.ProtocolModel;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.ProtocolUser;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = false)
public class ProtocolModelService extends CrudService<ProtocolModelDao,ProtocolModel> {

	@Autowired
	private ProtocolModelDao protocolModelDao;

	/**
	 * 查找协议类型列表
	 * @return
	 */
	public List<ProtocolType> findModelTypeList() {
		return dao.findModelTypeList();
	}

	
	/**
	 * 保存协议模板内容
	 * @param protocolModel
	 */
	public void saveProtocolModel(ProtocolModel protocolModel) {
		if(protocolModel.getIsNewRecord()){
			protocolModel.preInsert();
			protocolModel.setStatus("1");
			dao.insert(protocolModel);
			//新增协议的同事修改供应链那边签约状态为变更状态。
			
		}else{
			ProtocolModel findModel = protocolModelDao.findModel(protocolModel);
			boolean flag = isChanged(findModel,protocolModel);
			if(!flag){//如果内容或标题改变
				findModel.preInsert();
				findModel.setStatus("2");
				findModel.setPid(findModel.getId());
				dao.insert(findModel);//把原来的内容复制一份
				protocolModelDao.updateModelById(protocolModel);//把原来更新了
				if(protocolModel.getAssign()){	//是否重新签订--重新就更新原来的状态为变更
					//更改店铺-协议表该协议id的状态为变更
					protocolModel.setStatus("2");//店铺协议状态为变更
					protocolModelDao.updateProtocolShopById(protocolModel);//把原来更新了
					//更改签约状态变更
					
				}
			}
		}
	}

	/**
	 * 判断协议内容是否改变
	 * @param findModel
	 * @param protocolModel
	 * @return
	 */
	private boolean isChanged(ProtocolModel findModel, ProtocolModel protocolModel) {
		return findModel.getName().equals(protocolModel.getName()) && findModel.getContent().equals(protocolModel.getContent());
	}


	/**
	 * 根据模板类型查找列表
	 * @param type 
	 * @return
	 */
	public List<ProtocolModel> findModelList(String type) {
		return protocolModelDao.findModelList(type);
	}


	/**
	 * 根据id查找协议内容
	 * @param protocolModel
	 * @return
	 */
	public ProtocolModel findModel(ProtocolModel protocolModel) {
		return protocolModelDao.findModel(protocolModel);
	}

	/**
	 * 更改单个协议状态启用--停用
	 * @param protocolModel
	 */
	public void updateStatus(ProtocolModel protocolModel) {
		protocolModelDao.updateStatusById(protocolModel);
	}
	/**
	 * 更改模板状态
	 * @param protocolModel
	 */
	public void updateStatusType(ProtocolModel protocolModel) {
		protocolModelDao.updateStatusByType(protocolModel);
		protocolModelDao.updateModelTypeStatus(protocolModel);
		
	}


	
	/**
	 * 用户--协议表
	 * @param protocolUser 
	 * @return
	 */
	public List<ProtocolUser> findProtocolList(ProtocolUser protocolUser) {
		return protocolModelDao.findProtocolList(protocolUser);
	}

	/**
	 * 查询机构已签协议
	 * @param office_id
	 * @return
	 */
	public List<ProtocolUser> findProtocolListOfOffice(String office_id){
		return this.protocolModelDao.findProtocolListOfOffice(office_id);
	}
	
}
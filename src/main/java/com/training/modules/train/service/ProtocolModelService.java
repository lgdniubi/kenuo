/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.service;

import java.util.List;

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
		protocolModel.preInsert();
		if(protocolModel.getAssign()){	//是否重新签订--重新就更新原来的状态为变更
			protocolModel.setStatus("3");
			protocolModelDao.updateStatusById(protocolModel);
		}
		protocolModel.setStatus("1");
		dao.insert(protocolModel);
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
	 * @return
	 */
	public List<ProtocolUser> findProtocolList() {
		return protocolModelDao.findProtocolList();
	}


	
}
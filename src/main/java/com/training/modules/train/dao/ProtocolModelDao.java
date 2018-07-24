/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ProtocolModel;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.ProtocolUser;

/**
 * 菜单DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface ProtocolModelDao extends CrudDao<ProtocolModel> {

	public List<ProtocolType> findModelTypeList();

	public List<ProtocolModel> findModelList(String type);

	public ProtocolModel findModel(ProtocolModel protocolModel);
	//是否重新签订--重新就更新原来的状态为变更
	public void updateStatusById(ProtocolModel protocolModel);

	public void updateStatusByType(ProtocolModel protocolModel);

	//更改模板类型状态
	public void updateModelTypeStatus(ProtocolModel protocolModel);

	
	/**
	 * 用户--协议表
	 * @param protocolUser 
	 * @return
	 */
	public List<ProtocolUser> findProtocolList(ProtocolUser protocolUser);

	public void updateModelById(ProtocolModel protocolModel);
	/**
	 * 更改supply_protocol_shop表所有签过该协议的状态为变更
	 * @param protocolModel
	 */
	public void updateProtocolShopById(ProtocolModel protocolModel);

	/*public List<MediaMenu> findByParentIdsLike(MediaMenu menu);

	public List<MediaMenu> findByUserId(MediaMenu menu);
	
	public int updateParentIds(MediaMenu menu);
	
	public int updateSort(MediaMenu menu);
	
	public List<MediaMenu> findByPidforChild(String id);

	public List<MediaMenu> findAllMenuByModid(MediaRole mediaRole);

	public int checkName(@Param("name")String name,@Param("parentId")Integer parentId);*/
	/**
	 * 查询机构已签协议
	 * @param office_id
	 * @return
	 */
	public List<ProtocolUser> findProtocolListOfOffice(@Param("office_id")String office_id);

	public void deleteProtocolShopById(@Param("id")Integer id);
	//查询旧版本集合
	public List<ProtocolModel> findOldModelList(String id);

	public void deleteProtocolShop(String typeId);
	
	public void deleteProtocolShopOfOffice(@Param("office_id")String office_id);
	
}

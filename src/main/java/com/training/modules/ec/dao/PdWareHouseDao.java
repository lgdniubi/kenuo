package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.PdWareHouse;
import com.training.modules.sys.entity.Area;
/**
 * 仓库dao
 * @author dalong
 *
 */

@MyBatisDao
public interface PdWareHouseDao extends TreeDao<PdWareHouse> {

	/**
	 * 添加
	 * @param courierHouse
	 */
	void insertWareHouse(PdWareHouse wareHouse);

	/**
	 * 修改
	 * @param courierHouse
	 */
	void updateWareHouse(PdWareHouse wareHouse);

	/**
	 * 逻辑删除仓库
	 * @param courierHouse
	 */
	void updateWareHouseDelFlag(PdWareHouse courierHouse);

	/**
	 * 获取区域
	 * @param areaId
	 * @return
	 */
	Area getAreaById(@Param("areaId")String areaId);

	/**
	 * 通过ids获取所有满足条件的区域名称
	 * @param areaids
	 * @return
	 */
	List<Area> getAreaByIds(List<String> areaids);


}

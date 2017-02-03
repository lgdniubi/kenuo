package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.sys.entity.Office;
/**
 * 预约dao
 * @author coffee
 *
 */
@MyBatisDao
public interface ReservationDao extends CrudDao<Reservation>{
	/**
	 * 修改预约详情
	 * @param reservation
	 */
	public void updateMnappointment(Reservation reservation);
	/**
	 * 修改美容师时验证此美容师下有无预约
	 * @param id
	 * @return
	 */
	public int findCountById(String id);
	/**
	 * 加载可用店铺
	 * @param reservation
	 * @return
	 */
	public List<Office> loadOffice(@Param(value="goodsId")int goodsId,@Param(value="areaName")String areaName);
}

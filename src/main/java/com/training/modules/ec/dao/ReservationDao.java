package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.ReservationLog;
import com.training.modules.sys.entity.User;
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
	 * 加载所有美容师
	 * @param reservation
	 * @return
	 */
	public List<User> loadBeauty(Reservation reservation);
	/**
	 * 保存预约日志
	 * @param reservationLog
	 */
	public void saveLog(ReservationLog reservationLog);
	/**
	 * 查询前两条预约日志
	 * @param reservationLog
	 * @return
	 */
	public List<ReservationLog> findLog(ReservationLog reservationLog);
	/**
	 * 分页查询预约日志
	 * @param reservationLog
	 * @return
	 */
	public List<ReservationLog> findListLog(ReservationLog reservationLog);
	/**
	 * 修改美容师时验证此美容师下有无预约
	 * @param id
	 * @return
	 */
	public int findCountById(String id);
}

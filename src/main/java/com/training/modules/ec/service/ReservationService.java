package com.training.modules.ec.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.ReservationLog;
import com.training.modules.sys.entity.User;


/**
 * 预约service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ReservationService extends CrudService<ReservationDao,Reservation>{
	/**
	 * 查看所有预约
	 */
	public Page<Reservation> findPage(Page<Reservation> page,Reservation reservation){
		reservation.setPage(page);
		page.setList(dao.findAllList(reservation));
		return page;
	}
	/**
	 * 查询单个服务详情
	 * @param reservation
	 * @return
	 */
	public Reservation oneMnappointment(Reservation reservation){
		return dao.get(reservation);
	}
	/**
	 * 修改预约详情
	 * @param reservation
	 */
	public void updateMnappointment(Reservation reservation){
		dao.updateMnappointment(reservation);
	}
	/**
	 * 加载所有美容师
	 * @return
	 */
	public List<User> loadBeauty(Reservation reservation){
		return dao.loadBeauty(reservation);
	}
	/**
	 * 保存预约日志
	 * @param reservationLog
	 */
	public void saveLog(ReservationLog reservationLog){
		dao.saveLog(reservationLog);
	}
	/**
	 * 查询日志详情前两条
	 * @param reservationLog
	 * @return
	 */
	public List<ReservationLog> findLog(ReservationLog reservationLog){
		return dao.findLog(reservationLog);
	}
	/**
	 * 分页查询预约日志查询
	 * @param reservationLog
	 * @return
	 */
	public  Page<ReservationLog> findListLog(Page<ReservationLog> page,ReservationLog reservationLog){
		reservationLog.setPage(page);
		page.setList(dao.findListLog(reservationLog));
		return page;
	}
}

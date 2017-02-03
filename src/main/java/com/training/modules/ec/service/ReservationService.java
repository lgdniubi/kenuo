package com.training.modules.ec.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.sys.entity.Office;


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
	 * 加载美容师
	 * @return
	 */
	public List<Office> loadOffice(int goodsId,String areaName){
		return dao.loadOffice(goodsId,areaName);
	}
}

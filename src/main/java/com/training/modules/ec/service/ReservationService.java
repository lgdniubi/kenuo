package com.training.modules.ec.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.sys.dao.AreaDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;


/**
 * 预约service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ReservationService extends CrudService<ReservationDao,Reservation>{
	
	@Autowired
	private AreaDao areaDao;
	
	
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
	public List<Office> loadOffice(int goodsId,String areaId){
		String nationName = "";
		String provinceId = "";
		String cityId = "";
		String districtId = "";
		Area area = areaDao.get(areaId);
		if("1".equals(area.getType())){
			nationName = area.getName();
		}else if("2".equals(area.getType())){
			provinceId = area.getId();
		}else if("3".equals(area.getType())){
			cityId = area.getId();
		}else if("4".equals(area.getType())){
			districtId = area.getId();
		}
		return dao.loadOffice(goodsId,nationName,provinceId,cityId,districtId);
	}
}

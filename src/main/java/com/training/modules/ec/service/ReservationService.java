package com.training.modules.ec.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.utils.CommonScopeUtils;
import com.training.modules.sys.dao.AreaDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;
import com.training.modules.train.entity.Subscribe;


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
	 * 查看预约相关的用户评论
	 */
	public List<Comment> findReservationComment(String reservationId){
		return dao.findReservationComment(reservationId);
	}
	
	
	
	/**
	 * 查看所有预约
	 */
	public Page<Reservation> findPage(Page<Reservation> page,Reservation reservation){
		if(reservation.getBeginDate() == null && reservation.getEndDate() == null){
			reservation.setBeginDate(new Date());
			reservation.setEndDate(new Date());
		}
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		reservation.getSqlMap().put("dsf", CommonScopeUtils.newDataScopeFilter("r"));
		reservation.setPage(page);
		page.setList(dao.findAllList(reservation));
		return page;
	}

	/**
	 * 查看单个用户所有预约
	 */
	public Page<Reservation> findUserPage(Page<Reservation> page,Reservation reservation){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		reservation.getSqlMap().put("dsf", CommonScopeUtils.newDataScopeFilter("r"));
		reservation.setPage(page);
		page.setList(dao.findUserPage(reservation));
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
	public List<Office> loadOffice(String goodsIds,String franchiseeId,String areaId){
		String nationName = "";
		String provinceId = "";
		String cityId = "";
		String districtId = "";
		Area area = areaDao.get(areaId);
		if(area != null){
			if("1".equals(area.getType())){
				nationName = area.getName();
			}else if("2".equals(area.getType())){
				provinceId = area.getId();
			}else if("3".equals(area.getType())){
				cityId = area.getId();
			}else if("4".equals(area.getType())){
				districtId = area.getId();
			}
		}
		return dao.loadOffice(goodsIds,franchiseeId,nationName,provinceId,cityId,districtId);
	}
	
	/**
	 * 查询用户可服务的订单
	 * @return
	 */
	public List<OrderGoods> findOrderGoodsByUserId(int userId){
		return dao.findOrderGoodsByUserId(userId);
	}

	/**
	 * 查询预约的实际服务时间
	 * @param reservation
	 * @return
	 */
	public Reservation getServiceTime(Reservation reservation) {
		return dao.getServiceTime(reservation);
	}

	/**
	 * 添加/修改  实际服务时长
	 * @param reservation
	 */
	public void editServiceTime(Reservation reservation) {
		dao.editServiceTime(reservation);
	}
	
	/**
	 * 获取预约时间服务已过没有完成状态的预约
	 * @return
	 */
	public List<Subscribe> querySubscribelist() {
		return dao.querySubscribelist();
	}
	
	/**
	 * 修改预约状态
	 * @param appt_id
	 * @return
	 */
	public int updateapptstatus(int appt_id) {
		return dao.updateapptstatus(appt_id);
	}
}

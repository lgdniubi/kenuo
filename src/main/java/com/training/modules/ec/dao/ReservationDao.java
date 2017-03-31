package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Comment;
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
	public List<Office> loadOffice(@Param(value="goodsId")int goodsId,@Param(value="nationName")String nationName,@Param(value="provinceId")String provinceId,@Param(value="cityId")String cityId,@Param(value="districtId")String districtId);       
    /**
     * @param 
     * @return List<Reservation>
     * @author sharp
     * 找到单个用户的预约单
     */
    public List<Reservation> findUserPage(Reservation reservation);
   
    /**
     * 查询单个预约下的评论
     * @author shiba
     * @param 预约Id
     * @return List<Comment>
     */
    public List<Comment> findReservationComment(String reservationId);
	
}

package com.training.modules.ec.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.ReservationLog;
import com.training.modules.sys.entity.Office;
import com.training.modules.train.entity.Subscribe;
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
	public List<Office> loadOffice(@Param(value="goodsIds")String goodsIds,@Param(value="franchiseeId")String franchiseeId,@Param(value="nationName")String nationName,@Param(value="provinceId")String provinceId,@Param(value="cityId")String cityId,@Param(value="districtId")String districtId);       
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
	
	/**
	 * 隐藏店铺时  验证店铺下是否有预约
	 * @param id
	 * @return
	 */
	public int findCountByOfficeId(String id);
	
	/**
	 * 查询用户可服务的订单
	 * @return
	 */
	public List<OrderGoods> findOrderGoodsByUserId(int userId);
	/**
	 * 查询预约的实际服务时间
	 * @param reservation
	 * @return
	 */
	public Reservation getServiceTime(Reservation reservation);
	/**
	 * 添加/修改  实际服务时长
	 * @param reservation
	 */
	public void editServiceTime(Reservation reservation);
	
	/**
	 * 获取预约时间服务已过没有完成状态的预约
	 * @return
	 */
	public List<Subscribe> querySubscribelist();

	/**
	 * 修改预约状态
	 * @param appt_id
	 * @return
	 */
	public int updateapptstatus(@Param("appt_id")int appt_id);
	
	/**
	 * 保存修改预约日志
	 * @param apptOrderLog
	 */
	public void saveApptOrderLog(ReservationLog reservationLog);
	/**
	 * 查询预约操作日志
	 * @param appt_id
	 * @return
	 */
	public List<ReservationLog> findReservationLog(ReservationLog reservationLog);
	
	/**
	 * 查询一定时间段内已完成、爽约的预约，用于给店铺分预约金和补偿金 
	 * @param lastTime
	 * @return
	 */
	public List<Reservation> queryApptOrderForAdvancePrice(@Param(value="lastTime")Date lastTime,@Param(value="startTime")Date startTime);
	
	/**
	 * 查询某个订单下的预约完成与爽约的总数
	 * @param groupId
	 * @param goodsMappingId
	 * @return
	 */
	public int queryCompleteNum(@Param(value="groupId")int groupId,@Param(value="goodsMappingId")String goodsMappingId,@Param(value="apptId")int apptId);
	
	/**
	 * 校验同一美容师同一时间段内只能有一条已完成的预约记录
	 * @param reservation
	 */
	public int verifyApptDate(Reservation reservation);
}

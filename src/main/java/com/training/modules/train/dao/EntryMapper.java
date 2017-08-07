package com.training.modules.train.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.EntrySopCastBean;
import com.training.modules.train.entity.LiveRoomidAndAuditid;



@MyBatisDao
public interface EntryMapper {



	public void upauditstatus(@Param("value")int value);

	public int querylive_outTime(@Param("live_outTime")String live_outTime);

	public void uplivestatus(@Param("value")int open_live_expiration_time,@Param("auditid") int auditid);

	public List<LiveRoomidAndAuditid> queryRoomidandAuditid(@Param("times")int times);

	public int queryCount(@Param("value")int open_live_expiration_time,@Param("auditid") int auditid);

	public EntrySopCastBean getliveendbyid(@Param("value") int auditids);

	public void alterstatus(@Param("value") int auditids);

	public void LiveEnd(Map<String, Object> m);

	public void liveendtime(@Param("value") int auditids);

	public void operatebackrecord();

	public void SyncIntegrals(@Param("mtmy_id")String mtmy_id, @Param("integral")String integral);

	public void updateauditstatus(@Param("auditid") int auditid);

	/**
	 * 查看云币余额
	 * @param send_mtmy_id
	 * @return
	 */
	public int queryintegralsnum(@Param("send_mtmy_id")int send_mtmy_id);

	/**
	 * 添加云币交易日志
	 * @param user_id
	 * @return
	 */
	public int cloudcoinOrderlog(Map<String, Object> m);

	/**
	 * 将平台获得云币加到平台账户
	 * @param map
	 */
	public void addofficeaccount(Map<String, Object> m);

	/**
	 * 将云币划分结果信息入库
	 * @param m
	 */
	public void addproportionsmessage(Map<String, Object> m);

	


	
}

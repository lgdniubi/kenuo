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

	public List<LiveRoomidAndAuditid> queryRoomidandAuditid();

	public int queryCount(@Param("value")int open_live_expiration_time,@Param("auditid") int auditid);

	public EntrySopCastBean getliveendbyid(@Param("value") int auditids);

	public void alterstatus(@Param("value") int auditids);

	public void LiveEnd(Map<String, Object> m);

	public void liveendtime(@Param("value") int auditids);

	public void operatebackrecord();

	public void SyncIntegrals(@Param("mtmy_id")String mtmy_id, @Param("integral")String integral);

	public void updateauditstatus(@Param("auditid") int auditid);

	


	
}

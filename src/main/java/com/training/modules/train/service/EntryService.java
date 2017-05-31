package com.training.modules.train.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.training.modules.train.dao.EntryMapper;
import com.training.modules.train.entity.EntrySopCastBean;
import com.training.modules.train.entity.LiveRoomidAndAuditid;
import com.training.modules.train.entity.rooms;
import com.training.modules.train.utils.EncryptLiveUtils;


@Service
@SuppressWarnings("all")
public class EntryService {
	@Autowired
	private EntryMapper entrymapper;

		
	//检测申请直播表里当前过期的数据
	public int querylive_outTime(String value){
		 return entrymapper.querylive_outTime(value);
	}
	
	public int querylive_expiration_time(String value){
		return entrymapper.querylive_outTime(value);
	}
	
	public void upauditstatus(int value){
		entrymapper.upauditstatus(value);
	}
	
	public List<LiveRoomidAndAuditid> queryRoomidandAuditid(){
		return entrymapper.queryRoomidandAuditid();
	}
	
	public void uplivestatus(int value1,int value2){
		entrymapper.uplivestatus(value1,value2);
	}

	public int queryCount(int value1,int value2) {
		return entrymapper.queryCount(value1,value2);
	}

	public EntrySopCastBean getliveendbyid(int auditids) {
		return entrymapper.getliveendbyid(auditids);
	}


	public void alterstatus(int auditids) {
		entrymapper.alterstatus(auditids);
	}

	public void LiveEnd(Map<String, Object> m) {
		entrymapper.LiveEnd(m);
	}

	public void liveendtime(int auditids) {
		entrymapper.liveendtime(auditids);
	}

	public void operatebackrecord() {
		entrymapper.operatebackrecord();
	}

	/**
	 * 同步云币
	 * @param mtmy_id
	 * @param integral
	 */
	public void SyncIntegrals(String mtmy_id, String integral) {
		entrymapper.SyncIntegrals(mtmy_id,integral);
		
	}

	/**
	 * 修改直播状态
	 * @param auditid
	 */
	public void updateauditstatus(int auditid) {
		entrymapper.updateauditstatus(auditid);
	}

	

}

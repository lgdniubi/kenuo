package com.mtmy.common.cachemanager.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneDayTime implements Serializable {

	private static final long serialVersionUID = -5039718167733306794L;
	private Map<String, Integer> index = new HashMap<String, Integer>();
	private String key = ""; // 2016-06-28 星期一
	private List<Time> times; //时间列表
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Time> getTimes() {
		return times;
	}
	public void setTimes(List<Time> times) {
		this.times = times;
	}
	public Map<String, Integer> getIndex() {
		return index;
	}
	public void setIndex(Map<String, Integer> index) {
		this.index = index;
	}

}

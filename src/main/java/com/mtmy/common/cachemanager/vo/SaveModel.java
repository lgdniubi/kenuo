package com.mtmy.common.cachemanager.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SaveModel implements Serializable {

	private static final long serialVersionUID = 4769717915034658980L;

	private Map<String, Integer> index = new HashMap<String, Integer>();
	
	private LinkedList<OneDayTime> odts = new LinkedList<OneDayTime>();

	public Map<String, Integer> getIndex() {
		return index;
	}

	public void setIndex(Map<String, Integer> index) {
		this.index = index;
	}

	public LinkedList<OneDayTime> getOdts() {
		return odts;
	}

	public void setOdts(LinkedList<OneDayTime> odts) {
		this.odts = odts;
	}

	

	
}

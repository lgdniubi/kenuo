package com.mtmy.common.cachemanager.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * 预约时间对象
 * @author QJL
 *
 */
public class Time implements Serializable{
	
	private static final long serialVersionUID = 1770129122448154161L;
	private String name="";//时间点
	private int is_use = 0;//是否被占用
	private Date time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIs_use() {
		return is_use;
	}
	public void setIs_use(int is_use) {
		this.is_use = is_use;
	}
	@Override
	public int hashCode() {
		
		return this.name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		Time t = (Time) obj;
		return t.name.equals(this.name);
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}

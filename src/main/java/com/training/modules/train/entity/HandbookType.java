package com.training.modules.train.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.IdGen;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 
 * @author: jingfeng
 * @date 2018年5月2日下午3:18:53
 * @Description:供应链--协议模板
 */
public class HandbookType extends DataEntity<HandbookType>{
	
	private static final long serialVersionUID = 1L;
	private String name;			//手册类型名称
	private Integer sort;			//排序
	private String iconUrl;			//图标链接地址
	private String type = "0";			//类型，1妃子校，2每天美耶，3pc端
	private String isShop = "0";	//PC端分类，0管理端，1店铺端
	
	public HandbookType() {
		super();
		this.sort = 30;
	}
	public HandbookType(String id) {
		super(id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsShop() {
		return isShop;
	}
	public void setIsShop(String isShop) {
		this.isShop = isShop;
	}
	
}

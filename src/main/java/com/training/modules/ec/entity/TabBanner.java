package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;


/**
 * tab_banner实体类
 * @author 小叶
 *
 */
public class TabBanner extends DataEntity<TabBanner>{

	private static final long serialVersionUID = 1L;
	private int tabBannerId;         //tab_bannner图id 
	private String tabName;	         //内容名称
	private String tabIcon;		 //未选中样式图标(图片)
	private String tabSelectIcon;  //选中样式图标(图片)
	private String newSort;		     //排序
	private int tabId;               //tab_banneer对应的背景图的Id
	
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	
	public String getTabIcon() {
		return tabIcon;
	}
	public void setTabIcon(String tabIcon) {
		this.tabIcon = tabIcon;
	}
	public String getTabSelectIcon() {
		return tabSelectIcon;
	}
	public void setTabSelectIcon(String tabSelectIcon) {
		this.tabSelectIcon = tabSelectIcon;
	}
	public int getTabId() {
		return tabId;
	}
	public void setTabId(int tabId) {
		this.tabId = tabId;
	}
	public int getTabBannerId() {
		return tabBannerId;
	}
	public void setTabBannerId(int tabBannerId) {
		this.tabBannerId = tabBannerId;
	}

	public String getNewSort() {
		return newSort;
	}
	public void setNewSort(String newSort) {
		this.newSort = newSort;
	}
	
	
	
}

package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;


/**
 * 背景图实体类
 * @author 小叶   2016.12.8
 *
 */
public class TabBackground extends TreeEntity<TabBackground>{
	

	private static final long serialVersionUID = 3594323532751319064L;
	private int tabBackgroundId;          //背景图id  
	private String groupName;             //标签组名称
	private String tabBg;                 //背景图(图片)
	private String topStyle;             //顶部样式名称(文本样式)
	private String tabNameStyle;          //样式名称(文本样式)
	private String tabNameColor;          //未选中样式颜色(文本样式)
	private String tabNameSelectColor;    //选中样式颜色(文本样式)
	private int isShow;                   //是否显示
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTabBg() {
		return tabBg;
	}
	public void setTabBg(String tabBg) {
		this.tabBg = tabBg;
	}
	public String getTabNameStyle() {
		return tabNameStyle;
	}
	public void setTabNameStyle(String tabNameStyle) {
		this.tabNameStyle = tabNameStyle;
	}
	public String getTabNameColor() {
		return tabNameColor;
	}
	public void setTabNameColor(String tabNameColor) {
		this.tabNameColor = tabNameColor;
	}
	public String getTabNameSelectColor() {
		return tabNameSelectColor;
	}
	public void setTabNameSelectColor(String tabNameSelectColor) {
		this.tabNameSelectColor = tabNameSelectColor;
	}
	@Override
	public TabBackground getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(TabBackground parent) {
		// TODO Auto-generated method stub
		
	}
	public String getTopStyle() {
		return topStyle;
	}
	public void setTopStyle(String topStyle) {
		this.topStyle = topStyle;
	}
	public int getTabBackgroundId() {
		return tabBackgroundId;
	}
	public void setTabBackgroundId(int tabBackgroundId) {
		this.tabBackgroundId = tabBackgroundId;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
	
	
	
}

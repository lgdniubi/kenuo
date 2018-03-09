package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 热门主题实体类
 * @author xiaoye  2017年9月13日
 *
 */
public class Theme extends DataEntity<Theme>{

	private static final long serialVersionUID = 1L;
	private int themeId;                      //主键ID
	private String name;                      //主题名称
	private String primaryName;               //主标题
	private String deputyName;                //副标题
	private String content;                   //文字描述
	private String img;                       //头图
	private int sort;                         //排序(数字越大越靠前)
	private int isRecommend;                  //是否推荐(0:否 1:是)
	private int isShow;                      //是否显示（0：否；1：是）
	private int delflag;                      //删除标识（0：正常；1：删除；）
	private String flag;                     //标识
	
	private String isOpen;                  //是否公开(0：公开；不公开则保存相应的商家id，以,拼接)
	
	public int getThemeId() {
		return themeId;
	}
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimaryName() {
		return primaryName;
	}
	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}
	public String getDeputyName() {
		return deputyName;
	}
	public void setDeputyName(String deputyName) {
		this.deputyName = deputyName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
}

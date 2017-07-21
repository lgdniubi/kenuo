package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 商品副标题实体类
 * @author xiaoye
 *
 */
public class GoodsSubhead extends DataEntity<GoodsSubhead>{

	private static final long serialVersionUID = 1L;
	
	private int goodsSubheadId;    //主键id
	private String name;           //副标题名称（活动名称，用于后台管理）
	private String subheading;     //副标题文案（前端展示的副标题文字）
	private String type;          //类型（1、动态，2、静态）
	private String redirectUrl;   //链接地址
	private String status;         //活动状态(0 开启 1 关闭)
	private String img;           //头图(用于类型为动态时)
	private Date startDate;       //生效时间
	private Date endDate;        //失效时间
	private int delflag;         //删除标识[0-正常;1-删除;]
	
	private String flag;        //标识，用来判断查看，修改，添加
	private String openStatus;    //用来进行开启状态的判断的
	
	public int getGoodsSubheadId() {
		return goodsSubheadId;
	}
	public void setGoodsSubheadId(int goodsSubheadId) {
		this.goodsSubheadId = goodsSubheadId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubheading() {
		return subheading;
	}
	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	
}

package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 商品评论导出实体类
 * @author 
 *
 */
public class MtmyComment extends DataEntity<MtmyComment>{

	private static final long serialVersionUID = 1L;
	
	private String name;   //名称/昵称
	private String goodsId;  //商品id
	private String goodsName;  //商品名称
	private String addTime;    //评论时间
	private String goodsRank;   //评论星级
	private String contents;    //评论内容
	private String specKeyName;  //规格项
	private String barCode;      //条形码
	private String goodsNo;      //商品编号
	
	@ExcelField(title="名称/昵称", align=2, sort=5)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="商品id", align=2, sort=10)
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	@ExcelField(title="商品名称", align=2, sort=15)
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	@ExcelField(title="评论时间", align=2, sort=20)
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	@ExcelField(title="评论星级", align=2, sort=25)
	public String getGoodsRank() {
		return goodsRank;
	}
	public void setGoodsRank(String goodsRank) {
		this.goodsRank = goodsRank;
	}
	@ExcelField(title="评论内容", align=2, sort=30)
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	@ExcelField(title="规格项", align=2, sort=35)
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	@ExcelField(title="条形码", align=2, sort=40)
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	@ExcelField(title="商品编号", align=2, sort=45)
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	
	

}

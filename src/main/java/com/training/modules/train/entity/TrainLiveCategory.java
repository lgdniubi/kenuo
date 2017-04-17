package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 直播分类实体类
 * @author 小叶  2017年2月22日
 *
 */
public class TrainLiveCategory extends DataEntity<TrainLiveCategory>{

	
	private static final long serialVersionUID = 1L;
	
	private String trainLiveCategoryId;   //直播分类id
	private String parentId;              //父类id
	private String parentIds;             //父类ids
	private String name;                  //分类名称
	private int type;                     //级别类型（1：一级；2：二级；3：三级）
	private int sort;                    //排序
	private int delflag;                 //删除标识（0：正常；1：删除）
	private int num;                    //父类数量
	private int flag;                //标识       1.增加 2.查看  3.修改
	
	public String getTrainLiveCategoryId() {
		return trainLiveCategoryId;
	}
	public void setTrainLiveCategoryId(String trainLiveCategoryId) {
		this.trainLiveCategoryId = trainLiveCategoryId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}

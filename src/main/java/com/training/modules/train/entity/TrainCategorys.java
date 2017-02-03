package com.training.modules.train.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Office;

/**
 * 课程分类
 * @author kele
 * @version 2016年3月11日
 */
public class TrainCategorys extends DataEntity<TrainCategorys>{
	
	private static final long serialVersionUID = 1L;
	private String categoryId;		//类别ID
	private String parentId;		//父类ID
	private String name;			//类别名称
	private int priority;			//优先级
	private String coverPic;		//分类封面
	private String introduce;		//分类介绍
	private int status;				//状态
	
	private String cateType;		//分类类型（0：公共；1：定制）
	private String officeType;		//机构类型（1：公司管理部；2：区域管理部；3：市场管理部；4：店铺管理部）
	private String officeCode;		//机构编码
	private String createuser;		//创建者
	
	private int num;				//是否包含子类
	
	private Office office;			//绑定机构  用于数据权限
	private Franchisee franchisee;	//商家
	
	private int sort;				//排序
	private int isShow;				//是否显示
	
	public TrainCategorys() {
		super();
	}

	public TrainCategorys(String categoryId) {
		super();
		this.categoryId = categoryId;
	}
	
	public TrainCategorys(String categoryId, String parentId, String name, int priority, String coverPic,
			String introduce, int status, String cateType, String officeType, String officeCode, String createuser) {
		super();
		this.categoryId = categoryId;
		this.parentId = parentId;
		this.name = name;
		this.priority = priority;
		this.coverPic = coverPic;
		this.introduce = introduce;
		this.status = status;
		this.cateType = cateType;
		this.officeType = officeType;
		this.officeCode = officeCode;
		this.createuser = createuser;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCateType() {
		return cateType;
	}

	public void setCateType(String cateType) {
		this.cateType = cateType;
	}

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	/**
	 * 对分类进行"递归"方式进行重新排序
	 * 规则： 父类 1--子类列表
	 * 		父类2--子类列表
	 * @param list
	 * @param sourcelist
	 * @param parentId
	 * @param cascade
	 */
	@JsonIgnore
	public static void sortList(List<TrainCategorys> list, List<TrainCategorys> sourcelist, String parentId, boolean cascade){
		for (int i = 0; i < sourcelist.size(); i++) {
			TrainCategorys t = sourcelist.get(i);  
			if(null != t.getParentId() && !"".equals(t.getParentId()) 
					&& (parentId).equals(t.getParentId())){
				list.add(t);
				if(cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j = 0; j < sourcelist.size(); j++) {
						TrainCategorys child = sourcelist.get(j);
						if (null != child.getParentId() && !"".equals(child.getParentId()) 
								&& (child.getParentId()).equals(t.getCategoryId())) {
							sortList(list, sourcelist, child.getParentId(), true);
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 根目录ID
	 * @return
	 */
	@JsonIgnore
	public static String getRootId(){
		return "0";
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Franchisee getFranchisee() {
		return franchisee;
	}

	public void setFranchisee(Franchisee franchisee) {
		this.franchisee = franchisee;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
}

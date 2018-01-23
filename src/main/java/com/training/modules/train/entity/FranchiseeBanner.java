package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
import com.training.modules.ec.entity.Banner;

/**
 * 
 * @className franchiseeBanner
 * @description TODO 妃子校banner图商家权限Entity
 * @author chenbing
 * @date 2018年1月22日 兵子
 *
 *
 */
public class FranchiseeBanner extends DataEntity<Banner>{

	private static final long serialVersionUID = 1L;
	
	private int fbId;		//主键
	private int createFranchisee;	//创建者商家id
	private int bannerId;			//banner图id
	private int soFranchisee;		//可见商家id
	
	
	public int getFbId() {
		return fbId;
	}
	public void setFbId(int fbId) {
		this.fbId = fbId;
	}
	public int getCreateFranchisee() {
		return createFranchisee;
	}
	public void setCreateFranchisee(int createFranchisee) {
		this.createFranchisee = createFranchisee;
	}
	public int getBannerId() {
		return bannerId;
	}
	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}
	public int getSoFranchisee() {
		return soFranchisee;
	}
	public void setSoFranchisee(int soFranchisee) {
		this.soFranchisee = soFranchisee;
	}
}

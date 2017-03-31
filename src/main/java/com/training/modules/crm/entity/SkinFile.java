package com.training.modules.crm.entity;

import com.training.common.persistence.DataEntity;

/**    
* kenuo      
* @description：皮肤状况实体类   
* @author：sharp   
* @date：2017年3月8日            
*/
public class SkinFile extends DataEntity<SkinFile> {

	private static final long serialVersionUID = 1L;
	
	private String userId;				//用户Id
	private String nowSkinCondition;	//当前皮肤状态
	private String skinType;			//皮肤类型
	private String skinResistibility;	//抵抗力
	private String sleepStatus;		//睡眠状况
	private String agingCause;	    	//老化原因
	private String eyeSituation;		//眼部状况
	private String pachulosisType;		//斑类型
	private String allergicCause;		//过敏原因
	private String acneCause;			//暗疮原因
	private String immuneSignal;		//免疫信号
	private String usualCareProduct;  //平日所用产品
	private String physique;			//体质判断
	private String remark;              //其他问题
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNowSkinCondition() {
		return nowSkinCondition;
	}
	public void setNowSkinCondition(String nowSkinCondition) {
		this.nowSkinCondition = nowSkinCondition;
	}
	public String getSkinType() {
		return skinType;
	}
	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}
	public String getSkinResistibility() {
		return skinResistibility;
	}
	public void setSkinResistibility(String skinResistibility) {
		this.skinResistibility = skinResistibility;
	}
	public String getSleepStatus() {
		return sleepStatus;
	}
	public void setSleepStatus(String sleepStatus) {
		this.sleepStatus = sleepStatus;
	}
	public String getAgingCause() {
		return agingCause;
	}
	public void setAgingCause(String agingCause) {
		this.agingCause = agingCause;
	}
	public String getEyeSituation() {
		return eyeSituation;
	}
	public void setEyeSituation(String eyeSituation) {
		this.eyeSituation = eyeSituation;
	}
	public String getPachulosisType() {
		return pachulosisType;
	}
	public void setPachulosisType(String pachulosisType) {
		this.pachulosisType = pachulosisType;
	}
	public String getAllergicCause() {
		return allergicCause;
	}
	public void setAllergicCause(String allergicCause) {
		this.allergicCause = allergicCause;
	}
	public String getAcneCause() {
		return acneCause;
	}
	public void setAcneCause(String acneCause) {
		this.acneCause = acneCause;
	}
	public String getImmuneSignal() {
		return immuneSignal;
	}
	public void setImmuneSignal(String immuneSignal) {
		this.immuneSignal = immuneSignal;
	}
	public String getUsualCareProduct() {
		return usualCareProduct;
	}
	public void setUsualCareProduct(String usualCareProduct) {
		this.usualCareProduct = usualCareProduct;
	}
	public String getPhysique() {
		return physique;
	}
	public void setPhysique(String physique) {
		this.physique = physique;
	}
}
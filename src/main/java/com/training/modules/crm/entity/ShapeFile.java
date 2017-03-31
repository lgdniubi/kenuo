package com.training.modules.crm.entity;

import com.training.common.persistence.DataEntity;

/**    
* kenuo      
* @description：形体档案实体类   
* @author：sharp   
* @date：2017年3月8日            
*/
public class ShapeFile extends DataEntity<ShapeFile> {

	private static final long serialVersionUID = 1L;
	  
	private String userId;			//用户Id
	private String breastHeightStandard;	//胸高标准
	private String upperBust;					//上胸围
	private String bust;					//胸围
	private String lowerBust;				//下胸围
	private String abdomen;	    			//腹围
	private String leftBp;					//左BP
	private String rightBp;				//右BP
	private String hip;						//臀围
	private String bb;						//BB
	private String waist;					//腰围
	private String leftThign;			    //左大腿
	private String rightThign;				//右大腿

	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBreastHeightStandard() {
		return breastHeightStandard;
	}
	public void setBreastHeightStandard(String breastHeightStandard) {
		this.breastHeightStandard = breastHeightStandard;
	}

	public String getUpperBust() {
		return upperBust;
	}
	public void setUpperBust(String upperBust) {
		this.upperBust = upperBust;
	}
	public String getLowerBust() {
		return lowerBust;
	}
	public void setLowerBust(String lowerBust) {
		this.lowerBust = lowerBust;
	}
	public String getLeftBp() {
		return leftBp;
	}
	public void setLeftBp(String leftBp) {
		this.leftBp = leftBp;
	}
	public String getRightBp() {
		return rightBp;
	}
	public void setRightBp(String rightBp) {
		this.rightBp = rightBp;
	}
	public String getLeftThign() {
		return leftThign;
	}
	public void setLeftThign(String leftThign) {
		this.leftThign = leftThign;
	}
	public String getRightThign() {
		return rightThign;
	}
	public void setRightThign(String rightThign) {
		this.rightThign = rightThign;
	}
	public String getBust() {
		return bust;
	}
	public void setBust(String bust) {
		this.bust = bust;
	}
	
	public String getAbdomen() {
		return abdomen;
	}
	public void setAbdomen(String abdomen) {
		this.abdomen = abdomen;
	}
	
	public String getHip() {
		return hip;
	}
	public void setHip(String hip) {
		this.hip = hip;
	}
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
	}
	public String getWaist() {
		return waist;
	}
	public void setWaist(String waist) {
		this.waist = waist;
	}
}
package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

public class Fvo{
	private static final long serialVersionUID = 1L;
	
	private int franchiseeId;
	private String officeId;
	private String officeName;
	private String officePid;
	private String officePids;
	private String isCargo;
	
	
	public int getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getOfficePid() {
		return officePid;
	}
	public void setOfficePid(String officePid) {
		this.officePid = officePid;
	}
	public String getOfficePids() {
		return officePids;
	}
	public void setOfficePids(String officePids) {
		this.officePids = officePids;
	}
	public String getIsCargo() {
		return isCargo;
	}
	public void setIsCargo(String isCargo) {
		this.isCargo = isCargo;
	}

}

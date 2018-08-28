package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class UserWorkExperience extends DataEntity<UserWorkExperience>{
    
	
	private static final long serialVersionUID = 4663081582352284474L;
	private String userId;//用户ID
    private String onceCompany;//曾经工作过公司
    private Date workStartDate;//开始日期
    private Date workEndDate;//结束日期
    private String position;//职位
    private String major;//专业
    private String languageAbili;//离职原因
    private Integer workType;//工作类型区分（工作1的还是工作2）
    private Integer baseInfoId;		//user_base_info的id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOnceCompany() {
        return onceCompany;
    }

    public void setOnceCompany(String onceCompany) {
        this.onceCompany = onceCompany;
    }

    public Date getWorkStartDate() {
        return workStartDate;
    }

    public void setWorkStartDate(Date workStartDate) {
        this.workStartDate = workStartDate;
    }

    public Date getWorkEndDate() {
        return workEndDate;
    }

    public void setWorkEndDate(Date workEndDate) {
        this.workEndDate = workEndDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getWorkType() {
		return workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLanguageAbili() {
        return languageAbili;
    }

    public void setLanguageAbili(String languageAbili) {
        this.languageAbili = languageAbili;
    }

	public Integer getBaseInfoId() {
		return baseInfoId;
	}

	public void setBaseInfoId(Integer baseInfoId) {
		this.baseInfoId = baseInfoId;
	}
}
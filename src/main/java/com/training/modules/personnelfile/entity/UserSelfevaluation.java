package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 用户-个人评价
 * @author ouwei
 *
 */
public class UserSelfevaluation extends DataEntity<UserSelfevaluation> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8059711452283292857L;

	private String id;

    private String userId;//用户id

    private String hobby;//爱好

    private String specialty;//优点／专长

    private String failing;//弱点

    private String expansibility;//发展性

    private String selfEvaluation;//自我评价

    private Date createDate;//建立时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getFailing() {
        return failing;
    }

    public void setFailing(String failing) {
        this.failing = failing;
    }

    public String getExpansibility() {
        return expansibility;
    }

    public void setExpansibility(String expansibility) {
        this.expansibility = expansibility;
    }

    public String getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(String selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
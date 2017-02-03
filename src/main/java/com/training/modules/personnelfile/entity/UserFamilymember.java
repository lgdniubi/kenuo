package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class UserFamilymember extends DataEntity<UserFamilymember> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7149188180848845435L;

	private String id;

    private String userId;//用户id

    private String name;//姓名

    private String relation;//关系

    private Date birthday;//出生日期

    private String works;//工作单位

    private String phone;//联系电话

    private Integer ischild;//是否有小孩

    private Integer nameType;//类型区分(1：自己，2：紧急联系1,3：紧急联系2)

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIschild() {
        return ischild;
    }

    public void setIschild(Integer ischild) {
        this.ischild = ischild;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Integer getNameType() {
		return nameType;
	}

	public void setNameType(Integer nameType) {
		this.nameType = nameType;
	}
}
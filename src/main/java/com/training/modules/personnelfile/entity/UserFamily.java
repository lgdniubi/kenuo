package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class UserFamily extends DataEntity<UserFamily>  {

	private static final long serialVersionUID = -7059993997348103197L;

	private String id;

    private String userId;//用户id

    private String name;//名字

    private String contactNumber;//联系电话

    private Date birthday;//出生日期

    private String familyAddress;//父母住址

    private Date createDate;//建立时间
    
    private Integer nameType;//名字类型

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

	public Integer getNameType() {
		return nameType;
	}

	public void setNameType(Integer nameType) {
		this.nameType = nameType;
	}

	public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
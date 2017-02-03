package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 用户-离职情况
 * @author ouwei
 *
 */
public class UserDepartures extends DataEntity<UserDepartures>  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3780228590839939173L;

	private String id;

    private String userId;//用户id

    private Integer isleaveoffice;//是否离职

    private Date leaveofficeDate;//离职日期

    private Integer stopAccountingwages;//停止核算工资

    private String leaveofficeReasons;//离职原因

    private String operationLog;//操作日志

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

    public Integer getIsleaveoffice() {
        return isleaveoffice;
    }

    public void setIsleaveoffice(Integer isleaveoffice) {
        this.isleaveoffice = isleaveoffice;
    }

    public Date getLeaveofficeDate() {
        return leaveofficeDate;
    }

    public void setLeaveofficeDate(Date leaveofficeDate) {
        this.leaveofficeDate = leaveofficeDate;
    }

    public Integer getStopAccountingwages() {
        return stopAccountingwages;
    }

    public void setStopAccountingwages(Integer stopAccountingwages) {
        this.stopAccountingwages = stopAccountingwages;
    }

    public String getLeaveofficeReasons() {
        return leaveofficeReasons;
    }

    public void setLeaveofficeReasons(String leaveofficeReasons) {
        this.leaveofficeReasons = leaveofficeReasons;
    }

    public String getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(String operationLog) {
        this.operationLog = operationLog;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
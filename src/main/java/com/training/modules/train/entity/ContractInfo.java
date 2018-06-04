package com.training.modules.train.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 签约内容
 * @author QJL
 *
 */
public class ContractInfo extends DataEntity<ContractInfo> {
	private static final long serialVersionUID = 5103299117253232531L;

	private String office_id;	//机构id
	private String status; //状态：0创建，1：待审核，2：审核通过，3：审核驳回，4：补充协议
	private int franchisee_id; //商家ID
	private String office_name;	//机构名称
	private String office_pid; //机构父ID
	private String office_pids; //机构父IDS
	private String office_license; //营业执照
	private String office_no;	//注册号
	private String office_type;	//类型 0个体户、1合伙企业、2个人独资企业、3公司
	private String office_address;	//营业地址
	private String office_legalman;	//法人
	private String office_fonturl;	//证件前照
	private String office_backurl;	//证件反照
	private String office_bank;	//银行名称
	private String office_accountname;	//账户名称
	private String office_account;	//账户
	private String office_fontbank;	//银行卡前照
	private String office_backbank;	//银行卡反照
	private String office_date;	//成立日期
	private String sign_userid;	//签约人ID
	private String sign_username;	//签约人姓名
	private String sign_idcard;	//签约人证件
	private String sign_email;	//签约人邮箱
	private String sign_mobile;	//签约人手机号
	private String sign_fonturl;	//签约人证件前照
	private String sign_backurl;	//签约人证件反照
	private String cargo_userid;	//包活人ID
	private String cargo_username; //包活人姓名
	private String cargo_idcard;	//包活人身份证号
	private String cargo_email;	//报货人邮箱
	private String cargo_mobile;	//报货人手机号
	private String cargo_fonturl; //包活人身份证前图片
	private String cargo_backurl; //包活人身份证后图片
	private String audit_userid; 	//审核人ID
	private String audit_username;	//审核人姓名
	private String audit_idcard;	//审核人身份证
	private String audit_email;	//审核人邮箱
	private String audit_mobile;	//审核人手机号
	private String audit_fonturl;	//审核人身份证前图片
	private String audit_backurl;	//审核人身份证后图片
	private String proxy_userid;	//代付人ID
	private String proxy_username;	//代付人姓名
	private String proxy_idcard;	//代付人身份证
	private String proxy_email;	//代付人邮箱
	private String proxy_mobile;	//代付人手机号
	private String proxy_fonturl;	//代付人身份证前图片
	private String proxy_backurl;	//代付人身份证后图片
	private String create_user;	//创建人
	private String create_time; //创建日期
	private String update_user;	//更新人
	private String update_time;	//更新日期
	
	private String remarks; //驳回原因
	private String franchiseeName;
	private List<PayInfo> payInfos;
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
	public String getOffice_name() {
		return office_name;
	}
	public void setOffice_name(String office_name) {
		this.office_name = office_name;
	}
	public String getOffice_no() {
		return office_no;
	}
	public void setOffice_no(String office_no) {
		this.office_no = office_no;
	}
	public String getOffice_type() {
		return office_type;
	}
	public void setOffice_type(String office_type) {
		this.office_type = office_type;
	}
	public String getOffice_address() {
		return office_address;
	}
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}
	public String getOffice_legalman() {
		return office_legalman;
	}
	public void setOffice_legalman(String office_legalman) {
		this.office_legalman = office_legalman;
	}
	public String getOffice_fonturl() {
		return office_fonturl;
	}
	public void setOffice_fonturl(String office_fonturl) {
		this.office_fonturl = office_fonturl;
	}
	public String getOffice_backurl() {
		return office_backurl;
	}
	public void setOffice_backurl(String office_backurl) {
		this.office_backurl = office_backurl;
	}
	public String getOffice_bank() {
		return office_bank;
	}
	public void setOffice_bank(String office_bank) {
		this.office_bank = office_bank;
	}
	public String getOffice_accountname() {
		return office_accountname;
	}
	public void setOffice_accountname(String office_accountname) {
		this.office_accountname = office_accountname;
	}
	public String getOffice_account() {
		return office_account;
	}
	public void setOffice_account(String office_account) {
		this.office_account = office_account;
	}
	public String getOffice_fontbank() {
		return office_fontbank;
	}
	public void setOffice_fontbank(String office_fontbank) {
		this.office_fontbank = office_fontbank;
	}
	public String getOffice_backbank() {
		return office_backbank;
	}
	public void setOffice_backbank(String office_backbank) {
		this.office_backbank = office_backbank;
	}
	public String getOffice_date() {
		return office_date;
	}
	public void setOffice_date(String office_date) {
		this.office_date = office_date;
	}
	public String getSign_userid() {
		return sign_userid;
	}
	public void setSign_userid(String sign_userid) {
		this.sign_userid = sign_userid;
	}
	public String getSign_username() {
		return sign_username;
	}
	public void setSign_username(String sign_username) {
		this.sign_username = sign_username;
	}
	public String getSign_idcard() {
		return sign_idcard;
	}
	public void setSign_idcard(String sign_idcard) {
		this.sign_idcard = sign_idcard;
	}
	public String getSign_email() {
		return sign_email;
	}
	public void setSign_email(String sign_email) {
		this.sign_email = sign_email;
	}
	public String getSign_mobile() {
		return sign_mobile;
	}
	public void setSign_mobile(String sign_mobile) {
		this.sign_mobile = sign_mobile;
	}
	public String getSign_fonturl() {
		return sign_fonturl;
	}
	public void setSign_fonturl(String sign_fonturl) {
		this.sign_fonturl = sign_fonturl;
	}
	public String getSign_backurl() {
		return sign_backurl;
	}
	public void setSign_backurl(String sign_backurl) {
		this.sign_backurl = sign_backurl;
	}
	public String getCargo_userid() {
		return cargo_userid;
	}
	public void setCargo_userid(String cargo_userid) {
		this.cargo_userid = cargo_userid;
	}
	public String getCargo_username() {
		return cargo_username;
	}
	public void setCargo_username(String cargo_username) {
		this.cargo_username = cargo_username;
	}
	public String getCargo_idcard() {
		return cargo_idcard;
	}
	public void setCargo_idcard(String cargo_idcard) {
		this.cargo_idcard = cargo_idcard;
	}
	public String getCargo_email() {
		return cargo_email;
	}
	public void setCargo_email(String cargo_email) {
		this.cargo_email = cargo_email;
	}
	public String getCargo_mobile() {
		return cargo_mobile;
	}
	public void setCargo_mobile(String cargo_mobile) {
		this.cargo_mobile = cargo_mobile;
	}
	public String getCargo_fonturl() {
		return cargo_fonturl;
	}
	public void setCargo_fonturl(String cargo_fonturl) {
		this.cargo_fonturl = cargo_fonturl;
	}
	public String getCargo_backurl() {
		return cargo_backurl;
	}
	public void setCargo_backurl(String cargo_backurl) {
		this.cargo_backurl = cargo_backurl;
	}
	public String getAudit_userid() {
		return audit_userid;
	}
	public void setAudit_userid(String audit_userid) {
		this.audit_userid = audit_userid;
	}
	public String getAudit_username() {
		return audit_username;
	}
	public void setAudit_username(String audit_username) {
		this.audit_username = audit_username;
	}
	public String getAudit_idcard() {
		return audit_idcard;
	}
	public void setAudit_idcard(String audit_idcard) {
		this.audit_idcard = audit_idcard;
	}
	public String getAudit_email() {
		return audit_email;
	}
	public void setAudit_email(String audit_email) {
		this.audit_email = audit_email;
	}
	public String getAudit_mobile() {
		return audit_mobile;
	}
	public void setAudit_mobile(String audit_mobile) {
		this.audit_mobile = audit_mobile;
	}
	public String getAudit_fonturl() {
		return audit_fonturl;
	}
	public void setAudit_fonturl(String audit_fonturl) {
		this.audit_fonturl = audit_fonturl;
	}
	public String getAudit_backurl() {
		return audit_backurl;
	}
	public void setAudit_backurl(String audit_backurl) {
		this.audit_backurl = audit_backurl;
	}
	public String getProxy_userid() {
		return proxy_userid;
	}
	public void setProxy_userid(String proxy_userid) {
		this.proxy_userid = proxy_userid;
	}
	public String getProxy_username() {
		return proxy_username;
	}
	public void setProxy_username(String proxy_username) {
		this.proxy_username = proxy_username;
	}
	public String getProxy_idcard() {
		return proxy_idcard;
	}
	public void setProxy_idcard(String proxy_idcard) {
		this.proxy_idcard = proxy_idcard;
	}
	public String getProxy_email() {
		return proxy_email;
	}
	public void setProxy_email(String proxy_email) {
		this.proxy_email = proxy_email;
	}
	public String getProxy_mobile() {
		return proxy_mobile;
	}
	public void setProxy_mobile(String proxy_mobile) {
		this.proxy_mobile = proxy_mobile;
	}
	public String getProxy_fonturl() {
		return proxy_fonturl;
	}
	public void setProxy_fonturl(String proxy_fonturl) {
		this.proxy_fonturl = proxy_fonturl;
	}
	public String getProxy_backurl() {
		return proxy_backurl;
	}
	public void setProxy_backurl(String proxy_backurl) {
		this.proxy_backurl = proxy_backurl;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public int getFranchisee_id() {
		return franchisee_id;
	}
	public void setFranchisee_id(int franchisee_id) {
		this.franchisee_id = franchisee_id;
	}
	public String getOffice_pid() {
		return office_pid;
	}
	public void setOffice_pid(String office_pid) {
		this.office_pid = office_pid;
	}
	public String getOffice_pids() {
		return office_pids;
	}
	public void setOffice_pids(String office_pids) {
		this.office_pids = office_pids;
	}
	public List<PayInfo> getPayInfos() {
		return payInfos;
	}
	public void setPayInfos(List<PayInfo> payInfos) {
		this.payInfos = payInfos;
	}
	public String getOffice_license() {
		return office_license;
	}
	public void setOffice_license(String office_license) {
		this.office_license = office_license;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
}

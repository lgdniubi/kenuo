/**
 * 
 */
package com.training.modules.train.entity;

import java.util.List;

/**  
* <p>Title: Authentication.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月24日  
* @version 3.0.0 
*/

public class AuthenticationBean {

	private int id;				//授权id
	private int franchisee_id;		//商家id
	private String user_id;			//用户id
	private String name;			//用户名称
	private String mod_name;		//name
	private String mod_ename;		//ename
	private List<UserBean> user_ids;	//超级管理员id
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<UserBean> getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(List<UserBean> user_ids) {
		this.user_ids = user_ids;
	}
	public String getMod_name() {
		return mod_name;
	}
	public void setMod_name(String mod_name) {
		this.mod_name = mod_name;
	}
	public String getMod_ename() {
		return mod_ename;
	}
	public void setMod_ename(String mod_ename) {
		this.mod_ename = mod_ename;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFranchisee_id() {
		return franchisee_id;
	}
	public void setFranchisee_id(int franchisee_id) {
		this.franchisee_id = franchisee_id;
	}
	
}

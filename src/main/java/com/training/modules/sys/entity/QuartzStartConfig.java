/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月8日 下午4:47:39
 * 修改人:	
 * 修改时间:	2017年6月8日 下午4:47:39
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 类名称:	QuartzStartConfig
 * 类描述:	定时任务执行起始配置
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月8日 下午4:47:39
 */
public class QuartzStartConfig extends TreeEntity<QuartzStartConfig> {

	/**
	 *@Fields	serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private int qid;			//id
	private String key;			//表名称	
	private Integer value;		//当前id
	private Date createTime;	//创建时间
	
	
	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#getParent()
	 */
	@Override
	public QuartzStartConfig getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(QuartzStartConfig parent) {
		// TODO Auto-generated method stub
		
	}

}

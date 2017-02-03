package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * 文章资源Entity
 * @author coffee
 *
 */
public class ArticleIssueLogs extends DataEntity<ArticleIssueLogs>{
	private static final long serialVersionUID = 1L;
	private int logId;
	private int articleId;					//id
	private String log;
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
}

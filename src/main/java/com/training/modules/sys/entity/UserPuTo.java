/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.supcan.annotation.treelist.cols.SupCol;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 员工列表查看时，展示普通会员信息
 * 2018-03-23
 */
public class UserPuTo extends DataEntity<UserPuTo> {
	
	private static final long serialVersionUID = 1L;
	/*			
	
	*/
	private String memName;				//会员姓名
	private String memNickName;			//会员昵称
	private String memType;				//会员类型
	private String weiXin;				//微信
	private String weibo;				//微博
	private String memLevel;			//会员等级
	private String QQ;					//QQ
	private String memMobile;			//会员手机号
	private String regisTime;			//注册时间
	private String lastFee;				//最近消费
	private String attention;			//关注
	private String lesson;				//收藏课程
	private String scan;				//历史浏览
	private String fans;				//粉丝
	private String video;				//收藏视频
	private String articleComment;				//文章评论
	private String article;				//收藏文章
	private String score;				//学分
	private String answers;				//问答评论
	private String praise;				//获赞数
	private String goods;				//收藏商品
	private String totalFee;			//消费总金额
	private String yesterdayFee;		//昨日消费
	private String sevenFee;			//最近7日消费
	private String totalOrderNum;		//商学院订单数量
	private String totalOrderFund;		//商学院订单金额
	private String goodsOrderNum;		//商品订单数量
	private String goodsOrderFund;		//商品订单金额
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemNickName() {
		return memNickName;
	}
	public void setMemNickName(String memNickName) {
		this.memNickName = memNickName;
	}
	public String getMemType() {
		return memType;
	}
	public void setMemType(String memType) {
		this.memType = memType;
	}
	public String getWeiXin() {
		return weiXin;
	}
	public void setWeiXin(String weiXin) {
		this.weiXin = weiXin;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getMemLevel() {
		return memLevel;
	}
	public void setMemLevel(String memLevel) {
		this.memLevel = memLevel;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getMemMobile() {
		return memMobile;
	}
	public void setMemMobile(String memMobile) {
		this.memMobile = memMobile;
	}
	public String getRegisTime() {
		return regisTime;
	}
	public void setRegisTime(String regisTime) {
		this.regisTime = regisTime;
	}
	public String getLastFee() {
		return lastFee;
	}
	public void setLastFee(String lastFee) {
		this.lastFee = lastFee;
	}
	public String getAttention() {
		return attention;
	}
	public void setAttention(String attention) {
		this.attention = attention;
	}
	public String getLesson() {
		return lesson;
	}
	public void setLesson(String lesson) {
		this.lesson = lesson;
	}
	public String getScan() {
		return scan;
	}
	public void setScan(String scan) {
		this.scan = scan;
	}
	public String getFans() {
		return fans;
	}
	public void setFans(String fans) {
		this.fans = fans;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getArticleComment() {
		return articleComment;
	}
	public void setArticleComment(String articleComment) {
		this.articleComment = articleComment;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getAnswers() {
		return answers;
	}
	public void setAnswers(String answers) {
		this.answers = answers;
	}
	public String getPraise() {
		return praise;
	}
	public void setPraise(String praise) {
		this.praise = praise;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getYesterdayFee() {
		return yesterdayFee;
	}
	public void setYesterdayFee(String yesterdayFee) {
		this.yesterdayFee = yesterdayFee;
	}
	public String getSevenFee() {
		return sevenFee;
	}
	public void setSevenFee(String sevenFee) {
		this.sevenFee = sevenFee;
	}
	public String getTotalOrderNum() {
		return totalOrderNum;
	}
	public void setTotalOrderNum(String totalOrderNum) {
		this.totalOrderNum = totalOrderNum;
	}
	public String getTotalOrderFund() {
		return totalOrderFund;
	}
	public void setTotalOrderFund(String totalOrderFund) {
		this.totalOrderFund = totalOrderFund;
	}
	public String getGoodsOrderNum() {
		return goodsOrderNum;
	}
	public void setGoodsOrderNum(String goodsOrderNum) {
		this.goodsOrderNum = goodsOrderNum;
	}
	public String getGoodsOrderFund() {
		return goodsOrderFund;
	}
	public void setGoodsOrderFund(String goodsOrderFund) {
		this.goodsOrderFund = goodsOrderFund;
	}
//	@SupCol(isUnique="true", isHide="true")
//	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}
	
	
}
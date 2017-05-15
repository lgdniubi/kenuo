package com.training.modules.forms.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 直播/回放信息表
 * 
 * @author stone
 * @date 2017年4月24日
 */
public class TvShowTimeReport extends TreeEntity<TvShowTimeReport>{

	private static final long serialVersionUID = 1L;
	
	private String showLiveId;                  //直播ID
	private String showManName;				    //主播姓名
	private String showLiveTitle;				//直播标题
	private String showLiveNum;				    //直播播放次数
	private String showBackNum;				    //回看播放次数
	private String showLiveLookNum;				//直播观看人数
	private String showBackLookNum;				//回看观看人数
	private String collectNum;					//收藏量
	private String praiseNum;					//点赞量
	private String showLiveMaxLookNum;			//直播峰值人数
	private String isPay;						//是否付费
	private String stairOne;					//商家
	private String stairTwo;					//区域
	private String stairThree;					//集团军
	private String stairFour;					//市场
	private String stairFive;					//门店
	private String job;							//职位
	private String role;						//角色
	private String showLiveOne;					//直播一级分类
	private String showLiveTwo;					//直播二级分类
	private String showLiveThree;				//直播三级分类
	private String showLivebegtime;				//开始直播时间
	private String showLiveendtime;				//结束直播时间
	private Date begtime;					    //查询开始时间
	private Date endtime;					    //查询开始时间
	private String showId;						//直播Id
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@ExcelField(title = "直播ID", sort = 150 )
	public String getShowLiveId() {
		return showLiveId;
	}

	public void setShowLiveId(String showLiveId) {
		this.showLiveId = showLiveId;
	}
	
	@ExcelField(title = "主播姓名" , sort = 200)
	public String getShowManName() {
		return showManName;
	}

	public void setShowManName(String showManName) {
		this.showManName = showManName;
	}
	
	@ExcelField(title = "直播标题" , sort = 300)
	public String getShowLiveTitle() {
		return showLiveTitle;
	}

	public void setShowLiveTitle(String showLiveTitle) {
		this.showLiveTitle = showLiveTitle;
	}

	@ExcelField(title = "直播播放次数" , sort = 400)
	public String getShowLiveNum() {
		return showLiveNum;
	}

	public void setShowLiveNum(String showLiveNum) {
		this.showLiveNum = showLiveNum;
	}

	/*@ExcelField(title = "回看播放次数" , sort = 500)*/
	public String getShowBackNum() {
		return showBackNum;
	}

	public void setShowBackNum(String showBackNum) {
		this.showBackNum = showBackNum;
	}

	@ExcelField(title = "直播观看人数" , sort = 600)
	public String getShowLiveLookNum() {
		return showLiveLookNum;
	}

	public void setShowLiveLookNum(String showLiveLookNum) {
		this.showLiveLookNum = showLiveLookNum;
	}

	/*@ExcelField(title = "回看观看人数" , sort = 700)*/
	public String getShowBackLookNum() {
		return showBackLookNum;
	}

	public void setShowBackLookNum(String showBackLookNum) {
		this.showBackLookNum = showBackLookNum;
	}

	@ExcelField(title = "收藏量" , sort = 800)
	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	@ExcelField(title = "点赞量" , sort = 900)
	public String getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(String praiseNum) {
		this.praiseNum = praiseNum;
	}

	/*@ExcelField(title = "直播峰值人数" , sort = 1000)*/
	public String getShowLiveMaxLookNum() {
		return showLiveMaxLookNum;
	}

	public void setShowLiveMaxLookNum(String showLiveMaxLookNum) {
		this.showLiveMaxLookNum = showLiveMaxLookNum;
	}

	@ExcelField(title = "是否付费" , sort = 1100)
	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	@ExcelField(title = "商家" , sort = 1200)
	public String getStairOne() {
		return stairOne;
	}

	public void setStairOne(String stairOne) {
		this.stairOne = stairOne;
	}

	@ExcelField(title = "区域" , sort = 1300)
	public String getStairTwo() {
		return stairTwo;
	}

	public void setStairTwo(String stairTwo) {
		this.stairTwo = stairTwo;
	}

	@ExcelField(title = "集团军" , sort = 1400)
	public String getStairThree() {
		return stairThree;
	}

	public void setStairThree(String stairThree) {
		this.stairThree = stairThree;
	}

	@ExcelField(title = "市场" , sort = 1500)
	public String getStairFour() {
		return stairFour;
	}

	public void setStairFour(String stairFour) {
		this.stairFour = stairFour;
	}

	@ExcelField(title = "门店" , sort = 1600)
	public String getStairFive() {
		return stairFive;
	}

	public void setStairFive(String stairFive) {
		this.stairFive = stairFive;
	}

	@ExcelField(title = "职位" , sort = 1700)
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@ExcelField(title = "角色" , sort = 1800)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@ExcelField(title = "直播一级分类" , sort = 1900)
	public String getShowLiveOne() {
		return showLiveOne;
	}

	public void setShowLiveOne(String showLiveOne) {
		this.showLiveOne = showLiveOne;
	}

	@ExcelField(title = "直播二级分类" , sort = 2000)
	public String getShowLiveTwo() {
		return showLiveTwo;
	}

	public void setShowLiveTwo(String showLiveTwo) {
		this.showLiveTwo = showLiveTwo;
	}

	@ExcelField(title = "直播三级分类" , sort = 2100)
	public String getShowLiveThree() {
		return showLiveThree;
	}

	public void setShowLiveThree(String showLiveThree) {
		this.showLiveThree = showLiveThree;
	}

	@ExcelField(title = "开始直播时间" , sort = 2200)
	public String getShowLivebegtime() {
		return showLivebegtime;
	}

	public void setShowLivebegtime(String showLivebegtime) {
		this.showLivebegtime = showLivebegtime;
	}

	@ExcelField(title = "结束直播时间" , sort = 2300)
	public String getShowLiveendtime() {
		return showLiveendtime;
	}

	public void setShowLiveendtime(String showLiveendtime) {
		this.showLiveendtime = showLiveendtime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getBegtime() {
		return begtime;
	}

	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}

	@Override
	public TvShowTimeReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(TvShowTimeReport parent) {
		// TODO Auto-generated method stub
		
	}
	
}

package com.training.modules.ec.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;
/**
 * 文章资源Entity
 * @author coffee
 *
 */
public class ArticleRepository extends DataEntity<ArticleRepository>{
	private static final long serialVersionUID = 1L;
	private int articleId;					//id
	private int categoryId;			//分类id
	private String authorName;		//文章作者
	private String authorPhoto;		//作者头像
	private String title;			//文章标题
	private String shortTitle;		//短标题
	private String digest;			//摘要
	private String keywords;		//关键字
	private String contents;		//文章内容
	private int imageType;			//图片类型（0:无图  、1：一图 、2：二图 、 3：三图）  首图模式
	private int type;				//类型  1：草稿  0：发布
	private Date beginDate;			//开始时间
	private Date endDate;			//结束时间
	
	private ArticleImage articleImage;		//首图
	private List<ArticleImage> imageList;	//首图集合
	
	private ArticleRepositoryCategory category;		//文章分类
	private int likeNum;			//文章点赞数
	
	private String articleType;		//用于条件查询
	
	private int labelGoodsId;           //文章对应的商品id
	private String newLabelGoodsId;     //用来接收页面传过来的商品id，有可能为空，所以要接受再判断
	private String labelGoodsName;    //文章对应商品的名称
	private String labelActionType;    //商品活动类型（0：普通商品；1：限时抢购；2：团购；3：促销优惠；）
	private String goodsname;         //商品名称（回显用的）
	private String goodsCategoryName;  //商品分类名称（回显用的）
	
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorPhoto() {
		return authorPhoto;
	}
	public void setAuthorPhoto(String authorPhoto) {
		this.authorPhoto = authorPhoto;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getImageType() {
		return imageType;
	}
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public ArticleImage getArticleImage() {
		return articleImage;
	}
	public void setArticleImage(ArticleImage articleImage) {
		this.articleImage = articleImage;
	}
	public List<ArticleImage> getImageList() {
		return imageList;
	}
	public void setImageList(List<ArticleImage> imageList) {
		this.imageList = imageList;
	}
	public ArticleRepositoryCategory getCategory() {
		return category;
	}
	public void setCategory(ArticleRepositoryCategory category) {
		this.category = category;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public int getLabelGoodsId() {
		return labelGoodsId;
	}
	public void setLabelGoodsId(int labelGoodsId) {
		this.labelGoodsId = labelGoodsId;
	}
	public String getLabelGoodsName() {
		return labelGoodsName;
	}
	public void setLabelGoodsName(String labelGoodsName) {
		this.labelGoodsName = labelGoodsName;
	}
	public String getLabelActionType() {
		return labelActionType;
	}
	public void setLabelActionType(String labelActionType) {
		this.labelActionType = labelActionType;
	}
	public String getNewLabelGoodsId() {
		return newLabelGoodsId;
	}
	public void setNewLabelGoodsId(String newLabelGoodsId) {
		this.newLabelGoodsId = newLabelGoodsId;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}
	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}
	
}

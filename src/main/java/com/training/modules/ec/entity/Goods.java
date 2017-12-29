package com.training.modules.ec.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.BeanUtil;
import com.training.common.utils.StringUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Skill;

/**
 * 每天美耶-商品表
 * 
 * @author kele
 * @version 2016-6-15
 */
public class Goods extends DataEntity<Goods> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int goodsId; // 商品id
	private String goodsCategoryId; // 分类ID
	private GoodsCategory goodsCategory; // 商品分类id
	private String franchiseeId; // 加盟商ID
	private Franchisee franchisee; // 所属加盟商
	private String goodsBrandId; // 品牌ID
	private GoodsBrand goodsBrand; // 商品品牌
	private Effect effect; // 商品功效
	private String goodsSn; // 商品编号
	private String goodsShortName; // 商品短名称
	private String goodsDescription; // 商品描述
	private String goodsKey; // 商品密匙
	private String goodsName; // 商品名称
	private int totalStore;	// 商品总库存
	private int storeCount; // 剩余数量
	private int weight; // 单位：克
	private double marketPrice; // 市场价
	private double shopPrice; // 优惠价
	private double costPrice; // 商品成本价
	private String goodsTags; // 商品标签
	private String keywords; // 商品关键词
	private String goodsRemark; // 商品简单描述
	private String goodsContent; // 商品详细描述
	private String originalImg; // 商品上传原始图
	private String isRecommend; // 是否推荐[0:否推荐；1：是推荐；]
	private String isNew; // 是否新品[0:否新品；1：是新品；]
	private String isHot; // 是否热卖[0:否热卖；1：是热卖；]
	private String isOnSale; // 是否上架[0:否上架；1：是上架；]
	private String isFreeShipping; // 是否包邮[0:否包邮；1：是包邮；]
	private String isReal; // 是否为实物[0:是；1：否；]
	private Date onTime; // 商品上架时间
	private int sort; // 商品排序
	private String attrType; // 商品所属类型
	private String specType; // 商品所属规格
	private int giveIntegral; // 购买商品赠送积分
	private int salesSum; // 商品销量
	private String regionName; // 商品适合城市
	private String regionId;    //商品适合城市id（用于界面回写）
	private String actionType = "0"; // 活动类型 (0 普通商品,1 限时抢购, 2 团购 , 3 促销优惠)
	private int actionId; // 优惠活动id
	private String actionName;	//活动名称
	private int serviceMin; // 服务时长
	private Date lastUpdate; // 最后更新时间
	private int goodsNum;	//商品数量
	private String querytext; // 文本查询

	public int imageType; // 商品图片类型[0:是商品图;1:护理流程图;]

	private List<GoodsImages> goodsImagesList; // 商品相册
	private List<GoodsSpecPrice> goodsSpecPricesList;// 商品的规格排列组合列表
	private List<GoodsSpecImage> goodsSpecImagesList;// 商品的规格图片
	private List<GoodsAttributeMappings> goodsAttributeMappingsList;// 商品属性列表
	
	private Skill skill;      //商品技能标签
	private EquipmentLabel equipmentLabel;   //商品设备标签
	private String isAppshow;      //是否app显示[0：否；1：是]
	
	private double advancePrice;	// 预约金
	private int goodsType;			// 商品区分(0: 老商品 1: 新商品)
	private int supplierId;		//供应商id
	private String supplierName;//供应商name
	
	private int subId;           //主题图中商品对应的主题图id
	
	private int adId;           //首页广告图中商品对应的首页广告图id
	
	private String rank;		//商品评论星级
	private int commentNum;		//商品评论总数
	private int buyCount;		//商品购买总数
	
	private int integral ;		//赠送云币数量
	//---------------------------套卡使用字段-----------------------------------
	private List<Integer> goodsIds;			//卡项商品 ID
	private List<Integer> goodsNums;		//卡项商品 次（个）数
	private List<Double> marketPrices;		//卡项商品 市场单价
	private List<Double> prices;			//卡项商品 优惠价
	private List<Double> totalMarketPrices;	//卡项商品 市场价合计
	private List<Double> totalPrices;		//卡项商品 优惠价合计
	//---------------------------项目部位-----------------------------------
	private String positionId;			//项目部位ID
	private String positionIds;		//项目部位IDS（以下划线隔开）
	
	private List<String> cityIds;    //城市异价商品的归属城市
	private int isRatio;          //是否参与了城市异价（0：否，1：是）
	
	/**
	 * get/set
	 */
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(String goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public GoodsCategory getGoodsCategory() {
		return goodsCategory;
	}
	public void setGoodsCategory(GoodsCategory goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public Franchisee getFranchisee() {
		return franchisee;
	}
	public void setFranchisee(Franchisee franchisee) {
		this.franchisee = franchisee;
	}
	public String getGoodsBrandId() {
		return goodsBrandId;
	}
	public void setGoodsBrandId(String goodsBrandId) {
		this.goodsBrandId = goodsBrandId;
	}
	public GoodsBrand getGoodsBrand() {
		return goodsBrand;
	}
	public void setGoodsBrand(GoodsBrand goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	public Effect getEffect() {
		return effect;
	}
	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	public String getGoodsShortName() {
		return goodsShortName;
	}
	public void setGoodsShortName(String goodsShortName) {
		this.goodsShortName = goodsShortName;
	}
	public String getGoodsDescription() {
		return goodsDescription;
	}
	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
	}
	public String getGoodsKey() {
		return goodsKey;
	}
	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getTotalStore() {
		return totalStore;
	}
	public void setTotalStore(int totalStore) {
		this.totalStore = totalStore;
	}
	public int getStoreCount() {
		RedisClientTemplate redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
		String store_count = redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX + this.getGoodsId());
		if(StringUtils.isNotBlank(store_count)){
			storeCount = Integer.parseInt(store_count);
		}
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getShopPrice() {
		return shopPrice;
	}
	public void setShopPrice(double shopPrice) {
		this.shopPrice = shopPrice;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public String getGoodsTags() {
		return goodsTags;
	}
	public void setGoodsTags(String goodsTags) {
		this.goodsTags = goodsTags;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getGoodsRemark() {
		return goodsRemark;
	}
	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}
	public String getGoodsContent() {
		return goodsContent;
	}
	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}
	public String getOriginalImg() {
		return originalImg;
	}
	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
	}
	public String getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(String isOnSale) {
		this.isOnSale = isOnSale;
	}
	public String getIsFreeShipping() {
		return isFreeShipping;
	}
	public void setIsFreeShipping(String isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	public String getIsReal() {
		return isReal;
	}
	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}
	public Date getOnTime() {
		return onTime;
	}
	public void setOnTime(Date onTime) {
		this.onTime = onTime;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getSpecType() {
		return specType;
	}
	public void setSpecType(String specType) {
		this.specType = specType;
	}
	public int getGiveIntegral() {
		return giveIntegral;
	}
	public void setGiveIntegral(int giveIntegral) {
		this.giveIntegral = giveIntegral;
	}
	public int getSalesSum() {
		return salesSum;
	}
	public void setSalesSum(int salesSum) {
		this.salesSum = salesSum;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public int getServiceMin() {
		return serviceMin;
	}
	public void setServiceMin(int serviceMin) {
		this.serviceMin = serviceMin;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getQuerytext() {
		return querytext;
	}
	public void setQuerytext(String querytext) {
		this.querytext = querytext;
	}
	public int getImageType() {
		return imageType;
	}
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	public List<GoodsImages> getGoodsImagesList() {
		return goodsImagesList;
	}
	public void setGoodsImagesList(List<GoodsImages> goodsImagesList) {
		this.goodsImagesList = goodsImagesList;
	}
	public List<GoodsSpecPrice> getGoodsSpecPricesList() {
		return goodsSpecPricesList;
	}
	public void setGoodsSpecPricesList(List<GoodsSpecPrice> goodsSpecPricesList) {
		this.goodsSpecPricesList = goodsSpecPricesList;
	}
	public List<GoodsSpecImage> getGoodsSpecImagesList() {
		return goodsSpecImagesList;
	}
	public void setGoodsSpecImagesList(List<GoodsSpecImage> goodsSpecImagesList) {
		this.goodsSpecImagesList = goodsSpecImagesList;
	}
	public List<GoodsAttributeMappings> getGoodsAttributeMappingsList() {
		return goodsAttributeMappingsList;
	}
	public void setGoodsAttributeMappingsList(List<GoodsAttributeMappings> goodsAttributeMappingsList) {
		this.goodsAttributeMappingsList = goodsAttributeMappingsList;
	}
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public String getIsAppshow() {
		return isAppshow;
	}
	public void setIsAppshow(String isAppshow) {
		this.isAppshow = isAppshow;
	}
	public EquipmentLabel getEquipmentLabel() {
		return equipmentLabel;
	}
	public void setEquipmentLabel(EquipmentLabel equipmentLabel) {
		this.equipmentLabel = equipmentLabel;
	}
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public double getAdvancePrice() {
		return advancePrice;
	}
	public void setAdvancePrice(double advancePrice) {
		this.advancePrice = advancePrice;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public List<Integer> getGoodsIds() {
		return goodsIds;
	}
	public void setGoodsIds(List<Integer> goodsIds) {
		this.goodsIds = goodsIds;
	}
	public List<Integer> getGoodsNums() {
		return goodsNums;
	}
	public void setGoodsNums(List<Integer> goodsNums) {
		this.goodsNums = goodsNums;
	}
	public List<Double> getMarketPrices() {
		return marketPrices;
	}
	public void setMarketPrices(List<Double> marketPrices) {
		this.marketPrices = marketPrices;
	}
	public List<Double> getPrices() {
		return prices;
	}
	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}
	public List<Double> getTotalMarketPrices() {
		return totalMarketPrices;
	}
	public void setTotalMarketPrices(List<Double> totalMarketPrices) {
		this.totalMarketPrices = totalMarketPrices;
	}
	public List<Double> getTotalPrices() {
		return totalPrices;
	}
	public void setTotalPrices(List<Double> totalPrices) {
		this.totalPrices = totalPrices;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionIds() {
		return positionIds;
	}
	public void setPositionIds(String positionIds) {
		this.positionIds = positionIds;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public List<String> getCityIds() {
		return cityIds;
	}
	public void setCityIds(List<String> cityIds) {
		this.cityIds = cityIds;
	}
	public int getIsRatio() {
		return isRatio;
	}
	public void setIsRatio(int isRatio) {
		this.isRatio = isRatio;
	}
	
}

package com.training.modules.quartz.tasks.utils;

/**
 * 每天美耶-redis配置文件
 * @author kele
 * @version 2016年9月28日
 */
public class RedisConfig {
	public static final String BEAUTY_ID_HASH = "BEAUTY_ID_HASH"; //美容师id hash定时任务用
	public static final String SUBSCRIBETIME_PREFIX = "SUBTIME_"; //美容师预约时间列表key前缀
	public static final String SUBSCRIBETIMETP_KEY = "SUBSCRIBETIMETP"; //美容师时间点数组hash
	public static final String GOODS_STORECOUNT_PREFIX = "GOODSSTORE_"; //商品库存
	public static final String GOODS_SPECPRICE_PREFIX = "SPECPRICE_";//规格库存
	public static final String GOODS_IDS_HASH = "GOODS_IDS_HASH"; //商品idhash
	public static final String GOODS_SPECPRICE_HASH = "GOODS_SPECPRICE_HASH"; //商品规格 hash key
	public static final String OFFICE_IDS_KEY = "OFFICE_IDS";//部门office——code
	public static final String SCORE_SORT_KEY = "SCORE_SORT_"; //总学分排行
	public static final String LEVEL_VALUE_UPLIMIT_KEY = "LEVEL_VALUE_UPLIMIT"; //颜值上限
	public static final String PAY_POINT_UPLIMIT_KEY = "PAY_POINT_UPLIMIT";//金币上限
	public static final String BEAUTIFUL_DAY_SCORE = "BEAUTIFUL_DAY_SCORE";//美容师当天学分累计
	public static final String BEAUTIFUL_DAY_GOLD = "BEAUTIFUL_DAY_GOLD";//美容师当天金币累计
	public static final String buying_limit_prefix = "buying_limit_"; //活动抢购商品限购
	public static final String buying_limit_user_prefix = "buying_limit_user_"; //用户活动抢购商品限购
	public static final String ORDER_TIMEOUT = "order_timeout";//过期时间
	public static final String coupon_amountids_hash = "coupon_amountids";//存放缓存红包的id
	public static final String coupon_store_prefix = "coupon_store_";//面值库存
	public static final String AREA_CODE_KEY = "AREA_CODE";//区code集合
	public static final String office_area_ids_key = "office_area_ids"; //区id集合
	public static final String AREA_WEEK_BEAUTIFUL_KEY = "AREA_WEEK_BEAUTIFUL_";//区域美容师周数据
	public static final String train_uplimit_set = "train_uplimit_set";//用户当天上限用户id
	public static final String train_nowruleuplimit_prefix = "train_nowruleuplimit_";//用户当前获得分数
	public static final String mtmy_uplimit_set = "mtmy_uplimit_set"; //用户id
	public static final String mtmy_nowruleuplimit_prefix = "mtmy_nowruleuplimit_"; //用户当天已获得
	//日志记录器
	public static final String GOODSCOLLECT_KEY = "GOODSCOLLECT_KEY";//商品收藏
	
	public static final String COUNT_ARTICLE_DETAIL_NUM_KEY = "COUNT_ARTICLE_DETAIL_NUM_KEY";//记录文章详情  浏览量
	public static final String ARTICLE_SHARE_COUNT_KEY = "ARTICLE_SHARE_COUNT_KEY";//记录文章详情分享数
	
	// 团购活动
	public static final String GOODS_GROUPACTIVITY_IDS_HASH = "GOODS_GROUPACTIVITY_IDS_HASH"; //团购活动商品idhash
	public static final String GOODS_GROUPACTIVITY_SPECPRICE_HASH = "GOODS_GROUPACTIVITY_SPECPRICE_HASH";//团购活动商品规格hash key
	public static final String GOODS_GROUPACTIVITY_STORECOUNT_PREFIX = "GOODSSTORE_GROUPACTIVITY_"; //团购活动商品库存
	public static final String GOODS_GROUPACTIVITY_SPECPRICE_PREFIX = "SPECPRICE_GROUPACTIVITY_";//团购活动规格库存
	
}

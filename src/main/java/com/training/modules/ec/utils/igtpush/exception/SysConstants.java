package com.training.modules.ec.utils.igtpush.exception;

public class SysConstants {
	
	public static final String SUCCESS_MESSAGE = "成功";
	public static final String SUCCESS_RESULT = "200";
	
	public static final String ERROR_MESSAGE = "失败";
	public static final String ERROR_RESULT = "10000";
	
	public static final String ERROR_LOGIN_MESSAGE = "用户名或密码错误";
	public static final String ERROR_LOGIN_RESULT = "10001";
	
	public static final String ERROR_PARAM_MESSAGE = "参数有误";
	public static final String ERROR_PARAM_RESULT = "10002";
	
	public static final String ERROR_OPERATE_MESSAGE = "操作失败";
	public static final String ERROR_OPERATE_RESULT = "10003";
	
	public static final String ERROR_UPLOAD_MESSAGE = "文件为空或不存在";
	public static final String ERROR_UPLOAD_RESULT = "10004";
	
	public static final String ERROR_IDENTIFY_MESSAGE = "获取验证码失败";
	public static final String ERROR_IDENTIFY_RESULT = "10005";
	
	public static final String ERROR_PUSHTYPE_MESSAGE = "推送类型为NULL";
	public static final String ERROR_PUSHTYPE_RESULT = "10006";
	
	public static final String ERROR_CID_MESSAGE = "CID为NULL";
	public static final String ERROR_CID_RESULT = "10007";
	
	public static final String ERROR_CIDS_MESSAGE = "CIDS为NULL或大小为0";
	public static final String ERROR_CIDS_RESULT = "10008";
	
	public static final String ERROR_DUPKEY_MESSAGE = "记录重复提交";
	public static final String ERROR_DUPKEY_RESULT = "10009";
	
	public static final String ERROR_CONTENT_MESSAGE = "内容为NULL";
	public static final String ERROR_CONTENT_RESULT = "10010";
	
	public static final String ERROR_NOTIFYIDS_MESSAGE = "notify_ids为NULL或大小为0";
	public static final String ERROR_NOTIFYIDS_RESULT = "10011";
	
	public static final String ERROR_USERTYPE_MESSAGE = "只有美容师可以登录";
	public static final String ERROR_USERTYPE_RESULT = "10012";
	
	public static final String ERROR_PUSH_RESULT = "10012"; //推送失败,由个推推送失败结果获得message
	
	public static final String ERROR_SQL_MESSAGE = "操作数据错误";
	public static final String ERROR_SQL_RESULT = "10013";
	
	public static final String ERROR_TOKEN_MESSAGE = "token过期";
	public static final String ERROR_TOKEN_RESULT = "10014";
	
	public static final String ERROR_OFFICECODE_MESSAGE = "非法officecode";
	public static final String ERROR_OFFICECODE_RESULT = "10015";
	
	
	public static final String ASKLIST_KEY = "ASKLIST"; //提问列表
	public static final String ASKCOMML_KEY = "ASKCOMML";//提问的评论列表
	public static final String MYASKLIST_KEY = "MYASKL"; //我的提问列表
	public static final String ARTCONTENT_KEY = "ARTCONTENT";//文章内容
	public static final String MYARTL_KEY = "MYARTL"; //我的文章列表
	public static final String MYSUBSCRIBEL_KEY = "MYSUBSCRIBEL";//我订阅号列表
	public static final String MYLESSONL_KEY = "MYLESSONL";//我的课程列表
	public static final String LESSION_SCORE_KEY =  "LESSION_SCORE"; //
	//public static final String ARTCOMML_KEY = "ARTCOMML";//文章评论
	public static final String MYASKCOMML_KEY = "MYASKCOMML";//我的提问评论列表
	public static final String CATEGORYL_KEY = "CATEGORYL";//分类列表
	public static final String LESSONDETAIL_KEY = "LESSONDETAIL";//课程详情
	public static final String ASKDETAIL_KEY = "ASKDETAIL";//提问详情
	public static final String EXERCISEL_KEY = "EXERCISEL";//考试题列表：课后习题、单元测试
	public static final String OFFICE_USER_PREFIX = "OFFICE_USER_";//我的同事
	public static final String ORDEREDLIST_KEY = "ORDEREDLIST";//积分排行榜
	public static final String LESSONCOMML_KEY = "LESSONCOMML";//课程评论
	public static final String OFFICESUBTIMEL_KEY = "OFFICESUBTIMEL";//线下预约时间列表
	public static final String OFFICE_NAME_KEY = "OFFICE_NAME"; //部门name
	public static final String OFFICE_CODE_KEY = "OFFICE_CODE";//部门office——code
	public static final String FRANCHISEE_NAME_KEY = "FRANCHISEE_NAME";
	public static final String SPECIALITY_PREFIX = "SPECIALITY_";//特长（按加盟商存储）
	
	public static final String IDENTIFYCODE_KEY = "IDENTIFYCODE";//验证码
	public static final String USERINFO_KEY = "USERINFO";//用户信息
	public static final String USERTOKEN_KEY = "USERTOKEN";
	public static final String USER_TYPE_KEY = "USER_TYPE"; //用户类型
	public static final String SCORE_SORT_KEY = "SCORE_SORT_"; //总学分排行
	//public static final String SCORE_SORT_TEMP_KEY = "SCORE_SORT_TEMP_";//临时学分排行（存前一天的数据）
	public static final String AREA_CODE_KEY = "AREA_CODE";//区code集合
	public static final String SHOP_CODE_KEY = "SHOP_CODE"; //店铺code集合
	public static final String AREA_WEEK_BEAUTIFUL_KEY = "AREA_WEEK_BEAUTIFUL";//区域美容师周数据
	public static final String AREA_WEEK_SHOP_KEY = "AREA_WEEK_SHOP";//区域店铺周数据
	
	public static final String BEAUTIFUL_DAY_SCORE = "BEAUTIFUL_DAY_SCORE";//美容师当天学分累计
	public static final String BEAUTIFUL_DAY_GOLD = "BEAUTIFUL_DAY_GOLD";//美容师当天金币累计
	
	
	//------------------------金币 或 学分的规则-----------------------------------------//
	public static final String SCORE_ADD = "0";//添加学分
	public static final String SCORE_DEL = "1";//负学分
	//学分
	public static final String SCORE_L_VEDIO_ = "score_l_video_";//看视频
	public static final String SCORE_L_LIVE_ = "score_l_live_";	//看直播
	public static final String SCORE_L_BACK_ = "score_l_back_";//看直播回放
	public static final String SCORE_PDF_ = "score_pdf_";//看pdf
	public static final String SCORE_LESSON_TEST_ = "score_sesson_test_";//课程测试
	public static final String SCORE_ACTIVE_ = "score_active_";//任务计划
	public static final String SCORE_LOAD_PDF_ = "score_load_pdf_";//下载pdf
	public static final String SCORE_LOAD_VIDEO_ = "score_load_video_";//下载课程视频
	
	
	//金币
	public static final String GOLD_RELEASE_ = "gold_release_";//发布内容
	public static final String GOLD_COMMENTS_ = "gold_comments_";//评论
	public static final String GOLD_COLLECTION_ = "gold_collection_";//收藏
	public static final String GOLD_FEEDBACK_ = "gold_feedback_";//意见建议
	
	public static final int DAY_HEIGHT_GOLD = 200;//当天最高金币
	public static final int DAY_HEIGHT_SCORE = 200;//当天最高学分
}	

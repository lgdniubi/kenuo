/**
 * 项目名称：	kenuo
 * 类名称：  	TrackUserAuthentPub
 * 类描述：
 * 创建人：	kele
 * 创建时间：    2018年6月14日 下午3:20:47
 * 修改人：  	idata
 * 修改时间：    2018年6月14日 下午3:20:47
 * 修改备注：
 * @Version
 */
package com.training.common.track.modules;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.training.common.track.config.TrackConfig;
import com.training.common.track.utils.TrackUtils;
import com.training.common.utils.BeanUtil;
import com.training.modules.track.entity.TUserAudit;
import com.training.modules.track.service.TUserAuditService;

/**
 * 类名称：  	TrackUserAuthentPub
 * 类描述：	用户审核-公共事件
 * 创建人：  	kele
 * 创建时间：	2018年6月14日 下午3:20:47
 */
public class TrackUserAuditPub {

	private static Log log = LogFactory.getLog(TrackUserAuditPub.class);
	// 用户审核Service层
	private static TUserAuditService tUserAuditService = (TUserAuditService) BeanUtil.getBean("TUserAuditService");
	
	private Map<String, Object> paramMap;
	
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * 方法说明：	认证成功用户
	 * 创建时间：	2018年6月14日 下午3:27:06
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月14日 下午3:27:06
	 */
	public void user_authent_win() {
		try {
			// 使用 DebugConsumer 初始化 SensorsAnalytics
			final SensorsAnalytics sa = TrackConfig.getFzxSensorsAnalytics(this.paramMap);
			
			// 用户的 Distinct Id	(登录用用户ID，非登录用户用设备ID)
			String distinctId = String.valueOf(this.paramMap.get("DISTINCT_ID"));
			// 申请ID
			String applyId = String.valueOf(this.paramMap.get("APPLY_ID"));
			
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("applyId", applyId);
			TUserAudit tUserAudit = tUserAuditService.queryUserAuditDetails(map);
			
			Map<String, Object> properties = new HashMap<String, Object>();
			// 认证类型
		    properties.put("authent_type", String.valueOf(tUserAudit.getAuditType()));
		    // 认证申请时间
		    properties.put("authent_apply_date", tUserAudit.getAuthentApplyDate());
		    // 认证时长
		    properties.put("authent_time", String.valueOf(TrackUtils.getTimeDispose(tUserAudit.getAuthentApplyDate())));
		    // 认证用户id
		    properties.put("fzx_user_id", String.valueOf(tUserAudit.getFzxUserId()));
		    // 认证用户所属企业名称
		    properties.put("authent_name", String.valueOf(tUserAudit.getFranchiseeName()==null?"":tUserAudit.getFranchiseeName()));
		    
		    sa.track(distinctId, true, "user_authent_win", properties);
		    
		    // 程序结束前，停止 Sensors Analytics SDK 所有服务
		    sa.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-美货-认证成功用户[user_authent_win],出现异常，信息为："+e.getMessage()); 
		}
	}
	
	/**
	 * 方法说明：	认证失败用户
	 * 创建时间：	2018年6月14日 下午3:27:30
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月14日 下午3:27:30
	 */
	public void user_authent_Loser() {
		try {
			// 使用 DebugConsumer 初始化 SensorsAnalytics
			final SensorsAnalytics sa = TrackConfig.getFzxSensorsAnalytics(this.paramMap);
			
			// 用户的 Distinct Id	(登录用用户ID，非登录用户用设备ID)
			String distinctId = String.valueOf(this.paramMap.get("DISTINCT_ID"));
			
			// 申请ID
			String applyId = String.valueOf(this.paramMap.get("APPLY_ID"));
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("applyId", applyId);
			TUserAudit tUserAudit = tUserAuditService.queryUserAuditDetails(map);
			
			Map<String, Object> properties = new HashMap<String, Object>();
			// 认证类型
		    properties.put("authent_type", String.valueOf(tUserAudit.getAuditType()));
		    // 认证申请时间
		    properties.put("authent_apply_date", tUserAudit.getAuthentApplyDate());
		    // 认证时长
		    properties.put("authent_time", String.valueOf(TrackUtils.getTimeDispose(tUserAudit.getAuthentApplyDate())));
		    // 认证用户id
		    properties.put("fzx_user_id", String.valueOf(tUserAudit.getFzxUserId()));
		    // 认证用户所属企业名称
		    properties.put("authent_name", String.valueOf(tUserAudit.getFranchiseeName()==null?"":tUserAudit.getFranchiseeName()));
		    // 拒绝原因
		    properties.put("reject_remark", String.valueOf(tUserAudit.getRejectRemark()));
		    
		    sa.track(distinctId, true, "user_authent_Loser", properties);
		    
		    // 程序结束前，停止 Sensors Analytics SDK 所有服务
		    sa.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-美货-认证失败用户[user_authent_Loser],出现异常，信息为："+e.getMessage()); 
		}
	}
}

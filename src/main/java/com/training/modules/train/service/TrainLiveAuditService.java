package com.training.modules.train.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.oa.dao.OaNotifyDao;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainLiveAuditDao;
import com.training.modules.train.dao.TrainLivePlaybackDao;
import com.training.modules.train.dao.TrainLiveUserDao;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLiveOrder;
import com.training.modules.train.entity.TrainLivePlayback;
import com.training.modules.train.entity.TrainLiveRewardRecord;
import com.training.modules.train.entity.TrainLiveSku;

import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = false)
public class TrainLiveAuditService  extends CrudService<TrainLiveAuditDao,TrainLiveAudit>{
	
	@Autowired
	private TrainLiveAuditDao trainLiveAuditDao;
	@Autowired
	private TrainLivePlaybackDao trainLivePlaybackDao;
	@Autowired
	private OaNotifyDao oaNotifyDao;
	@Autowired
	private TrainLiveUserDao trainLiveUserDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLiveAudit> findLive(Page<TrainLiveAudit> page, TrainLiveAudit trainLiveAudit) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		//获取当前登录用户
		trainLiveAudit.getSqlMap().put("dsf", companyDateScope((String)userDao.findFranchiseeAuth(UserUtils.getUser()).get("companyIds"),UserUtils.getUser()));
		// 设置分页参数
		trainLiveAudit.setPage(page);
		// 执行分页查询
		page.setList(trainLiveAuditDao.findList(trainLiveAudit));
		return page;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLivePlayback> findback(Page<TrainLivePlayback> page, TrainLivePlayback trainLivePlayback) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLivePlayback.setPage(page);
		// 执行分页查询
		page.setList(trainLivePlaybackDao.findList(trainLivePlayback));
		return page;
	}
	
	
	/**
	 * 保存数据
	 * @param trainLiveAudit
	 * @return
	 */
	public int update(TrainLiveAudit trainLiveAudit){
		trainLiveAuditDao.deleteJurisdiction(trainLiveAudit.getId());	// 删除直播商家权限ID
		if(null != trainLiveAudit.getCompanyIds() || !"".equals(trainLiveAudit.getCompanyIds())){
			String idArray[] =trainLiveAudit.getCompanyIds().split(",");
			for(String id : idArray){
				trainLiveAuditDao.insertJurisdiction(trainLiveAudit.getId(),id);	// 插入直播商家权限ID
			}
		}
		return trainLiveAuditDao.update(trainLiveAudit);
	}
	/**
	 * 直播审核
	 * @return
	 */
	public List<TrainLiveAudit> selectOutLive(){
		return dao.selectOutLive();
	}
	/**
	 * 修改过期数据
	 * @param id
	 * @return
	 */
	public int updateLiveOut(String id){
		return dao.updateLiveOut(id);
	}
	
	/**
	 * 查询将要直播的数据
	 * @return
	 */
	public List<TrainLiveAudit> selectWantLive(){
		return dao.selectWantLive();
	}
	
	/**
	 * 查询购买的数据
	 * @return
	 */
	public List<String> selectLiveUser(String auditId){
		return trainLiveUserDao.selectLiveUser(auditId);
	}
	/**
	 * 查询预约的用户
	 * @param auditId
	 * @return
	 */
	public List<String> selectWantLiveUser(String auditId){
		return trainLiveUserDao.selectWantLiveUser(auditId);
	}
	/**
	 * 查询每天美耶直播预约的用户cid
	 * @param auditId
	 * @return
	 */
	public List<String> selectMtmyLiveUserClient(String auditId){
		return trainLiveUserDao.selectMtmyLiveUserClient(auditId);
	}
	
	/**
	 * 推送
	 * @param trainLiveAudit
	 */
	public  void pushMsg(String Status,List<String> list,Date begTime){

		// 统一全部用列推
		JSONObject jsonObject = new JSONObject();
		String push_url = ParametersFactory.getMtmyParamValues("push_url");

		logger.info("##### 直播推送 状态："+Status);
		String param="";
		String title="";
		String notify_type="";
		String content="";
		
		if("0".equals(Status)){				//直播审核失败
			title="直播审核失败";
			notify_type="0";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您申请的直播审核失败，请重新申请！";
			param=pushParam(title,notify_type,content,list);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.postTrainObject(param,push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("2".equals(Status)){		//审核通过消息推送
			title="直播审核通过";
			notify_type="0";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您申请的直播已经通过，请准时直播！";
			param=pushParam(title,notify_type,content,list);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.postTrainObject(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("8".equals(Status)){			// 8 直播将要开始 主播
			title="直播将要开始";
			notify_type="8";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您的直播将要在5分钟内开始，请准时直播！";
			param=pushParam(title,notify_type,content,list);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.postTrainObject(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("9".equals(Status)){			//  9 直播将要开始  购买者
			title="购买直播将要开始";
			notify_type="9";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您购买的直播将要在5分钟内开始，请准时观看！";
			param=pushParam(title,notify_type,content,list);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.postTrainObject(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}else if("10".equals(Status)){				//9 预约直播提醒  预约
			title="预约直播将要开始";
			notify_type="9";				//notify_type 0 审核失败  0 审核通过 8 直播将要开始  9 预约直播提醒
			content="您预约的直播将要在5分钟内开始，请准时观看！";
			param=pushParam(title,notify_type,content,list);
			WebUtils webUtils = new WebUtils();
			String result = webUtils.postTrainObject(param, push_url);
			jsonObject = JSONObject.fromObject(result);
			System.out.println(jsonObject.toString());
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
		}

		
	}
	
	/**
	 * 推送解析拼接字符串
	 * @param title
	 * @param notify_type
	 * @param content
	 * @param list
	 * @return
	 */
	public String pushParam(String title,String notify_type,String content,List<String> list){
		String notify_id=UUID.randomUUID().toString();
		String param="{";
		String push_type="1";		//1 是列表推送
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateStr=sdf.format(date);
		String cid="[";
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				cid=cid+"\""+oaNotifyDao.selectByuserId(list.get(i).toString())+"\",";
			}
			cid=cid.substring(0, cid.length()-1);
		}
		
		cid=cid+"]";
		param=param+"\"cid_list\":"+cid+"," ;
		param=param+"\"push_type\":"+push_type+",";
		param=param+"\"content\":{\"notify_id\":\""+notify_id+"\",\"title\":\""+title+"\",\"notify_type\":\""+notify_type+"\",\"start_time\":\""+dateStr+"\","
				+"\"end_time\":\""+dateStr+"\",\"push_time\":\""+dateStr+"\",\"content\":"+"\""+content+"\"}}";
	
		return param;
	}
	
	/**
	 * 分页查询sku配置
	 * @param page
	 * @param 
	 * @return
	 */
	public Page<TrainLiveSku> findSkuList(Page<TrainLiveSku> page, TrainLiveSku trainLiveSku) {
		// 设置分页参数
		trainLiveSku.setPage(page);
		// 执行分页查询
		page.setList(trainLiveAuditDao.findSkuList(trainLiveSku));
		return page;
	}
	
	/**
	 * 分页查询直播订单列表
	 * @param page
	 * @param 
	 * @return
	 */
	public Page<TrainLiveOrder> findOrderList(Page<TrainLiveOrder> page, TrainLiveOrder trainLiveOrder) {
		// 设置分页参数
		trainLiveOrder.setPage(page);
		// 执行分页查询
		page.setList(trainLiveAuditDao.findOrderList(trainLiveOrder));
		return page;
	}
	
	/**
	 * 根据trainLiveSkuId查询Sku配置
	 * @param trainLiveSkuId
	 * @return
	 */
	public TrainLiveSku findByTrainLiveSkuId(int trainLiveSkuId){
		return trainLiveAuditDao.findByTrainLiveSkuId(trainLiveSkuId);
	}
	
	/**
	 * 保存Sku配置
	 * @param trainLiveSku
	 */
	public void saveSku(TrainLiveSku trainLiveSku){
		trainLiveAuditDao.saveSku(trainLiveSku);
	}
	
	/**
	 * 根据直播id查找Sku配置价格
	 * @param id
	 * @return
	 */
	public double findSkuPrice(String id){
		return trainLiveAuditDao.findSkuPrice(id);
	}
	
	/**
	 * 查看云币贡献榜
	 * @param trainLiveRewardRecord
	 * @return
	 */
	public Page<TrainLiveRewardRecord> findCloudContribution(Page<TrainLiveRewardRecord> page,TrainLiveRewardRecord trainLiveRewardRecord){
		trainLiveRewardRecord.setPage(page);
		page.setList(trainLiveAuditDao.findCloudContribution(trainLiveRewardRecord));
		return page;
	}
	/**
	 * 云币贡献管理
	 * @param page
	 * @param trainLiveAudit
	 * @return
	 */
	public Page<TrainLiveAudit> liveIntegralsList(Page<TrainLiveAudit> page,TrainLiveAudit trainLiveAudit){
		trainLiveAudit.setPage(page);
		page.setList(trainLiveAuditDao.liveIntegralsList(trainLiveAudit));
		return page;
	}
	/**
	 * 商家总云币(临时版本)
	 * @return
	 */
	public int findOfficeIntegrals(){
		return dao.findOfficeIntegrals();
	}

	/**
	 * 添加直播推荐(只有一个能够推荐)
	 * @param trainLiveAudit
	 */
	public void addRecommend(TrainLiveAudit trainLiveAudit) {
		trainLiveAuditDao.updateRecommend();//修改全部直播为不推荐
		trainLiveAuditDao.addRecommend(trainLiveAudit);//给唯一的直播添加推荐
	}
}

package com.training.modules.quartz.tasks;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import com.training.common.mapper.JsonMapper;
import com.training.common.utils.BeanUtil;
import com.training.common.utils.IdGen;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.entity.EntrySopCastBean;
import com.training.modules.train.entity.LiveRoomidAndAuditid;
import com.training.modules.train.entity.QueryccBack;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.rooms;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.TrainLiveAuditService;
import com.training.modules.train.utils.EncryptLiveUtils;
import com.training.modules.train.utils.Utils;

import net.sf.json.JSONObject;
/**
 * 妃子校直播审核
 *	
 */
@Component
@SuppressWarnings("all")
public class LiveAudit extends CommonService{

	private Logger logger = Logger.getLogger(LiveAudit.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static TrainLiveAuditService trainLiveAuditService;
	private static EntryService entryService;
	
	static{
		trainLiveAuditService = (TrainLiveAuditService) BeanUtil.getBean("trainLiveAuditService");
		entryService = (EntryService) BeanUtil.getBean("entryService");
	}
	
	/**
	 * 妃子校直播审核
	 * @author yangyang 
	 */
	public void liveAudit(){
		logger.info("[work0],start,妃子校直播处理，开始时间："+df.format(new Date()));
		HttpServletRequest request=null;
		List<TrainLiveAudit> list=new ArrayList<TrainLiveAudit>();
		List<TrainLiveAudit> wantlist=new ArrayList<TrainLiveAudit>();
		List<String> liveUsers=new ArrayList<String>();
		List<String> yuUser=new ArrayList<String>();
		List<String> pushList=new ArrayList<String>();
		List<String> mtmyUsersClient = new ArrayList<String>();
		Map<String, Object> messageMap = new HashMap<String, Object>();
		Map<String, Object> putMap = new HashMap<String, Object>();
		int yueNum=0;
		int gouNum=0;
		int jiaNum=0;
		int mtmyNum=0;
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("trainLiveAudit");
		taskLog.setStartDate(startDate);
		
		try {
			//查询过期的数据的 自动审核失败状态
			list=  trainLiveAuditService.selectOutLive();					
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					TrainLiveAudit liveAudit=list.get(i);
					pushList.add(liveAudit.getUserId());
					trainLiveAuditService.updateLiveOut(liveAudit.getId());
				}
				//直播过期没有审核通过推送提醒
				trainLiveAuditService.pushMsg("0",pushList,new Date());				
			}
			
			//清空list
			pushList.clear();	
			wantlist=trainLiveAuditService.selectWantLive();
			//直播将要开始  给主播提醒
			if(wantlist.size()>0){									
				for (int i = 0; i < wantlist.size(); i++) {
					TrainLiveAudit liveAudit=wantlist.get(i);
					pushList.add(liveAudit.getUserId());
					jiaNum++;
				}
				trainLiveAuditService.pushMsg("8",pushList,new Date());	
			}
			
			if(wantlist.size()>0){
				for (int i = 0; i < wantlist.size(); i++) {
					TrainLiveAudit liveAudit=wantlist.get(i);
					//直播是付费直播  去查询购买了直播的用户推送
					if(liveAudit.getIsPay()==2){		
						liveUsers=trainLiveAuditService.selectLiveUser(liveAudit.getId());
						if(liveUsers.size()>0){
							trainLiveAuditService.pushMsg("9",liveUsers,new Date());	
							gouNum=gouNum+liveUsers.size();
							liveUsers.clear();
						}
						//直播免费的去查询有没有预约提醒的
					}else if(liveAudit.getIsPay()==1){	
						yuUser=trainLiveAuditService.selectWantLiveUser(liveAudit.getId());
						if(yuUser.size()>0){
							trainLiveAuditService.pushMsg("10",yuUser,new Date());
							yueNum=yueNum+yuUser.size();
							yuUser.clear(); 
						}
					//线上收费直播价格为0的预约提醒
					}else if(liveAudit.getIsPay()==3 && trainLiveAuditService.findSkuPrice(liveAudit.getId()) == 0){
						yuUser=trainLiveAuditService.selectWantLiveUser(liveAudit.getId());
						if(yuUser.size()>0){
							trainLiveAuditService.pushMsg("10",yuUser,new Date());
							yueNum=yueNum+yuUser.size();
							yuUser.clear(); 
						}
					}
					
					//每天美耶直播预约推送
					mtmyUsersClient = trainLiveAuditService.selectMtmyLiveUserClient(liveAudit.getId());
					if(mtmyUsersClient.size() > 0){
						messageMap.put("title","您预约的"+liveAudit.getName()+"直播间还有5分钟就要开始啦，快去围观～");
						messageMap.put("notify_type", "4");
						messageMap.put("push_type",3);
						messageMap.put("push_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						messageMap.put("content","");
						messageMap.put("notify_id", IdGen.uuid());
						
						putMap.put("content", messageMap);
						putMap.put("cid_list", mtmyUsersClient);
						
						GetTUtil.list(JSONObject.fromObject(JsonMapper.toJsonString(putMap)));
						
						mtmyNum = mtmyNum + mtmyUsersClient.size();
						mtmyUsersClient.clear();
					}
				}
			}

			taskLog.setJobDescription("[work],妃子校直播审核过期个数："+list.size()+"直播将要开始的个数："+wantlist.size()
										+"将要直播的用户推送数："+jiaNum+",购买的直播个数："+gouNum+",预约直播个数："+yueNum+",每天美耶预约的个数："+mtmyNum);
			//任务状态
			taskLog.setStatus(0);
			
			
			/**
			 * 1.扫描审核通过且没开直播的数据
			 * 2.扫描打开直播间没有正常关闭的数据
			 * 3.扫描直播的线下支付是否过期
			 * @author fengfeng
			 * @version 2017年2月23日
			 */
			logger.info("[直播定时任务任务]，操作过期的数据,开始时间：" + df.format(new Date()));
			
			//获取没开直播的过期规则
			int value = Integer.parseInt(ParametersFactory.getTrainsParamValues("live_outTime"));
			//获取没正常结束直播的过期规则
			int open_live_expiration_time = Integer.parseInt(ParametersFactory.getTrainsParamValues("open_live_expiration_time"));
			
			//将没开审核通过且没开直播的直播间作废
			entryService.upauditstatus(value);
			
			SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			//获取直播开启状态的所有数据
			List<LiveRoomidAndAuditid> noticelives = entryService.queryRoomidandAuditid(open_live_expiration_time);
			String roomids = "";
			//将所有直播的roomid拼在一起用逗号隔开
			for (int i = 0; i < noticelives.size(); i++) {
				roomids = roomids + noticelives.get(i).getRoomid() + ",";
			}

			List<rooms> recommendArr = null;
			//判断roomid是否为空值
			if (roomids != ""){
				//截取最后一个逗号
				roomids = roomids.substring(0, roomids.length() - 1);
				//请求CC接口，参数roomid
				recommendArr = EncryptLiveUtils.querycc(roomids);
			}

			//定义一个集合
			//List<LiveRoomidAndAuditid> auditid = new ArrayList<LiveRoomidAndAuditid>();
			//判断数据库取回的数据和CC放回的数据都不能为空
			Map<String,Object> m = new HashMap<String,Object>();
			if (noticelives != null && recommendArr != null) {
				
				Map<String,rooms> map = new HashMap<String,rooms>();
				for (int i = 0; i < recommendArr.size(); i++) {
					map.put(recommendArr.get(i).getRoomId(), recommendArr.get(i));
				}
				//循环数据库取回的数据集合
				for (int h = 0; h < noticelives.size(); h++) {
					//循环CC放回的数据集合
					if(map.containsKey(noticelives.get(h).getRoomid())){
						rooms room = map.get(noticelives.get(h).getRoomid());
						//判断直播间是否是开启状态
						if (noticelives.get(h).getRoomid().equals(room.getRoomId()) && room.getLiveStatus() == 0) {
							// noticelives.get(h).getAuditid() + ",";
							//获取直播间处于关闭状态的直播
							//auditid.add(noticelives.get(h));
							entryService.updateauditstatus(noticelives.get(h).getAuditid());
							
							//计算直播浏览数量和浏览人数
							Map<String, Object> maps = new HashMap<String,Object>();
							maps.put("audit_id", noticelives.get(h).getAuditid());
							//获取直播浏览次数
							String browsetime = redisClientTemplate.get("browsetime_"+noticelives.get(h).getAuditid());
							maps.put("browsetime", browsetime == null ? 0 : Integer.parseInt(browsetime));
							//获取直播浏览人数
							Long scard = redisClientTemplate.scard("browsenum_"+noticelives.get(h).getAuditid());
							maps.put("browsenum", scard == null ? 0 : scard);
							//获取直播点赞次数
							String livelike = redisClientTemplate.get("livelike_"+noticelives.get(h).getAuditid());
							maps.put("livelike", livelike == null ? 0 : Integer.parseInt(livelike));
							//将数据保存在对应的直播信息表中
							entryService.updatebrowsenumber(maps);
							//清除暂存数据
							redisClientTemplate.del("browsetime_"+noticelives.get(h).getAuditid());
							redisClientTemplate.del("browsenum_"+noticelives.get(h).getAuditid());
							redisClientTemplate.del("livelike_"+noticelives.get(h).getAuditid());
							
							RedisLock redisLock = new RedisLock(redisClientTemplate,"mtmy_id_"+noticelives.get(h).getMtmyUserId());
							redisLock.lock();
							String num = redisClientTemplate.get(noticelives.get(h).getAuditid() + "_"+ noticelives.get(h).getMtmyUserId());
							int integrals = num == null ? 0 : Integer.parseInt(num);
							//获取直播后台设置的收益比例
							//double proportion = entrymapper.queryproportion(auditId);
							double x = integrals * noticelives.get(h).getProportion();
							//平台获得云币
							int round = Utils.round(x, 0, BigDecimal.ROUND_HALF_UP);
							//主播获得云币
							int proportions = integrals - round;
							//给平台加上比例云币
							m.put("office_id",noticelives.get(h).getCompanyId());
							m.put("integral_earnings",round);
							//将平台获得云币加到平台账户
							entryService.addofficeaccount(m);
							
							m.put("user_id", 0000000);
							m.put("integral_type", 0);
							m.put("integral_source", 1);
							m.put("action_type", 5);
							m.put("integral", round);
							m.put("remark", "直播编号：" + noticelives.get(h).getAuditid()+ "云币分成平台获得：" + round);
							
							entryService.cloudcoinOrderlog(m);
							//判断缓存中是否有主播的数据
							Boolean exists = redisClientTemplate.exists("mtmy_id_" + noticelives.get(h).getMtmyUserId());
							if (exists) {
								//给主播加上云币
								redisClientTemplate.incrBy("mtmy_id_"+ noticelives.get(h).getMtmyUserId(), proportions);
								m.put("user_id", noticelives.get(h).getMtmyUserId());
								m.put("integral", proportions);
								m.put("remark", "直播编号：" + noticelives.get(h).getAuditid()+ "云币分成主播获得：" + proportions);
								entryService.cloudcoinOrderlog(m);
							} else {
								//将主播的账户加进redis内存
								int integralsnum = entryService.queryintegralsnum(noticelives.get(h).getMtmyUserId());
								redisClientTemplate.set("mtmy_id_"+noticelives.get(h).getMtmyUserId(),integralsnum+"");
								redisClientTemplate.hset("MTMY_ID",noticelives.get(h).getMtmyUserId()+"",noticelives.get(h).getMtmyUserId()+"");
								//给主播加上云币
								redisClientTemplate.incrBy("mtmy_id_"+ noticelives.get(h).getMtmyUserId(), proportions);
								m.put("user_id", noticelives.get(h).getMtmyUserId());
								m.put("integral", proportions);
								m.put("remark", "直播编号：" + noticelives.get(h).getAuditid()+ "云币分成主播获得：" + proportions);
								entryService.cloudcoinOrderlog(m);
							}
							redisClientTemplate.del(noticelives.get(h).getAuditid() + "_"+ noticelives.get(h).getMtmyUserId());
							redisLock.unlock();
							//将云币划分结果信息入库
							m.put("total_reward", num);//总收益
							m.put("platform_earnings", round);//平台收益
							m.put("user_earnings", proportions);//用户收益
							m.put("auditId", noticelives.get(h).getAuditid());//申请id
							entryService.addproportionsmessage(m);
						}
					}
				}
			}
			//循环直播间处于关闭状态的直播
			/*for (int i = 0; i < auditid.size(); i++) {
				//判断直播间是否超时
				int count = entryService.queryCount(open_live_expiration_time,auditid.get(i).getAuditid());
				//直播间超时将去请求CC接口查看是否有回放
				if (count == 1) {
					String endtime = smp.format(new Date());
					String roomid = auditid.get(i).getRoomid();
					String begintime = auditid.get(i).getBengtime();
					
					//请求CC接口，有回放就插到回放表，状态为3（已完成），没有回放就直接作废，状态为5（作废）
					Object object = EncryptLiveUtils.queryccback(roomid,begintime, endtime);
					int auditids = auditid.get(i).getAuditid();
					if (object.equals("FAIL")) {
						
						// 没有回放的数据直接作废（状态5）
						entryService.uplivestatus(open_live_expiration_time,auditid.get(i).getAuditid());
					} else {
						
						//获取申请直播信息数据
						EntrySopCastBean entrysopcastbean = entryService.getliveendbyid(auditids);
						//将CC请求回来的数据用实体类接收
						List<QueryccBack> lists = (List<QueryccBack>) object;
						Map<String, Object> m = new HashMap<String, Object>();
						//循环将CC接口返回的数据插到数据库
						for (int j = 0; j < lists.size(); j++) {
							m.put("playbackId", lists.get(j).getId());
							m.put("bengtime", lists.get(j).getStartTime());
							//判断当前数据是第一条就去系统当前时间作为结束时间
							if (j == 0) {
								m.put("overtime", endtime);
							} else {
								m.put("overtime", lists.get(j).getEndTime());
							}
							if (entrysopcastbean != null) {
								m.put("auditId", entrysopcastbean.getAuditId());
								m.put("name", entrysopcastbean.getTitle());
								m.put("desc", entrysopcastbean.getDesc());
								m.put("imgurl", entrysopcastbean.getImgurl());
								m.put("playpass",entrysopcastbean.getPlaypass());
								m.put("user_id", entrysopcastbean.getUserId());
								m.put("liveId", entrysopcastbean.getLiveId());
							}
							// 将回放的数据插到回放表
							entryService.LiveEnd(m);

						}
						//将数据的状态改为3（已完成）
						entryService.alterstatus(auditids);
						// entryService.liveendtime(auditids);
					}
				}
			}*/
			//扫描线下支付表，将过期的数据作废
			entryService.operatebackrecord();

			
		} catch (Exception e) {
			logger.error("#####【定时任务trainLiveAudit】妃子校审核直播,出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "抢购活动定时器", e);
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,妃子校审核直播,结束时间："+df.format(new Date()));
	}
}

package com.training.modules.sys.utils;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.common.utils.SpringContextHolder;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeLog;

import net.sf.json.JSONObject;

/**
 * 
 * @className ThreadUtils
 * @description TODO 同步机构到报货
 * @author chenbing
 * @date 2018年2月1日 兵子
 *
 *
 */
public class OfficeThreadUtils{
	
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
	private static final Log logger = LogFactory.getLog(OfficeThreadUtils.class);
	private static int num = 0;
	private static final int sum = 5;
	private static final int DATE_TIME = 5000;	//如果同步不成功等待5分钟之后再进行同步
	
	public static void equalBH(Office obj,Office oldoffice,OfficeLog officeLog){
		new EqualBH(obj, oldoffice, officeLog).start();
		
		
	}
	
	public static class EqualBH extends Thread {
		private String weburl;
		private List<Office> oList;
		private JSONObject jsonObject;
		private Office office;
		private String exdata;
		private Office oldoffice;
		private OfficeLog officeLog;

		public EqualBH(Office office,Office oldoffice,OfficeLog officeLog) {
			//super(equalBH.class.getSimpleName());
			List<Office> oList = officeDao.finAllByPId(office);
			this.officeLog = officeLog;
			this.oList =  oList;
			this.office = office;
			this.oldoffice = oldoffice;
			
		}
		
		
		
		@Override
		public void run() {
			if (num < sum) {
				this.weburl = ParametersFactory.getMtmyParamValues("modifyToOffice");
			if (!"-1".equals(weburl)) {
				if (!office.getParentId().equals(oldoffice.getParentId())) {
						if (oList != null && oList.size() > 0) {
							for (Office off : oList) {
								String parpm = "{\"office_id\":\""+off.getId()+"\",\"office_name\":\""+off.getName()+"\","
										+ "\"franchisee_id\":"+off.getFranchisee().getId()+","
										+ "\"office_pid\":\""+off.getParent().getId()+"\",\"office_pids\":\""+off.getParentIds()+"\"}";
								String url=weburl;
								String result = WebUtils.postCSObject(parpm, url);
								jsonObject = JSONObject.fromObject(result);
								if ("200".equals(jsonObject.get("result"))) {
									logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
								}else{
									this.exdata = this.exdata+","+off.getId();
									officeLog.setContent("机构同步报货");
									officeLog.setOfficeId(off.getId());
									officeLog.setType(4);
									officeLog.setUpdateBy(UserUtils.getUser());
									officeLog.setRemark(this.exdata);
									ExSleep(off,oldoffice,officeLog);
									break;
								}
							}
						}
				}else{
					String parpm = "{\"office_id\":\""+this.office.getId()+"\",\"office_name\":\""+this.office.getName()+"\","
							+ "\"franchisee_id\":"+this.office.getFranchisee().getId()+","
							+ "\"office_pid\":\""+this.office.getParent().getId()+"\",\"office_pids\":\""+this.office.getParentIds()+"\"}";
					String url=weburl;
					String result = WebUtils.postCSObject(parpm, url);
					jsonObject = JSONObject.fromObject(result);
					if ("200".equals(jsonObject.get("result"))) {
						logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
					}else{
						logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
						officeLog.setContent("机构同步报货");
						officeLog.setOfficeId(office.getId());
						officeLog.setType(4);
						officeLog.setUpdateBy(UserUtils.getUser());
						officeLog.setRemark(office.getId());
						officeDao.saveOfficeLog(officeLog);
					}
				}
				}else{
					ExSleep(office,oldoffice,officeLog);
				}
			}else{
				logger.info("##### 无法连接报货系统，停止连接");
				officeLog.setContent("机构同步报货");
				officeLog.setOfficeId(this.office.getId());
				officeLog.setType(4);
				officeLog.setUpdateBy(UserUtils.getUser());
				officeLog.setRemark("无法获得报货路径：规则key=modifyToOff");
				officeDao.saveOfficeLog(officeLog);
			}
		}
	}
	
	public static void ExSleep(Office office,Office oldoffice,OfficeLog officeLog){
			try {
				num = num + 1;
				new EqualBH(office,oldoffice,officeLog).sleep(DATE_TIME);
				new EqualBH(office, oldoffice, officeLog).start();
			} catch (InterruptedException e) {
				logger.error("修改机构时同步报货出现异常，异常信息为：异步同步机构到报货系统不成功，机构id为："+office.getId());
				officeDao.saveOfficeLog(officeLog);
			}
	}
}


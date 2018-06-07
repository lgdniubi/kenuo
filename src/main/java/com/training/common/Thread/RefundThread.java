package com.training.common.Thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.utils.ParametersFactory;

import net.sf.json.JSONObject;

public class RefundThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(RefundThread.class);
	private String office_id;
	private String order_id;
	private double amount;
	private String user_id;
	public RefundThread(String office_id,String order_id,double amount,String user_id) {
		this.office_id = office_id;
		this.order_id = order_id;
		this.amount = amount;
		this.user_id = user_id;
	}
	@Override
	public void run() {
		try {
			//将信用额度还款到账户
			Thread.sleep(1000);
			JSONObject jsonO = new JSONObject();
			jsonO.put("office_id", office_id);
			jsonO.put("fund", 0);
			jsonO.put("used_limit", amount);
			jsonO.put("order_id", order_id);
			jsonO.put("client", "bm");
			jsonO.put("action_type", 2);
			JSONObject json = WebUtils.postfzx(jsonO, ParametersFactory.getTrainsParamValues("refund"));
			logger.info("确认入账还款返回结果："+json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

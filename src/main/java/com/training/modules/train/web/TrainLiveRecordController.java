package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.train.entity.TrainLiveRecord;
import com.training.modules.train.service.TrainLiveRecordService;

/**
 * 云币充值记录和佣金兑换云币记录Controller
 * @author xiaoye 2017年3月15日
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/record")
public class TrainLiveRecordController extends BaseController{
	
	@Autowired
	private TrainLiveRecordService trainLiveRecordService;
	
	/**
	 * 云币充值记录
	 * @param trainLiveRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="payRecordList")
	public String payRecordList(TrainLiveRecord trainLiveRecord,HttpServletRequest request,HttpServletResponse response,Model model){
		Page<TrainLiveRecord> page = trainLiveRecordService.selectPayRecord(new Page<TrainLiveRecord>(request, response), trainLiveRecord);
		model.addAttribute("page", page);
		return "modules/train/payRecordList";
	}
	
	/**
	 * 佣金兑换云币记录
	 * @param trainLiveRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="exchangeRecordList")
	public String exchangeRecordList(TrainLiveRecord trainLiveRecord,HttpServletRequest request,HttpServletResponse response,Model model){
		Page<TrainLiveRecord> page = trainLiveRecordService.selectExchangeRecord(new Page<TrainLiveRecord>(request, response), trainLiveRecord);
		model.addAttribute("page", page);
		return "modules/train/exchangeRecordList";
	}
}

package com.training.modules.train.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.training.common.persistence.Page;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.entity.ContractInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class ContractInfoService {

	
	
	public Page<ContractInfo> findPage(Page<ContractInfo> page, ContractInfo contractInfo){
		JSONObject jsonO = new JSONObject();
		jsonO.put("page", page.getPageNo());
		jsonO.put("size", page.getPageSize());
		if(StringUtils.isNotBlank(contractInfo.getOffice_id()))
			jsonO.put("office_id", contractInfo.getOffice_id());
		if(StringUtils.isNotBlank(contractInfo.getStatus()))
			jsonO.put("status", contractInfo.getStatus());
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("contract_list_path"));
		
		List<ContractInfo> list = JSONArray.toList(json.getJSONObject("data").getJSONArray("list"), new ContractInfo(),new JsonConfig());
		page.setCount(json.getJSONObject("data").getLong("count"));
		page.setList(list);
		contractInfo.setPage(page);
		return page;
	}
	
	public JSONObject queryContractInfoDetail(String office_id){
		JSONObject jsonO = new JSONObject();
		jsonO.put("office_id", office_id);
		
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("queryContractInfoAudit"));
		
		return json.getJSONObject("data");
	}
	
	public void auditContractInfo(ContractInfo info){
		JSONObject jsonO = new JSONObject();
		jsonO.put("office_id", info.getOffice_id());
		jsonO.put("status", info.getStatus());
		jsonO.put("remarks", info.getRemarks());
		WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("contract_status_path"));
	}
}

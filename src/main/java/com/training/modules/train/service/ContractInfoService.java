package com.training.modules.train.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("queryContractInfoAuditList"));
		
		List<ContractInfo> list = JSONArray.toList(json.getJSONObject("data").getJSONArray("list"), new ContractInfo(),new JsonConfig());
		/*List<ContractInfo> list = new ArrayList<>();
		JSONArray arr = json.getJSONObject("data").getJSONArray("list");
		for(Object o : arr){
			JSONObject info = (JSONObject) o;
			ContractInfo inf = (ContractInfo) JSONObject.toBean(info, new ContractInfo(), new JsonConfig());
			list.add(inf);
		}*/
		
		page.setCount(json.getJSONObject("data").getLong("count"));
		page.setList(list);
		contractInfo.setPage(page);
		return page;
	}
}

package com.training.modules.train.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.training.common.persistence.Page;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Uvo;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ContractInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class ContractInfoService {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	
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
	
	public Page<ContractInfo> findSignedPage(Page<ContractInfo> page, ContractInfo contractInfo){
		JSONObject jsonO = new JSONObject();
		jsonO.put("page", page.getPageNo());
		jsonO.put("size", page.getPageSize());
		if(StringUtils.isNotBlank(contractInfo.getOffice_id()))
			jsonO.put("office_id", contractInfo.getOffice_id());
		
		jsonO.put("status", 1);
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("queryContractInfoList"));
		List<ContractInfo> list = new ArrayList<>();
		if(!(json.get("data") instanceof JSONNull)){
			list = JSONArray.toList(json.getJSONObject("data").getJSONArray("list"), new ContractInfo(),new JsonConfig());
			page.setCount(json.getJSONObject("data").getLong("count"));
		}
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
	
	/**
	 * 查询已签协议详情
	 * @param office_id
	 * @return
	 */
	public JSONObject querySignedContractInfoDetail(String office_id){
		JSONObject jsonO = new JSONObject();
		jsonO.put("office_id", office_id);
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("queryContractInfo"));
		
		return json.getJSONObject("data");
	}
	
	public void auditContractInfo(ContractInfo info) throws Exception{
		JSONObject j = new JSONObject();
		JSONObject jsonO = new JSONObject();
		jsonO.put("office_id", info.getOffice_id());
		jsonO.put("status", info.getStatus());
		jsonO.put("remarks", info.getRemarks());
		jsonO.put("update_user", UserUtils.getUser().getId());
		JSONObject json = WebUtils.postCS(jsonO, ParametersFactory.getTrainsParamValues("contract_status_path"));
		
		if(!"200".equals(json.getString("result"))){
			throw new Exception("RPC调用失败");
		}
		
		if("2".equals(jsonO.get("status"))){
			
			j.put("office", officeService.queryFvo(info.getOffice_id()));
			JSONObject j0 = new JSONObject();
			j0.put("office_id", info.getOffice_id());
			JSONObject js = WebUtils.postCS(j0, ParametersFactory.getTrainsParamValues("contract_data_path"));
			if("200".equals(js.getString("result")) && js.get("data") != null){
				
				if(StringUtils.isNotBlank(js.getJSONObject("data").getString("cargo_userid"))){
					Uvo vo = systemService.findUvo(js.getJSONObject("data").getString("cargo_userid"));
					j.put("cargo", vo);
				}
				if(StringUtils.isNotBlank(js.getJSONObject("data").getString("audit_userid"))){
					Uvo vo = systemService.findUvo(js.getJSONObject("data").getString("audit_userid"));
					j.put("audit", vo);
				}
				if(StringUtils.isNotBlank(js.getJSONObject("data").getString("proxy_userid"))){
					Uvo vo = systemService.findUvo(js.getJSONObject("data").getString("proxy_userid"));
					j.put("proxy", vo);
				}
			}
			JSONObject jso = WebUtils.postCS(j, ParametersFactory.getTrainsParamValues("syncAccountAndRole"));
		}
		
		
	}
}

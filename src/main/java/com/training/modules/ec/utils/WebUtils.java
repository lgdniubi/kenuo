package com.training.modules.ec.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;
public class WebUtils {
	public String webUtilsMain(String parpm,String url){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		       
		JSONObject jsonObj = JSONObject.fromObject(parpm);
		//jsonObj.put("client_side", "wap");
		HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
		String result = restTemplate.postForObject(url, formEntity, String.class);
		
		System.out.println(result);
		
		return result;
	}
}


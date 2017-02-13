package com.training.modules.ec.utils;

import java.security.MessageDigest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.training.modules.sys.utils.ParametersFactory;

import net.sf.json.JSONException;
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
	
	
	/**
	 * json 传参调用加密
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 传参调用方法
	 * @param param
	 * @param url
	 * @return
	 */
	public static String postObject(String param, String url){
		String result = "";
		try {
			String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			JSONObject jsonObj = JSONObject.fromObject(param);
			jsonObj.put("version", connector);
			String json = jsonObj.toString();
			String sign = MD5("mtmy"+json+"mtmy");
			
			String paramter = "{'sign':'"+sign+"' , 'jsonStr':'mtmy"+json+"'}";
			HttpEntity<String> entity = new HttpEntity<String>(paramter, headers);
			result = restTemplate.postForObject(url, entity, String.class);

		} catch (JSONException e) {
		//	logger.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}

	
}


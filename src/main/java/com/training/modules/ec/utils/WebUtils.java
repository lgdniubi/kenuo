package com.training.modules.ec.utils;

import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.training.modules.sys.utils.BugLogUtils;
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
	
	public static String webUtilsPostObject(String parpm,String url){
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
	 * mtmy传参调用方法
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

	/**
	 * trains传参调用方法
	 * @param param
	 * @param url
	 * @return
	 */
	public static String postTrainObject(String param, String url){
		String result = "";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
			
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			       
			JSONObject jsonObj = JSONObject.fromObject(param);
			//jsonObj.put("client_side", "wap");
			String json=jsonObj.toString();
			String sign = MD5("train"+json+"train");
			System.err.println(sign);
			System.err.println("train"+json+"train");

			String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+json+"'}";
			HttpEntity<String> entity = new HttpEntity<String>(paramter, headers);
			result = restTemplate.postForObject(url, entity, String.class);
			
			System.out.println(result);
			

		} catch (JSONException e) {
		//	logger.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}
	/**
	 * 报货接口
	 * @param param
	 * @param url
	 * @return
	 */
	public static String postCSObject(String param, String url){
		String result = "";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
			
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			       
			JSONObject jsonObj = JSONObject.fromObject(param);
			//jsonObj.put("client_side", "wap");
			String json=jsonObj.toString();
			String sign = MD5("cs"+json+"cs");
			System.err.println(sign);
			System.err.println("cs"+json+"cs");

			String paramter = "{'sign':'"+sign+"' , 'jsonStr':'cs"+json+"'}";
			HttpEntity<String> entity = new HttpEntity<String>(paramter, headers);
			result = restTemplate.postForObject(url, entity, String.class);
			
			System.out.println(result);
			

		} catch (JSONException e) {
		//	logger.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 自媒体传参调用方法
	 * @param param
	 * @param url
	 * @return
	 */
	public static String postMediaObject(String param, String url){
		String result = "";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			JSONObject jsonObj = JSONObject.fromObject(param);
			String json = jsonObj.toString();
			String sign = MD5("media"+json+"media");
			
			String paramter = "{'sign':'"+sign+"' , 'jsonStr':'media"+json+"'}";
			HttpEntity<String> entity = new HttpEntity<String>(paramter, headers);
			result = restTemplate.postForObject(url, entity, String.class);

		} catch (JSONException e) {
		//	logger.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 请求接口共用方法
	 * @param type  1、每天美耶  2、妃子校  3、报货  4、自媒体    --其他,不需要加密
	 * @param title	标题
	 * @param parpm	请求参数
	 * @param url	请求地址
	 * @return
	 */
	public static String webUtilsPostObject(HttpServletRequest request, String title,int type,String parpm,String url){
		String result = "";
		String typeName = "";

		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			MediaType contentType = MediaType.parseMediaType("application/json;charset=UTF-8");
			
			headers.setContentType(contentType);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			       
			JSONObject jsonObj = JSONObject.fromObject(parpm);
			String paramter = null,json = null,sign = null;
			switch (type) {
				case 1:	// 每天美耶
					
					typeName = "每天美耶";
					String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
					jsonObj.put("version", connector);
					json = jsonObj.toString();
					sign = MD5("mtmy"+json+"mtmy");
					paramter = "{'sign':'"+sign+"' , 'jsonStr':'mtmy"+json+"'}";
					
					break;
				case 2:	// 妃子校
					
					typeName = "妃子校";
					json=jsonObj.toString();
					sign = MD5("train"+json+"train");
					paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+json+"'}";
					
					break;
				case 3:	// 报货
					
					typeName = "报货";
					json=jsonObj.toString();
					sign = MD5("cs"+json+"cs");
					paramter = "{'sign':'"+sign+"' , 'jsonStr':'cs"+json+"'}";
					
					break;
				case 4:	// 自媒体
					
					typeName = "自媒体";
					json = jsonObj.toString();
					sign = MD5("media"+json+"media");
					paramter = "{'sign':'"+sign+"' , 'jsonStr':'media"+json+"'}";
					
					break;
	
				default: // 其他,不需要加密
					
					typeName = "其他,不需要加密";
					paramter = jsonObj.toString();
					
					break;
			}
			
			System.out.println("#######调用接口：调用类型"+typeName+"_"+type+"_"+title+","+"请求地址"+url+",请求参数："+paramter);
			
			HttpEntity<String> entity = new HttpEntity<String>(paramter, headers);
			result = restTemplate.postForObject(url, entity, String.class);
			
			System.out.println("#######调用接口返回参数：调用类型"+typeName+"_"+type+"_"+title+","+"请求地址"+url+",返回数据："+result);

		} catch (Exception e) {
			System.out.println("#######调用接口错误日志：调用类型"+typeName+"_"+type+"_"+title+","+"请求地址"+url+",出现异常："+e.getMessage());
			BugLogUtils.saveBugLog(request, "调用接口错误日志", e);
		}finally {
			
		}
		return result;
	}
}


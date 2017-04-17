package com.training.modules.train.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;

import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.training.modules.train.entity.QueryccBack;
import com.training.modules.train.entity.rooms;

public class EncryptLiveUtils {
	
	
	
	
	public static List<rooms> querycc(String roomids) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("roomids", URLEncoder.encode(roomids, "utf-8"));
		map.put("userid", URLEncoder.encode("E8F7E756412DC768", "utf-8"));
		String API_KEY = URLEncoder.encode("K4MV4Mv4Q90FaEEQYclkz0XJIqEZf5rK", "utf-8");
		Date date = new Date();
		long time = date.getTime();
		String qString = createHashedQueryString(map,time, API_KEY);
		String reString = CreateLiveGet("http://api.csslcloud.net/api/rooms/publishing", qString);
		JSONObject jsons = JSONObject.fromObject(reString);
		//System.out.println(jsons);
		JSONArray hotJsonArr = jsons.getJSONArray("rooms");
		List<rooms> recommendArr = (List<rooms>) JSONArray.toCollection(hotJsonArr,rooms.class);
		return recommendArr;
	}
	
	
	
	public static String createHashedQueryString(Map<String, String> queryMap,long time, String salt) {

		Map<String, String> map = new TreeMap<String, String>(queryMap);
		String qs = createQueryString(map); // 生成queryString方法可自己编写
		if (qs == null){
			return null;
		}
		qs = qs.substring(0, qs.lastIndexOf("&"));
		time = time / 1000;
		// time=URLEncoder.encode(time,"utf-8")
		String hash = MD5Util.getMD5Format(String.format("%s&time=%d&salt=%s",qs, time, salt));
		hash = hash.toUpperCase();
		String thqs = String.format("%s&time=%d&hash=%s", qs, time, hash);
		System.out.println(thqs);
		return thqs;
	}

	/**
	 * map 排序
	 * 
	 * @param map
	 * @return
	 */

	private static String createQueryString(Map<String, String> map) {
		String qString = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			qString = qString + entry.getKey() + "=" + entry.getValue() + "&";
		}
		return qString;
	}

	/**
	 * http请求cc视频
	 * 
	 * @param httpUrl
	 * @param qString
	 * @return
	 * @throws IOException
	 */
	public static String CreateLiveGet(String httpUrl, String qString)
			throws IOException {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		String getURL = httpUrl + "?" + qString;// URLEncoder.encode(qString,"utf-8");
		System.out.println(getURL);
		URL getUrl = new URL(getURL);
		// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
		// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
		// 服务器
		connection.connect();
		// 取得输入流，并使用Reader读取
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String lines = reader.readLine();
		while (reader.readLine() != null) {
			System.out.println(lines);
		}
		reader.close();
		// 断开连接
		connection.disconnect();

		return lines;
	}

	
	public static Object queryccback(String roomid,String starttime,String endtime) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("roomid", URLEncoder.encode(roomid, "utf-8"));
		map.put("userid", URLEncoder.encode("E8F7E756412DC768", "utf-8"));
		map.put("starttime", URLEncoder.encode(starttime, "utf-8"));
		map.put("endtime", URLEncoder.encode(endtime, "utf-8"));
		//map.put("pagenum", URLEncoder.encode("10", "utf-8"));
		String API_KEY = URLEncoder.encode("K4MV4Mv4Q90FaEEQYclkz0XJIqEZf5rK", "utf-8");
		Date date = new Date();
		long time = date.getTime();
		String qString = createHashedQueryString(map,time, API_KEY);
		String reString = CreateLiveGet("http://api.csslcloud.net/api/live/info", qString);
		JSONObject jsons = JSONObject.fromObject(reString);
		String flag = jsons.getString("result");
		int count = 0;
		if(flag.equals("OK")){
			count = jsons.getInt("count");
		}
		if(flag.equals("OK") && count > 0){
			JSONArray hotJsonArr = jsons.getJSONArray("lives");
			List<QueryccBack> recommendArr = (List<QueryccBack>) JSONArray.toCollection(hotJsonArr,QueryccBack.class);
			return recommendArr;
		}else{
			return "FAIL";
		}
		
	}
	//"AB6987FE88D0A2D49C33DC5901307461"
	//"2016-11-25 11:30:00"
	//"2016-11-26 16:10:00"
	
	
	/*public static void main(String[] args) throws Exception {
		Object object = queryccback("AB6987FE88D0A2D49C33DC5901307461","2016-12-25 17:55:00","2016-12-26 10:05:00");
		System.out.println(object);
		if(object.equals("FAIL")){
			
		}else{
			List<QueryccBack> list = (List<QueryccBack>) object;
			System.out.println(list.size());
			for(int i=0;i<list.size();i++){
					System.out.println(list.get(i).getId());
					System.out.println(list.get(i).getEndTime());
					System.out.println(list.get(i).getStartTime());
			}
		}
	}*/
	
	public static void main(String[] args) {
	    String str=null;  
	    //$使用  
	    str=String.format("格式参数$的使用：%1$d,%2$s", 99,"abc");             
	    //System.out.println(str);   
	    
	    //System.out.println(String.format("%d",10));
	    
	    Date date=new Date();                                  
	    //c的使用  
	    String time = String .format("%tF", date);
	    String time2 = String.format("%tT", date);
	    String time3 = time+" "+time2;
	    System.err.println(time3);
	    //System.out.printf("全部日期和时间信息：%tF%n",date);     
	}
}

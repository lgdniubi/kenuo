package com.training.modules.train.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import com.ndktools.javamd5.Mademd5;

public class EncryptLiveUtil {
	
	
	public static final String CRATE_LIVE_URL = "http://api.csslcloud.net/api/room/create";
	public static final String UPDATE_LIVE_URL="http://api.csslcloud.net/api/room/update";


	/**
	 * 功能：将一个Map按照Key字母升序构成一个QueryString. 并且加入时间混淆的hash串
	 * @param queryMap  query内容
	 * @param time  加密时候，为当前时间；解密时，为从querystring得到的时间；
	 * @param salt   加密salt
	 * @return
	 */

	public static String createHashedQueryString(Map<String, String> queryMap,long time, String salt) {
		Mademd5 md5=new Mademd5();
	    Map<String, String> map = new TreeMap<String, String>(queryMap);
	    String qs = createQueryString(map); //生成queryString方法可自己编写
	    if (qs == null) {
	        return null;
	    }
	    qs=qs.substring(0,qs.lastIndexOf("&"));
	    time = time / 1000;
	    //time=URLEncoder.encode(time,"utf-8")
	    String hash = md5.toMd5(String.format("%s&time=%d&salt=%s", qs, time, salt));
	    hash = hash.toUpperCase();
	    String thqs = String.format("%s&time=%d&hash=%s", qs, time, hash);
	    System.out.println(thqs);
	    return thqs;
	}
	/**
	 * map 排序
	 * @param map
	 * @return
	 */
	
	private static String createQueryString(Map<String, String> map){
		String qString="";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			qString=qString+entry.getKey()+ "="+ entry.getValue()+"&";
		}
		return qString;
	}
	
	
	
	/**
	 * 创建直播
	 * @param qString
	 * @return
	 * @throws IOException
	 */

    public static String CreateLiveGet(String qString) throws IOException {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String getURL = CRATE_LIVE_URL +"?"+qString;//URLEncoder.encode(qString,"utf-8");
        System.out.println(getURL);
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines = reader.readLine();
        while ( reader.readLine()!= null) {
            System.out.println(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
       
        return lines;
    }
    
    /**
	 * 修改直播间
	 * @param qString
	 * @return
	 * @throws IOException
	 */

    public static String UpdateLiveGet(String qString) throws IOException {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String getURL = UPDATE_LIVE_URL +"?"+qString;//URLEncoder.encode(qString,"utf-8");
        System.out.println(getURL);
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines = reader.readLine();
        reader.close();
        // 断开连接
        connection.disconnect();
        return lines;
    }

}

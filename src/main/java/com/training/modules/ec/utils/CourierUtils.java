package com.training.modules.ec.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ndktools.javamd5.Mademd5;
import com.training.modules.ec.entity.CourierResultXML;

public class CourierUtils {
	
	private static final String FIND_COUR = "http://58.32.246.70:8002";   //走件流程查询接口
	
	public static String findCourierPost(String shipcode){
		Mademd5 md5=new Mademd5();
		String sign="";
		String user_id="idengyun";
		String app_key="Z6OcEb";
		String format="XML";
		String method="yto.Marketing.WaybillTrace";
		String timestamp="";
		String v="1.01";
		String param="<?xml version=\"1.0\"?><ufinterface><Result><WaybillCode><Number>"+shipcode+"</Number></WaybillCode></Result></ufinterface>";
		String Secret_Key="8PpkdD";
		PrintWriter out = null;
	    BufferedReader in = null;
	    String result = "";
		Date nDate=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timestamp=df.format(nDate);
		String A="app_key"+app_key+"format"+format+"method"+method+"timestamp"+timestamp+"user_id"+user_id+"v"+v;
		String B=Secret_Key+A;
		String C=md5.toMd5(B);
		sign=C;
		String qString="sign="+sign+"&app_key="+app_key+"&format="+format+"&method="+method+"&timestamp="+timestamp+"&user_id="+user_id+"&v="+v+"&param="+param;
		System.out.println(qString);
		 try {
			// qString=URLEncoder.encode(qString,"utf-8");
			 String getURL = FIND_COUR; //+"?"+ URLEncoder.encode(qString,"utf-8"); // qString;
	            URL realUrl = new URL(getURL);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(qString);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result +=line;
	            }
	           // result = new String(result.getBytes("GBK"),"UTF-8");
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
		
		return result;
	        
	    }    
	
	/**
     * @description 将xml字符串转换成list
     * @param xml
     * @return list
     */
    public static List<CourierResultXML> readStringXmlOut(String xml) {
        List<CourierResultXML> list = new ArrayList<CourierResultXML>();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml); 
           // 获取根节点
           Element rootElt = doc.getRootElement(); 
           // 拿到根节点的名称

           // 获取根节点下的子节点head
           Iterator iter = rootElt.elementIterator("Result"); 
           // 遍历head节点
           while (iter.hasNext()) {

               Element recordEle = (Element) iter.next();
               // 拿到Result节点下的子节点title值
               
               Iterator iter1= recordEle.elementIterator("WaybillProcessInfo");
               
               while (iter1.hasNext()) {
            	 CourierResultXML resultXML=new CourierResultXML();
              	 Element recordEle1 = (Element) iter1.next();
              	 String Waybill_No = recordEle1.elementTextTrim("Waybill_No"); 
              	 resultXML.setWaybillNo(Waybill_No);
              	 String Upload_Time = recordEle1.elementTextTrim("Upload_Time"); 
              	 resultXML.setUploadTime(Upload_Time);
              	 String ProcessInfo = recordEle1.elementTextTrim("ProcessInfo"); 
              	 resultXML.setProcessInfo(ProcessInfo);
              	 list.add(resultXML);
				}
               
           }
  
       } catch (DocumentException e) {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return list;
   }
	
	
	

}

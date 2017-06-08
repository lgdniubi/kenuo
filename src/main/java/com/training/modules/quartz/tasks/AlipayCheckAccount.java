package com.training.modules.quartz.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.csvreader.CsvReader;
import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.MtmyCheckAccount;
import com.training.modules.ec.service.CheckAccountService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.TrainRuleParam;

/**
 * 支付宝对账
 *
 */
@Component
public class AlipayCheckAccount extends CommonService{
	
	private static CheckAccountService checkAccountService;
	private static TrainRuleParamDao trainRuleParamDao;
	static{
		checkAccountService = (CheckAccountService) BeanUtil.getBean("checkAccountService");
		trainRuleParamDao = (TrainRuleParamDao) BeanUtil.getBean("trainRuleParamDao");
	}
	
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String downloadAccount = "https://openapi.alipay.com/gateway.do";	// https统一路径
	public static final String appid = "2017042006848196"; // APP应用ID
	public static final String alipay_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ5omdHA4d7iXNb0Z/5f5tHXT/MoH2OZ1SigzTk0Rjd62gWdsadQ7JijatNOFFusa8Pmyv/DLltCeV92CtLmxNR0ZNQDb3GeZmMHovaX6bgCSa+y75AVd5ovybZ9W7i6396Z2VUFTl1VvqoKNSAM+UAOZ0nsBM235vV4xiygDCJDAgMBAAECgYAOTefF9yLuW4aCqqNRZxuSy1ye2nqrJdMHzi16AuxsSh2x8CGAuGAFEIyu9BgGhzVcBVVDbz6aYxBOvHwRu2D+oa2YkgcH4u0yVy271cKWhsqtXC9ezfqYIFpk39Fy5ADUsCwVLuyAyh2NeqxXHbhC+cag8rYD9lw8EeaWB6uZoQJBAOWNNHCirbDQ4cMiadVzcNu9SOsDcuJd58KO1+RDN7a066uHV/s18axTdUlnnERDpa+M16upU/k3oeSouh5RzJsCQQCwqPsJbh7xxsOb4TsGL37kvpL0A4RawCYOpVE44FMZor5JNwX+V8MwXlUvz21ftMtWOAZlFbb7Uui5zriSyZd5AkEAizOLv1oHNhgVL73oq/XrVHV+iHV472i+qC7zIIraeENSPpw+cCoQOc4Ka88W5haXnNMt3f063QAtfnoLE2PLTQJAJDyVOcr31/pAd4IMvGkTq1IYDKuIA6F0bP6mGXeSNCj4xUXfGdvgstQ2vxbaRY5tQyM81JFOtmC8UhjALYPiaQJBALgWz4H3NREZA6XdkKdao2vo7xSKhyUOBdOoHyOMTnzgviFvcnvziVKqZH6OVd5QnxHkiptu2VLNEgbxW1nyOnA="; // 应用私钥
	public static final String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB"; // 支付宝公钥
	public static final String sign_type = "RSA";
	public static final String input_charset = "utf-8"; // 字符编码格式 目前支持utf-8
	
	public static List<String> list = new ArrayList<String>();
	public static String URL;
	
	/**
	 * 对账
	 */
	public void alipayCheckAccount(){
		TrainRuleParam trainRuleParam = new TrainRuleParam();
		trainRuleParam.setParamKey("excel_path");
		URL = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/cutImg/alipayCheckAccount";
		deleteFile(new File(URL));
		logger.info("[work0],start,支付宝对账，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("alipayCheckAccount");
		taskLog.setStartDate(startDate);
		
		try {
			list.clear();
			String zipFile = null;	// 调用支付宝 将zip文件下载到本地
			// 调用支付宝对账接口
			AlipayClient alipayClient = new DefaultAlipayClient(downloadAccount,appid,alipay_private_key,"json",input_charset,alipay_public_key,sign_type);
			AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
			String date= formatDate();
			request.setBizContent("{\"bill_type\":\"trade\",\"bill_date\":\""+date+"\"}");	
			AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
			if(response.isSuccess()){
				zipFile = downloadZip(response.getBillDownloadUrl());
				if(null == zipFile || zipFile.indexOf("下载支付宝账单失败")!=-1){
					taskLog.setStatus(1);
					taskLog.setExceptionMsg("支付宝下载数据异常:"+zipFile);
				}else{
					if (isZip(zipFile)) {	// 校验是否为zip文件
						// 解压支付宝对账单
						String unzip = unzip(zipFile,URL+"/zip");
						if(null == unzip || unzip.indexOf("zip文件解压异常")!=-1){
							taskLog.setStatus(1);
							taskLog.setExceptionMsg("zip文件解压异常:"+unzip);
						}else{
							// 获取所有文件夹下的文件
							List<String> allList = traverseFolder(unzip);
							if(allList.size() > 0){
								String csvFile = null;
								for (int i = 0; i < allList.size(); i++) {
									if(allList.get(i).indexOf("业务明细.csv")!=-1){
										csvFile = allList.get(i);
										continue;
									}
								}
								if(null == csvFile){
									taskLog.setStatus(1);
									taskLog.setExceptionMsg("zip文件解压成功,但文件夹下不存在*业务明细.csv文件!请核实支付宝是否更新对账单");
								}else{
									String isSuccess = insterAccountList(csvFile);
									if(null == isSuccess || isSuccess.indexOf("")==-1){
										taskLog.setStatus(1);
										taskLog.setExceptionMsg("CSV文件解析异常:"+isSuccess);
									}else{
										taskLog.setJobDescription("[work],支付宝对账成功,"+isSuccess);
										taskLog.setStatus(0);//任务状态
									}
								}
							}else{
								taskLog.setStatus(1);
								taskLog.setExceptionMsg("zip文件解压异常:文件夹下不存在文件!");
							}
						}
					}else{
						taskLog.setStatus(1);
						taskLog.setExceptionMsg("支付宝下载数据保存到本地异常:"+zipFile);
					}
				}
			} else {
				taskLog.setStatus(1);
				taskLog.setExceptionMsg("调用支付宝接口失败:"+response.getBody());
			}
		} catch (Exception e) {
			logger.error("#####【定时任务alipayCheckAccount】支付宝对账,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		deleteFile(new File(URL));
		logger.info("[work0],end,支付宝对账,结束时间："+df.format(new Date()));
	}
	// 解析CSV文件
	public static String insterAccountList(String filepath){
		try {
			List<MtmyCheckAccount> mcaList = new ArrayList<MtmyCheckAccount>();	//
	        if (isCsv(filepath)) {	// 校验是否为CSV文件
	        	String[] t = null;
	        	boolean flag = false;	//取出表头
	            CsvReader reader = new CsvReader(filepath, ',', Charset.forName("GBK"));
	            //reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。
	            while (reader.readRecord()) { //逐行读入除表头的数据 
	                String[] row= reader.getValues();
	                if(row.length > 1){
	                	if(flag){
	                		MtmyCheckAccount mtmyCheckAccount = new MtmyCheckAccount();
	                		for (int i = 0; i < row.length; i++) {
	                			if("支付宝交易号".equals(t[i])){
	                				mtmyCheckAccount.setPingId(row[i]);
	                			}
	                			if("商户订单号".equals(t[i])){
	                				mtmyCheckAccount.setOrderNo(row[i].trim());
	                			}
	                			if("业务类型".equals(t[i]) && (!"交易".equals(row[i]))){
	            					continue;
	                			}
	                			if("完成时间".equals(t[i])){
	                				mtmyCheckAccount.setPayDate(df.parse(row[i]));
	                			}
	                			if("商家实收（元）".equals(t[i])){
	                				mtmyCheckAccount.setPayAmount(row[i]);
	                			}
	                			if("商品名称".equals(t[i])){
	                				mtmyCheckAccount.setPayRemark(row[i]);
	                			}
		                	}
	                		mtmyCheckAccount.setPayChannel("支付宝移动支付(定时任务)");
	                		if("每天美耶".equals(mtmyCheckAccount.getPayRemark())){
	                			if(checkAccountService.findByOrderNo(mtmyCheckAccount) == 0){
	                				mcaList.add(mtmyCheckAccount);
	                			};
	                		}
	            		}else{
	            			t = row;
	            			flag = true;
	            		}
	                }
	            }
	            reader.close();
	        } else {
	            return "此文件不是CSV文件！";
	        }
			if(mcaList.size() > 0){
				int num = checkAccountService.insterAccount(mcaList);
				return "成功插入数据:"+num+"条";
			}else{
				return "成功插入数据:0条";
			}
		} catch (Exception e) {
			return  "解析支付宝CSV数据异常:"+e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length());
		}
	}
	// 下载支付宝zip文件
	public static String downloadZip(String urlStr){
		try {
			String filePath = URL + "/downZip_"+new java.util.Date().getTime()+"_zip.zip";
			URL url = null;
			HttpURLConnection httpUrlConnection = null;
			InputStream fis = null;
			FileOutputStream fos = null;
			try {
			    url = new URL(urlStr);
			    httpUrlConnection = (HttpURLConnection) url.openConnection();
			    httpUrlConnection.setConnectTimeout(5 * 1000);
			    httpUrlConnection.setDoInput(true);
			    httpUrlConnection.setDoOutput(true);
			    httpUrlConnection.setUseCaches(false);
			    httpUrlConnection.setRequestMethod("GET");
			    httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
			    httpUrlConnection.connect();
			    fis = httpUrlConnection.getInputStream();
			    byte[] temp = new byte[1024];
			    int b;
			    fos = new FileOutputStream(new File(filePath));
			    while ((b = fis.read(temp)) != -1) {
			        fos.write(temp, 0, b);
			        fos.flush();
			    }
			} catch (MalformedURLException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			} finally {
			    try {
			        if(fis!=null) fis.close();
			        if(fos!=null) fos.close();
			        if(httpUrlConnection!=null) httpUrlConnection.disconnect();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			return filePath;
		} catch (Exception e) {
			return "下载支付宝账单失败:"+e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length());
		}
	}
	/**
	 * 解压zip格式压缩包 
	 * 对应的是ant.jar
	 */
	public static String unzip(String sourceZip, String destDir)throws Exception {
		try {
			Project p = new Project();
			Expand e = new Expand();
			e.setProject(p);
			e.setSrc(new File(sourceZip));
			e.setOverwrite(false);
			e.setDest(new File(destDir));
			/*
			 * ant下的zip工具默认压缩编码为UTF-8编码， 而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
			 * 所以解压缩时要制定编码格式
			 */
			e.setEncoding("gbk");
			e.execute();
			return destDir;
		} catch (Exception e) {
			return "zip文件解压异常:"+e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length());
		}
	}
	//获取文件夹下所有内容
	public static List<String> traverseFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return list;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                    	list.add(file2.getAbsolutePath());
                    }
                }
                return list;
            }
        }
		return list;
    }
	/**
     * 删除所有文件夹下面的 文件(不删除文件夹)
     * @param oldPath
     */
    public static void deleteFile(File path) {
    	if (path.isDirectory()) {
    		File[] files = path.listFiles();
	    	for (File file : files) {
	    		deleteFile(file);
	    	}
    	}else{
    		path.delete();
    	}
    }
	//判断是否是csv文件
    private static boolean isCsv(String fileName) {
        return fileName.matches("^.+\\.(?i)(csv)$");
    }
    //判断是否是zip文件
    private static boolean isZip(String fileName) {
        return fileName.matches("^.+\\.(?i)(zip)$");
    }
    public static String formatDate(){
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());				//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -2);  	//设置为前两天
		dBefore = calendar.getTime();   			//得到前两天的时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    		//格式化前两天
		return defaultStartDate;
	}
}

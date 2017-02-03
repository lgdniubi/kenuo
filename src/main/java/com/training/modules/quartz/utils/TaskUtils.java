package com.training.modules.quartz.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.modules.quartz.entity.Task;

public class TaskUtils {

	private static final Log logger = LogFactory.getLog(TaskUtils.class);
	
	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokMethod(Task scheduleJob) {
		try {
			Object object = null;
			Class<?> clazz = null;
			if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
				object = SpringUtils.getBean(scheduleJob.getSpringId());
			} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			}
			if (object == null) {
				logger.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
				return;
			}
			clazz = object.getClass();
			Method method = null;
			try {
				method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
			} catch (NoSuchMethodException e) {
				logger.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (method != null) {
				method.invoke(object);
			}
			logger.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
		} catch (Exception e) {
			logger.error("任务名称 = [" + scheduleJob.getJobName() + "]执行出现异常，异常信息为："+e.getMessage());
		}
	}
	
	/**
	 * 序列化
	 * @param value
	 * @return
	 */
	public static byte[] serialize(Object value){
		if(value == null){
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(value);
			os.close();
			bos.close();
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object",e);
		}
		return rv;
	}
	/**
	 * 反序列化
	 * @param in
	 * @return
	 */
	public static Object deserialize(byte[] in){
		Object rv = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {

			if(in != null){
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				rv = is.readObject();
				is.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rv;
	}
}

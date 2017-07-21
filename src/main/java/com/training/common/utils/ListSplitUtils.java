package com.training.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分割list
 * @author coffee
 * 2017年6月28日09:59:47
 */
public class ListSplitUtils {
	
	/**
	 * 分割list
	 * @param name 用于日志输出
	 * @param object 分割list对象
	 * @param count 单次分割数量
	 * @return 返回object集合
	 */
	public static List<Object> listSplit(String name,Object object,int count){
		List<Object> subList = new ArrayList<Object>();
		try {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) object;
			if(list != null){
				if(list.size() > 0){
			        int num = (int)Math.ceil((double)list.size()/(double)count);	// 需执行次数
			        System.out.println("###分割"+name+"list,集合总数:"+list.size()+",单次分割数量："+count+",需执行次数："+num);
			        for(int i=0;i<num;i++){
			        	List<Object> newList = new ArrayList<Object>();
			        	if((i+1) == num){
			        		newList= list.subList(i*count,list.size());
			        	}else{
			        		newList= list.subList(i*count,count*(i+1));
			        	}
			        	subList.add(newList);
			        }
				}
			}
		} catch (Exception e) {
			System.out.println("#####error:分割list错误信息"+e.toString());
		}
		return subList;
	}
}

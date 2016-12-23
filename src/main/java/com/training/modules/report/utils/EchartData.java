package com.training.modules.report.utils;

import java.util.List;


public class EchartData {
	/**
	 * echart公共方法
	 */
	private List<String> legend;
    private List<String> xdata;
    private List<Integer> data;				//Y坐标轴 要为Integer类型 方便视图自己去计算
    public EchartData(){}
    
    
    public EchartData(List<String> legend, List<String> xdata, List<Integer> data){
        this.legend = legend;
        this.xdata=xdata;
        this.data=data;
    }





	public List<String> getLegend() {
		return legend;
	}


	public void setLegend(List<String> legend) {
		this.legend = legend;
	}


	public List<String> getXdata() {
		return xdata;
	}


	public void setXdata(List<String> xdata) {
		this.xdata = xdata;
	}


	public List<Integer> getData() {
		return data;
	}


	public void setData(List<Integer> data) {
		this.data = data;
	}
    
    

}

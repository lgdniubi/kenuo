package com.training.modules.ec.utils.igtpush.exception;

import net.sf.json.JSONObject;

public class ResponseError extends Exception {

	private static final long serialVersionUID = -5688171529148067114L;

	private String result;
	private String content;
	
	public ResponseError(){}
	
	public ResponseError(String message){
		super(message);
	}
	
	public ResponseError(String message,String result){
		super(message);
		this.result = result;
		this.content = message;
	}

	public String getResult() {
		return result;
	}
	public String getContent(){
		return content;
	}
	
	public String toString(){
		ResponseBean bean = new ResponseBean();
		bean.setMessage(content);
		bean.setResult(result);
		
		JSONObject json = JSONObject.fromObject(bean);
		return json.toString();
	}
	
	public ResponseBean toResBean(){
		ResponseBean bean = new ResponseBean();
		bean.setMessage(content);
		bean.setResult(result);
		return bean;
	}
}

package com.training.modules.ec.utils.igtpush.exception;

public class ResponseBean {
	
	
	private String message = SysConstants.SUCCESS_MESSAGE;
	private String result = SysConstants.SUCCESS_RESULT; 
	private Object data;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getData() {
		if(data == null){
			data = "";
		}
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}

package com.feeling.enums;

/**
 * 用户行为
 * @author dutao
 */
public enum UserLocusEnum {
	REG(0, "REG"),
	LOGIN(1, "LOGIN");
	private UserLocusEnum(int code, String message){
		this.code = code;
		this.message = message;
	}
	private int     code;
	private String  message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

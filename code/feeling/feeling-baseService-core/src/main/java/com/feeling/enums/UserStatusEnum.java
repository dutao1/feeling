package com.feeling.enums;

//-9: 注销， -1:锁定，1:正常
/**
 * 用户状态
 * @author dutao
 */
public enum UserStatusEnum {
	OK(1, "OK"),
	ERROR(-1, "LOCK"),
	CANCEL(-9, "CANCEL");
	private UserStatusEnum(int code, String message){
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

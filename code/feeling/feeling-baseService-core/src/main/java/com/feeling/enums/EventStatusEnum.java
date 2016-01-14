package com.feeling.enums;

//-3:被忽略次数达到上限，-2: 过期， -1:删除，1:正常
/**
 * 用户状态
 * @author dutao
 */
public enum EventStatusEnum {
	OK(1, "OK"),
	DEL(-1, "DEL"),
	EXPIRE(-2, "EXPIRE"),
	SKIP_UPPER(-3, "SKIP_UPPER");
	private EventStatusEnum(int code, String message){
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

package com.feeling.enums;

/**
 * 异常类
 * @author  dutao
 *
 */
public enum ReturnCodeEnum  {

	SUCCESS(200, "SUCCESS"),
	ERROR(500, "ERROR"),
	SQL_PARAM_NAME_ERROR(1001, "sql对象参数必须定义为t"),
	SQL_TABLE_NAME_EMPTY(1002,"%s实体内需要定义@Table表名"),
	SQL_PK_EMPTY(1003,"主键更新操作时,主键%s不可为空"),
	 PARAMETER_ERROR(5001, "请求参数错误，%s")
	;
	
	private int     code;
	private String  message;

	private ReturnCodeEnum(int code, String message){
		this.code = code;
		this.message = message;
	}
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}

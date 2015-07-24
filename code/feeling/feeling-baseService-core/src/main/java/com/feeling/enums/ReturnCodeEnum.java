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
	SQL_PK_EMPTY_ERROR(1004, "主键查询id不能为空"),
	
	PWD_NO_MODIFY_ERROR(2001, "新密码和旧密码一样，不需要修改"),
	PWD_ERROR(2002, "用户名密码错误"),
	NO_USER_ERROR(2003, "用户不存在"),
	
	EVENT_TYPE_EMPTY_ERROR(3001, "事件类型不能为空"),
	EVENT_PUBLISH_ERROR(3002, "事件发布失败，id为空"),
	EVENT_NO_INFO_ERROR(3003, "无可推荐事件"),
	EVENT_PAGE_ERROR(3004, "请输入正确的页码信息"), 
	PARAMETER_ERROR(5001, "请求参数错误，%s"),
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

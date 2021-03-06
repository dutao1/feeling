package com.feeling.enums;

/**
 * 异常类
 * @author  dutao
 *
 */
public enum ReturnCodeEnum  {

	SUCCESS(200, "SUCCESS"),
	ERROR(500, "SYSTEM ERROR"),
	SQL_PARAM_NAME_ERROR(1001, "sql对象参数必须定义为t"),
	SQL_TABLE_NAME_EMPTY(1002,"%s实体内需要定义@Table表名"),
	SQL_PK_EMPTY(1003,"主键更新操作时,主键%s不可为空"),
	SQL_PK_EMPTY_ERROR(1004, "主键查询id不能为空"),
	
	PWD_NO_MODIFY_ERROR(2001, "新密码和旧密码一样，不需要修改"),
	PWD_ERROR(2002, "用户名密码错误"),
	NO_USER_ERROR(2003, "用户不存在"),
	USER_NAME_DUPLICATEKSY_ERROR(2004, "手机号[%s]已注册"),
	PWD_UNAME_EMPTY_ERROR(2005, "手机号或密码不能为空"),
	STATUS_ERROR_ERROR(2006, "用户状态异常"),
	STATUS_EMPTY_ERROR(2007, "用户ID或状态不能为空"),
	NO_LOGIN_ERROR(2008, "请先登录"),
	PWD_MODIFY_INPUT_ERROR(2009, "新旧密码输入有误"),
	LOGIN_TOKEN_EMPTY(2010, "用户登录密钥为空"),
	LOGIN_TOKEN_ERROR(2011, "用户登录密钥异常,请重新登录"),
	MOBILE_EMPTY_ERROR(2012, "手机号不能为空"),
	
	VERIFY_CODE_EMPTY(2020, "验证码不能为空"),
	VERIFY_CODE_ERROR(2021, "验证码输入错误"),
	VERIFY_CODE_FRE_QUENCY(2022, "验证码发送过于频繁,请%s秒重试"),
	VERIFY_CODE_MAX_TIMES(2023, "1小时内发送次数达上限"),
	VERIFY_CODE_EXPIRE(2024, "验证码未发送或已过期"),
	VERIFY_CODE_SEND_ERROR(2025, "下发验证码异常,稍后重试"),
	
	EVENT_TYPE_EMPTY_ERROR(3001, "事件类型不能为空"),
	EVENT_PUBLISH_ERROR(3002, "事件发布失败，id为空"),
	EVENT_NO_INFO_ERROR(3003, "无可推荐事件"),
	EVENT_PAGE_ERROR(3004, "请输入正确的页码信息"), 
	EVENT_LOT_ERROR(3005, "经纬度输入错误"),
	EVENT_DEVICE_ERROR(3006, "设备类型错误"),
	EVENT_TEXT_SO_LARGE_ERROR(3007, "文字内容超过250个字"),
	NO_EVENT_VOTE_ERROR(3008, "不存在的投票事件，%s"),
	
	PARAMETER_ERROR(5001, "请求参数错误，%s"),
	FILE_LARGE_ERROR(5002, "文件超出最大值上限[%s]mb"),
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

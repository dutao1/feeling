package com.feeling.constants;

public class RedisConstants {

	
	public static  final String MAIN_KEY = "feeling";
	
	public static  final String USER_LOGIN_KEY = MAIN_KEY+"_login_";
	
	public static final String USER_INFO_SPLIT= "||";
	
	public static final String VERIFYCODE_SEND_KEY = MAIN_KEY+"_verify_send_";
	
	
	/**
	 * 上次发送时间
	 */
	public static final String VERIFYCODE_LAST_SEND_KEY = MAIN_KEY+"_verify_last_";
	
	
	/**
	 * 发送次数
	 */
	public static final String VERIFYCODE_TIMES_KEY = MAIN_KEY+"_verify_send_times_";
	
	/**
	 * 1小时一个ip最多发多少次
	 */
	public static final int SEND_SMS_MAX_TIMES_FOR_HOUR_IP = 10;
	
	/**
	 * 1小时一个手机号最多发多少次
	 */
	public static final int SEND_SMS_MAX_TIMES_FOR_HOUR_MOBILE = 3;
	
	/**
	 * 同一手机发送验证码间隔多少秒
	 */
	public static final int SEND_SMS_SPACE_SECOND = 120;
}

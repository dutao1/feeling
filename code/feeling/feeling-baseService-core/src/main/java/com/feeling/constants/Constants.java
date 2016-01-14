package com.feeling.constants;

public class Constants {

	/**
	 * 发送sms短信验证码接口
	 */
	public static  final String SEND_SMS_URL = "https://sms.yunpian.com/v1/sms/send.json";

	/**
	 * SMS API KEY
	 */
	public static final String API_KEY = "1528fa4d1fddfa490aa3d8b95ce64336";

	public static final String SMS_CONTENT = "【蒲公英】您的验证码是%s,5分钟内有效";
	
	//以下是sms的参数key
	public static final String API_KEY_NAME_SMS = "apikey";
	public static final String TEXT_SMS = "text";
	public static final String MOBILE_SMS = "mobile";
	public static final String CODE_SMS = "code";
	public static final String RETURN_CODE_SUCCESSED_SMS = "0";
	//事件被skip的最大次数
	public static final int MAX_SKIP_TIME = 50;
	
	/**
	 * 多少天之前的过期  -7
	 */
	public static final int EXPIRE_DAYS = -7;
}

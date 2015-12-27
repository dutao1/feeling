package com.feeling.service;

import java.util.HashMap;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.utils.http.HttpPoolUtils;
import com.feeling.utils.http.HttpResult;
import com.feeling.constants.Constants;

/**
 * 验证码服务
 * @author dutao
 */
@Service
public class VerifyCodeService {

	//redis 客户端
	@Autowired
    RedisClient redisClient;
	
	/**
	 * 发送验证码
	 * @param mobile 手机号
	 * @param remoteIP  远程ip
	 * @return boolean
	 */
	public void sendVerifyCode(String mobile,String remoteIP){
		redisClient.checkCanSendVerifyCode(mobile, remoteIP);
		String vcode = getCode();
		boolean b=sendSms(mobile,String.format
				(Constants.SMS_CONTENT,vcode));
		if(b){
			 b= redisClient.setVerifyCode(mobile, vcode);
			 b = redisClient.setVerifyCodeSecurity(mobile, remoteIP);
		}
		if(!b){
			throw new OptException(ReturnCodeEnum.VERIFY_CODE_SEND_ERROR);
		}
	}
	/**
	 * 是否发送成功
	 * @param mobile 手机号
	 * @param code  验证码
	 * @return   boolean
	 */
	public boolean checkVerfyCode(String mobile,String code){
		if(StringUtils.isEmpty(mobile)){
			throw new OptException(ReturnCodeEnum.MOBILE_EMPTY_ERROR);
		}
		if(StringUtils.isEmpty(code)){
			throw new OptException(ReturnCodeEnum.VERIFY_CODE_EMPTY);
		}
		String codeRedis  = redisClient.getVerifyCode(mobile);
		if(StringUtils.isEmpty(codeRedis)||codeRedis.equals("OK")){
			throw new OptException(ReturnCodeEnum.VERIFY_CODE_EXPIRE);
		}
		if(!codeRedis.equals(code.trim())){
			throw new OptException(ReturnCodeEnum.VERIFY_CODE_ERROR);
		}
		return true;
	}
	private String getCode() {
        String code = "0" + new Random().nextLong();
        return code.substring(code.length() - 6);
    }
	/**
	 * 下发短信
	 * @param mobile 手机号
	 * @param msg  内容
	 * @return true 成功
	 */
	private boolean sendSms(String mobile,String msg){
		//掉用运营商接口下发短信
		HashMap<String, String> params = new HashMap<String, String>();
	 	params.put(Constants.API_KEY_NAME_SMS, Constants.API_KEY);
        params.put(Constants.TEXT_SMS, msg);
        params.put(Constants.MOBILE_SMS, mobile);
		String url = HttpPoolUtils.formatPathUrl(Constants.SEND_SMS_URL, params);
	    HttpResult httpResult = HttpPoolUtils.doPost(params, url, null, "utf-8");
	    boolean result =false;
	    if(httpResult!=null &&httpResult.getStatus()==200){
	    	JSONObject json = JSONObject.parseObject(httpResult.getBody());
	    	String rCode = json.getString(Constants.CODE_SMS);
	    	if(StringUtils.isNotEmpty(rCode)&& 
	    			rCode.equals(Constants.RETURN_CODE_SUCCESSED_SMS)){
	    		result =true;
	    	}
	    } 
	    if(!result){
	    	LogInfo.WEB_LOG.info(" warn  sendSms  failure result="+httpResult.toString());
	    }
	    return result;
	}
}

package com.feeling.service;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.feeling.constants.RedisConstants;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.service.redis.RedisClientTemplate;
import com.feeling.utils.CryptUtil;


@Component("redisClient")
public class RedisClient {

    @Resource(name="redisClientTemplate")
    protected RedisClientTemplate redisClientTemplate;
	/**
	 * 检查token是否合法
	 * @param token
	 * @param uid
	 * @return
	 */
	public boolean checkLoginToken(String token,Integer uid){
		if(StringUtils.isEmpty(token)||uid==null){
			throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_EMPTY);
		}
		String key = RedisConstants.USER_LOGIN_KEY+String.valueOf(uid.intValue());
		String result = redisClientTemplate.get(key);
		LogInfo.REDIS_LOG.info("redis key=="+key+",result="+result); 
		if(StringUtils.isNotEmpty(result)&& result.equals(token)){
			return true;
		}
		return false;
	}
	/**
	 * 设置登录token
	 * @param uid
	 * @param deviceId
	 * @return
	 */
	public String setLoginToken(Integer uid,String deviceId){
		String key = RedisConstants.USER_LOGIN_KEY+String.valueOf(uid.intValue());
		String val = String.valueOf(System.currentTimeMillis());
		if(StringUtils.isNotEmpty(deviceId)){
			val =val+RedisConstants.USER_INFO_SPLIT+deviceId;
		}
		val = CryptUtil.encrypt(val);
		String result = redisClientTemplate.set(key, val);
		redisClientTemplate.expire(key, 7*24*3600);
		LogInfo.REDIS_LOG.info("redis key=="+key+",result="+result);
		return val;
	}
	
	/**
	 * 清除token
	 * @param uid
	 * @return
	 */
	public boolean clearLoginToken(Integer uid){
		String key = RedisConstants.USER_LOGIN_KEY+String.valueOf(uid.intValue());
	    long result = redisClientTemplate.del(key);
	    return true;
	}
	/**
	 * 校验是否可以下发验证码
	 * @param mobile  手机号
	 * @param remoteIP  远程ip
	 * @return boolean
	 */
	public void checkCanSendVerifyCode(String mobile,String remoteIP){
		String lastSendKey = RedisConstants.VERIFYCODE_LAST_SEND_KEY+mobile;
		String lastSendTime =redisClientTemplate.get(lastSendKey);
		long spaceTime = 1000000;//间隔秒
		if(!StringUtils.isEmpty(lastSendTime)&&!lastSendTime.equals("OK")){
			try{
				spaceTime = (System.currentTimeMillis()-Long.valueOf(lastSendTime))/1000;
			}catch(Exception e){
				LogInfo.REDIS_LOG.error("redis key=="+lastSendKey+",error="+e.getMessage());
			}
		}
		//间隔时间过短
		if(spaceTime<RedisConstants.SEND_SMS_SPACE_SECOND){
			throw new OptException(ReturnCodeEnum.VERIFY_CODE_FRE_QUENCY,RedisConstants.SEND_SMS_SPACE_SECOND); 
		}
		String mobileKey = RedisConstants.VERIFYCODE_TIMES_KEY+mobile;
		String valMobile =redisClientTemplate.get(mobileKey);
		if(!StringUtils.isEmpty(valMobile)&&!valMobile.equals("OK")){
			try{
				if(Integer.valueOf(valMobile)>=
						RedisConstants.SEND_SMS_MAX_TIMES_FOR_HOUR_MOBILE){
					throw new OptException(ReturnCodeEnum.VERIFY_CODE_MAX_TIMES);
				}
			}catch(Exception e){
				LogInfo.REDIS_LOG.error("redis key=="+mobileKey+",error="+e.getMessage());
			}
		}
		String ipKey = RedisConstants.VERIFYCODE_TIMES_KEY+remoteIP;
		String valIp =redisClientTemplate.get(ipKey);
		if(!StringUtils.isEmpty(valIp)&&!valIp.equals("OK")){
			try{
				if(Integer.valueOf(valIp)>=
						RedisConstants.SEND_SMS_MAX_TIMES_FOR_HOUR_IP){
					throw new OptException(ReturnCodeEnum.VERIFY_CODE_MAX_TIMES);
				}
			}catch(Exception e){
				LogInfo.REDIS_LOG.error("redis key=="+mobileKey+",error="+e.getMessage());
			}
		}
	}
	
	
	/**
	 * 设置验证码安全相关信息
	 * @param mobile 手机号
	 * @param remoteIP  ip
	 * @return boolean
	 */
	public boolean setVerifyCodeSecurity(String mobile,String remoteIP){
		String lastSendKey = RedisConstants.VERIFYCODE_LAST_SEND_KEY+mobile;
		String ipKey = RedisConstants.VERIFYCODE_TIMES_KEY+remoteIP;
		String mobileKey = RedisConstants.VERIFYCODE_TIMES_KEY+mobile;
		
		String valIp =redisClientTemplate.get(ipKey);
		String valMobile =redisClientTemplate.get(mobileKey);
		int timesIP = 1;
		int timesMobile = 1;
		if(!StringUtils.isEmpty(valIp)&&!valIp.equals("OK")){
			try{
				timesIP = Integer.valueOf(valIp)+1;
			}catch(Exception e){
				LogInfo.REDIS_LOG.error("redis key=="+ipKey+",error="+e.getMessage());
			}
		}
		if(!StringUtils.isEmpty(valMobile)&&!valMobile.equals("OK")){
			try{
				timesMobile = Integer.valueOf(valMobile)+1;
			}catch(Exception e){
				LogInfo.REDIS_LOG.error("redis key=="+mobileKey+",error="+e.getMessage());
			}
		}
		try{
			//记录ip发送次数
			redisClientTemplate.set(ipKey, String.valueOf(timesIP));
			redisClientTemplate.expire(ipKey,3600);
			//记录手机号发送次数
			redisClientTemplate.set(mobileKey, String.valueOf(timesMobile));
			redisClientTemplate.expire(mobileKey,3600);
			//记录上次发送时间
			redisClientTemplate.set(lastSendKey, String.valueOf(System.currentTimeMillis()));
			redisClientTemplate.expire(mobileKey,600);
		}catch(Exception e){
			LogInfo.REDIS_LOG.error("redis setVerifyCodeSecurity error ,error="+e.getMessage());
		}
		return true ;
	}
	/**
	 * 设置验证码
	 * @param mobile 手机号
	 * @param code 验证码
	 * @return boolean
	 */
	public boolean setVerifyCode(String mobile,String code){
		String key = RedisConstants.VERIFYCODE_SEND_KEY+mobile;
		String result =redisClientTemplate.set(key, code);
		redisClientTemplate.expire(key, 300);
		LogInfo.REDIS_LOG.info("redis key=="+key+",result="+result);
		return true;
	}
	/**
	 * 根据手机号查验证码
	 * @param mobile 手机号
	 * @return String
	 */
	public String getVerifyCode(String mobile){
		String key = RedisConstants.VERIFYCODE_SEND_KEY+mobile;
		return redisClientTemplate.get(key);
	} 
	
}

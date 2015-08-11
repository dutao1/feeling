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
}

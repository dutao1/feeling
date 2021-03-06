package com.feeling.service;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feeling.dao.UserBaseDao;
import com.feeling.dao.UserLocusDao;
import com.feeling.dto.UserBaseDto;
import com.feeling.dto.UserLocusDto;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.enums.UserLocusEnum;
import com.feeling.enums.UserStatusEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.utils.CryptUtil;
import com.feeling.vo.UserLoginVo;
import com.feeling.vo.UserVo;

/**
 * 用户相关操作
 * @author dutao
 *
 */
@Service
public class UserService extends BaseService{

	@Autowired
	UserBaseDao userBaseDao;
	@Autowired
	UserLocusDao userLocusDao;
    @Autowired
    RedisClient redisClient;
    
    /**
     * 根据id查询数据
     * @param id  id
     * @return UserBaseDto
     */
	public UserBaseDto getUserById(Integer id){
		UserBaseDto userDto = userBaseDao.getUserById(id);
		if(userDto!=null){
			userDto.setPwd(null);
		}
		return  userDto;
	}
	/**
	 * 更新用户信息，包括更新状态，照片等等
	 * @param uvo 必须有用户id
	 * @param loginToken token
	 * @return boolean
	 */
	public boolean updateUserInfo(UserVo uvo,String loginToken){
		if(uvo==null||uvo.getId()==null){
			throw new OptException(ReturnCodeEnum.NO_LOGIN_ERROR);
		}
		boolean isLogin = redisClient.checkLoginToken(loginToken, uvo.getId());
		if(!isLogin){
			throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR);
		}
		return updateUser(uvo);
	}
	
	/**
	 * 修改用户密码
	 * @param nickName
	 * @param oldPwd
	 * @param newPwd
	 * @return boolean
	 */ 
	public boolean modifyPwd(Integer uid,String oldPwd,String newPwd,String token){
		if(uid==null){
			throw new OptException(ReturnCodeEnum.NO_LOGIN_ERROR);
		}
		boolean tokenFlag = redisClient.checkLoginToken(token, uid);
		if(!tokenFlag){
			throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR);
		}
		if(StringUtils.isEmpty(oldPwd)||
				StringUtils.isEmpty(newPwd)){
			throw new OptException(ReturnCodeEnum.PWD_MODIFY_INPUT_ERROR);
		}
		if(oldPwd!=null &&oldPwd.equals(newPwd)){
			throw new OptException(ReturnCodeEnum.PWD_NO_MODIFY_ERROR);
		}
		String pwd = CryptUtil.encrypt(oldPwd);
		UserBaseDto userBaseDto = userBaseDao.checkPwd(uid, pwd);
		if(userBaseDto!=null){
			userBaseDto.setPwd(CryptUtil.encrypt(newPwd));
			int result = userBaseDao.updateByPk(userBaseDto);
			boolean returnResult =result==1?true:false;
			return returnResult;
		}else{
			throw new OptException(ReturnCodeEnum.PWD_ERROR);
		}
	}
	/**
	 * 退出登录状态
	 * @param uid
	 * @return
	 */
	public boolean logout(Integer uid,String loginToken){
		boolean b = redisClient.checkLoginToken(loginToken, uid);
		if(!b){
			throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR);
		}
		return redisClient.clearLoginToken(uid);
	}
	
	/**
	 * 用户登录
	 * @param uvo
	 * @return UserBaseDto
	 */
	public UserBaseDto login(UserLoginVo uvo){
		
		if(uvo==null){
			return null;
		}
		String pwd = CryptUtil.encrypt(uvo.getPwd());
		UserBaseDto userBaseDto = userBaseDao.checkPwdByMobile(uvo.getMobile(), pwd);
		if(userBaseDto!=null){
			UserVo userVo = new UserVo();
			try {
				BeanUtils.copyProperties(userVo, uvo);
			} catch (Exception e) {
				LogInfo.USER_LOG.error(e.getMessage());
			}  
			insertUserLocus(userVo,userBaseDto.getId(),UserLocusEnum.LOGIN.getCode());
			//设置登录状态
			String token = redisClient.setLoginToken(userBaseDto.getId(),uvo.getDeviceId());
			uvo.setLoginToken(token);
		}else{
			throw new OptException(ReturnCodeEnum.PWD_ERROR);
		}
		return userBaseDto;
	}
	
	/**
	 * 新用户注册，密码长度小于30位
	 * @param uvo
	 * @return
	 */
	@Transactional
	public Integer regNewUser(UserVo uvo){
		Integer uid = -1;
		if(uvo!=null){
			String nickName = uvo.getNickName();
			String mobile = uvo.getMobile();
			if(StringUtils.isEmpty(nickName)){
				 if(StringUtils.isNotEmpty(mobile) &&mobile.length()==11){
					 nickName = mobile.substring(mobile.length()-4);
				 }
			}
			uvo.setNickName(nickName);
			UserBaseDto userBaseDto = new UserBaseDto();
			try {
				BeanUtils.copyProperties(userBaseDto, uvo);
				String pwd = userBaseDto.getPwd();
				userBaseDto.setPwd(CryptUtil.encrypt(pwd));
				userBaseDto.setStatus(UserStatusEnum.OK.getCode());//可用
				userBaseDao.insertWithId(userBaseDto);
				uid=userBaseDto.getId();
				insertUserLocus(uvo,uid,UserLocusEnum.REG.getCode());
			} catch (Exception e) {
				if(e instanceof DuplicateKeyException){
					throw new OptException(ReturnCodeEnum.USER_NAME_DUPLICATEKSY_ERROR, userBaseDto.getNickName());
				}else{
					e.printStackTrace();
				}
				
			}  
		}
		return uid;
	}
	
	/**
	 * 更新用户信息
	 * @param uvo
	 * @return
	 */
	public boolean updateUser(UserVo uvo){
		
		UserBaseDto userBaseDto  = new UserBaseDto();
		try { 
			if(uvo==null||uvo.getId()==null){
				throw new OptException(ReturnCodeEnum.NO_LOGIN_ERROR);
			}
			BeanUtils.copyProperties(userBaseDto, uvo);
			int result = userBaseDao.updateByPk(userBaseDto);
			if(result==0){
				throw new OptException(ReturnCodeEnum.NO_USER_ERROR);
			}
			return result==1?true:false;
		} catch (Exception e) {
			LogInfo.USER_LOG.error(e.getMessage());
		} 
		return false;
	}
	
	/**
	 * 插入用户行为数据
	 * @param uvo 用户信息
	 * @param uid 用户id
	 * @param locusType
	 */
	public void insertUserLocus(UserVo uvo,Integer uid,Integer locusType){
		UserLocusDto userLocusDto = new UserLocusDto();
		if(uvo.getLat()!=null&&uvo.getLon()!=null){
			Double lat = uvo.getLat();
			Double lon = uvo.getLon();
			userLocusDto.setLat(lat);
			userLocusDto.setLon(lon);
			userLocusDto.setCityCode(uvo.getCityCode());
			userLocusDto.setLocationLongCode(getLongGeoHash(lat, lon));
			userLocusDto.setLocationHash(getGeoHash(lat,lon));
		}
		userLocusDto.setUid(uid);
		userLocusDto.setDeviceId(uvo.getDeviceId());
		userLocusDto.setDeviceImei(uvo.getDeviceImei());
		userLocusDto.setDeviceType(uvo.getDeviceType());
		userLocusDto.setLocusType(locusType);
		userLocusDao.insertWithOutId(userLocusDto, 0);
	}
}

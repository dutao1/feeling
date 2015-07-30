package com.feeling.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feeling.dto.UserBaseDto;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.enums.UserStatusEnum;
import com.feeling.exception.OptException;
import com.feeling.service.UserService;
import com.feeling.vo.UserUptVo;
import com.feeling.vo.UserVo;
import com.feeling.web.common.ReturnResult;
import com.feeling.web.common.WebFileHelper;
/**
 * @author dutao
 *
 */
@Controller
public class UserInfoController  extends BaseController{
    
	@Autowired
	private UserService userService;
    @Autowired
    private WebFileHelper webFileHelper;
    
    /**
     * 密码修改
     * @param uid 用户id
     * @param pwd 原密码
     * @param newPwd 新密码
     * @return String
     */
    @RequestMapping(value = "/user/uptPwd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uptPwd(Integer uid ,String pwd,String newPwd){
    	boolean result = userService.modifyPwd(uid, pwd, newPwd);
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
    	returnResult.setData(result);
        return returnResult.toString();
    }
    
    /**
     * 更新用户信息
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/user/uptUserInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uptUserInfo(UserUptVo userVo,HttpServletRequest request,HttpServletResponse response){
    	if(userVo==null||userVo.getId()==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR);
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        boolean result = false;
        try {
	       	 String url = webFileHelper.uploadUserAvatar(request,userVo.getId());
	       	 if(url!=null){
	       		 userVo.setAvatar(url);
	       	 }
	       	 UserVo uvo = new UserVo();
	       	 BeanUtils.copyProperties(uvo, userVo);
	       	 result =  userService.updateUserInfo(uvo) ;
		} catch (Exception e) {
			super.writeErrorLog(e.getMessage());
		}
    	returnResult.setData(result);
        return returnResult.toString();
    }
    /**
     * 更新用户状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/user/uptUserStatus", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uptUserStatus(Integer uid ,Integer status){
    	
    	if(uid==null||status==null){
    		throw new OptException(ReturnCodeEnum.STATUS_EMPTY_ERROR);
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
    	UserVo uvo = new UserVo();
    	uvo.setId(uid);
    	uvo.setStatus(status);
    	boolean result = userService.updateUserInfo(uvo);
    	returnResult.setData(result);
        return returnResult.toString();
    }
    
    /**
     * 用户登录
     * @param userName 用户名
     * @param pwd 密码
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String login(String userName ,String pwd){
    	if(StringUtils.isEmpty(userName)||
    			StringUtils.isEmpty(pwd)){
    		throw new OptException(ReturnCodeEnum.PWD_UNAME_EMPTY_ERROR);
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS); 
    	UserVo uvo = new UserVo();
    	uvo.setPwd(pwd);
    	uvo.setNickName(userName);
    	UserBaseDto udto = userService.login(uvo);
    	if(udto!=null){
    		udto.getStatus();
    		if(UserStatusEnum.OK.getCode()!=udto.getStatus()){
    			throw new OptException(ReturnCodeEnum.STATUS_ERROR_ERROR);
    		}
    		uvo = new UserVo();
    		try {
				BeanUtils.copyProperties(uvo, udto);
			} catch (Exception e) {
				super.writeErrorLog(e.getMessage());
			} 
    	}
       uvo.setAvatar(webFileHelper.getUserAvatarUrl(uvo.getAvatar()));
	   returnResult.setData(uvo);
       return returnResult.toString();
    }
    /**
     * 用户注册
     * @param  
     * @return
     */
    @RequestMapping(value = "/user/reg", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String reg(UserVo uvo,HttpServletRequest request,HttpServletResponse response){
    	 ReturnResult returnResult=new ReturnResult();
         returnResult.setResultEnu(ReturnCodeEnum.SUCCESS); 
         Integer uid = userService.regNewUser(uvo);
         try {
        	 String url = webFileHelper.uploadUserAvatar(request,uid);
        	 if(url!=null){
        		 UserVo userVo = new UserVo();
        		 userVo.setId(uid);
        		 userVo.setAvatar(url);
        		 userService.updateUserInfo(userVo);
        	 }
		} catch (Exception e) {
			super.writeErrorLog(e.getMessage());
		}
         returnResult.setData(uid);
         return returnResult.toString();
    }
   
    
}

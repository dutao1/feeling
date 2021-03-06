package com.feeling.web.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.service.EventService;
import com.feeling.service.RedisClient;
import com.feeling.vo.UserEventVo;
import com.feeling.web.common.ReturnResult;
import com.feeling.service.common.WebFileHelper;

/**
 * 
 * 用户事件
 * @author dutao
 *
 */
@Controller
public class UserEventController extends BaseController{

	@Autowired
	private EventService eventService;
    @Autowired
    private WebFileHelper webFileHelper;
    
    @Autowired
    RedisClient redisClient;
    
    @RequestMapping(value = "/userEvent/my", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String myEventInfos(UserEventVo eventVo,Integer offset,Integer limit){
    	if(eventVo==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR);
    	}
    	if(eventVo.getUid()==null ){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户id不能为空");
    	}
    	boolean isLogin = redisClient.checkLoginToken(eventVo.getLoginToken(), eventVo.getUid());
    	if(!isLogin){
    		throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR); 
    	}
    	int count = eventService.countEventListByUid(eventVo.getUid());
    	HashMap<String,Object> hm = new HashMap<String,Object>();
    	hm.put("userEventCount", count) ;
    	List<UserEventVo> list  =null;
    	if(count>=1){
    		if(limit==null){
    			limit = 6;
    		}else{
    			if(limit.intValue()>=
        				super.defaultUserEventPageSize*10){
        			limit = super.defaultUserEventPageSize;
        		}
    		}
    		if(offset==null||offset<0){
    			offset=0;
    		}
    		list = eventService.getUserEventByUid(eventVo.getUid(), limit, offset);
    	}
    	hm.put("userEventList", list) ;
    	hm.put("toSendAround", 5) ;
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        returnResult.setData(hm);
        return returnResult.toString();
    }
    
	/**
	 * 根据用户id 统计对应发布事件信息总数
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/userEvent/countEventByUid", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String countEventByUid(Integer uid){
    	if(uid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户id为空");
    	}
    	int count = eventService.countEventListByUid(uid);
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        returnResult.setData(count);
        return returnResult.toString();
    }
	/**
	 * 获得用户事件列表
	 * @param uid 用户id
	 * @param offset 从第几条开始 0开始
	 * @param count 是否返回总数 1返回 ， 0不返回
	 * @return
	 */
	@RequestMapping(value = "/userEvent/userEventList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getUserEventList(Integer uid,Integer offset,Integer limit,Integer count,String loginToken){
		
		boolean isLogin = redisClient.checkLoginToken(loginToken,uid);
    	if(!isLogin){
    		throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR); 
    	}
		List<UserEventVo> list  =null;
    	boolean isFindList = true;//是否查询列表
    	int dataCount = 0;
    	count = count==null?0:count;
    	if(uid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户id为空");
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        HashMap<String,Object> hm = new HashMap<String,Object>();
    	if(count==1){
    		dataCount = eventService.countEventListByUid(uid);
    		if(dataCount>0){
    			isFindList=true;//无记录不查列表
    		}
    		hm.put("userEventCount", dataCount) ;
    	}
    	if(isFindList){
    		if(limit==null||limit.intValue()>=
    				super.defaultUserEventPageSize*10){
    			limit = super.defaultUserEventPageSize;
    		}
    		list = eventService.getUserEventByUid(uid,limit, offset);
    	}
    	hm.put("userEventList", list) ;
        returnResult.setData(hm);
        return returnResult.toString();
    }
}

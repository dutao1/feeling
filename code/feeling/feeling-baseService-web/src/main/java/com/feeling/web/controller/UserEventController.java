package com.feeling.web.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.service.EventService;
import com.feeling.vo.UserEventVo;
import com.feeling.web.common.ReturnResult;
import com.feeling.web.common.WebFileHelper;

/**
 * 
 * 用户事件
 * @author dutao
 *
 */
public class UserEventController extends BaseController{

	@Autowired
	private EventService eventService;
    @Autowired
    private WebFileHelper webFileHelper;
    
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
    public String getUserEventList(Integer uid,Integer offset,Integer count){
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
    			isFindList=false;//无记录不查列表
    		}
    		hm.put("userEventCount", dataCount) ;
    	}
    	if(isFindList){
    		list = eventService.getUserEventByUid(uid, super.defaultUserEventPageSize, offset);
    	}
    	hm.put("userEventList", list) ;
        returnResult.setData(hm);
        return returnResult.toString();
    }
}

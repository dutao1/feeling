package com.feeling.web.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feeling.dto.EventCommentRecordDto;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.service.EventCommentService;
import com.feeling.vo.EventCommentRecordVo;
import com.feeling.web.common.ReturnResult;
@Controller
public class EventCommentController   extends BaseController{
	@Autowired
    private EventCommentService eventCommentService;
 
    
    /**
     * 查询评论总数
     * @param eid 事件id
     * @return String
     */
	@RequestMapping(value = "/ec/countComment", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String countCommentList(Integer eid){
    	if(eid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"事件id为空");
    	}
    	int count = eventCommentService.countEventCommentListByEid(eid);
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        returnResult.setData(count);
        return returnResult.toString();
    }
    
    /**
     * 获得评论列表
     * @param eid 事件id
     * @param offset 从第几条开始
     * @param count 是否返回总数 1返回 ， 0不返回
     * @return
     */
    @RequestMapping(value = "/ec/commentList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getCommentList(Integer eid,Integer offset,Integer count){
    	List<EventCommentRecordDto> list  =null;
    	boolean isFindList = true;//是否查询列表
    	int dataCount = 0;
    	count = count==null?0:count;
    	if(eid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"事件id为空");
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        HashMap<String,Object> hm = new HashMap<String,Object>();
    	if(count==1){
    		dataCount = eventCommentService.countEventCommentListByEid(eid);
    		if(dataCount>0){
    			isFindList=true;//无记录不查列表
    		}
    		hm.put("commentCount", dataCount) ;
    	}
    	if(isFindList){
    		list= eventCommentService.getCommentListByEidPage(eid, super.defaultCommentPageSize, offset);
    	}
    	hm.put("commentList", list) ;
        returnResult.setData(hm);
        return returnResult.toString();
    }
    
    /**
     * 评论事件
     * @param eventCycleRecordVo
     * @return
     */
    @RequestMapping(value = "/ec/commentEvent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String commentEvent(EventCommentRecordVo eventCommentRecordVo){
    	if(eventCommentRecordVo.getUid()==null){
    		throw new OptException(ReturnCodeEnum.NO_LOGIN_ERROR);
    	}
    	eventCommentService.toCommentEvent(eventCommentRecordVo);
    	List<EventCommentRecordDto> list = eventCommentService.getCommentListByEidPage
    		(eventCommentRecordVo.getEid(), defaultCommentPageSize, 0);
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        int count = eventCommentService.countEventCommentListByEid(eventCommentRecordVo.getEid());
        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("commentList", list) ;
        hm.put("commentCount", count) ;
        returnResult.setData(hm);
        return returnResult.toString();
    }
}

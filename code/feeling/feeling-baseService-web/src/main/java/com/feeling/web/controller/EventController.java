package com.feeling.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feeling.dto.EventCycleRecordDto;
import com.feeling.dto.EventVoteDto;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.service.EventService;
import com.feeling.service.RedisClient;
import com.feeling.vo.EventCycleRecordVo;
import com.feeling.vo.EventPicVo;
import com.feeling.vo.EventRecommendVo;
import com.feeling.vo.EventVoteVo;
import com.feeling.vo.UserEventVo;
import com.feeling.web.common.ReturnResult;
import com.feeling.web.common.WebFileHelper;

/**
 * @author dutao
 */
@Controller
public class EventController   extends BaseController{

	@Autowired
	private EventService eventService;
    @Autowired
    private WebFileHelper webFileHelper;
    @Autowired
    RedisClient redisClient;
    /**
     * 对投票事件进行投票
     * @param eventVoteVo
     * @return
     */
    @RequestMapping(value = "/event/voteEvent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String voteEvent(EventVoteVo eventVoteVo){
    	if(eventVoteVo==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"投票信息为空");
    	}
    	if(eventVoteVo.getVoteType()==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"投票类型为空");
    	}
    	if(eventVoteVo.getId()==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"投票id为空");
    	}
    	// 1 OK -1:一个都没选 -2 单选选了多个
    	int checkVote = eventVoteVo.checkVote();
    	if(checkVote==-1){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"请选择一个选项进行投票");
    	}
    	if(checkVote==-2){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"只能选择一个选项");
    	}
    	EventVoteDto edto = eventService.voteEvent(eventVoteVo);
    	if(edto!=null){
    		try {
    			BeanUtils.copyProperties(eventVoteVo, edto);
    		} catch (Exception e) {
    			super.writeErrorLog(e.getMessage());
    			throw new OptException(ReturnCodeEnum.ERROR);
    		} 
    	}else{
    		throw new OptException(ReturnCodeEnum.NO_EVENT_VOTE_ERROR,eventVoteVo.getId());
    	}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        returnResult.setData(eventVoteVo);
        return returnResult.toString();
    }
    
    /**
     * 获得事件流转列表
     * @param eid 事件id
     * @return
     */
    @RequestMapping(value = "/event/getEventCycle", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getEventCycle(Integer eid){
    	if(eid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"事件id为空");
    	}
    	LinkedHashSet<List<EventCycleRecordDto>> set = 
    			eventService.getEventCycle(eid);
    	
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        returnResult.setData(set);
        return returnResult.toString();
    }
    
    /**
     * 根据事件id获得事件信息
     * @param eid 事件id
     * @return String
     */
    @RequestMapping(value = "/event/getEventByEid", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getEventByEid(Integer eid){
    	if(eid==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"事件id为空");
    	}
    	 UserEventVo userEventVo = eventService.getEventInfoById(eid);
    	 if(userEventVo!=null){
    		    List<EventPicVo> eventPicVos=  userEventVo.getEventPicVos();
	    		if(eventPicVos!=null&&eventPicVos.size()>0){
	    			for(EventPicVo picVo:eventPicVos){
	    				String picPath = webFileHelper.getEventUrl(picVo.getPicPath());
	    				picVo.setPicPath(picPath);
	    			}
	    		}
		 }
    	 ReturnResult returnResult=new ReturnResult();
         returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
         returnResult.setData(userEventVo);
         return returnResult.toString();
    }
    
    /**
     * 事件忽略
     * @param eventCycleRecordVo
     * @return
     */
    @RequestMapping(value = "/event/skipEvent", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String skipEvent(EventCycleRecordVo eventCycleRecordVo){
    	 return eventOpt(eventCycleRecordVo,false);
    }
    /**
     * 事件传播
     * @param eventCycleRecordVo
     * @return
     */
    @RequestMapping(value = "/event/spreadEvent", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String spreadEvent(EventCycleRecordVo eventCycleRecordVo){
    	 return eventOpt(eventCycleRecordVo,true);
    }
    
    /**
     * 推荐事件
     * @param uid 用户id
     * @param mobile 手机号  ---用户id，手机号 2选1
     * @param lat 
     * @param lon
     * @return
     */
    @RequestMapping(value = "/event/recommendEvent", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String recommendEvent(Integer uid,String deviceId,Double lat,Double lon){
    	if(lat==null||lon==null){
    		throw new OptException(ReturnCodeEnum.EVENT_LOT_ERROR);
    	}
    	List<EventRecommendVo> list =null;
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
    	try {
    		if(uid==null){
    			uid=-1;
    		}
		    list = eventService.recommendEvent(uid, deviceId, lat, lon);
		    if(list!=null){
		    	for(EventRecommendVo rvo:list){
		    		List<EventPicVo> listPic = rvo.getEventPicVos();
		    		if(listPic!=null&&listPic.size()>0){
		    			for(EventPicVo picVo:listPic){
		    				rvo.setRemark(picVo.getRemark());
		    				picVo.setRemark(null);
		    				String picPath = webFileHelper.getEventUrl(picVo.getPicPath());
		    				picVo.setPicPath(picPath);
		    			}
		    		}
		    	}
		    }
		    returnResult.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return returnResult.toString();
    	
    }
    /**
     * 发布投票信息
     * @param eventVo
     * @return
     */
    @RequestMapping(value = "/event/publishVote", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String publishVote(UserEventVo eventVo){
    		checkPublishEventParam(eventVo);
	    	eventService.publishNewEvent(eventVo);
    		ReturnResult returnResult=new ReturnResult();
            returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
            return returnResult.toString();
    }
    /**
     * 发布文本事件
     * @param eventVo
     * @return
     */
    @RequestMapping(value = "/event/publishText", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String publishText(UserEventVo eventVo){
    		checkPublishEventParam(eventVo);
	    	eventService.publishNewEvent(eventVo);
    		ReturnResult returnResult=new ReturnResult();
            returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
            return returnResult.toString();
    }
    
    /**
     * 发布媒体信息
     * @param eventVo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/event/publishMedia", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String publishMedia(UserEventVo eventVo,HttpServletRequest request,HttpServletResponse response){
    		checkPublishEventParam(eventVo);
    		if(eventVo.getEventPicVo()==null||StringUtils.isEmpty(eventVo.getEventPicVo().getRemark())){
        		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"请输入你的描述");
        	}
    		 try {
    			 //可传多个文件
    			 HashMap<String,String> urls = webFileHelper.uploadEvent(request);
    	       	 if(urls!=null&&urls.size()>0){
    	       		List<EventPicVo> eventPicVos = new ArrayList<EventPicVo>();
    	       		Iterator<String> it = urls.keySet().iterator();
    	       		while(it.hasNext()){
    	       			String key = it.next();
    	       			//path,type
    	       			String type = urls.get(key);
    	       			EventPicVo epicVo = new EventPicVo();
    	       			epicVo.setPicPath(key);
    	       			epicVo.setPicType(type);
    	       			epicVo.setRemark(eventVo.getEventPicVo().getRemark());
    	       			eventPicVos.add(epicVo);
    	       		}
    	       		eventVo.setEventPicVos(eventPicVos);
    	       	 }
    			 
    		} catch (Exception e) {
    			super.writeErrorLog(e.getMessage());
    		}
	    	eventService.publishNewEvent(eventVo);
    		ReturnResult returnResult=new ReturnResult();
            returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
            return returnResult.toString();
    }
    
    /**
     * 事件操作【传播，忽略】
     * @param eventCycleRecordVo
     * @param isSpread true 传播行为
     * @return String
     */
    private String eventOpt(EventCycleRecordVo eventCycleRecordVo,boolean isSpread){
    	if(eventCycleRecordVo==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR);
    	}
    	if(eventCycleRecordVo.getUid()==null&&StringUtils.isEmpty(eventCycleRecordVo.getDeviceId())){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户未登录或设备号为空");
    	}
    	if(eventCycleRecordVo.getLat()==null||eventCycleRecordVo.getLon()==null){
    		throw new OptException(ReturnCodeEnum.EVENT_LOT_ERROR);
    	}
    	try {
    		if(isSpread){
    			eventService.spreadEvent(eventCycleRecordVo);
    		}else{
    			eventService.skipEvent(eventCycleRecordVo);
    		}
			
		} catch (Exception e) {
			writeErrorLog(e.getMessage());
			throw new OptException(ReturnCodeEnum.ERROR);
		}
    	ReturnResult returnResult=new ReturnResult();
        returnResult.setResultEnu(ReturnCodeEnum.SUCCESS);
        return returnResult.toString();
    }
    
    /**
     * 检查参数
     * @param eventVo
     */
    private void checkPublishEventParam(UserEventVo eventVo){
    	if(eventVo==null){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR);
    	}
    	if(eventVo.getUid()==null&&StringUtils.isEmpty(eventVo.getDeviceId())){
    		throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户id或设备号至少输入一个");
    	}
    	boolean isLogin = redisClient.checkLoginToken(eventVo.getLoginToken(), eventVo.getUid());
    	if(!isLogin){
    		throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR); 
    	}
    	//经纬度判断
    	if(eventVo.getLat()==null||eventVo.getLon()==null
    			||eventVo.getLat()>90 ||eventVo.getLat()<-90
    			||eventVo.getLon()>180 ||eventVo.getLon()<-180){
    		throw new OptException(ReturnCodeEnum.EVENT_LOT_ERROR);
    	}
    	//设备类型
    	if(StringUtils.isEmpty(eventVo.getDeviceType())){
    		throw new OptException(ReturnCodeEnum.EVENT_DEVICE_ERROR);
    	}
    }
}

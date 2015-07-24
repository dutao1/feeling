package com.feeling.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feeling.dao.EventBaseDao;
import com.feeling.dao.EventCommentRecordDao;
import com.feeling.dao.EventCycleRecordDao;
import com.feeling.dao.EventPicDao;
import com.feeling.dao.EventTextDao;
import com.feeling.dao.EventVoteDao;
import com.feeling.dto.EventBaseDto;
import com.feeling.dto.EventCommentRecordDto;
import com.feeling.dto.EventCycleRecordDto;
import com.feeling.dto.EventPicDto;
import com.feeling.dto.EventTextDto;
import com.feeling.dto.EventVoteDto;
import com.feeling.enums.EventTypeEnum;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.vo.EventCommentRecordVo;
import com.feeling.vo.EventCycleRecordVo;
import com.feeling.vo.EventVo;


/**
 * 
 * 事件相关操作服务
 * @author dutao
 *
 */
@Service
public class EventService extends BaseService{
	
	@Autowired
	EventBaseDao eventBaseDao;//事件基本操作
	@Autowired
	EventVoteDao eventVoteDao;//投票事件操作
	@Autowired
	EventTextDao eventTextDao;//文案事件操作
	@Autowired
	EventPicDao eventPicDao;//图片及视频事件操作
	@Autowired
	EventCycleRecordDao eventCycleRecordDao;//事件流转操作
	@Autowired
	EventCommentRecordDao eventCommentRecordDao;//事件评论操作
	
	
	/**
	 * 根据用户id获取事件信息
	 * @param uid 用户id
	 * @param limit 每页显示条数
	 * @param offset  第几条开始
	 * @return List<EventVo>
	 */
	public List<EventVo> getUserEventByUid(Integer uid,Integer limit,Integer offset){
		List<EventVo> listBack = null;
		if(limit==null||offset==null){
			throw new OptException(ReturnCodeEnum.EVENT_PAGE_ERROR);
		}
		if(uid==null){
			throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"用户id为空");
		}
		List<EventBaseDto> list = eventBaseDao.getEventListByUid(uid, limit, offset);
		if(list!=null&&list.size()>0){
		    listBack = new ArrayList<EventVo>();
			for(EventBaseDto eventBaseDto:list){
				EventVo eventVo = new EventVo();
				try{
					BeanUtils.copyProperties(eventVo, eventBaseDto);
					listBack.add(eventVo);
				}catch(Exception e){
					LogInfo.EVENT_LOG.error(e.getMessage());
					throw new OptException(ReturnCodeEnum.ERROR);
				}
			}
		}
		return  listBack;
	}
	
	/**
	 * 对投票事件进行投票，并返回投票后的结果对象
	 * @param evo
	 * @return EventVoteDto
	 */
	public EventVoteDto voteEvent(EventCycleRecordVo evo){
		//更新投票信息
		EventVoteDto edto  = new EventVoteDto();
		edto.setId(evo.getId());
		edto.setVotes1(evo.getVotes1());
		edto.setVotes2(evo.getVotes2());
		edto.setVotes3(evo.getVotes3());
		edto.setVotes4(evo.getVotes4());
		edto.setVotes5(evo.getVotes5());
		edto.setVotes6(evo.getVotes6());
		eventVoteDao.updateByPk(edto);
		//查询
		return eventVoteDao.selectByPk(edto);
	}
	
	/**
	 * 统计评论数量
	 * @param eid 事件id
	 * @return int
	 */
	public int  countEventCommentListByEid(Integer eid){
		return eventCommentRecordDao.countEventCommentListByEid(eid);
	}
	
	/**
	 * 通过事件id分页获得事件评论列表
	 * @param eid 事件id
	 * @param limit 每页显示数量
	 * @param offset 从第几条开始
	 */
	public List<EventCommentRecordDto> getCommentListByEidPage(Integer eid,Integer limit ,Integer offset){
		
		if(limit==null||offset==null){
			throw new OptException(ReturnCodeEnum.EVENT_PAGE_ERROR);
		}
		return eventCommentRecordDao.
				getEventCommentListByEid(eid, limit, offset);
	}
	
	/**
	 * 对事件进行评论
	 * @param eventCommentRecordVo
	 */
	public void toCommentEvent(EventCommentRecordVo eventCommentRecordVo){
		if(eventCommentRecordVo!=null){
			EventCommentRecordDto eventCommentDto = new EventCommentRecordDto();
			try {
				BeanUtils.copyProperties(eventCommentDto, eventCommentRecordVo);
				eventCommentDto.setLocationLongCode(getLongGeoHash(eventCommentRecordVo.getLat(), eventCommentRecordVo.getLon()));
				eventCommentDto.setLocationHash(getGeoHash(eventCommentRecordVo.getLat(), eventCommentRecordVo.getLon()));
			}   catch (Exception e) {
				LogInfo.EVENT_LOG.error(e.getMessage());
				throw new OptException(ReturnCodeEnum.ERROR);
			}
			eventCommentRecordDao.insertWithOutId(eventCommentDto, 0);
		}
	}
	/**
	 * 根据事件id 获得事件信息
	 * @param eid 事件id
	 * @return EventVo
	 */
	public EventVo getEventInfoById(Integer eid){
		EventBaseDto edto = new EventBaseDto();
		edto.setId(eid);
		edto = eventBaseDao.selectByPk(edto);
		try {
			if(edto!=null){
				EventVo  evo = new EventVo();
				BeanUtils.copyProperties(evo, edto);
				return evo;
			}
		} catch (Exception e) {
			LogInfo.EVENT_LOG.error(e.getMessage());
			throw new OptException(ReturnCodeEnum.ERROR);
		}  
		return null;
	}
	/**
	 * 转发推荐的信息
	 * @param eventCycleRecordVo
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public boolean spreadEvent(EventCycleRecordVo eventCycleRecordVo) throws  Exception{
		//插入记录
		EventCycleRecordDto eventCycleRecordDto=new EventCycleRecordDto();
		BeanUtils.copyProperties(eventCycleRecordDto, eventCycleRecordVo);
		eventCycleRecordDto.setLocationLongCode(getLongGeoHash(eventCycleRecordVo.getLat(), eventCycleRecordVo.getLon()));
		eventCycleRecordDto.setLocationHash(getGeoHash(eventCycleRecordVo.getLat(), eventCycleRecordVo.getLon()));
		eventCycleRecordDao.insertWithOutId(eventCycleRecordDto, 0);
		//更新次数
		EventBaseDto eventBaseDto = new EventBaseDto();
		eventBaseDto.setId(eventCycleRecordVo.getEid());
		eventBaseDto.setSpreadTimes(1);
		eventBaseDao.updateByPk(eventBaseDto) ;
		return true;
	}
	
	/**
	 * 忽略【跳过】推荐的信息
	 * @param eventCycleRecordVo
	 * @return boolean
	 */
	public boolean skipEvent(EventCycleRecordVo eventCycleRecordVo){
		if(eventCycleRecordVo==null){
			throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"无任何参数");
		}
		LogInfo.EVENT_LOG.info("skipEvent.log=="+JSONObject.fromObject(eventCycleRecordVo).toString());
		//更新次数
		EventBaseDto eventBaseDto = new EventBaseDto();
		eventBaseDto.setId(eventCycleRecordVo.getEid());
		eventBaseDto.setSkipTimes(1);
		int result = eventBaseDao.updateByPk(eventBaseDto) ;
		return result==1?true:false;
	}
	
	/**
	 * 获得事件流转生命周期
	 * @param eid
	 * @return LinkedHashSet<List<EventCycleRecordDto>>
	 */
	public LinkedHashSet<List<EventCycleRecordDto>>  getEventCycle(Integer eid){
		List<EventCycleRecordDto> list = 
				eventCycleRecordDao.getEventCycleInfo(eid);
		if(list!=null &&list.size()>0){
			LinkedHashMap<Integer,List<EventCycleRecordDto>> hm = 
					new LinkedHashMap<Integer,List<EventCycleRecordDto>>();
			for(EventCycleRecordDto edto:list){
				Integer fromEid = edto.getFromEid();
				List<EventCycleRecordDto> listOne = hm.get(fromEid);
				if(listOne==null){
					listOne = new ArrayList<EventCycleRecordDto>();
				}
				listOne.add(edto);
				hm.put(fromEid, listOne);
			}
			return new LinkedHashSet<List<EventCycleRecordDto>>(hm.values());
		}
		return null;
	}
	
	/**
	 * 推荐事件
	 */
	public List<EventCycleRecordVo> recommendEvent(int uid,String mobile,double lat,double lon){
		if(mobile==null){
			mobile = "";
		}
		//距离最近的事件信息
		List<EventCycleRecordDto> eventList =
				eventCycleRecordDao.getNearEventList(uid,mobile,super.getLongGeoHash(lat, lon));
		
		if(eventList!=null &&!eventList.isEmpty()){
			LinkedHashMap<Integer,EventCycleRecordVo> hm = new LinkedHashMap<Integer,EventCycleRecordVo>();
			for(EventCycleRecordDto erd:eventList){
				EventCycleRecordVo crcleVo = new EventCycleRecordVo();
				Integer eid = erd.getEid();
				if(hm.get(eid)==null){
					crcleVo.setFromEid(erd.getId());
					crcleVo.setEid(eid);
					hm.put(eid, crcleVo);
				}
			}
			Integer ids[] = (Integer[])hm.keySet().toArray(new Integer[hm.keySet().size()]);
			List<EventBaseDto> list = eventBaseDao.getEventListByIdList(ids);
			if(list!=null){
				for(EventBaseDto eventBase:list){
					EventCycleRecordVo cycleVo = hm.get(eventBase.getId());
					if(cycleVo!=null){
						cycleVo.setCommentTimes(eventBase.getCommentTimes());
						cycleVo.setSpreadTimes(eventBase.getSpreadTimes());
						//TODO 需要确认
						
						
						
						//eventBase.set
						//cycleVo.set
						//eventBase.getEventCity()
					//	cycleVo.set
					}
				}
			}
		}
		else{
			throw new OptException(ReturnCodeEnum.EVENT_NO_INFO_ERROR);
		}
		return null;
	}
	
	/**
	 * 发布新事件
	 * 
	 * 包括 视频/图片,文案，投票
	 * @param eventVo
	 */
	@Transactional
	public void publishNewEvent(EventVo eventVo){
		
		if(eventVo==null){
			return ;
		}
		String eventType = eventVo.getEventType();
		if(StringUtils.isEmpty(eventType)){
			throw new OptException(ReturnCodeEnum.EVENT_TYPE_EMPTY_ERROR);
		}
		 EventBaseDto eventBaseDto = new EventBaseDto();
		 Integer eventId = null;
		 try {
			BeanUtils.copyProperties(eventBaseDto, eventVo);
			eventBaseDto.setLocationLongCode(getLongGeoHash(eventBaseDto.getLat(), eventBaseDto.getLon()));
			eventBaseDto.setLocationHash(getGeoHash(eventBaseDto.getLat(), eventBaseDto.getLon()));
			//1 插入事件基本表
			eventBaseDao.insertWithId(eventBaseDto);
			eventId = eventBaseDto.getId();
			if(eventId==null){
				LogInfo.EVENT_LOG.error(ReturnCodeEnum.EVENT_PUBLISH_ERROR.getMessage());
				throw new OptException(ReturnCodeEnum.EVENT_PUBLISH_ERROR);
			}
			//2.事件分支表
			insertEventDetailInfo(eventVo,eventId);
			//3.事件流转信息表
			EventCycleRecordDto eCycleDto = new EventCycleRecordDto();
			BeanUtils.copyProperties(eCycleDto, eventBaseDto);
			eCycleDto.setFromEid(0);//初始化0
			eCycleDto.setEid(eventId);
			eventCycleRecordDao.insertWithOutId(eCycleDto, 0);
		} catch (Exception e) {
			LogInfo.EVENT_LOG.error(e.getMessage());
			throw new OptException(ReturnCodeEnum.ERROR);
		}  
	}
	
	/**
	 * 插入事件基础信息
	 * @param eventVo 事件信息
	 * @param eventId 事件id
	 * @throws Exception
	 */
	public void insertEventDetailInfo(EventVo eventVo,Integer eventId) throws Exception{
		String eventType = eventVo.getEventType(); 
		//文本事件
		if(eventType.equals(EventTypeEnum.TEXT.getName())){
			EventTextDto eventTextDto = new EventTextDto();
			eventTextDto.setEid(eventId);
			eventTextDto.setUid(eventVo.getUid());
			eventTextDto.setContent(eventVo.getContent());
			eventTextDao.insertWithOutId(eventTextDto, 0);
			
		}
		//投票事件
		else if(eventType.equals(EventTypeEnum.VOTE.getName())){
			EventVoteDto eventVoteDto = new EventVoteDto();
			eventVoteDto.setEid(eventId);
			eventVoteDto.setUid(eventVo.getUid());
			eventVoteDto.setVoteType(eventVo.getVoteType());
			eventVoteDto.setTitle(eventVo.getTitle());
			eventVoteDto.setOption1(eventVo.getOption1());
			eventVoteDto.setOption2(eventVo.getOption2());
			eventVoteDto.setOption3(eventVo.getOption3());
			eventVoteDto.setOption4(eventVo.getOption4());
			eventVoteDto.setOption5(eventVo.getOption5());
			eventVoteDto.setOption6(eventVo.getOption6());
			eventVoteDao.insertWithOutId(eventVoteDto, 0);
		}
		//其他事件
		else{
			EventPicDto eventPicDto = new EventPicDto();
			eventPicDto.setEid(eventId);
			eventPicDto.setPicPath(eventVo.getPicPath());
			eventPicDto.setPicType(eventVo.getPicType());
			eventPicDto.setRemark(eventVo.getRemark());
			eventPicDto.setUid(eventVo.getUid());
			eventPicDao.insertWithOutId(eventPicDto, 0);
		}
	}
}

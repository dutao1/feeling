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
import com.feeling.dao.EventCycleRecordDao;
import com.feeling.dao.EventPicDao;
import com.feeling.dao.EventTextDao;
import com.feeling.dao.EventVoteDao;
import com.feeling.dto.EventBaseDto;
import com.feeling.dto.EventCycleRecordDto;
import com.feeling.dto.EventPicDto;
import com.feeling.dto.EventTextDto;
import com.feeling.dto.EventVoteDto;
import com.feeling.enums.EventTypeEnum;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.vo.EventCycleRecordVo;
import com.feeling.vo.EventPicVo;
import com.feeling.vo.EventRecommendVo;
import com.feeling.vo.EventTextVo;
import com.feeling.vo.EventVoteVo;
import com.feeling.vo.UserEventVo;

/**
 * 
 * 事件相关操作服务
 * 
 * @author dutao
 * 
 */
@Service
public class EventService extends BaseService {

	@Autowired
	EventBaseDao eventBaseDao;// 事件基本操作
	@Autowired
	EventVoteDao eventVoteDao;// 投票事件操作
	@Autowired
	EventTextDao eventTextDao;// 文案事件操作
	@Autowired
	EventPicDao eventPicDao;// 图片及视频事件操作
	@Autowired
	EventCycleRecordDao eventCycleRecordDao;// 事件流转操作
	
	/**
	 * 查询用id 发布的事件数量
	 * @param uid 用户id
	 * @return int
	 */
	public int countEventListByUid(Integer uid){
		if (uid == null) {
			throw new OptException(ReturnCodeEnum.PARAMETER_ERROR, "用户id为空");
		}
		return eventBaseDao.countEventListByUid(uid);
	}
	
	/**
	 * 根据用户id获取事件信息
	 * 
	 * @param uid
	 *            用户id
	 * @param limit
	 *            每页显示条数
	 * @param offset
	 *            第几条开始
	 * @return List<EventVo>
	 */
	public List<UserEventVo> getUserEventByUid(Integer uid, Integer limit,
			Integer offset) {
		List<UserEventVo> listBack = null;
		if (limit == null || offset == null) {
			throw new OptException(ReturnCodeEnum.EVENT_PAGE_ERROR);
		}
		if (uid == null) {
			throw new OptException(ReturnCodeEnum.PARAMETER_ERROR, "用户id为空");
		}
		List<EventBaseDto> list = eventBaseDao.getEventListByUid(uid, limit,
				offset);
		if (list != null && list.size() > 0) {
			listBack = new ArrayList<UserEventVo>();
			for (EventBaseDto eventBaseDto : list) {
				try {
					listBack.add(setUserEventDetails(eventBaseDto));
				} catch (Exception e) {
					LogInfo.EVENT_LOG.error(e.getMessage());
					throw new OptException(ReturnCodeEnum.ERROR);
				}
			}
		}
		return listBack;
	}

	/**
	 * 对投票事件进行投票，并返回投票后的结果对象
	 * 
	 * @param evo
	 * @return EventVoteDto
	 */
	public EventVoteDto voteEvent(EventVoteVo evo) {
		// 更新投票信息
		EventVoteDto edto = new EventVoteDto();
		edto.setId(evo.getId());
		edto.setVotes1(evo.getVotes1());
		edto.setVotes2(evo.getVotes2());
		edto.setVotes3(evo.getVotes3());
		edto.setVotes4(evo.getVotes4());
		edto.setVotes5(evo.getVotes5());
		edto.setVotes6(evo.getVotes6());
		eventVoteDao.updateByPk(edto);
		// 查询
		return eventVoteDao.selectByPk(edto);
	}

	/**
	 * 根据事件id 获得事件信息
	 * 
	 * @param eid
	 *            事件id
	 * @return EventVo
	 */
	public UserEventVo getEventInfoById(Integer eid) {
		EventBaseDto edto = new EventBaseDto();
		edto.setId(eid);
		edto = eventBaseDao.selectByPk(edto);
		try {
			if (edto != null) {
				return setUserEventDetails(edto);
			}
		} catch (Exception e) {
			LogInfo.EVENT_LOG.error(e.getMessage());
			throw new OptException(ReturnCodeEnum.ERROR);
		}
		return null;
	}

	/**
	 * 转发推荐的信息
	 * 
	 * @param eventCycleRecordVo
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public boolean spreadEvent(EventCycleRecordVo eventCycleRecordVo)
			throws Exception {
		// 插入记录
		EventCycleRecordDto eventCycleRecordDto = new EventCycleRecordDto();
		BeanUtils.copyProperties(eventCycleRecordDto, eventCycleRecordVo);
		eventCycleRecordDto.setLocationLongCode(getLongGeoHash(
				eventCycleRecordVo.getLat(), eventCycleRecordVo.getLon()));
		eventCycleRecordDto.setLocationHash(getGeoHash(
				eventCycleRecordVo.getLat(), eventCycleRecordVo.getLon()));
		eventCycleRecordDao.insertWithOutId(eventCycleRecordDto, 0);
		// 更新次数
		EventBaseDto eventBaseDto = new EventBaseDto();
		eventBaseDto.setId(eventCycleRecordVo.getEid());
		eventBaseDto.setSpreadTimes(1);
		eventBaseDao.updateByPk(eventBaseDto);
		return true;
	}

	/**
	 * 忽略【跳过】推荐的信息
	 * 
	 * @param eventCycleRecordVo
	 * @return boolean
	 */
	public boolean skipEvent(EventCycleRecordVo eventCycleRecordVo) {
		if (eventCycleRecordVo == null) {
			throw new OptException(ReturnCodeEnum.PARAMETER_ERROR, "无任何参数");
		}
		LogInfo.EVENT_LOG.info("skipEvent.log=="
				+ JSONObject.fromObject(eventCycleRecordVo).toString());
		// 更新次数
		EventBaseDto eventBaseDto = new EventBaseDto();
		eventBaseDto.setId(eventCycleRecordVo.getEid());
		eventBaseDto.setSkipTimes(1);
		int result = eventBaseDao.updateByPk(eventBaseDto);
		return result == 1 ? true : false;
	}

	/**
	 * 获得事件流转生命周期
	 * 
	 * @param eid
	 * @return LinkedHashSet<List<EventCycleRecordDto>>
	 */
	public LinkedHashSet<List<EventCycleRecordDto>> getEventCycle(Integer eid) {
		List<EventCycleRecordDto> list = eventCycleRecordDao
				.getEventCycleInfo(eid);
		if (list != null && list.size() > 0) {
			LinkedHashMap<Integer, List<EventCycleRecordDto>> hm = new LinkedHashMap<Integer, List<EventCycleRecordDto>>();
			for (EventCycleRecordDto edto : list) {
				Integer fromEid = edto.getFromEid();
				List<EventCycleRecordDto> listOne = hm.get(fromEid);
				if (listOne == null) {
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
	@SuppressWarnings("static-access")
	public List<EventRecommendVo> recommendEvent(int uid, String deviceId,
			double lat, double lon) throws Exception {
		List<EventRecommendVo> resultList = null;
		if (deviceId == null) {
			deviceId = "";
		}
		// 1.获取距离最近的事件信息
		List<EventCycleRecordDto> eventList = eventCycleRecordDao
				.getNearEventList(uid, deviceId, super.getLongGeoHash(lat, lon));
		//1.1 根据事件id 转换成map，下一步根据id列表批量查时间基本信息
		if (eventList != null && !eventList.isEmpty()) {
			LinkedHashMap<Integer, EventRecommendVo> hm = new LinkedHashMap<Integer, EventRecommendVo>();
			for (EventCycleRecordDto erd : eventList) {
				EventRecommendVo recommendVo = new EventRecommendVo();
				Integer eid = erd.getEid();
				if (hm.get(eid) == null) {
					//添加最近一次传播相关数据到VO
					recommendVo.setId(erd.getId());
					recommendVo.setEid(eid);
					recommendVo.setUpdateTime(erd.getCreateTime());
					hm.put(eid, recommendVo);
				}
			}
			//2.批量查事件基本信息
			Integer ids[] = (Integer[]) hm.keySet().toArray(
					new Integer[hm.keySet().size()]);
			List<EventBaseDto> list = eventBaseDao.getEventListByIdList(ids);
			if (list != null) {
				for (EventBaseDto eventBase : list) {
					EventRecommendVo recommendVo = hm.get(eventBase.getId());
					if (recommendVo != null) {
						//添加时间基础信息
						recommendVo.setCommentTimes(eventBase.getCommentTimes());
						recommendVo.setSpreadTimes(eventBase.getSpreadTimes());
						recommendVo.setSkipTimes(eventBase.getSkipTimes());
						recommendVo.setCreateTime(eventBase.getCreateTime());
						recommendVo.setEventType(eventBase.getEventType());
						recommendVo.setLat(eventBase.getLat());
						recommendVo.setLon(eventBase.getLon());
						recommendVo.setEventCity(eventBase.getEventCity());
						recommendVo.setNickName(eventBase.getNickName());
						recommendVo.setMobile(eventBase.getMobile());
						recommendVo.setCreateTime(eventBase.getCreateTime());
						if(eventBase.getLat()!=null&&eventBase.getLon()!=null){
							double dist = geoHash.getPointDistance
										(lat, lon, eventBase.getLat(),eventBase.getLon());
							recommendVo.setDistMeter(dist);
							recommendVo.setDistKm(dist/1000);
						}
						UserEventVo userEventVo = setUserEventDetails(eventBase);
						recommendVo.setEventTextVo(userEventVo.getEventTextVo());
						recommendVo.setEventPicVos(userEventVo.getEventPicVos());
						recommendVo.setEventVoteVo(userEventVo.getEventVoteVo());
						hm.put(eventBase.getId(), recommendVo);
					}
				}
			}
			resultList = new ArrayList<EventRecommendVo>(hm.values());
		} else {
			throw new OptException(ReturnCodeEnum.EVENT_NO_INFO_ERROR);
		}
		return resultList;
	}

	/**
	 * 发布新事件
	 * 
	 * 包括 视频/图片,文案，投票
	 * 
	 * @param eventVo
	 */
	@Transactional
	public void publishNewEvent(UserEventVo eventVo) {

		if (eventVo == null) {
			return;
		}
		String eventType = eventVo.getEventType();
		if (StringUtils.isEmpty(eventType)) {
			throw new OptException(ReturnCodeEnum.EVENT_TYPE_EMPTY_ERROR);
		}
		EventBaseDto eventBaseDto = new EventBaseDto();
		Integer eventId = null;
		try {
			BeanUtils.copyProperties(eventBaseDto, eventVo);
			eventBaseDto.setLocationLongCode(getLongGeoHash(
					eventBaseDto.getLat(), eventBaseDto.getLon()));
			eventBaseDto.setLocationHash(getGeoHash(eventBaseDto.getLat(),
					eventBaseDto.getLon()));
			// 1 插入事件基本表
			Integer uid = eventBaseDto.getUid();
			//游客的昵称是 deviceid 后6位
			if(uid==null){
				String deviceId = eventBaseDto.getDeviceId();
				if(StringUtils.isNotEmpty(deviceId)){
					if(deviceId.length()>6){
						deviceId = deviceId.substring(deviceId.length()-6);
					}
					eventBaseDto.setNickName(deviceId);
				}
			}
			eventBaseDao.insertWithId(eventBaseDto);
			eventId = eventBaseDto.getId();
			if (eventId == null) {
				LogInfo.EVENT_LOG.error(ReturnCodeEnum.EVENT_PUBLISH_ERROR
						.getMessage());
				throw new OptException(ReturnCodeEnum.EVENT_PUBLISH_ERROR);
			}
			// 2.事件分支表
			insertEventDetailInfo(eventVo, eventId);
			// 3.事件流转信息表
			EventCycleRecordDto eCycleDto = new EventCycleRecordDto();
			BeanUtils.copyProperties(eCycleDto, eventBaseDto);
			eCycleDto.setFromEid(0);// 初始化0
			eCycleDto.setEid(eventId);
			eventCycleRecordDao.insertWithOutId(eCycleDto, 0);
		} catch (Exception e) {
			LogInfo.EVENT_LOG.error(e.getMessage());
			throw new OptException(ReturnCodeEnum.ERROR);
		}
	}

	/**
	 * 插入事件基础信息
	 * 
	 * @param eventVo
	 *            事件信息
	 * @param eventId
	 *            事件id
	 * @throws Exception
	 */
	private void insertEventDetailInfo(UserEventVo eventVo, Integer eventId)
			throws Exception {
		String eventType = eventVo.getEventType();
		// 文本事件
		if (eventType.equals(EventTypeEnum.TEXT.getName())) {
			if (eventVo.getEventTextVo() == null 
		||StringUtils.isEmpty(eventVo.getEventTextVo().getContent())) {
				throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"文字信息为空");
			}
			EventTextDto eventTextDto = new EventTextDto();
			eventTextDto.setEid(eventId);
			eventTextDto.setUid(eventVo.getUid());
			eventTextDto.setContent(eventVo.getEventTextVo().getContent());
			eventTextDao.insertWithOutId(eventTextDto, 0);

		}
		// 投票事件
		else if (eventType.equals(EventTypeEnum.VOTE.getName())) {
			EventVoteVo voteVo = eventVo.getEventVoteVo();
			if (voteVo == null ||
					voteVo.getVoteType()==null ||
					StringUtils.isEmpty(voteVo.getTitle())) {
				throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"投票信息为空");
			}
			EventVoteDto eventVoteDto = new EventVoteDto();
			eventVoteDto.setEid(eventId);
			eventVoteDto.setUid(eventVo.getUid());
			eventVoteDto.setVoteType(voteVo.getVoteType());
			eventVoteDto.setTitle(voteVo.getTitle());
			eventVoteDto.setOption1(voteVo.getOption1());
			eventVoteDto.setOption2(voteVo.getOption2());
			eventVoteDto.setOption3(voteVo.getOption3());
			eventVoteDto.setOption4(voteVo.getOption4());
			eventVoteDto.setOption5(voteVo.getOption5());
			eventVoteDto.setOption6(voteVo.getOption6());
			eventVoteDao.insertWithOutId(eventVoteDto, 0);
		}
		// 其他事件
		else {
			List<EventPicVo> picVos = eventVo.getEventPicVos();
			if (picVos == null||picVos.size()<1) {
				throw new OptException(ReturnCodeEnum.PARAMETER_ERROR,"至少上传一个文件");
			}
			for(EventPicVo epic:picVos){
				EventPicDto eventPicDto = new EventPicDto();
				eventPicDto.setEid(eventId);
				eventPicDto.setPicPath(epic.getPicPath());
				eventPicDto.setPicType(epic.getPicType());
				eventPicDto.setRemark(epic.getRemark());
				eventPicDto.setUid(eventVo.getUid());
				eventPicDao.insertWithOutId(eventPicDto, 0);
			}
		}
	}

	/**
	 * 通过事件基础信息获得事件相关细节数据
	 * 
	 * @param eventBaseDto
	 * @return UserEventVo
	 * @throws Exception
	 */
	private UserEventVo setUserEventDetails(EventBaseDto eventBaseDto)
			throws Exception {
		UserEventVo eventVo = new UserEventVo();
		BeanUtils.copyProperties(eventVo, eventBaseDto);
		if (eventVo.getEventType().equals(EventTypeEnum.TEXT.getName())) {
			EventTextDto edto = eventTextDao.getEventByEid(eventVo.getId());
			if (edto != null) {
				EventTextVo eventTextVo = new EventTextVo();
				BeanUtils.copyProperties(eventTextVo, edto);
				eventVo.setEventTextVo(eventTextVo);
			}

		} else if (eventVo.getEventType().equals(EventTypeEnum.VOTE.getName())) {
			EventVoteDto edto = eventVoteDao.getEventByEid(eventVo.getId());
			if (edto != null) {
				EventVoteVo eventVoteVo = new EventVoteVo();
				BeanUtils.copyProperties(eventVoteVo, edto);
				eventVo.setEventVoteVo(eventVoteVo);
			}
		} else {
			List<EventPicDto> edtos = eventPicDao.getEventByEid(eventVo.getId());
			if (edtos != null) {
				List<EventPicVo> ePicVos = new ArrayList<EventPicVo>();
				for(EventPicDto edto:edtos){
					EventPicVo eventPicVo = new EventPicVo();
					BeanUtils.copyProperties(eventPicVo, edto);
					ePicVos.add(eventPicVo);
				}
			}
		}
		return eventVo;
	}
}

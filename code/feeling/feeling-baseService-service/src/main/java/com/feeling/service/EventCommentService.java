package com.feeling.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feeling.dao.EventBaseDao;
import com.feeling.dao.EventCommentRecordDao;
import com.feeling.dto.EventBaseDto;
import com.feeling.dto.EventCommentRecordDto;
import com.feeling.enums.ReturnCodeEnum;
import com.feeling.exception.OptException;
import com.feeling.log.LogInfo;
import com.feeling.vo.EventCommentRecordVo;


@Service
public class EventCommentService  extends BaseService{

	@Autowired
	EventCommentRecordDao eventCommentRecordDao;//事件评论操作
	@Autowired
	EventBaseDao eventBaseDao;//事件基本操作
	@Autowired
    RedisClient redisClient;
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
			boolean login = redisClient.checkLoginToken(eventCommentRecordVo.getLoginToken(), eventCommentRecordVo.getUid());
			if(!login){
				throw new OptException(ReturnCodeEnum.LOGIN_TOKEN_ERROR);
			}
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
			EventBaseDto edto = new EventBaseDto();
			edto.setId(eventCommentRecordVo.getEid());
			edto.setCommentTimes(1);//评论数加1
			eventBaseDao.updateByPk(edto);
		}
	}
}

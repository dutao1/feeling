package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventCycleRecordDto;

/**
 * 事件流转dao
 * @author dutao
 */
public interface EventCycleRecordDao  extends  BaseDao<EventCycleRecordDto> {
	/**
	 * 获得距离最近的事件列表10条
	 * @param uid 用户id
	 * @param mobile 手机号
	 * @return  List<EventCycleRecordDto>
	 */
	@Select(SqlConstants.LIST_NEAR_EVENTS)
	public List<EventCycleRecordDto> getNearEventList(Integer uid,String mobile);
	
	/**
	 * 根据事件id 查询事件的流转周期
	 * @param eid
	 * @return List<EventCycleRecordDto>
	 */
	@Select(SqlConstants.GET_EVENT_CYCLE_BY_ID)
	public List<EventCycleRecordDto> getEventCycleInfo(@Param("eid")Integer eid);
	
}

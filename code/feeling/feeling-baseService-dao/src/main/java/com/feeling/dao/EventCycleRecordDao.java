package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
	 * @param deviceId  设备号
	 * @param locationLongCode
	 * @return List<EventCycleRecordDto>
	 */
	@Select(SqlConstants.LIST_NEAR_EVENTS)
	public List<EventCycleRecordDto> getNearEventList(@Param("uid")Integer uid,@Param("deviceId")String deviceId,@Param("locationLongCode")Long locationLongCode);
	
	/**
	 * 根据事件id 查询事件的流转周期
	 * @param eid
	 * @return List<EventCycleRecordDto>
	 */
	@Select(SqlConstants.GET_EVENT_CYCLE_BY_ID)
	public List<EventCycleRecordDto> getEventCycleInfo(@Param("eid")Integer eid);
	
	/**
	 * 更新事件相关状态
	 * @param eid
	 * @param status
	 */
	@Update("update event_cycle_record set status=#{status} where eid=#{eid}")
	public void updateEventCycleStatus(@Param("eid")Integer eid,@Param("status")Integer status);
}

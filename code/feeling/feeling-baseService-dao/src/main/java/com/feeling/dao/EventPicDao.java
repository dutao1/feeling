package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventPicDto;

/**
 * 图片事件dao
 * @author dutao
 *
 */
public interface EventPicDao  extends  BaseDao<EventPicDto> {
	
	@Select(SqlConstants.GET_EVENT_PIC_LIST_BY_EID)
	public List<EventPicDto> getEventByEid(@Param("eid")Integer eid);
}

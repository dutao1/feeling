package com.feeling.dao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventTextDto;

/**
 * 文字事件dao
 * @author dutao
 *
 */
public interface EventTextDao  extends  BaseDao<EventTextDto> {

	@Select(SqlConstants.GET_EVENT_TEXT_BY_EID)
	public EventTextDto getEventByEid(@Param("eid")Integer eid);
}

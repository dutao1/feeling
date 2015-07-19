package com.feeling.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventVoteDto;

/**
 * 投票事件dao
 * @author dutao
 *
 */
public interface EventVoteDao  extends  BaseDao<EventVoteDto> {

	
	@Select(SqlConstants.GET_EVENT_VOTE_BY_EID)
	public EventVoteDto getEventByEid(@Param("eid")Integer eid);
}

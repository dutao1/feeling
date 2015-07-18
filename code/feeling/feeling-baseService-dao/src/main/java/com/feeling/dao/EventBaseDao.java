package com.feeling.dao;

import org.apache.ibatis.annotations.Insert;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventBaseDto;


public interface EventBaseDao   extends  BaseDao<EventBaseDto> {
	
	@Insert(SqlConstants.INSERT_EVENT_BASE_SQL)
	public void insertWithId(EventBaseDto eventBaseDto);
}

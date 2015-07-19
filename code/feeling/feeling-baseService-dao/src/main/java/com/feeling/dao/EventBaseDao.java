package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventBaseDto;

/**
 * 事件基础dao
 * @author dutao
 *
 */
public interface EventBaseDao   extends  BaseDao<EventBaseDto> {
	
	@Insert(SqlConstants.INSERT_EVENT_BASE_SQL)
	public void insertWithId(EventBaseDto eventBaseDto);
	
	/*
	 * 获得用户id发布的事件
	 * @param uid 用户id
	 * @param limit 每次显示条数
	 * @param offset 列表索引位置
	 * @return 返回 List<EventBaseDto> 
	 */
	@Select(SqlConstants.GET_EVENT_LIST_BY_UID)
	public List<EventBaseDto> getEventListByUid(@Param("uid")Integer uid,@Param("limit")int limit,@Param("offset") int offset);
}

package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.feeling.constants.SqlConstants;
import com.feeling.dto.EventBaseDto;
import com.feeling.dto.EventCommentRecordDto;

/**
 * 事件评论记录dao
 * @author dutao
 *
 */
public interface EventCommentRecordDao  extends  BaseDao<EventCommentRecordDto> {
	
	/**
	 * 根据事件id查询事件评论列表
	 * @param eid
	 * @param limit
	 * @param offset
	 * @return List<EventCommentRecordDto>
	 */
	@Select(SqlConstants.GET_EVENT_COMMENT_LIST_BY_EVENTID)
	public List<EventCommentRecordDto> getEventCommentListByEid(@Param("eid")Integer eid,@Param("limit")int limit,@Param("offset")int offset);
}

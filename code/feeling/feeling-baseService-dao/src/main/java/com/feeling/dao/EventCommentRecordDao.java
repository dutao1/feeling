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
	 * @param eid  事件id
	 * @param limit 每页显示数量
	 * @param offset 从第几条开始
	 * @return List<EventCommentRecordDto>
	 */
	@Select(SqlConstants.GET_EVENT_COMMENT_LIST_BY_EVENTID)
	public List<EventCommentRecordDto> getEventCommentListByEid(@Param("eid")Integer eid,@Param("limit")int limit,@Param("offset")int offset);
	
	/**
	 * 根据事件id查询事件评论列表
	 * @param eid  事件id
	 * @return int 总数
	 */
	@Select(SqlConstants.COUNT_EVENT_COMMENT_LIST_BY_EVENTID)
	public int countEventCommentListByEid(@Param("eid")Integer eid);

}

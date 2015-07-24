package com.feeling.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.feeling.constants.SqlConstants;
import com.feeling.dao.sqlProvider.EventSqlProvider;
import com.feeling.dto.EventBaseDto;

/**
 * 事件基础dao
 * @author dutao
 *
 */
public interface EventBaseDao   extends  BaseDao<EventBaseDto> {
	
	/**
	 * 插入事件信息
	 * @param eventBaseDto
	 */
	@Insert(SqlConstants.INSERT_EVENT_BASE_SQL)
	public void insertWithId(EventBaseDto eventBaseDto);
	 
	/**
	 * 返回用户事件数量
	 * @param uid 用户id
	 * @return  int
	 */
	@Select(SqlConstants.COUNT_EVENT_LIST_BY_UID)
	public int countEventListByUid(@Param("uid")Integer uid);
	/**
	 * 获得用户id发布的事件
	 * @param uid  用户id
	 * @param limit  每次显示条数
	 * @param offset  每次显示条数
	 * @return 返回 List<EventBaseDto> 
	 */
	@Select(SqlConstants.GET_EVENT_LIST_BY_UID)
	public List<EventBaseDto> getEventListByUid(@Param("uid")Integer uid,@Param("limit")int limit,@Param("offset") int offset);
	/**
	 * 根据事件id 查询事件信息
	 * @param ids
	 * @return
	 */
	@SelectProvider(type=EventSqlProvider.class,method = "getEventListByIdList")
	public List<EventBaseDto> getEventListByIdList(@Param("ids")Integer[] ids);

}

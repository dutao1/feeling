package com.feeling.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.feeling.constants.SqlConstants;
import com.feeling.dao.sqlProvider.BaseSqlProvider;
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

	@SelectProvider(type=BaseSqlProvider.class,method = "selectByPk")
	public EventBaseDto selectByPk(@Param("tVo") EventBaseDto tVo);
	
	/**
	 * 更新事件相关状态
	 * @param eid
	 * @param status
	 */
	@Update("update event_base_info set status=#{status} where id=#{id}")
	public void updateEventStatus(@Param("id")Integer id,@Param("status")Integer status);

	/**
	 * 查出过期的事件返回id
	 * @param createDate
	 * @return List<EventBaseDto>
	 */
	@Select("select id from  event_base_info where status=1 and  create_time<=#{createDate} limit 200 ")
	public List<EventBaseDto> getExpireEventList(Date createDate);
}

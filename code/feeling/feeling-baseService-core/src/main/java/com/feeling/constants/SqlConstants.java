package com.feeling.constants;

import org.apache.ibatis.annotations.InsertProvider;

/**
 * sql常量
 * @author dutao
 *
 */
public class SqlConstants {
	public static  final String ID = "id";
	public static  final String TABLE_SPLIT_LINE = "_";
	public static  final String PARAM_NAME = "tVo";
	public static  final String CREATE_TIME = "create_time";
	
	public static final String USER_BASE_TABLE="user_base_info";//用户基本信息
	public static final String EVENT_BASE_TABLE="event_base_info";//事件基本信息
	
	public static final String EVENT_COMMENT_TABLE="event_comment_record";//事件评论
	public static final String EVENT_CYCLE_TABLE="event_cycle_record";//事件传播与流转
	public static final String EVENT_PIC_TABLE="event_pic";//事件图片信息
	public static final String EVENT_TEXT_TABLE="event_text";//事件文字
	public static final String EVENT_VOTE_TABLE="event_vote";//事件投票
	public static final String USER_LOCUS_TABLE="user_locus_info";//用户行为记录
	
	
	/**
	 * 用户资料插入语句--本来可以反射 
	 * 原因是：InsertProvider无法返回主键
	 */
	public static final String INSERT_USER_BASE_SQL="insert into "+USER_BASE_TABLE+""
			+ " (mobile,nick_name,gender,pwd,city_code,birthday,avatar,status,create_time) "
			+ "values (#{mobile},#{nickName},#{gender},#{pwd},#{cityCode},#{birthday},#{avatar},#{status},now())";
	
	/**
	 * 事件基本资料插入语句--同上
	 */
	public static final String INSERT_EVENT_BASE_SQL="insert into "+EVENT_BASE_TABLE+""
			+ " (uid,event_type,event_city,lat,lon,location_long_code,is_display,create_time,location_hash,mobile,nick_name,device_type,device_imei,device_id) "
			+ "values (#{uid},#{eventType},#{eventCity},#{lat},#{lon},#{locationLongCode},#{isDisplay},now(),#{locationHash},#{mobile},#{nickName},#{deviceType},#{deviceImei},#{deviceId})";
	
	
}

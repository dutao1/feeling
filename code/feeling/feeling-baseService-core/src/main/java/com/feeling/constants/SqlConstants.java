package com.feeling.constants;

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
	
	public static final String USER_BASE_TABLE="user_base_info";
	public static final String EVENT_BASE_TABLE="event_base_info";
	
	
	public static final String INSERT_USER_BASE_SQL="insert into "+USER_BASE_TABLE+""
			+ " (mobile,nick_name,gender,pwd,city_code,birthday,avatar,status,create_time) "
			+ "values (#{mobile},#{nickName},#{gender},#{pwd},#{cityCode},#{birthday},#{avatar},#{status},now())";
	
	public static final String INSERT_EVENT_BASE_SQL="insert into "+EVENT_BASE_TABLE+""
			+ " (uid,event_type,event_city,lat,lon,location_long_code,is_display,create_time,location_hash,mobile,nick_name,device_type,device_imei,device_id) "
			+ "values (#{uid},#{eventType},#{eventCity},#{lat},#{lon},#{locationLongCode},#{isDisplay},now(),#{locationHash},#{mobile},#{nickName},#{deviceType},#{deviceImei},#{deviceId})";
	
	
}

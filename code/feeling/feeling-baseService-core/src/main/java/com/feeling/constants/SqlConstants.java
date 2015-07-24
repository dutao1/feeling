package com.feeling.constants;


/**
 * sql常量
 * @author dutao
 *
 */
/**
 * @author Lenovo
 *
 */
public class SqlConstants {
	public static  final String ID = "id";
	public static  final String TABLE_SPLIT_LINE = "_";
	public static  final String PARAM_NAME = "tVo";
	public static  final String CREATE_TIME = "create_time";
	public static  final String NO_EQUAL_SQL = " 1=2 ";
	public static  final String WHERE = " where ";
	
	public static final String USER_BASE_TABLE="user_base_info";//用户基本信息
	public static final String EVENT_BASE_TABLE="event_base_info";//事件基本信息
	
	public static final String EVENT_COMMENT_TABLE="event_comment_record";//事件评论
	public static final String EVENT_CYCLE_TABLE="event_cycle_record";//事件传播与流转
	public static final String EVENT_PIC_TABLE="event_pic";//事件图片信息
	public static final String EVENT_TEXT_TABLE="event_text";//事件文字
	public static final String EVENT_VOTE_TABLE="event_vote";//事件投票
	public static final String USER_LOCUS_TABLE="user_locus_info";//用户行为记录
	
	public static final int NEAR_LIMIT_NUMS =10;//推荐数量
	
	
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
	
	
	/**
	 * 根据用户昵称和密码查询是否存在用户
	 */
	public static final String CHECK_PWD_SQL = "select id,avatar,status,mobile from "+USER_BASE_TABLE+" where nick_name=#{nickName} and pwd=#{pwd} limit 1" ;

	/**
	 * 取离最近的前10条记录
	 */
	public static final String LIST_NEAR_EVENTS="SELECT id"
			        + " ,eid FROM (  SELECT id,lat,lon,location_long_code,"
			        + " location_hash,eid,ABS(location_long_code-#{locationLongCode}) nearcode,create_time "
			        + " FROM event_cycle_record"
					+"  WHERE uid<>#{uid} AND mobile <>#{mobile}   LIMIT "+NEAR_LIMIT_NUMS
					+" ) t  ORDER BY create_time DESC,nearcode ASC";

	
	public static final String GET_EVENT_BASE_LIST="SELECT * FROM "+EVENT_BASE_TABLE ;
	
	/**
	 * 获得对应事件的评论列表
	 */
	public static final String GET_EVENT_COMMENT_LIST_BY_EVENTID="SELECT * FROM event_comment_record WHERE  eid= #{eid} ORDER BY id DESC limit ${offset},${limit}" ;
	
	
	/**
	 * 获得对应事件的评论数量
	 */
	public static final String COUNT_EVENT_COMMENT_LIST_BY_EVENTID="SELECT count(0) FROM event_comment_record WHERE  eid= #{eid} " ;
			
	/**
	 * 获得事件流转生命周期
	 */
	public static final String GET_EVENT_CYCLE_BY_ID=" SELECT * FROM  event_cycle_record  WHERE eid=#{eid} ORDER BY from_eid ";
	
	/**
	 * 获得图片事件信息
	 */
	public static final String GET_EVENT_PIC_BY_EID="select  * from event_pic where eid=#{eid} ";
	/**
	 * 获得文案事件信息
	 */
	public static final String GET_EVENT_TEXT_BY_EID="select  * from event_text where eid=#{eid} ";
	/**
	 * 获得投票事件信息
	 */
	public static final String GET_EVENT_VOTE_BY_EID="select  * from event_vote where eid=#{eid} ";
	/**
	 * 根据用户获得所有事件信息
	 */
	public static final String GET_EVENT_LIST_BY_UID="select  * from event_base_info where uid=#{uid} limit ${offset},${limit}";
	
	/**
	 * 统计用户获得所有事件信息数量
	 */
	public static final String COUNT_EVENT_LIST_BY_UID="select  count(0) from event_base_info where uid=#{uid} ";
	
}

package com.feeling.dto;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 事件基础dto
 * @author dutao
 *
 */
@Table(name=SqlConstants.EVENT_BASE_TABLE)
public class EventBaseDto  extends BaseDto{

	private Integer uid;//用户id
	//pic:图片|text:文字|vote:调查|video:视频|audio:语音
	private String eventType;//事件类型 
	private String eventCity;//事件发布所在城市
	private Integer isDisplay;//是否显示
	private Integer spreadTimes;//转发次数
	private Integer skipTimes;//忽略次数
	private Integer commentTimes;//评论次数
	private String mobile;//手机号
	private String nickName;//昵称
	private Integer status;//状态，是否可用,1:可用,-1:删除，-2:过期
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventCity() {
		return eventCity;
	}
	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}
	public Integer getSpreadTimes() {
		return spreadTimes;
	}
	public void setSpreadTimes(Integer spreadTimes) {
		this.spreadTimes = spreadTimes;
	}
	public Integer getSkipTimes() {
		return skipTimes;
	}
	public void setSkipTimes(Integer skipTimes) {
		this.skipTimes = skipTimes;
	}
	public Integer getCommentTimes() {
		return commentTimes;
	}
	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}

package com.feeling.vo;

import com.feeling.annotation.NotEmpty;

public class EventVo extends BaseVo{

	private Integer id;                     // 主键
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

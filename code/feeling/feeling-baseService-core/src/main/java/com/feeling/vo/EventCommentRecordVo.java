package com.feeling.vo;
 

/**
 * 事件评论VO
 * @author dutao
 *
 */
public class EventCommentRecordVo   extends BaseVo  {

	private Integer uid;//用户id
	private Integer eid; //主事件id
	private String comment;//评论
	private Integer isDisplay;//是否显示
	private String mobile;//手机号
	private String nickName;//昵称
	private Integer status;
	private String eventCity;//城市
	 
	public String getEventCity() {
		return eventCity;
	}
	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}

package com.feeling.vo;

import java.util.Date;

import com.feeling.annotation.NotNull;



 
public class EventCycleRecordVo  extends BaseVo {

	private Integer id; //主id
	@NotNull
	private Integer fromEid;//上级事件id
	private Integer uid;//用户id
	private String mobile;//手机号
	@NotNull
	private Integer eid; //主事件id
	private String nickName;//昵称 
	private String eventCity;//城市
	private Date createTime;//创建时间
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFromEid() {
		return fromEid;
	}
	public void setFromEid(Integer fromEid) {
		this.fromEid = fromEid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getEid() {
		return eid;
	}
	public void setEid(Integer eid) {
		this.eid = eid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEventCity() {
		return eventCity;
	}
	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}
	
}

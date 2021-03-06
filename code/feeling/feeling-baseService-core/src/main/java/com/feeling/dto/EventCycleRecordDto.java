package com.feeling.dto;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 
 * 事件流转实体【从A传到B，c等等】
 * @author dutao
 *
 */
@Table(name=SqlConstants.EVENT_CYCLE_TABLE)
public class EventCycleRecordDto  extends BaseDto {

	 
	private Integer fromEid;//上级事件id
	private Integer uid;//用户id
	private String mobile;//手机号
	private Integer eid; //主事件id
	private String nickName;//昵称 
	private String eventCity;//城市
	private Integer status;//状态-见EventStatusEnum
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
}

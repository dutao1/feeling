package com.feeling.vo;

import java.util.Date;
import java.util.List;

/**
 * 推荐事件vo
 * @author dutao
 *
 */
public class EventRecommendVo extends BaseVo{

	private Integer id; //主id
	private Integer uid;//用户id
	private String mobile;//手机号
	private Integer eid; //主事件id
	private String nickName;//昵称 
	private String eventCity;//城市
	private Date updateTime;//最近一次更新时间
	private Date createTime;//创建时间
	private String eventType;//事件类型 
	private Integer spreadTimes;//转发次数
	private Integer skipTimes;//忽略次数
	private Integer commentTimes;//评论次数
	private String   remark;//只有图片才有
	private List<EventPicVo> eventPicVos;//图片/视频 vo
	private EventVoteVo eventVoteVo;//投票vo
	private EventTextVo eventTextVo;//文案vo
	private Double distMeter;//相距多少米
	private Double distKm;// 相距多少公里
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getDistMeter() {
		return distMeter;
	}
	public void setDistMeter(Double distMeter) {
		this.distMeter = distMeter;
	}
	public Double getDistKm() {
		return distKm;
	}
	public void setDistKm(Double distKm) {
		this.distKm = distKm;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
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
	 
	public List<EventPicVo> getEventPicVos() {
		return eventPicVos;
	}
	public void setEventPicVos(List<EventPicVo> eventPicVos) {
		this.eventPicVos = eventPicVos;
	}
	public EventVoteVo getEventVoteVo() {
		return eventVoteVo;
	}
	public void setEventVoteVo(EventVoteVo eventVoteVo) {
		this.eventVoteVo = eventVoteVo;
	}
	public EventTextVo getEventTextVo() {
		return eventTextVo;
	}
	public void setEventTextVo(EventTextVo eventTextVo) {
		this.eventTextVo = eventTextVo;
	}
}

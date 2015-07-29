package com.feeling.vo;

import java.util.List;

public class UserEventVo extends EventVo{
	private EventPicVo eventPicVo;//图片/视频 vo
	private EventVoteVo eventVoteVo;//投票vo
	private EventTextVo eventTextVo;//文案vo
	
	private List<EventPicVo> eventPicVos;//图片/视频 vo[列表]
	
	public List<EventPicVo> getEventPicVos() {
		return eventPicVos;
	}
	public void setEventPicVos(List<EventPicVo> eventPicVos) {
		this.eventPicVos = eventPicVos;
	}
	public EventPicVo getEventPicVo() {
		return eventPicVo;
	}
	public void setEventPicVo(EventPicVo eventPicVo) {
		this.eventPicVo = eventPicVo;
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

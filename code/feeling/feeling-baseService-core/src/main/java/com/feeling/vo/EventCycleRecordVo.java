package com.feeling.vo;

import java.util.Date;
 

 
public class EventCycleRecordVo  extends BaseVo {

	private Integer id; //主id
	private Integer fromEid;//上级事件id
	private Integer uid;//用户id
	private String mobile;//手机号
	private Integer eid; //主事件id
	private String  nickName; //昵称
	
	
	private String eventType;//事件类型 
	private String eventCity;//事件发布所在城市
	private Integer spreadTimes;//转发次数
	private Integer commentTimes;//评论次数
	
	private String picPath; //图片地址
	private String picType; //图片类型
	private String remark; //图片或视频信息描述
	private String content;//内容
	private String title;//标题
	private Integer voteType;//投票类型（1：单选 2：多选）
	private String option1;//选项1
	private String option2;//选项2
	private String option3;//选项3
	private String option4;//选项4
	private String option5;//选项5
	private String option6;//选项6
	
	private Integer votes1;//投票数
	private Integer votes2;
	private Integer votes3;
	private Integer votes4;
	private Integer votes5;
	private Integer votes6;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVotes1() {
		return votes1;
	}
	public void setVotes1(Integer votes1) {
		this.votes1 = votes1;
	}
	public Integer getVotes2() {
		return votes2;
	}
	public void setVotes2(Integer votes2) {
		this.votes2 = votes2;
	}
	public Integer getVotes3() {
		return votes3;
	}
	public void setVotes3(Integer votes3) {
		this.votes3 = votes3;
	}
	public Integer getVotes4() {
		return votes4;
	}
	public void setVotes4(Integer votes4) {
		this.votes4 = votes4;
	}
	public Integer getVotes5() {
		return votes5;
	}
	public void setVotes5(Integer votes5) {
		this.votes5 = votes5;
	}
	public Integer getVotes6() {
		return votes6;
	}
	public void setVotes6(Integer votes6) {
		this.votes6 = votes6;
	}
	private Date createTime;//事件创建时间
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public Integer getSpreadTimes() {
		return spreadTimes;
	}
	public void setSpreadTimes(Integer spreadTimes) {
		this.spreadTimes = spreadTimes;
	}
	public Integer getCommentTimes() {
		return commentTimes;
	}
	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getVoteType() {
		return voteType;
	}
	public void setVoteType(Integer voteType) {
		this.voteType = voteType;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public String getOption4() {
		return option4;
	}
	public void setOption4(String option4) {
		this.option4 = option4;
	}
	public String getOption5() {
		return option5;
	}
	public void setOption5(String option5) {
		this.option5 = option5;
	}
	public String getOption6() {
		return option6;
	}
	public void setOption6(String option6) {
		this.option6 = option6;
	}
	
}

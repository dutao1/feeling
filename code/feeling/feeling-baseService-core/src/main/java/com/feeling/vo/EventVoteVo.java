package com.feeling.vo;

import java.util.Date;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 投票事件对应dto
 * @author dutao
 *
 */
public class EventVoteVo {

	private Integer id;//主键id
	private Integer uid;//用户id
	private Integer eid; //主事件id
	private String title;//标题
	private Date createTime;//创建时间
	private Date updateTime ;//更新时间
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
}

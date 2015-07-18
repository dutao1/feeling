package com.feeling.dto;

import java.util.Date;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 文案事件对应内容dto
 * @author dutao
 *
 */
@Table(name=SqlConstants.EVENT_TEXT_TABLE)
public class EventTextDto {

	private Integer id;//主键id
	private Integer uid;//用户id
	private Integer eid; //主事件id
	private String content;//内容
	private Date createTime;//创建时间
	private Date updateTime ;//更新时间
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	
}




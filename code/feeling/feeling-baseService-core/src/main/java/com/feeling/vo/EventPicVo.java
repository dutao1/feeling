package com.feeling.vo;

import java.util.Date;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 图片事件dto
 * @author dutao
 *
 */
public class EventPicVo {
	
	private Integer id;//主键id
	private Integer uid;//用户id
	private Integer eid; //主事件id
	private String picPath; //图片地址
	private String picType; //图片类型
	private String remark; //图片或视频信息描述
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

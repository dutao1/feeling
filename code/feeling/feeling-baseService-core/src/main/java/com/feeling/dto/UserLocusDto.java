package com.feeling.dto;

/**
 * 
 * 用户行为
 * @author dutao
 *
 */
public class UserLocusDto extends BaseDto{
	
	//类型（0:注册 1：登录 ）
	private Integer locusType;//行为类型
	private Integer uid;//用户id
	public Integer getLocusType() {
		return locusType;
	}
	public void setLocusType(Integer locusType) {
		this.locusType = locusType;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
}

package com.feeling.dto;

import com.feeling.annotation.Table;
import com.feeling.constants.SqlConstants;

/**
 * 
 * 用户行为
 * @author dutao
 *
 */
@Table(name=SqlConstants.USER_LOCUS_TABLE)
public class UserLocusDto extends BaseDto{
	
	//类型（0:注册 1：登录 ）
	private Integer locusType;//行为类型
	private Integer uid;//用户id
	private String cityCode;//城市
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
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

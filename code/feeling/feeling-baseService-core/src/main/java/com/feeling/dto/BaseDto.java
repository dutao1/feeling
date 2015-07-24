package com.feeling.dto;

import java.util.Date;

/**
 * 基础dto
 * @author dutao
 */
public class BaseDto {

	private Integer id;//主键id
	private Double lat;//纬度
	private Double lon;//经度
	private Long locationLongCode;//经纬度转换的长整形数字
	private String deviceType;//设备类型 android ios
	private String deviceImei;//imei号
	private String deviceId;//设备id
	private Date createTime;//创建时间
	private Date updateTime ;//更新时间
	private String locationHash;//经纬度hash值
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Long getLocationLongCode() {
		return locationLongCode;
	}
	public void setLocationLongCode(Long locationLongCode) {
		this.locationLongCode = locationLongCode;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceImei() {
		return deviceImei;
	}
	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	public String getLocationHash() {
		return locationHash;
	}
	public void setLocationHash(String locationHash) {
		this.locationHash = locationHash;
	}
}

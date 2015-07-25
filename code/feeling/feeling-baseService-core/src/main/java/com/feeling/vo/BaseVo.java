package com.feeling.vo;

public class BaseVo {

	
	private Double lat;//纬度
	private Double lon;//经度
	private String deviceType;//设备类型 android ios
	private String deviceImei;//imei号
	private String deviceId;//设备id
	private Long locationLongCode;//经纬度转换的长整形数字
	private String locationHash;//经纬度hash值
	
	public Long getLocationLongCode() {
		return locationLongCode;
	}
	public void setLocationLongCode(Long locationLongCode) {
		this.locationLongCode = locationLongCode;
	}
	public String getLocationHash() {
		return locationHash;
	}
	public void setLocationHash(String locationHash) {
		this.locationHash = locationHash;
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
	
	
}

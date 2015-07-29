package com.feeling.vo;

import java.util.Date;

import com.feeling.annotation.NotEmpty;
import com.feeling.annotation.NotNull;


/**
 * 用户vo
 * @author dutao
 *
 */
public class UserVo  extends BaseVo{

	private Integer id;                     // 主键
	private String mobile;//手机号
	@NotEmpty(minLength=3,maxLength=9)
	private String  nickName; //昵称or用户名
	@NotNull
	private Integer gender;//性别 0:女  1：男
	@NotEmpty(minLength=6,maxLength=9)
	private String pwd;//密码 加密后的
	private String cityCode;//城市代码
	private Date birthday;//生日
	private String avatar;//照片url
	private Integer status;//状态  -9: 注销 0:正常 1:锁定
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	 
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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

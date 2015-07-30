package com.feeling.vo;
import java.util.Date;
/**
 * 用户update vo
 * @author dutao
 *
 */
public class UserUptVo  extends BaseVo{

	private Integer id;                     // 主键
	private String mobile;//手机号
	private String  nickName; //昵称or用户名
	private String cityCode;//城市代码
	private Date birthday;//生日
	private String avatar;//照片url
	
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
}

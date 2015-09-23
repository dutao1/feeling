package com.feeling.vo;

public class UserLoginVo extends BaseVo{
	
	private String  mobile; //手机号
	
	private String pwd;//密码 
	
	private String loginToken;//登录token
	
	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
}

package com.feeling.vo;

public class UserLoginVo extends BaseVo{
	
	private String  nickName; //昵称or用户名
	
	private String pwd;//密码 
	
	private String loginToken;//登录token
	
	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
}

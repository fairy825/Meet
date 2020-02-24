package com.meet.pojo.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class LoginVO {
	
//	@NotNull
//	@IsMobile
	private String name;
//	@NotNull
//	@Length(min=32)
	private String password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginVo [mobile=" + name + ", password=" + password + "]";
	}
}

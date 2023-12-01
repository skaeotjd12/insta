package com.cos.photogramstart.web.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data  //롬복에서 getter, setter
public class SignupDto {
	
	@Size(min = 2, max = 20)
	@NotBlank 
	private String username;
	@NotBlank //null,빈 문자열, 스페이스만 있는 문자열 불가
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	
	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
	}
}

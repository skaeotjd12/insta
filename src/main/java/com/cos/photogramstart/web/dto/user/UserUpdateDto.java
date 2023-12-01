package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

//fk는 반드시 many n(many):1가 가져간다. n:n은 중간테이블이 생긴다. n:1:n
@Data
public class UserUpdateDto {
	@NotBlank
	private String name;
	@NotBlank
	private String password;
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	
	//조금 위험함, 코드 수정이 필요할 예정
	public User toEntity() {
		return User.builder()
				.name(name)  //이름을 기재안하면 문제! 벨리데이션 체크
				.password(password) // 마찬가지 벨리데이션 체크
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
	}
}

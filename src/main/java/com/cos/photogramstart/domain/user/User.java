package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor //전체생성자
@NoArgsConstructor //빈생성자
@Data
@Entity // 디비에 테이블을 생서
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //아이덴티티는 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100, unique = true) //Oauth2 로그인을 위해 칼럼 늘리기
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String website;
	private String bio;
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	private String profileImageUrl;
	private String role; 
	
	
	//1. mapped by란?  나는연관관계의 주인이아니다. 그러므로 테이이블에 칼럼을 만들지마.
	//Lazy = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages() 함수가 호출될때만 가져와
	//Eager = User를 Select할 때 해당 User id로 등록된 image들을 전부 join해서 가져와
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) 
	@JsonIgnoreProperties({"user"})  //jason 응답 때 무한참조 발생안되게하는것
	private List<Image> images; //이중맵핑이며 양방향매핑이다 46강 우선 데이터베이스에 해당 테이블을 만들지 말라고 mappeby해줘야함
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
	//Aop 공통 기능 proceedjoinpont 로 sysout할떄 무한참조 에러때무에 해당 투스틸ㅇ 만들어서 무한 참조되고있는 이미지를 직접 삭제해줌
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role +", createDate="
				+ createDate + "]";
	}
	
}

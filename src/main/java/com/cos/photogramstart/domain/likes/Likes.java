package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor //전체생성자
@NoArgsConstructor //빈생성자
@Data
@Entity // 디비에 테이블을 생성
@Table(
			uniqueConstraints =  {
					@UniqueConstraint(
							name ="likes_uk",
							columnNames = {"imageId", "userId"} //1번 유저가 1번 이미지를 좋아하고 또 1번 이미즐ㄹ 좋아할 수 ㅇ없기 때문에 이렇게하면 중복될수없다고 유니크하다고 걸어둠 
					)
				}
		)
public class Likes { // N
	
	//어떤 이미지를 누가 좋아했는지에 대한 테이블 구성
	//무한참조 68강 강의보기
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //아이덴티티는 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;  
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; // 1
	
	//무한참조 오류가 터지고 나서 잡아봅시다.
	//user 안에 images를 배제한다.
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;  // 1
	
	private LocalDateTime createDate;
	
	@PrePersist 
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}

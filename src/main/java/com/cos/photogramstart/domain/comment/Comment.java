package com.cos.photogramstart.domain.comment;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor 
@Data
@Entity 
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //아이덴티티는 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(length = 100, nullable  = false) // 댓글은 반드시 null일 수 없어야하고 앵간하면 글자수 제한을 두는게 좋다
	private String content; 
	
	
	@JsonIgnoreProperties({"images"}) //댓글 쓸 때 유저넴이떄문에 유저 객체를 가져오는데 이때 이미지스는 피료없다
	@JoinColumn(name = "userId")  //fk로 해당 네임으로 컬럼 생성된다. 
	@ManyToOne(fetch = FetchType.EAGER) // 매니투원은 이거가 디폴트값임   eager는 select 할 때 다 들고오고 lazy는 하나 셀렉할떄 딸려오는게 여러개면 기본 전략이 레이키 그래서 원투매니는 레이지 매니투원은 이거 72강 댓글에서 자세히 설ㅇ명해줌
	private User user;  // user 가 1 comment는 n
	
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; // image 가 1 comment 는 n
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}

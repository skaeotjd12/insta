package com.cos.photogramstart.domain.subscribe;

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

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//밑에 @table 이거는 유니크가 2개라서 만드는것 1개면 user 테이블처러만들면됨
@Builder
@AllArgsConstructor //전체생성자
@NoArgsConstructor //빈생성자
@Data
@Entity // 디비에 테이블을 생성
@Table(
			uniqueConstraints =  {
					@UniqueConstraint(
							name ="subscribe_uk",
							columnNames = {"fromUserId", "toUserId"}
					)
			}
		)
public class Subscribe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //아이덴티티는 번호 증가 전략이 데이터베이스를 따라간다.
	private int id; //fk
	
	
	//투와 ㅈ프롬은 유니크해야ㅐ한다. 구독을 또 인서트를 하면안되기때문에
	@JoinColumn(name= "fromUserId") //이렇게 컬럼명 정확히만들라고하는것
	@ManyToOne
	private User fromUser; //구독하는 사람
	
	@JoinColumn(name= "toUserId")
	@ManyToOne
	private User toUser; // 구독 받는 사람

	private LocalDateTime createDate;
	
	
	@PrePersist //디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}

package com.cos.photogramstart.domain.image;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
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
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //아이덴티티는 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	private String caption;
	private String postImageUrl;
	
	@JsonIgnoreProperties({"images"}) //images는 무시해
	@JoinColumn(name = "userId")
	@ManyToOne  
	private User user; // 누가 업로드해야되는지 알아야하니깐 user가 1 image가 n
	
	//이미지 좋아요
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image") //나는 연관계에 주인이아니다 포리근키를 만들지마 image : 1  , likes : n
	private List<Likes> likes;
	
	@Transient  //이 어노테이션 쓰면 DB에 컬럼이 만들어 지지 않는다.
	private boolean likeState;
	
	@Transient 
	private int likeCount;
	
	//댓글, story 불러올때 이미지를 불러오는데 그떄 댓글을 불러오기 위해 양방향 매핑해줌
	@OrderBy("id DESC") 
	@JsonIgnoreProperties({"image"})
	@OneToMany(mappedBy = "image")  //연관 관계의주인이아니다. 그래서 fk변수명은 image의 comment 위에 있음 imageId
	private  List<Comment> comments;
	
	//시간
	private LocalDateTime createDate;
	
	
	@PrePersist //디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

//	//toString은 @data를 사용하면 자동으로 만들어진다.  imageservice 단에서 이건 syso ImageEntity를 출력할떄 user가 같이 출력되서 내부적으로 무한반복ㄹ되는 에러를 잡기 위해 사용
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl
//				+ ", createDate=" + createDate + "]";
//	}
	
	
}

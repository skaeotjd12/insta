package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import lombok.Data;

//NotNull = Null값 체크
//NotEmpty = 빈값이거나 null 체크
//NotBlank = 빈값이거나 null 체크 그리고 빈 공백(스페이스 까지)


@Data
public class CommentDto {
	//dto를 만드는 이유 댓글쓰기는 ajax로 통신해서 id와 컨텐트를 받는데 이걸 comment 오브젝트로 받을려고하면 안에 image나 user 등 오브젝트들이 인자로 들어있어서 이런 경우 지금 처럼 dto를 만들어줘야한다.
	
	@NotBlank //notblank를 걸면 api에서 데이터 받아올떄 valid 어노테이션을 붙혀ㅑ줘야한다
	private String content;
	@NotNull 
	private Integer imageId;
	
	//private int imageId  <-- 최초에는 이렇게해서 문제 ㅇ벗엇찌만 78강 유효성검사에서 @notblank 어노테이션 시 int는 적용되지않고 integer로 적용ㅎ루 notnull 시 정상 작동
	//toEntity가 필요없다.
}

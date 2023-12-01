package com.cos.photogramstart.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	/*
	 이건 내가 값을 넣을때는 comment 객체로는 리턴을 받짐 ㅗㅅ한다네 75강 확인
	@Modifying
	@Query(value = "INSERT INTO comment(content, imageId, userId, createDate) VALUES(:content, :imaged, :userId, now())", nativeQuery = true)
	Comment mSave(String content, int imageId, int userId);
	*/
}

package com.cos.photogramstart.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
	
	//네이티브 쿼리  쓸 떄는 createDate 자동으로 안들어가니 직접 넣어줘야함 now() 함수로
	
	@Modifying
	@Query(value = "INSERT INTO likes(imageId, userId, createDate) VALUES(:imageId, :principalId, now())", nativeQuery = true)
	int mLikes(int imageId, int principalId);
	
	@Modifying
	@Query(value = "DELETE FROM likes WHERE imageId= :imageId AND userId = :principalId", nativeQuery = true)
	int mUnLikes(int imageId, int principalId);
}

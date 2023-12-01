package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		/* 이런식으로 찾아서 해당 인자에 넣어서 쓸 순 있으나 추천 하는 방법은 아니고 쿼리를 작성해보자
		UserRepository.findById(userId);
		imageRepository.findById(imageId);
		*/
		
		//Tip 가짜 객체 만들어서 id값만 담아서 insert 할 수  있다.
		//대신 return 시에 image 객체와 user 객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		//근데 여기선 username도 가져가야해서 user 객체찾아서 넣어줬음
		//msave는 현재 우리가 네이티브쿼리 작성해서 할떈 안 먹혀서 save로하고 객체로 넣어줬음
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		User user = new User();
		user.setId(userId);
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	//딜리트는 웬만하연 에러 안나서 익셉션 안 잡지만 하고싶다면 밑처럼
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}

package com.cos.photogramstart.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		UserProfileDto dto =  userService.회원프로필(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	//authenticationprincipal은 세션정보 찾는 방법 30강 
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		//System.out.println("세션 정보 : " + principalDetails.getUser());
		
		/** 이 2가지 방법은 안씀 어노테이션으로 대체하자
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails mPrincipalDetails = (PrincipalDetails) auth.getPrincipal();
		System.out.println("직접 찾은 세션 정보 1번 방법"+ auth.getPrincipal());
		System.out.println("직접 찾은 세션 정보  2번 방법"+ mPrincipalDetails.getUser());
		**/
		//기존엔 모델로 넘겨줬지만 헤더에 principal 객체를 아예 생성 시큐리티 태그 라이브러리 이용해서 
		//model.addAttribute("principal", principalDetails.getUser());
		return "user/update";
	}
}

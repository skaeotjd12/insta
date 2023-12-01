package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //파이널이 걸려있는 애들 전부에 생성자를 만드어줄 final을 di할때 만들어줌
@Controller  //파일을 리턴하는 컨트롤러
public class AuthController {
		
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;
	
	//의존성 주입 잘 안  씀 private final authservice와 똑같음
	//public AuthController(AuthService authService) {
	//	this.authService = authService;
	//}
	
	
	@GetMapping("/auth/signin")
	public String siginForm() {
		return "auth/signin";
	}
	//회원가입버튼 -> /auth/signup -> /auth/signin
	@GetMapping("/auth/signup")
	public String signupForm() { 
		return "auth/signup";
	}

	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {// key=value(form 방식이며 x-www-form-urlencoded)
		
//		if(bindingResult.hasErrors()) {
//			Map<String, String> erroMap= new HashMap<>();
//			
//			for(FieldError error : bindingResult.getFieldErrors()) {
//				erroMap.put(error.getField(), error.getDefaultMessage());		
//			}
//			throw new CustomValidationException("유효성 검사 실패함",erroMap);
//		} else {
				log.info(signupDto.toString());
				//User에 SignupDto를 옮겨준다 이때 투엔티티 이용해서 user 객체를 만들어서 옮겨줌 이게 편함
				User user = signupDto.toEntity();
				log.info(user.toString());
				authService.회원가입(user);
				return "auth/signin";
	//	}
	
	}
}

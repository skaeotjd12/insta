package com.cos.photogramstart.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
//import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
	
//	클라이언트에게 쓰긴 다소 무리
//	@ExceptionHandler(CustomValidationException.class) //런타임 익셉션을 발생하는 모든 익셉션을 이 함수가 가로챔
//	public CMRespDto<?> validationException(CustomValidationException e) {
//		return new CMRespDto<Map<String,String>>(-1, e.getMessage(), e.getErrorMap());
//	}
	
	//CMRespDto, Script 비교
	//1. 클라이언트에게 응답할 때는 Script 좋음.
	//2. Ajax통신 - CMRespDto
	//3. Android - CMRespDto 
	@ExceptionHandler(CustomValidationException.class) //런타임 익셉션을 발생하는 모든 익셉션을 이 함수가 가로챔
	public String validationException(CustomValidationException e) {
		
		if(e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		}else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	
	@ExceptionHandler(CustomException.class) //런타임 익셉션을 발생하는 모든 익셉션을 이 함수가 가로챔
	public String exception(CustomException e) {
		return Script.back(e.getMessage());
	}
	
	//ajax 통신용 restcontroller
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.getErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
	}
	
	
	
}

package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException {

	/**
	 * 객체 구분할 때 쓰는건데 중요한거아님
	 */
	private static final long serialVersionUID = 1L;
	

	private Map<String, String> errorMap;
	
	//스트링만 ㅂ다는 오류 회원 수정때 오류 받는 거는 스트링만주기때문에 하나만 받는것도 만들어줌
	public CustomValidationApiException(String message) {
		super(message);
	}
	
	
	public CustomValidationApiException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
	
	public Map<String, String> getErrorMap(){
		return errorMap;
	}
}

package com.cos.photogramstart.handler.aop;


import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // RestController, Service 등 모든 것들이 Component를 상속해서 만들어져잇음, 공통기능은 딱히 주요 기능이 없기 떄문에 메모리에 띄우기 위해 componentㄹ로 만들어준다.
@Aspect
public class ValidationAdvice {
	//AOP 공통 기능 79강
	
	//특정 함수에 전에 쓰이는건 before, 후는 after, 모든 구간 around (..) <-- 모든 함수를 실행한다.
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") 
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable  {
		
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {//arg가 바인딩러절트란게있으면
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> erroMap= new HashMap<>();
					for(FieldError error : bindingResult.getFieldErrors()) {
						erroMap.put(error.getField(), error.getDefaultMessage());		
					}
					throw new CustomValidationApiException("유효성 검사 실패함",erroMap);
				}
			}
		}
		//proceedingJoinPoint =>예를들어 profile 함수의 모든 곳에 접근 할 수 있는 변수
		//profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed();
	}
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String, String> erroMap= new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						erroMap.put(error.getField(), error.getDefaultMessage());		
					}
					throw new CustomValidationException("유효성 검사 실패함",erroMap);
				}
			}
		}
		return proceedingJoinPoint.proceed();
	}
}

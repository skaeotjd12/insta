package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final OAuth2DetailsService oAuth2DetailsService;
	
	@Bean
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	//여기서 컨트롤 스페이스로 오버라이드 해주면 자동 완성 슈퍼는 삭제
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/comment/**", "/subscribe/**", "/image/**", "/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin") //get요청시 낚아챔 로그인페이지 
			.loginProcessingUrl("/auth/signin") //POST일 시 낚아챔 요청시 로그인 후)
			.defaultSuccessUrl("/")
			.and() //oauth넣기 위해 시작
			.oauth2Login() //form로그인도 하는데, oauth2로그인도 할꺼야!
			.userInfoEndpoint() //oauth2로그인을하면 최종 응답을 회원정보를 바로 받을 수 있다.
			.userService(oAuth2DetailsService);

	}
	}


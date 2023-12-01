package com.cos.photogramstart.service;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); 
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename();
		System.out.println("이미지 파일이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//통신 or I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@프로필이미지변경 아이디 확인 : "+ principalId);
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		return userEntity;
	} //더티체킹으로 업데이트됨
	
	
	@Transactional(readOnly = true) //변경감지는 안 함 일을 적게 안함
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		
		UserProfileDto dto = new UserProfileDto();
		
		//SELECT * FROM image WHERE userId= :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()-> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		dto.setUser(userEntity);
		dto.setImageCount(userEntity.getImages().size());
		dto.setPageOwnerState(pageUserId == principalId); //같으면 true 틀리면 false
		
		//구독수, 구독했는지 분기
		int subscribeState =  subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1); //1이면 트루 구독한 거임 화면상에선 구독취소로 보여야함 
		dto.setSubscribeCount(subscribeCount);;
		return dto;
	}
	
	
	@Transactional
	public User 회원수정(int id, User user) {
		//1. 영속화
		//User userEntity = userRepository.findById(id).get(); //1.무조건 찾았다.걱정마 get() 2. 못찾았어 익셉션 발동시킬게 orElthThrow 함수
		
		User userEntity = userRepository.findById(id).orElseThrow(() -> {return new  CustomValidationApiException("찾을 수 없는 id입니다.");});
		//유저 수정했으나 db에 해당 id가 없는 경우엔 get으로 처리불가 db에서의 익셉션 처리
		//여긴 에러맵이 없어서 ajax에 리스폰스엔티티 호출 시 에러맵은 null로 에러남
				
		//2. 영속화된 오브젝트 수정 (더티체킹)
		userEntity.setName(user.getName());
		String rawPasswrod = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPasswrod);
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;		
	}
}

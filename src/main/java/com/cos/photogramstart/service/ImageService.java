package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}
	
	
	@Transactional(readOnly = true) //select 문 일떈 리드 온리 트루(읽기전용) 로하면 되는데 영속선 컨텍스트 변경감지를 해서 , 더티체킹, flush(반영) 이런 것들을 안 함!
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) ->{
				if(like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람들을 찾아서 현재 로긴한 사람이 좋아요 한것인지 비교 후 내가 했다면 true; 아니면 false; 
					image.setLikeState(true);
				}
			});
			
		});
		
		return images;
	}
	
	
	
	//upload 폴더를 리소스안에 안놔둔 이유는 target이 컴파일할떄 deploy가되면 컴파일된 후에 사진업로드안된 걸 읽어서... 시간이 길어지기때문에 외부에 놔둔다.. 43강 참조
	@Value("${file.path}")
	private String uploadFolder;  //실제 경로르 여기에 적어도되지만 yml에 적어두고 value 어노테이션으로 가져오는게 좋다 
	
	@Transactional  //db에 변형이 들어가면 반드시 트렌제셔널해야ㅐ한다. 거는 이뉴느 48강
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); // uuid
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename();
		System.out.println("이미지 파일이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//통신 or I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());  //toEntity를 만들어줘야함
		imageRepository.save(image);
		
		//System.out.println(imageEntity.toString());  //해당 syso때문에 image model에서 user가 무한참조되는걸 tostring을 다시 만들고 user부분을 없애서 해결 48강 참조
		}
}

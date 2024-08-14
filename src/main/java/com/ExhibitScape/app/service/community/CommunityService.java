package com.ExhibitScape.app.service.community;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.common.DataNotException;
import com.ExhibitScape.app.domain.community.ComCommentRepository;
import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.domain.community.CommunityRepository;
import com.ExhibitScape.app.dto.community.CommunityDTO;

@Service("CommunityService")
public class CommunityService {

	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private ComCommentRepository commentRepository;
	
	//카테고라이징
	public Page<Community> findAllCommunities(Pageable pageable) {
        return communityRepository.findAllByOrderByPostDateDesc(pageable);
    }
	
    public Page<Community> findCommunitiesByCategory(String category, Pageable pageable) {
    	return communityRepository.findByComCategoryContainingOrderByPostDateDesc(category, pageable);
    }
	
	//페이징처리 목록조회
	//매개변수 : 주의! 스프링부트의 Jpa의 페이지시작번호는 0부터 시작한다. 1아니다.
	public Page<Community> getList(int page){
		//정렬기준 : 여기에서는 Community의 createDate값을 내림차순
		//==> 글등록일 내림차순 ===> 최신글부터 조회
		//참고 Sort.Order.asc("정렬기준") : 오름차순
		//참고 Sort.Order.desc("정렬기준"): 내림차순
		List<Sort.Order> sorts =new ArrayList<>();
		sorts.add(Sort.Order.desc("postDate"));
		
		//PageRequest.of(페이지번호,1페이징당 게시글수,정렬기준) //Jpa의 페이지시작번호는 0부터 시작
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return communityRepository.findAll(pageable);
	}

	// 글쓰기
	public void communityWrite(CommunityDTO communityDTO) {

		Community community = new Community();
		community.setComId(communityDTO.getComId());
		community.setMemberId(communityDTO.getMemberId());
		community.setComTitle(communityDTO.getComTitle());
		community.setComContent(communityDTO.getComContent());
		community.setComImgSname(communityDTO.getComImgSname());
		community.setComImgPath(communityDTO.getComImgPath());
		community.setComCategory(communityDTO.getComCategory());
		community.setComHits(communityDTO.getComHits());
		community.setComTag(communityDTO.getComTag());

		communityRepository.save(community);
	}

	// 글 수정하기
	public void communityReWrite(CommunityDTO communityDTO) {

		Community community = new Community();
		community.setComId(communityDTO.getComId());
		community.setMemberId(communityDTO.getMemberId());
		community.setComTitle(communityDTO.getComTitle());
		community.setComContent(communityDTO.getComContent());
		community.setComImgSname(communityDTO.getComImgSname());
		community.setComImgPath(communityDTO.getComImgPath());
		community.setComCategory(communityDTO.getComCategory());
		community.setComHits(communityDTO.getComHits());
		community.setComTag(communityDTO.getComTag());

		communityRepository.save(community);
	}

	// 목록조회
	public List<CommunityDTO> communityList() {
		List<CommunityDTO> comDTOList = new ArrayList<CommunityDTO>();

		// Repository로 부터 받은 엔터티를 DTO로 가공
		List<Community> comList = communityRepository.findAllByOrderByPostDateDesc();

		for (Community community : comList) {
			CommunityDTO dTo = new CommunityDTO();
			dTo.setComId(community.getComId());
			dTo.setMemberId(community.getMemberId());
			dTo.setComTitle(community.getComTitle());
			dTo.setComContent(community.getComContent());
			dTo.setComImgSname(community.getComImgSname());
			dTo.setComImgPath(community.getComImgPath());
			dTo.setComCategory(community.getComCategory());
			dTo.setPostDate(community.getPostDate());
			dTo.setComHits(community.getComHits());
			dTo.setComTag(community.getComTag());
			dTo.setCommentList(community.getCommentList());

			comDTOList.add(dTo);
		}

		return comDTOList;
	}

	
	// 글 상세조회
	public CommunityDTO communityDetail(Integer comId){
		Optional<Community> oCommunity = communityRepository.findById(comId);
		if(oCommunity.isPresent()) {
			
			Community com = oCommunity.get();
			CommunityDTO communityDTO = convertDTO(com);
			
			return communityDTO;
			
		}else {	
			throw new DataNotException("question not found");
		}
		
	}

// 글수정

// 글삭제
	
	private CommunityDTO convertDTO(Community community) {
		CommunityDTO dTo = new CommunityDTO();
		
		dTo.setComId(community.getComId());
		dTo.setMemberId(community.getMemberId());
		dTo.setComTitle(community.getComTitle());
		dTo.setComContent(community.getComContent());
		dTo.setComImgSname(community.getComImgSname());
		dTo.setComImgPath(community.getComImgPath());
		dTo.setComCategory(community.getComCategory());
		dTo.setPostDate(community.getPostDate());
		dTo.setComHits(community.getComHits());
		dTo.setComTag(community.getComTag());
		dTo.setCommentList(community.getCommentList());
		
		return dTo;
	}
	
	@Value("${spring.servlet.multipart.location}")//application.properties의 변수
	private String uploadPath;
	//폴더 생성
	public String makeFolder() {
		
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String folderPath = str;
		
		//make folder 
		File uploadPathFolder = new File(uploadPath,folderPath);
		
		if(uploadPathFolder.exists()==false) {
			uploadPathFolder.mkdirs();
		}
		
		return folderPath;
	}
	
	
	
	
}

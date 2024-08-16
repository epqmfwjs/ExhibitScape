package com.ExhibitScape.app.service.community;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.common.DataNotException;
import com.ExhibitScape.app.domain.community.ComLike;
import com.ExhibitScape.app.domain.community.ComLikeRepository;
import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.domain.community.CommunityRepository;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.community.CommunityDTO;

@Service("CommunityService")
public class CommunityService {

	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private ComLikeRepository comLikeRepository;
	
	//카테고라이징
	public Page<Community> findAllCommunities(Pageable pageable) {
        return communityRepository.findAllByOrderByPostDateDesc(pageable);
    }
	
    public Page<Community> findCommunitiesByCategory(String category, Pageable pageable) {
    	return communityRepository.findByComCategoryContainingOrderByPostDateDesc(category, pageable);
    }
    
    
    //좋아요
    
    public CommunityDTO getCommunityDetails(Integer comId, MemberDomain memberDomain) {
        Community community = communityRepository.findById(comId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found with id: " + comId));

        CommunityDTO communityDTO = new CommunityDTO();
        
        // CommunityDTO에 필요한 필드 설정
        communityDTO.setComId(community.getComId());
        communityDTO.setMemberId(community.getMemberId());
        communityDTO.setComTitle(community.getComTitle());
        communityDTO.setComContent(community.getComContent());
        communityDTO.setPostDate(community.getPostDate());
        communityDTO.setComImgSname(community.getComImgSname());
        communityDTO.setComImgPath(community.getComImgPath());
        communityDTO.setComCategory(community.getComCategory());
        communityDTO.setComHits(community.getComHits());
        communityDTO.setComTag(community.getComTag());
        communityDTO.setCommentList(community.getCommentList());
        
     // 좋아요 정보 설정
        boolean liked = comLikeRepository.existsByCommunityAndMemberDomain(community, memberDomain);
        int likeCount = comLikeRepository.countByCommunity(community);
        communityDTO.setLiked(liked);
        communityDTO.setLikeCount(likeCount);

        return communityDTO;
    }
    
    public boolean toggleComLike(Integer comId, MemberDomain memberDomain) {
        Community community = communityRepository.findById(comId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found with id: " + comId));

        Optional<ComLike> optionalComLike = comLikeRepository.findByCommunityAndMemberDomain(community, memberDomain);

        if (optionalComLike.isPresent()) {
            ComLike comLike = optionalComLike.get();
            comLikeRepository.delete(comLike);
            community.setLiked(false);
            communityRepository.save(community);
            return false;
        } else {
            ComLike comLike = new ComLike();
            comLike.setCommunity(community);
            comLike.setMemberDomain(memberDomain);
            comLike.setComLikeCheck(true);
            comLikeRepository.save(comLike);            
            community.setLiked(true);
            communityRepository.save(community);
            
            return true;
        }
    }
    
    public int getLikeCount(Integer comId) {
        Community community = communityRepository.findById(comId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found with id: " + comId));
        int likeCount=comLikeRepository.countByCommunity(community);
        community.setLikeCount(likeCount);
        communityRepository.save(community);
        return likeCount;
    }
    
   
  	
  	//삭제처리
  	public void delete(CommunityDTO communityDTO) {
  		communityRepository.deleteById(communityDTO.getComId());
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
		community.setPlaceLat(communityDTO.getPlaceLat());
		community.setPlaceLong(communityDTO.getPlaceLong());
		community.setPlaceName(communityDTO.getPlaceName());
		

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
		community.setPlaceLat(communityDTO.getPlaceLat());
		community.setPlaceLong(communityDTO.getPlaceLong());
		community.setPlaceName(communityDTO.getPlaceName());

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
	
	public List<Integer> findLikedCommunityIds(String memberId) {
	    // memberId를 사용하여 해당 사용자가 좋아요를 누른 게시물의 ID 목록을 조회하는 로직 구현
	     List<ComLike> comLikes = comLikeRepository.findByMemberDomain_memberId(memberId);
	     
	     List<Integer> likedCommunityIds = comLikes.stream()
	             .map(comLike -> comLike.getCommunity().getComId())
	             .collect(Collectors.toList());
	     
	     return likedCommunityIds;
	}

	
	// 글 상세조회
	public CommunityDTO communityDetailWithLike(Integer comId, MemberDomain memberDomain) {
	    Optional<Community> oCommunity = communityRepository.findById(comId);
	    if (oCommunity.isPresent()) {
	        Community com = oCommunity.get();
	        CommunityDTO communityDTO = convertDTO(com);

	        // 좋아요 정보 설정
	        boolean liked = false;
	        int likeCount = comLikeRepository.countByCommunity(com);
	        
	        if (memberDomain != null) {
	            // 로그인한 사용자인 경우에만 좋아요 정보 설정
	            liked = comLikeRepository.existsByCommunityAndMemberDomain(com, memberDomain);
	        }
	        
	        communityDTO.setLiked(liked);
	        communityDTO.setLikeCount(likeCount);
	        
	        return communityDTO;
	    } else {
	        throw new DataNotException("question not found");
	    }
	}
	
	public CommunityDTO communityDetailWithoutLike(Integer comId) {
	    Optional<Community> oCommunity = communityRepository.findById(comId);
	    if (oCommunity.isPresent()) {
	        Community com = oCommunity.get();
	        CommunityDTO communityDTO = convertDTO(com);

	        // 좋아요 정보 설정
	        int likeCount = comLikeRepository.countByCommunity(com);
	        
	        communityDTO.setLiked(false);
	        communityDTO.setLikeCount(likeCount);
	        return communityDTO;
	    } else {
	        throw new DataNotException("question not found");
	    }
	}
	
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
		dTo.setPlaceLat(community.getPlaceLat());
		dTo.setPlaceLong(community.getPlaceLong());
		dTo.setPlaceName(community.getPlaceName());
		
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

	//희경추가
    public List<Community> getUserCommunity(String memberId) {
        return communityRepository.findByMemberId(memberId);
    }
    
    public int getTotalCount() {
        return (int) communityRepository.count();
    }
    
    public Map<String, Integer> getBoardCountsByDate() {
        List<Object[]> result = communityRepository.getBoardCountsByDate();
        Map<String, Integer> boardCountsByDate = new HashMap<>();
        for (Object[] row : result) {
            String date = (String) row[0];
            int count = ((Long) row[1]).intValue();
            boardCountsByDate.put(date, count);
        }
        return boardCountsByDate;
    }
	
	
}

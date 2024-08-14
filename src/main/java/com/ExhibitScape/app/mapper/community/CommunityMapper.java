package com.ExhibitScape.app.mapper.community;

import org.apache.ibatis.annotations.Mapper;

import com.ExhibitScape.app.dto.community.ComLikeDTO;

@Mapper
public interface CommunityMapper {
	//좋아요 관련
	
	public void createLike(ComLikeDTO comLikeDTO);
	public int countLikeByComId(int comId);
	public int countLikeByArticleIdAndMemberId(ComLikeDTO comLikeDTO);
	public int deleteLikeByArticleIdAndUserID(ComLikeDTO comLikeDTO);
}

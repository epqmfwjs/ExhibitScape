package com.ExhibitScape.app.controller.gallery;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ExhibitScape.app.service.gallery.GalleryLikeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gallery/{id}/like")
public class GalleryLikeController {

	private final GalleryLikeService galleryLikeService;

	@PostMapping("/like")
	public ResponseEntity<String> likeGallery(@RequestParam("galId") Long galId, @RequestParam("memberId") String memberId) {
	    galleryLikeService.likeGallery(galId, memberId);
	    return ResponseEntity.ok("Gallery liked successfully.");
	}

	// 좋아요 수를 포함하는 사용자 정의 응답 클래스 정의
	static class LikeResponse {
		private final String message;
		private final int likeCount;

		public LikeResponse(String message, int likeCount) {
			this.message = message;
			this.likeCount = likeCount;
		}

		public String getMessage() {
			return message;
		}

		public int getLikeCount() {
			return likeCount;
		}
	}
}

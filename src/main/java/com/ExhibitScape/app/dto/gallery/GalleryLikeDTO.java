package com.ExhibitScape.app.dto.gallery;

import com.ExhibitScape.app.domain.gallery.GalleryLikeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GalleryLikeDTO {
    private Long id;
    private Long galId;
    private String memberId;
    private Boolean love;

    public static GalleryLikeDTO toGalleryLikeDTO(GalleryLikeEntity galleryLikeEntity) {
        GalleryLikeDTO galleryLikeDTO = new GalleryLikeDTO();
        galleryLikeDTO.setId(galleryLikeEntity.getId());
        galleryLikeDTO.setGalId(galleryLikeEntity.getGallery().getGalId());
        galleryLikeDTO.setMemberId(galleryLikeEntity.getMemberId());
        galleryLikeDTO.setLove(galleryLikeEntity.getLove());
        return galleryLikeDTO;
    }
}
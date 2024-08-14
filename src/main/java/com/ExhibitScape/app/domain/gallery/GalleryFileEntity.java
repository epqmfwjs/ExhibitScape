package com.ExhibitScape.app.domain.gallery;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gallery_file_table")
public class GalleryFileEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String galImgOfile;
	
	@Column
	private String galImgSfile;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="gallery_galId")
	private GalleryEntity galleryEntity;
	
    public static GalleryFileEntity toGalleryFileEntity(GalleryEntity galleryEntity, String galImgOfile, String galImgSfile) {
    	GalleryFileEntity galleryFileEntity = new GalleryFileEntity();
    	galleryFileEntity.setGalImgOfile(galImgOfile);
    	galleryFileEntity.setGalImgSfile(galImgSfile);
    	galleryFileEntity.setGalleryEntity(galleryEntity);
        return galleryFileEntity;
    }
}

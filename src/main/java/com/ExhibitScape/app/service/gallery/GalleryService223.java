//package com.ExhibitScape.app.service.gallery;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.ExhibitScape.app.domain.gallery.GalleryFileRepository;
//import com.ExhibitScape.app.domain.gallery.GalleryRepository;
//import com.ExhibitScape.app.dto.gallery.GalleryDTO;
//import com.ExhibitScape.app.entity.GalleryEntity;
//import com.ExhibitScape.app.entity.GalleryFileEntity;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class GalleryService223 {
//
//	private final GalleryRepository galleryRepository;
//    private final GalleryFileRepository galleryFileRepository;
//
//    public void save(GalleryDTO galleryDTO) throws IOException {
//        GalleryEntity galleryEntity = GalleryEntity.toSaveEntity(galleryDTO);
//        galleryRepository.save(galleryEntity);
//        
//        saveGalleryFiles(galleryDTO.getGalleryFile(), galleryEntity);
//    }
//
//    @Transactional
//    public GalleryDTO update(GalleryDTO galleryDTO) throws IOException {
//        Optional<GalleryEntity> optionalGalleryEntity = galleryRepository.findById(galleryDTO.getGalId());
//        if (optionalGalleryEntity.isPresent()) {
//            GalleryEntity galleryEntity = optionalGalleryEntity.get();
//            
//            galleryEntity.setGalTitle(galleryDTO.getGalTitle());
//            galleryEntity.setGalInfo(galleryDTO.getGalInfo());
//
//            saveGalleryFiles(galleryDTO.getGalleryFile(),galleryEntity);
//            galleryRepository.save(galleryEntity);
//
//            return GalleryDTO.toGalleryDTO(galleryEntity);
//        } else {
//            throw new IllegalArgumentException("Invalid gallery ID: " + galleryDTO.getGalId());
//        }
//    }
//
//    private void saveGalleryFiles(MultipartFile[] uploadFiles, GalleryEntity galleryEntity) throws IOException {
//        if (uploadFiles != null) {
//            for (MultipartFile multipartFile : uploadFiles) {
//                if (multipartFile.isEmpty()) {
//                    continue;
//                }
//
//                String rootPath = "C:/imgs/";
//
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
//                String todayPath = sdf.format(new Date()); 
//
//                File todayFolderForCreate = new File(rootPath + todayPath);
//                if (!todayFolderForCreate.exists()) {
//                    todayFolderForCreate.mkdirs();
//                }
//
//                String originalFilename = multipartFile.getOriginalFilename();
//                String uuid = UUID.randomUUID().toString();
//                long currentTime = System.currentTimeMillis();
//                String filename = uuid + "_" + currentTime;
//
//                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//                filename += ext; 
//
//                try {
//                    multipartFile.transferTo(new File(rootPath + todayPath + filename));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                GalleryFileEntity galleryFileEntity = GalleryFileEntity.toGalleryFileEntity(galleryEntity, originalFilename, todayPath + filename);
//                galleryFileRepository.save(galleryFileEntity);
//            }
//        }
//    }
//
//    @Transactional
//    public List<GalleryDTO> findAll() {
//        List<GalleryEntity> galleryEntityList = galleryRepository.findAll();
//        List<GalleryDTO> galleryDTOList = new ArrayList<>();
//        for (GalleryEntity galleryEntity : galleryEntityList) {
//            galleryDTOList.add(GalleryDTO.toGalleryDTO(galleryEntity));
//        }
//        return galleryDTOList;
//    }
//
//    @Transactional
//    public void updateHits(Long id) {
//        galleryRepository.updateHits(id);
//    }
//
//    @Transactional
//    public GalleryDTO findById(Long id) {
//        Optional<GalleryEntity> optionalGalleryEntity = galleryRepository.findById(id);
//        if (optionalGalleryEntity.isPresent()) {
//            GalleryEntity galleryEntity = optionalGalleryEntity.get();
//            return GalleryDTO.toGalleryDTO(galleryEntity);
//        } else {
//            return null;
//        }
//    }
//
//    public void delete(Long id) {
//        galleryRepository.deleteById(id);
//    }
//}

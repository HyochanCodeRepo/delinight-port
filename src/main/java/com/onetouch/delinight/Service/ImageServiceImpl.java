package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.ImageDTO;
import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Repository.ImageRepository;
import com.onetouch.delinight.Util.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ImageServiceImpl implements ImageService {
    @Value("${imgUploadLocation}") //이미지가 저장될 실질적인 위치
    private String imgLocation;

    //private final S3Service s3Service;
    private final FileUpload fileUpload;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final StoreService storeService;
    private final HotelService hotelService;

    @Override
    public String readHotel(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findByHotelEntity_Id(id);
        if(imageEntity.isPresent()){
            return imageEntity.get().getFullUrl();
        }else {
            return "/img/defaultImg.png";
        }
    }

    @Override
    public String readStore(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findByStoreEntity_Id(id);
        if(imageEntity.isPresent()){
            return imageEntity.get().getFullUrl();
        }else {
            return "/img/defaultImg.png";
        }
    }

    @Override
    public String read(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findByMenuEntity_Id(id);//받은 이미지 넘버로 url 던지기
        if(imageEntity.isPresent()){
            return imageEntity.get().getFullUrl();
        }else {
            return "/img/defaultImg.png";
        }

    }

    @Override
    public Map<Long,String> register(MultipartFile multipartFile, String imgType) throws IOException, InterruptedException {

        UUID uuid = UUID.randomUUID();
        String originName = multipartFile.getOriginalFilename();
        String fileName = uuid+"_"+originName.substring(originName.lastIndexOf("/")+1);
        String fullUrl = "/images/"+fileName;

        //FileUpload로 변경처리
        fileUpload.FileUpload(imgLocation, multipartFile, fileName, imgType);
        //s3Service.upload(multipartFile, "/images/"+fileName, imgType);

        //fullUrl = "https://myimghouse.s3.ap-northeast-2.amazonaws.com/images/"+fileName;
        fullUrl = "/images/"+fileName;

        ImageDTO imageDTO = ImageDTO.builder().fullUrl(fullUrl).originName(originName).fileName(fileName).build();
        ImageEntity imageEntity = modelMapper.map(imageDTO, ImageEntity.class);
        ImageEntity afterImage = imageRepository.save(imageEntity);
        Map<Long, String> result = new HashMap<>();
        result.put(afterImage.getImgNum(), afterImage.getFullUrl());
        return result;
    }

    @Override
    public void delete(Long imgNum) {
        Optional<ImageEntity> optionalImage = imageRepository.findById(imgNum);

        if(optionalImage.isPresent()){
            ImageEntity imageEntity = optionalImage.get();

            //fileupload로 파일 삭제 처리
            fileUpload.Filedelete(imgLocation, imageEntity.getFileName());
            //s3Service.deleteFile(imageEntity.getFullUrl());

            imageRepository.deleteById(imgNum);
        }
        else {
        }
    }

    @Override
    public void update(MultipartFile multipartFile, Long imgNum, String imgType) throws IOException, InterruptedException {
        Optional<ImageEntity> optionalImage = imageRepository.findById(imgNum);

        if(optionalImage.isPresent()) {
            fileUpload.Filedelete(imgLocation, optionalImage.get().getFileName());

            UUID uuid = UUID.randomUUID();
            String originName = multipartFile.getOriginalFilename();
            String fileName = uuid+"_"+originName.substring(originName.lastIndexOf("/")+1);
            String fullUrl = "/images/"+fileName;

            //fileupload로 변경
            fileUpload.FileUpload(imgLocation, multipartFile, fileName, imgType);
            //s3Service.upload(multipartFile, "images/"+fileName, imgType);

            //s3Service.deleteFile(fullUrl);
            fullUrl = "/images/"+fileName;

            ImageEntity imageEntity = optionalImage.get();
            imageEntity.setFullUrl(fullUrl);
            imageEntity.setFileName(fileName);
            imageEntity.setOriginName(originName);
            imageRepository.save(imageEntity);
        }
    }

    @Override
    public boolean ExistStoreImgByEmail(String email) {
        Long id = storeService.findStoreByEmail(email);
        boolean result = imageRepository.existsByStoreEntity_Id(id);
        return result;
    }
    @Override
    public boolean ExistHotelImgByEmail(String email) {
        Long id = hotelService.findHotelByEmail(email);
        boolean result = imageRepository.existsByHotelEntity_Id(id);
        return result;
    }

    @Override
    public void dummyImgDelete() {
        imageRepository.deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNullAndStoreEntityIsNullAndAndRoomCareMenuEntityIsNull(LocalDateTime.now().minusHours(3));
    }
}

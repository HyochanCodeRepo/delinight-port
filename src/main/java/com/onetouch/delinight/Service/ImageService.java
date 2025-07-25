package com.onetouch.delinight.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageService {

    Map<Long, String> register(MultipartFile multipartFile, String imgType) throws IOException, InterruptedException;
    String read(Long id);
    String readStore(Long id);
    String readHotel(Long id);


    void delete(Long imgNum);

    void update(MultipartFile multipartFile, Long num, String imgType) throws IOException, InterruptedException;
    void dummyImgDelete();
    boolean ExistStoreImgByEmail(String email);
    boolean ExistHotelImgByEmail(String email);



}

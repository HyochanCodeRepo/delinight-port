package com.onetouch.delinight.Util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//개인이 개별적으로 만든 메소드를 등록(Controller, Service에 주로 사용)
@Component
public class FileUpload {
    //2개 파일을 저장, 파일을 삭제
    public String FileUpload(String imgLocation, MultipartFile imageFile, String fileName, String imgType) {  //저장할 파일을 받아서 지정한 위치에 저장
        //파일에서 이름만 분리-새이름과 확장자를 결합해서 저장
        //String originalFilename = imageFile.getOriginalFilename(); //파일명읽기 poster.jpg
        //String extension = originalFilename.substring(originalFilename.lastIndexOf("."));  //확장자 분리 .jpg
        //UUID uuid = UUID.randomUUID();  //난수로 새이름 생성 1234-6554562
        //String saveFileName = uuid +extension;  //새파일명 123450-5217466.jpg
        String uploadUrl = imgLocation+fileName;    //c:/movie/poster/123450-5217466.jpg

        //저장작업(try 오류발생시 정상적으로 동작하세)
        try {
            byte[] resizedImage = reSizing(imageFile, imgType);

            File folder = new File(imgLocation);
            if (!folder.exists()) {  //c:/movie/poster폴더가 존재하지 않으면 만든다.
                boolean result = folder.mkdirs();
            }
            //byte[] filedata = imageFile.getBytes(); //파일을 읽어서
            FileOutputStream fos = new FileOutputStream(uploadUrl);
            fos.write(resizedImage); //지정된 위치에 파일 쓰기를 진행
            fos.close();

        }catch (Exception e) {
            return null;
        }

        return fileName;  //새로운 파일명을 전단
    }

    public byte[] reSizing(MultipartFile multipartFile, String imgType) throws IOException {

        int width = 600;
        int height;
        if(imgType.equals("square")){
            height = 600;
        }else {
            height = 200;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(multipartFile.getInputStream()).forceSize(width, height).outputFormat("jpg").toOutputStream(byteArrayOutputStream);

        byte[] resizedImage = byteArrayOutputStream.toByteArray();

        return  resizedImage;
    }

    public void Filedelete(String imgLocation, String imageFileName){  //지정한 파일을 찾아서 삭제
        //int lastSlashIndex = imageFileName.lastIndexOf("/");
        //String temp = imageFileName.substring(lastSlashIndex+1);

        String deleteFileName = imgLocation+imageFileName;

        try {
            File deleteFile = new File(deleteFileName);
            if(deleteFile.exists()){
                deleteFile.delete();
            }
        } catch (Exception e) {

        }
    }
}

package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class MembersServiceImplTest {

    @Autowired
    MembersRepository membersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CenterRepository centerRepository;
    @Autowired
    HotelRepository hotelRepository;


    @Test
    public void listTest(){
        List<MembersEntity> membersEntityList =
            membersRepository.selectHotelAd();

        log.info(membersEntityList.size());
    }
    @Test
    @Commit
    public void createTest(){

       Long hotelId = 11L;
       String hotelName = "잠실점";
       String hotelEmail = "11-";

       for(int i=1; i<10; i++){
           MembersEntity membersEntity = new MembersEntity();
           membersEntity.setEmail("store"+hotelEmail+i+"@test.com");
           membersEntity.setName(hotelName+i);
           membersEntity.setPassword(passwordEncoder.encode("akqjqtk12!"));
           membersEntity.setPhone("0101113122"+i);
           membersEntity.setRole(Role.STOREADMIN);
           membersEntity.setStatus(Status.VALID);
           membersEntity.setHotelEntity(hotelRepository.findById(hotelId).get());
           membersRepository.save(membersEntity);
       }


    }

    @Test
    public void listTtest(){
        String email = "super@test.com";
        List<HotelEntity> hotelEntityList =
            hotelRepository.selectallBySuper(email);

        log.info(hotelEntityList);

    }

    @Test
    @Commit
    public void EncodingTest(){
        MembersEntity membersEntity = membersRepository.findById(137L).get();
        membersEntity.setPassword(passwordEncoder.encode("akqjqtk12!"));
        membersRepository.save(membersEntity);
    }

//
//
//    @Test
//    public void createTest(){
//        MembersDTO membersDTO = new MembersDTO();
//        membersDTO.setName("이효찬");
//        membersDTO.setEmail("harry@naver.com");
//        membersDTO.setPhone("01041634202");
//
//        membersService.create(membersDTO);
//    }
//
//    @Test
//    public void listTest(){
//
//        List<MembersDTO> membersDTOList =
//            membersService.findAll();
//        membersDTOList.forEach(member -> log.info(member));
//
//
//    }



}
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.GuestRepository;
import com.onetouch.delinight.Repository.RoomRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2

class CheckInServiceImplTest {

    @Autowired
    CheckInService checkInService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    GuestRepository guestRepository;
    @Autowired
    CheckInRepository checkInRepository;

//    @Test
//    public void createTest(){
//
//        CheckInDTO checkInDTO = new CheckInDTO();
//        UsersEntity usersEntity =
//            usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//        UsersDTO usersDTO =
//            modelMapper.map(usersEntity, UsersDTO.class);
//
//        checkInDTO.setPrice(10000);
//        checkInDTO.setCheckoutDate(LocalDate.now());
//        checkInDTO.setCheckinDate(LocalDate.now());
//        checkInDTO.setCheckInStatus(CheckInStatus.CHECKIN);
//        checkInDTO.setUsersDTO(usersDTO);
//
//        checkInService.create(checkInDTO);
//
//
//    }
    @Test
    public void test(){
        GuestEntity guestEntity =
            guestRepository.findById(1L).orElseThrow();

        log.info(guestEntity);
        log.info(guestEntity);
        log.info(guestEntity.getPhone());
        log.info(guestEntity.getPhone());

        String num = guestEntity.getPhone();
        String lastfour =
            num.substring(num.length() - 4);
        log.info(lastfour);
        log.info(lastfour);

    }

    @Test
    @Commit
    public void checkInAll(){

            for(int i = 4; i<139; i++){
                if(!roomRepository.findById((long)i).isPresent()){
                    continue;
                }
                CheckInDTO checkInDTO = new CheckInDTO();
                checkInDTO.setId((long)i+2);
                checkInDTO.setPassword("1234");
                checkInDTO.setCheckinDate(LocalDate.now());
                checkInDTO.setCheckoutDate(LocalDate.now().plusDays(1));
                checkInDTO.setUserId((long)i+2);
                checkInService.checkin(checkInDTO);
            }
    }



}
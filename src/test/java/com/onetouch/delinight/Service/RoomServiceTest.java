package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.RoomRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
@Transactional
class RoomServiceTest {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CenterRepository centerRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    RoomService roomService;



    @Test
    @Commit
    public void roomInsert(){


        Long hotelId = 10L;
        String roomName = "10";
        String content = "1bed, 1room";

        for(int i=2; i<10; i++){
            RoomDTO roomDTO = new RoomDTO();
            HotelDTO hotelDTO = new HotelDTO();
            hotelDTO.setId(hotelId);
            roomDTO.setContent(content);
            roomDTO.setName(roomName+i+"호");
            roomDTO.setHotelDTO(hotelDTO);
            roomService.create(roomDTO);
        }


    }


}
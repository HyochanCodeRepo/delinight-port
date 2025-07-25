package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CheckInService {

    //    public void create(CheckInDTO checkInDTO, String email);
    void create(RoomEntity roomEntity);


    List<CheckInDTO> list();

    List<CheckInDTO> list2();

    void checkin(CheckInDTO checkInDTO);

    void checkout(Long id);

    UsersDTO  checkEmail(String email);

    List<CheckInDTO> getListCheckinByStatus(CheckInStatus checkInStatus, String email);

    CheckInDTO findCheckInByEmail(String email);
    HotelEntity findHotelInByEmail(Long CheckInId);


    void del(Long id);

    List<CheckInDTO> listCheckInWithPrice(String email);





}

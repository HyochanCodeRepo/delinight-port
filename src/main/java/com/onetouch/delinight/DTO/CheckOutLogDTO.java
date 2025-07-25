/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckOutLogDTO {

    private Long id;                    // checkOutLogId

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private int price;
    private String phone;



    private UsersDTO usersDTO;

    private GuestDTO guestDTO;

    private RoomDTO roomDTO;

    private CheckInStatus checkInStatus;
    public CheckOutLogDTO setRoomDTO(RoomDTO roomDTO){
        this.roomDTO = roomDTO;
        return this;
    }

    public CheckOutLogDTO setUsersDTO(UsersDTO usersDTO){
        this.usersDTO = usersDTO;
        return  this;
    }

    public CheckOutLogDTO setGuestDTO(GuestDTO guestDTO){
        this.guestDTO = guestDTO;
        return  this;
    }
}

/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.CheckInStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {

    private Long id;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;

    private Long price;

    private String phone;

    private String email;

    private Long userId;


    private String certId;
    private String password;

    private UsersDTO usersDTO;

    private GuestDTO guestDTO;

    private RoomDTO roomDTO;

    private CheckInStatus checkInStatus;

    public CheckInDTO setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
        return this;
    }




    public CheckInDTO setGuestDTO(GuestDTO guestDTO) {
        this.guestDTO = guestDTO;
        return this;
    }

    public CheckInDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CheckInDTO setRoomId(Long roomid) {
        if (this.roomDTO == null) {
            this.roomDTO = new RoomDTO(); // roomDTO가 null이면 초기화
        }
        this.roomDTO.setId(roomid); // RoomDTO의 id를 설정
        return this;
    }

    public CheckInDTO setCertId(String certId) {

        this.certId = certId;
        return this;
    }

    public CheckInDTO setPassword(String password) {
        this.password = password;
        return this;

    }

    public CheckInDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public CheckInDTO setUsersDTO(UsersDTO usersDTO) {
        this.usersDTO = usersDTO;
        return this;
    }

//    public CheckInDTO setOrderPrice(Long price) {
//        this.price = price;
//        return this;
//    }





}

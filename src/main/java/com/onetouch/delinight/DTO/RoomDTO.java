/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long id;


    private String name;

    private String content;



    private HotelDTO hotelDTO;

    private UsersDTO usersDTO;


    public RoomDTO setHotelDTO(HotelDTO hotelDTO) {
        this.hotelDTO = hotelDTO;
        return this;
    }

    public RoomDTO setId(Long id) {
        this.id = id;
        return this;
    }





}

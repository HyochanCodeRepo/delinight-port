/*********************************************************************
 * 클래스명 : RoomCareEntity
 * 기능 : 유저가 주문한 룸케어아이템들을 모아서 보낸다.
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.RoomCareStatus;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.RoomCareItemEntity;
import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCareDTO{

    private Long id;

    private List<RoomCareItemDTO> roomCareItemDTOList; // 룸케어아이템 리스트
    private HotelDTO hotelDTO; // 호텔
    private CheckInDTO checkInDTO; // 체크인 정보
    private CheckOutLogDTO checkOutLogDTO;

    private LocalDateTime awaitingTime; // 요청시간
    private LocalDateTime deliveredTime; // 완료시간


    private RoomCareStatus roomCareStatus; // 요청완료 상태

    public RoomCareDTO setCheckInDTO(CheckInDTO checkInDTO){
        this.checkInDTO = checkInDTO;
        return this;
    }
    public RoomCareDTO setCheckOutLogDTO(CheckOutLogDTO checkOutLogDTO){
        this.checkOutLogDTO = checkOutLogDTO;
        return this;
    }
    public RoomCareDTO setHotelDTO(HotelDTO hotelDTO){
        this.hotelDTO = hotelDTO;
        return this;
    }

    public RoomCareDTO setRoomCareItemDTOList(List<RoomCareItemDTO> roomCareItemDTOList){
        this.roomCareItemDTOList = roomCareItemDTOList;
        return this;
    }

}

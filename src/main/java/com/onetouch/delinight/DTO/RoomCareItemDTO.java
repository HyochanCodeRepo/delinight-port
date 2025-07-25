/*********************************************************************
 * 클래스명 : RoomCareItemEntity
 * 기능 : 룸케어 메뉴 항목들
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.RoomCareEntity;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCareItemDTO {

    private Long id; //룸케어아이템 코드번호

    @ToString.Exclude
    private RoomCareDTO roomCareDTO; // 룸케어

    private Long roomCareMenuDTOId;
    private Long quantity;

    private RoomCareMenuDTO roomCareMenuDTO;

    public RoomCareItemDTO setRoomCareMenuDTO(RoomCareMenuDTO roomCareMenuDTO){
        this.roomCareMenuDTO = roomCareMenuDTO;
        return this;
    }

    public RoomCareItemDTO setRoomCareDTO(RoomCareDTO roomCareDTO){
        this.roomCareDTO = roomCareDTO;
        return this;
    }
}

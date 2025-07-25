/*********************************************************************
 * 클래스명 : RoomCareItemEntity
 * 기능 : 룸케어 메뉴 항목들
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_care_item")
public class RoomCareItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_care_item_id")
    private Long id; //룸케어아이템 코드번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_care_id")
    private RoomCareEntity roomCareEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_care_menu_id")
    private RoomCareMenuEntity roomCareMenuEntity;

    private Long quantity;
}

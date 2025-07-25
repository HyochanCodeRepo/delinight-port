/*********************************************************************
 * 클래스명 : RoomCareEntity
 * 기능 : 유저가 주문한 룸케어아이템들을 모아서 보낸다.
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.Constant.RoomCareStatus;
import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_care")
public class RoomCareEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_care_id")
    private Long id;

    @ToString.Exclude
    @OneToMany(mappedBy = "roomCareEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomCareItemEntity> roomCareItemEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity; // 호텔

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id")
    private CheckInEntity checkInEntity; // 체크인 정보

    private LocalDateTime awaitingTime; // 요청시간
    private LocalDateTime deliveredTime; // 완료시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    private CheckOutLogEntity checkOutLogEntity; // 체크아웃 정보


    @Enumerated(EnumType.STRING)
    RoomCareStatus roomCareStatus; // 요청완료 상태

}

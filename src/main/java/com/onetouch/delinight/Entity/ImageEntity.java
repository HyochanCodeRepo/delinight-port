/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class ImageEntity extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgNum;

    private String fullUrl;
    private String fileName;
    private String originName;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menuEntity;

    @ManyToOne
    @JoinColumn(name="store_id")
    private StoreEntity storeEntity;

    @ManyToOne
    @JoinColumn(name = "room_care_menu_id")
    private RoomCareMenuEntity roomCareMenuEntity;
}

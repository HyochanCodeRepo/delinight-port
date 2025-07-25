/*********************************************************************
 * 클래스명 : MenuEntity
 * 기능 : MenuEntity 항목 수정(price,stockNumber, MenuStatus 추가)
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.AbleCheck;
import com.onetouch.delinight.Constant.Menu;
import com.onetouch.delinight.Constant.MenuStatus;
import com.onetouch.delinight.Constant.RoomCareStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_care_menu")
public class RoomCareMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_care_menu_id")
    private Long id; //메뉴 코드번호

    @Column(nullable = false, length = 50)
    private String name; //메뉴명

    @Column(nullable = false, length = 200)
    private String content; //내용



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;


    private AbleCheck ableCheck;








}

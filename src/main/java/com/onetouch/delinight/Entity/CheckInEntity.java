/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;
import com.onetouch.delinight.Constant.CheckInStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checkin")
public class CheckInEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkin_id")
    private Long id;


    private LocalDate checkinDate;

    private LocalDate checkoutDate;


    private int price;
    private String phone;

    private String password;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UsersEntity usersEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private GuestEntity guestEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity roomEntity;

    @Enumerated(EnumType.STRING)
    private CheckInStatus checkInStatus;

    public CheckInEntity setUsersEntity(UsersEntity usersEntity){
        this.usersEntity = usersEntity;
        return  this;
    }

    public CheckInEntity setGuestEntity(GuestEntity guestEntity){
        this.guestEntity = guestEntity;
        return  this;
    }

}


/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "check_out_log")
public class CheckOutLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;


    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private int price;
    private String phone;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    @ToString.Exclude
    private UsersEntity usersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private GuestEntity guestEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity roomEntity;

    @Enumerated(EnumType.STRING)
    private CheckInStatus checkInStatus;

    public CheckOutLogEntity setUsersEntity(UsersEntity usersEntity){
        this.usersEntity = usersEntity;
        return  this;
    }

    public CheckOutLogEntity setGuestEntity(GuestEntity guestEntity){
        this.guestEntity = guestEntity;
        return  this;
    }

    @OneToMany(mappedBy = "checkOutLogEntity", fetch = FetchType.LAZY)
    private List<NetPromoterScoreEntity> netPromoterScoreEntity=new ArrayList<>();






}

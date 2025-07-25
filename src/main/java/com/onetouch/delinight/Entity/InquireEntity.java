package com.onetouch.delinight.Entity;


import com.onetouch.delinight.Constant.InquireType;
import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inquire")
public class InquireEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquire_id")
    private Long id;

    @Column(length = 50,nullable = false) //컬럼길이, null허용여부
    private String title;
    @Column(length = 2000, nullable = true)
    private String content;

    @Column(nullable = true) //null을 허용하여 답변 시간이 없을 때 null로 유지
    private LocalDateTime responseTime; //답변시간

    @Enumerated(EnumType.STRING)
    private InquireType inquireType;




    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "checkin_id")
    private CheckInEntity checkInEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    private CheckOutLogEntity checkOutLogEntity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UsersEntity usersEntity;

}

package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NetPromoterScoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "netpromoterscore_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    @ToString.Exclude
    private CheckOutLogEntity checkOutLogEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private OrdersEntity ordersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    // 설문 1 ~ 5 문항
    private int questionOne;
    private int questionTwo;
    private int questionThree;
    private int questionFour;
    private int questionFive;

    private LocalDateTime insertTime;

    // 기타 문의사항
    private String etcQuestion;


    // 호텔 + 스토어 설문의 총점
    @Column(name = "total_score")
    private int totalScore;






}

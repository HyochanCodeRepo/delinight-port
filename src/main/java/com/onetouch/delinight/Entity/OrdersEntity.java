/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;


import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.OrdersStatus;
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
@Table(name = "orders")
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;


    @Column(name="orders_memo")
    private String memo;

    @ToString.Exclude
    @OneToMany(mappedBy = "ordersEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdersItemEntity> ordersItemEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id")
    private CheckInEntity checkInEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    private CheckOutLogEntity checkOutLogEntity;

    private LocalDateTime pendingTime;
    private LocalDateTime awaitingTime;
    private LocalDateTime preparingTime;
    private LocalDateTime deliveringTime;
    private LocalDateTime deliveredTime;



    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    OrdersStatus ordersStatus; // 현 주문 상태

    public OrdersEntity setCheckOutLogEntity(CheckOutLogEntity checkOutLogEntity) {
        this.checkOutLogEntity = checkOutLogEntity;
        return this;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @ToString.Exclude
    private PaymentEntity paymentEntity;






}

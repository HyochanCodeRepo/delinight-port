/*********************************************************************
 * 클래스명 : PaymentEntity
 * 기능 : 정산
 * 작성자 : 이동건
 * 작성일 : 2025-04-15
 * 수정 : 2025-04-15
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToMany(mappedBy = "paymentEntity", fetch = FetchType.LAZY)
    private List<OrdersEntity> ordersEntityList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MembersEntity members;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    OrderType orderType;            // 선결제(PAYNOW) or 후결제(PAYLATER)

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_check")
    private PaidCheck paidCheck;    // 정산상태


    @Column(name = "price_month")
    private String priceMonth;      // 정산 연월




}

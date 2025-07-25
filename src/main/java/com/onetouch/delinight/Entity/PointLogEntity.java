package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.PointType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pointLog")
public class PointLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointLog_id")
    private Long id;

    @Column(name = "point_amount")
    private int pointAmount; // 적립 및 사용 포인트 양

    @Enumerated(EnumType.STRING)
    @Column(name = "point_type")
    private PointType pointType; //적립(EARN) or 사용(USE)

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;
}

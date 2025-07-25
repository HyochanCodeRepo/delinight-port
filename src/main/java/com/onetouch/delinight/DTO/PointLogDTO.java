package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.PointType;
import com.onetouch.delinight.Entity.PaymentEntity;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointLogDTO {

    private Long id; //포인트 아이디

    private int pointAmount; //포인트 양

    private PointType pointType;

    private PaymentEntity payment;
}

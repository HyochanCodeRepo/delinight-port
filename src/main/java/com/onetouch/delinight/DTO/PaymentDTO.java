/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;


import com.onetouch.delinight.Constant.PaidCheck;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {


    private Long totalId;               // 정산 ID
    private PaidCheck checkPaid;        // 정산상태
    private LocalDateTime regTime;      // 등록일

    // Excel → 계산식
    private int vat;         // 부가세
    private int totalPrice;  // 합계 금액
    private int unpaid;      // 미결제 금액


    private List<OrdersDTO> ordersDTOList;
    private List<MenuDTO> menuDTOList;

    public PaymentDTO setOrdersDTOList(List<OrdersDTO> ordersDTOList) {
        this.ordersDTOList = ordersDTOList;
        return this;

    }

    public PaymentDTO setMenuDTOList(List<MenuDTO> menuDTOList) {
        this.menuDTOList = menuDTOList;
        return this;
    }


}


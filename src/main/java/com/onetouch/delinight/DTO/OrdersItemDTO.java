/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Entity.OrdersEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersItemDTO {

    private Long id;
    private Long quantity;
    private OrdersDTO ordersDTO;
    private MenuDTO menuDTO;


    public OrdersItemDTO setOrdersDTO(OrdersDTO ordersDTO){
        this.ordersDTO = ordersDTO;
        return  this;
    }
    public OrdersItemDTO setMenuDTO(MenuDTO menuDTO){
        this.menuDTO = menuDTO;
        return  this;
    }

}

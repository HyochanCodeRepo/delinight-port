/*********************************************************************
 * 클래스명 : CartItemDTO
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Entity.CartEntity;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long id;
    private CartDTO cartDTO;
    private MenuDTO menuDTO;
    private Long quantity;
    public CartItemDTO setCartDTO(CartDTO cartDTO){
        this.cartDTO = cartDTO;
        return this;
    }
    public CartItemDTO setMenuDTO(MenuDTO menuDTO){
        this.menuDTO = menuDTO;
        return this;
    }


}

/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.Entity.GuestEntity;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface CartService {

    Long cartCheck(String email);

    List<CartItemDTO> list(Long cartNum);
    void makeCart(int sep, Long id);
    void deleteCart(int sep, Long cartNum);

    Integer add(Long cartNum, Long menuNum);
    String clear(Long cartNum);

    void remove(Long cartItemNum);

    Long cartToOrder(Long cartNum);
    String plusQuantity(Long cartItemNum);
    String minusQuantity(Long cartItemNum);


}

package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.Entity.CartEntity;
import com.onetouch.delinight.Repository.CartRepository;
import com.onetouch.delinight.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
@Log4j2
public class CartRestController {
    private final CartRepository cartRepository;

    private final CartService cartService;

    @GetMapping("/showList")
    public ResponseEntity<List<CartItemDTO>> showList(Principal principal) {
        Long cartNum = cartService.cartCheck(principal.getName());
        List<CartItemDTO> cartItemDTOList = cartService.list(cartNum);
        return ResponseEntity.ok(cartItemDTOList);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(Long menuNum, Principal principal) {
        log.info("임"+principal.getName());
        Long cartId = cartService.cartCheck(principal.getName()); // 원래는 회원이면 principal로 해당 카트 찾아서 맵핑
        Integer existCartItem = cartService.add(cartId, menuNum);
        if (existCartItem == 2) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.ok("2");
        }

    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<String> removeFromCart(Long cartItemNum, Principal principal) {

        cartService.remove(cartItemNum);

        return ResponseEntity.ok("삭제 성공");


    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(Principal principal) {
        Long id = cartService.cartCheck(principal.getName());
        cartService.clear(id);
        return ResponseEntity.ok("비우기 성공");
    }


    @PostMapping("/cartToOrder")
    public String  cartToOrder(Principal principal) {
        Long cartId;
        CartEntity cartEntity = cartRepository.findByUsersEntity_Email(principal.getName());
        CartEntity cartEntity1 = cartRepository.findByGuestEntity_Phone(principal.getName());
        if(cartEntity!=null){
            cartId = cartEntity.getId();
        }
        else {
            cartId = cartEntity1.getId();
        }
        log.info("진입여부");
        Long paymentId = cartService.cartToOrder(cartId);
        return String.valueOf(paymentId);
    }

    @PostMapping("/plusQuantity")
    private ResponseEntity<String> plusQuantity(Long cartItemNum){
        String result = cartService.plusQuantity(cartItemNum);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/minusQuantity")
    private ResponseEntity<String> minusQuantity(Long cartItemNum){
        String result = cartService.minusQuantity(cartItemNum);
        return ResponseEntity.ok(result);
    }


}

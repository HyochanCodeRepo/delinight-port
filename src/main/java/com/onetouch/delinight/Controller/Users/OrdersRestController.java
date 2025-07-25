package com.onetouch.delinight.Controller.Users;


import com.onetouch.delinight.Service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomService/order")
@Log4j2
public class OrdersRestController {

    private final OrdersService ordersService;


    @PostMapping("/payNow")
    public ResponseEntity<String> payNow(Long ordersId, String memo, Principal principal){
        ordersService.changePayNow(ordersId, memo, principal.getName());
        return ResponseEntity.ok("저장 성공");
    }


    @PostMapping("/payLater")
    public ResponseEntity<String> payLater(Long ordersId, String memo, Principal principal){
        ordersService.changePayLater(ordersId, memo, principal.getName());
        return ResponseEntity.ok("저장 성공");
    }
}

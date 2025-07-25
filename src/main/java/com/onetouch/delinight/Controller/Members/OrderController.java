/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Service.OrdersService;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/store/orders")
public class OrderController {
    private final OrdersService ordersService;

    @GetMapping("/list")
    public String orderList(@AuthenticationPrincipal MemberDetails memberDetails, Model model){
        String email = memberDetails.getUsername();
        log.info(email);
        model.addAttribute("member", memberDetails.getMembersEntity());
        StoreDTO storeDTO = ordersService.findStoreByADMINEmail(email);
        model.addAttribute("storeDTO", storeDTO);
        return "members/orders/list";

    }

}

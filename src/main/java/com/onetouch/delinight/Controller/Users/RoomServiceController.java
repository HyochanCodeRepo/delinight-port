/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.CheckInService;
import com.onetouch.delinight.Service.MenuService;
import com.onetouch.delinight.Service.OrdersService;
import com.onetouch.delinight.Service.PointService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users/order/")

public class RoomServiceController {
    private final MenuService menuService;
    private final OrdersService ordersService;
    private final CheckInService checkInService;

    private final PointService pointService;

    @GetMapping("/main")
    public String main(Model model, Principal principal){
        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(principal.getName());
        List<MenuDTO> menuDTOList = menuService.menuListByHotel(checkInDTO.getRoomDTO().getHotelDTO().getId());
        model.addAttribute("menuDTOList", menuDTOList);
        log.info(menuDTOList);
        return "users/order/main";
    }

    @GetMapping("/list")
    public String list(Principal principal, Model model){
        List<OrdersDTO> ordersDTOList = ordersService.ordersListByEmail(principal.getName());
        model.addAttribute("ordersDTOList", ordersDTOList);
        return "users/order/list";
    }

    @GetMapping("/hub")
    public String hub(){
        return "redirect:/users/order/list";
    }

    @GetMapping("/read")
    public String read(Long orderId, Model model){
        OrdersDTO ordersDTO = ordersService.readOne(orderId);
        log.info(ordersDTO);
        model.addAttribute("ordersDTO", ordersDTO);
        return "users/order/read";

    }

    @GetMapping("/request")
    public String request(Long paymentId, Model model){
        List<OrdersDTO> ordersDTOList = ordersService.read(paymentId);
        Long totalPrice = 0L;
     for(int i = 0; i<ordersDTOList.size(); i++){
         totalPrice += ordersDTOList.get(i).getTotalPrice();
        }

     log.info(totalPrice);
        log.info(ordersDTOList);
     model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("ordersDTOList", ordersDTOList);
        return "users/order/request";
    }







}

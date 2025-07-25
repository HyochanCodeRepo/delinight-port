package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.CheckInService;
import com.onetouch.delinight.Service.MenuService;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping("/roomService")
@RequiredArgsConstructor
@RestController
@Log4j2
public class RoomServiceRestController {

    private final CheckInService checkInService;
    private final RoomService roomService;
    private final MenuService menuService;

    @GetMapping("/order/showMainList")
    public ResponseEntity<List<MenuDTO>> showList(Principal principal) {
        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(principal.getName());
        List<MenuDTO> menuDTOList = menuService.menuListByHotel(checkInDTO.getRoomDTO().getHotelDTO().getId());
        log.info(menuDTOList);
        return ResponseEntity.ok(menuDTOList);
    }



}

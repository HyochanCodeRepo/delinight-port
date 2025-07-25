package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomCareMenuDTO;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.RoomCareMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/roomCare/Menu")
public class RoomCareMenuController {

    private final RoomCareMenuService roomCareMenuService;
    private final HotelService hotelService;

    @GetMapping("/list")
    public String list(Principal principal, Model model){
        Long hotelId = hotelService.findHotelByEmail(principal.getName());
        List<RoomCareMenuDTO> list = roomCareMenuService.list(hotelId);
        model.addAttribute("list", list);
        model.addAttribute("hotelId", hotelId);

        return "members/roomCare/menu/list";
    }
}

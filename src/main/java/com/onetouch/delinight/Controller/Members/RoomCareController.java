/*********************************************************************
 * 클래스명 : RoomCareItemController
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.RoomCareDTO;
import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/roomCare")
@Log4j2
public class RoomCareController {

    private final CheckInService checkInService;
    private final MembersService membersService;
    private final HotelService hotelService;
    private final RoomCareItemService roomCareItemService;
    private final RoomCareService roomCareService;

    @GetMapping("/list")
    public String listView(Principal principal, Model model) {

        Long hotelId = hotelService.findHotelByEmail(principal.getName());
        HotelDTO hotelDTO =  hotelService.findHotelDTOById(hotelId);
        model.addAttribute("hotelDTO", hotelDTO);


        return "members/roomCare/listB";
    }



}

/*********************************************************************
 * 클래스명 : RoomCareItemController
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Service.RoomCareItemService;
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
@RequestMapping("/members/roomCareItem")
@Log4j2
public class RoomCareItemController {

    private final RoomCareItemService roomCareItemService;

    @GetMapping("/create")
    public String createView(){

        return "members/roomCareItem/create";
    }

    @PostMapping("/create")
    public String createProc(RoomCareItemDTO roomCareItemDTO, Principal principal) {

        String email = principal.getName();
        roomCareItemService.create(roomCareItemDTO, email);

        return "redirect:/members/roomCareItem/list";
    }

    @GetMapping("/list")
    public String listView(Model model){

        List<RoomCareItemDTO> roomCareItemDTOList =
                roomCareItemService.list();
        model.addAttribute("roomCareItemDTOList", roomCareItemDTOList);

        return "members/roomCareItem/list";
    }


}

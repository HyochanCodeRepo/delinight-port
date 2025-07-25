/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/room")
public class RoomController {

    private final ImageService imageService;
    private final RoomService roomService;

    @GetMapping("/list")
    public String listView(Model model, Principal principal) {
        boolean storeImgExistence = imageService.ExistHotelImgByEmail(principal.getName());
        if (storeImgExistence) {
            List<RoomDTO> roomDTOList = roomService.list(principal.getName());
            model.addAttribute("roomDTOList", roomDTOList);
            return "members/room/listB";
        } else {
            model.addAttribute("sep", "hotel");
            return "members/account/common/imgRedirect";
        }
    }

    @GetMapping("/listB")
    public String listView2(Model model, Principal principal) {
        List<RoomDTO> roomDTOList = roomService.list(principal.getName());
        model.addAttribute("roomDTOList", roomDTOList);
        return "members/room/listB";
    }
}

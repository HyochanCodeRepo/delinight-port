package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomCareDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.RoomCareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/roomCare/rest")
public class RoomCareRestController {

    private final MembersService membersService;
    private final RoomCareService roomCareService;

    @GetMapping("/showList")
    public ResponseEntity<List<RoomCareDTO>> showList(Principal principal){
        Long membersId = membersService.findByEmail(principal.getName()).getId();
        List<RoomCareDTO> result = roomCareService.showList(membersId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/changeStatus/{roomCareId}")
    public ResponseEntity<String> changeStatus(@PathVariable("roomCareId") Long id){
        roomCareService.changeStatus(id);
        return ResponseEntity.ok("상태 변경 완료");
    }

}

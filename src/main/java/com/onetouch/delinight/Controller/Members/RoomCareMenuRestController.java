package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomCareMenuDTO;
import com.onetouch.delinight.Service.RoomCareMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/roomCare/menu/rest")
public class RoomCareMenuRestController {

    private final RoomCareMenuService roomCareMenuService;
    @PostMapping("/register")
    public ResponseEntity<String> register(RoomCareMenuDTO roomCareMenuDTO){
        log.info(roomCareMenuDTO);
        roomCareMenuService.register(roomCareMenuDTO);
        return ResponseEntity.ok("저장 완료");

    }

    @PostMapping("/update")
    public ResponseEntity<String> update(RoomCareMenuDTO roomCareMenuDTO){
        log.info("업데이트 요청 : "+roomCareMenuDTO);
        roomCareMenuService.update(roomCareMenuDTO);
        return ResponseEntity.ok("수정 완료");
    }

    @GetMapping("/delete/{menuId}")
    public ResponseEntity<String> delete(@PathVariable("menuId") Long menuId){
        roomCareMenuService.delete(menuId);
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("/imgRead/{menuId}")
    public ResponseEntity<String> imgRead(@PathVariable("menuId") Long menuId){
        String imgFullUrl = roomCareMenuService.imgRead(menuId);
        if(imgFullUrl!=null){
            return ResponseEntity.ok(imgFullUrl);
        }
        else {
            return ResponseEntity.badRequest().body("등록된 이미지가 없습니다.");
        }
    }
}

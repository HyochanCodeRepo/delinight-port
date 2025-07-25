package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room/rest")
@Log4j2
public class RoomRestController {
    private final RoomService roomService;

    @PostMapping("/register")
    private ResponseEntity register(RoomDTO roomDTO, Principal principal) {


        String email = principal.getName();

        if (roomDTO == null) {
            return new ResponseEntity<String>("저장 실패", HttpStatus.BAD_REQUEST);
        } else {
            roomService.create(roomDTO,email);


            return new ResponseEntity<String>(HttpStatus.OK);

        }


    }

    @PostMapping("/modify")
    public ResponseEntity<String> modify(RoomDTO roomDTO) {
        log.info(roomDTO);

        roomService.update(roomDTO);

        return ResponseEntity.ok("성공");
    }

    @DeleteMapping("/del")
    public ResponseEntity<String> del(Long id) {

        log.info(id);
        try {
            roomService.del(id);
            return ResponseEntity.ok("성공");

        } catch (IllegalStateException e) {
            log.info("삭제 불가 : ", e.getMessage());
            return ResponseEntity.badRequest().body("현재 체크인 상태인 방입니다.");
        }




    }
}

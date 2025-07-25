package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkin/rest")
@Log4j2
public class CheckInRestController {
    private final CheckInService checkInService;


    @PostMapping("/register")
    public ResponseEntity<String> checkin(@RequestBody CheckInDTO checkInDTO,Long userId) {


        log.info("들어오는 값 : " + checkInDTO);




        checkInService.checkin(checkInDTO);
        log.info("certNum  : "+checkInDTO.getCertId());


        return ResponseEntity.ok("성공");
    }

    @PostMapping("/out")
    public ResponseEntity<String> checkout(@RequestParam Long id) {
        log.info("받은 id : " + id);

        checkInService.checkout(id);

        return ResponseEntity.ok("성공");

    }

    @PostMapping("/reservation")
    public ResponseEntity<Map<String, Object>> reservation(String reservNum) {
        log.info("받은 값 : " + reservNum);

        String[] parts = reservNum.split("/");

        Long roomid = Long.parseLong(parts[0]);
        String checkinDate = parts[1].substring(0, 6);
        String checkoutDate = parts[1].substring(6, 12);
        int password = Integer.parseInt(parts[2]);

        log.info("cert 값 나오냐??"+password);
        log.info("cert 값 나오냐??"+password);

        Map<String, Object> response = new HashMap<>();
        response.put("roomid", roomid);
        response.put("checkinDate", checkinDate);
        response.put("checkoutDate", checkoutDate);
        response.put("password", password);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkemail")
    public ResponseEntity<?> checkEmail( String emailcheck) {
        if (emailcheck == null || emailcheck.isEmpty()) {
            log.warn("emailcheck 값이 비어있습니다.");
            return ResponseEntity.badRequest().body("이메일 값이 비어 있습니다.");
        }

        log.info("들어온 값 : "+emailcheck);

        UsersDTO usersDTO =
            checkInService.checkEmail(emailcheck);

        if (usersDTO == null) {
            log.warn("해당 이메일로 회원 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
        }


        return ResponseEntity.ok(usersDTO);
    }

}

package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.AdMailDTO;
import com.onetouch.delinight.Util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/members/adMail")
@RequiredArgsConstructor
public class adMailController {
    private final EmailService emailService;

    @GetMapping("/sendPage")
    private String sendPage() {



        return "members/adMail/sendPage";
    }

    @PostMapping("/send")
    @ResponseBody
    private ResponseEntity<String> send(Principal principal, AdMailDTO adMailDTO) {
        try {

            log.info("Sending ad mail : {}", adMailDTO.getImage());
            log.info("Sending ad mail : {}", adMailDTO.getImage().getBytes());
            emailService.sendAdMail(adMailDTO, principal.getName()); // 서비스에서 처리
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("메일 발송 실패");
        }


    }

}

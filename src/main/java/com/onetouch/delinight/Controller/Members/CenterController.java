/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.CenterDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Service.CenterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/center")
@Log4j2
public class CenterController {

    private final CenterService centerService;
    private final CenterRepository centerRepository;

    @GetMapping("/create")
    public String createView() {


        return "members/center/create";
    }

    @PostMapping("/create")
    public String createProc(CenterDTO centerDTO, Principal principal, Model model) {

        log.info("center create 진입");
        boolean hasError = false;

        if (centerDTO.getName() == null || centerDTO.getName().trim().isEmpty()) {
            model.addAttribute("nameError", "본사명을 입력해주세요");
            hasError = true;
        }
        if (centerDTO.getContent() == null || centerDTO.getContent().trim().isEmpty()) {
            model.addAttribute("contentError", "설명을 입력해주세요");
            hasError = true;
        }
        if (hasError) {
            return "members/center/create";
        }

        String email =
                principal.getName();

        MembersDTO membersDTO =
                centerService.checkEmail(email);

        log.info(membersDTO);

        centerService.create(centerDTO, email);

        return "redirect:/members/center/list";

    }

    @GetMapping("/list")
    public String listView(Model model) {
        List<CenterDTO> centerDTOList =
                centerService.list();
        model.addAttribute("centerDTOList", centerDTOList);
        return "members/center/list";
    }

    @GetMapping("/read")
    public String readView(Principal principal, Model model) {
        log.info("principal log ! : " + principal.getName());

        CenterDTO centerDTO =
                centerService.read(principal.getName());

        model.addAttribute("center", centerDTO);

        return "members/center/read";
    }

    @GetMapping("/update/{id}")
    public String updateView(Model model, @PathVariable("id") Long id) {
        log.info(id);


        CenterEntity centerEntity = centerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 center 없음"));

        model.addAttribute("center", centerEntity);

        return "members/center/update";
    }

    @PostMapping("/update")
    public String updateProc(@ModelAttribute CenterDTO centerDTO) {

        log.info("update post 페이지" + centerDTO);

        centerService.update(centerDTO);

        return "redirect:/members/center/read";
    }

    @DeleteMapping("/del")
    public ResponseEntity<String> del(@RequestParam Long id) {
        if (centerRepository.existsById(id)) {
            centerRepository.deleteById(id);
            return ResponseEntity.ok("삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 ID가 존재하지않습니다.");
        }
    }
}

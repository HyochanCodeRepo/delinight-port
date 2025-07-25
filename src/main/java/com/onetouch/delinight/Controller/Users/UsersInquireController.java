package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Service.CheckInService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.InquireService;
import com.onetouch.delinight.Service.UsersService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users/inquire")
public class UsersInquireController {
    private final InquireService inquireService;
    private final CheckInService checkInService;

    private final UsersService usersService;

    //등록get
    @Transactional
    @GetMapping("/register")
    public String register(InquireDTO inquireDTO, Long roomID, Long usersId){


        return "users/inquire/register";
    }
    //등록post
//    @PostMapping("/register")
//    public String registerPost(InquireDTO inquireDTO, Principal principal){
//
//        String loginId = null;
//        if (principal == null) {
//            // 익명 사용자의 경우 처리
//            System.out.println("로그인 안 됨. 익명으로 처리");
//        } else {
//            loginId = principal.getName();
//            System.out.println("로그인 사용자: " + loginId);
//        }
//
//
//        inquireService.register(inquireDTO,loginId);
//        return "redirect:/users/inquire/list";
//    }
    @PostMapping("/register")
    public String registerPost(@Valid InquireDTO inquireDTO, Principal principal){


        if (principal == null) {
            // 익명 사용자의 경우 처리
            System.out.println("로그인 안 됨. 익명으로 처리");
            return "redirect:/users/login";
        }
        String email = principal.getName(); // getName()이 email인지 확인 필요
        log.info("email은??" + email);


        inquireService.register(inquireDTO,email);
        return "redirect:/users/inquire/list";
    }
    //목록

    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            , Principal principal){ //usersId 파라미터로 받아서 해당 유저의 문의글만 조회



        Page<InquireDTO> inquireDTOList = inquireService.inquireList(pageable,principal.getName());

        log.info(principal.getName());

        log.info("list 읽어옴? : " + inquireDTOList);

        model.addAttribute("inquireDTOList",inquireDTOList);
        log.info("inquireDTOList 읽어옴??? : " + inquireDTOList);

        return "users/inquire/list";
    }


    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        InquireDTO inquireDTO = inquireService.read(id);
        model.addAttribute("inquireDTO", inquireDTO);
        return "users/inquire/read";
    }
    //수정get
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id")Long id,Model model){
        log.info("수정" +id);
        InquireDTO inquireDTO = inquireService.read(id);
        model.addAttribute("inquireDTO", inquireDTO);
        return "users/inquire/update";
    }
    //수정post
    @PostMapping("/update")
    public String updatePost(InquireDTO inquireDTO){
        log.info(inquireDTO);
        inquireService.update(inquireDTO);
        return "redirect:/users/inquire/read?id="+ inquireDTO.getId();
    }
    //삭제
    @PostMapping("/delete")
    public String deleteGET(Long id){

        log.info("히히아이디" + id);
        inquireService.delete(id);
        return "redirect:/users/inquire/list";
    }





}

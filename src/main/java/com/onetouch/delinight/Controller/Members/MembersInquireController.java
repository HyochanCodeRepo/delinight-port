package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.InquireService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/inquire")
public class MembersInquireController {
    private final InquireService inquireService;
    private final InquireRepository inquireRepository;
    private final MembersRepository membersRepository;
    private final HotelService hotelService;
    private final ReplyService replyService;
    private final MembersService membersService;
    private final ModelMapper modelMapper;


    // 멤버에서는 읽기(읽기 페이지 속에 댓글이 들어가는거임), 리스트

    //목록
    @GetMapping("/list")
    public String list(Model model, Principal principal,@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Long hotelId = hotelService.findHotelByEmail(principal.getName());

        log.info(hotelId);
        Page<InquireDTO> inquireDTOList =
            inquireService.inquireList(pageable,hotelId);
        log.info(inquireDTOList);
        model.addAttribute("inquireDTOList", inquireDTOList);

        return "members/inquire/list";

    }





    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model){
        InquireDTO inquireDTO = inquireService.read(id);
        ReplyDTO replyDTO = replyService.findByInquireId(id);

        model.addAttribute("inquireDTO", inquireDTO);
        model.addAttribute("replyDTO", replyDTO);

        log.info(inquireDTO);
        log.info(inquireDTO);
        return "members/inquire/read";
    }

    @GetMapping("/dashboard")
    @ResponseBody
    public ResponseEntity<List<InquireDTO>> dashboard(Principal principal){

        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        List<InquireDTO> result = inquireService.findUnprocessedInquire(membersDTO);
        return ResponseEntity.ok(result);
    }

}

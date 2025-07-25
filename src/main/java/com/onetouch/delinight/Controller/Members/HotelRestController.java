package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel/rest")
@Log4j2
public class HotelRestController {
    private final HotelService hotelService;
    private final MembersService membersService;


    @PostMapping("/addMember")
    public ResponseEntity<String> addMember(Long membersId, Long hotelId){

        hotelService.addMembers(membersId, hotelId);

        return ResponseEntity.ok("정상 추가");
    }


    @GetMapping("/membersList")
    public ResponseEntity<List<MembersDTO>> membersList(Principal principal){
        List<MembersDTO> membersDTOList = membersService.findMembersListByCenterEmail(principal.getName());

        log.info(membersDTOList);


        return ResponseEntity.ok(membersDTOList);

    }

    @PostMapping("/del")
    public void del(Long id){
        hotelService.del(id);
    }

    @PostMapping("/modify")
    public ResponseEntity<String> modify(Long memberId,Long hotelId) {

        log.info(memberId);
        log.info(hotelId);


        hotelService.modify(memberId, hotelId);


        return ResponseEntity.ok("성공");

    }
}

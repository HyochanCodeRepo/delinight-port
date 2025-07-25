package com.onetouch.delinight.Controller.Members;


import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/nps")
@Log4j2
public class AdminNpsRestController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final MembersService membersService;

    @GetMapping("/list")
    public ResponseEntity<List<NetPromoterScoreDTO>> npsList(@AuthenticationPrincipal MemberDetails memberDetails) {
        Long memberId = memberDetails.getMembersEntity().getId();
        List<NetPromoterScoreDTO> netPromoterScoreDTOList = netPromoterScoreService.npsAll(memberId);
        log.info(netPromoterScoreDTOList);
        return ResponseEntity.ok(netPromoterScoreDTOList);
    }

    @PostMapping("/chart")
    public ResponseEntity<List<NetPromoterScoreDTO>> npsChart(@AuthenticationPrincipal MemberDetails memberDetails) {
        Long memberId = memberDetails.getMembersEntity().getId();
        List<NetPromoterScoreDTO> netPromoterScoreDTOList = netPromoterScoreService.npsAll(memberId);
        return ResponseEntity.ok(netPromoterScoreDTOList);
    }


    @GetMapping("/dashboard")
    public ResponseEntity<List<Integer>> dashboard(Principal principal){
        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        log.info(membersDTO);
        List<Integer> result = netPromoterScoreService.dashboard(membersDTO);
        return ResponseEntity.ok(result);
    }

}

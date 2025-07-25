package com.onetouch.delinight.Controller.Users;


import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/nps")
@Log4j2
public class NetPromoterScoreRestController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final MembersService membersService;

    @PostMapping("/saveSurvey")
    public ResponseEntity<String> saveSurvey(@RequestBody List<NetPromoterScoreDTO> npsDTOList) {

        Long checkOutId = 0L; // 체크아웃 ID 초기값 설정

        // 전달받은 DTO 리스트에서 checkOutId가 있는 항목을 찾아 저장
        for (NetPromoterScoreDTO dto : npsDTOList) {
            if (dto.getCheckOutId() != null) {
                checkOutId = dto.getCheckOutId(); // 마지막으로 발견된 checkOutId 사용
            }
        }

        // NPS 설문 응답 데이터를 DB에 저장하는 서비스 메서드 호출
        netPromoterScoreService.npsInsert(npsDTOList, checkOutId);

        return ResponseEntity.ok("success");

    }




}

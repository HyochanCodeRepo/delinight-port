package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.OrdersDTO;

import java.util.List;

public interface NetPromoterScoreService {


    List<OrdersDTO> npsSelect(Long checkOutId);

    void npsInsert(List<NetPromoterScoreDTO> npsDTOList, Long checkOutId);

    List<NetPromoterScoreDTO> npsAll(Long memberId);

    List<Integer> dashboard(MembersDTO membersDTO);

    String makePrompt(Long membersId);
    List<Integer> dailyPerformanceScore(String email);
    boolean npsOperationCheck(String email);



}

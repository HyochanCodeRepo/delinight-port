package com.onetouch.delinight.Controller.Members;


import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.OrdersService;

import com.onetouch.delinight.Util.MemberDetails;

import com.onetouch.delinight.Service.StoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
@Log4j2
public class StoreRestController {

    private final OrdersService ordersService;
    private final StoreService storeService;
    private final MembersService membersService;

    @GetMapping("/orders/processingList")
    public ResponseEntity<List<OrdersDTO>> processingList( @AuthenticationPrincipal MemberDetails memberDetails) {


        String email = memberDetails.getUsername();
        List<OrdersDTO> ordersDTOPage = ordersService.processList(email);
        log.info(ordersDTOPage);

        return ResponseEntity.ok(ordersDTOPage);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<OrdersDTO>> dashboard(Principal principal){
        List<OrdersDTO> result = ordersService.dashboard(principal.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/orders/completeList")
    public ResponseEntity<List<OrdersDTO>> completeList(Principal principal) {

        String email = principal.getName();
        List<OrdersDTO> ordersDTOPage = ordersService.completeList(email);
        log.info(ordersDTOPage);
        return ResponseEntity.ok(ordersDTOPage);
    }

    @PostMapping("/orders/changeStatus")
    public ResponseEntity<String> changeStatus(Long ordersId, String ordersStatus) {
        ordersService.changeStatus(ordersId, ordersStatus);
        return ResponseEntity.ok("변경 완료");
    }

    @DeleteMapping("/rest/del")
    public void del(Long id) {
        storeService.del(id);
    }

    @PostMapping("/rest/addMember")
    public ResponseEntity<String> addMember(Long memberId, Long storeId) {

        try {
            storeService.addMembers(memberId, storeId);
            return ResponseEntity.ok("정상 추가");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/rest/membersList")
    public ResponseEntity<List<MembersDTO>> membersList(Principal principal) {
        List<MembersDTO> membersDTOList = membersService.findMembersListByHotelEmail(principal.getName());
        log.info(membersDTOList);

        return ResponseEntity.ok(membersDTOList);
    }

    @PostMapping("/rest/modify")
    public ResponseEntity<String> modify(Long memberId, Long storeId) {
        log.info("modify 진입");
        log.info("memberid" + memberId);
        log.info("memberid" + storeId);


        try {
            storeService.modiMembers(memberId, storeId);
            return ResponseEntity.ok("정상 수정");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}

package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.Service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/point")
@RequiredArgsConstructor
@Log4j2
public class PointRestController {
//
//    private final PointService pointService;
//
//    //포인트 포회
//    @GetMapping("/point")
//    public ResponseEntity<Long> getPoint(@RequestParam Long userId) {
//        log.info("[포인트 조회 요청] 사용자 ID: {}", userId);
//
//        //서비스에서 포인트 조회 요청, 해당 사용자의 보유 포인트를 db에서 읽어옴
//        Long currentPoint = pointService.getCurrentPoint(userId);
//        log.info("[포인트 조회 완료] 사용자 ID: {}, 보유 포인트: {}", userId, currentPoint);
//
//        return ResponseEntity.ok(currentPoint);
//    }
//
//
//    //결제 시 포인트 사용
//    @PostMapping("/use")
//    public ResponseEntity<String> uesPoint(@RequestParam Long usersId, @RequestParam Long paymentId,@RequestParam int usedPoint){
//
//        log.info("[포인트 사용] 사용자 ID: {}, 결제 ID: {}, 사용 포인트: {}", usersId, paymentId, usedPoint);
//        //포인트 사용 로직 실행
//        pointService.usePoint(usersId, paymentId, usedPoint);
//
//        log.info("[포인트 사용 완료] 사용자 ID: {}", usersId);
//        //성공 응답
//        return ResponseEntity.ok("포인트 사용 완료");
//    }
//
//    //결제 후 포인트 적립 (포인트 사용 안한 경우)
//    @PostMapping("/earn")
//    public ResponseEntity<String> earnPoint(String email,Long paymentId, Principal principal){
//        email = principal.getName();
//
//        //포인트 적립 로직 실행
//        pointService.earnPoint(email,paymentId);
//
//        //성공 응답
//        return ResponseEntity.ok("포인트 적립 완료");
//    }
//
//    //회원가입 시 지갑 생성
//    @PostMapping("/create-wallet")
//    public ResponseEntity<String> createWallet(@RequestParam Long usersId) {
//
//        log.info("[지갑 생성 요청] 사용자 ID: {}", usersId);
//        // 지갑 생성
//        pointService.createWallet(usersId);
//
//        log.info("[지갑 생성 완료] 사용자 ID: {}", usersId);
//        return ResponseEntity.ok("포인트 지갑 생성 완료");
//    }
//
//    //회원탈퇴 시 지갑 삭제
//    @DeleteMapping("/delete-wallet")
//    public ResponseEntity<String> deleteWallet(@RequestParam Long usersId) {
//
//        log.info("[지갑 삭제 요청] 사용자 ID: {}", usersId);
//        // 지갑 삭제
//        pointService.deleteWallet(usersId);
//
//        log.info("[지갑 삭제 완료] 사용자 ID: {}", usersId);
//        return ResponseEntity.ok("포인트 지갑 삭제 완료");
//    }




}

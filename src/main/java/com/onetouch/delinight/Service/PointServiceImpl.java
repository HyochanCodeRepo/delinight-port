package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.PointType;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{



    private final PointWalletRepository pointWalletRepository;
    private final PointLogRepository pointLogRepository;
    private final UsersRepository usersRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final OrdersRepository ordersRepository;

//
//    //현재 포인트 조회
//    @Override
//    public Long getCurrentPoint(Long usersId) {
//        log.info("[포인트 조회 시작] 사용자 ID: {}", usersId);
//
//        //사용자id로 유저를 찾고, 없으면 예외처리
//        UsersEntity user = usersRepository.findById(usersId)
//                .orElseThrow(() -> {
//                    log.error("[포인트 조회 실패] 존재하지 않는 회원 - ID: {}", usersId);
//                    return new IllegalArgumentException("존재하지 않는 회원입니다.");
//                });
//
//        //유저id로 포인트 지갑을 찾고 없으면 예외처리
//        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
//                .orElseThrow(() -> {
//                    log.error("[포인트 조회 실패] 포인트 지갑 없음 - ID: {}", usersId);
//                    return new IllegalStateException("포인트 지갑이 존재하지 않습니다.");
//                });
//
//        log.info("[포인트 조회 성공] 사용자 ID: {}, 현재 포인트: {}", usersId, wallet.getCPoint());
//
//        // 현재 포인트 반환
//        return wallet.getCPoint();
//    }
//
//
//    //포인트 사용
//    @Override
//    public void usePoint(Long usersId, Long paymentId, int usedPoint) {
//
//
//        //유저가 실제 가입된 회원인지 먼저 확인
//        UsersEntity users = usersRepository.findById(usersId)
//                .orElseThrow(()-> new IllegalArgumentException("회원이 아닙니다. 포인트 사용 불가"));
//
//        //사용자 ID로 포인트 지갑을 DB에서 찾는다. 없으면 예외를 던진다 ("지갑이 없습니다").
//        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersId)
//                .orElseThrow(()-> new ResolutionException("포인트 지갑이 없습니다"));
//
//        //사용하려는 포인트가 보유 포인트보다 많으면 예외처리
//        if (wallet.getCPoint() < usedPoint){
//
//            log.error("[포인트 사용 실패] 포인트 부족 - 보유: {}, 요청: {}", wallet.getCPoint(), usedPoint);
//            throw new ResolutionException("포인트가 부족합니다.");
//        }
//
//        //사용한 포인트만큼 지갑에서 포인트를 차감한다.
//        wallet.setCPoint(wallet.getCPoint() - usedPoint);
//
//        //변경된 지갑 상태(차감된 포인트)를 DB에 저장한다.
//        pointWalletRepository.save(wallet);
//        log.info("[포인트 차감 완료] 차감 후 포인트: {}", wallet.getCPoint());
//
//
//        //결제 정보를 조회(없으면 예외)
//        PaymentEntity payment = paymentRepository.findById(paymentId)
//                .orElseThrow(()-> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
//
//        //포인트 사용 로그 생성 및 저장 (사용 타입, 어떤 결제에 사용했는지, 몇 포인트 썼는지)
//        PointLogEntity log = PointLogEntity.builder()
//                .pointType(PointType.USE) //포인트 사용 로그
//                .payment(paymentRepository.findById(paymentId).orElseThrow()) //결제 id로 결제 정보 조회
//                .pointAmount(usedPoint) //사용한 포인트 양 기록
//                .build();
//
//        //만든 로그를 DB에 저장한다.
//        pointLogRepository.save(log);
//
//
//    }
//
//
//    //포인트 적립
//    @Override
//    public void earnPoint(String email, Long paymentId) {
//        //이메일을 기준으로 유저 정보를 데이터베이스에서 조회
//        UsersEntity user = usersRepository.findByEmail(email);
//
//        //조회된 유저가 없다면(null이면), 예외를 발생
//        if (user == null) {
//            throw new IllegalArgumentException("해당 이메일의 회원이 존재하지 않습니다.");
//        }
//
//        // 결제id로 결제 정보 조회
//        PaymentEntity payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
//
//        //결제 금액이 0원이라면 (포인트를 전부 써서 결제 했거나 결제 오류), 포인트 적립을 하지 않음
//        if (payment.getAmount() == 0) {
//            log.warn("결제 금액이 0원입니다. 포인트를 사용했거나 결제 오류로 적립이 불가합니다.");
//            return;
//        }
//
//        //적립할 포인트를 계산. 결제 금액의 10%를 적립 포인트로 정함
//        int earnPoint = (int) (payment.getAmount() * 0.1); // 결제금액 10% 적립
//        log.info("[적립 포인트 계산] 결제 금액: {}, 적립 포인트: {}", payment.getAmount(), earnPoint);
//
//        //유저id를 기준으로 해당 유저의 포인트 지갑으 조회
//        PointWalletEntity wallet = pointWalletRepository.findByUsersId(user.getId())
//                .orElseThrow(() -> new IllegalStateException("포인트 지갑이 존재하지 않습니다."));
//
//        //기존 포인트에 적립 포인트를 더함
//        wallet.setCPoint(wallet.getCPoint() + earnPoint);
//
//        //업데이트된 포인트 지갑 정보를 DB에 저장
//        pointWalletRepository.save(wallet);
//
//        //포인트 적립 로그를 생성(어떤 결제에 대해 얼마나 적립했는지를 기록)
//        PointLogEntity log = PointLogEntity.builder()
//                .pointType(PointType.EARN) // 적립 타입(EARN)을 설정
//                .payment(payment) //어떤 결제건에 대한 로그인지 설정
//                .pointAmount(earnPoint) //적립된 포인트 양 설정
//                .build();
//
//        //포인트 로그를 DB에 저장
//        pointLogRepository.save(log);
//    }
//
//
//
//
////    @Override
////    public void earnPoint(String email,Long paymentId) {
////
////        //이메일로 사용자 정보를 조회해서 usersEntity 객체로 가져옴
////        UsersEntity usersEntity =
////            usersRepository.findByEmail(email);
////
////        //엔티티를 dto로 변환
////        UsersDTO usersDTO =
////            modelMapper.map(usersEntity, UsersDTO.class);
////
////        //사용자의 id를 기준으로 총 결제 금액을 pointWalletRepository에서 조회
////        //결제 이력을 바탕으로 사용자의 총 결제 금액을 반환하는 메서드
////        Long totalPrice =
////            pointWalletRepository.selectTotalPrice(usersDTO.getId());
////
////        //총 결제 금액의 10%를 정수형으로 캐스팅하여 적립할 포인트를 계산
////        int earnPoint = (int) (totalPrice * 0.1);
////        log.info("[적립 포인트 계산] 결제 금액: {}, 적립 포인트: {}", totalPrice, earnPoint);
////
////
//////        // 결제 방식이 'PAYNOW'일 경우 선결제, 'PAYLATER'일 경우 후결제 방식 처리
//////        int earnPoint = 0;
////
////
////        // 결제 방식에 따른 포인트 적립
//////        List<OrdersEntity> orders = ordersRepository.findByCheckInEntity_UsersEntity_Id(usersDTO.getId());
//////
//////        for (OrdersEntity order : orders) {
//////            if (order.getPaymentEntity().getOrderType().equals("PAYNOW")) {
//////                // 선결제일 경우 포인트 적립 (총 결제 금액의 10%)
//////                earnPoint += (int) (order.getTotalPrice() * 0.1); // 선결제에 대해 포인트 계산
//////            } else if (order.getPaymentEntity().getOrderType().equals("PAYLATER")) {
//////                // 후결제일 경우 포인트 적립 (후결제 금액의 10% 적립)
//////                earnPoint += (int) (order.getTotalPrice() * 0.1); // 후결제에 대해 포인트 계산
//////            }
//////        }
////
////
////        //사용자의 포인트 지갑을 유저id로 조회
////        //지갑이 없을 경우 예외처리
////        PointWalletEntity wallet = pointWalletRepository.findByUsersId(usersDTO.getId())
////                .orElseThrow(()-> new ResolutionException("포인트 지갑이 없습니다."));
////
////        //계산된 적립 포인트(earnPoint)를 포인트 지갑의 현재 포인트(cPoint)로 설정
////        //기존 포인트에 더하는 방식이 아닌 덮어쓰기 방식
//////        wallet.setCPoint((long) earnPoint); //수정필요
////
////        //기존 포인트에 새로 적립할 포인트를 더함(누적 적립 방식)
////        wallet.setCPoint(wallet.getCPoint() + earnPoint);
////
////        //포인트 지갑에 해당 사용자를 다시 설정
////        wallet.setUsers(usersEntity);
////
////        //변경된 포인트 지갑 정보를 저장
////        pointWalletRepository.save(wallet);
////
////        PaymentEntity payment = paymentRepository.findById(paymentId)
////                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
////
////        PointLogEntity log = PointLogEntity.builder()
////                .pointType(PointType.EARN)
////                .payment(payment)
////                .pointAmount(earnPoint)
////                .build();
////
////        pointLogRepository.save(log);
////
////
////
////    }
//
//
//        //유저인지
////        UsersEntity users = usersRepository.findById(usersId)
////                .orElseThrow(()-> new IllegalArgumentException("회원이 아닙니다. 포인트 사용 불가"));
////
////        // 사용자 ID로 포인트 지갑을 찾는다. 없으면 예외 발생.
//
////
////        //결제 금액의 10%를 포인트로 적립할 양으로 계산한다.
////        log.info("[적립 포인트 계산] 적립 포인트: {}", earnPoint);
////
////
////        //계산된 적립 포인트만큼 지갑에 추가한다.
////        wallet.setCPoint(wallet.getCPoint() + earnPoint); //적립
////        //변경된 지갑 상태를 저장한다 (포인트 적립 반영).
////        pointWalletRepository.save(wallet);
////        log.info("[포인트 적립 완료] 적립 후 포인트: {}", wallet.getCPoint());
////
////
////        //포인트 적립 로그를 생성한다.
////        PaymentEntity payment = paymentRepository.findById(paymentId)
////                .orElseThrow(()-> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));
////
////        PointLogEntity log = PointLogEntity.builder()
////                .pointType(PointType.EARN) //포인트 적립 타입
////                .payment(paymentRepository.findById(paymentId).orElseThrow()) // 결제 정보 조회
////                .pointAmount(earnPoint) //적집 포인트 양 기록
////                .build();
////
////        //만든 로그를 DB에 저장한다.
////        pointLogRepository.save(log);
//
//
//
//
//    //회원가입 시 지갑 생성
//    @Override
//    public void createWallet(Long usersId) {
//
//        //유저 존재 여부 확인
//        UsersEntity users = usersRepository.findById(usersId)
//                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
//
//        //이미 지갑이 있다면 중복 생성 방지
//        if (pointWalletRepository.existsByUsersId(usersId)){
//            throw new IllegalArgumentException("이미 포인트 지갑이 존재합니다.");
//        }
//
//
//        //새 포인트 지갑을 만든다. 이 유저에게, 0포인트로.
//        PointWalletEntity wallet = PointWalletEntity.builder()
//                .users(users)//유저와 연결
//                .cPoint(0L) //처음엔 0포인 트
//                .build();
//
//        // 만든 지갑을 DB에 저장한다.
//        pointWalletRepository.save(wallet);
//
//    }
//
//    //회원탈퇴 시 지갑 삭제
//    @Override
//    public void deleteWallet(Long usersId) {
//
//        //유저 존재 여부 확인
//        if (!usersRepository.existsById(usersId)) {
//            throw new IllegalStateException("존재하지 않는 회원입니다. 지갑 삭제 불가.");
//        }
//        //해당 유저 ID로 포인트 지갑을 삭제한다.
//        pointWalletRepository.deleteByUsersId(usersId);
//        log.info("[지갑 삭제 완료] 사용자 ID: {}", usersId);
//
//
//    }


}

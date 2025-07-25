package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.NpsSurveyScheduler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final NpsSurveyScheduler npsSurveyScheduler;
    private final CheckOutLogRepository checkOutLogRepository;

    @GetMapping("/npsmail/{checkOutId}")
    public String sendNpsTestMail(@PathVariable Long checkOutId, Model model) {

        log.info("NPS 메일 발송 테스트 시작");
        log.info("NPS 설문 발송 완료, checkOutId : {}" + checkOutId);

        // NPS 설문 발송 서비스 호출
        try {

            // NPS 설문 이메일 발송
            npsSurveyScheduler.sendNpsSurvey();
            model.addAttribute("message", "NPS 설문 이메일이 실제 고객에게 발송되었습니다.");

        } catch (Exception e) {

            log.error("NPS 설문 발송 중 오류 발생", e);
            model.addAttribute("message", "NPS 설문 이메일 발송에 실패했습니다. 관리자에게 문의해주세요.");

        }
        // 설문 페이지로 이동
        return "users/nps/survey";

    }

    @Transactional
    @GetMapping("/survey/{checkOutId}")
    public String survey(@PathVariable Long checkOutId, Model model) {

        log.info("NPS 설문 데이터 조회, checkOutId : {}", checkOutId);

        try {
            // 체크아웃 ID로 주문 정보 조회
            List<OrdersDTO> ordersDTOList = netPromoterScoreService.npsSelect(checkOutId);

            // 주문 정보가 없으면
            if (ordersDTOList == null || ordersDTOList.isEmpty()) {
                log.info("해당 checkOutId로 주문 정보 없음");
            }

            // StoreDTO 리스트 추출 (각 주문에서 StoreDTO가 null일 수 있으므로 null 체크 추가)
            List<StoreDTO> storeDTOList = new ArrayList<>();
            for (OrdersDTO ordersDTO : ordersDTOList) {
                StoreDTO store = ordersDTO.getStoreDTO();   // 주문에서 스토어 정보 추출
                if (store != null) {                        // null인 경우에는 리스트에 추가하지 않음
                    storeDTOList.add(store);
                }
            }

            if(!ordersDTOList.isEmpty()){
                OrdersDTO firstOrder = ordersDTOList.getFirst();    // 첫 번째 주문 정보
                Long hotelId = firstOrder.getHotelId();             // 호텔 ID 추출
                String hotelName = firstOrder.getHotelName();       // 호텔 이름 추출

                // 모델에 필요한 데이터 추가
                model.addAttribute("checkOutId", checkOutId);       // 체크아웃 ID
                model.addAttribute("storeDTOList", storeDTOList);   // 스토어 리스트 (비어 있을 수 있음)
                model.addAttribute("hotelId", hotelId);             // 호텔 ID
                model.addAttribute("hotelName", hotelName);

            } else {

                CheckOutLogEntity checkOutLogEntity = checkOutLogRepository.findById(checkOutId).get();
                model.addAttribute("checkOutId", checkOutId);
                model.addAttribute("hotelId", checkOutLogEntity.getRoomEntity().getHotelEntity().getId());
                model.addAttribute("hotelName", checkOutLogEntity.getRoomEntity().getHotelEntity().getName());
                model.addAttribute("storeDTOList", null);

            }

            return "users/nps/survey";

        } catch (Exception e) {

            // 예외 발생 시 에러 로그를 남기고 에러 페이지로 리다이렉트
            log.info("NPS 설문 데이터 조회 실패", e);
            return null;

        }
    }














}

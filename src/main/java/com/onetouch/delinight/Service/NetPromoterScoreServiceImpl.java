package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Repository.*;
import com.onetouch.delinight.Util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
@EnableAsync
public class NetPromoterScoreServiceImpl implements NetPromoterScoreService {

    private final NetPromoterScoreRepository netPromoterScoreRepository;
    private final CheckOutLogRepository checkOutLogRepository;
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final StoreRepository storeRepository;
    private final MembersRepository membersRepository;



    @Override
    public List<OrdersDTO> npsSelect(Long checkOutId) {

        try {

            CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);

            // 해당 체크아웃 ID에 속한 주문 목록 조회
            List<OrdersEntity> ordersEntityList = ordersRepository.findByCheckOutLogEntity_Id(checkOutId);

            // OrdersEntity → OrdersDTO 변환 및 필요한 정보(호텔명, 호텔ID, StoreDTO) 추가 매핑
            List<OrdersDTO> ordersDTOList = ordersEntityList.stream().map(ordersEntity -> modelMapper
                            .map(ordersEntity, OrdersDTO.class)
                            .setHotelName(checkOut.getRoomEntity().getHotelEntity().getName())                      // 호텔 이름
                            .setHotelId(checkOut.getRoomEntity().getHotelEntity().getId())                          // 호텔 ID
                            .setStoreDTO(modelMapper.map(ordersEntity.getStoreEntity(),StoreDTO.class)))            // 매장
                            .toList();

            return ordersDTOList;

        }catch (Exception e) {

            throw e;

        }

    }

    @Override
    public void npsInsert(List<NetPromoterScoreDTO> npsDTOList, Long checkOutId) {

        for (NetPromoterScoreDTO netPromoterScoreDTO : npsDTOList) {
            // NetPromoterScoreEntity 빌더 패턴으로 생성
            NetPromoterScoreEntity netPromoterScoreEntity = NetPromoterScoreEntity.builder()
                    .checkOutLogEntity(checkOutLogRepository.findById(checkOutId).get())    // 체크아웃 ID
                    .questionOne(netPromoterScoreDTO.getQuestionOne())                      // 설문1
                    .questionTwo(netPromoterScoreDTO.getQuestionTwo())                      // 설문2
                    .questionThree(netPromoterScoreDTO.getQuestionThree())                  // 설문3
                    .questionFour(netPromoterScoreDTO.getQuestionFour())                    // 설문4
                    .questionFive(netPromoterScoreDTO.getQuestionFive())                    // 설문5
                    .etcQuestion(netPromoterScoreDTO.getEtcQuestion())                      // 기타 문의사항
                    .totalScore(netPromoterScoreDTO.getTotalScore())                        // 합계점수
                    .insertTime(LocalDateTime.now())                                        // 설문 응답 시간 기록
                    .build();


            // 설문 대상이 호텔인지 매장인지 구분하여 설정
            if(netPromoterScoreDTO.getHotelOrStore().equals("hotel")){
                netPromoterScoreEntity.setHotelEntity(hotelRepository.findById(netPromoterScoreDTO.getHotelId()).get());
            } else {
                netPromoterScoreEntity.setStoreEntity(storeRepository.findById(netPromoterScoreDTO.getStoreId()).get());
            }

            netPromoterScoreRepository.save(netPromoterScoreEntity);

        }
    }

    @Override
    public boolean npsOperationCheck(String email) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.withDayOfMonth(1).toLocalDate().atStartOfDay();

        List<NetPromoterScoreEntity> netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_MembersEntity_EmailAndRegTimeBetween(email, startDate, today);
        return !netPromoterScoreEntityList.isEmpty();
    }

    @Override
    public List<Integer> dailyPerformanceScore(String email) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.withDayOfMonth(1).toLocalDate().atStartOfDay();

        List<NetPromoterScoreEntity> netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_MembersEntity_EmailAndRegTimeBetween(email, startDate, today);

        if (netPromoterScoreEntityList.isEmpty()) {
            return Arrays.asList(0, 0, 0, 0, 0, 0); // 값이 없으면 0으로 채움
        }

        int totalScoreSum = 0;
        int q1Sum = 0;
        int q2Sum = 0;
        int q3Sum = 0;
        int q4Sum = 0;
        int q5Sum = 0;

        for (NetPromoterScoreEntity entity : netPromoterScoreEntityList) {
            totalScoreSum += entity.getTotalScore();
            q1Sum += entity.getQuestionOne();
            q2Sum += entity.getQuestionTwo();
            q3Sum += entity.getQuestionThree();
            q4Sum += entity.getQuestionFour();
            q5Sum += entity.getQuestionFive();
        }

        int count = netPromoterScoreEntityList.size();
        return Arrays.asList(
                totalScoreSum / count,
                q1Sum / count,
                q2Sum / count,
                q3Sum / count,
                q4Sum / count,
                q5Sum / count
        );
    }

    @Override
    public String  makePrompt(Long membersId) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.withDayOfMonth(1).toLocalDate().atStartOfDay();

        String email = membersRepository.findById(membersId).get().getEmail();
        List<NetPromoterScoreEntity> netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_MembersEntity_EmailAndRegTimeBetween(email, startDate, today);
        List<NetPromoterScoreDTO> netPromoterScoreDTOList =
                netPromoterScoreEntityList.stream().map(netPromoterScoreEntity -> modelMapper.map(netPromoterScoreEntity, NetPromoterScoreDTO.class)).toList();

        StringBuilder prompt =  new StringBuilder();
        int count = 0;
        for (NetPromoterScoreDTO netPromoterScoreDTO : netPromoterScoreDTOList){
            count++;
            if(netPromoterScoreDTO.getEtcQuestion()!=null){
                prompt.append(count).append("번 만족도 평가의 1번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionOne()).append(" , 2번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionTwo()).append(", 3번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionThree()).append(", 4번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionFour()).append(", 5번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionFive()).append(", 총 평균 점수 : ").append(netPromoterScoreDTO.getTotalScore()).append(", 개별 코멘트 : ").append(netPromoterScoreDTO.getEtcQuestion()).append(", ");

            }
            else {
                prompt.append(count).append("번 만족도 평가의 1번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionOne()).append(" , 2번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionTwo()).append(", 3번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionThree()).append(", 4번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionFour()).append(", 5번 문항 점수 : ").append(netPromoterScoreDTO.getQuestionFive()).append(", 총 평균 점수 : ").append(netPromoterScoreDTO.getTotalScore()).append(", ");

            }
        }

        return prompt.toString();
    }

    @Override
    public List<Integer> dashboard(MembersDTO membersDTO) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.withDayOfMonth(1).toLocalDate().atStartOfDay();

        LocalDateTime endDate = today.withDayOfMonth(today.toLocalDate().lengthOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999_999_999);
        List<NetPromoterScoreEntity> result;
        if(membersDTO.getRole().equals(Role.STOREADMIN)){
            result = netPromoterScoreRepository.findByStoreEntity_MembersEntity_EmailAndRegTimeBetween(membersDTO.getEmail(), startDate, endDate);
            log.info(result);
        }
        else if(membersDTO.getRole().equals(Role.SUPERADMIN)){
            result = netPromoterScoreRepository.findByHotelEntity_BranchEntity_CenterEntity_MembersEntity_EmailAndRegTimeBetween(membersDTO.getEmail(), startDate, endDate);
            log.info(result);
        }
        else {
            result = netPromoterScoreRepository.findByHotelEntity_MembersEntity_EmailAndRegTimeBetween(membersDTO.getEmail(), startDate, endDate);
            log.info("asadsa"+result);
        }

        Integer avgOne = 0;
        Integer avgTwo = 0;
        Integer avgThree = 0;
        Integer avgFour = 0;
        Integer avgFive = 0;
        Integer avgTotal = 0;
        Integer count = 0;

        for(NetPromoterScoreEntity netPromoterScoreEntity : result){
            count++;
            avgOne += netPromoterScoreEntity.getQuestionOne();
            avgTwo += netPromoterScoreEntity.getQuestionTwo();
            avgThree += netPromoterScoreEntity.getQuestionThree();
            avgFour += netPromoterScoreEntity.getQuestionFour();
            avgFive += netPromoterScoreEntity.getQuestionFive();
        }
        avgOne = avgOne/count;
        avgTwo = avgTwo/count;
        avgThree = avgThree/count;
        avgFour = avgFour/count;
        avgFive = avgFive/count;
        avgTotal = (avgOne+avgTwo+avgThree+avgFour+avgFive)/5;
        List<Integer> resultList = new ArrayList<>();
        resultList.add(avgOne);
        resultList.add(avgTwo);
        resultList.add(avgThree);
        resultList.add(avgFour);
        resultList.add(avgFive);
        resultList.add(avgTotal);
        return resultList;

    }

    @Override
    public List<NetPromoterScoreDTO> npsAll(Long memberId) {
        MembersEntity membersEntity = membersRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        List<NetPromoterScoreEntity> netPromoterScoreEntityList = new ArrayList<>();

        try{

            if (membersEntity.getRole().equals(Role.SUPERADMIN)){
                netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_HotelEntity_BranchEntity_CenterEntity_MembersEntity_IdOrHotelEntity_BranchEntity_CenterEntity_MembersEntity_Id(memberId, memberId);
            }else if(membersEntity.getRole().equals(Role.ADMIN)){
                netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_HotelEntity_MembersEntity_IdOrHotelEntity_MembersEntity_Id(memberId, memberId);
            }else if(membersEntity.getRole().equals(Role.STOREADMIN)){
                netPromoterScoreEntityList = netPromoterScoreRepository.findByStoreEntity_MembersEntity_Id(memberId);
            }else {
                log.info("알 수 없는 관리자 권한입니다.");
            }

        }catch (Exception e){
            log.info("데이터 조회 중 오류 발생 : {}", e.getMessage());
        }

        List<NetPromoterScoreDTO> netPromoterScoreDTOList = netPromoterScoreEntityList.stream().map(entity -> {
                    // 기본 DTO 변환
                    NetPromoterScoreDTO npsDTO = modelMapper.map(entity, NetPromoterScoreDTO.class);

                    // 호텔 정보 설정
                    if (entity.getHotelEntity() != null) {
                        npsDTO.setHotelOrStore("hotel");
                        npsDTO.setHotelDTO(modelMapper.map(entity.getHotelEntity(), HotelDTO.class));

                    }

                    // 스토어 정보 설정
                    if (entity.getStoreEntity() != null) {
                        npsDTO.setHotelOrStore("store");
                        npsDTO.setStoreDTO(modelMapper.map(entity.getStoreEntity(), StoreDTO.class).setHotelDTO(modelMapper.map(entity.getStoreEntity().getHotelEntity(),HotelDTO.class)));
                    }

                    return npsDTO;
                }).toList();

        // 변환된 DTO 리스트 반환
        return netPromoterScoreDTOList;
    }


}

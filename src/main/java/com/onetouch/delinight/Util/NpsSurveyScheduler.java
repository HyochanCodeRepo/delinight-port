package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PerformanceMailDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class NpsSurveyScheduler {

    private final CheckOutLogRepository checkOutLogRepository;
    private final EmailService emailService;
    private final StoreService storeService;
    private final ImageService imageService;

    // 매일 오전 9시에 실행 될 스케줄러
    @Scheduled(cron = "0 0 7 * * ?")
    public void sendNpsSurvey(){
        log.info("스케줄러 동작시작");
        // 어제 체크아웃한 고객들 찾기
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<CheckOutLogEntity> checkOutLogs = checkOutLogRepository.findByCheckoutDate(yesterday);

        if (checkOutLogs.isEmpty()) {
            log.info("어제 체크아웃한 고객이 없습니다.");
        }

            for (CheckOutLogEntity checkOutAddEmail : checkOutLogs) {
                UsersEntity usersEntity = checkOutAddEmail.getUsersEntity();
                if (usersEntity != null) {
                    String email = checkOutAddEmail.getUsersEntity().getEmail();
                    String name = checkOutAddEmail.getUsersEntity().getName();
                    Long checkOutId = checkOutAddEmail.getId();


                    String surveyLink = "http://wooriproject.iptime.org:9003/users/nps/survey/" + checkOutId;

                    emailService.sendNpsEmail(email, name, surveyLink, checkOutId);
                }
                else
                {
                }

            }

    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteOrphanImg(){
        imageService.dummyImgDelete();
    }

    @Scheduled(cron = "0 0 8  * * ?")
    public void sendDailyPerformance(){

        log.info("스케쥴러 작동 ");
        List<StoreDTO> storeDTOList = storeService.findOperationStore();
        for(StoreDTO storeDTO : storeDTOList){
            if(storeDTO.getMembersDTO()!=null){
                log.info("이게 널이라?");
                MembersDTO membersDTO = storeDTO.getMembersDTO();
                PerformanceMailDTO mailDTO = new PerformanceMailDTO();
                mailDTO.setDate(LocalDate.now());
                mailDTO.setName(membersDTO.getName());
                mailDTO.setTargetName(storeDTO.getName());
                mailDTO.setEmail(membersDTO.getEmail());
                emailService.sendDailyPerformance(mailDTO);
            }
        }


    }

}


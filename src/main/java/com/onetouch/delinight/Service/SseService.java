package com.onetouch.delinight.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class SseService {


    // 맵타입으로 스트링(스토어 아이디랑, 유저 이메일), 이미터로 JVM 힙스택에 서버 시작하면 로그인할때 컨트롤러에서 커넥트 엔드포인트 실행 하면 밀어넣고
    // 푸쉬하는 쪽 메소드에 이미터s에서 해당 이미터가 존재하면 거기에 데이터 보내는 로직을 구현 하기
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final StoreService storeService;
    private final HotelService hotelService;
    private final UsersService usersService;

    public SseEmitter connect(String response) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유효하게
        log.info("sse 연결 시도함222");

            emitters.put(response, emitter);


        // 연결 끊겼을 때 제거
        emitter.onCompletion(() -> emitters.remove(response));
        emitter.onTimeout(() -> emitters.remove(response));
        emitter.onError((e) -> emitters.remove(response));

        try {
            log.info("sse 연결 시도함");
            emitter.send(SseEmitter.event().name("connect").data(response+"nice, connected"));
        }
        catch (IOException e){
            emitters.remove(response);
        }

        return emitter;
    }

    public void sendToSAdmin(String storeId, String eventName, Object data){
        SseEmitter emitter = emitters.get(storeId);
        Long id = Long.parseLong(storeId.substring(1));
        Integer alertCount = storeService.awaitingCountCheck(id);
        if(emitter != null){
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("data", data);
                payload.put("alertCount", alertCount);
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(payload));
            }
            catch (IOException e){
                log.info("error from sendToAdmin");
                emitters.remove(storeId);
            }
        }
    }
    public void sendToHAdmin(String hotelId, String eventName, Object data){
        SseEmitter emitter = emitters.get(hotelId);
        Long id = Long.parseLong(hotelId.substring(1));
        Integer alertCount = hotelService.unansweredCheck(id);
        if(emitter != null){
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("data", data);
                payload.put("alertCount", alertCount);
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(payload));
            }
            catch (IOException e){
                log.info("error from sendToHotelAdmin");
                emitters.remove(hotelId);
            }
        }
    }
    public void sendToUsers(String usersId, String eventName, Object data){
        SseEmitter emitter = emitters.get(usersId);
        if(emitter != null){
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("data", data);
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(payload));
            }
            catch (IOException e){
                log.info("error from sendToUsers");
                emitters.remove(usersId);
            }
        }
    }


}

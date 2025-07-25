package com.onetouch.delinight.Service;

import com.onetouch.delinight.Config.OpenAiConfig;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.StoreDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class OpenAIService {
    private final OpenAiConfig openAiConfig;
    private final PaymentService paymentService;
    private final NetPromoterScoreService netPromoterScoreService;
    private final RestTemplate restTemplate = new RestTemplate();


    public String analyzeSales(String prompt){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", openAiConfig.getModel());
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = openAiConfig.getUrl();

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString();
    }

    public String makeRecommendPrompt(String prompt){
        log.info(prompt);
        String result = analyzeSales(prompt);

        return result ;
    }
    public String makePrompt(Long id, Role role, String storeName){
        String today = LocalDate.now().toString();
        String firstDay = LocalDate.now().withDayOfMonth(1).toString();

        String ordersSuffix =  "위 데이터는 주문 데이터 이며 아래 데이터는 일자별 고객 만족도 평가 데이터 입니다. ";
        String npsPrefix = "NPS의 문항 별 질문 사항 : 1번 질문 : 매장에서 구매하신 상품의 품질(신선도, 상태, 내구성 등)은 기대에 부응했나요?, 2번 질문 : 스토어 직원들의 응대 태도 및 친절함은 고객님의 쇼핑 경험에 긍정적인 영향을 주었나요?, 3번 질문 : 매장 내부의 청결 상태와 정돈 상태(진열, 바닥, 화장실 등)는 어떠했는지 평가해 주세요., 4번 질문 : 상품 가격이 품질이나 서비스 수준에 비추어 합리적이라고 느끼셨나요?, 5번 질문 : 이번 매장에서의 전반적인 쇼핑 경험에 대해 얼마나 만족하셨는지 평가해 주세요. 고객별 만족도 평가 데이터 : ";
        String npsSuffix =  "이상 데이터 전달 종료.  이 분석에 대한 chatGPT에게 보내는 요청 사항 : 위 일자별 매출 데이터, 일자별 고객 만족도 평가 트렌드를 보고 분석해서 1. 주말, 평일 별 트랜드. 2. 개선사항 솔루션, 3. 매출 개선을 위한 신규 메뉴 추천(예시 : 최근 매운 맛을 유독 좋아하는 젊은 세대들을 공략하여 매운 짬뽕(최근 매운맛을 유독 좋아하는 젊은 세대들이 늘어남으로 매운 짬뽕(네이밍 : 119짬뽕)메뉴를 추가하여 고객 선택의 다양성을 이끌어내세요)을 추천 근거와 함께 주세요. 4. 고객만족도 평가 각 항목별 점수들을 보고 개선해야 할 사항이 있는지도 주시고 고객들 요청사항들 중에 내가 놓친것도 있을 수 있으니까 심각한 사건같은게 있었으면 같이 줘. chatGPT Agent님이 주시는 이 API response는 스프링으로 전달 받아 타임리프를 통해 html에 th:utext로 렌더링 되어 자바 메일 센더로 발송할 떄 response 받는 값 그대로 삽입될 예정으로 답변에 <br>등 html 태그를 적극 활용하여 함께 답변 해주시고 (<h3>주말, 평일 별 트랜드</h3>{답변 내용}<h3 style='margin-top:10px'>주말, 평일 별 트랜드</h3> {답변 내용}) 좌측 처럼 이쁘게 렌더링 되게끔 틀을 맞춰주시고 utf-8로 인코딩될 예정으로 이모지도 삽입 가능합니다. 답변은 한글로 부탁 드립니다. 앞에 불필요한 답변 쓰지말고 바로 내가 요청한 트렌드 같은 본문에 대한 답변만 줘.. 고객한테 메일로 렌더링해서 바로 나가서 검수를 못해";

        String prompt = "아래의 데이터는 "+storeName+"의 "+firstDay+"부터 "+today+"까지 고객 만족도 평가 결과 및 주문 데이터야. 이걸 보고 분석 마지막 요청사항에 맞게 분석해줘. ";
        String paymentPrompt = paymentService.makePrompt(id, role);
        String npsPrompt = netPromoterScoreService.makePrompt(id);
        prompt += paymentPrompt+ordersSuffix+npsPrefix+npsPrompt+npsSuffix;
        return prompt;

    }
}

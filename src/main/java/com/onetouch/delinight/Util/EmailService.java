/*****************************
 * 작성자 : 이동건
 * 공용모듈
 * 필요할 때마다 사용가능한 메소드
 * ***************************/
package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.AdMailDTO;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PerformanceMailDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Repository.PaymentRepository;
import com.onetouch.delinight.Service.*;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final PaymentRepository paymentRepository;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PaymentService paymentService;
    private final MembersService membersService;
    private final OpenAIService openAIService;
    private final NetPromoterScoreService netPromoterScoreService;
    private final HotelService hotelService;
    private final CheckOutLogRepository checkOutLogRepository;

    // 초기 셋팅
    public void sendHtmlEmail(String to, String subject, String templateName, String tempPassword, Map<String, Object> variables) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            // 타임리프 context에 변수 추가 (DTO 대신 Map 사용)
            Context context = new Context();
            context.setVariables(variables); // Map 데이터 추가
            context.setVariable("message", tempPassword);

            // 템플릿 엔진을 사용하여 HTML 렌더링
            String htmlTemplate = templateEngine.process(templateName, context);

            // 이메일 설정
            helper.setFrom("linkon9277@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate, true);


            // 이메일 발송
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 중 오류 발생");
        }

    }

    @Transactional
    public void sendCheckInEmail(CheckInEntity check, String email, List<String> storeNames, String hotelName, String name) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());


            Context context = new Context();

            context.setVariable("email", email);
            context.setVariable("name", name);
            context.setVariable("checkInDate", check.getCheckinDate());
            context.setVariable("checkOutDate", check.getCheckoutDate());
            context.setVariable("hotelName", hotelName);
            helper.setSubject(check.getRoomEntity().getHotelEntity().getName() + " 체크인을 환영합니다!");

            String storeNames1 = "";
            for (String store : storeNames) {
                storeNames1 += store + ", ";
            }
            context.setVariable("storeNames", storeNames1);

            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Title</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style=\"max-width:600px; margin:0 auto; font-family:'Noto Sans', sans-serif; background-color:#ffffff; border:1px solid #ddd; padding:30px; border-radius:8px;\">\n" +
                    "  <h2 style=\"color:#2c3e50; text-align:center;\">"+check.getRoomEntity().getHotelEntity().getName()+"</h2>\n" +
                    "\n" +
                    "  <p style=\"font-size:16px; color:#333; line-height:1.6; margin-top:24px;\">\n" +
                    "    <strong>"+name+"</strong> 고객님,\n" +
                    "    <br>\n" +
                    "    저희 호텔을 찾아주셔서 진심으로 감사드리며, 체크인 및 체크아웃 일정을 아래와 같이 안내드립니다.\n" +
                    "  </p>\n" +
                    "\n" +
                    "  <div style=\"margin:20px 0; padding:16px; background-color:#f9f9f9; border-left:4px solid #3498db;\">\n" +
                    "    <p style=\"margin:0; font-size:15px; color:#2c3e50;\">\n" +
                    "      ✅ <strong>체크인</strong> : <span style=\"color:#34495e;\">"+ check.getCheckinDate()+"15:00</span><br>\n" +
                    "      ✅ <strong>체크아웃</strong> : <span style=\"color:#34495e;\">"+ check.getCheckoutDate()+ "11:00</span>\n" +
                    "    </p>\n" +
                    "  </div>\n" +
                    "\n" +
                    "  <p style=\"font-size:15px; color:#333; line-height:1.6;\">\n" +
                    "    호텔을 이용하시는 동안 다음 서비스를 편리하게 이용하실 수 있습니다:\n" +
                    "  </p>\n" +
                    "  <ul style=\"font-size:15px; color:#2c3e50; line-height:1.8; padding-left:20px;\">\n" +
                    "    <li>룸서비스 (음식 주문하기)</li>\n" +
                    "    <li>룸케어 서비스</li>\n" +
                    "    <li>호텔 문의</li>\n" +
                    "  </ul>\n" +
                    "\n" +
                    "  <p style=\"font-size:15px; color:#333;\">\n" +
                    "    아래 링크를 클릭하거나 호텔 객실에 비치된 QR코드를 스캔하시면 로그인 후 바로 이용하실 수 있습니다.\n" +
                    "    <br><br>\n" +
                    "    \uD83D\uDC49 <a href=\"http://wooriproject.iptime.org:9003/users/login\" style=\"color:#2980b9; text-decoration:none;\">호텔 서비스 로그인하기</a>\n" +
                    "  </p>\n" +
                    "\n" +
                    "  <p style=\"font-size:15px; color:#333; margin-top:24px;\">\n" +
                    "    <strong>주문 가능한 스토어 목록</strong>\n" +
                    "    <br>\n" +
                    "    <span style=\"color:#555;\">"+storeNames1+"</span>\n" +
                    "  </p>\n" +
                    "\n" +
                    "  <hr style=\"margin:30px 0; border:0; border-top:1px solid #ddd;\">\n" +
                    "\n" +
                    "  <p style=\"font-size:14px; color:#777; text-align:center;\">\n" +
                    "    "+hotelName+"을(를) 선택해주셔서 다시 한번 감사드리며,\n" +
                    "    고객님의 편안하고 즐거운 여행이 되시길 바랍니다.\n" +
                    "    <br><br>\n" +
                    "    ⓒ "+hotelName+"\n" +
                    "  </p>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo("dlwlgh908@naver.com");
            helper.setText(html, true);


            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
        }

    }

    @Transactional
    public void sendAdMail(AdMailDTO adMailDTO, String email) {

        Long hotelId = hotelService.findHotelByEmail(email);
        String hotelName = hotelService.findHotelDTOById(hotelId).getName();
        List<CheckOutLogEntity> checkOutLogEntities = checkOutLogRepository.findByRoomEntity_HotelEntity_IdAndUsersEntityIsNotNull(hotelId);
        List<String> sendTarget = new ArrayList<>();
//        for (CheckOutLogEntity checkOutLogEntity : checkOutLogEntities) {
//
//            sendTarget.add(checkOutLogEntity.getUsersEntity().getEmail());
//        }
//        for (String target : sendTarget) {
            try {

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());


                Context context = new Context();

                String title = adMailDTO.getTitle();
                String content = adMailDTO.getContent();
                String benefit = adMailDTO.getBenefit();
                String mainContent = adMailDTO.getMainContent();
                String subject = "광고 메일";

                String html = null;
                if (adMailDTO.getTempleteId() == 1) {
                    html = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title></title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div style=\"max-width:650px; margin:0 auto; font-family:'Helvetica Neue', sans-serif; background-color:#ffffff; border:1px solid #e0e0e0; border-radius:10px; overflow:hidden;\">\n" +
                            "    <!-- 헤더 이미지 -->\n" +
                            "\n" +
                            "    <!-- 콘텐츠 영역 -->\n" +
                            "    <div style=\"max-width:650px; margin:0 auto; font-family:'Apple SD Gothic Neo','Helvetica Neue',sans-serif; background-color:#ffffff; border:1px solid #ddd; border-radius:12px; overflow:hidden;\">\n" +
                            "\n" +
                            "        <!-- 제목 배경 -->\n" +
                            "        <div style=\"background-color:#f44336; color:#fff; padding:24px 32px; text-align:center;\">\n" +
                            "            <h2 style=\"margin:0; font-size:22px; line-height:1.4;\">"+title+"</h2>\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <!-- 이미지 삽입 -->\n" +
                            "        <div style=\"text-align:center;\">\n" +
                            "            <img src=\"cid:adImage\" alt=\"이벤트 이미지\" style=\"width:100%; max-height:280px; object-fit:cover;\">\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <!-- 본문 -->\n" +
                            "        <div style=\"padding:24px 32px; color:#333; background-color:#fafafa;\">\n" +
                            "            <h3 style=\"font-size:18px; margin-top:0; color:#d32f2f;\">\uD83D\uDCE3 행사 개요</h3>\n" +
                            "            <ul style=\"list-style:none; padding:0; margin:0 0 24px 0; font-size:15px; line-height:1.8;\">\n" +
                            "                <li><strong>\uD83D\uDCC5 행사 일정 :</strong> "+ adMailDTO.getPeriod()+"</li>\n" +
                            "                <li><strong>\uD83D\uDCDD 행사 내용 :</strong> "+content+"</li>\n" +
                            "                <li><strong>\uD83C\uDF81 행사 혜택 :</strong> "+benefit+"</li>\n" +
                            "            </ul>\n" +
                            "\n" +
                            "            <div style=\"border-left:4px solid #f44336; background-color:#fff3f3; padding:16px; font-size:14.5px; color:#555; line-height:1.6; margin-bottom:20px;\">\n" +
                            "                <div>"+mainContent+"</div>  <!-- 줄바꿈 포함된 HTML 렌더링 -->\n" +
                            "            </div>\n" +
                            "\n" +
                            "            <div style=\"text-align:center; margin:24px 0;\">\n" +
                            "                <a href=\"http://wooriproject.iptime.org:9003/users/login\"\n" +
                            "                   style=\"display:inline-block; background-color:#f44336; color:#fff; text-decoration:none; padding:12px 28px; border-radius:8px; font-weight:bold;\">\n" +
                            "                    이벤트 확인 및 참여하기\n" +
                            "                </a>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <!-- 푸터 -->\n" +
                            "        <div style=\"padding:16px; background-color:#eee; font-size:12.5px; color:#666; text-align:center;\">\n" +
                            "            본 메일은 "+hotelName+" 회원에게 발송되었습니다. <br>\n" +
                            "            이벤트 참여 문의는 호텔 프론트 또는 고객센터를 통해 주세요.\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>";

                } else if (adMailDTO.getTempleteId() == 2) {
                    html = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title></title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div style=\"max-width:650px; margin:0 auto; font-family:'Apple SD Gothic Neo','Helvetica Neue',sans-serif; background-color:#fff; border:1px solid #ddd; border-radius:12px; overflow:hidden;\">\n" +
                            "    <!-- 제목 영역 -->\n" +
                            "    <div style=\"background-color:#002b5b; color:#ffd700; padding:24px 32px; text-align:center;\">\n" +
                            "        <h2 style=\"margin:0; font-size:22px; line-height:1.4;\">"+title+"</h2>\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <!-- 이미지 삽입 -->\n" +
                            "    <div style=\"text-align:center;\">\n" +
                            "        <img src=\"cid:adImage\" alt=\"이벤트 이미지\" style=\"width:100%; max-height:280px; object-fit:cover;\">\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <!-- 본문 -->\n" +
                            "    <div style=\"padding:24px 32px; color:#222; background-color:#f8f8f8;\">\n" +
                            "        <h3 style=\"font-size:18px; margin-top:0; color:#002b5b;\">\uD83C\uDF89 행사 개요</h3>\n" +
                            "        <ul style=\"list-style:none; padding:0; margin:0 0 24px 0; font-size:15px; line-height:1.8;\">\n" +
                            "            <li><strong>\uD83D\uDCC5 일정 :</strong> "+ adMailDTO.getPeriod()+"</li>\n" +
                            "            <li><strong>\uD83D\uDCD8 내용 :</strong> "+content+"</li>\n" +
                            "            <li><strong>\uD83C\uDF81 혜택 :</strong> "+benefit+"</li>\n" +
                            "        </ul>\n" +
                            "\n" +
                            "        <div style=\"border-left:4px solid #002b5b; background-color:#eef3fa; padding:16px; font-size:14.5px; color:#444; line-height:1.6; margin-bottom:20px;\">\n" +
                            "            <div>"+mainContent+"</div>  <!-- 줄바꿈 포함된 HTML 렌더링 -->\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <div style=\"text-align:center; margin:24px 0;\">\n" +
                            "            <a href=\"http://wooriproject.iptime.org:9003/users/login\"\n" +
                            "               style=\"display:inline-block; background-color:#ffd700; color:#002b5b; text-decoration:none; padding:12px 28px; border-radius:8px; font-weight:bold;\">\n" +
                            "                이벤트 참여하기\n" +
                            "            </a>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <div style=\"padding:16px; background-color:#eee; font-size:12.5px; color:#666; text-align:center;\">\n" +
                            "        본 메일은 "+hotelName+" 회원에게 발송되었습니다. <br>\n" +
                            "        궁금하신 점은 호텔 프론트에 문의해 주세요.\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "\n" +
                            "</body>\n" +
                            "</html>";

                } else {
                    html = "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title></title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div style=\"max-width:650px; margin:0 auto; font-family:'Apple SD Gothic Neo','Helvetica Neue',sans-serif; background-color:#ffffff; border:1px solid #b3e5fc; border-radius:12px; overflow:hidden;\">\n" +
                            "\n" +
                            "    <div style=\"background-color:#03a9f4; color:#ffffff; padding:24px 32px; text-align:center;\">\n" +
                            "        <h2 style=\"margin:0; font-size:22px;\">"+title+"</h2>\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <div style=\"text-align:center;\">\n" +
                            "        <img src=\"cid:adImage\" alt=\"이벤트 이미지\" style=\"width:100%; max-height:280px; object-fit:cover;\">\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <div style=\"padding:24px 32px; background-color:#e1f5fe; color:#01579b;\">\n" +
                            "        <h3 style=\"font-size:18px; margin-top:0;\">\uD83C\uDFD6 행사 개요</h3>\n" +
                            "        <ul style=\"list-style:none; padding:0; margin:0 0 24px 0; font-size:15px; line-height:1.8;\">\n" +
                            "            <li><strong>\uD83D\uDCC5 일정 :</strong> "+ adMailDTO.getPeriod()+"</li>\n" +
                            "            <li><strong>\uD83D\uDCDD 내용 :</strong> "+content+"</li>\n" +
                            "            <li><strong>\uD83C\uDF81 혜택 :</strong> "+benefit+"</li>\n" +
                            "        </ul>\n" +
                            "\n" +
                            "        <div style=\"border-left:4px solid #0288d1; background-color:#ffffff; padding:16px; font-size:14.5px; color:#333; line-height:1.6; margin-bottom:20px;\">\n" +
                            "            <div>"+mainContent+"</div>  <!-- 줄바꿈 포함된 HTML 렌더링 -->\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <div style=\"text-align:center; margin:24px 0;\">\n" +
                            "            <a href=\"http://wooriproject.iptime.org:9003/users/login\"\n" +
                            "               style=\"display:inline-block; background-color:#0288d1; color:#ffffff; text-decoration:none; padding:12px 28px; border-radius:8px; font-weight:bold;\">\n" +
                            "                참여하러 가기\n" +
                            "            </a>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "\n" +
                            "    <div style=\"padding:16px; background-color:#cfd8dc; font-size:12.5px; color:#555; text-align:center;\">\n" +
                            "        본 메일은 "+hotelName+" 회원에게 발송되었습니다.\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "\n" +
                            "</body>\n" +
                            "</html>";

                }

                helper.setFrom("linkon9277@gmail.com");
                helper.setTo("dlwlgh908@naver.com");
                MultipartFile image = adMailDTO.getImage();
                    String contentType = image.getContentType(); // 예: image/png
                    String imageName = image.getOriginalFilename(); // 예: event_banner.png
                    InputStreamSource imageSource = new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return image.getInputStream();
                        }
                    };
                    DataSource dataSource = new ByteArrayDataSource(image.getBytes(), contentType);
                    helper.addInline("adImage", imageSource, contentType);

                helper.setText(html, true);


                javaMailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//        }
    }

    @Async
    public CompletableFuture<Object> sendNpsEmail(String email, String name, String surveyLink, Long checkOutId) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("name", name);
            context.setVariable("surveyLink", surveyLink);
            context.setVariable("checkOutId", checkOutId);

            String html = templateEngine.process("users/nps/npsmail", context);

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo(email);
            helper.setSubject("NPS 설문 참여 요청");
            helper.setText(html, true);

            javaMailSender.send(message);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생 - email: {}, name: {}, link: {}", email, name, surveyLink, e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e);
        }

    }


    @Transactional
    public void sendDailyPerformance(PerformanceMailDTO mailDTO) {
        MembersDTO membersDTO = membersService.findByEmail(mailDTO.getEmail());

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());


            Context context = new Context();

            context.setVariable("email", mailDTO.getEmail());
            context.setVariable("name", mailDTO.getName());
            context.setVariable("date", mailDTO.getDate());
            context.setVariable("targetName", mailDTO.getTargetName());
            String totalPrice = paymentService.dailyPerformancePrice(mailDTO.getEmail());
            context.setVariable("totalPrice", totalPrice);
            if (netPromoterScoreService.npsOperationCheck(mailDTO.getEmail())) {
                List<Integer> npsScore = netPromoterScoreService.dailyPerformanceScore(mailDTO.getEmail());
                context.setVariable("totalScore", npsScore.get(0));
                context.setVariable("firstScore", npsScore.get(1));
                context.setVariable("secondScore", npsScore.get(2));
                context.setVariable("thirdScore", npsScore.get(3));
                context.setVariable("fourthScore", npsScore.get(4));
                context.setVariable("fifthScore", npsScore.get(5));
            }
            log.info(mailDTO);
            helper.addAttachment(mailDTO.getDate().toString() + "_일일매출현황.xlsx", new ByteArrayResource(paymentService.extractDailyExcel(membersDTO.getId(), membersDTO.getRole())));
            context.setVariable("aiResponse", openAIService.analyzeSales(openAIService.makePrompt(membersDTO.getId(), membersDTO.getRole(), mailDTO.getTargetName())));


            String html = templateEngine.process("/members/payment/dailyPerformance", context);

            helper.setFrom("linkon9277@gmail.com");
            helper.setTo("dlwlgh908@naver.com");
            helper.setSubject(mailDTO.getTargetName() + "일일 매출 현황");
            helper.setText(html, true);

            FileSystemResource result = new FileSystemResource(new File("src/main/resources/static/img/AI.png"));
            helper.addInline("aiImage", result);


            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("이메일 전송 중 오류 발생 - email: {}, name: {}", mailDTO.getEmail(), mailDTO.getName(), e);
            throw new RuntimeException("이메일 전송 중 오류 발생", e); // 원래 예외를 함께 던짐
        } catch (IOException e) {
            log.info(e.getMessage() + mailDTO);
            throw new RuntimeException(e);
        }

    }


}

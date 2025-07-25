/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.CustomPaymentRepositoryImpl;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.OrdersRepository;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CustomPaymentRepositoryImpl customPaymentRepository;
    private final PaymentRepository paymentRepository;
    private final MembersRepository membersRepository;
    private final ModelMapper modelMapper;
    private final OrdersRepository ordersRepository;


    @Override
    public byte[] extractMonthlyExcel(Long id, Role role) throws IOException {

        //비교할 전달 첫날, 마지막날 추출
        LocalDate prevMonthStartdate = LocalDate.now().minusMonths(2).withDayOfMonth(1);
        LocalDate prevMonthEnddate = LocalDate.now().minusMonths(1).withDayOfMonth(1).minusDays(1);

        //마감한 달 첫날, 마지막날 추출
        LocalDate MonthStartdate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate MonthEnddate = LocalDate.now().minusDays(1);

        if (role.equals(Role.SUPERADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, prevMonthEnddate, MonthEnddate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.SUPERADMIN);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_center.xlsx");

            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : processedExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }
        } else if (role.equals(Role.ADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, prevMonthEnddate, MonthEnddate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.ADMIN);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_hotel.xlsx");
            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : processedExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }


        } else {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, prevMonthEnddate, MonthEnddate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_store.xlsx");
            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : rawExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }
        }
    }

    @Override
    public byte[] extractDailyExcel(Long id, Role role) throws IOException {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = yesterday.withDayOfMonth(1);


        if (role.equals(Role.SUPERADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, yesterday);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.SUPERADMIN);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_center.xlsx");

            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : processedExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }
        } else if (role.equals(Role.ADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, yesterday);
            log.info(beforeProcessData);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.ADMIN);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_hotel.xlsx");
            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : processedExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                log.info(beforeProcessData);
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }

        } else {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, yesterday);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            ClassPathResource templateFile = new ClassPathResource("templates/payment_data_store.xlsx");
            try (InputStream inp = templateFile.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inp);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIdx = 1;
                for (ExcelDTO row : rawExcelData) {
                    Row rowOne = sheet.createRow(rowIdx++);
                    rowOne.createCell(0).setCellValue(row.getDate());
                    rowOne.createCell(1).setCellValue(row.getMenuName());
                    rowOne.createCell(2).setCellValue(row.getUnitPrice());
                    rowOne.createCell(3).setCellValue(row.getQuantity());
                    rowOne.createCell(4).setCellValue(row.getTotalPrice());
                }
                workbook.write(out);
                byte[] excelFile = out.toByteArray();
                return excelFile;


            }


        }


    }

    @Override
    public String makePrompt(Long id, Role role) {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = yesterday.withDayOfMonth(1);
        LocalDate endDate = yesterday;

        StringBuilder prompt = new StringBuilder();
        prompt.append("일자별 매출 ");


        if (role.equals(Role.SUPERADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, endDate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.SUPERADMIN);
            for (ExcelDTO data : processedExcelData) {
                prompt.append(data.getDate()).append("일 호텔명 : ").append(data.getHotelName()).append(", 총합 가격 : ").append(data.getTotalPrice()).append("원 ");
            }
            prompt.append("입니다. 호텔별 이번달 룸서비스(음식 주문 서비스) 일자별 매출 데이터로 호텔별 매출 트렌드, 일자별 매출 트렌드를 보고 분석하여 1. 주말, 평일 별 트렌드, 2. 개선사항 솔루션, 3. 매출 개선을 위해 추천하는 가맹점 계약 추천(예: 최근 트렌드 음식인 마라탕 등 젊은 이의 입맛을 사로잡는 중화요리집과의 계약 추천)을 근거와 함께 주세요. 해당 답변은 th:utext를 통해 메일 내 자동 삽입될 예정으로 <br>등 html 태그를 함께 답변 해주시고 utf-8로 인코딩될 예정으로 이모지도 삽입 가능합니다. gpt agent님님 답변은 꼭 한글로 주세요. ");
        } else if (role.equals(Role.ADMIN)) {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, endDate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            List<ExcelDTO> processedExcelData = groupExcelDataBy(rawExcelData, Role.ADMIN);
            for (ExcelDTO data : processedExcelData) {
                prompt.append(data.getDate()).append("일 가맹점 명 : ").append(data.getStoreName()).append(" 총합 판매액 : ").append(data.getTotalPrice()).append("원 ");
            }
            prompt.append("입니다. 호텔의 이번달 룸서비스(음식 주문 서비스) 일자별 매출 데이터로 스토어별 매출 트렌드, 일자별 매출 트렌드를 보고 분석하여 1. 주말, 평일 별 트렌드, 2. 개선사항 솔루션, 3. 매출 개선을 위해 스토어별 기존 메뉴들과 연관성(스토어 이름들을 보고 스토어 주력 메뉴들을 유추하여) 메뉴 추천(예: 최근 매운맛을 유독 좋아하는 젊은 세대들이 늘어남으로 매운 짬뽕(네이밍 : 119짬뽕)메뉴를 추가하여 고객 선택의 다양성을 이끌어내세요)을 근거와 함께 주세요. 해당 답변은 th:utext를 통해 메일 내 자동 삽입될 예정으로 <br>등 html 태그를 함께 답변 해주시고 utf-8로 인코딩될 예정으로 이모지도 삽입 가능합니다. gpt agent님님 답변은 꼭 한글로 주세요. ");

        } else {
            List<PaymentDTO> beforeProcessData = paymentByCriteria(PaidCheck.both, id, startDate, endDate);
            List<ExcelDTO> rawExcelData = extractData(beforeProcessData);
            for (ExcelDTO data : rawExcelData) {
                prompt.append(data.getDate()).append("일 메뉴 : ").append(data.getMenuName()).append(", ").append(data.getQuantity()).append(" 개 판매, 개별 가격 : ").append(data.getUnitPrice()).append("원, 총합 판매액 : ").append(data.getTotalPrice()).append("원 ");
            }
            prompt.append("입니다.");
        }

        return prompt.toString();
    }

    @Override
    public List<OrdersDTO> readOrders(Long paymentId) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream()
                .map(data -> modelMapper.map(data, OrdersDTO.class)
                        .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))
                        .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }

    @Override
    public List<PaymentDTO> paymentByCriteria(PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate
            endDate) {

        Role role = membersRepository.findById(memberId).get().getRole();

        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        if (role.equals(Role.SYSTEMADMIN)) {
            // todo : 추 후에 플랫폼에 속해있는 총 각 센터 매출 및 하위 매출 순정보
        } else if (role.equals(Role.SUPERADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.ADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else if (role.equals(Role.STOREADMIN)) {
            paymentDTOList = customPaymentRepository.findPaymentByCriteria(paidCheck, memberId, startDate, endDate);
        } else {
            throw new IllegalArgumentException("잘못된 권한입니다.");
        }

        return processPayments(paymentDTOList); // 계산된 데이터 반환
    }


    @Override
    public String dailyPerformancePrice(String email) {
        LocalDateTime yesterDay = LocalDate.now().minusDays(1).atStartOfDay();

        List<OrdersEntity> ordersEntityList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndPendingTimeIsAfter(email, yesterDay);
        if (ordersEntityList.isEmpty()) {
            return "0";
        }

        double sum = 0;
        for (OrdersEntity order : ordersEntityList) {
            sum += order.getTotalPrice();  // totalPrice는 숫자형 필드라고 가정
        }

        double average = sum / ordersEntityList.size();
        return String.format("%.0f", average);


    }

    @Override
    public List<ExcelDTO> extractData(List<PaymentDTO> paymentDTOList) {

        List<ExcelDTO> result = new ArrayList<>();


        for (PaymentDTO paymentDTO : paymentDTOList) {
            LocalDate date = paymentDTO.getRegTime().toLocalDate();
            for (OrdersDTO ordersDTO : paymentDTO.getOrdersDTOList()) {
                for (OrdersItemDTO ordersItemDTO : ordersDTO.getOrdersItemDTOList()) {
                    MenuDTO menuDTO = ordersItemDTO.getMenuDTO();
                    ExcelDTO row = new ExcelDTO();
                    row.setDate(date);
                    row.setQuantity(ordersItemDTO.getQuantity().intValue());
                    row.setUnitPrice(menuDTO.getPrice());
                    row.setMenuName(menuDTO.getName());
                    row.setTotalPrice(menuDTO.getPrice() * ordersItemDTO.getQuantity().intValue());
                    row.setHotelName(ordersDTO.getStoreDTO().getHotelDTO().getName());
                    row.setStoreName(ordersDTO.getStoreDTO().getName());
                    result.add(row);
                }
            }
        }
        return result;
    }

    @Override
    public List<PaymentDTO> processPayments(List<PaymentDTO> paymentDTOList) {

        // AtomicInteger 사용하여 최종 금액 합산 값을 변경할 수 있게 처리 (멀티스레드 환경에서 안전)
        AtomicInteger TotalPriceVat = new AtomicInteger(0);  // 최종 금액 합산 (전체 합계금액 + 부가세 합산)

        // 리스트 내 각 PaymentDTO 처리
        return paymentDTOList.stream().map(data -> {

            int totalPrice = 0;   // 합계 금액 (주문 금액 합)
            int unpaid = 0;       // 미결제 금액 (결제가 안 된 주문 금액)
            int vat = 0;          // 부가세 (각 주문의 부가세 합)

            // 각 PaymentDTO에 포함된 주문들에 대해 계산 처리
            for (OrdersDTO order : data.getOrdersDTOList()) {

                Long orderPrice = order.getTotalPrice();    // 주문 금액
                int orderVat = (int) (orderPrice * 0.1);    // 부가세 10% 계산

                // 결제 상태에 따라 미결제 금액과 부가세 계산
                if (!order.isPaid()) {                      // 미결제 상태인 경우
                    unpaid += orderPrice;                   // 미결제 금액 누적
                    vat += orderVat;                        // 부가세 누적
                    totalPrice += orderPrice + orderVat;    // 미결제 금액과 부가세를 합산한 금액
                } else {                                    // 결제 완료된 주문은 금액만 누적
                    totalPrice += orderPrice;               // 결제 완료된 주문의 합계금액만 누적
                }
            }

            // 각 PaymentDTO에 최종 계산된 값 설정
            data.setTotalPrice(totalPrice);                 // 합계 금액 설정
            data.setUnpaid(unpaid);                         // 미결제 금액 설정
            data.setVat(vat);                               // 부가세 설정

            // 각 PaymentDTO에 대해 최종 합계에 부가세 + 합계금액을 더하여 전체 합계에 반영
            int finalTotal = totalPrice + vat;              // 최종 금액 계산 (합계금액 + 부가세)
            TotalPriceVat.addAndGet(finalTotal);            // 전체 총합에 최종 금액 누적

            // 처리된 PaymentDTO 반환
            return data;
        }).collect(Collectors.toList());  // 결과 리스트 반환
    }

    @Override
    public List<ExcelDTO> groupExcelDataBy(List<ExcelDTO> data, Role role) {
        Map<String, ExcelDTO> groupedMap = new LinkedHashMap<>();

        for (ExcelDTO dto : data) {
            String key;

            if (role.equals(Role.SUPERADMIN)) {
                key = dto.getDate() + "|" + dto.getHotelName();
            } else if (role.equals(Role.ADMIN)) {
                key = dto.getDate() + "|" + dto.getStoreName();
            } else {
                return data; // STOREADMIN은 그룹핑 없이 그대로 사용
            }

            if (groupedMap.containsKey(key)) {
                ExcelDTO existing = groupedMap.get(key);
                existing.setTotalPrice(existing.getTotalPrice() + dto.getTotalPrice());
            } else {
                ExcelDTO copy = new ExcelDTO();
                copy.setDate(dto.getDate());
                copy.setHotelName(dto.getHotelName());
                copy.setStoreName(dto.getStoreName());
                copy.setTotalPrice(dto.getTotalPrice());
                groupedMap.put(key, copy);
            }
        }

        return new ArrayList<>(groupedMap.values());
    }


}

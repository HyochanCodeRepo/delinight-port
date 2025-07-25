package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.ExcelDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.PaymentDTO;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.PaymentService;
import com.onetouch.delinight.Util.ExcelUtil;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;
    private final MembersService membersService;

    @PostMapping("/process")
    public List<PaymentDTO> processPayments(@RequestBody List<PaymentDTO> paymentDTOList) {
        // 1. 서비스 호출 후 결제처리 된 결제 내역처리
        List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
        // 2. 처리된 결제 내역 반환
        return processedPayment;
    }

    @GetMapping("/totalPrice")
    public ResponseEntity<?> paymentTotalPrice(
            @RequestParam(value = "paidCheck") PaidCheck paidCheck,
            @RequestParam(value = "startDate") String startDate1,
            @RequestParam(value = "endDate") String endDate1,
            @RequestParam(value = "download", required = false, defaultValue = "false") Boolean download,
            Principal principal, @AuthenticationPrincipal MemberDetails memberDetails) {
log.info(memberDetails.getAuthorities());

        log.info(startDate1 + " " + endDate1);
        LocalDate startDate = LocalDate.parse(startDate1);
        LocalDate endDate = LocalDate.parse(endDate1);
        log.info(startDate + " " + endDate);


        try {
            log.info(startDate);
            log.info(endDate);
            // 1. 현재 로그인한 사용자 정보 가져오기
            String admin = principal.getName();
            // 2. 이메일로 회원 정보 조회
            MembersDTO membersDTO = membersService.findByEmail(admin);
            Long memberId = membersDTO.getId();
            log.info("회원 정보 : {}", membersDTO);
            // 3. 서비스 호출 : 가격 월, 타입, 매장 ID, 결제 상태로 필터링된 결제 정보 조회
            List<PaymentDTO> paymentDTOList = paymentService.paymentByCriteria(paidCheck, memberId, startDate, endDate);
            // 4. 조회된 데이터가 없다면
            if (paymentDTOList.isEmpty()) {
                log.info("조회된 데이터가 없습니다.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // 후처리 된 결제 내역
            List<PaymentDTO> processedPayment = paymentService.processPayments(paymentDTOList);
            log.info("결제 내역 후처리 완료");
            // 6. 다운로드 요청일 경우 엑셀 파일로 반환
            if (download) {
                Role role = membersService.findRoleByEmail(memberDetails.getUsername()).keySet().iterator().next();
                if(role.equals(Role.SUPERADMIN)){
                    log.info("본사 관리자 엑셀 다운로드 시작 ");
                    List<ExcelDTO> excelDTOList = paymentService.extractData(processedPayment);
                    ClassPathResource templateFile = new ClassPathResource("templates/payment_data_center.xlsx");
                    try (InputStream inp = templateFile.getInputStream();
                         Workbook workbook = new XSSFWorkbook(inp);
                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        Sheet sheet = workbook.getSheetAt(0);
                        List<ExcelDTO> groupedExcelDTOList = paymentService.groupExcelDataBy(excelDTOList,Role.SUPERADMIN);
                        int rowIdx = 1;
                        for (ExcelDTO row : groupedExcelDTOList) {
                            Row rowOne = sheet.createRow(rowIdx++);
                            rowOne.createCell(0).setCellValue(row.getDate());
                            rowOne.createCell(1).setCellValue(row.getHotelName());
                            rowOne.createCell(2).setCellValue(row.getTotalPrice());
                        }
                        workbook.write(out);


                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Disposition", "attachment; filename=payment_data.xlsx");
                        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(out.toByteArray());
                    }


                }
                else if (role.equals(Role.ADMIN)){
                    log.info("호텔 관리자 엑셀 다운로드 시작 ");
                    List<ExcelDTO> excelDTOList = paymentService.extractData(processedPayment);
                    ClassPathResource templateFile = new ClassPathResource("templates/payment_data_hotel.xlsx");
                    try (InputStream inp = templateFile.getInputStream();
                         Workbook workbook = new XSSFWorkbook(inp);
                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        Sheet sheet = workbook.getSheetAt(0);
                        List<ExcelDTO> groupedExcelDTOList = paymentService.groupExcelDataBy(excelDTOList,Role.ADMIN);
                        int rowIdx = 1;
                        for (ExcelDTO row : groupedExcelDTOList) {
                            Row rowOne = sheet.createRow(rowIdx++);
                            rowOne.createCell(0).setCellValue(row.getDate());
                            rowOne.createCell(1).setCellValue(row.getStoreName());
                            rowOne.createCell(2).setCellValue(row.getTotalPrice());
                        }
                        workbook.write(out);


                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Disposition", "attachment; filename=payment_data.xlsx");
                        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(out.toByteArray());
                    }


                }
                else if(role.equals(Role.STOREADMIN)){

                    log.info("스토어 관리자 엑셀 다운로드 시작 ");
                    List<ExcelDTO> excelDTOList = paymentService.extractData(processedPayment);
                    ClassPathResource templateFile = new ClassPathResource("templates/payment_data_store.xlsx");
                    try (InputStream inp = templateFile.getInputStream();
                         Workbook workbook = new XSSFWorkbook(inp);
                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        Sheet sheet = workbook.getSheetAt(0);
                        int rowIdx = 1;
                        List<ExcelDTO> groupedExcelDTOList = paymentService.groupExcelDataBy(excelDTOList,Role.STOREADMIN);
                        for (ExcelDTO row : groupedExcelDTOList) {
                            Row rowOne = sheet.createRow(rowIdx++);
                            rowOne.createCell(0).setCellValue(row.getDate());
                            rowOne.createCell(1).setCellValue(row.getMenuName());
                            rowOne.createCell(2).setCellValue(row.getUnitPrice());
                            rowOne.createCell(3).setCellValue(row.getQuantity());
                            rowOne.createCell(4).setCellValue(row.getTotalPrice());
                        }
                        workbook.write(out);


                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Disposition", "attachment; filename=payment_data.xlsx");
                        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(out.toByteArray());
                    }

                }

            }
            // 후처리 된 결제 내역 반환
            return new ResponseEntity<>(processedPayment, HttpStatus.OK);
        } catch (Exception e) {
            log.info("결제 내역 조회 중 오류 발생"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

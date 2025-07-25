package com.onetouch.delinight.Util;

import com.onetouch.delinight.DTO.PaymentDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
//
//
//    public static ByteArrayInputStream ExcelToStream(List<PaymentDTO> list) {
//
//        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            Sheet sheet = workbook.createSheet("결제내역");
//
//            // 헤더 생성
//            Row headerRow = sheet.createRow(0);
//            String[] headers = {"결제 ID", "정산타입", "정산상태", "등록일", "수정일", "금액", "부가세", "미결제 금액", "합계 금액"};
//            for (int i = 0; i < headers.length; i++) {
//                headerRow.createCell(i).setCellValue(headers[i]);
//            }
//
//            // 데이터 입력
//            int rowIdx = 1;
//            for (PaymentDTO dto : list) {
//                Row row = sheet.createRow(rowIdx++);
//                row.createCell(0).setCellValue(dto.getTotalId());
//                row.createCell(1).setCellValue(dto.getCheckPaid().toString());
//                row.createCell(2).setCellValue(dto.getRegTime());
//                row.createCell(4).setCellValue(dto.getTotalPrice());                // 합계 금액
//                row.createCell(5).setCellValue(dto.getVat());                       // 부가세
//                row.createCell(6).setCellValue(dto.getUnpaid());                    // 미결제 금액
//                row.createCell(7).setCellValue(dto.getTotalPrice());                // 최종 합계 금액 (가격 + 미결제 금액 + 부가세)
//
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//
//        } catch (IOException e) {
//            throw new RuntimeException("엑셀 파일 생성 실패", e);
//        }
//    }


}

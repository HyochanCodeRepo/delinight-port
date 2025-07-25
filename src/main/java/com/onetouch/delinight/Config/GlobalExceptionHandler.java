//package com.onetouch.delinight.Config;
//
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.nio.file.AccessDeniedException;
//
//@ControllerAdvice
//@Component
//public class GlobalExceptionHandler {
//
//
//    // 400 Bad Request
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("잘못된 요청: " + e.getMessage());
//    }
//
//    // 401 Unauthorized
//    @ExceptionHandler(SecurityException.class)
//    public ResponseEntity<String> handleUnauthorized(SecurityException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body("인증이 필요합니다.");
//    }
//
//    // 403 Forbidden
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleForbidden(AccessDeniedException e) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body("접근 권한이 없습니다.");
//    }
//
//    // 500 Internal Server Error
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("서버 내부 오류 발생: " + e.getMessage());
//    }
//
//}

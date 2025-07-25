package com.onetouch.delinight.Controller.UtilController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) throws IOException {
        log.error("Exception 발생 : ", ex);
        if (ex.getMessage().equals("멤버 로그인 필요")) {
            response.sendRedirect("/members/account/login");
        } else if (ex.getMessage().equals("유저 로그인 필요")) {
            response.sendRedirect("/users/login");
        }
        else if (ex.getMessage().equals("체크인 없는 사람")) {
            response.sendRedirect("/users/login?error=notCheckedIn");
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException e) {
        log.error("잘못된 요청 발생 : ", e);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/400"); // static 폴더 내 error/400.html을 보여줌
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleUnauthorized(SecurityException e) {
        log.error("인증이 필요합니다 : ", e);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/401");
        mav.setStatus(HttpStatus.UNAUTHORIZED);
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbidden(AccessDeniedException e) {
        log.error("접근 권한이 없습니다 : ", e);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/403");
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }


}

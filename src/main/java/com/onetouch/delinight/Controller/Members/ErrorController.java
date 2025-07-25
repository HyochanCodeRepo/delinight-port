package com.onetouch.delinight.Controller.Members;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {


    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");


        if (statusCode == 400) {
            return "error/400";
        } else if (statusCode == 401) {
            return "error/401";
        } else if (statusCode == 403) {
            return "error/403";
        } else if (statusCode == 404) {
            return "error/404";
        } else if (statusCode == 405) {
            return "error/405";
        } else if (statusCode == 500) {
            return "error/500";
        }
        return "error/default";
    }
}

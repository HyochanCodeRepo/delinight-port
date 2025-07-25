package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.BrandDTO;
import com.onetouch.delinight.Service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand/rest")
@Log4j2
public class BrandRestController {
    private final BrandService brandService;


    @PostMapping("/register")
    public ResponseEntity<String> create(BrandDTO brandDTO) {
        log.info(brandDTO);
        brandService.create(brandDTO);

        return ResponseEntity.ok("성공");
    }

    @PostMapping("/del")
    public ResponseEntity<String> del(@RequestParam("num") Long num) {
        log.info("controller에 들어오는것"+num);
        brandService.del(num);
        return ResponseEntity.ok("성공");
    }
}

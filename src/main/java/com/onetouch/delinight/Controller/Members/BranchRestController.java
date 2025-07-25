package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch/rest")
@Log4j2
public class BranchRestController {
    private final BranchService branchService;

    @DeleteMapping("/del")
    public void del(Long id) {
        log.info("들어온 id : " + id);

        branchService.del(id);

    }
}

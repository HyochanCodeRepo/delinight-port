package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.Service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Log4j2
public class UsersRestController {

    private final UsersService usersService;


}

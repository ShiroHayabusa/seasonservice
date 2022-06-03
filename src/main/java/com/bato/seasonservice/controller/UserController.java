package com.bato.seasonservice.controller;

import com.bato.seasonservice.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


}
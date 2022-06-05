package com.bato.seasonservice.controller;

import com.bato.seasonservice.config.Jwt.JwtProvider;
import com.bato.seasonservice.model.AuthRequest;
import com.bato.seasonservice.model.AuthResponse;
import com.bato.seasonservice.model.RegistrationRequest;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = new User();
        user.setLogin(registrationRequest.getLogin());
        user.setPassword(registrationRequest.getPassword());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPatronymic(registrationRequest.getPatronymic());
        user.setEmail(registrationRequest.getEmail());
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }
}

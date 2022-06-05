package com.bato.seasonservice.controller;

import com.bato.seasonservice.config.Jwt.JwtProvider;
import com.bato.seasonservice.model.AuthRequest;
import com.bato.seasonservice.model.AuthResponse;
import com.bato.seasonservice.model.RegistrationRequest;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.repo.RoleRepo;
import com.bato.seasonservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final RoleRepo roleRepo;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthController(RoleRepo roleRepo, UserService userService, JwtProvider jwtProvider) {
        this.roleRepo = roleRepo;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрация пользователя, все поля обязательны!"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = new User();
        user.setLogin(registrationRequest.getLogin());
        user.setPassword(registrationRequest.getPassword());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPatronymic(registrationRequest.getPatronymic());
        user.setEmail(registrationRequest.getEmail());
        user.setRole(roleRepo.findByName("ROLE_USER"));
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Авторизация пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),
    })
    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByLogin(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

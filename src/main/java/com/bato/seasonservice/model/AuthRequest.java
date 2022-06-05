package com.bato.seasonservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequest {
    @Schema(description = "Имя пользователя", example = "user")
    private String login;
    @Schema(description = "Пароль", example = "password")
    private String password;
}

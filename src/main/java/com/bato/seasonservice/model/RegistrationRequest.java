package com.bato.seasonservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {
    @NotEmpty
    @Schema(description = "Имя пользователя", example = "user")
    private String login;
    @NotEmpty
    @Schema(description = "Пароль", example = "password")
    private String password;
    @NotEmpty
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @NotEmpty
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
    @NotEmpty
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;
    @NotEmpty
    @Schema(description = "Почта", example = "mail@mail.ru")
    private String email;
}

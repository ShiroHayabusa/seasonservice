package com.bato.seasonservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Data

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "user")
    private String login;
    @Schema(description = "Пароль", example = "password")
    private String password;
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;
    @Schema(description = "Почта", example = "mail@mail.ru")
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    @Schema(description = "Роль", example = "ROLE_USER")
    private Role role;
}

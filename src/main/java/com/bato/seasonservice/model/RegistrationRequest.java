package com.bato.seasonservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String patronymic;
    @NotEmpty
    private String email;
}
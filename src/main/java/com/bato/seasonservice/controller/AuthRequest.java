package com.bato.seasonservice.controller;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
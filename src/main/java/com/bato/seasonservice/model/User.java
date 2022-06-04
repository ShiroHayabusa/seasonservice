package com.bato.seasonservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "usr")
@Data

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

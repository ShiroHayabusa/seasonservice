package com.bato.seasonservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "servrequests")
@Data
public class ServRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "serv_id")
    private Serv serv;
}

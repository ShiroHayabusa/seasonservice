package com.bato.seasonservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "servs")
@Data
public class Serv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer servLimit;
}

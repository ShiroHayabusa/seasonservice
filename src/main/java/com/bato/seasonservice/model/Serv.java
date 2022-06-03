package com.bato.seasonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "servs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Serv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    private Integer servLimit;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

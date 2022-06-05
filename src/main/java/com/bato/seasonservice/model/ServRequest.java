package com.bato.seasonservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "servrequests")
@Data
public class ServRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "1")
    private Long id;
    @Schema(description = "Дата подачи заявки", example = "06.06.2022 17:00")
    private String date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "serv_id")
    private Serv serv;
}

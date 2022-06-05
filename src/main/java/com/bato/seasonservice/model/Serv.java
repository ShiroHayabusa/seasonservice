package com.bato.seasonservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "servs")
@Data
public class Serv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "1")
    private Long id;
    @Schema(description = "Название услуги", example = "Выдача охотничьего билета")
    private String name;
    @Schema(description = "Лимит услуги", example = "500")
    private Integer servLimit;
}

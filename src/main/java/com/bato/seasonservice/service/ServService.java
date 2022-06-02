package com.bato.seasonservice.service;

import com.bato.seasonservice.model.Serv;

import java.util.List;

public interface ServService {
    Serv getById(Long id);

    void save(Serv serv);

    void delete(Long id);

    List<Serv> getAll();
}

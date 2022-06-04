package com.bato.seasonservice.service;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.repo.ServRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServService {
    private final
    ServRepo servRepo;

    public ServService(ServRepo servRepo) {
        this.servRepo = servRepo;
    }

    public Serv getById(Long id) {
        return servRepo.findById(id).get();
    }

    public void save(Serv serv) {
        servRepo.save(serv);
    }

    public void delete(Long id) {
        servRepo.deleteById(id);
    }

    public List<Serv> getAll() {
        return servRepo.findAll();
    }
}

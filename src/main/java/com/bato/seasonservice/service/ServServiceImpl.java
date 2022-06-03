package com.bato.seasonservice.service;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.repo.ServRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServServiceImpl implements ServService {
    private final
    ServRepo servRepo;

    public ServServiceImpl(ServRepo servRepo) {
        this.servRepo = servRepo;
    }

    @Override
    public Serv getById(Long id) {
        return servRepo.findById(id).get();
    }

    @Override
    public void save(Serv serv) {
        servRepo.save(serv);
    }

    @Override
    public void delete(Long id) {
        servRepo.deleteById(id);
    }

    @Override
    public List<Serv> getAll() {
        return servRepo.findAll();
    }
}

package com.bato.seasonservice.service;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.repo.ServRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServServiceImpl implements ServService {
    private final
    ServRepo servRepo;

    public ServServiceImpl(ServRepo servRepo) {
        this.servRepo = servRepo;
    }

    @Override
    public Serv getById(Long id) {
        log.info("in ServServiceImpl getById {}", id);
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

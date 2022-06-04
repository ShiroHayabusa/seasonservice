package com.bato.seasonservice.service;

import com.bato.seasonservice.model.ServRequest;
import com.bato.seasonservice.repo.ServRequestRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServRequestService {
    private final ServRequestRepo servRequestRepo;

    public ServRequestService(ServRequestRepo servRequestRepo) {
        this.servRequestRepo = servRequestRepo;
    }

    public ServRequest getById(Long id){
        return servRequestRepo.findById(id).get();
    }

    public void saveServRequest(ServRequest servRequest){
        servRequestRepo.save(servRequest);
    }

    public void deleteServRequest(Long id){
        servRequestRepo.deleteById(id);
    }

    public List<ServRequest> getAllServRequests() {
        return servRequestRepo.findAll();
    }

    public List<ServRequest> findByUserIdAndServId(Long userId, Long servId) {
        return servRequestRepo.findByUserIdAndServId(userId, servId);
    }

    public List<ServRequest> findByUserId(Long userId) {
        return servRequestRepo.findByUserId(userId);
    }
}

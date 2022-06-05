package com.bato.seasonservice.repo;

import com.bato.seasonservice.model.ServRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServRequestRepo extends JpaRepository<ServRequest, Long> {
    List<ServRequest> findByUserIdAndServId(Long userId, Long servId);
    List<ServRequest> findByUserIdOrderByDateDesc(Long userId);
}

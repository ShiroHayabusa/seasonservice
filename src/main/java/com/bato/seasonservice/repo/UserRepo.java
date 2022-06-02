package com.bato.seasonservice.repo;

import com.bato.seasonservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}

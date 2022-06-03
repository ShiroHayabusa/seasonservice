package com.bato.seasonservice.service;

import com.bato.seasonservice.model.Role;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.repo.RoleRepo;
import com.bato.seasonservice.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        Role role = roleRepo.findByName("ROLE_USER");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User findByLogin(String login){
        return userRepo.findByLogin(login);
    }

    public User findByLoginAndPassword(String login, String password){
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }
}

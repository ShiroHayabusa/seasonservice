package com.bato.seasonservice.controller;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.service.ServService;
import com.bato.seasonservice.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ServController {

    private final UserService userService;
    private final ServService servService;

    public ServController(UserService userService, ServService servService) {
        this.userService = userService;
        this.servService = servService;
    }

    @GetMapping(value = "/servs/{id}")
    public ResponseEntity<Serv> getServ(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Serv serv = servService.getById(id);

        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serv, HttpStatus.OK);
    }

    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByLogin(username);
        return user;
    }

    @PostMapping("/servs")
    public ResponseEntity<Serv> saveServ(@RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/servs")
    public ResponseEntity<Serv> updateServ(@RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/servs/{id}")
    public ResponseEntity<Serv> deleteServ(@PathVariable Long id) {
        Serv serv = servService.getById(id);
        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/servs")
    public ResponseEntity<List<Serv>> getAllServs(){
        List<Serv> servs = servService.getAll();
        if (servs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servs, HttpStatus.OK);
    }
}

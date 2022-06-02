package com.bato.seasonservice.controller;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.service.ServService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servs/")
public class ServController {

    private final
    ServService servService;

    public ServController(ServService servService) {
        this.servService = servService;
    }

    @GetMapping(value = "{id}")
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

    @PostMapping("")
    public ResponseEntity<Serv> saveServ(@RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Serv> updateServ(@RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Serv> deleteServ(@PathVariable Long id) {
        Serv serv = servService.getById(id);
        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public ResponseEntity<List<Serv>> getAllServs(){
        List<Serv> servs = servService.getAll();
        if (servs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servs, HttpStatus.OK);
    }

}

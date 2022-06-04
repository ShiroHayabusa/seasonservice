package com.bato.seasonservice.controller;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.model.ServRequest;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.repo.UserRepo;
import com.bato.seasonservice.service.MailSender;
import com.bato.seasonservice.service.ServRequestService;
import com.bato.seasonservice.service.ServService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
public class ServRequestController {

    public static final String DATE_FORMAT_NOW = "dd.MM.yyyy HH:mm";

    private final ServService servService;
    private final MailSender mailSender;
    private final UserRepo userRepo;
    private final ServRequestService servRequestService;

    public ServRequestController(ServService servService, MailSender mailSender,
                                 UserRepo userRepo,
                                 ServRequestService servRequestService) {
        this.servService = servService;
        this.mailSender = mailSender;
        this.userRepo = userRepo;
        this.servRequestService = servRequestService;
    }

    @GetMapping(value = "/requests/{requestId}")
    public ResponseEntity<ServRequest> getServ(@PathVariable("requestId") Long requestId) {
        if (requestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ServRequest servRequest = servRequestService.getById(requestId);

        if (servRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servRequest, HttpStatus.OK);
    }

    @PostMapping("/servs/{servId}/requests/")
    public ResponseEntity<ServRequest> saveServRequest(@PathVariable("servId") Long servId,
                                                       @RequestBody @Valid ServRequest servRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (servRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByLogin(username);
        servRequest.setUser(user);

        Serv serv = servService.getById(servId);
        servRequest.setServ(serv);

        servRequest.setDate(now());

        sendMessage(user);
        servRequestService.saveServRequest(servRequest);
        return new ResponseEntity<>(servRequest, httpHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<ServRequest> deleteServRequest(@PathVariable Long requestId) {
        servRequestService.deleteServRequest(requestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userId}/servs/{servId}/requests")
    public ResponseEntity<List<ServRequest>> getAllServRequests(@PathVariable Long userId,
                                                                @PathVariable Long servId){
        List<ServRequest> servRequests = servRequestService.findByUserIdAndServId(userId, servId);
        if (servRequests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servRequests, HttpStatus.OK);
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public void sendMessage(User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        user = userRepo.findByLogin(username);
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("%s, Здравствуйте!\n", user.getFirstName());
            mailSender.send(user.getEmail(), "Информация об услуге", message);
        }
    }
}

package com.bato.seasonservice.controller;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.model.ServRequest;
import com.bato.seasonservice.model.User;
import com.bato.seasonservice.repo.UserRepo;
import com.bato.seasonservice.service.MailSender;
import com.bato.seasonservice.service.ServRequestService;
import com.bato.seasonservice.service.ServService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
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

    @Operation(
            summary = "Найти обращение за услугой по заданному идентификатору",
            description = "Запрашивает обращение за услугой по заданному идентификатору",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "Идентификатор запрашиваемого обращения за услугой",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Bad Request (Неверный запрос)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping(value = "/requests/{requestId}")
    public ResponseEntity<ServRequest> addServ(@PathVariable("requestId") Long requestId) {
        if (requestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ServRequest servRequest = servRequestService.getById(requestId);

        if (servRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servRequest, HttpStatus.OK);
    }

    @Operation(
            summary = "Создать новое обращение за услугой",
            description = "Создает новое обращение за услугой",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "servId",
                            required = true,
                            description = "Идентификатор услуги, по которой создается обращение",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "201", description = "Created (Cервер принял запрос, обработал и создал новый ресурс)"),
            @ApiResponse(responseCode = "302", description = "Found (Ресурс временно перемещен)"),
            @ApiResponse(responseCode = "400", description = "Bad Request (Неверный запрос)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/servs/{servId}/requests")
    public ResponseEntity<?> saveServRequest(@PathVariable("servId") Long servId,
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

        if (serv.getServLimit() > 0) {
            serv.setServLimit(serv.getServLimit() - 1);
            String message = String.format("%s, здравствуйте!\n" +
                            "Вы успешно подали заявление на получение услуги",
                    user.getFirstName());
            sendMessage(user, message);
            servRequest.setServ(serv);
            servRequest.setDate(now());
            servRequestService.saveServRequest(servRequest);
            return new ResponseEntity<>(servRequest, httpHeaders, HttpStatus.CREATED);
        } else {
            String message = String.format("%s, здравствуйте!\n" +
                            "к сожалению лимит по данной услуге закончился",
                    user.getFirstName());
            sendMessage(user, message);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:8080/servs")).build();
            //на фронте при редиректе вывести окно с сообщением о превышении лимита
        }
    }

    @Operation(
            summary = "Удаляет обращение за услугой по заданному идентификатору",
            description = "Удаляет обращение за услугой из базы данных по заданному идентификатору",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "id",
                            required = true,
                            description = "Идентификатор обращения за услугой",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<ServRequest> deleteServRequest(@PathVariable Long requestId) {
        servRequestService.deleteServRequest(requestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Запрос списка обращений пользователя по услуге",
            description = "Список обращений пользователя по услуге",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "userId",
                            required = true,
                            description = "Идентификатор пользователя",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    ),
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "servId",
                            required = true,
                            description = "Идентификатор услуги, по которой запрашиваются обращения",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/users/{userId}/servs/{servId}/requests")
    public ResponseEntity<List<ServRequest>> getServRequests(@PathVariable Long userId,
                                                             @PathVariable Long servId) {
        List<ServRequest> servRequests = servRequestService.findByUserIdAndServId(userId, servId);
        if (servRequests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servRequests, HttpStatus.OK);
    }

    @Operation(
            summary = "Запрос списка всех обращений пользователя",
            description = "Список всех обращений пользователя",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "userId",
                            required = true,
                            description = "Идентификатор пользователя",
                            schema = @Schema(
                                    minimum = "1",
                                    allOf = {Long.class}
                            ),
                            style = ParameterStyle.SIMPLE
                    ),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<ServRequest>> getAllServRequests(@PathVariable Long userId) {
        List<ServRequest> servRequests = servRequestService.findByUserIdOrderByDateDesc(userId);
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

    public void sendMessage(User user, String message) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        user = userRepo.findByLogin(username);
        if (!StringUtils.isEmpty(user.getEmail())) {
            mailSender.send(user.getEmail(), "Информация об услуге", message);
        }
    }
}

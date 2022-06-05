package com.bato.seasonservice.controller;

import com.bato.seasonservice.model.Serv;
import com.bato.seasonservice.service.ServService;
import com.bato.seasonservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(
            summary = "Найти услугу по заданному идентификатору",
            description = "Запрашивает в базе данных информацию о услуге по заданному идентификатору",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "servId",
                            required = true,
                            description = "Идентификатор запрашиваемой услуги",
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
    @GetMapping(value = "/servs/{servId}")
    public ResponseEntity<Serv> getServ(@PathVariable("servId") Long servId) {
        if (servId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Serv serv = servService.getById(servId);

        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serv, HttpStatus.OK);
    }

    @Operation(
            summary = "Создать новую услугу",
            description = "Создает новую услугу"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Bad Request (Неверный запрос)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Внутренняя ошибка сервера)")
    })
    @PostMapping("/servs")
    public ResponseEntity<Serv> addServ(@RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Изменить ранее созданную услугу",
            description = "Изменяет ранее созданную в соответсвии с заданными параметрами"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Bad Request (Неверный запрос)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (У клиента нет прав доступа к содержимому)"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Внутренняя ошибка сервера)")
    })
    @PutMapping("/servs/{servId}")
    public ResponseEntity<Serv> updateServ(@PathVariable("servId") Long servId,
                                           @RequestBody @Valid Serv serv) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (serv == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Serv servFromDb = servService.getById(servId);
        BeanUtils.copyProperties(serv, servFromDb, "id");
        servService.save(serv);
        return new ResponseEntity<>(serv, httpHeaders, HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить услугу по заданному идентификатору",
            description = "Удаляет услугу из базы данных по заданному идентификатору",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            name = "servId",
                            required = true,
                            description = "Идентификатор удаляемой услуги",
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
            @ApiResponse(responseCode = "400", description = "Bad Request (Неверный запрос)"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error (Внутренняя ошибка сервера)")
    })
    @DeleteMapping("/servs/{servId}")
    public ResponseEntity<Serv> deleteServ(@PathVariable Long servId) {
        Serv serv = servService.getById(servId);
        if (serv == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        servService.delete(servId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Запрос списка услуг",
            description = "Список услуг"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос был выполнен успешно"),
            @ApiResponse(responseCode = "404", description = "Not Found (Ресурс не найден)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),
    })
    @GetMapping("/servs")
    public ResponseEntity<List<Serv>> getAllServs(){
        List<Serv> servs = servService.getAll();
        if (servs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(servs, HttpStatus.OK);
    }
}

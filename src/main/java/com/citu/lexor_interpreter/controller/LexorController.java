package com.citu.lexor_interpreter.controller;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LexorController {

    private final InterpreterService interpreterService;

    public LexorController(InterpreterService interpreterService) {
        this.interpreterService = interpreterService;
    }

    @PostMapping(value = "/execute", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExecuteResponse execute(@RequestBody String code) {
        return interpreterService.execute(code);
    }
}

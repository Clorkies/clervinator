package com.citu.lexor_interpreter.controller;

import com.citu.lexor_interpreter.model.ExecuteRequest;
import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LexorController {

    private final InterpreterService interpreterService;

    public LexorController(InterpreterService interpreterService) {
        this.interpreterService = interpreterService;
    }

    // JSON endpoint: accepts { "code": "...", "inputs": ["1", "2"] }
    @PostMapping(value = "/execute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExecuteResponse executeJson(@RequestBody ExecuteRequest request) {
        List<String> inputs = request.inputs() != null ? request.inputs() : List.of();
        return interpreterService.execute(request.code(), inputs);
    }

    // Plain-text fallback (no inputs)
    @PostMapping(value = "/execute", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExecuteResponse executePlainText(@RequestBody String code) {
        return interpreterService.execute(code);
    }
}

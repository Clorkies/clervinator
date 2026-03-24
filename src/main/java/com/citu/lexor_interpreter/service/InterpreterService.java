package com.citu.lexor_interpreter.service;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Placeholder only
 */
@Service
public class InterpreterService {

    public ExecuteResponse execute(String code) {
        if (code == null || code.isBlank()) {
            return ExecuteResponse.withError("No code provided.");
        }
        return ExecuteResponse.success("Hello from LEXOR", List.of());
    }
}

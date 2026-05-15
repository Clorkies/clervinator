package com.citu.lexor_interpreter.model;

import java.util.List;

public record ExecuteResponse(
        String output,
        String error,
        boolean isWaitingForInput,
        List<Object> tokens
) {
    public static ExecuteResponse success(String output, List<Object> tokens) {
        return new ExecuteResponse(output, null, false, tokens != null ? tokens : List.of());
    }

    public static ExecuteResponse waiting(String output, List<Object> tokens) {
        return new ExecuteResponse(output, null, true, tokens != null ? tokens : List.of());
    }

    public static ExecuteResponse withError(String error) {
        return new ExecuteResponse(null, error, false, List.of());
    }
}

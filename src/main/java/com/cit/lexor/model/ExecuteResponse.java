package com.cit.lexor.model;

import java.util.List;

public record ExecuteResponse(
        String output,
        String error,
        List<Object> tokens
) {
    public static ExecuteResponse success(String output, List<Object> tokens) {
        return new ExecuteResponse(output, null, tokens != null ? tokens : List.of());
    }

    public static ExecuteResponse withError(String error) {
        return new ExecuteResponse(null, error, List.of());
    }
}

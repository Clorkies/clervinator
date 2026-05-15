package com.citu.lexor_interpreter.interpreter;

public class InputRequiredException extends RuntimeException {
    public InputRequiredException(String message) {
        super(message);
    }
}

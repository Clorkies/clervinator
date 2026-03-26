package com.citu.lexor_interpreter.lexer.token;

public record TokenPosition(int line, int col) {

    // line & col is 1-based index
    // int index is 0-based index, to be implemented in the future..

    public TokenPosition {
        if (line < 1 || col < 1) {
            throw new IllegalArgumentException("Line and column must be greater than 0");
        }
    }

    @Override
    public String toString() {
        return line + ":" + col;
    }
    
}
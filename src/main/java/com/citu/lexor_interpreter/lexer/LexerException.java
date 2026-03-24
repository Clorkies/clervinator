package com.citu.lexor_interpreter.lexer;

import com.citu.lexor_interpreter.lexer.token.TokenPosition;

public class LexerException extends RuntimeException {
    private final TokenPosition position;
    private String offendingLexeme;


    public LexerException(String message, TokenPosition position, String offendingLexeme) {
        super("Lexer error at " + position + ": " + message + " (offending lexeme: '" + offendingLexeme + "')");
        this.position = position;
        this.offendingLexeme = offendingLexeme;
    }

}

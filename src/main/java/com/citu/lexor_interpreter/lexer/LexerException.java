package com.citu.lexor_interpreter.lexer;

import com.citu.lexor_interpreter.lexer.token.TokenPosition;

public class LexerException extends RuntimeException {

    public LexerException(String message, TokenPosition position, String offendingLexeme) {
        super("Lexer error at " + position + ": " + message + " (offending lexeme: '" + offendingLexeme + "')");
    }

    public LexerException(String message) {
        super(message);
    }

}

package com.citu.lexor_interpreter.lexer.token;

import com.citu.lexor_interpreter.lexer.LexerException;

public record Token(
    TokenType type,
    String value,
    Object literalValue,
    TokenPosition position
){
    public Token {
        if (type == null) throw new LexerException("Token type cannot be null");
        if (value == null) throw new LexerException("Token value cannot be null");
        if (literalValue == null) throw new LexerException("Token literal value cannot be null");
        if (position == null) throw new LexerException("Token position cannot be null");
    }
    

    public String debugValue() {
        return literalValue == null ? "null" : literalValue.toString();
    }
}

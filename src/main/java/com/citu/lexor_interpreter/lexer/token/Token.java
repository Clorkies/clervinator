package com.citu.lexor_interpreter.lexer.token;

import com.citu.lexor_interpreter.lexer.LexerException;

public record Token(
    TokenType type,
    String lexeme,
    Object literalValue,
    TokenPosition position
){
    public Token {
        if (type == null) throw new LexerException("Token type cannot be null", position, debugValue());
        if (lexeme == null) throw new LexerException("Token value cannot be null", position, debugValue());
        if (position == null) throw new LexerException("Token position cannot be null", position, debugValue());
    }
    
    public boolean hasLiteral() {
        return literalValue != null;
    }

    public String debugValue() {
        return hasLiteral() ? literalValue.toString(): lexeme;
    }
}

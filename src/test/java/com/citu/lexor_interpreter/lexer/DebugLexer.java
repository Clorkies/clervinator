package com.citu.lexor_interpreter.lexer;

import java.util.List;
import com.citu.lexor_interpreter.lexer.token.Token;

public class DebugLexer {
    public static void main(String[] args) {
        String source = "SWITCH (x)\nSTART SWITCH\nCASE 1:\n    PRINT: \"one\"\nDEFAULT:\n    PRINT: \"other\"\nEND SWITCH\n";
        List<Token> tokens = new Lexer().lex(source);
        for(Token t : tokens) {
            System.out.println(t.type() + " " + t.lexeme());
        }
    }
}
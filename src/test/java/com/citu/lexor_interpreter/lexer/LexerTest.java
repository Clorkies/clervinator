package com.citu.lexor_interpreter.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.lexer.token.TokenType;

class LexerTest {

    @Test
    void lex_programStructure_keywordsAndEof() {
        String source = """
            SCRIPT AREA
            START SCRIPT
            END SCRIPT
            """;

        List<Token> tokens = new Lexer().lex(source);

        assertTokenTypes(tokens,
            TokenType.SCRIPT_AREA,
            TokenType.START_SCRIPT,
            TokenType.END_SCRIPT,
            TokenType.EOF
        );
    }

    @Test
    void lex_declareAssignmentAndPrint_concatAndNewlineSymbol() {
        String source = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x, y=5
            x=y=4
            PRINT: x & $ & "last"
            END SCRIPT
            """;

        List<Token> tokens = new Lexer().lex(source);

        assertTokenTypes(tokens,
            TokenType.SCRIPT_AREA,
            TokenType.START_SCRIPT,
            TokenType.DECLARE,
            TokenType.INT_TYPE,
            TokenType.IDENTIFIER,
            TokenType.COMMA,
            TokenType.IDENTIFIER,
            TokenType.ASSIGN,
            TokenType.INT_LITERAL,
            TokenType.IDENTIFIER,
            TokenType.ASSIGN,
            TokenType.IDENTIFIER,
            TokenType.ASSIGN,
            TokenType.INT_LITERAL,
            TokenType.PRINT,
            TokenType.COLON,
            TokenType.IDENTIFIER,
            TokenType.CONCAT,
            TokenType.NEWLINE,
            TokenType.CONCAT,
            TokenType.STRING_LITERAL,
            TokenType.END_SCRIPT,
            TokenType.EOF
        );
    }

    @Test
    void lex_literals_intFloatCharStringBool() {
        String source = """
            DECLARE INT a=123
            DECLARE FLOAT b=10.5
            DECLARE CHAR c='z'
            DECLARE BOOL t="TRUE"
            PRINT: "hello"
            """;

        List<Token> tokens = new Lexer().lex(source);

        assertTrue(tokens.stream().anyMatch(t -> t.type() == TokenType.INT_LITERAL && Integer.valueOf(123).equals(t.literalValue())));
        assertTrue(tokens.stream().anyMatch(t -> t.type() == TokenType.FLOAT_LITERAL && Double.valueOf(10.5).equals(t.literalValue())));
        assertTrue(tokens.stream().anyMatch(t -> t.type() == TokenType.CHAR_LITERAL && Character.valueOf('z').equals(t.literalValue())));
        assertTrue(tokens.stream().anyMatch(t -> t.type() == TokenType.BOOL_LITERAL && Boolean.TRUE.equals(t.literalValue())));
        assertTrue(tokens.stream().anyMatch(t -> t.type() == TokenType.STRING_LITERAL && "hello".equals(t.literalValue())));
    }

    @Test
    void lex_comments_areIgnored() {
        String source = """
            %% full-line comment
            DECLARE INT x %% trailing comment
            """;

        List<Token> tokens = new Lexer().lex(source);

        assertTokenTypes(tokens,
            TokenType.DECLARE,
            TokenType.INT_TYPE,
            TokenType.IDENTIFIER,
            TokenType.EOF
        );
    }

    @Test
    void lex_unterminatedString_throwsLexerException() {
        String source = "PRINT: \"abc";

        LexerException ex = assertThrows(LexerException.class, () -> new Lexer().lex(source));
        assertTrue(ex.getMessage().contains("Unterminated string literal"));
    }

    @Test
    void lex_invalidCharLiteral_throwsLexerException() {
        String source = "DECLARE CHAR c='ab'";

        LexerException ex = assertThrows(LexerException.class, () -> new Lexer().lex(source));
        assertTrue(ex.getMessage().contains("Invalid char literal"));
    }

    @Test
    void lex_unterminatedEscape_throwsLexerException() {
        String source = "PRINT: [#";

        LexerException ex = assertThrows(LexerException.class, () -> new Lexer().lex(source));
        assertTrue(ex.getMessage().contains("Unterminated escape sequence"));
    }

    private static void assertTokenTypes(List<Token> tokens, TokenType... expected) {
        assertEquals(expected.length, tokens.size(), "Token count mismatch");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], tokens.get(i).type(), "Unexpected token at index " + i + " (" + tokens.get(i).lexeme() + ")");
        }
    }
}

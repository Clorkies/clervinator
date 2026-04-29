package com.citu.lexor_interpreter.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.lexer.token.TokenPosition;
import com.citu.lexor_interpreter.lexer.token.TokenType;

public class Lexer {
    private static final Map<String, TokenType> KEYWORDS = Map.ofEntries(
        Map.entry("DECLARE", TokenType.DECLARE),

        Map.entry("INT", TokenType.INT_TYPE),
        Map.entry("CHAR", TokenType.CHAR_TYPE),
        Map.entry("BOOL", TokenType.BOOL_TYPE),
        Map.entry("FLOAT", TokenType.FLOAT_TYPE),

        Map.entry("PRINT", TokenType.PRINT),
        Map.entry("SCAN", TokenType.SCAN),
        Map.entry("FOR", TokenType.FOR),
        Map.entry("REPEAT", TokenType.REPEAT),
        Map.entry("WHEN", TokenType.WHEN),
        Map.entry("IF", TokenType.IF),
        Map.entry("ELSE", TokenType.ELSE),
        Map.entry("AND", TokenType.AND),
        Map.entry("OR", TokenType.OR),
        Map.entry("NOT", TokenType.NOT),
        
        Map.entry("START_SCRIPT", TokenType.START_SCRIPT),
        Map.entry("END_SCRIPT", TokenType.END_SCRIPT),
        Map.entry("START_FOR", TokenType.START_FOR),
        Map.entry("END_FOR", TokenType.END_FOR),
        Map.entry("START_IF", TokenType.START_IF),
        Map.entry("END_IF", TokenType.END_IF),
        Map.entry("START_REPEAT", TokenType.START_REPEAT),
        Map.entry("END_REPEAT", TokenType.END_REPEAT)
    );

    private String code;
    private final List<Token> tokens = new ArrayList<>();
    private int start;
    private int currentIndex;
    private int line;
    private int col;
    private int tokenLine;
    private int tokenCol;

    public List<Token> lex(String code) {
        if (code == null) {
            throw new LexerException("Source code cannot be null", new TokenPosition(1, 1), "null");
        }

        this.code = code;
        this.tokens.clear();
        this.start = 0;
        this.currentIndex = 0;
        this.line = 1;
        this.col = 1;

        while (!isAtEnd()) {
            start = currentIndex;
            tokenLine = line;
            tokenCol = col;
            scanToken();
        }

        addToken(TokenType.EOF, "EOF");
        return new ArrayList<>(tokens);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case ' ', '\t', '\r' -> {
                // ignore whitespace
            }
            case '\n' -> {
                // line/column tracking already handled by advance()
            }
            case '%' -> {
                if (match('%')) {
                    skipComment();
                } else {
                    addToken(TokenType.MODULO, "%");
                }
            }
            case ':' -> addToken(TokenType.COLON, ":");
            case ',' -> addToken(TokenType.COMMA, ",");
            case '(' -> addToken(TokenType.LPAREN, "(");
            case ')' -> addToken(TokenType.RPAREN, ")");
            case '&' -> addToken(TokenType.CONCAT, "&");
            case '$' -> addToken(TokenType.NEWLINE, "$");
            case '+' -> addToken(TokenType.PLUS, "+");
            case '-' -> addToken(TokenType.MINUS, "-");
            case '*' -> addToken(TokenType.MULTIPLY, "*");
            case '/' -> addToken(TokenType.DIVIDE, "/");
            case '[' -> escape();
            case ']' -> addToken(TokenType.ESCAPE_CLOSE, "]");
            case '\'' -> charLiteral();
            case '"' -> stringOrBoolLiteral();
            case '=' -> addToken(match('=') ? TokenType.EQUAL : TokenType.ASSIGN, currentLexeme());
            case '>' -> addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER_THAN, currentLexeme());
            case '<' -> {
                if (match('=')) {
                    addToken(TokenType.LESS_EQUAL, "<=");
                } else if (match('>')) {
                    addToken(TokenType.NOT_EQUAL, "<>");
                } else {
                    addToken(TokenType.LESS_THAN, "<");
                }
            }
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isIdentifierStart(c)) {
                    identifierOrKeyword();
                } else {
                    throw error("Unexpected character '" + c + "'", String.valueOf(c));
                }
            }
        }
    }

    private void skipComment() {
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Floats must look like: <digits>.<digits>
        // - ".5" does not reach this method; '.' is treated as an unexpected character in scanToken().
        // - "5." is rejected here because there are no digits after the dot.
        if (peek() == '.') {
            if (!isDigit(peekNext())) {
                throw error("Malformed float literal; expected digits after decimal point", currentLexeme());
            }

            // Consume '.'
            advance();
            while (isDigit(peek())) {
                advance();
            }
            addToken(TokenType.FLOAT_LITERAL, currentLexeme());
            return;
        }

        // Preserve explicitness/zeros by storing the exact numeric lexeme text.
        addToken(TokenType.INT_LITERAL, currentLexeme());
    }

    private void stringOrBoolLiteral() {
        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') {
                throw error("Unterminated string literal", currentLexeme());
            }
            advance();
        }

        if (isAtEnd()) {
            throw error("Unterminated string literal", currentLexeme());
        }

        advance();
        String full = currentLexeme();
        String content = full.substring(1, full.length() - 1);
        if ("TRUE".equals(content)) {
            addToken(TokenType.BOOL_LITERAL, true);
        } else if ("FALSE".equals(content)) {
            addToken(TokenType.BOOL_LITERAL, false);
        } else {
            addToken(TokenType.STRING_LITERAL, content);
        }
    }

    private void charLiteral() {
        if (isAtEnd() || peek() == '\n') {
            throw error("Unterminated char literal", currentLexeme());
        }

        char value = advance();
        if (isAtEnd() || peek() != '\'') {
            throw error("Invalid char literal; expected closing quote", currentLexeme());
        }

        advance();
        addToken(TokenType.CHAR_LITERAL, value);
    }

    private void escape() {
        // Escape sequences start with '[' and normally end at the next ']'.
        // To include a literal ']' inside the escape, write ']]' (double-close):
        // e.g. `[]]` => content `]`, `[#]` => content `#`, `[[]` => content `[`.
        StringBuilder content = new StringBuilder();

        while (!isAtEnd()) {
            char next = peek();

            if (next == '\n') {
                throw error("Unterminated escape sequence", currentLexeme());
            }

            if (next == ']') {
                // Treat `]]` as an escaped ']' character inside the escape.
                if (peekNext() == ']') {
                    advance(); // consume first ']'
                    content.append(']');
                    continue;
                }

                // Single ']' ends the escape.
                advance(); // consume closing ']'
                addToken(TokenType.STRING_LITERAL, content.toString());
                return;
            }

            content.append(advance());
        }

        throw error("Unterminated escape sequence", currentLexeme());
    }

    private void identifierOrKeyword() {
        while (isIdentifierPart(peek())) {
            advance();
        }

        String firstWord = currentLexeme();
        String upper = firstWord.toUpperCase();

        if ("SCRIPT".equals(upper)) {
            if (tryReadSecondWord("AREA")) {
                addToken(TokenType.SCRIPT_AREA, currentLexeme());
                return;
            }
        } else if ("START".equals(upper)) {
            if (tryReadSecondWord("SCRIPT")) {
                addToken(TokenType.START_SCRIPT, currentLexeme());
                return;
            }
            if (tryReadSecondWord("FOR")) {
                addToken(TokenType.START_FOR, currentLexeme());
                return;
            }
            if (tryReadSecondWord("IF")) {
                addToken(TokenType.START_IF, currentLexeme());
                return;
            }
            if (tryReadSecondWord("REPEAT")) {
                addToken(TokenType.START_REPEAT, currentLexeme());
                return;
            }
        } else if ("END".equals(upper)) {
            if (tryReadSecondWord("SCRIPT")) {
                addToken(TokenType.END_SCRIPT, currentLexeme());
                return;
            }
            if (tryReadSecondWord("FOR")) {
                addToken(TokenType.END_FOR, currentLexeme());
                return;
            }
            if (tryReadSecondWord("IF")) {
                addToken(TokenType.END_IF, currentLexeme());
                return;
            }
            if (tryReadSecondWord("REPEAT")) {
                addToken(TokenType.END_REPEAT, currentLexeme());
                return;
            }
        } else if ("ELSE".equals(upper) && tryReadSecondWord("IF")) {
            addToken(TokenType.ELSE_IF, currentLexeme());
            return;
        }

        TokenType tokenType = KEYWORDS.getOrDefault(upper, TokenType.IDENTIFIER);
        addToken(tokenType, firstWord);
    }

    private boolean tryReadSecondWord(String expectedWord) {
        int savedIndex = currentIndex;
        int savedLine = line;
        int savedCol = col;

        while (!isAtEnd() && (peek() == ' ' || peek() == '\t' || peek() == '\r')) {
            advance();
        }

        if (!isIdentifierStart(peek())) {
            currentIndex = savedIndex;
            line = savedLine;
            col = savedCol;
            return false;
        }

        int secondStart = currentIndex;
        while (isIdentifierPart(peek())) {
            advance();
        }
        String second = code.substring(secondStart, currentIndex).toUpperCase();
        if (!expectedWord.equals(second)) {
            currentIndex = savedIndex;
            line = savedLine;
            col = savedCol;
            return false;
        }
        return true;
    }

    private boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return code.charAt(currentIndex);
    }

    private char peekNext() {
        int next = currentIndex + 1;
        if (next >= code.length()) {
            return '\0';
        }
        return code.charAt(next);
    }

    private char advance() {
        char c = code.charAt(currentIndex++);
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (code.charAt(currentIndex) != expected) {
            return false;
        }
        advance();
        return true;
    }

    private boolean isAtEnd() {
        return currentIndex >= code.length();
    }

    private String currentLexeme() {
        return code.substring(start, currentIndex);
    }

    private void addToken(TokenType type, Object literalValue) {
        String lexeme = currentLexeme();
        tokens.add(new Token(type, lexeme, literalValue, new TokenPosition(tokenLine, tokenCol)));
    }

    private LexerException error(String message, String offendingLexeme) {
        return new LexerException(message, new TokenPosition(tokenLine, tokenCol), offendingLexeme);
    }
}

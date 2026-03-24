package com.citu.lexor_interpreter.lexer.token;

public enum TokenType {
    // Reserved keywords and sections
    SCRIPT_AREA,
    START_SCRIPT,
    END_SCRIPT,
    DECLARE,
    START_FOR,
    END_FOR,
    FOR,
    REPEAT,
    WHEN,
    IF,
    START_IF,
    ELSE_IF,
    ELSE,
    END_IF,
    START_REPEAT,
    END_REPEAT,

    // Data Types
    INT_TYPE,
    CHAR_TYPE,
    BOOL_TYPE,
    FLOAT_TYPE,

    // Input/Output
    PRINT,
    SCAN,

    // Operators
    PLUS,           // +
    MINUS,          // -
    MULTIPLY,       // *
    DIVIDE,         // /
    MODULO,         // %

    // Assignment
    ASSIGN,         // =

    // Comparison Operators
    GREATER_THAN,   // >
    GREATER_EQUAL,  // >=
    LESS_THAN,      // <
    LESS_EQUAL,     // <=
    EQUAL,          // ==
    NOT_EQUAL,      // <>

    // Logical Operators
    AND,
    OR,
    NOT,

    // Special Characters
    NEWLINE,         // $
    CONCAT,          // &
    ESCAPE_OPEN,     // [
    ESCAPE_CLOSE,    // ]
    COLON,           // :
    COMMA,           // ,
    LPAREN,          // (
    RPAREN,          // )

    // Literals
    INT_LITERAL,
    FLOAT_LITERAL,
    CHAR_LITERAL,
    BOOL_LITERAL,
    STRING_LITERAL,

    // Identifiers
    IDENTIFIER,

    // Comments
    COMMENT,

    // Whitespace, separators, control, EOF
    END_OF_LINE,
    EOF,
    UNKNOWN
}

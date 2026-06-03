package com.citu.lexor_interpreter.interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ParserException;

class EnvironmentTest {

    @Test
    void declareArray_fillsWithTypeDefault() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 3);

        assertEquals(3, env.getArrayLength("nums"));
        assertEquals(Integer.valueOf(0), env.getElement("nums", 0));
        assertEquals(Integer.valueOf(0), env.getElement("nums", 2));
    }

    @Test
    void setElement_thenGetElement_returnsStoredValue() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 3);

        env.setElement("nums", 1, 42);

        assertEquals(Integer.valueOf(42), env.getElement("nums", 1));
    }

    @Test
    void getElement_outOfBounds_throwsIndexError() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 3);

        ParserException high = assertThrows(ParserException.class, () -> env.getElement("nums", 3));
        assertTrue(high.getMessage().contains("IndexError"));

        assertThrows(ParserException.class, () -> env.getElement("nums", -1));
    }

    @Test
    void setElement_outOfBounds_throwsIndexError() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 2);

        ParserException ex = assertThrows(ParserException.class, () -> env.setElement("nums", 5, 1));
        assertTrue(ex.getMessage().contains("IndexError"));
    }

    @Test
    void setElement_typeMismatch_throwsTypeError() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 2);

        ParserException ex = assertThrows(ParserException.class, () -> env.setElement("nums", 0, true));
        assertTrue(ex.getMessage().contains("TypeError"));
    }

    @Test
    void setElement_intIntoFloatArray_isCoercedToDouble() {
        Environment env = new Environment();
        env.declareArray("temps", TokenType.FLOAT_TYPE, 2);

        env.setElement("temps", 0, 5);

        assertEquals(Double.valueOf(5.0), env.getElement("temps", 0));
    }

    @Test
    void get_wholeArray_throws() {
        Environment env = new Environment();
        env.declareArray("nums", TokenType.INT_TYPE, 3);

        ParserException ex = assertThrows(ParserException.class, () -> env.get("nums"));
        assertTrue(ex.getMessage().toLowerCase().contains("array"));
    }
}

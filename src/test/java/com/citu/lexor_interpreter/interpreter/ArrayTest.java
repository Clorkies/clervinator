package com.citu.lexor_interpreter.interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.parser.Parser;
import com.citu.lexor_interpreter.parser.ParserException;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;

class ArrayTest {

    private String run(String code) {
        ProgramNode program = new Parser(new Lexer().lex(code)).parseProgram();
        return new Interpreter().interpret(program);
    }

    @Test
    void array_declaredIntArray_defaultsToZero() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@3
                PRINT: nums@0
                END SCRIPT
                """;
        assertEquals("0", run(code));
    }

    @Test
    void array_writeThenRead_returnsStoredValues() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@3
                nums@0 = 10
                nums@2 = 20
                PRINT: nums@0 & "," & nums@2
                END SCRIPT
                """;
        assertEquals("10,20", run(code));
    }

    @Test
    void array_lengthOperator_returnsSize() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@4
                PRINT: LENGTH nums
                END SCRIPT
                """;
        assertEquals("4", run(code));
    }

    @Test
    void array_forLoopFillAndSum_usingLength() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@5
                DECLARE INT i, sum=0
                FOR (i=0, i < LENGTH nums, i=i+1)
                START FOR
                    nums@i = i * i
                END FOR
                FOR (i=0, i < LENGTH nums, i=i+1)
                START FOR
                    sum = sum + nums@i
                END FOR
                PRINT: sum
                END SCRIPT
                """;
        // 0 + 1 + 4 + 9 + 16 = 30
        assertEquals("30", run(code));
    }

    @Test
    void array_indexExpression_evaluatesArithmeticInParens() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@5
                DECLARE INT i=1
                nums@(i + 2) = 99
                PRINT: nums@3
                END SCRIPT
                """;
        assertEquals("99", run(code));
    }

    @Test
    void array_floatElement_coercesIntToFloat() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE FLOAT temps@2
                temps@0 = 5
                PRINT: temps@0
                END SCRIPT
                """;
        assertEquals("5.0", run(code));
    }

    @Test
    void array_charAndBoolElements_printCorrectly() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE CHAR letters@2
                DECLARE BOOL flags@2
                letters@0 = 'A'
                flags@1 = "TRUE"
                PRINT: letters@0 & flags@0 & flags@1
                END SCRIPT
                """;
        // letters@0='A', flags@0 default FALSE, flags@1=TRUE
        assertEquals("AFALSETRUE", run(code));
    }

    @Test
    void array_indexOutOfBounds_throwsIndexError() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@2
                nums@5 = 1
                END SCRIPT
                """;
        ParserException ex = assertThrows(ParserException.class, () -> run(code));
        assertTrue(ex.getMessage().contains("IndexError"));
    }

    @Test
    void array_nonIntIndex_throwsIndexError() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@3
                DECLARE FLOAT f=1.5
                nums@f = 1
                END SCRIPT
                """;
        ParserException ex = assertThrows(ParserException.class, () -> run(code));
        assertTrue(ex.getMessage().contains("array index must be INT"));
    }

    @Test
    void array_wrongElementType_throwsTypeError() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT nums@2
                DECLARE BOOL b="TRUE"
                nums@0 = b
                END SCRIPT
                """;
        ParserException ex = assertThrows(ParserException.class, () -> run(code));
        assertTrue(ex.getMessage().contains("TypeError"));
    }
}

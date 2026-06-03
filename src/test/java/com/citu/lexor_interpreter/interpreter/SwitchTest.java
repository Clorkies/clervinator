package com.citu.lexor_interpreter.interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.parser.Parser;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;

class SwitchTest {

    private String run(String code) {
        ProgramNode program = new Parser(new Lexer().lex(code)).parseProgram();
        return new Interpreter().interpret(program);
    }

    @Test
    void switch_happyPath_matchesFirstCase() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=1
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "One"
                CASE 2:
                    PRINT: "Two"
                DEFAULT:
                    PRINT: "Default"
                END SWITCH
                END SCRIPT
                """;
        assertEquals("One", run(code));
    }

    @Test
    void switch_happyPath_matchesSubsequentCase() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=2
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "One"
                CASE 2:
                    PRINT: "Two"
                DEFAULT:
                    PRINT: "Default"
                END SWITCH
                END SCRIPT
                """;
        assertEquals("Two", run(code));
    }

    @Test
    void switch_noMatch_executesDefault() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=99
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "One"
                CASE 2:
                    PRINT: "Two"
                DEFAULT:
                    PRINT: "Default"
                END SWITCH
                END SCRIPT
                """;
        assertEquals("Default", run(code));
    }

    @Test
    void switch_noMatchNoDefault_doesNothing() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=99
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "One"
                CASE 2:
                    PRINT: "Two"
                END SWITCH
                PRINT: "Done"
                END SCRIPT
                """;
        assertEquals("Done", run(code));
    }

    @Test
    void switch_stringType_matchesCorrectly() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                DECLARE BOOL b="TRUE"
                DECLARE INT val=0
                SWITCH (b)
                START SWITCH
                CASE "TRUE":
                    val=1
                CASE "FALSE":
                    val=0
                END SWITCH
                PRINT: val
                END SCRIPT
                """;
        assertEquals("1", run(code));
    }
}
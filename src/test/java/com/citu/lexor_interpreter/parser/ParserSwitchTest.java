package com.citu.lexor_interpreter.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import com.citu.lexor_interpreter.parser.ast.SwitchNode;
import com.citu.lexor_interpreter.parser.ast.StatementNode;

class ParserSwitchTest {

    private ProgramNode parse(String code) {
        return new Parser(new Lexer().lex(code)).parseProgram();
    }

    @Test
    void parse_switchStatement_validStructure() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "one"
                CASE 2:
                    PRINT: "two"
                DEFAULT:
                    PRINT: "other"
                END SWITCH
                END SCRIPT
                """;

        ProgramNode program = parse(code);
        assertEquals(1, program.statements().size());

        StatementNode stmt = program.statements().get(0);
        assertEquals(SwitchNode.class, stmt.getClass());

        SwitchNode switchNode = (SwitchNode) stmt;
        assertNotNull(switchNode.condition());
        assertEquals(2, switchNode.cases().size());
        assertNotNull(switchNode.defaultBranch());
    }

    @Test
    void parse_switchStatement_missingEndSwitchThrowsException() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                SWITCH (x)
                START SWITCH
                CASE 1:
                    PRINT: "one"
                END SCRIPT
                """;

        ParserException ex = assertThrows(ParserException.class, () -> parse(code));
        // The parser expects END SWITCH inside the parseSwitchStatement loop
        assertNotNull(ex.getMessage());
    }

    @Test
    void parse_switchStatement_duplicateDefaultThrowsException() {
        String code = """
                SCRIPT AREA
                START SCRIPT
                SWITCH (x)
                START SWITCH
                DEFAULT:
                    PRINT: "one"
                DEFAULT:
                    PRINT: "two"
                END SWITCH
                END SCRIPT
                """;

        ParserException ex = assertThrows(ParserException.class, () -> parse(code));
        assertEquals(true, ex.getMessage().contains("duplicate DEFAULT case in SWITCH"));
    }
}
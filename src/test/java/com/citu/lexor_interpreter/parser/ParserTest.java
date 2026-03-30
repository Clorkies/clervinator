package com.citu.lexor_interpreter.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ast.DeclareNode;

class ParserTest {
    
    private ProgramNode parseProgram(String source) {
        var tokens = new Lexer().lex(source);

        return new Parser(tokens).parseProgram();
    }

    // Naming conv: void methodUnderTest_condition_expectedResult() {}

    @Test
    void parse_emptyProgram_returnsEmptyProgram() {
        String source = """
            SCRIPT AREA
            START SCRIPT
            END SCRIPT
        """;

        ProgramNode program = parseProgram(source);
    
        assertEquals(0, program.statements().size());
    }

    @Test
    void parse_declareWithoutInitializer_returnsDeclareStatement() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT X
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);

        assertEquals(1, program.statements().size());

        // StatementNode doesn't expose a `type()` method directly,
        // so we can't assert statement kind via a TokenType on the interface.
        // assertEquals(TokenType.DECLARE, program.statements().get(0).type()); 

        assertTrue(program.statements().get(0) instanceof DeclareNode);

        DeclareNode declare = (DeclareNode) program.statements().get(0);
        assertEquals(TokenType.INT_TYPE, declare.type());
        assertEquals(1, declare.declarations().size());
        assertEquals("X", declare.declarations().get(0).name());
        assertEquals(null, declare.declarations().get(0).initializer());
    }

    @Test
    void parse_declareWithInitializer_returnsLiteralInitializer() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=4
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);

        assertEquals(1, program.statements().size());
        assertTrue(program.statements().get(0) instanceof DeclareNode);

        DeclareNode declare = (DeclareNode) program.statements().get(0);
        assertEquals(TokenType.INT_TYPE, declare.type());
        assertEquals(1, declare.declarations().size());
        assertEquals("x", declare.declarations().get(0).name());

        var initializer = declare.declarations().get(0).initializer();
        assertTrue(initializer instanceof com.citu.lexor_interpreter.parser.ast.expression.LiteralNode);
        assertEquals(Integer.valueOf(4),
            ((com.citu.lexor_interpreter.parser.ast.expression.LiteralNode) initializer).value());
    }

    @Test
    void parse_singleAssignment_returnsAssignStatement() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x
                x=4
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);

        assertEquals(2, program.statements().size());
        assertTrue(program.statements().get(0) instanceof DeclareNode);
        assertTrue(program.statements().get(1) instanceof com.citu.lexor_interpreter.parser.ast.AssignNode);

        var assign = (com.citu.lexor_interpreter.parser.ast.AssignNode) program.statements().get(1);
        assertEquals("x", assign.variableName());
        assertTrue(assign.value() instanceof com.citu.lexor_interpreter.parser.ast.expression.LiteralNode);
        assertEquals(Integer.valueOf(4),
            ((com.citu.lexor_interpreter.parser.ast.expression.LiteralNode) assign.value()).value());
    }

    @Test
    void parse_chainedAssignment_expandsIntoMultipleAssignStatements() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x, y
                x=y=4
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);

        // 1 declare + 2 assignment statements (y=4 then x=y)
        assertEquals(3, program.statements().size());
        assertTrue(program.statements().get(0) instanceof DeclareNode);
        assertTrue(program.statements().get(1) instanceof com.citu.lexor_interpreter.parser.ast.AssignNode);
        assertTrue(program.statements().get(2) instanceof com.citu.lexor_interpreter.parser.ast.AssignNode);

        var assign1 = (com.citu.lexor_interpreter.parser.ast.AssignNode) program.statements().get(1);
        assertEquals("y", assign1.variableName());
        assertTrue(assign1.value() instanceof com.citu.lexor_interpreter.parser.ast.expression.LiteralNode);
        assertEquals(Integer.valueOf(4),
            ((com.citu.lexor_interpreter.parser.ast.expression.LiteralNode) assign1.value()).value());

        var assign2 = (com.citu.lexor_interpreter.parser.ast.AssignNode) program.statements().get(2);
        assertEquals("x", assign2.variableName());
        assertTrue(assign2.value() instanceof com.citu.lexor_interpreter.parser.ast.expression.VariableNode);
        assertEquals("y",
            ((com.citu.lexor_interpreter.parser.ast.expression.VariableNode) assign2.value()).name());
    }

    @Test
    void parse_printConcatAndNewlineDollar_usesNewlineNodeForDollar() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x=1
                x=5
                PRINT: x & $ & "last"
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);

        // declare + assignment + print
        assertEquals(3, program.statements().size());
        assertTrue(program.statements().get(2) instanceof com.citu.lexor_interpreter.parser.ast.PrintNode);

        var print = (com.citu.lexor_interpreter.parser.ast.PrintNode) program.statements().get(2);
        assertEquals(3, print.expressions().size());

        assertTrue(print.expressions().get(0) instanceof com.citu.lexor_interpreter.parser.ast.expression.VariableNode);
        assertEquals("x",
            ((com.citu.lexor_interpreter.parser.ast.expression.VariableNode) print.expressions().get(0)).name());

        assertTrue(print.expressions().get(1) instanceof com.citu.lexor_interpreter.parser.ast.expression.NewlineNode);

        assertTrue(print.expressions().get(2) instanceof com.citu.lexor_interpreter.parser.ast.expression.LiteralNode);
        assertEquals("last",
            ((com.citu.lexor_interpreter.parser.ast.expression.LiteralNode) print.expressions().get(2)).value());
    }

    @Test
    void parse_groupedExpression_parenthesesAreAccepted() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x
                x=(4)
                END SCRIPT
            """;

        ProgramNode program = parseProgram(source);
        assertEquals(2, program.statements().size());
        assertTrue(program.statements().get(1) instanceof com.citu.lexor_interpreter.parser.ast.AssignNode);

        var assign = (com.citu.lexor_interpreter.parser.ast.AssignNode) program.statements().get(1);
        assertTrue(assign.value() instanceof com.citu.lexor_interpreter.parser.ast.expression.LiteralNode);
        assertEquals(Integer.valueOf(4),
            ((com.citu.lexor_interpreter.parser.ast.expression.LiteralNode) assign.value()).value());
    }

    @Test
    void parse_missingStartScript_throwsParserException() {
        String source = """
                SCRIPT AREA
                END SCRIPT
            """;

        ParserException ex = assertThrows(ParserException.class, () -> parseProgram(source));
        assertTrue(ex.getMessage().contains("expected START SCRIPT"));
    }

    @Test
    void parse_printMissingColon_throwsParserException() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x
                PRINT x
                END SCRIPT
            """;

        ParserException ex = assertThrows(ParserException.class, () -> parseProgram(source));
        assertTrue(ex.getMessage().contains("expected ':' after PRINT"));
    }

    @Test
    void parse_declareAfterExecutable_throwsParserException() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x
                x=4
                DECLARE INT y
                END SCRIPT
            """;

        ParserException ex = assertThrows(ParserException.class, () -> parseProgram(source));
        assertTrue(ex.getMessage().contains("DECLARE statements must appear immediately"));
    }

    @Test
    void parse_assignmentMissingRhs_throwsParserException() {
        String source = """
                SCRIPT AREA
                START SCRIPT
                DECLARE INT x
                x=
                END SCRIPT
            """;

        ParserException ex = assertThrows(ParserException.class, () -> parseProgram(source));
        assertTrue(ex.getMessage().contains("expected expression"));
    }
    
}

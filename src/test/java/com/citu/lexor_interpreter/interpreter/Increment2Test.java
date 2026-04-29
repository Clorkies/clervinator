package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Increment2Test {

    private final InterpreterService service = new InterpreterService();

    private String run(String code) {
        ExecuteResponse response = service.execute(code);
        assertNull(response.error(), "Unexpected error: " + response.error());
        return response.output();
    }

    private String run(String code, List<String> inputs) {
        ExecuteResponse response = service.execute(code, inputs);
        assertNull(response.error(), "Unexpected error: " + response.error());
        return response.output();
    }

    private void expectError(String code) {
        ExecuteResponse response = service.execute(code);
        assertNotNull(response.error(), "Expected an error but got output: " + response.output());
    }

    // Arithmetic
    @Test
    void testAddition() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=5
            PRINT: x+y
            END SCRIPT
            """;
        assertEquals("15", run(code));
    }

    @Test
    void testSubtraction() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=3
            PRINT: x-y
            END SCRIPT
            """;
        assertEquals("7", run(code));
    }

    @Test
    void testMultiplication() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=4, y=3
            PRINT: x*y
            END SCRIPT
            """;
        assertEquals("12", run(code));
    }

    @Test
    void testDivision() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=3
            PRINT: x/y
            END SCRIPT
            """;
        assertEquals("3", run(code));
    }

    @Test
    void testModulo() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=3
            PRINT: x%y
            END SCRIPT
            """;
        assertEquals("1", run(code));
    }

    // Operator precedence
    @Test
    void testPrecedence_multiplyBeforeAdd() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=2, y=3, z=4
            PRINT: x+y*z
            END SCRIPT
            """;
        assertEquals("14", run(code));
    }

    @Test
    void testPrecedence_parenthesesOverride() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=2, y=3, z=4
            PRINT: (x+y)*z
            END SCRIPT
            """;
        assertEquals("20", run(code));
    }

    // Unary
    @Test
    void testUnaryMinus() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=5
            PRINT: -x
            END SCRIPT
            """;
        assertEquals("-5", run(code));
    }

    @Test
    void testUnaryNot() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            PRINT: NOT t
            END SCRIPT
            """;
        assertEquals("FALSE", run(code));
    }

    // Comparison
    @Test
    void testComparison_greaterThan() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=5
            PRINT: x>y
            END SCRIPT
            """;
        assertEquals("TRUE", run(code));
    }

    @Test
    void testComparison_equal() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=5, y=5
            PRINT: x==y
            END SCRIPT
            """;
        assertEquals("TRUE", run(code));
    }

    @Test
    void testComparison_notEqual() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=5, y=3
            PRINT: x<>y
            END SCRIPT
            """;
        assertEquals("TRUE", run(code));
    }

    // Logical
    @Test
    void testLogical_and() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL a="TRUE", b="FALSE"
            PRINT: a AND b
            END SCRIPT
            """;
        assertEquals("FALSE", run(code));
    }

    @Test
    void testLogical_or() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL a="TRUE", b="FALSE"
            PRINT: a OR b
            END SCRIPT
            """;
        assertEquals("TRUE", run(code));
    }

    // SCAN
    @Test
    void testScan_intInput() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x
            SCAN: x
            PRINT: x
            END SCRIPT
            """;
        assertEquals("42", run(code, List.of("42")));
    }

    @Test
    void testScan_multipleInputs() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT a, b
            SCAN: a
            SCAN: b
            PRINT: a+b
            END SCRIPT
            """;
        assertEquals("15", run(code, List.of("10", "5")));
    }

    // Float arithmetic
    @Test
    void testFloat_addition() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE FLOAT x=2.5, y=1.5
            PRINT: x+y
            END SCRIPT
            """;
        assertEquals("4", run(code));
    }

    // Division by zero
    @Test
    void testDivisionByZero() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=0
            PRINT: x/y
            END SCRIPT
            """;
        expectError(code);
    }

    // Complex expression
    @Test
    void testComplexExpression() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, y=5, z=2
            PRINT: (x+y)*z-3
            END SCRIPT
            """;
        assertEquals("27", run(code));
    }

    @Test
    void testEscapeCodes_TC11_printsBracketsAroundValue() {
        // TC-11: `PRINT: [[] & xyz & []]` should produce `[<value>]`
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT xyz=60
            PRINT: [[] & xyz & []]
            END SCRIPT
            """;

        assertEquals("[60]", run(code));
    }
}

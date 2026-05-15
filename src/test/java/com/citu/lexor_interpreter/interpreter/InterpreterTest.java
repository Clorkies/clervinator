package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private final InterpreterService service = new InterpreterService();

    private String run(String code) {
        ExecuteResponse response = service.execute(code);
        assertNull(response.error(), "Unexpected error: " + response.error());
        return response.output();
    }

    private void expectError(String code) {
        ExecuteResponse response = service.execute(code);
        assertNotNull(response.error(), "Expected an error but got output: " + response.output());
    }

    // Strong typing: FLOAT variable must not accept INT literal
    @Test
    void testStrongTyping_intAssignedToFloat_throwsError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE FLOAT g
            g=4
            END SCRIPT
            """;
        expectError(code);
    }

    // Strong typing: FLOAT variable must not accept BOOL
    @Test
    void testStrongTyping_boolAssignedToInt_throwsError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x
            x="TRUE"
            END SCRIPT
            """;
        expectError(code);
    }

    // Strong typing: CHAR variable must not accept INT
    @Test
    void testStrongTyping_intAssignedToChar_throwsError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE CHAR c
            c=5
            END SCRIPT
            """;
        expectError(code);
    }

    // Valid: INT variable accepts INT literal
    @Test
    void testTyping_intAcceptsIntLiteral() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10
            PRINT: x
            END SCRIPT
            """;
        assertEquals("10", run(code));
    }

    // Valid: FLOAT variable accepts FLOAT literal
    @Test
    void testTyping_floatAcceptsFloatLiteral() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE FLOAT x=3.14
            PRINT: x
            END SCRIPT
            """;
        assertEquals("3.14", run(code));
    }
}

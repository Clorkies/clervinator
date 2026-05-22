package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Increment4Test {

    private final InterpreterService service = new InterpreterService();

    private String run(String code) {
        ExecuteResponse response = service.execute(code);
        assertNull(response.error(), "Unexpected error: " + response.error());
        return response.output();
    }

    private String expectError(String code) {
        ExecuteResponse response = service.execute(code);
        assertNotNull(response.error(), "Expected an error but got output: " + response.output());
        return response.error();
    }

    // ==================================================================
    // GROUP A — Basic FOR Loop (Happy Path)
    // ==================================================================

    @Test
    void for_countUp_printsValues() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<4, i=i+1)
            START FOR
            PRINT: i & $
            END FOR
            END SCRIPT
            """;
        assertEquals("1\n2\n3\n", run(code));
    }

    @Test
    void for_countDown_printsValues() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=3, i>0, i=i-1)
            START FOR
            PRINT: i & $
            END FOR
            END SCRIPT
            """;
        assertEquals("3\n2\n1\n", run(code));
    }

    @Test
    void for_accumulateSum() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, total
            total=0
            FOR (i=1, i<6, i=i+1)
            START FOR
            total=total+i
            END FOR
            PRINT: total
            END SCRIPT
            """;
        assertEquals("15", run(code));
    }

    @Test
    void for_zeroIterations_conditionFalseFromStart() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=10, i<5, i=i+1)
            START FOR
            PRINT: i
            END FOR
            PRINT: "done"
            END SCRIPT
            """;
        assertEquals("done", run(code));
    }

    @Test
    void for_exactlyOneIteration() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<2, i=i+1)
            START FOR
            PRINT: "once"
            END FOR
            END SCRIPT
            """;
        assertEquals("once", run(code));
    }

    @Test
    void for_multiplicationInUpdate() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<100, i=i*2)
            START FOR
            PRINT: i & $
            END FOR
            END SCRIPT
            """;
        assertEquals("1\n2\n4\n8\n16\n32\n64\n", run(code));
    }

    // ==================================================================
    // GROUP B — Basic REPEAT WHEN (Happy Path)
    // ==================================================================

    @Test
    void repeatWhen_countUp() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i<4)
            START REPEAT
            PRINT: i & $
            i=i+1
            END REPEAT
            END SCRIPT
            """;
        assertEquals("1\n2\n3\n", run(code));
    }

    @Test
    void repeatWhen_zeroIterations_conditionFalseFromStart() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=10
            REPEAT WHEN (i<5)
            START REPEAT
            PRINT: i
            END REPEAT
            PRINT: "done"
            END SCRIPT
            """;
        assertEquals("done", run(code));
    }

    @Test
    void repeatWhen_accumulateSum() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, total
            i=1
            total=0
            REPEAT WHEN (i<6)
            START REPEAT
            total=total+i
            i=i+1
            END REPEAT
            PRINT: total
            END SCRIPT
            """;
        assertEquals("15", run(code));
    }

    @Test
    void repeatWhen_boolVariableAsCondition() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT count
            DECLARE BOOL running
            count=0
            running="TRUE"
            REPEAT WHEN (running)
            START REPEAT
            count=count+1
            running=(count<3)
            END REPEAT
            PRINT: count
            END SCRIPT
            """;
        assertEquals("3", run(code));
    }

    // ==================================================================
    // GROUP C — Nested Loops
    // ==================================================================

    @Test
    void nested_forInsideFor_multiplicationTable() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, j, prod
            FOR (i=1, i<4, i=i+1)
            START FOR
            FOR (j=1, j<4, j=j+1)
            START FOR
            prod=i*j
            PRINT: prod & " "
            END FOR
            PRINT: $
            END FOR
            END SCRIPT
            """;
        assertEquals("1 2 3 \n2 4 6 \n3 6 9 \n", run(code));
    }

    @Test
    void nested_repeatWhenInsideFor() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, j
            FOR (i=1, i<3, i=i+1)
            START FOR
            j=1
            REPEAT WHEN (j<3)
            START REPEAT
            PRINT: i & "x" & j & $
            j=j+1
            END REPEAT
            END FOR
            END SCRIPT
            """;
        assertEquals("1x1\n1x2\n2x1\n2x2\n", run(code));
    }

    @Test
    void nested_forInsideRepeatWhen() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT outer, i
            outer=2
            REPEAT WHEN (outer>0)
            START REPEAT
            FOR (i=1, i<3, i=i+1)
            START FOR
            PRINT: outer & ":" & i & $
            END FOR
            outer=outer-1
            END REPEAT
            END SCRIPT
            """;
        assertEquals("2:1\n2:2\n1:1\n1:2\n", run(code));
    }

    @Test
    void nested_repeatInsideRepeat_doubleNested() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, j
            i=1
            REPEAT WHEN (i<3)
            START REPEAT
            j=1
            REPEAT WHEN (j<3)
            START REPEAT
            PRINT: i & j & $
            j=j+1
            END REPEAT
            i=i+1
            END REPEAT
            END SCRIPT
            """;
        assertEquals("11\n12\n21\n22\n", run(code));
    }

    @Test
    void nested_threeLevelsNesting() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i, j, k, count
            count=0
            FOR (i=1, i<3, i=i+1)
            START FOR
            FOR (j=1, j<3, j=j+1)
            START FOR
            FOR (k=1, k<3, k=k+1)
            START FOR
            count=count+1
            END FOR
            END FOR
            END FOR
            PRINT: count
            END SCRIPT
            """;
        assertEquals("8", run(code));
    }

    // ==================================================================
    // GROUP D — Mixed Control Flow
    // ==================================================================

    @Test
    void mixed_ifInsideForBody() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<6, i=i+1)
            START FOR
            IF (i==3)
            START IF
            PRINT: "three" & $
            END IF
            END FOR
            END SCRIPT
            """;
        assertEquals("three\n", run(code));
    }

    @Test
    void mixed_ifElseInsideRepeatWhenBody() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i<4)
            START REPEAT
            IF (i==2)
            START IF
            PRINT: "two" & $
            END IF
            ELSE
            START IF
            PRINT: "other" & $
            END IF
            i=i+1
            END REPEAT
            END SCRIPT
            """;
        assertEquals("other\ntwo\nother\n", run(code));
    }

    @Test
    void mixed_forLoopInsideIfBranch() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            DECLARE BOOL flag
            flag="TRUE"
            IF (flag)
            START IF
            FOR (i=1, i<4, i=i+1)
            START FOR
            PRINT: i & $
            END FOR
            END IF
            END SCRIPT
            """;
        assertEquals("1\n2\n3\n", run(code));
    }

    // ==================================================================
    // GROUP E — Error Cases
    // ==================================================================

    @Test
    void error_declareInsideForBody_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<3, i=i+1)
            START FOR
            DECLARE INT x
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE statements must appear immediately"), "Got: " + error);
    }

    @Test
    void error_declareInForInitialization_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            FOR (DECLARE INT i=1, i<3, i=i+1)
            START FOR
            PRINT: i
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE is not allowed in FOR"), "Got: " + error);
    }

    @Test
    void error_declareInForUpdate_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<3, DECLARE INT x=1)
            START FOR
            PRINT: i
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE is not allowed in FOR"), "Got: " + error);
    }

    @Test
    void error_declareInsideRepeatWhenBody_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i<3)
            START REPEAT
            DECLARE INT x
            i=i+1
            END REPEAT
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE statements must appear immediately"), "Got: " + error);
    }

    @Test
    void error_nonBoolConditionInFor_runtimeError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i+1, i=i+1)
            START FOR
            PRINT: i
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.toLowerCase().contains("bool") || error.toLowerCase().contains("condition"), "Got: " + error);
    }

    @Test
    void error_nonBoolConditionInRepeatWhen_runtimeError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i+1)
            START REPEAT
            i=i+1
            END REPEAT
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.toLowerCase().contains("bool") || error.toLowerCase().contains("condition"), "Got: " + error);
    }

    @Test
    void error_missingStartFor_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<3, i=i+1)
            PRINT: i
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected START FOR"), "Got: " + error);
    }

    @Test
    void error_missingEndFor_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1, i<3, i=i+1)
            START FOR
            PRINT: i
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected END FOR"), "Got: " + error);
    }

    @Test
    void error_missingStartRepeat_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i<3)
            PRINT: i
            END REPEAT
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected START REPEAT"), "Got: " + error);
    }

    @Test
    void error_missingEndRepeat_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT WHEN (i<3)
            START REPEAT
            PRINT: i
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected END REPEAT"), "Got: " + error);
    }

    @Test
    void error_missingCommaInForHeader_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            FOR (i=1 i<3 i=i+1)
            START FOR
            PRINT: i
            END FOR
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected ','"), "Got: " + error);
    }

    @Test
    void error_missingWhenAfterRepeat_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT i
            i=1
            REPEAT (i<3)
            START REPEAT
            PRINT: i
            END REPEAT
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected WHEN"), "Got: " + error);
    }
}

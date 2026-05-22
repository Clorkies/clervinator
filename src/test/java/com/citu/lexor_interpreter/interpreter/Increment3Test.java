package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.service.InterpreterService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Increment 3 — Conditional Flow: IF / ELSE IF / ELSE
 *
 * <p>Tests are organised into five groups:
 * <ul>
 *   <li>Group A – IF only (happy path)</li>
 *   <li>Group B – IF-ELSE (happy path)</li>
 *   <li>Group C – IF / ELSE IF / ELSE chains (happy path)</li>
 *   <li>Group D – Deep nesting (happy path)</li>
 *   <li>Group E – Error and edge cases</li>
 * </ul>
 */
class Increment3Test {

    private final InterpreterService service = new InterpreterService();

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    /** Runs code and asserts no error; returns the output string. */
    private String run(String code) {
        ExecuteResponse response = service.execute(code);
        assertNull(response.error(), "Unexpected error: " + response.error());
        return response.output();
    }

    /** Runs code and asserts an error is present; returns the error message. */
    private String expectError(String code) {
        ExecuteResponse response = service.execute(code);
        assertNotNull(response.error(), "Expected an error but got output: " + response.output());
        return response.error();
    }

    // ==================================================================
    // GROUP A — IF only
    // ==================================================================

    /** TC-A1: Condition is TRUE — body executes. */
    @Test
    void if_conditionTrue_bodyExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            PRINT: "yes"
            END IF
            END SCRIPT
            """;
        assertEquals("yes", run(code));
    }

    /** TC-A2: Condition is FALSE — body is skipped, output is empty. */
    @Test
    void if_conditionFalse_bodySkipped() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="FALSE"
            IF (t)
            START IF
            PRINT: "yes"
            END IF
            END SCRIPT
            """;
        assertEquals("", run(code));
    }

    /** TC-A3: Inline comparison evaluates to TRUE — body executes. */
    @Test
    void if_comparisonConditionTrue_bodyExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10
            IF (x > 5)
            START IF
            PRINT: "big"
            END IF
            END SCRIPT
            """;
        assertEquals("big", run(code));
    }

    /** TC-A4: Inline comparison evaluates to FALSE — body skipped. */
    @Test
    void if_comparisonConditionFalse_bodySkipped() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=3
            IF (x > 5)
            START IF
            PRINT: "big"
            END IF
            END SCRIPT
            """;
        assertEquals("", run(code));
    }

    /** TC-A5: Multiple statements inside the IF body — all execute. */
    @Test
    void if_multipleStatementsInBody_allExecute() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            DECLARE INT x=0
            IF (t)
            START IF
            x=42
            PRINT: x
            END IF
            END SCRIPT
            """;
        assertEquals("42", run(code));
    }

    /** TC-A6: Compound AND condition — both sides true, body executes. */
    @Test
    void if_logicalAndCondition_bothTrueExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT a=10, b=5
            IF (a > b AND b > 0)
            START IF
            PRINT: "ok"
            END IF
            END SCRIPT
            """;
        assertEquals("ok", run(code));
    }

    // ==================================================================
    // GROUP B — IF-ELSE
    // ==================================================================

    /** TC-B1: Condition TRUE — IF branch taken, ELSE branch skipped. */
    @Test
    void ifElse_conditionTrue_ifBranchExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            PRINT: "if"
            END IF
            ELSE
            START IF
            PRINT: "else"
            END IF
            END SCRIPT
            """;
        assertEquals("if", run(code));
    }

    /** TC-B2: Condition FALSE — ELSE branch taken. */
    @Test
    void ifElse_conditionFalse_elseBranchExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="FALSE"
            IF (t)
            START IF
            PRINT: "if"
            END IF
            ELSE
            START IF
            PRINT: "else"
            END IF
            END SCRIPT
            """;
        assertEquals("else", run(code));
    }

    /** TC-B3: Mutation inside IF branch is applied; ELSE branch is not. */
    @Test
    void ifElse_mutationInIfBranch_notLeakedToElse() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=0
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            x=1
            END IF
            ELSE
            START IF
            x=99
            END IF
            PRINT: x
            END SCRIPT
            """;
        assertEquals("1", run(code));
    }

    /** TC-B4: Mutation inside ELSE branch is applied; IF branch is not. */
    @Test
    void ifElse_mutationInElseBranch_notLeakedToIf() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=0
            DECLARE BOOL t="FALSE"
            IF (t)
            START IF
            x=1
            END IF
            ELSE
            START IF
            x=99
            END IF
            PRINT: x
            END SCRIPT
            """;
        assertEquals("99", run(code));
    }

    /** TC-B5: Comparison yields FALSE — else branch ("not equal") executes. */
    @Test
    void ifElse_comparisonConditionFalse_elseBranchTaken() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT a=5, b=10
            IF (a == b)
            START IF
            PRINT: "equal"
            END IF
            ELSE
            START IF
            PRINT: "not equal"
            END IF
            END SCRIPT
            """;
        assertEquals("not equal", run(code));
    }

    /** TC-B6: NOT operator inverts a FALSE BOOL — IF branch executes. */
    @Test
    void ifElse_notOperatorCondition_ifBranchTaken() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL f="FALSE"
            IF (NOT f)
            START IF
            PRINT: "inverted"
            END IF
            ELSE
            START IF
            PRINT: "not inverted"
            END IF
            END SCRIPT
            """;
        assertEquals("inverted", run(code));
    }

    // ==================================================================
    // GROUP C — IF / ELSE IF / ELSE chains
    // ==================================================================

    /** TC-C1: First condition TRUE — first branch executes. */
    @Test
    void elseIf_firstConditionTrue_firstBranchExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=1
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            PRINT: "two"
            END IF
            ELSE
            START IF
            PRINT: "other"
            END IF
            END SCRIPT
            """;
        assertEquals("one", run(code));
    }

    /** TC-C2: Second condition TRUE — second branch executes. */
    @Test
    void elseIf_secondConditionTrue_secondBranchExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=2
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            PRINT: "two"
            END IF
            ELSE
            START IF
            PRINT: "other"
            END IF
            END SCRIPT
            """;
        assertEquals("two", run(code));
    }

    /** TC-C3: No condition matches — ELSE branch executes. */
    @Test
    void elseIf_noConditionMatches_elseBranchExecutes() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=9
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            PRINT: "two"
            END IF
            ELSE
            START IF
            PRINT: "other"
            END IF
            END SCRIPT
            """;
        assertEquals("other", run(code));
    }

    /** TC-C4: Four ELSE IF clauses — middle match (x==3) fires exactly once. */
    @Test
    void elseIf_multipleElseIf_middleConditionTrue() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=3
            IF (x == 1)
            START IF
            PRINT: "A"
            END IF
            ELSE IF (x == 2)
            START IF
            PRINT: "B"
            END IF
            ELSE IF (x == 3)
            START IF
            PRINT: "C"
            END IF
            ELSE IF (x == 4)
            START IF
            PRINT: "D"
            END IF
            ELSE
            START IF
            PRINT: "E"
            END IF
            END SCRIPT
            """;
        assertEquals("C", run(code));
    }

    /** TC-C5: ELSE IF chain with no trailing ELSE — no match means no output. */
    @Test
    void elseIf_noTrailingElse_noMatchNoOutput() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=99
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            PRINT: "two"
            END IF
            END SCRIPT
            """;
        assertEquals("", run(code));
    }

    // ==================================================================
    // GROUP D — Deep Nesting
    // ==================================================================

    /** TC-D1: IF nested inside IF body — inner condition TRUE, prints. */
    @Test
    void nested_ifInsideIfBody_innerConditionTrue() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL outer="TRUE"
            DECLARE BOOL inner="TRUE"
            IF (outer)
            START IF
            IF (inner)
            START IF
            PRINT: "deep"
            END IF
            END IF
            END SCRIPT
            """;
        assertEquals("deep", run(code));
    }

    /** TC-D2: IF nested inside IF body — inner condition FALSE, body skipped. */
    @Test
    void nested_ifInsideIfBody_innerConditionFalse() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL outer="TRUE"
            DECLARE BOOL inner="FALSE"
            IF (outer)
            START IF
            IF (inner)
            START IF
            PRINT: "deep"
            END IF
            END IF
            END SCRIPT
            """;
        assertEquals("", run(code));
    }

    /** TC-D3: IF-ELSE nested inside the ELSE branch of an outer IF-ELSE. */
    @Test
    void nested_ifElseInsideElseBranch() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=5
            IF (x > 10)
            START IF
            PRINT: "big"
            END IF
            ELSE
            START IF
            IF (x > 3)
            START IF
            PRINT: "medium"
            END IF
            ELSE
            START IF
            PRINT: "small"
            END IF
            END IF
            END SCRIPT
            """;
        assertEquals("medium", run(code));
    }

    /** TC-D4: Three levels of nesting — all conditions TRUE, deepest prints. */
    @Test
    void nested_threeDeep_correctPathTaken() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL a="TRUE"
            DECLARE BOOL b="TRUE"
            DECLARE BOOL c="TRUE"
            IF (a)
            START IF
            IF (b)
            START IF
            IF (c)
            START IF
            PRINT: "3deep"
            END IF
            END IF
            END IF
            END SCRIPT
            """;
        assertEquals("3deep", run(code));
    }

    /** TC-D5: IF-ELSE nested inside an ELSE IF branch. */
    @Test
    void nested_ifElseInsideElseIfBranch() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=2
            DECLARE BOOL flag="TRUE"
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            IF (flag)
            START IF
            PRINT: "two-flag"
            END IF
            ELSE
            START IF
            PRINT: "two-no-flag"
            END IF
            END IF
            END SCRIPT
            """;
        assertEquals("two-flag", run(code));
    }

    // ==================================================================
    // GROUP E — Error and Edge Cases
    // ==================================================================

    /** TC-E1: DECLARE inside IF body — parse error. */
    @Test
    void error_declareInsideIfBlock_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            DECLARE INT x=1
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE statements must appear immediately"),
            "Expected DECLARE-placement error, got: " + error);
    }

    /** TC-E2: DECLARE inside ELSE body — parse error. */
    @Test
    void error_declareInsideElseBranch_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="FALSE"
            IF (t)
            START IF
            PRINT: "ok"
            END IF
            ELSE
            START IF
            DECLARE INT x=1
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE statements must appear immediately"),
            "Expected DECLARE-placement error, got: " + error);
    }

    /** TC-E3: DECLARE inside ELSE IF body — parse error. */
    @Test
    void error_declareInsideElseIfBranch_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=2
            IF (x == 1)
            START IF
            PRINT: "one"
            END IF
            ELSE IF (x == 2)
            START IF
            DECLARE INT y=9
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("DECLARE statements must appear immediately"),
            "Expected DECLARE-placement error, got: " + error);
    }

    /** TC-E4: INT variable used directly as condition — runtime error (must be BOOL). */
    @Test
    void error_intExpressionAsCondition_runtimeError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=5
            IF (x)
            START IF
            PRINT: "bad"
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.toLowerCase().contains("bool") || error.toLowerCase().contains("condition"),
            "Expected a BOOL/condition type error, got: " + error);
    }

    /** TC-E5: FLOAT variable used directly as condition — runtime error. */
    @Test
    void error_floatExpressionAsCondition_runtimeError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE FLOAT f=1.5
            IF (f)
            START IF
            PRINT: "bad"
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.toLowerCase().contains("bool") || error.toLowerCase().contains("condition"),
            "Expected a BOOL/condition type error, got: " + error);
    }

    /** TC-E6: START IF token missing — parse error with clear message. */
    @Test
    void error_missingStartIf_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            IF (t)
            PRINT: "bad"
            END IF
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected START IF"),
            "Expected 'expected START IF' error, got: " + error);
    }

    /** TC-E7: END IF token missing — parse error before END SCRIPT is consumed. */
    @Test
    void error_missingEndIf_parseError() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            PRINT: "oops"
            END SCRIPT
            """;
        String error = expectError(code);
        assertTrue(error.contains("expected END IF"),
            "Expected 'expected END IF' error, got: " + error);
    }

    /**
     * TC-E8: Runtime error inside an unchosen branch is completely ignored.
     * The IF condition is TRUE, so the ELSE body (which contains division-by-zero)
     * is never evaluated — the program must succeed and print "safe".
     */
    @Test
    void error_runtimeErrorInUnchosenBranch_isIgnored() {
        String code = """
            SCRIPT AREA
            START SCRIPT
            DECLARE INT x=10, z=0
            DECLARE BOOL t="TRUE"
            IF (t)
            START IF
            PRINT: "safe"
            END IF
            ELSE
            START IF
            PRINT: x/z
            END IF
            END SCRIPT
            """;
        assertEquals("safe", run(code));
    }
}

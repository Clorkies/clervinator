package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

/**
 * AST node representing a full IF / ELSE IF* / ELSE? conditional structure.
 *
 * <p>Lexor syntax (spec §Conditional):
 * <pre>
 *   IF (&lt;BOOL expression&gt;)
 *   START IF
 *       &lt;statement&gt;...
 *   END IF
 *   [ELSE IF (&lt;BOOL expression&gt;)
 *   START IF
 *       &lt;statement&gt;...
 *   END IF]*
 *   [ELSE
 *   START IF
 *       &lt;statement&gt;...
 *   END IF]
 * </pre>
 *
 * @param branches   One {@link Branch} for the leading IF clause plus one per
 *                   ELSE IF clause, in source order. Never null or empty.
 * @param elseBranch Statements for the trailing ELSE body, or {@code null}
 *                   when no ELSE clause is present.
 */
public record IfNode(
        List<Branch> branches,
        List<StatementNode> elseBranch
) implements StatementNode {

    /**
     * A single guarded branch: the boolean condition expression and the
     * ordered list of statements to execute when that condition is TRUE.
     */
    public record Branch(ExpressionNode condition, List<StatementNode> body) {}
}

package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

/**
 * AST node representing a SWITCH statement.
 *
 * <pre>
 *   SWITCH (&lt;expression&gt;)
 *   START SWITCH
 *   CASE &lt;expression&gt;:
 *       &lt;statement&gt;...
 *   DEFAULT:
 *       &lt;statement&gt;...
 *   END SWITCH
 * </pre>
 */
public record SwitchNode(
        ExpressionNode condition,
        List<CaseBranch> cases,
        List<StatementNode> defaultBranch
) implements StatementNode {

    /**
     * A single case branch: the matching condition expression and the
     * ordered list of statements to execute when matched.
     */
    public record CaseBranch(ExpressionNode matchExpression, List<StatementNode> body) {}
}
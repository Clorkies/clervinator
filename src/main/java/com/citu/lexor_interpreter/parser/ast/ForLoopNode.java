package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

public record ForLoopNode(
    AssignNode initialization,
    ExpressionNode condition,
    AssignNode update,
    List<StatementNode> body
) implements StatementNode {}

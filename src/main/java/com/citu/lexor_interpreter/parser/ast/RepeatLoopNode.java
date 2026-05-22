package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

public record RepeatLoopNode(
    ExpressionNode condition,
    List<StatementNode> body
) implements StatementNode {}

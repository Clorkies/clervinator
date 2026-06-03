package com.citu.lexor_interpreter.parser.ast;

public record IndexAssignNode(String arrayName, ExpressionNode index, ExpressionNode value) implements StatementNode {
}

package com.citu.lexor_interpreter.parser.ast;

public record AssignNode(String variableName, ExpressionNode value) implements StatementNode {
}

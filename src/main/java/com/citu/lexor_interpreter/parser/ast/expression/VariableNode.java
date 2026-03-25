package com.citu.lexor_interpreter.parser.ast.expression;

import com.citu.lexor_interpreter.parser.ast.ExpressionNode;

public record VariableNode(String name) implements ExpressionNode {
}

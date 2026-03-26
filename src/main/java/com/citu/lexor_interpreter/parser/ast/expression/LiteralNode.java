package com.citu.lexor_interpreter.parser.ast.expression;

import com.citu.lexor_interpreter.parser.ast.ExpressionNode;

public record LiteralNode(Object value) implements ExpressionNode {
}

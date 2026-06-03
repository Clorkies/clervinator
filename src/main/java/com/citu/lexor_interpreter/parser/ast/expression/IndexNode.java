package com.citu.lexor_interpreter.parser.ast.expression;

import com.citu.lexor_interpreter.parser.ast.ExpressionNode;

public record IndexNode(String arrayName, ExpressionNode index) implements ExpressionNode {
}

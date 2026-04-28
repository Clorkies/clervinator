package com.citu.lexor_interpreter.parser.ast.expression;

import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ast.ExpressionNode;

public record BinaryExpressionNode(ExpressionNode left, TokenType operator, ExpressionNode right) implements ExpressionNode {
}

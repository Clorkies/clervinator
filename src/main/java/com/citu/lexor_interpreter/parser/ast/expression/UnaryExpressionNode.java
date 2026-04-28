package com.citu.lexor_interpreter.parser.ast.expression;

import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ast.ExpressionNode;

public record UnaryExpressionNode(TokenType operator, ExpressionNode operand) implements ExpressionNode {
}

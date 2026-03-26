package com.citu.lexor_interpreter.parser.ast;

import java.util.List;
import com.citu.lexor_interpreter.lexer.token.TokenType;

public record DeclareNode(TokenType type, List<Declaration> declarations) implements StatementNode {
    public record Declaration(String name, ExpressionNode initializer) {}
}

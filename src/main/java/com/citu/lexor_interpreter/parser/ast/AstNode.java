package com.citu.lexor_interpreter.parser.ast;

public sealed interface AstNode 
    permits StatementNode, ExpressionNode {
}

package com.citu.lexor_interpreter.parser.ast;


public sealed interface StatementNode extends AstNode
    permits ProgramNode, DeclareNode, AssignNode, PrintNode {
}

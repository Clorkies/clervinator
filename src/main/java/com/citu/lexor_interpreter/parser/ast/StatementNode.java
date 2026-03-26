package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

public sealed interface StatementNode extends AstNode
    permits ProgramNode, DeclareNode, AssignNode, PrintNode {
}

package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

public record ProgramNode(List<StatementNode> statements) implements StatementNode {
}

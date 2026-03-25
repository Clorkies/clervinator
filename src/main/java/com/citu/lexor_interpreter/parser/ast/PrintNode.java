package com.citu.lexor_interpreter.parser.ast;

import java.util.List;

public record PrintNode(List<ExpressionNode> expressions) implements StatementNode {
}

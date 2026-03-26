package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.parser.ast.*;
import com.citu.lexor_interpreter.parser.ast.expression.*;
import java.util.stream.Collectors;

public class Interpreter {
    private final Environment environment = new Environment();
    private final StringBuilder output = new StringBuilder();

    public String interpret(ProgramNode program) {
        output.setLength(0);
        for (StatementNode statement : program.statements()) {
            execute(statement);
        }
        return output.toString();
    }

    private void execute(StatementNode statement) {
        if (statement instanceof DeclareNode node) {
            executeDeclare(node);
        } else if (statement instanceof AssignNode node) {
            executeAssign(node);
        } else if (statement instanceof PrintNode node) {
            executePrint(node);
        }
    }

    private void executeDeclare(DeclareNode node) {
        for (DeclareNode.Declaration decl : node.declarations()) {
            environment.declare(decl.name(), node.type());
            if (decl.initializer() != null) {
                Object value = evaluate(decl.initializer());
                environment.assign(decl.name(), value);
            }
        }
    }

    private void executeAssign(AssignNode node) {
        Object value = evaluate(node.value());
        environment.assign(node.variableName(), value);
    }

    private void executePrint(PrintNode node) {
        for (ExpressionNode expr : node.expressions()) {
            if (expr instanceof NewlineNode) {
                output.append("\n");
            } else {
                output.append(evaluate(expr).toString());
            }
        }
    }

    private Object evaluate(ExpressionNode expression) {
        if (expression instanceof LiteralNode node) {
            return node.value();
        } else if (expression instanceof VariableNode node) {
            return environment.get(node.name());
        }
        return null;
    }
}

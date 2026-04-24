package com.citu.lexor_interpreter.interpreter;

import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ParserException;
import com.citu.lexor_interpreter.parser.ast.*;
import com.citu.lexor_interpreter.parser.ast.expression.*;

import java.util.List;
import java.util.function.Supplier;

public class Interpreter {
    private final Environment environment = new Environment();
    private final StringBuilder output = new StringBuilder();
    private List<String> inputQueue;
    private int inputIndex;

    // Entry point
    public String interpret(ProgramNode program) {
        return interpret(program, List.of());
    }

    public String interpret(ProgramNode program, List<String> inputs) {
        output.setLength(0);
        inputQueue = inputs;
        inputIndex = 0;
        for (StatementNode statement : program.statements()) {
            execute(statement);
        }
        return output.toString();
    }

    // Statement dispatch
    private void execute(StatementNode statement) {
        if (statement instanceof DeclareNode node) {
            executeDeclare(node);
        } else if (statement instanceof AssignNode node) {
            executeAssign(node);
        } else if (statement instanceof PrintNode node) {
            executePrint(node);
        } else if (statement instanceof ScanNode node) {
            executeScan(node);
        }
    }

    // DECLARE
    private void executeDeclare(DeclareNode node) {
        for (DeclareNode.Declaration decl : node.declarations()) {
            environment.declare(decl.name(), node.type());
            if (decl.initializer() != null) {
                Object value = evaluate(decl.initializer());
                environment.assign(decl.name(), value);
            }
        }
    }

    // Assignment
    private void executeAssign(AssignNode node) {
        Object value = evaluate(node.value());
        environment.assign(node.variableName(), value);
    }

    // PRINT
    private void executePrint(PrintNode node) {
        for (ExpressionNode expr : node.expressions()) {
            if (expr instanceof NewlineNode) {
                output.append("\n");
            } else {
                output.append(stringify(evaluate(expr)));
            }
        }
    }

    // SCAN
    private void executeScan(ScanNode node) {
        if (inputQueue == null || inputIndex >= inputQueue.size()) {
            throw new ParserException("SCAN requires input but none was provided for variable '" + node.variableName() + "'");
        }
        String raw = inputQueue.get(inputIndex++);
        Object converted = convertInput(node.variableName(), raw);
        environment.assign(node.variableName(), converted);
    }

    private Object convertInput(String varName, String raw) {
        TokenType type = environment.getType(varName);
        try {
            return switch (type) {
                case INT_TYPE -> Integer.parseInt(raw.trim());
                case FLOAT_TYPE -> Double.parseDouble(raw.trim());
                case CHAR_TYPE -> {
                    if (raw.length() != 1) throw new ParserException("SCAN: expected single character for CHAR variable '" + varName + "'");
                    yield raw.charAt(0);
                }
                case BOOL_TYPE -> {
                    if ("TRUE".equals(raw.trim())) yield true;
                    if ("FALSE".equals(raw.trim())) yield false;
                    throw new ParserException("SCAN: expected TRUE or FALSE for BOOL variable '" + varName + "'");
                }
                default -> throw new ParserException("SCAN: unsupported type for variable '" + varName + "'");
            };
        } catch (NumberFormatException e) {
            throw new ParserException("SCAN: invalid " + type + " input '" + raw + "' for variable '" + varName + "'");
        }
    }

    // Expression evaluation
    private Object evaluate(ExpressionNode expression) {
        if (expression instanceof LiteralNode node) return node.value();
        if (expression instanceof VariableNode node) return environment.get(node.name());
        if (expression instanceof UnaryExpressionNode node) return evaluateUnary(node);
        if (expression instanceof BinaryExpressionNode node) return evaluateBinary(node);
        throw new ParserException("Unknown expression type: " + expression.getClass().getSimpleName());
    }

    // Unary: NOT, -
    private Object evaluateUnary(UnaryExpressionNode node) {
        Object operand = evaluate(node.operand());
        return switch (node.operator()) {
            case NOT -> {
                if (!(operand instanceof Boolean)) throw new ParserException("NOT requires a BOOL operand");
                yield !(Boolean) operand;
            }
            case MINUS -> {
                if (operand instanceof Integer i) yield -i;
                if (operand instanceof Double d) yield -d;
                throw new ParserException("Unary minus requires a numeric operand");
            }
            default -> throw new ParserException("Unknown unary operator: " + node.operator());
        };
    }

    // Binary: arithmetic, comparison, logical
    private Object evaluateBinary(BinaryExpressionNode node) {
        Object left = evaluate(node.left());
        Object right = evaluate(node.right());

        return switch (node.operator()) {
            // Arithmetic
            case PLUS, MINUS, MULTIPLY, DIVIDE, MODULO -> evaluateArithmetic(node.operator(), left, right);
            // Comparison
            case EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_EQUAL, LESS_THAN, LESS_EQUAL -> evaluateComparison(node.operator(), left, right);
            // Logical
            case AND -> {
                if (!(left instanceof Boolean l) || !(right instanceof Boolean r))
                    throw new ParserException("AND requires BOOL operands");
                yield l && r;
            }
            case OR -> {
                if (!(left instanceof Boolean l) || !(right instanceof Boolean r))
                    throw new ParserException("OR requires BOOL operands");
                yield l || r;
            }
            default -> throw new ParserException("Unknown binary operator: " + node.operator());
        };
    }

    // Arithmetic with type promotion
    private Object evaluateArithmetic(TokenType op, Object left, Object right) {
        if (!(left instanceof Number) || !(right instanceof Number))
            throw new ParserException("Arithmetic requires numeric operands");

        boolean useDouble = (left instanceof Double || right instanceof Double);
        double l = ((Number) left).doubleValue();
        double r = ((Number) right).doubleValue();

        double result = switch (op) {
            case PLUS -> l + r;
            case MINUS -> l - r;
            case MULTIPLY -> l * r;
            case DIVIDE -> {
                if (r == 0) throw new ParserException("Division by zero");
                yield l / r;
            }
            case MODULO -> {
                if (r == 0) throw new ParserException("Modulo by zero");
                yield l % r;
            }
            default -> throw new ParserException("Unknown arithmetic operator");
        };

        if (useDouble) return result;
        return (int) result;
    }

    // Comparison
    private Object evaluateComparison(TokenType op, Object left, Object right) {
        if (left instanceof Number && right instanceof Number) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();
            return switch (op) {
                case EQUAL -> l == r;
                case NOT_EQUAL -> l != r;
                case GREATER_THAN -> l > r;
                case GREATER_EQUAL -> l >= r;
                case LESS_THAN -> l < r;
                case LESS_EQUAL -> l <= r;
                default -> throw new ParserException("Unknown comparison operator");
            };
        }

        if (op == TokenType.EQUAL) return left.equals(right);
        if (op == TokenType.NOT_EQUAL) return !left.equals(right);

        throw new ParserException("Comparison operators >, >=, <, <= require numeric operands");
    }

    // Stringify for PRINT output
    private String stringify(Object value) {
        if (value instanceof Boolean b) return b ? "TRUE" : "FALSE";
        if (value instanceof Double d) {
            if (d == Math.floor(d) && !Double.isInfinite(d)) return String.valueOf((int) d.doubleValue());
            return String.valueOf(d);
        }
        return String.valueOf(value);
    }
}

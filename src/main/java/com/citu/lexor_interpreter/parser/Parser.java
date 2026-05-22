package com.citu.lexor_interpreter.parser;

import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import com.citu.lexor_interpreter.parser.ast.AssignNode;
import com.citu.lexor_interpreter.parser.ast.DeclareNode;
import com.citu.lexor_interpreter.parser.ast.ExpressionNode;
import com.citu.lexor_interpreter.parser.ast.PrintNode;
import com.citu.lexor_interpreter.parser.ast.ScanNode;
import com.citu.lexor_interpreter.parser.ast.IfNode;
import com.citu.lexor_interpreter.parser.ast.ForLoopNode;
import com.citu.lexor_interpreter.parser.ast.RepeatLoopNode;
import com.citu.lexor_interpreter.parser.ast.StatementNode;
import com.citu.lexor_interpreter.parser.ast.expression.BinaryExpressionNode;
import com.citu.lexor_interpreter.parser.ast.expression.LiteralNode;
import com.citu.lexor_interpreter.parser.ast.expression.NewlineNode;
import com.citu.lexor_interpreter.parser.ast.expression.UnaryExpressionNode;
import com.citu.lexor_interpreter.parser.ast.expression.VariableNode;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int currentIndex;

    private Token previous() {
        return tokens.get(currentIndex - 1);
    }

    private Token peek() {
        if (currentIndex >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(currentIndex);
    }

    private Token peekNext() {
        int next = currentIndex + 1;
        if (next >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(next);
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) {
            currentIndex++;
        }
        return previous();
    }

    private boolean check(TokenType type) {
        return peek().type() == type;
    }

    private boolean match(TokenType... expectedTypes) {
        for (TokenType expected : expectedTypes) {
            if (check(expected)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(message);
    }

    private ParserException error(String message) {
        Token token = peek();
        String full = "Syntax error at " + token.position() + " - " + message
            + " (found " + token.type() + " '" + token.lexeme() + "')";
        return new ParserException(full);
    }

    private boolean isTypeToken(TokenType type) {
        return type == TokenType.INT_TYPE
            || type == TokenType.FLOAT_TYPE
            || type == TokenType.CHAR_TYPE
            || type == TokenType.BOOL_TYPE;
    }

    private void requireNextTokenOnNewLine(int statementLine, String context) {
        if (check(TokenType.EOF)) {
            return;
        }

        if (peek().position().line() == statementLine) {
            throw error("expected line break after " + context + "; only one statement is allowed per line");
        }
    }

    private Object parseTypedLiteral(Token token) {
        String raw = token.lexeme();
        return switch (token.type()) {
            case INT_LITERAL -> Integer.parseInt(raw);
            case FLOAT_LITERAL -> Double.parseDouble(raw);
            case CHAR_LITERAL, BOOL_LITERAL, STRING_LITERAL -> token.literalValue();
            default -> throw error("expected literal value");
        };
    }

    private ExpressionNode parsePrimary() {
        if (match(TokenType.INT_LITERAL, TokenType.FLOAT_LITERAL, TokenType.CHAR_LITERAL, TokenType.BOOL_LITERAL, TokenType.STRING_LITERAL)) {
            Token lit = previous();
            return new LiteralNode(parseTypedLiteral(lit));
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(previous().lexeme());
        }

        if (match(TokenType.LPAREN)) {
            ExpressionNode inner = parseExpression();
            consume(TokenType.RPAREN, "expected ')' after grouped expression");
            return inner;
        }

        throw error("expected expression");
    }

    // Expression precedence (lowest to highest)
    private ExpressionNode parseExpression() {
        return parseOr();
    }

    // OR
    private ExpressionNode parseOr() {
        ExpressionNode left = parseAnd();
        while (match(TokenType.OR)) {
            TokenType op = previous().type();
            ExpressionNode right = parseAnd();
            left = new BinaryExpressionNode(left, op, right);
        }
        return left;
    }

    // AND
    private ExpressionNode parseAnd() {
        ExpressionNode left = parseComparison();
        while (match(TokenType.AND)) {
            TokenType op = previous().type();
            ExpressionNode right = parseComparison();
            left = new BinaryExpressionNode(left, op, right);
        }
        return left;
    }

    // Comparison: ==, <>, >, >=, <, <=
    private ExpressionNode parseComparison() {
        ExpressionNode left = parseAddition();
        while (match(TokenType.EQUAL, TokenType.NOT_EQUAL, TokenType.GREATER_THAN, TokenType.GREATER_EQUAL, TokenType.LESS_THAN, TokenType.LESS_EQUAL)) {
            TokenType op = previous().type();
            ExpressionNode right = parseAddition();
            left = new BinaryExpressionNode(left, op, right);
        }
        return left;
    }

    // Addition and subtraction
    private ExpressionNode parseAddition() {
        ExpressionNode left = parseMultiplication();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            TokenType op = previous().type();
            ExpressionNode right = parseMultiplication();
            left = new BinaryExpressionNode(left, op, right);
        }
        return left;
    }

    // Multiplication, division, modulo
    private ExpressionNode parseMultiplication() {
        ExpressionNode left = parseUnary();
        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)) {
            TokenType op = previous().type();
            ExpressionNode right = parseUnary();
            left = new BinaryExpressionNode(left, op, right);
        }
        return left;
    }

    // Unary: NOT, -, +
    private ExpressionNode parseUnary() {
        if (match(TokenType.NOT)) {
            return new UnaryExpressionNode(TokenType.NOT, parseUnary());
        }
        if (match(TokenType.MINUS)) {
            return new UnaryExpressionNode(TokenType.MINUS, parseUnary());
        }
        if (match(TokenType.PLUS)) {
            return new UnaryExpressionNode(TokenType.PLUS, parseUnary());
        }
        return parsePrimary();
    }

    private DeclareNode parseDeclareStatement() {
        consume(TokenType.DECLARE, "expected DECLARE");

        Token typeToken = peek();
        if (!isTypeToken(typeToken.type())) {
            throw error("expected data type after DECLARE");
        }
        advance();

        List<DeclareNode.Declaration> declarations = new ArrayList<>();

        do {
            Token name = consume(TokenType.IDENTIFIER, "expected variable name in declaration");
            ExpressionNode initializer = null;
            if (match(TokenType.ASSIGN)) {
                initializer = parseExpression();
            }
            declarations.add(new DeclareNode.Declaration(name.lexeme(), initializer));
        } while (match(TokenType.COMMA));

        return new DeclareNode(typeToken.type(), declarations);
    }

    private PrintNode parsePrintStatement() {
        consume(TokenType.PRINT, "expected PRINT");
        consume(TokenType.COLON, "expected ':' after PRINT");

        List<ExpressionNode> expressions = new ArrayList<>();
        expressions.add(parsePrintExpression());

        while (match(TokenType.CONCAT)) {
            expressions.add(parsePrintExpression());
        }

        return new PrintNode(expressions);
    }

    private ExpressionNode parsePrintExpression() {
        if (match(TokenType.NEWLINE)) {
            return new NewlineNode();
        }
        return parseExpression();
    }

    // SCAN: variableName, variableName...
    private List<StatementNode> parseScanStatement() {
        consume(TokenType.SCAN, "expected SCAN");
        consume(TokenType.COLON, "expected ':' after SCAN");
        
        List<StatementNode> scans = new ArrayList<>();
        Token name = consume(TokenType.IDENTIFIER, "expected variable name after SCAN:");
        scans.add(new ScanNode(name.lexeme()));

        while (match(TokenType.COMMA)) {
            name = consume(TokenType.IDENTIFIER, "expected variable name after comma in SCAN statement");
            scans.add(new ScanNode(name.lexeme()));
        }
        
        return scans;
    }

    private List<StatementNode> parseAssignmentStatement() {
        List<String> names = new ArrayList<>();
        names.add(consume(TokenType.IDENTIFIER, "expected variable name on assignment").lexeme());
        consume(TokenType.ASSIGN, "expected '=' in assignment");

        while (check(TokenType.IDENTIFIER) && peekNext().type() == TokenType.ASSIGN) {
            names.add(advance().lexeme());
            consume(TokenType.ASSIGN, "expected '=' in chained assignment");
        }

        ExpressionNode rhs = parseExpression();

        List<StatementNode> statements = new ArrayList<>();
        String lastAssigned = names.get(names.size() - 1);
        statements.add(new AssignNode(lastAssigned, rhs));

        for (int i = names.size() - 2; i >= 0; i--) {
            statements.add(new AssignNode(names.get(i), new VariableNode(lastAssigned)));
            lastAssigned = names.get(i);
        }

        return statements;
    }

    private List<StatementNode> parseStatement() {
        if (check(TokenType.DECLARE)) {
            throw error("DECLARE statements must appear immediately after START SCRIPT");
        }

        if (check(TokenType.IF)) {
            return List.of(parseIfStatement());
        }

        if (check(TokenType.FOR)) {
            return List.of(parseForStatement());
        }

        if (check(TokenType.REPEAT)) {
            return List.of(parseRepeatStatement());
        }

        if (check(TokenType.PRINT)) {
            return List.of(parsePrintStatement());
        }

        if (check(TokenType.SCAN)) {
            return parseScanStatement();
        }

        if (check(TokenType.IDENTIFIER) && peekNext().type() == TokenType.ASSIGN) {
            return parseAssignmentStatement();
        }

        throw error("expected executable statement");
    }

    // -------------------------------------------------------------------------
    // Conditional: IF / ELSE IF* / ELSE?
    // -------------------------------------------------------------------------

    /**
     * Parses a complete IF / ELSE IF* / ELSE? structure.
     *
     * <pre>
     *   IF (&lt;BOOL expression&gt;)
     *   START IF
     *       &lt;statement&gt;...
     *   END IF
     *   [ELSE IF (&lt;BOOL expression&gt;)
     *   START IF
     *       &lt;statement&gt;...
     *   END IF]*
     *   [ELSE
     *   START IF
     *       &lt;statement&gt;...
     *   END IF]
     * </pre>
     */
    private IfNode parseIfStatement() {
        // -- IF header --
        consume(TokenType.IF, "expected IF");
        consume(TokenType.LPAREN, "expected '(' after IF");
        ExpressionNode condition = parseExpression();
        consume(TokenType.RPAREN, "expected ')' after IF condition");
        // START IF must begin on the next line
        requireNextTokenOnNewLine(previous().position().line(), "IF condition");

        // -- IF body --
        List<IfNode.Branch> branches = new ArrayList<>();
        List<StatementNode> body = parseBlock();
        branches.add(new IfNode.Branch(condition, body));
        // Whatever follows END IF must be on its own line
        requireNextTokenOnNewLine(previous().position().line(), "END IF");

        // -- ELSE IF* chain --
        while (check(TokenType.ELSE_IF)) {
            consume(TokenType.ELSE_IF, "expected ELSE IF");
            consume(TokenType.LPAREN, "expected '(' after ELSE IF");
            ExpressionNode elseIfCondition = parseExpression();
            consume(TokenType.RPAREN, "expected ')' after ELSE IF condition");
            requireNextTokenOnNewLine(previous().position().line(), "ELSE IF condition");

            List<StatementNode> elseIfBody = parseBlock();
            branches.add(new IfNode.Branch(elseIfCondition, elseIfBody));
            requireNextTokenOnNewLine(previous().position().line(), "END IF");
        }

        // -- Optional ELSE --
        List<StatementNode> elseBranch = null;
        if (check(TokenType.ELSE)) {
            consume(TokenType.ELSE, "expected ELSE");
            requireNextTokenOnNewLine(previous().position().line(), "ELSE");
            elseBranch = parseBlock();
            // The outer parseProgram()/parseBlock() loop enforces line break after the last END IF
        }

        return new IfNode(branches, elseBranch);
    }

    /**
     * Parses a block delimited by {@code START IF} ... {@code END IF}.
     * Used for every branch body in an IF structure (IF, ELSE IF, and ELSE
     * all share identical {@code START IF}/{@code END IF} delimiters per spec).
     *
     * <p>Stops early at {@code END_SCRIPT}, {@code ELSE}, or {@code ELSE_IF}
     * so that a missing {@code END IF} produces a clean diagnostic rather than
     * cascading into an "expected executable statement" error.
     */
    private List<StatementNode> parseBlock() {
        consume(TokenType.START_IF, "expected START IF to open block");
        requireNextTokenOnNewLine(previous().position().line(), "START IF");

        List<StatementNode> body = new ArrayList<>();

        while (!check(TokenType.END_IF)
                && !check(TokenType.END_SCRIPT)
                && !check(TokenType.ELSE)
                && !check(TokenType.ELSE_IF)
                && !isAtEnd()) {
            int statementLine = peek().position().line();
            body.addAll(parseStatement());
            requireNextTokenOnNewLine(statementLine, "statement");
        }

        consume(TokenType.END_IF, "expected END IF to close block");
        return body;
    }

    // -------------------------------------------------------------------------
    // Loop Control: FOR / REPEAT WHEN
    // -------------------------------------------------------------------------

    private ForLoopNode parseForStatement() {
        consume(TokenType.FOR, "expected FOR");
        consume(TokenType.LPAREN, "expected '(' after FOR");

        if (check(TokenType.DECLARE)) {
            throw error("DECLARE is not allowed in FOR initialization");
        }
        AssignNode initialization = parseInlineAssignment();
        consume(TokenType.COMMA, "expected ',' after FOR initialization");

        ExpressionNode condition = parseExpression();
        consume(TokenType.COMMA, "expected ',' after FOR condition");

        if (check(TokenType.DECLARE)) {
            throw error("DECLARE is not allowed in FOR update");
        }
        AssignNode update = parseInlineAssignment();

        consume(TokenType.RPAREN, "expected ')' after FOR update");
        requireNextTokenOnNewLine(previous().position().line(), "FOR header");

        List<StatementNode> body = parseForBlock();
        return new ForLoopNode(initialization, condition, update, body);
    }

    private AssignNode parseInlineAssignment() {
        Token name = consume(TokenType.IDENTIFIER, "expected variable name on assignment");
        consume(TokenType.ASSIGN, "expected '=' in assignment");
        ExpressionNode value = parseExpression();
        return new AssignNode(name.lexeme(), value);
    }

    private List<StatementNode> parseForBlock() {
        consume(TokenType.START_FOR, "expected START FOR to open FOR block");
        requireNextTokenOnNewLine(previous().position().line(), "START FOR");

        List<StatementNode> body = new ArrayList<>();

        while (!check(TokenType.END_FOR)
                && !check(TokenType.END_SCRIPT)
                && !isAtEnd()) {
            int statementLine = peek().position().line();
            body.addAll(parseStatement());
            requireNextTokenOnNewLine(statementLine, "statement");
        }

        consume(TokenType.END_FOR, "expected END FOR to close FOR block");
        return body;
    }

    private RepeatLoopNode parseRepeatStatement() {
        consume(TokenType.REPEAT, "expected REPEAT");
        consume(TokenType.WHEN, "expected WHEN after REPEAT");
        consume(TokenType.LPAREN, "expected '(' after REPEAT WHEN");
        ExpressionNode condition = parseExpression();
        consume(TokenType.RPAREN, "expected ')' after REPEAT WHEN condition");
        requireNextTokenOnNewLine(previous().position().line(), "REPEAT WHEN header");

        List<StatementNode> body = parseRepeatBlock();
        return new RepeatLoopNode(condition, body);
    }

    private List<StatementNode> parseRepeatBlock() {
        consume(TokenType.START_REPEAT, "expected START REPEAT to open REPEAT block");
        requireNextTokenOnNewLine(previous().position().line(), "START REPEAT");

        List<StatementNode> body = new ArrayList<>();

        while (!check(TokenType.END_REPEAT)
                && !check(TokenType.END_SCRIPT)
                && !isAtEnd()) {
            int statementLine = peek().position().line();
            body.addAll(parseStatement());
            requireNextTokenOnNewLine(statementLine, "statement");
        }

        consume(TokenType.END_REPEAT, "expected END REPEAT to close REPEAT block");
        return body;
    }

    public Parser(List<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            throw new ParserException("Parser requires a non-empty token list.");
        }
        this.tokens = tokens;
        this.currentIndex = 0;
    }


    public ProgramNode parseProgram() {
        List<StatementNode> statements = new ArrayList<>();

        consume(TokenType.SCRIPT_AREA, "expected SCRIPT AREA at the beginning of program");
        requireNextTokenOnNewLine(previous().position().line(), "SCRIPT AREA");
        consume(TokenType.START_SCRIPT, "expected START SCRIPT after SCRIPT AREA");
        requireNextTokenOnNewLine(previous().position().line(), "START SCRIPT");

        while (check(TokenType.DECLARE)) {
            int declarationLine = peek().position().line();
            statements.add(parseDeclareStatement());
            requireNextTokenOnNewLine(declarationLine, "declaration");
        }

        while (!check(TokenType.END_SCRIPT) && !isAtEnd()) {
            int statementLine = peek().position().line();
            statements.addAll(parseStatement());
            requireNextTokenOnNewLine(statementLine, "statement");
        }

        consume(TokenType.END_SCRIPT, "expected END SCRIPT to close program");
        consume(TokenType.EOF, "expected end of input");

        return new ProgramNode(statements);
    }


    public List<StatementNode> parseStatement(List<Token> ignoredTokens) {
        return parseStatement();
    }
    

}

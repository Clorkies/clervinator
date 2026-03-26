package com.citu.lexor_interpreter.parser;

import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import com.citu.lexor_interpreter.parser.ast.AssignNode;
import com.citu.lexor_interpreter.parser.ast.DeclareNode;
import com.citu.lexor_interpreter.parser.ast.ExpressionNode;
import com.citu.lexor_interpreter.parser.ast.PrintNode;
import com.citu.lexor_interpreter.parser.ast.StatementNode;
import com.citu.lexor_interpreter.parser.ast.expression.LiteralNode;
import com.citu.lexor_interpreter.parser.ast.expression.NewlineNode;
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

    private ExpressionNode parseExpression() {
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

        if (check(TokenType.PRINT)) {
            return List.of(parsePrintStatement());
        }

        if (check(TokenType.IDENTIFIER) && peekNext().type() == TokenType.ASSIGN) {
            return parseAssignmentStatement();
        }

        throw error("expected executable statement");
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
        consume(TokenType.START_SCRIPT, "expected START SCRIPT after SCRIPT AREA");

        while (check(TokenType.DECLARE)) {
            statements.add(parseDeclareStatement());
        }

        while (!check(TokenType.END_SCRIPT) && !isAtEnd()) {
            statements.addAll(parseStatement());
        }

        consume(TokenType.END_SCRIPT, "expected END SCRIPT to close program");
        consume(TokenType.EOF, "expected end of input");

        return new ProgramNode(statements);
    }


    public List<StatementNode> parseStatement(List<Token> ignoredTokens) {
        return parseStatement();
    }
    

}

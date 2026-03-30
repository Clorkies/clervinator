package com.citu.lexor_interpreter.service;

import com.citu.lexor_interpreter.model.ExecuteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Placeholder only
 */
import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.lexer.LexerException;
import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.parser.Parser;
import com.citu.lexor_interpreter.parser.ParserException;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import com.citu.lexor_interpreter.interpreter.Interpreter;

import java.util.ArrayList;

@Service
public class InterpreterService {

    public ExecuteResponse execute(String code) {
        if (code == null || code.isBlank()) {
            return ExecuteResponse.withError("No code provided.");
        }

        try {
            // Lexing
            Lexer lexer = new Lexer();
            List<Token> tokens = lexer.lex(code);

            // Parsing
            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parseProgram();

            // Interpreting
            Interpreter interpreter = new Interpreter();
            String result = interpreter.interpret(program);

            // Prepare tokens for response (optional but good for debugging)
            List<Object> responseTokens = new ArrayList<>(tokens);

            return ExecuteResponse.success(result, responseTokens);

        } catch (LexerException | ParserException e) {
            // Catch custom errors and return them to the user nicely
            return ExecuteResponse.withError(e.getMessage());
        } catch (Exception e) {
            // Safety: Catch unexpected system errors so the UI doesn't crash
            return ExecuteResponse.withError("System error: " + e.getClass().getSimpleName() + " - Please check your code.");
        }
    }
}


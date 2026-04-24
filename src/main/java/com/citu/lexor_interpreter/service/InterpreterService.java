package com.citu.lexor_interpreter.service;

import com.citu.lexor_interpreter.interpreter.Interpreter;
import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.lexer.LexerException;
import com.citu.lexor_interpreter.lexer.token.Token;
import com.citu.lexor_interpreter.model.ExecuteResponse;
import com.citu.lexor_interpreter.parser.Parser;
import com.citu.lexor_interpreter.parser.ParserException;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterpreterService {

    public ExecuteResponse execute(String code) {
        return execute(code, List.of());
    }

    public ExecuteResponse execute(String code, List<String> inputs) {
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
            String result = interpreter.interpret(program, inputs != null ? inputs : List.of());

            List<Object> responseTokens = new ArrayList<>(tokens);
            return ExecuteResponse.success(result, responseTokens);

        } catch (LexerException | ParserException e) {
            return ExecuteResponse.withError(e.getMessage());
        } catch (Exception e) {
            return ExecuteResponse.withError("System error: " + e.getClass().getSimpleName() + " - Please check your code.");
        }
    }
}

package com.citu.lexor_interpreter.interpreter;

import java.util.HashMap;
import java.util.Map;
import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ParserException;

public class Environment {
    private final Map<String, VariableInfo> variables = new HashMap<>();

    private record VariableInfo(TokenType type, Object value) {}

    /**
     * Declares a new variable with a default value based on its type.
     */
    public void declare(String name, TokenType type) {
        if (variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' is already declared.");
        }
        variables.put(name, new VariableInfo(type, getDefaultValue(type)));
    }

    public void assign(String name, Object value) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }

        VariableInfo info = variables.get(name);
        Object coerced = coerceToDeclaredType(info.type(), value);
        validateType(info.type(), coerced);
        variables.put(name, new VariableInfo(info.type(), coerced));
    }

    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }
        return variables.get(name).value();
    }

    // Returns the declared type of a variable
    public TokenType getType(String name) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }
        return variables.get(name).type();
    }

    private Object getDefaultValue(TokenType type) {
        return switch (type) {
            case INT_TYPE -> 0;
            case FLOAT_TYPE -> 0.0f;
            case CHAR_TYPE -> '\0';
            case BOOL_TYPE -> false;
            default -> throw new ParserException("Unsupported variable type: " + type);
        };
    }

    private Object coerceToDeclaredType(TokenType type, Object value) {
        if (type == TokenType.FLOAT_TYPE) {
            if (value instanceof Integer i) {
                return i.doubleValue();
            }
            if (value instanceof Float f) {
                return f.doubleValue();
            }
        }
        return value;
    }

    private void validateType(TokenType expected, Object value) {
        boolean valid = switch (expected) {
            case INT_TYPE -> value instanceof Integer;
            case FLOAT_TYPE -> value instanceof Float || value instanceof Double;
            case CHAR_TYPE -> value instanceof Character;
            case BOOL_TYPE -> value instanceof Boolean;
            default -> false;
        };

        if (!valid) {
            throw new ParserException("TypeError: Type mismatch: Cannot assign " + 
                value.getClass().getSimpleName() + " to " + expected);
        }
    }
}

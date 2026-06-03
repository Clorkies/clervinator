package com.citu.lexor_interpreter.interpreter;

import java.util.HashMap;
import java.util.Map;
import com.citu.lexor_interpreter.lexer.token.TokenType;
import com.citu.lexor_interpreter.parser.ParserException;

public class Environment {
    private final Map<String, VariableInfo> variables = new HashMap<>();

    private record VariableInfo(TokenType type, Object value, boolean isArray) {}

    /**
     * Declares a new variable with a default value based on its type.
     */
    public void declare(String name, TokenType type) {
        if (variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' is already declared.");
        }
        variables.put(name, new VariableInfo(type, getDefaultValue(type), false));
    }

    public void assign(String name, Object value) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }

        VariableInfo info = variables.get(name);
        Object coerced = coerceToDeclaredType(info.type(), value);
        validateType(info.type(), coerced);
        variables.put(name, new VariableInfo(info.type(), coerced, false));
    }

    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }
        VariableInfo info = variables.get(name);
        if (info.isArray()) {
            throw new ParserException("Cannot use whole array '" + name + "' as a value.");
        }
        return info.value();
    }

    // Returns the declared type of a variable
    public TokenType getType(String name) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }
        return variables.get(name).type();
    }

    // -------------------------------------------------------------------------
    // Arrays (fixed-size, single element type)
    // -------------------------------------------------------------------------

    public void declareArray(String name, TokenType elementType, int size) {
        if (variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' is already declared.");
        }
        Object[] elements = new Object[size];
        Object defaultValue = getDefaultValue(elementType);
        for (int i = 0; i < size; i++) {
            elements[i] = defaultValue;
        }
        variables.put(name, new VariableInfo(elementType, elements, true));
    }

    public Object getElement(String name, int index) {
        Object[] elements = arrayElements(name);
        checkBounds(name, index, elements.length);
        return elements[index];
    }

    public void setElement(String name, int index, Object value) {
        VariableInfo info = arrayInfo(name);
        Object[] elements = (Object[]) info.value();
        checkBounds(name, index, elements.length);
        Object coerced = coerceToDeclaredType(info.type(), value);
        validateType(info.type(), coerced);
        elements[index] = coerced;
    }

    public int getArrayLength(String name) {
        return arrayElements(name).length;
    }

    private VariableInfo arrayInfo(String name) {
        if (!variables.containsKey(name)) {
            throw new ParserException("Variable '" + name + "' has not been declared.");
        }
        VariableInfo info = variables.get(name);
        if (!info.isArray()) {
            throw new ParserException("Variable '" + name + "' is not an array.");
        }
        return info;
    }

    private Object[] arrayElements(String name) {
        return (Object[]) arrayInfo(name).value();
    }

    private void checkBounds(String name, int index, int length) {
        if (index < 0 || index >= length) {
            throw new ParserException("IndexError: index " + index
                + " out of bounds for array '" + name + "' of size " + length);
        }
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

package com.citu.lexor_interpreter.lexer.token;

import com.citu.lexor_interpreter.lexer.LexerException;

public record TokenPosition(int line, int col) {

    // line & col is 1-based index
    // int index is 0-based index, to be implemented in the future..

    public TokenPosition {
        if (line < 1 || col < 1) {
            throw new LexerException("Line and column must be greater than 0");
        }
    }

    @Override
    public String toString() {
        return line + ":" + col;
    }
    
}

/*

use index for more precise, char by char positioning, in the future..

public record TokenPosition(int line, int col, int index) {

    public TokenPosition {
        if (line < 1 || col < 1 || index < 0) {
            throw new LexerException("Line and column must be greater than 0, and index must be greater than or equal to 0");
        }
    }

    @Override
    public String toString() {
        return line + ":" + col + ":" + index;
    }

}


*/
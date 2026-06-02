package com.citu.lexor_interpreter.parser;

import com.citu.lexor_interpreter.lexer.Lexer;
import com.citu.lexor_interpreter.parser.ast.ProgramNode;

public class DebugParser {
    public static void main(String[] args) {
        String source = "SCRIPT AREA\nSTART SCRIPT\nDECLARE INT val\nDECLARE INT i\nDECLARE BOOL flag=\"FALSE\"\nPRINT: \"Guess the magic number!\" & $ & \"Enter a number: \"\nSCAN: val\nPRINT: $\nIF (val == 67)\nSTART IF\nFOR (i=1, i<=3, i=i+1)\nSTART FOR\nSWITCH (i)\nSTART SWITCH\nCASE 1:\nPRINT: \"One\" & $\nCASE 2:\nIF (NOT flag)\nSTART IF\nPRINT: \"Flag false\" & $\nflag=\"TRUE\"\nEND IF\nDEFAULT:\nSWITCH (flag)\nSTART SWITCH\nCASE \"TRUE\":\nPRINT: \"Nested true\" & $\nCASE \"FALSE\":\nPRINT: \"Nested false\" & $\nEND SWITCH\nEND SWITCH\nEND FOR\nEND IF\nELSE IF (val > 100)\nSTART IF\nPRINT: \"That is way too high!\" & $\nEND IF\nELSE IF (val >= 70)\nSTART IF\nPRINT: \"Almost there! Just a little over the top\" & $\nEND IF\nELSE IF (val > 1)\nSTART IF\nPRINT: \"Shucks! Maybe try something bigger\" & $\nEND IF\nELSE\nSTART IF\nPRINT: \"Nothing interesting.\" & $\nEND IF\nEND SCRIPT\n";
        try {
            ProgramNode program = new Parser(new Lexer().lex(source)).parseProgram();
            System.out.println("Parsed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
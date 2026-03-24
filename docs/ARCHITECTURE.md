# Clervinator Architecture Guide

This document is the "do not vibe blindly" guide for organizing the LEXOR interpreter codebase.

Use this as the source of truth for where new code should go and what each layer is responsible for.

## Package Ownership

Root package:

- `com.citu.lexor_interpreter`

Package responsibilities:

- `controller` - HTTP endpoints only (web layer).
- `service` - use-case orchestration between web and language engine.
- `model` - API/domain data objects (request/response DTOs, simple records).
- `lexer` - turns source code text into a token stream.
- `parser` - turns token stream into AST or parser result.
- `interpreter` - executes AST/runtime instructions and produces output/state.

## Recommended Directory Structure

```text
src/main/java/com/citu/lexor_interpreter/
  LexorInterpreterApplication.java

  controller/
    HomeController.java
    LexorController.java

  service/
    InterpreterService.java

  model/
    ExecuteResponse.java
    (web-facing DTOs only)

  lexer/
    Lexer.java
    LexerException.java
    token/
      Token.java
      TokenType.java
      TokenPosition.java

  parser/
    Parser.java
    ParserException.java
    ast/
      ProgramNode.java
      StatementNode.java
      ExpressionNode.java
      (other AST nodes)

  interpreter/
    Interpreter.java
    RuntimeContext.java
    RuntimeError.java
    value/
      LexorValue.java
      IntValue.java
      FloatValue.java
      BoolValue.java
      CharValue.java
```

## What Goes Where

### `controller/`
- receive HTTP requests (`@RestController`, `@Controller`)
- map request/response formats
- delegate quickly to `service/`

### `service/`
- coordinate the execution pipeline
- call lexer -> parser -> interpreter
- map engine outputs to web/API response models

Keep this as orchestration. Heavy syntax/runtime internals stay in language-engine packages.

### `model/`
- represent API payloads and simple shared records
- are not tied to lexer/parser internals

Examples:

- `ExecuteResponse`

### `lexer/`
- scan raw source text character-by-character
- produce `Token` objects
- classify symbols/keywords into `TokenType`

Recommended rule:
- `TokenType` belongs in `lexer/token/TokenType.java`

### `parser/`
- validate token order against grammar
- build AST nodes for valid syntax
- surface syntax errors with useful locations/messages

This package does not execute code directly.

### `interpreter/`
- execute AST/runtime operations
- maintain symbol tables/scopes
- apply type checks and operation semantics
- produce final output and runtime errors

This is where language behavior lives.

## End-to-End Flow

The backend execution pipeline should follow this order:

1. `controller` receives source code.
2. `service` starts execution use-case.
3. `lexer` tokenizes source.
4. `parser` builds AST (or parser result).
5. `interpreter` executes AST.
6. `service` maps results to `model` response.
7. `controller` returns HTTP response.

## Naming and Boundaries
- `*Controller` for web entrypoints
- `*Service` for use-case orchestration
- `*Lexer`, `*Parser`, `*Interpreter` for engine stages
- `*Exception` for stage-specific failures
- `*Node` for AST nodes

Boundary rules:

- `controller` may depend on `service` and `model`.
- `service` may depend on `lexer`, `parser`, `interpreter`, and `model`.
- `lexer/parser/interpreter` should avoid depending on web/controller classes.

## Current Actionable Placement Notes

- Keep `ExecuteResponse` in `model`.
- Keep `LexorController` and `HomeController` in `controller`.
- Keep `InterpreterService` in `service`.
- Keep `TokenType` in `lexer/token` (not `model`).

## Future Docs To Add

As implementation grows, add:

- `docs/GRAMMAR.md` - parser grammar reference.
- `docs/TOKEN_RULES.md` - lexer tokenization rules and regex/char rules.
- `docs/RUNTIME_SEMANTICS.md` - type system and runtime behavior.
- `docs/ERRORS.md` - standardized diagnostic formats.

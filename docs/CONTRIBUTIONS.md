# Contributions

This document attributes the work in **Clervinator** (the web-based interpreter for the
LEXOR language) to its two contributors, broken down by component and feature.

Attribution is derived from the project's full Git history (commit authorship and
per-file ownership).

## Contributors

| Name | Git identities | Email |
| --- | --- | --- |
| **Clark** | `Clorkies`, `Clorky` | `clarkisnoob434@gmail.com`, `75629760+Clorkies@users.noreply.github.com` |
| **Jervin** | `jermochi`, `Jervin Ryle I. Milleza` | `jrvnryle@gmail.com` |

## Ownership Overview

| Section / Component | Primary Owner | Notes |
| --- | --- | --- |
| Project scaffold & Spring Boot setup | Clark | Initial structure, app entrypoint |
| Lexer & Tokens | Clark | Jervin added operator/array tokens |
| Parser & AST core | Shared | Clark: parser; Jervin: AST node hierarchy |
| Interpreter & Environment | Jervin | Clark added conditional/loop execution |
| Increment 2 — Operators & SCAN | Jervin | New operators, interactive terminal |
| Increment 3 — Conditionals (IF / ELSE) | Clark | |
| Increment 4 — Loops (FOR / REPEAT WHEN) | Clark | |
| Switch Case | Clark | End-to-end (lexer → UI) |
| Arrays | Jervin | End-to-end (lexer → UI) |
| Web Frontend (UI) | Clark | Jervin added pickers & SCAN terminal |
| Web Backend (controllers/service/model) | Jervin | Clark added `HomeController` |
| Testing | Shared | See per-suite breakdown below |
| Documentation | Clark | Jervin contributed spec & test-case docs |
| DevOps / Build / Deployment | Clark | Docker, Render, Maven config |

---

## Lexer & Tokens

`lexer/`, `lexer/token/`

### Clark
- Created the lexer and parser skeletons for later implementation.
- Implemented `TokenPosition` (immutable token source location) and the `Token` record
  with field validation and a debug-value helper.
- Built the `Lexer` core: tokenization, keyword recognition, literal handling, and error
  reporting.
- Implemented `LexerException` for custom error reporting (token position + offending lexeme).
- Strict float/double handling — enforced the `{<digit>.<digit>}` format and leading/trailing
  zero rules.
- Enforced reserved words to be uppercase (case-sensitivity rules), then refactored that logic.
- Escape-sequence handling, including nested/closing brackets and unterminated-sequence errors.

### Jervin
- Added new operator tokens to `TokenType` for Increment 2.
- Added the `@` and `LENGTH` lexer tokens for the Arrays feature.

---

## Parser & AST

`parser/`, `parser/ast/`, `parser/ast/expression/`

### Clark
- Implemented the `Parser` core: variable declarations, assignments, and print statements.
- Added expression parsing, function-call support, and improved error handling
  (`ParserException`).
- Introduced a dedicated print-expression method and enforced statement line breaks.

### Jervin
- Created the original AST structure for the LEXOR language.
- Authored most of the AST node hierarchy: `AstNode`, `ProgramNode`, `StatementNode`,
  `ExpressionNode`, `AssignNode`, `DeclareNode`, `PrintNode`, `ScanNode`, and the
  `expression/` nodes (`LiteralNode`, `VariableNode`, `BinaryExpressionNode`,
  `UnaryExpressionNode`, `NewlineNode`).
- Added new AST nodes and parser support for the Increment 2 operators.

---

## Interpreter & Environment

`interpreter/`

### Jervin
- Created the `Environment` and the `Interpreter` that executes the AST nodes.
- Built `InterpreterService` to wire together the Lexer, Parser, and Interpreter.
- Updated the interpreter to handle the new Increment 2 operators.
- Enforced strict typing for `FLOAT`; later capped float operations to two decimal places.

### Clark
- Added IF / ELSE IF / ELSE execution logic in the interpreter (Increment 3).
- Added execution logic for FOR and REPEAT WHEN loop nodes (Increment 4).
- Loosened the strongly-typed handling of floats and integers (hotfix).

---

## Increment 2 — Operators & Interactive SCAN

### Jervin
- Added the new AST nodes, parser rules, and interpreter handling for the Increment 2 operators.
- Implemented the interactive split-terminal execution for `SCAN` with strict input parsing
  (both interpreter and web layers).
- Connected `InterpreterService` and wrote `Increment2Test` (all tests passing).
- Implemented the Increment 2 test-case picker and curated samples in the web UI.

---

## Increment 3 — Conditionals (IF / ELSE IF / ELSE)

### Clark
- Added IF / ELSE IF / ELSE conditional parsing (`IfNode`).
- Added the conditional execution logic in the interpreter.
- Wrote 30 TDD test cases for conditional flow (`Increment3Test`) and the
  Increment 3 test-case picker (TC-22 to TC-34) in the UI.

---

## Increment 4 — Loops (FOR / REPEAT WHEN)

### Clark
- Added AST and parsing logic for FOR and REPEAT WHEN loops
  (`ForLoopNode`, `RepeatLoopNode`).
- Added the execution logic for both loop node types.
- Added the JUnit suite and markdown test cases for Increment 4 (`Increment4Test`)
  and the Increment 4 UI checkpoint/test-case picker.

---

## Switch Case

Owned end-to-end by **Clark**.

### Clark
- Implemented the switch-case tokens and lexer logic.
- Defined the switch-case AST nodes (`SwitchNode`).
- Implemented the switch-case parsing rules.
- Implemented the switch-case evaluation logic in the interpreter.
- Added lexer tests, parser tests, and integration tests (`ParserSwitchTest`, `SwitchTest`).
- Updated the UI and documentation with switch-case test cases.

---

## Arrays

Owned end-to-end by **Jervin**.

### Jervin
- Added the `@` and `LENGTH` lexer tokens for arrays.
- Added the AST nodes and parser support for arrays
  (`IndexNode`, `LengthNode`, `IndexAssignNode`).
- Added array storage and element access to the `Environment`.
- Implemented evaluation of array declaration, indexing, and `LENGTH`.
- Added the array UI test cases and documentation (`ArrayTest`).

---

## Web Application — Frontend (UI)

`src/main/resources/templates/index.html`, static assets

### Clark
- Built and iterated the main `index.html` interface; integrated Tailwind CSS.
- Added the logo, footer/contacts section, and overall styling improvements.
- Added draggable/resizable input and output containers and expanded the editor viewport.
- Built the "Sample Programs" section with incremental test cases.
- Added the "View Detailed Documentation" button and handling.
- Added Monaco editor syntax highlighting for the LEXOR language.
- Added the "Added Features" section (showcasing Switch Case and Arrays) and the
  overall page look-and-feel refresh.
- Console loading state (spinner / dynamic dots) and increment-card styling.

### Jervin
- Implemented the Increment 2 test-case picker and curated samples.
- Implemented the inline interactive terminal execution for `SCAN`.

---

## Web Application — Backend (Controllers / Service / Model)

`controller/`, `service/`, `model/`

### Jervin
- `LexorController` (primary author) — execution endpoint.
- `InterpreterService` — orchestration between web and language engine.
- `ExecuteRequest`, `ExecuteResponse` DTOs, and `InputRequiredException`.

### Clark
- `LexorInterpreterApplication` (Spring Boot entrypoint) and `HomeController`.
- Initial Spring Boot project scaffold and `application.properties` (server port config).

---

## Testing

### Clark
- `LexerTest` (primary author), `ParserTest`, `DebugLexer`, `DebugParser`.
- `ParserSwitchTest` and `SwitchTest` (switch case).
- `Increment3Test` (conditionals) and `Increment4Test` (loops).
- Initial JUnit `LexerTest` scaffolding and edge-case refactors.

### Jervin
- `InterpreterTest` (primary author) and the original interpreter tests.
- `Increment2Test`, `EnvironmentTest`, and `ArrayTest`.
- Decoupled and tuned the Increment 1 test cases and their outputs.

---

## Documentation

### Clark
- `ARCHITECTURE.md` (architecture / package-ownership guide).
- `lexor-language-specifications.md` (LEXOR language specification) and its in-app
  documentation button.
- `README.md` (project README, badges, website snapshots).

### Jervin
- Updates to `lexor-language-specifications.md`.
- Test-case documentation contributions (`LEXOR_Test_Cases.md`), including Switch Case
  and Arrays entries.

---

## DevOps / Build / Deployment

### Clark
- `pom.xml` / Maven build configuration.
- `Dockerfile` and `.dockerignore` for Render deployment.
- `application.properties` configuration.

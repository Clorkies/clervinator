# Language Specification of LEXOR Programming Language

## Introduction

LEXOR is a strongly typed programming language developed to teach Senior High School students the basics of programming. It was developed by a group of students enrolled in the Programming Languages course. LEXOR is a pure interpreter.

## Sample Program

```text
%% this is a sample program in LEXOR
SCRIPT AREA
START SCRIPT
DECLARE INT x, y, z=5
DECLARE CHAR a_1='n'
DECLARE BOOL t="TRUE"
x=y=4
a_1='c'
%% this is a comment
PRINT: x & t & z & $ & a_1 & [#] & "last"
END SCRIPT
```

Output of the sample program:

```text
4TRUE5
c#last
```

## Language Grammar

### Program Structure

- All code starts with `SCRIPT AREA`.
- All code is placed inside `START SCRIPT` and `END SCRIPT`.
- Variable declarations must appear immediately after `START SCRIPT`; they cannot be placed elsewhere.
- Variable names are case-sensitive and must start with a letter or underscore (`_`), followed by letters, underscores, or digits.
- Every line contains a single statement.
- Comments start with a double percent sign (`%%`) and can be placed anywhere in the program.
- Executable code appears after variable declarations.
- All reserved words are uppercase and cannot be used as variable names.
- Dollar sign (`$`) signifies next line (carriage return).
- Ampersand (`&`) serves as a concatenator.
- Square brackets (`[]`) are used as escape codes.

### Data Types

1. `INT` - an ordinary number with no decimal part; occupies 4 bytes in memory.
2. `CHAR` - a single symbol.
3. `BOOL` - represents the literals `TRUE` or `FALSE`.
4. `FLOAT` - a number with a decimal part; occupies 4 bytes in memory.

### Operators

#### Arithmetic Operators

- `( )` - parentheses
- `*`, `/`, `%` - multiplication, division, modulo
- `+`, `-` - addition, subtraction
- `>`, `<` - greater than, less than
- `>=`, `<=` - greater than or equal to, less than or equal to
- `==`, `<>` - equal, not equal

#### Logical Operators

Format: `<BOOL expression><LogicalOperator><BOOL expression>`

- `AND` - both `BOOL` expressions must be `TRUE`; otherwise result is `FALSE`.
- `OR` - if one `BOOL` expression is `TRUE`, result is `TRUE`; otherwise `FALSE`.
- `NOT` - reverses the value of a `BOOL` expression.

#### Unary Operators

- `+` - positive
- `-` - negative

## Sample Programs

### 1) Program with arithmetic operation

```text
SCRIPT AREA
START SCRIPT
DECLARE INT xyz, abc=100
xyz=((abc *5)/10 + 10) * -1
PRINT: [[] & xyz & []]
END SCRIPT
```

Output:

```text
[-60]
```

### 2) Program with logical operation

```text
SCRIPT AREA
START SCRIPT
DECLARE INT a=100, b=200, c=300
DECLARE BOOL d="FALSE"
d=(a < b AND c <>200)
PRINT: d
END SCRIPT
```

Output:

```text
TRUE
```

## Output Statement

- `PRINT` - writes formatted output to the output device.

## Input Statement

- `SCAN` - allows the user to input values into variables.

Syntax:

```text
SCAN: <variableName>[,<variableName>]*
```

Sample use:

```text
SCAN: x, y
```

This means the user should input two values separated by a comma (`,`).

## Control Flow Structures

### 1) Conditional

#### a) if selection

```text
IF (<BOOL expression>)
START IF
<statement>
...
<statement>
END IF
```

#### b) if-else selection

```text
IF (<BOOL expression>)
START IF
<statement>
...
<statement>
END IF
ELSE
START IF
<statement>
...
<statement>
END IF
```

#### c) if-else with multiple alternatives

```text
IF (<BOOL expression>)
START IF
<statement>
...
<statement>
END IF
ELSE IF (<BOOL expression>)
START IF
<statement>
...
<statement>
END IF
ELSE
START IF
<statement>
...
<statement>
END IF
```

### 2) Loop Control Flow Structures

#### a) FOR (initialization, condition, update)

```text
FOR (initialization, condition, update)
START FOR
<statement>
...
<statement>
END FOR
```

#### b) REPEAT WHEN ()

```text
REPEAT WHEN (<BOOL expression>)
START REPEAT
<statement>
...
<statement>
END REPEAT
```

## Arrays

LEXOR supports fixed-size, single-dimensional arrays of any scalar type (`INT`, `FLOAT`, `CHAR`, `BOOL`). Because square brackets (`[]`) are reserved for escape codes, arrays use the `@` operator for indexing.

### Declaration

Arrays are declared with `@<size>`, where `<size>` is a positive integer literal. Like other declarations, arrays appear immediately after `START SCRIPT`, and every element is initialized to the type's default value (`INT` 0, `FLOAT` 0.0, `CHAR` empty, `BOOL` FALSE).

```text
DECLARE INT nums@5        %% an array of 5 INTs: [0, 0, 0, 0, 0]
DECLARE CHAR letters@3    %% an array of 3 CHARs
```

### Indexing

Elements are accessed with `<arrayName>@<index>`, where `<index>` is any `INT` expression. Indexes are zero-based. The `@` operator binds tighter than arithmetic, so `nums@i+1` means `(nums@i)+1`; use `nums@(i+1)` for an arithmetic index.

```text
nums@0 = 10               %% write to element 0
nums@1 = nums@0 + 5       %% read element 0, write element 1
PRINT: nums@1             %% prints 15
```

### Length

`LENGTH <arrayName>` returns the number of elements in an array as an `INT`.

```text
FOR (i=0, i < LENGTH nums, i=i+1)
START FOR
    nums@i = i * i
END FOR
```

### Rules and errors

- The array size is fixed at declaration; arrays cannot grow or shrink.
- An index outside the range `0 .. LENGTH-1` is a runtime error.
- An index expression that is not an `INT` is a runtime error.
- Assigning a value whose type does not match the array's element type is a runtime error (an `INT` assigned into a `FLOAT` array is coerced, matching scalar `FLOAT` behavior).

## Note

You may use any language to implement the interpreter except Python and JavaScript.
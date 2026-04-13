# LEXOR Language - Test Cases

Increments 1 & 2 test cases for sample loading.

## Increment 1

#### TC-01 - Minimal valid program
```lexor
SCRIPT AREA
START SCRIPT
END SCRIPT
```

#### TC-02 - Comment recognition
```lexor
%% This is a top-level comment
SCRIPT AREA
START SCRIPT %% This is an inline comment
END SCRIPT
```

#### TC-03 - Declare INT, CHAR, BOOL variables (no initialization)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT age
DECLARE CHAR grade
DECLARE BOOL isPass
END SCRIPT
```

#### TC-04 - Declare with initialization
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT score=100
DECLARE CHAR letter='A'
DECLARE BOOL flag="TRUE"
DECLARE FLOAT pi=3.14
END SCRIPT
```

#### TC-05 - Multiple variables in one DECLARE
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x, y, z=5
DECLARE CHAR a='n', b
END SCRIPT
```

#### TC-06 - Simple assignment
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x
DECLARE CHAR ch
DECLARE BOOL flag
x=42
ch='Z'
flag="FALSE"
PRINT: x & $ & ch & $ & flag
END SCRIPT
```

#### TC-07 - Chained assignment
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x, y
x=y=10
PRINT: x & $ & y
END SCRIPT
```

#### TC-08 - PRINT with ampersand concatenation
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT num=7
DECLARE CHAR ch='X'
DECLARE BOOL b="TRUE"
PRINT: num & ch & b
END SCRIPT
```

#### TC-09 - Dollar sign as newline
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=1, b=2, c=3
PRINT: a & $ & b & $ & c
END SCRIPT
```

#### TC-10 - Escape code [#] and string literal
```lexor
SCRIPT AREA
START SCRIPT
DECLARE CHAR a_1='n'
a_1='c'
PRINT: a_1 & [#] & "last"
END SCRIPT
```

#### TC-11 - Escape codes [[] and []]
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT xyz, abc=100
xyz= ((abc *5)/10 + 10) * -1
PRINT: [[] & xyz & []]
END SCRIPT
```

#### TC-12 - Full sample program from spec
```lexor
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

#### TC-13 - PRINT with string literals only
```lexor
SCRIPT AREA
START SCRIPT
PRINT: "Hello" & "," & " " & "World"
END SCRIPT
```

#### TC-14 - PRINT FLOAT variable
```lexor
SCRIPT AREA
START SCRIPT
DECLARE FLOAT temp=98.6
PRINT: temp
END SCRIPT
```

## Increment 2

#### TC-15 - Unary negative on a variable
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=5
DECLARE INT b
b= -a
PRINT: b
END SCRIPT
```

#### TC-16 - Unary positive
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=8
DECLARE INT y
y= +x
PRINT: y
END SCRIPT
```

#### TC-17 - Unary negative inside an expression
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT n=10
DECLARE INT result
result= (n + -3) * -1
PRINT: result
END SCRIPT
```

#### TC-18 - Addition and subtraction
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=15, b=7
DECLARE INT sum, diff
sum= a + b
diff= a - b
PRINT: sum & $ & diff
END SCRIPT
```

#### TC-19 - Multiplication and division
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=20, b=4
DECLARE INT product, quotient
product= a * b
quotient= a / b
PRINT: product & $ & quotient
END SCRIPT
```

#### TC-20 - Modulo operator
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=17, b=5
DECLARE INT rem
rem= a % b
PRINT: rem
END SCRIPT
```

#### TC-21 - Operator precedence with parentheses
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=3, y=4, z=2
DECLARE INT r1, r2
r1= x + y * z
r2= (x + y) * z
PRINT: r1 & $ & r2
END SCRIPT
```

#### TC-22 - Complex arithmetic expression (from spec)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT abc=100
DECLARE INT xyz
xyz= ((abc * 5) / 10 + 10) * -1
PRINT: xyz
END SCRIPT
```

#### TC-23 - FLOAT arithmetic
```lexor
SCRIPT AREA
START SCRIPT
DECLARE FLOAT a=5.5, b=2.0
DECLARE FLOAT result
result= a * b
PRINT: result
END SCRIPT
```

#### TC-24 - Relational operators > and <
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=10, b=20
DECLARE BOOL r1, r2
r1= (a < b)
r2= (a > b)
PRINT: r1 & $ & r2
END SCRIPT
```

#### TC-25 - Relational operators >=, <=, ==, <>
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5
DECLARE BOOL eq, neq, gte, lte
eq= (x == 5)
neq= (x <> 3)
gte= (x >= 5)
lte= (x <= 10)
PRINT: eq & $ & neq & $ & gte & $ & lte
END SCRIPT
```

#### TC-26 - AND logical operator (both true)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=100, b=200, c=300
DECLARE BOOL d
d= (a < b AND c <> 200)
PRINT: d
END SCRIPT
```

#### TC-27 - AND logical operator (one false)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5, y=10
DECLARE BOOL result
result= (x > 3 AND y < 5)
PRINT: result
END SCRIPT
```

#### TC-28 - OR logical operator
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=10, b=20
DECLARE BOOL result
result= (a > b OR a < b)
PRINT: result
END SCRIPT
```

#### TC-29 - NOT logical operator
```lexor
SCRIPT AREA
START SCRIPT
DECLARE BOOL flag="FALSE"
DECLARE BOOL result
result= NOT flag
PRINT: result
END SCRIPT
```

#### TC-30 - SCAN single INT
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT num
SCAN: num
PRINT: num
END SCRIPT
```

#### TC-31 - SCAN two variables (comma-separated)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a, b
DECLARE INT sum
SCAN: a, b
sum= a + b
PRINT: a & " + " & b & " = " & sum
END SCRIPT
```

#### TC-32 - SCAN FLOAT and compute area
```lexor
SCRIPT AREA
START SCRIPT
DECLARE FLOAT radius
DECLARE FLOAT area
SCAN: radius
area= 3.14 * radius * radius
PRINT: area
END SCRIPT
```

#### TC-33 - SCAN then compare with relational operator
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x
DECLARE BOOL isPositive
SCAN: x
isPositive= (x > 0)
PRINT: isPositive
END SCRIPT
```

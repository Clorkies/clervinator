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
PRINT: age & $ & grade & $ & isPass
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
PRINT: score & $ & letter & $ & flag & $ & pi
END SCRIPT
```

#### TC-05 - Multiple variables in one DECLARE
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x
DECLARE INT y
DECLARE INT z=5
DECLARE CHAR a='n'
DECLARE CHAR b
PRINT: x & $ & y & $ & z & $ & a & $ & b
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
DECLARE INT xyz=60
PRINT: [[] & xyz & []]
END SCRIPT
```

#### TC-12 - Full sample program from spec
```lexor
%% this is a sample program in LEXOR
SCRIPT AREA
START SCRIPT
DECLARE INT x
DECLARE INT y
DECLARE INT z=5
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

#### TC-15 - Unary operator
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=5, b=-6
DECLARE INT c,d,e
c= -a
d= --a
e= +b
PRINT: c & $ & d & $ & e
END SCRIPT
```

#### TC-16 - Complex arithmetic and operator precedence
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT abc=100
DECLARE INT xyz
xyz= ((abc * 5) / 10 + 10) * -1
PRINT: xyz
END SCRIPT
```

#### TC-17 - All comparison operators
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5
DECLARE BOOL eq, neq, gte, lte
eq= (x == 5)
neq= (x <> 5)
gte= (x >= 3)
lte= (x <= 4)
PRINT: eq & $ & neq & $ & gte & $ & lte
END SCRIPT
```

#### TC-18 - Logical operators AND, OR, NOT
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT a=100, b=200, c=300
DECLARE BOOL d, e, f
d= (a < b AND c <> 200)
e= (a > b OR a < b)
f= NOT d
PRINT: d & $ & e & $ & f
END SCRIPT
```

#### TC-19 - FLOAT arithmetic
```lexor
SCRIPT AREA
START SCRIPT
DECLARE FLOAT a=5.5, b=2.0
DECLARE FLOAT result
result= a * b
PRINT: result
END SCRIPT
```

#### TC-20 - Error: division by zero
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=10, y=0
PRINT: x / y
END SCRIPT
```

#### TC-21 - SCAN: read input and compute product
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x, y
SCAN: x, y
PRINT: x * y
END SCRIPT
```

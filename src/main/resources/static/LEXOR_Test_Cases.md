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

## Increment 3

#### TC-22 - Simple IF: condition TRUE executes body
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=10
IF (x > 5)
START IF
PRINT: "x is greater than 5"
END IF
END SCRIPT
```

#### TC-23 - Simple IF: condition FALSE skips body
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=3
IF (x > 5)
START IF
PRINT: "x is greater than 5"
END IF
PRINT: "done"
END SCRIPT
```

#### TC-24 - IF-ELSE: true branch taken
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT score=85
IF (score >= 75)
START IF
PRINT: "PASSED"
END IF
ELSE
START IF
PRINT: "FAILED"
END IF
END SCRIPT
```

#### TC-25 - IF-ELSE: false branch (else) taken
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT score=60
IF (score >= 75)
START IF
PRINT: "PASSED"
END IF
ELSE
START IF
PRINT: "FAILED"
END IF
END SCRIPT
```

#### TC-26 - IF ELSE IF ELSE: first branch matches
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT grade=95
IF (grade >= 90)
START IF
PRINT: "Excellent"
END IF
ELSE IF (grade >= 75)
START IF
PRINT: "Passing"
END IF
ELSE
START IF
PRINT: "Failing"
END IF
END SCRIPT
```

#### TC-27 - IF ELSE IF ELSE: middle ELSE IF branch matches
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT grade=80
IF (grade >= 90)
START IF
PRINT: "Excellent"
END IF
ELSE IF (grade >= 75)
START IF
PRINT: "Passing"
END IF
ELSE
START IF
PRINT: "Failing"
END IF
END SCRIPT
```

#### TC-28 - IF ELSE IF ELSE: ELSE branch taken (no condition matches)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT grade=50
IF (grade >= 90)
START IF
PRINT: "Excellent"
END IF
ELSE IF (grade >= 75)
START IF
PRINT: "Passing"
END IF
ELSE
START IF
PRINT: "Failing"
END IF
END SCRIPT
```

#### TC-29 - Nested IF inside IF body
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=10
DECLARE INT y=5
IF (x > 0)
START IF
IF (y > 0)
START IF
PRINT: "both positive"
END IF
END IF
END SCRIPT
```

#### TC-30 - Nested IF-ELSE inside ELSE branch
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT temp=25
IF (temp > 35)
START IF
PRINT: "Hot"
END IF
ELSE
START IF
IF (temp > 20)
START IF
PRINT: "Warm"
END IF
ELSE
START IF
PRINT: "Cold"
END IF
END IF
END SCRIPT
```

#### TC-31 - Error: DECLARE inside IF block (parse error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE BOOL flag="TRUE"
IF (flag)
START IF
DECLARE INT x=1
END IF
END SCRIPT
```

#### TC-32 - Error: INT used as condition (runtime type error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5
IF (x)
START IF
PRINT: "bad"
END IF
END SCRIPT
```

#### TC-33 - Error: missing END IF (parse error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE BOOL flag="TRUE"
IF (flag)
START IF
PRINT: "oops"
END SCRIPT
```

#### TC-34 - Lazy evaluation: runtime error in unchosen branch is ignored
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=10, z=0
DECLARE BOOL t="TRUE"
IF (t)
START IF
PRINT: "safe"
END IF
ELSE
START IF
PRINT: x/z
END IF
END SCRIPT
```

## Increment 4

#### TC-35 - Basic FOR count-up (Happy Path)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<4, i=i+1)
START FOR
PRINT: i & $
END FOR
END SCRIPT
```

#### TC-36 - FOR accumulate sum
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i, total
total=0
FOR (i=1, i<6, i=i+1)
START FOR
total=total+i
END FOR
PRINT: total
END SCRIPT
```

#### TC-37 - FOR zero-iteration
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=10, i<5, i=i+1)
START FOR
PRINT: i
END FOR
PRINT: "done"
END SCRIPT
```

#### TC-38 - FOR loop with multiplication in update
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<100, i=i*2)
START FOR
PRINT: i & $
END FOR
END SCRIPT
```

#### TC-39 - Basic REPEAT WHEN count-up
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
i=1
REPEAT WHEN (i<4)
START REPEAT
PRINT: i & $
i=i+1
END REPEAT
END SCRIPT
```

#### TC-40 - REPEAT WHEN zero-iteration
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
i=10
REPEAT WHEN (i<5)
START REPEAT
PRINT: i
END REPEAT
PRINT: "done"
END SCRIPT
```

#### TC-41 - REPEAT WHEN with BOOL variable condition
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT count
DECLARE BOOL running
count=0
running="TRUE"
REPEAT WHEN (running)
START REPEAT
count=count+1
running=(count<3)
END REPEAT
PRINT: count
END SCRIPT
```

#### TC-42 - Nested FOR inside FOR
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i, j, prod
FOR (i=1, i<4, i=i+1)
START FOR
FOR (j=1, j<4, j=j+1)
START FOR
prod=i*j
PRINT: prod & " "
END FOR
PRINT: $
END FOR
END SCRIPT
```

#### TC-43 - Nested REPEAT WHEN inside FOR
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i, j
FOR (i=1, i<3, i=i+1)
START FOR
j=1
REPEAT WHEN (j<3)
START REPEAT
PRINT: i & "x" & j & $
j=j+1
END REPEAT
END FOR
END SCRIPT
```

#### TC-44 - IF inside FOR body
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<6, i=i+1)
START FOR
IF (i==3)
START IF
PRINT: "three" & $
END IF
END FOR
END SCRIPT
```

#### TC-45 - Error: DECLARE inside FOR body (parse error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<3, i=i+1)
START FOR
DECLARE INT x
END FOR
END SCRIPT
```

#### TC-46 - Error: non-BOOL condition in FOR (runtime error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i+1, i=i+1)
START FOR
PRINT: i
END FOR
END SCRIPT
```

#### TC-47 - Error: missing END FOR (parse error)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<3, i=i+1)
START FOR
PRINT: i
END SCRIPT
```

## Increment 5

#### TC-48 - Basic SWITCH with INT match (Happy Path)
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=2
SWITCH (x)
START SWITCH
CASE 1:
PRINT: "one"
CASE 2:
PRINT: "two"
DEFAULT:
PRINT: "other"
END SWITCH
END SCRIPT
```

#### TC-49 - Basic SWITCH with DEFAULT taken
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=99
SWITCH (x)
START SWITCH
CASE 1:
PRINT: "one"
CASE 2:
PRINT: "two"
DEFAULT:
PRINT: "other"
END SWITCH
END SCRIPT
```

#### TC-50 - SWITCH with no match and no DEFAULT
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5
SWITCH (x)
START SWITCH
CASE 1:
PRINT: "one"
CASE 2:
PRINT: "two"
END SWITCH
PRINT: "done"
END SCRIPT
```

#### TC-51 - SWITCH with STRING matching
```lexor
SCRIPT AREA
START SCRIPT
DECLARE BOOL x="TRUE"
SWITCH (x)
START SWITCH
CASE "FALSE":
PRINT: "F"
CASE "TRUE":
PRINT: "T"
DEFAULT:
PRINT: "U"
END SWITCH
END SCRIPT
```

#### TC-52 - SWITCH with CHAR matching
```lexor
SCRIPT AREA
START SCRIPT
DECLARE CHAR grade='B'
SWITCH (grade)
START SWITCH
CASE 'A':
PRINT: "Excellent"
CASE 'B':
PRINT: "Good"
DEFAULT:
PRINT: "Unknown"
END SWITCH
END SCRIPT
```

#### TC-53 - SWITCH with BOOL matching
```lexor
SCRIPT AREA
START SCRIPT
DECLARE BOOL active="TRUE"
SWITCH (active)
START SWITCH
CASE "TRUE":
PRINT: "Running"
CASE "FALSE":
PRINT: "Stopped"
END SWITCH
END SCRIPT
```

#### TC-54 - SWITCH with FLOAT matching
```lexor
SCRIPT AREA
START SCRIPT
DECLARE FLOAT pi=3.14
SWITCH (pi)
START SWITCH
CASE 3.14:
PRINT: "Pi"
CASE 2.71:
PRINT: "E"
DEFAULT:
PRINT: "Other"
END SWITCH
END SCRIPT
```

#### TC-55 - SWITCH with expression in CASE
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=5
SWITCH (x)
START SWITCH
CASE 2+3:
PRINT: "Five"
CASE 1+1:
PRINT: "Two"
END SWITCH
END SCRIPT
```

#### TC-56 - Nested SWITCH inside SWITCH
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT outer=1, inner=2
SWITCH (outer)
START SWITCH
CASE 1:
SWITCH (inner)
START SWITCH
CASE 2:
PRINT: "1 and 2"
DEFAULT:
PRINT: "1 and other"
END SWITCH
DEFAULT:
PRINT: "outer is not 1"
END SWITCH
END SCRIPT
```

#### TC-57 - SWITCH inside IF statement
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
IF (x > 0)
START IF
SWITCH (x)
START SWITCH
CASE 1:
PRINT: "Positive 1"
DEFAULT:
PRINT: "Positive other"
END SWITCH
END IF
END SCRIPT
```

#### TC-58 - IF statement inside SWITCH CASE
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1, y=10
SWITCH (x)
START SWITCH
CASE 1:
IF (y > 5)
START IF
PRINT: "x is 1 and y > 5"
END IF
DEFAULT:
PRINT: "Default"
END SWITCH
END SCRIPT
```

#### TC-59 - SWITCH inside FOR loop
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i
FOR (i=1, i<=3, i=i+1)
START FOR
SWITCH (i)
START SWITCH
CASE 1:
PRINT: "One" & $
CASE 2:
PRINT: "Two" & $
CASE 3:
PRINT: "Three" & $
END SWITCH
END FOR
END SCRIPT
```

#### TC-60 - SWITCH inside REPEAT WHEN loop
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT i=1
REPEAT WHEN (i <= 2)
START REPEAT
SWITCH (i)
START SWITCH
CASE 1:
PRINT: "A" & $
CASE 2:
PRINT: "B" & $
END SWITCH
i=i+1
END REPEAT
END SCRIPT
```

#### TC-61 - Error: Parse Error on duplicate DEFAULT
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
START SWITCH
DEFAULT:
PRINT: "First"
DEFAULT:
PRINT: "Second"
END SWITCH
END SCRIPT
```

#### TC-62 - Error: Parse Error on missing END SWITCH
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
START SWITCH
CASE 1:
PRINT: "One"
END SCRIPT
```

#### TC-63 - Error: Parse Error on missing START SWITCH
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
CASE 1:
PRINT: "One"
END SWITCH
END SCRIPT
```

#### TC-64 - Error: Parse Error on missing COLON after CASE
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
START SWITCH
CASE 1
PRINT: "One"
END SWITCH
END SCRIPT
```

#### TC-65 - Error: Parse Error on statement outside CASE or DEFAULT
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
START SWITCH
PRINT: "I am lost"
CASE 1:
PRINT: "One"
END SWITCH
END SCRIPT
```

#### TC-66 - Valid Empty SWITCH
```lexor
SCRIPT AREA
START SCRIPT
DECLARE INT x=1
SWITCH (x)
START SWITCH
END SWITCH
PRINT: "Empty switch parsed successfully"
END SCRIPT
```

#### TC-67 - The Gauntlet: Deep Nesting, Dynamic Cases, and State Mutation
```lexor
SCRIPT AREA
START SCRIPT

DECLARE INT val
DECLARE INT i
DECLARE BOOL flag="FALSE"

PRINT: "Guess the magic number!" & $ & "Enter a number: "
SCAN: val
PRINT: $

IF (val == 67)
START IF

FOR (i=1, i<=3, i=i+1)
START FOR

    SWITCH (i)
    START SWITCH

        CASE 1:
            PRINT: "One" & $

        CASE 2:
            IF (NOT flag)
            START IF
                PRINT: "Flag false" & $
                flag="TRUE"
            END IF

        DEFAULT:
            SWITCH (flag)
            START SWITCH
                CASE "TRUE":
                    PRINT: "Nested true" & $
                CASE "FALSE":
                    PRINT: "Nested false" & $
            END SWITCH

    END SWITCH

END FOR

END IF

ELSE IF (val > 100)
START IF
PRINT: "That is way too high!" & $
END IF

ELSE IF (val >= 70)
START IF
PRINT: "Almost there! Just a little over the top" & $
END IF

ELSE IF (val > 1)
START IF
PRINT: "Shucks! Maybe try something bigger" & $
END IF

ELSE
START IF
PRINT: "Nothing interesting." & $
END IF

END SCRIPT
```
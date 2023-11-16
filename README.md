# Factorio CPU

### Introduction

Factorio, a popular sandbox game known for its intricate factory-building and automation mechanics, provides a unique platform for creative experimentation. Within its expansive world, players can design complex systems, simulating real-world engineering and computational concepts. This project leverages Factorio's versatile environment to construct an ambitious and innovative endeavor: a fully functional virtual processor.

The Factorio CPU Project transcends mere gameplay, venturing into the realms of computer engineering and programming language design. The game's control network, typically used for basic operations and automation within the game, serves as the foundation for this complex endeavor. By exploiting its capabilities for logical operations, memory creation, and timing control, a simplified but operational processor is brought to life within the game's universe.

This processor is not just a static creation; it's powered by a custom-designed programming language and a suite of tools developed specifically for this project. The language, tailored to the processor's architecture, supports essential programming constructs like variables, dynamic memory allocation, loops, and functions, including recursion. This language is then compiled into a format understood by the virtual processor, bridging the gap between high-level programming and low-level execution.

What makes this project stand out is not just the technical achievement of building a processor within a game but also the educational and experimental value it offers. It demonstrates key principles of computer science and electrical engineering in an accessible, engaging manner. Whether you're a Factorio enthusiast, a student of computer science, or simply someone fascinated by the intersection of gaming and technology, this project offers a unique perspective on how complex computational concepts can be visualized and executed within a virtual environment.

### Processor Architecture

Using the techniques mentioned above, I designed a simple processor equipped with 8 registers, a kilobyte of RAM, a separate stack, and input and output interfaces. The processor handles 15 instructions. Each of them can take two arguments. Each time, 3 numbers are sent to the processor: A, B, and C, where A is the instruction and B and C are the arguments.

```
1 - copy the value from register B to register C.
2 - perform a mathematical operation on registers 1 and 2 using value B. The result is stored in register 1.
3 - save the value of register B to the stack
4 - load the value from the stack to register B
5 - copy the value from register B to the memory RAM address at location C
6 - copy the value from the memory RAM address at location B to register C
7 - jump by constant value B
8 - jump by constant value B if the value of register 1 is equal to 0
9 - jump by the value of register 8
10 - jump by the value of register 8 if the value of register 1 is equal to 0
11 - load the value from input interface B to register C
12 - save the value from register B to output interface C
13 - set a constant value C in register B
14 - copy the value from register B to the memory RAM address specified in register C
15 - copy the value from the memory RAM address specified in register B to register C
```

### Programming Language

For the processor, I have created my own simple programming language that supports:

1. Variables (32-bit signed integer)
2. Dynamic memory allocation and arrays
3. Pointers
4. Mathematical operations and logical conditions
5. Conditional statements
6. While loops
7. Functions with recursion and local variables
8. Input/output

This simple programming language is a minimalist, procedural language with a straightforward syntax that focuses on the core aspects of programming. It is designed to demonstrate fundamental programming concepts such as variables, loops, conditional statements, functions, memory allocation, and basic arithmetic and logical operations.

Code examples:

```
// Declaration of a variable (undefined value)
var x;

// Declaration and definition of a variable
var x = 0;

// Assigning a value
x = 3;

// Reading from the input interface
x = input[1];

// Writing to the output interface
output[1] = x;

// Conditional expressions
if (x > 4) {
    output[1] = 2;
} else {
    output[1] = 1;
}

// While loop
var i = 0;
while (i < 10) {
    x = x * 2;
    i++;
}

// Dynamic memory allocation
var* array = malloc(5);

// These expressions are equivalent
array[0] = input[1];
*array = input[1];

// These expressions are equivalent
array[2] = input[1];
*(array + 2) = input[1];

// These expressions are equivalent
output[1] = array[2];
output[1] = *(array + 2);

// Increment/decrement
x++;
x--;
x+=2;
x-=2;
arr[x]++;
arr[x]--;
arr[x] += y;
arr[x] -= y;

// Functions and variable scope
var a = 2;
var c = 4;
fn func(a, b) {
    var d = 12;
    output[1] = a;
    output[2] = b;
    output[3] = c;
    output[4] = d;
}
func(7, 8);
// Results: [7, 8, 4]

// Returning a value from a function
fn mul(a, b) {
    a = a * b;
} ret a;

x = mul(3, 4);

// Logical and mathematical operators
// * / %
// + -
// >> <<
// == != > < >= <=
// and &
// or |

// Reading from the input interface into an array
var* arr = input[1..4];

// Writing an array to the output interface
output[1..4] = arr;
```

### Tools

I wrote all necessary tools in Java.

I used libraries:

1. FasterXML Jackson
2. ANTLR4
3. Apache Commons Lang 3
4. Apache Commons CLI
5. Apache Commons IO

The most important one is ANTLR4, which I used to generate lexer and parser for my programming language.

### Building the Abstract Syntax Tree of a Language

I utilized ANTLR4 to transform my code into an Abstract Syntax Tree. ANTLR4 is a robust parser generator that can parse and convert code in multiple languages. I created a .g4 file to generate a lexer and parser, which were then used to produce an Abstract Syntax Tree (AST). This AST enabled me to analyze and manipulate the code with greater ease. ANTLR4 proved to be a convenient and efficient tool for this process.

```
grammar Lang;

@header {
package com.skdziwak.factoriolang.antlr4;
}

prog: (func | stmt)+ EOF;
func: 'fn' IDENTIFIER '(' argsdecl ')' '{' statements '}' ('ret' IDENTIFIER ';')?;
argsdecl: (IDENTIFIER ',')* IDENTIFIER  | ;
args: (expr ',')* expr | ;
statements: stmt*;
stmt:
      IDENTIFIER '=' expr ';'                       # AssignmentStatement
    | '*' expr '=' expr ';'                         # PointerAssignmentStatement
    | IDENTIFIER '++' ';'                           # Increment
    | IDENTIFIER '--' ';'                           # Decrement
    | IDENTIFIER '+=' expr ';'                      # IncrementN
    | IDENTIFIER '-=' expr ';'                      # DecrementN
    | IDENTIFIER '[' expr ']' '++' ';'              # IncrementArr
    | IDENTIFIER '[' expr ']' '--' ';'              # DecrementArr
    | IDENTIFIER '[' expr ']' '+=' expr ';'         # IncrementArrN
    | IDENTIFIER '[' expr ']' '-=' expr ';'         # DecrementArrN
    | IDENTIFIER '[' expr ']' '=' expr ';'          # ArrayAssignmentStatement
    | 'output' '[' NUM ']' '=' expr ';'             # OutputStatement
    | 'output' '[' NUM '..' NUM ']' '=' expr ';'    # OutputArrayStatement
    | expr ';'                                      # ExpressionStatement
    | '{' stmt+ '}'                                 # MultipleStatements
    | 'if' '(' expr ')' stmt 'else' stmt            # IfElseStatement
    | 'if' '(' expr ')' stmt                        # IfStatement
    | 'while' '(' expr ')' stmt                     # WhileStatement
    | ('var' | 'var*') IDENTIFIER ';'               # VarDeclare
    | ('var' | 'var*') IDENTIFIER '=' expr ';'      # VarDeclareDefine
;

expr:
      '(' expr ')'                                              # Parenthesis
    | <assoc=right> '*' expr                                    # Dereference
    | <assoc=right> '-' expr                                    # UnaryMinus
    | <assoc=right> 'not' expr                                  # UnaryNot
    | expr op=('*' | '/' | '%') expr                            # MultiplicationPrecedenceOperation
    | expr op=('+' | '-') expr                                  # AdditionPrecedenceOperation
    | expr op=('>>' | '<<') expr                                # ShiftOperation
    | expr op=('==' | '!=' | '>' | '<' | '>=' | '<=') expr      # Comparison
    | expr ('and' | '&') expr                                   # AndOperator
    | expr ('or' | '|') expr                                    # OrOperator
    | 'input' '[' NUM ']'                                       # Input
    | 'input' '[' NUM '..' NUM ']'                              # InputArray
    | IDENTIFIER '[' expr ']'                                   # ArrayExpression
    | 'malloc' '(' expr ')'                                     # Malloc
    | IDENTIFIER '(' args ')'                                   # FunctionCall
    | IDENTIFIER                                                # Variable
    | NUM                                                       # Number
;

IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
NUM: '0' | '-'?[1-9][0-9]*(.[0-9]+)?;
WS: [ \t\n\r]+ -> skip;
COMMENT: '#'.+[\n\r] -> skip;
```

### Compilation

Abstract Syntax Tree is used to compile code into simple instructions. Each of them consists of 3 numbers.

Command:

```bash
java -jar factorio-compiler.jar power.fcl -s
```

Result:

```
State{signalA=7, signalB=30, signalC=0}
State{signalA=13, signalB=1, signalC=1}
State{signalA=1, signalB=1, signalC=5}
State{signalA=13, signalB=1, signalC=0}
State{signalA=1, signalB=1, signalC=6}
State{signalA=3, signalB=1, signalC=0}
State{signalA=1, signalB=4, signalC=1}
State{signalA=3, signalB=1, signalC=0}
...
```

Unfortunately, this is not a readable way to present a compiled program, so I also created a tool that outputs it in a human-readable form.

Command:

```bash
java -jar factorio-compiler.jar power.fcl -h
```

Result:

```
Program length: 43
Instructions: 
        [1]   Jump to 32
        [2]   Assign value 1 to register 1
        [3]   Copy register 1 to register 5
        [4]   Assign value 0 to register 1
        [5]   Copy register 1 to register 6
        [6]   Push register 1 to stack
        [7]   Copy register 4 to register 1
        [8]   Push register 1 to stack
        [9]   Pop stack to register 2
        [10]  Pop stack to register 1
        [11]  Math operation: (register 1) <- (register 1) LOWER (register 2)
        [12]  Jump to 30 if register 1 is equal to 0
        ...
```

However, the most important option is -b, because it builds the schema that we can load in the game and build memory blocks based on it, which will load the program.

```bash
java -jar factorio-compiler.jar power.fcl -b
```

### Simulation and bug finding

Finding bugs in the compiler was very difficult when the only debugging option was observing the behavior of the processor built in the game. That's why I wrote a program that simulates the correct behavior of the processor according to the specification. To run the simulation, execute the following command:

```bash
java -jar factorio-compiler.jar power.fcl -sim
```

As a result, we will receive a program in a human-readable form, a log of executing subsequent instructions, as well as the value of RAM and registers at the end of execution. We will also be informed if the stack is not empty at the end.

```
Execution log:
        [1]   Jump to 32
        [32]  Assign value 3 to register 1
        [33]  Copy register 1 to RAM(1)
        [34]  Assign value 4 to register 1
        [35]  Copy register 1 to RAM(2)
        [36]  Copy RAM(1) to register 1
        [37]  Copy register 1 to register 3
        [38]  Copy RAM(2) to register 1
        [39]  Copy register 1 to register 4
        [40]  Assign value 11 to register 1
        [41]  Push register 1 to stack
        [42]  Jump to 2
        [2]   Assign value 1 to register 1
        [3]   Copy register 1 to register 5
        [4]   Assign value 0 to register 1
        [5]   Copy register 1 to register 6
        [6]   Push register 1 to stack
        [7]   Copy register 4 to register 1
        [8]   Push register 1 to stack
        [9]   Pop stack to register 2
        [10]  Pop stack to register 1
        [11]  Math operation: (register 1) <- (register 1) LOWER (register 2)
        [12]  Jump to 30 if register 1 is equal to 0
        [13]  Copy register 5 to register 1
        ...

Register values: 
        1: 0
        2: 4
        3: 3
        4: 4
        5: 81
        6: 4
        7: 0
        8: 11
```

### Moving the compiled program to the game

In Factorio, there are blueprints which robots use to build pre-designed structures. The blueprints are actually compressed JSON files that are saved as Base64. I used this to generate memory with the program, ready to be read by the processor.
Sample JSON:

```json
{
  "icons": [
    {
      "signal": {
        "type": "item",
        "name": "constant-combinator"
      },
      "index": 1
    }
  ],
  "entities": [
    {
      "name": "constant-combinator",
      "position": {
        "x": 1.0,
        "y": 1.5
      },
      "connections": {
        "1": {
          "green": [
            {
              "entity_id": 2,
              "circuit_id": 1
            }
          ]
        }
      },
      "entity_number": 1,
      "control_behavior": {
        "filters": [
          {
            "signal": {
              "type": "virtual",
              "name": "signal-A"
            },
            "count": 7,
            "index": 1
          },
          {
            "signal": {
              "type": "virtual",
              "name": "signal-B"
            },
            "count": 30,
            "index": 2
          }
        ]
      }
    },
    ...
```

Base64 encoded blueprint ready for import in the game:

```
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
```

### The Great Test

To test the processor, I have chosen 3 algorithms.

1. Fast exponentiation algorithm
    
    ```
    fn power(a, n) {
        var result = 1;
        while (n > 0) {
            if (n % 2 == 0) {
                n = n / 2;
                a = a * a;
            } else {
                n--;
                result = result * a;
    
                n = n / 2;
                a = a * a;
            }
        }
    } ret result;
    
    output[1] = power(input[1], input[2]);
    ```
    
2. Bubble sort
    
    ```
    var* arr = input[1..16];
    
    fn bubbleSort(array, length) {
        var i = 0;
        while (i < length) {
            var j = i + 1;
            while (j < length) {
                if (array[i] > array[j]) {
                    var x = array[j];
                    array[j] = array[i];
                    array[i] = x;
                }
                j++;
            }
            i++;
        }
    }
    
    bubbleSort(arr, 16);
    
    output[1..16] = arr;
    ```
    
3. Merge sort
    
    ```
    var* arr = input[1..16];
    var len = 16;
    
    fn merge(array, left, mid, right) {
        var subArrayOne = mid - left + 1;
        var subArrayTwo = right - mid;
    
        var* leftArray = malloc(subArrayOne);
        var* rightArray = malloc(subArrayTwo);
    
        var i = 0;
        while (i < subArrayOne) {
            leftArray[i] = array[left + i];
            i++;
        }
        i = 0;
        while (i < subArrayTwo) {
            rightArray[i] = array[mid + 1 + i];
            i++;
        }
    
        var indexOfSubArrayOne = 0;
        var indexOfSubArrayTwo = 0;
        var indexOfMergedArray = left;
    
        while (indexOfSubArrayOne < subArrayOne and indexOfSubArrayTwo < subArrayTwo) {
            if (leftArray[indexOfSubArrayOne] <= rightArray[indexOfSubArrayTwo]) {
                array[indexOfMergedArray] = leftArray[indexOfSubArrayOne];
                indexOfSubArrayOne++;
            }
            else {
                array[indexOfMergedArray] = rightArray[indexOfSubArrayTwo];
                indexOfSubArrayTwo++;
            }
            indexOfMergedArray++;
        }
    
        while (indexOfSubArrayOne < subArrayOne) {
            array[indexOfMergedArray] = leftArray[indexOfSubArrayOne];
            indexOfSubArrayOne++;
            indexOfMergedArray++;
        }
    
        while (indexOfSubArrayTwo < subArrayTwo) {
            array[indexOfMergedArray] = rightArray[indexOfSubArrayTwo];
            indexOfSubArrayTwo++;
            indexOfMergedArray++;
        }
    }
    
    fn mergeSort(array, begin, end) {
        if (begin < end) {
            var mid = begin + (end - begin) / 2;
            mergeSort(array, begin, mid);
            mergeSort(array, mid + 1, end);
            merge(array, begin, mid, end);
        }
    }
    
    mergeSort(arr, 0, len - 1);
    
    output[1..16] = arr;
    ```
    

All algorithms worked and produced correct results.

### Performance

As expected, the processor is not particularly fast. The game allows for up to 60 changes in the control network state per second. Some instructions require several changes. The processor operates stably at a frequency of 10Hz. Additionally, we only have 15 instructions. Merge sort of 16 numbers took over 20 minutes.

### Summary

In summary, I managed to build a working processor in Factorio and write a compiler for the programming language I created. This was a challenging project that required me to think creatively and work diligently. Although the project may not have any practical application, it taught me many valuable skills, such as problem-solving and critical thinking.

One of the most rewarding aspects of the project was seeing the merge sort algorithm successfully work. This algorithm required dynamic memory allocation and recursion, which were both challenging concepts to implement.

Overall, this project was a great learning experience that allowed me to develop new skills and push myself to new limits. I am proud of what I was able to achieve and look forward to tackling more challenging projects in the future.

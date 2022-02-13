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
    | 'output' '[' NUM ']' '=' expr ';'             # OutputStatement
    | expr ';'                                      # ExpressionStatement
    | '{' stmt+ '}'                                 # MultipleStatements
    | 'if' '(' expr ')' stmt 'else' stmt            # IfElseStatement
    | 'if' '(' expr ')' stmt                        # IfStatement
    | 'while' '(' expr ')' stmt                     # WhileStatement
    | 'var' IDENTIFIER ';'                          # VarDeclare
    | 'var' IDENTIFIER '=' expr ';'                 # VarDeclareDefine
;

expr:
      '(' expr ')'                                              # Parenthesis
    | <assoc=right> '-' expr                                    # UnaryMinus
    | <assoc=right> 'not' expr                                  # UnaryNot
    | expr op=('*' | '/' | '%') expr                            # MultiplicationPrecedenceOperation
    | expr op=('+' | '-') expr                                  # AdditionPrecedenceOperation
    | expr op=('>>' | '<<') expr                                # ShiftOperation
    | expr op=('==' | '!=' | '>' | '<' | '>=' | '<=') expr      # Comparison
    | expr 'and' expr                                           # AndOperator
    | expr 'or' expr                                            # OrOperator
    | 'input' '[' NUM ']'                                       # Input
    | IDENTIFIER '(' args ')'                                   # FunctionCall
    | IDENTIFIER                                                # Variable
    | NUM                                                       # Number
;

IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
NUM: '0' | '-'?[1-9][0-9]*(.[0-9]+)?;
WS: [ \t\n\r]+ -> skip;

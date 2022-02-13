package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;

public class BinaryOperation extends Expression {
    private final Expression left;
    private final Expression right;
    private final CompilationState.Operator operator;

    public BinaryOperation(Expression left, CompilationState.Operator operator, Expression right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public void compile(CompilationState state) {
        left.compile(state);
        right.compile(state);
        state.popReg(2);
        state.popReg(1);
        state.math(operator);
        state.pushReg(1);
    }
}

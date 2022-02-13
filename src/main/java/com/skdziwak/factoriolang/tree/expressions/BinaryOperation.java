package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;

public class BinaryOperation extends Expression {
    private final Expression left;
    private final Expression right;
    private final MathOperator mathOperator;

    public BinaryOperation(Expression left, MathOperator mathOperator, Expression right) {
        this.left = left;
        this.right = right;
        this.mathOperator = mathOperator;
    }

    @Override
    public void compile(CompilationState state) {
        left.compile(state);
        right.compile(state);
        state.popReg(2);
        state.popReg(1);
        state.math(mathOperator);
        state.pushReg(1);
    }
}

package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.constants.MathOperator;
import com.skdziwak.factoriolang.tree.Expression;

public class MallocExpression extends Expression {
    private final Expression sizeExpression;

    public MallocExpression(Expression sizeExpression) {
        this.sizeExpression = sizeExpression;
    }

    @Override
    public void compile(CompilationState state) {
        sizeExpression.compile(state);
        state.popReg(2);
        state.copyRAMtoRegister(1, 1);
        state.pushReg(1);
        state.math(MathOperator.ADD);
        state.copyRegisterToRAM(1, 1);
    }
}

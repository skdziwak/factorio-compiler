package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;

public class ConstantExpression extends Expression {
    private final Integer constant;

    public ConstantExpression(Integer constant) {
        this.constant = constant;
    }

    @Override
    public void compile(CompilationState state) {
        state.setRegister(1, constant);
        state.pushReg(1);
    }
}

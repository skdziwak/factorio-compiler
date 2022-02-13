package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;

public class InputExpression extends Expression {
    private final Integer inputId;

    public InputExpression(Integer inputId) {
        this.inputId = inputId;
    }


    @Override
    public void compile(CompilationState state) {
        state.copyInput(this.inputId, 1);
        state.pushReg(1);
    }
}

package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.tree.Expression;

public class InputArrayExpression extends Expression {
    private final int start, end;

    public InputArrayExpression(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void compile(CompilationState state) {
        int size = end - start + 1;
        int address = state.allocateArray(size);
        for (int i = 0 ; i < size ; i++) {
            state.copyInput(start + i, 1);
            state.copyRegisterToRAM(1, address + i);
        }
        state.setRegister(1, address);
        state.pushReg(1);
    }
}

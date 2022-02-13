package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class DeclarationStatement extends Statement {
    private final String variable;
    private final Expression initialValue;

    public DeclarationStatement(String variable) {
        this.variable = variable;
        this.initialValue = null;
    }

    public DeclarationStatement(String variable, Expression initialValue) {
        this.variable = variable;
        this.initialValue = initialValue;
    }

    @Override
    public void compile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();
        if (functionContext == null) {
            int addr = state.declareVariable(variable);
            if (initialValue != null) {
                initialValue.compile(state);
                state.popReg(1);
                state.copyRegisterToRAM(1, addr);
            }
        } else {
            int addr = functionContext.declareVariable(variable);
            if (initialValue != null) {
                initialValue.compile(state);
                state.popReg(1);
                state.copyRegister(1, HardwareConstants.FUNCTION_ARG_REGISTERS[addr]);
            }
        }
    }
}

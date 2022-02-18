package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.compilation.interfaces.PreCompilable;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class DeclarationStatement extends Statement implements PreCompilable {
    private final String variable;
    private final Expression initialValue;
    private int address;

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
        if (initialValue != null) {
            initialValue.compile(state);
            state.popReg(1);
            if (functionContext == null) {
                state.copyRegisterToRAM(1, address);
            } else {
                state.copyRegister(1, HardwareConstants.FUNCTION_ARG_REGISTERS[address]);
            }
        }
    }

    @Override
    public void preCompile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();
        if (functionContext == null) {
            this.address = state.declareVariable(variable);
        } else {
            this.address = functionContext.declareVariable(variable);
        }
    }
}

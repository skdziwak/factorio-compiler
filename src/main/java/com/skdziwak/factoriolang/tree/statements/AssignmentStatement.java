package com.skdziwak.factoriolang.tree.statements;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.tree.Expression;
import com.skdziwak.factoriolang.tree.Statement;

public class AssignmentStatement extends Statement {
    private final String variable;
    private final Expression value;
    public AssignmentStatement(String variable, Expression value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void compile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();
        int address;
        if (functionContext == null || !functionContext.containsVariable(variable)) {
            address = state.getVariableAddress(variable);
        } else {
            address = functionContext.getVariableAddress(variable);
        }
        value.compile(state);
        state.popReg(1);
        state.copyRegisterToRAM(1, address);
    }
}

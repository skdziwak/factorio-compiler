package com.skdziwak.factoriolang.tree.expressions;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.FunctionContext;
import com.skdziwak.factoriolang.tree.Expression;

public class VariableExpression extends Expression {
    private final String variable;

    public VariableExpression(String variable) {
        this.variable = variable;
    }

    @Override
    public void compile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();
        if (functionContext == null || !functionContext.containsVariable(variable)) {
            state.copyRAMtoRegister(state.getVariableAddress(variable), 1);
            state.pushReg(1);
        } else {
            state.copyRegister(
                    HardwareConstants.FUNCTION_ARG_REGISTERS[functionContext.indexOfVariable(variable)],
                    1
            );
            state.pushReg(1);
        }
    }
}

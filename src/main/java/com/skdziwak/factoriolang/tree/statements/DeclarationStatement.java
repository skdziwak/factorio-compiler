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
        if (initialValue != null) {
            new AssignmentStatement(variable, initialValue).compile(state);
        }
    }

    @Override
    public void preCompile(CompilationState state) {
        FunctionContext functionContext = state.getFunctionContext();
        if (functionContext == null) {
            this.address = state.declareVariable(variable);
        } else {
            this.address = functionContext.declareVariable(state, variable);
        }
    }
}
